package alfheim.client.core.handler

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.extendables.ItemContainingTileEntity
import alfheim.api.AlfheimAPI
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase.SpellCastResult
import alfheim.client.core.handler.CardinalSystemClient.*
import alfheim.client.core.proxy.ClientProxy
import alfheim.client.render.world.SpellEffectHandlerClient
import alfheim.client.render.world.SpellEffectHandlerClient.Spells
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party.PartyStatus
import alfheim.common.core.util.AlfheimConfig
import alfheim.common.entity.Flight
import alfheim.common.network.Message1d
import alfheim.common.network.Message1d.m1d
import alfheim.common.network.Message2d
import alfheim.common.network.Message2d.m2d
import alfheim.common.network.Message3d
import alfheim.common.network.Message3d.m3d
import alfheim.common.network.MessageHotSpellC
import alfheim.common.network.MessageParticles
import alfheim.common.network.MessageParty
import alfheim.common.network.MessageTileItem
import alfheim.common.network.MessageTimeStop
import net.minecraft.client.Minecraft
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraft.world.storage.WorldInfo

object PacketHandlerClient {
	
	fun handle(packet: MessageParticles) {
		SpellEffectHandlerClient.select(Spells.values()[packet.i], packet.x, packet.y, packet.z, packet.x2, packet.y2, packet.z2)
	}
	
	fun handle(packet: MessageParty) {
		CardinalSystemClient.segment().party = packet.party
		PlayerSegmentClient.partyIndex = 0
	}
	
	fun handle(packet: MessageHotSpellC) {
		CardinalSystemClient.segment().hotSpells = packet.ids.clone()
	}
	
	fun handle(packet: MessageTileItem) {
		val world = Minecraft.getMinecraft().theWorld
		val te = world.getTileEntity(packet.x, packet.y, packet.z)
		if (te is ItemContainingTileEntity) te.item = packet.s
	}
	
	fun handle(packet: MessageTimeStop) {
		if (packet.party == null) packet.party = Party()
		TimeStopSystemClient.stop(packet.x, packet.y, packet.z, packet.party, packet.id)
	}
	
	fun handle(packet: Message1d) {
		when (m1d.values()[packet.type]) {
			Message1d.m1d.DEATH_TIMER      -> AlfheimConfig.deathScreenAddTime = packet.data1.toInt()
			Message1d.m1d.KNOWLEDGE        -> CardinalSystemClient.segment().knowledge[packet.data1.toInt()] = true
			Message1d.m1d.TIME_STOP_REMOVE -> TimeStopSystemClient.remove(packet.data1.toInt())
			Message1d.m1d.CL_SLOWDOWN      -> AlfheimConfig.slowDownClients = packet.data1 != 0.0
		}
	}
	
	fun handle(packet: Message2d) {
		when (m2d.values()[packet.type]) {
			Message2d.m2d.ATTRIBUTE -> {
				val d1 = packet.data1.toInt()
				when (d1) {
					0 -> EnumRace.setRaceID(Minecraft.getMinecraft().thePlayer, packet.data2)
					1 -> Flight.set(Minecraft.getMinecraft().thePlayer, packet.data2)
				}
			}
			
			Message2d.m2d.COOLDOWN  -> {
				val result = if (packet.data2 > 0) SpellCastResult.OK else SpellCastResult.values()[(-packet.data2).toInt()]
				when (result) {
					SpellBase.SpellCastResult.DESYNC                                                                                          -> throw IllegalArgumentException("Client-server spells desynchronization. Not found spell for " + EnumRace.getByID((packet.data1.toInt() shr 28 and 0xF).toDouble()) + " with id " + (packet.data1.toInt() and 0xFFFFFFF))
					SpellBase.SpellCastResult.NOMANA                                                                                          -> ASJUtilities.say(Minecraft.getMinecraft().thePlayer, "alfheimmisc.cast.momana")
					SpellBase.SpellCastResult.NOTALLOW                                                                                        -> ASJUtilities.say(Minecraft.getMinecraft().thePlayer, "alfheimmisc.cast.notallow")
					SpellBase.SpellCastResult.NOTARGET                                                                                        -> ASJUtilities.say(Minecraft.getMinecraft().thePlayer, "alfheimmisc.cast.notarget")
					
					SpellBase.SpellCastResult.NOTREADY /*ASJUtilities.say(Minecraft.getMinecraft().thePlayer, "alfheimmisc.cast.notready");*/ -> {
					}
					
					SpellBase.SpellCastResult.NOTSEEING                                                                                       -> ASJUtilities.say(Minecraft.getMinecraft().thePlayer, "alfheimmisc.cast.notseeing")
					SpellBase.SpellCastResult.OBSTRUCT                                                                                        -> ASJUtilities.say(Minecraft.getMinecraft().thePlayer, "alfheimmisc.cast.obstruct")
					SpellBase.SpellCastResult.OK                                                                                              -> SpellCastingSystemClient.setCoolDown(AlfheimAPI.getSpellByIDs(packet.data1.toInt() shr 28 and 0xF, packet.data1.toInt() and 0xFFFFFFF), packet.data2.toInt())
					SpellBase.SpellCastResult.WRONGTGT                                                                                        -> ASJUtilities.say(Minecraft.getMinecraft().thePlayer, "alfheimmisc.cast.wrongtgt")
				}// TODO playSound "not enough mana"
				// TODO playSound "not allowed"
				// TODO playSound "no target"
				// TODO playSound "spell not ready"
				// TODO playSound "not seeing"
				// TODO playSound "target obstructed"
				// TODO playSound "wrong target"
			}
			
			Message2d.m2d.UUID      -> CardinalSystemClient.segment().party!!.setUUID(packet.data2.toInt(), packet.data1.toInt())
			
			Message2d.m2d.MODES     -> {
				if (packet.data1 > 0) ClientProxy.enableESM() else ClientProxy.disableESM()
				if (packet.data2 > 0) ClientProxy.enableMMO() else ClientProxy.disableMMO()
			}
		}
	}
	
	fun handle(packet: Message3d) {
		when (m3d.values()[packet.type]) {
			Message3d.m3d.KEY_BIND     -> {
			}
			
			Message3d.m3d.PARTY_STATUS -> {
				when (PartyStatus.values()[packet.data1.toInt()]) {
					CardinalSystem.PartySystem.Party.PartyStatus.DEAD -> CardinalSystemClient.segment().party!!.setDead(packet.data2.toInt(), packet.data3.toInt() == -10)
					CardinalSystem.PartySystem.Party.PartyStatus.MANA -> CardinalSystemClient.segment().party!!.setMana(packet.data2.toInt(), packet.data3.toInt())
				}
			}
			
			Message3d.m3d.WAETHER      -> {
				val world = Minecraft.getMinecraft().theWorld
				val info = Minecraft.getMinecraft().theWorld.worldInfo
				info.isRaining = packet.data1.toInt() > 0
				info.rainTime = packet.data2.toInt()
				info.isThundering = packet.data1.toInt() > 1
				info.thunderTime = packet.data3.toInt()
			}
			
			Message3d.m3d.TOGGLER      -> ClientProxy.toggelModes(packet.data1 > 0, packet.data2.toInt() and 1 > 0, packet.data3.toInt() and 1 > 0, packet.data2.toInt() shr 1 and 1 > 0, packet.data3.toInt() shr 1 and 1 > 0)
		}
	}
}