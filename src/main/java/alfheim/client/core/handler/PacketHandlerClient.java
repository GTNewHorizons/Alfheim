package alfheim.client.core.handler;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.extendables.ItemContainingTileEntity;
import alfheim.api.AlfheimAPI;
import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase.SpellCastResult;
import alfheim.client.core.handler.CardinalSystemClient.SpellCastingSystemClient;
import alfheim.client.core.handler.CardinalSystemClient.TimeStopSystemClient;
import alfheim.client.core.proxy.ClientProxy;
import alfheim.client.render.world.SpellEffectHandlerClient;
import alfheim.client.render.world.SpellEffectHandlerClient.Spells;
import alfheim.common.core.handler.CardinalSystem.HotSpellsSystem;
import alfheim.common.core.handler.CardinalSystem.PartySystem;
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party;
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party.PartyStatus;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.core.util.AlfheimConfig;
import alfheim.common.entity.Flight;
import alfheim.common.network.Message1d;
import alfheim.common.network.MessageHotSpellC;
import alfheim.common.network.MessageParticles;
import alfheim.common.network.MessageParty;
import alfheim.common.network.MessageTileItem;
import alfheim.common.network.MessageTimeStop;
import alfheim.common.network.Message1d.m1d;
import alfheim.common.network.Message2d;
import alfheim.common.network.Message2d.m2d;
import alfheim.common.network.Message3d.m3d;
import alfheim.common.network.Message3d;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;

public class PacketHandlerClient {

	public static void handle(MessageParticles packet) {
		SpellEffectHandlerClient.select(Spells.values()[packet.i], packet.x, packet.y, packet.z, packet.x2, packet.y2, packet.z2);
	}

	public static void handle(MessageParty packet) {
		CardinalSystemClient.segment().party = packet.party;
		CardinalSystemClient.segment.partyIndex = 0;
	}

	public static void handle(MessageHotSpellC packet) {
		CardinalSystemClient.segment().hotSpells = packet.ids.clone();
	}

	public static void handle(MessageTileItem packet) {
		World world = Minecraft.getMinecraft().theWorld;
		TileEntity te = world.getTileEntity(packet.x, packet.y, packet.z);
		if (te != null && te instanceof ItemContainingTileEntity) ((ItemContainingTileEntity) te).setItem(packet.s);
	}

	public static void handle(MessageTimeStop packet) {
		if (packet.party == null) packet.party = new Party();
		TimeStopSystemClient.stop(packet.x, packet.y, packet.z, packet.party, packet.id);
	}

	public static void handle(Message1d packet) {
		switch (m1d.values()[packet.type]) {
			case DEATH_TIMER: AlfheimConfig.deathScreenAddTime = (int) packet.data1; break;
			case KNOWLEDGE: CardinalSystemClient.segment().knowledge[(int) packet.data1] = true; break;
			case TIME_STOP_REMOVE: TimeStopSystemClient.remove((int) packet.data1); break;
		}
	}

	public static void handle(Message2d packet) {
		switch (m2d.values()[packet.type]) {
			case ATTRIBUTE: {
				int d1 = (int) packet.data1;
				switch (d1) {
					case 0: EnumRace.setRaceID(Minecraft.getMinecraft().thePlayer, packet.data2); break;
					case 1: Flight.set(Minecraft.getMinecraft().thePlayer, packet.data2); break;
				}
			} break;
			case COOLDOWN: {
				SpellCastResult result = packet.data2 > 0 ? SpellCastResult.OK : SpellCastResult.values()[(int) -packet.data2];
				switch (result) {
					case DESYNC: throw new IllegalArgumentException("Client-server spells desynchronization. Not found spell for " + EnumRace.getByID(((int) packet.data1 >> 28) & 0xF) + " with id " + ((int) packet.data1 & 0xFFFFFFF));
					case NOMANA: ASJUtilities.say(Minecraft.getMinecraft().thePlayer, "alfheimmisc.cast.momana"); break; // TODO playSound "not enough mana"
					case NOTALLOW: ASJUtilities.say(Minecraft.getMinecraft().thePlayer, "alfheimmisc.cast.notallow"); break; // TODO playSound "not allowed"
					case NOTARGET: ASJUtilities.say(Minecraft.getMinecraft().thePlayer, "alfheimmisc.cast.notarget"); break; // TODO playSound "no target"
					case NOTREADY: /*ASJUtilities.say(Minecraft.getMinecraft().thePlayer, "alfheimmisc.cast.notready");*/ break; // TODO playSound "spell not ready"
					case NOTSEEING: ASJUtilities.say(Minecraft.getMinecraft().thePlayer, "alfheimmisc.cast.notseenig"); break; // TODO playSound "not seeing"
					case OBSTRUCT: ASJUtilities.say(Minecraft.getMinecraft().thePlayer, "alfheimmisc.cast.obstruct"); break; // TODO playSound "target obstructed"
					case OK: SpellCastingSystemClient.setCoolDown(AlfheimAPI.getSpellByIDs(((int) packet.data1 >> 28) & 0xF, (int) packet.data1 & 0xFFFFFFF), (int) packet.data2); break;
					case WRONGTGT: ASJUtilities.say(Minecraft.getMinecraft().thePlayer, "alfheimmisc.cast.wrongtgt"); break; // TODO playSound "wrong target"
				}
			} break;
			case UUID: CardinalSystemClient.segment().party.setUUID((int) packet.data2, (int) packet.data1); break;
			
		}
	}

	public static void handle(Message3d packet) {
		switch (m3d.values()[packet.type]) {
			case KEY_BIND:
				break;
			case PARTY_STATUS: {
				switch (PartyStatus.values()[(int) packet.data1]) {
					case DEAD: CardinalSystemClient.segment().party.setDead((int) packet.data2, ((int) packet.data3) == -10); break;
					case MANA: CardinalSystemClient.segment().party.setMana((int) packet.data2, (int) packet.data3); break;
				}
			} break;
			case WAETHER: {
				World world = Minecraft.getMinecraft().theWorld;
				WorldInfo info = Minecraft.getMinecraft().theWorld.getWorldInfo();
				info.setRaining(((int) packet.data1) > 0);
				info.setRainTime((int) packet.data2);
				info.setThundering(((int) packet.data1) > 1);
				info.setThunderTime((int) packet.data3);
			} break;
			case TOGGLER: ClientProxy.toggelModes(packet.data1 > 0, ((int) packet.data2 & 1) > 0, ((int) packet.data3 & 1) > 0, (((int) packet.data2 >> 1) & 1) > 0, (((int) packet.data3 >> 1) & 1) > 0); break;
		}
	}
}