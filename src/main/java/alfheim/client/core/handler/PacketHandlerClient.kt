package alfheim.client.core.handler

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.extendables.TileItemContainer
import alfheim.api.AlfheimAPI
import alfheim.api.entity.*
import alfheim.api.spell.SpellBase.SpellCastResult
import alfheim.client.core.handler.CardinalSystemClient.PlayerSegmentClient
import alfheim.client.core.handler.CardinalSystemClient.SpellCastingSystemClient
import alfheim.client.core.handler.CardinalSystemClient.TimeStopSystemClient
import alfheim.client.core.proxy.ClientProxy
import alfheim.client.render.world.SpellEffectHandlerClient
import alfheim.client.render.world.SpellEffectHandlerClient.Spells
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.handler.CardinalSystem.KnowledgeSystem.Knowledge
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party.PartyStatus
import alfheim.common.core.helper.flight
import alfheim.common.network.*
import alfheim.common.network.Message1d.m1d
import alfheim.common.network.Message2d.m2d
import alfheim.common.network.Message3d.m3d
import net.minecraft.client.Minecraft

object PacketHandlerClient {
	
	fun handle(packet: MessageParticles) {
		SpellEffectHandlerClient.select(Spells.values()[packet.i], packet.x, packet.y, packet.z, packet.x2, packet.y2, packet.z2)
	}
	
	fun handle(packet: MessageParty) {
		PlayerSegmentClient.party = packet.party
		PlayerSegmentClient.partyIndex = 0
	}
	
	fun handle(packet: MessageHotSpellC) {
		PlayerSegmentClient.hotSpells = packet.ids.clone()
	}
	
	fun handle(packet: MessageTileItem) {
		val world = Minecraft.getMinecraft().theWorld
		val te = world.getTileEntity(packet.x, packet.y, packet.z)
		if (te is TileItemContainer) te.item = packet.s
	}
	
	fun handle(packet: MessageTimeStop) {
		if (packet.party == null) packet.party = Party()
		TimeStopSystemClient.stop(packet.x, packet.y, packet.z, packet.party!!, packet.id)
	}
	
	fun handle(packet: Message1d) {
		when (m1d.values()[packet.type]) {
			m1d.CL_SLOWDOWN      -> AlfheimConfigHandler.slowDownClients = packet.data1 != 0.0
			m1d.DEATH_TIMER      -> AlfheimConfigHandler.deathScreenAddTime = packet.data1.toInt()
			m1d.ELVEN_FLIGHT_MAX -> AlfheimConfigHandler.flightTime = packet.data1.toInt()
			m1d.KNOWLEDGE        -> PlayerSegmentClient.knowledge.add("${Knowledge.values()[packet.data1.toInt()]}")
			m1d.TIME_STOP_REMOVE -> TimeStopSystemClient.remove(packet.data1.toInt())
		}
	}
	
	fun handle(packet: Message2d) {
		when (m2d.values()[packet.type]) {
			m2d.ATTRIBUTE -> {
				when (packet.data1.toInt()) {
					0 -> Minecraft.getMinecraft().thePlayer.raceID = packet.data2.toInt()
					1 -> Minecraft.getMinecraft().thePlayer.flight = packet.data2
				}
			}
			
			m2d.COOLDOWN  -> {
				when (if (packet.data2 > 0) SpellCastResult.OK else SpellCastResult.values()[(-packet.data2).toInt()]) {
					SpellCastResult.DESYNC    -> throw IllegalArgumentException("Client-server spells desynchronization. Not found spell for ${EnumRace[packet.data1.toInt() shr 28 and 0xF]} with id ${packet.data1.toInt() and 0xFFFFFFF}")
					SpellCastResult.NOMANA    -> ASJUtilities.say(Minecraft.getMinecraft().thePlayer, "alfheimmisc.cast.momana")// TODO playSound "not enough mana"
					SpellCastResult.NOTALLOW  -> ASJUtilities.say(Minecraft.getMinecraft().thePlayer, "alfheimmisc.cast.notallow")// TODO playSound "not allowed"
					SpellCastResult.NOTARGET  -> ASJUtilities.say(Minecraft.getMinecraft().thePlayer, "alfheimmisc.cast.notarget")// TODO playSound "no target"
					SpellCastResult.NOTREADY  -> { /*ASJUtilities.say(Minecraft.getMinecraft().thePlayer, "alfheimmisc.cast.notready");*/
					}// TODO playSound "spell not ready"
					SpellCastResult.NOTSEEING -> ASJUtilities.say(Minecraft.getMinecraft().thePlayer, "alfheimmisc.cast.notseeing")// TODO playSound "not seeing"
					SpellCastResult.OBSTRUCT  -> ASJUtilities.say(Minecraft.getMinecraft().thePlayer, "alfheimmisc.cast.obstruct")// TODO playSound "target obstructed"
					SpellCastResult.OK        -> SpellCastingSystemClient.setCoolDown(AlfheimAPI.getSpellByIDs(packet.data1.toInt() shr 28 and 0xF, packet.data1.toInt() and 0xFFFFFFF), packet.data2.toInt())
					SpellCastResult.WRONGTGT  -> ASJUtilities.say(Minecraft.getMinecraft().thePlayer, "alfheimmisc.cast.wrongtgt")// TODO playSound "wrong target"
				}
			}
			
			m2d.UUID      -> PlayerSegmentClient.party?.setUUID(packet.data2.toInt(), packet.data1.toInt())
			
			m2d.MODES     -> {
				if (packet.data1 > 0) ClientProxy.enableESM() else ClientProxy.disableESM()
				if (packet.data2 > 0) ClientProxy.enableMMO() else ClientProxy.disableMMO()
			}
		}
	}
	
	fun handle(packet: Message3d) {
		when (m3d.values()[packet.type]) {
			m3d.KEY_BIND     -> {
			}
			
			m3d.PARTY_STATUS -> {
				when (PartyStatus.values()[packet.data1.toInt()]) {
					PartyStatus.DEAD      -> PlayerSegmentClient.party?.setDead(packet.data2.toInt(), packet.data3.toInt() == -10)
					PartyStatus.MANA      -> PlayerSegmentClient.party?.setMana(packet.data2.toInt(), packet.data3.toInt())
					PartyStatus.HEALTH    -> PlayerSegmentClient.party?.setHealth(packet.data2.toInt(), packet.data3.toFloat())
					PartyStatus.MAXHEALTH -> PlayerSegmentClient.party?.setMaxHealth(packet.data2.toInt(), packet.data3.toFloat())
					PartyStatus.TYPE      -> PlayerSegmentClient.party?.setType(packet.data2.toInt(), packet.data3.toInt())
				}
			}
			
			m3d.WAETHER      -> {
				val info = Minecraft.getMinecraft().theWorld.worldInfo
				info.isRaining = packet.data1.toInt() > 0
				info.rainTime = packet.data2.toInt()
				info.isThundering = packet.data1.toInt() > 1
				info.thunderTime = packet.data3.toInt()
			}
			
			m3d.TOGGLER      -> ClientProxy.toggelModes(packet.data1 > 0, packet.data2.toInt() and 1 > 0, packet.data3.toInt() and 1 > 0, packet.data2.toInt() shr 1 and 1 > 0, packet.data3.toInt() shr 1 and 1 > 0)
		}
	}
}