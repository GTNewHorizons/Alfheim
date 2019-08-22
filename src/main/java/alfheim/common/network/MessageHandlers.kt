package alfheim.common.network

import alfheim.AlfheimCore
import alfheim.api.AlfheimAPI
import alfheim.api.spell.SpellBase
import alfheim.client.core.handler.KeyBindingHandlerClient.KeyBindingIDs.*
import alfheim.client.core.handler.PacketHandlerClient
import alfheim.common.core.handler.*
import alfheim.common.entity.EntityLolicorn
import cpw.mods.fml.common.network.simpleimpl.*
import net.minecraft.entity.EntityLivingBase
import net.minecraft.potion.*

class Message0dHandler: IMessageHandler<Message0d, IMessage> {
		
	override fun onMessage(packet: Message0d, message: MessageContext): IMessage? {
		PacketHandler.handle(packet, message)
		return null
	}
}

class Message1dHandler: IMessageHandler<Message1d, IMessage> {

	override fun onMessage(packet: Message1d, message: MessageContext): IMessage? {
		PacketHandlerClient.handle(packet)
		return null
	}
}

class Message2dHandler: IMessageHandler<Message2d, IMessage> {

	override fun onMessage(packet: Message2d, message: MessageContext): IMessage? {
		PacketHandlerClient.handle(packet)
		return null
	}
}

class Message3dHandler: IMessageHandler<Message3d, IMessage> {

	override fun onMessage(packet: Message3d, message: MessageContext): IMessage? {
		PacketHandlerClient.handle(packet)
		return null
	}
}

class MessageEffectHandler: IMessageHandler<MessageEffect, IMessage> {
	override fun onMessage(packet: MessageEffect, message: MessageContext): IMessage? {
		val e = message.clientHandler.clientWorldController.getEntityByID(packet.entity)
		if (e is EntityLivingBase) {
			val pe = e.getActivePotionEffect(Potion.potionTypes[packet.id])
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

class MessageHotSpellCHandler: IMessageHandler<MessageHotSpellC, IMessage> {

	override fun onMessage(packet: MessageHotSpellC, message: MessageContext): IMessage? {
		PacketHandlerClient.handle(packet)
		return null
	}
}

class MessageHotSpellSHandler: IMessageHandler<MessageHotSpellS, IMessage> {

	override fun onMessage(packet: MessageHotSpellS, message: MessageContext): IMessage? {
		CardinalSystem.HotSpellsSystem.setHotSpellID(message.serverHandler.playerEntity, packet.slot, packet.id)
		return null
	}
}

class MessageKeyBindHandler: IMessageHandler<MessageKeyBind, IMessage> {

	override fun onMessage(packet: MessageKeyBind, message: MessageContext): IMessage? {
		val player = message.serverHandler.playerEntity
		
		when (values()[packet.action]) {
			CORN   -> EntityLolicorn.call(player)
			
			ATTACK -> KeyBindingHandler.atack(player)
			
			CAST   -> {
				val ids = if (packet.state) CardinalSystem.HotSpellsSystem.getHotSpellID(player, packet.ticks) else packet.ticks
				val seg = CardinalSystem.forPlayer(player)
				val spell = AlfheimAPI.getSpellByIDs(ids shr 28 and 0xF, ids and 0xFFFFFFF)
				if (spell == null)
					AlfheimCore.network.sendTo(Message2d(Message2d.m2d.COOLDOWN, ids.toDouble(), (-SpellBase.SpellCastResult.DESYNC.ordinal).toDouble()), player)
				else {
					seg.ids = ids
					seg.init = spell.getCastTime()
					seg.castableSpell = spell
				}
			}
			
			UNCAST -> {
				run {
					val seg = CardinalSystem.forPlayer(player)
					seg.ids = 0
					seg.init = seg.ids
					seg.castableSpell = null
				}
				KeyBindingHandler.enableFlight(player, packet.state)
			}
			
			FLIGHT -> KeyBindingHandler.enableFlight(player, packet.state)
			
			SEL    -> {
				val e = player.worldObj.getEntityByID(packet.ticks)
				if (e is EntityLivingBase) CardinalSystem.TargetingSystem.setTarget(player, e, packet.state)
			}
		}
		return null
	}
}

class MessageParticlesHandler: IMessageHandler<MessageParticles, IMessage> {

	override fun onMessage(packet: MessageParticles, message: MessageContext): IMessage? {
		PacketHandlerClient.handle(packet)
		return null
	}
}

class MessagePartyHandler: IMessageHandler<MessageParty, IMessage> {

	override fun onMessage(packet: MessageParty, message: MessageContext): IMessage? {
		PacketHandlerClient.handle(packet)
		return null
	}
}

class MessageTileItemHandler: IMessageHandler<MessageTileItem, IMessage> {

	override fun onMessage(packet: MessageTileItem, message: MessageContext): IMessage? {
		PacketHandlerClient.handle(packet)
		return null
	}
}

class MessageTimeStopHandler: IMessageHandler<MessageTimeStop, IMessage> {

	override fun onMessage(packet: MessageTimeStop, message: MessageContext): IMessage? {
		PacketHandlerClient.handle(packet)
		return null
	}
}

class MessagePlayerItemHandler : IMessageHandler<MessagePlayerItem, IMessage> {
	
	override fun onMessage(message: MessagePlayerItem?, ctx: MessageContext?): IMessage? {
		if (ctx != null && message != null && message.item != null && ctx.side.isServer) {
			val player = ctx.serverHandler.playerEntity
			
			val heldItem = player.currentEquippedItem
			
			if (heldItem == null) {
				player.setCurrentItemOrArmor(0, message.item!!.copy())
			} else if (!player.inventory.addItemStackToInventory(message.item!!.copy())) {
				player.dropPlayerItemWithRandomChoice(message.item!!.copy(), false)
			}
		}
		
		return null
	}
}