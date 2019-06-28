package alfheim.common.network

import alexsocol.asjlib.network.ASJPacket
import cpw.mods.fml.common.network.simpleimpl.*
import net.minecraft.entity.EntityLivingBase
import net.minecraft.potion.*

class MessageEffect @JvmOverloads constructor(val entity: Int, val id: Int, val dur: Int, val amp: Int, val upd: Boolean = true): ASJPacket() {
	
	class Handler: IMessageHandler<MessageEffect, IMessage> {
		override fun onMessage(packet: MessageEffect, message: MessageContext): IMessage? {
			val e = message.clientHandler.clientWorldController.getEntityByID(packet.entity)
			if (e is EntityLivingBase) {
				val pe = e.getActivePotionEffect(Potion.potionTypes[packet.id])
				if (packet.dur <= 0) {
					if (pe != null) {
						e.removePotionEffect(packet.id)
						Potion.potionTypes[packet.id].removeAttributesModifiersFromEntity(e, e.getAttributeMap(), packet.amp)
					}
				} else {
					if (pe == null) {
						e.addPotionEffect(PotionEffect(packet.id, packet.dur, packet.amp, true))
						Potion.potionTypes[packet.id].applyAttributesModifiersToEntity(e, e.getAttributeMap(), packet.amp)
					} else {
						if (packet.upd) Potion.potionTypes[packet.id].removeAttributesModifiersFromEntity(e, e.getAttributeMap(), packet.amp)
						pe.amplifier = packet.amp
						pe.duration = packet.dur
						pe.isAmbient = true
						if (packet.upd) Potion.potionTypes[packet.id].applyAttributesModifiersToEntity(e, e.getAttributeMap(), packet.amp)
					}
				}
			}
			return null
		}
	}
}
