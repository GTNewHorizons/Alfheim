package alfheim.client.core.handler

import alexsocol.asjlib.*
import alexsocol.asjlib.extendables.block.TileItemContainer
import alfheim.api.AlfheimAPI
import alfheim.api.entity.*
import alfheim.api.spell.SpellBase.SpellCastResult
import alfheim.client.core.handler.CardinalSystemClient.PlayerSegmentClient
import alfheim.client.core.handler.CardinalSystemClient.SpellCastingSystemClient
import alfheim.client.core.handler.CardinalSystemClient.TimeStopSystemClient
import alfheim.client.core.proxy.ClientProxy
import alfheim.client.render.world.VisualEffectHandlerClient
import alfheim.client.render.world.VisualEffectHandlerClient.VisualEffects
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.handler.CardinalSystem.KnowledgeSystem.Knowledge
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party.PartyStatus
import alfheim.common.core.handler.ragnarok.RagnarokHandler
import alfheim.common.core.helper.*
import alfheim.common.entity.spell.EntitySpellFireball
import alfheim.common.item.relic.record.GinnungagapHandler
import alfheim.common.network.*
import alfheim.common.network.Message0dC.m0dc
import alfheim.common.network.Message1d.m1d
import alfheim.common.network.Message1l.m1l
import alfheim.common.network.Message2d.m2d
import alfheim.common.network.Message3d.m3d
import alfheim.common.network.MessageNI.mni
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.*

object PacketHandlerClient {
	
	fun handle(packet: MessageVisualEffect) {
		VisualEffectHandlerClient.select(VisualEffects.values()[packet.type], packet.data)
	}
	
	fun handle(packet: MessageParty) {
		PlayerSegmentClient.party = packet.party
		PlayerSegmentClient.partyIndex = 0
	}
	
	fun handle(packet: MessageHotSpellC) {
		PlayerSegmentClient.hotSpells = packet.ids.clone()
	}
	
	fun handle(packet: MessageTileItem) {
		val world = mc.theWorld
		val te = world.getTileEntity(packet.x, packet.y, packet.z)
		if (te is TileItemContainer) te.item = packet.s
	}
	
	fun handle(packet: MessageTimeStop) {
		if (packet.party == null) packet.party = Party()
		TimeStopSystemClient.stop(packet.x, packet.y, packet.z, packet.party!!, packet.id)
	}
	
	fun handle(packet: Message1d) {
		when (m1d.values()[packet.type]) {
			m1d.ESMABIL          -> PlayerSegmentClient.esmAbility = packet.data1 != 0.0
			m1d.DEATH_TIMER      -> AlfheimConfigHandler.deathScreenAddTime = packet.data1.I
			
			m1d.ELVEN_FLIGHT_MAX -> {
				AlfheimConfigHandler.flightTime = packet.data1.I
				ElvenFlightHelper.max = packet.data1
			}
			
			m1d.KNOWLEDGE        -> PlayerSegmentClient.knowledge.add("${Knowledge.values()[packet.data1.I]}")
			m1d.TIME_STOP_REMOVE -> TimeStopSystemClient.remove(packet.data1.I)
			m1d.GINNUNGAGAP      -> GinnungagapHandler.active = packet.data1 != 0.0
			
			m1d.RAGNAROK         -> {
				RagnarokHandler.ragnarok = packet.data1 < 1
				RagnarokHandler.fogFade = packet.data1.F
				
				if (0 < packet.data1 && packet.data1 < 1)
					mc.theWorld.playSound(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, "mob.enderdragon.end", 50f, 0.5f, false)
			}
		}
	}
	
	fun handle(packet: Message1l) {
		when (m1l.values()[packet.type]) {
			m1l.SEED -> mc.theWorld.worldInfo.randomSeed = packet.data1
		}
	}
	
