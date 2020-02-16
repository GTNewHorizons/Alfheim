package alfheim.common.network

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.api.AlfheimAPI
import alfheim.api.spell.SpellBase
import alfheim.client.core.handler.KeyBindingHandlerClient.KeyBindingIDs.*
import alfheim.client.core.handler.PacketHandlerClient
import alfheim.client.core.util.mc
import alfheim.common.block.tile.TileRaceSelector
import alfheim.common.core.handler.*
import alfheim.common.core.handler.CardinalSystem.HotSpellsSystem
import alfheim.common.core.handler.CardinalSystem.PartySystem
import alfheim.common.core.handler.CardinalSystem.TargetingSystem
import alfheim.common.core.util.*
import alfheim.common.entity.EntityLolicorn
import cpw.mods.fml.common.network.simpleimpl.*
import net.minecraft.entity.EntityLivingBase
import net.minecraft.potion.*
import net.minecraft.server.MinecraftServer
import vazkii.botania.common.Botania
import vazkii.botania.common.core.helper.Vector3

class Message0dSHandler: IMessageHandler<Message0dS, IMessage?> {
		
	override fun onMessage(packet: Message0dS, message: MessageContext): IMessage? {
		PacketHandler.handle(packet, message)
		return null
	}
}

class Message0dCHandler: IMessageHandler<Message0dC, IMessage?> {
	
	override fun onMessage(packet: Message0dC, message: MessageContext): IMessage? {
		PacketHandlerClient.handle(packet)
		return null
	}
}

class Message1dHandler: IMessageHandler<Message1d, IMessage?> {
	
	override fun onMessage(packet: Message1d, message: MessageContext): IMessage? {
		PacketHandlerClient.handle(packet)
		return null
	}
}

class Message1lHandler: IMessageHandler<Message1l, IMessage?> {
	
	override fun onMessage(packet: Message1l, message: MessageContext): IMessage? {
		PacketHandlerClient.handle(packet)
		return null
	}
}

class Message2dHandler: IMessageHandler<Message2d, IMessage?> {

	override fun onMessage(packet: Message2d, message: MessageContext): IMessage? {
		PacketHandlerClient.handle(packet)
		return null
	}
}

class Message3dHandler: IMessageHandler<Message3d, IMessage?> {

	override fun onMessage(packet: Message3d, message: MessageContext): IMessage? {
		PacketHandlerClient.handle(packet)
		return null
	}
}

class MessageNIHandler:  IMessageHandler<MessageNI, IMessage?> {
	
	override fun onMessage(packet: MessageNI, ctx: MessageContext): IMessage? {
		PacketHandlerClient.handle(packet)
		return null
	}
}

// ----

class MessageEffectHandler: IMessageHandler<MessageEffect, IMessage?> {
	override fun onMessage(packet: MessageEffect, message: MessageContext): IMessage? {
		val e = message.clientHandler.clientWorldController.getEntityByID(packet.entity)
		if (e is EntityLivingBase) {
			val pe = e.getActivePotionEffect(packet.id)
			when (packet.state) {
				1	/* add	*/	-> {
					if (pe == null) {
						e.addPotionEffect(PotionEffect(packet.id, packet.dur, packet.amp, true))
						Potion.potionTypes[packet.id].applyAttributesModifiersToEntity(e, e.getAttributeMap(), packet.amp)
					} else {
						if (packet.readd) Potion.potionTypes[packet.id].removeAttributesModifiersFromEntity(e, e.getAttributeMap(), packet.amp)
						pe.amplifier = packet.amp
						pe.duration = packet.dur
						pe.isAmbient = true
						if (packet.readd) Potion.potionTypes[packet.id].applyAttributesModifiersToEntity(e, e.getAttributeMap(), packet.amp)
					}
				}
				
				0	/* upd	*/	-> {
					if (pe == null) {
						e.addPotionEffect(PotionEffect(packet.id, packet.dur, packet.amp, true))
						Potion.potionTypes[packet.id].applyAttributesModifiersToEntity(e, e.getAttributeMap(), packet.amp)
					} else {
						if (packet.readd) Potion.potionTypes[packet.id].removeAttributesModifiersFromEntity(e, e.getAttributeMap(), packet.amp)
						pe.amplifier = packet.amp
						pe.duration = packet.dur
						pe.isAmbient = true
						if (packet.readd) Potion.potionTypes[packet.id].applyAttributesModifiersToEntity(e, e.getAttributeMap(), packet.amp)
					}
				}
				
				-1	/* rem	*/	-> {
					if (pe != null) {
						e.removePotionEffect(packet.id)
						Potion.potionTypes[packet.id].removeAttributesModifiersFromEntity(e, e.getAttributeMap(), packet.amp)
					}
				}
			}
		}
		return null
	}
}

class MessageEffectLightningHandler: IMessageHandler<MessageEffectLightning, IMessage?> {
	
	override fun onMessage(packet: MessageEffectLightning, message: MessageContext): IMessage? {
		Botania.proxy.lightningFX(mc.theWorld, Vector3(packet.x1, packet.y1, packet.z1), Vector3(packet.x2, packet.y2, packet.z2), packet.ticksPerMeter, packet.colorOuter, packet.colorInner)
		return null
	}
}

class MessageHotSpellCHandler: IMessageHandler<MessageHotSpellC, IMessage?> {

	override fun onMessage(packet: MessageHotSpellC, message: MessageContext): IMessage? {
		PacketHandlerClient.handle(packet)
		return null
	}
}

class MessageHotSpellSHandler: IMessageHandler<MessageHotSpellS, IMessage?> {

	override fun onMessage(packet: MessageHotSpellS, message: MessageContext): IMessage? {
		HotSpellsSystem.setHotSpellID(message.serverHandler.playerEntity, packet.slot, packet.id)
		return null
	}
}

class MessageKeyBindHandler: IMessageHandler<MessageKeyBindS, IMessage?> {

	override fun onMessage(packet: MessageKeyBindS, message: MessageContext): IMessage? {
		val player = message.serverHandler.playerEntity
		
		when (values()[packet.action]) {
			ATTACK  -> KeyBindingHandler.atack(player)
			
			CORN    -> EntityLolicorn.call(player)
			
			FLIGHT  -> KeyBindingHandler.enableFlight(player, packet.state)
			
			ESMABIL -> CardinalSystem.forPlayer(player).toggleESMAbility()
			
			CAST    -> {
				val ids = if (packet.state) HotSpellsSystem.getHotSpellID(player, packet.data) else packet.data
				val seg = CardinalSystem.forPlayer(player)
				val spell = AlfheimAPI.getSpellByIDs(ids shr 28 and 0xF, ids and 0xFFFFFFF)
				if (spell == null)
					AlfheimCore.network.sendTo(Message2d(Message2d.m2d.COOLDOWN, ids.D, (-SpellBase.SpellCastResult.DESYNC.ordinal).D), player)
				else {
					seg.ids = ids
					seg.init = spell.getCastTime()
					seg.castableSpell = spell
				}
			}
			
			UNCAST  -> {
				val seg = CardinalSystem.forPlayer(player)
				seg.ids = 0
				seg.init = seg.ids
				seg.castableSpell = null
			}
			
			SEL     -> {
				if (packet.state) {
					val mr = PartySystem.getParty(player)[packet.data]
					if (mr is EntityLivingBase) {
						TargetingSystem.setTarget(player, mr, packet.state, packet.data)
					}
				} else {
					if (packet.data != -1) {
						val e = player.worldObj.getEntityByID(packet.data)
						if (e is EntityLivingBase) {
							TargetingSystem.setTarget(player, e, packet.state)
						}
					} else {
						TargetingSystem.setTarget(player, null, false)
					}
				}
			}
		}
		return null
	}
}

class MessagePartyHandler: IMessageHandler<MessageParty, IMessage?> {
	
	override fun onMessage(packet: MessageParty, message: MessageContext): IMessage? {
		PacketHandlerClient.handle(packet)
		return null
	}
}

class MessagePlayerItemHandler: IMessageHandler<MessagePlayerItemS, IMessage?> {
	
	override fun onMessage(packet: MessagePlayerItemS?, ctx: MessageContext?): IMessage? {
		if (ctx != null && packet != null && packet.item != null && ctx.side.isServer) {
			val player = ctx.serverHandler.playerEntity
			
			val heldItem = player.currentEquippedItem
			
			if (heldItem == null) {
				player.setCurrentItemOrArmor(0, packet.item!!.copy())
			} else if (!player.inventory.addItemStackToInventory(packet.item!!.copy())) {
				player.dropPlayerItemWithRandomChoice(packet.item!!.copy(), false)
			}
		}
		
		return null
	}
}

class MessageSkinInfoHandler: IMessageHandler<MessageSkinInfo, IMessage?> {
	
	override fun onMessage(packet: MessageSkinInfo, message: MessageContext): IMessage? {
		PacketHandlerClient.handle(packet)
		
		return null
	}
}

class MessageSpellParamsHandler: IMessageHandler<MessageSpellParams, IMessage?> {
	
	override fun onMessage(packet: MessageSpellParams, message: MessageContext): IMessage? {
		val spell = AlfheimAPI.getSpellInstance(packet.name) ?: return null
		spell.damage = packet.damage
		spell.duration = packet.duration
		spell.efficiency = packet.efficiency
		spell.radius = packet.radius
		
		return null
	}
}

class MessageRaceSelectionHandler: IMessageHandler<MessageRaceSelection, IMessage?> {
	
	override fun onMessage(packet: MessageRaceSelection, message: MessageContext): IMessage? {
		val world = MinecraftServer.getServer().worldServerForDimension(packet.dim) ?: return null
		val tile = world.getTileEntity(packet.x, packet.y, packet.z) as? TileRaceSelector ?: return null
		
		if (packet.doMeta) world.setBlockMetadataWithNotify(packet.x, packet.y, packet.z, packet.meta, 3)
		
		tile.activeRotation = packet.arot
		tile.rotation = packet.rot
		tile.custom = packet.custom
		tile.female = packet.female
		tile.timer = packet.timer
		
		if (packet.give) tile.giveRaceAndReset(message.serverHandler.playerEntity)
		
		ASJUtilities.dispatchTEToNearbyPlayers(tile)
		
		return null
	}
}

class MessageTileItemHandler: IMessageHandler<MessageTileItem, IMessage?> {

	override fun onMessage(packet: MessageTileItem, message: MessageContext): IMessage? {
		PacketHandlerClient.handle(packet)
		return null
	}
}

class MessageTimeStopHandler: IMessageHandler<MessageTimeStop, IMessage?> {

	override fun onMessage(packet: MessageTimeStop, message: MessageContext): IMessage? {
		PacketHandlerClient.handle(packet)
		return null
	}
}

class MessageVisualEffectHandler: IMessageHandler<MessageVisualEffect, IMessage?> {
	
	override fun onMessage(packet: MessageVisualEffect, message: MessageContext): IMessage? {
		PacketHandlerClient.handle(packet)
		return null
	}
}