	fun handle(packet: Message2d) {
		when (m2d.values()[packet.type]) {
			m2d.ATTRIBUTE    -> {
				when (packet.data1.I) {
					0 -> mc.thePlayer.raceID = packet.data2.I
					1 -> mc.thePlayer.flight = packet.data2
				}
			}
			
			m2d.COOLDOWN     -> {
				when (if (packet.data2 > 0) SpellCastResult.OK else SpellCastResult.values()[(-packet.data2).I]) {
					SpellCastResult.DESYNC    -> throw IllegalArgumentException("Client-server spells desynchronization. Not found spell for ${EnumRace[packet.data1.I shr 28 and 0xF]} with id ${packet.data1.I and 0xFFFFFFF}")
					SpellCastResult.NOMANA    -> ASJUtilities.say(mc.thePlayer, "alfheimmisc.cast.momana")// TODO playSound "not enough mana"
					SpellCastResult.NOTALLOW  -> ASJUtilities.say(mc.thePlayer, "alfheimmisc.cast.notallow")// TODO playSound "not allowed"
					SpellCastResult.NOTARGET  -> ASJUtilities.say(mc.thePlayer, "alfheimmisc.cast.notarget")// TODO playSound "no target"
					SpellCastResult.NOTREADY  -> { /*ASJUtilities.say(mc.thePlayer, "alfheimmisc.cast.notready");*/
					}// TODO playSound "spell not ready"
					SpellCastResult.NOTSEEING -> ASJUtilities.say(mc.thePlayer, "alfheimmisc.cast.notseeing")// TODO playSound "not seeing"
					SpellCastResult.OBSTRUCT  -> ASJUtilities.say(mc.thePlayer, "alfheimmisc.cast.obstruct")// TODO playSound "target obstructed"
					SpellCastResult.OK        -> SpellCastingSystemClient.setCoolDown(AlfheimAPI.getSpellByIDs(packet.data1.I shr 28 and 0xF, packet.data1.I and 0xFFFFFFF), packet.data2.I)
					SpellCastResult.WRONGTGT  -> ASJUtilities.say(mc.thePlayer, "alfheimmisc.cast.wrongtgt")// TODO playSound "wrong target"
				}
			}
			
			m2d.UUID         -> PlayerSegmentClient.party?.setUUID(packet.data2.I, packet.data1.I)
			
			m2d.MODES        -> {
				if (packet.data1 > 0) ClientProxy.enableESM() else ClientProxy.disableESM()
				if (packet.data2 > 0) ClientProxy.enableMMO() else ClientProxy.disableMMO()
			}
			
			m2d.FIREBALLSYNC -> {
				(mc.theWorld.getEntityByID(packet.data1.I) as? EntitySpellFireball)?.target = mc.theWorld.getEntityByID(packet.data2.I) as? EntityLivingBase
			}
		}
	}
	
	fun handle(packet: Message3d) {
		when (m3d.values()[packet.type]) {
			m3d.KEY_BIND     -> Unit
			
			m3d.PARTY_STATUS -> {
				when (PartyStatus.values()[packet.data1.I]) {
					PartyStatus.DEAD      -> PlayerSegmentClient.party?.setDead(packet.data2.I, packet.data3.I == -10)
					PartyStatus.MANA      -> PlayerSegmentClient.party?.setMana(packet.data2.I, packet.data3.I)
					PartyStatus.HEALTH    -> PlayerSegmentClient.party?.setHealth(packet.data2.I, packet.data3.F)
					PartyStatus.MAXHEALTH -> PlayerSegmentClient.party?.setMaxHealth(packet.data2.I, packet.data3.F)
					PartyStatus.TYPE      -> PlayerSegmentClient.party?.setType(packet.data2.I, packet.data3.I)
				}
			}
			
			m3d.WAETHER      -> {
				mc.theWorld.setRainStrength(if (packet.data1.I > 0) 1f else 0f)
				mc.theWorld.setThunderStrength(if (packet.data1.I > 1) 1f else 0f)
				
				val info = mc.theWorld.worldInfo
				info.isRaining = packet.data1.I > 0
				info.rainTime = packet.data2.I
				info.isThundering = packet.data1.I > 1
				info.thunderTime = packet.data3.I
			}
			
			m3d.TOGGLER      -> ClientProxy.toggelModes(packet.data1 > 0, packet.data2.I and 1 > 0, packet.data3.I and 1 > 0, packet.data2.I shr 1 and 1 > 0, packet.data3.I shr 1 and 1 > 0)
		}
	}
	
	fun handle(packet: MessageNI) {
		when (mni.values()[packet.type]) {
			mni.WINGS_BL -> AlfheimConfigHandler.wingsBlackList = packet.intArray
		}
	}
	
	fun handle(packet: MessageSkinInfo) {
		CardinalSystemClient.playerSkinsData[packet.name] = packet.isFemale to packet.isSkinOn
	}
	
	fun handle(packet: Message0dC) {
		when (m0dc.values()[packet.type]) {
			m0dc.MTSPELL -> {
				val spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID) ?: return
				mc.thePlayer?.addChatMessage(ChatComponentText(StatCollector.translateToLocalFormatted("spell.$spell.mtinfo", *spell.usableParams)))
			}
		}
	}
}