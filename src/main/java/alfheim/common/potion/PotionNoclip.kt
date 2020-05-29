package alfheim.common.potion

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.network.MessageEffect
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.ai.attributes.BaseAttributeMap
import net.minecraft.entity.player.EntityPlayer

class PotionNoclip: PotionAlfheim(AlfheimConfigHandler.potionIDNoclip, "noclip", false, 0xAAAAAA) {
	
	override fun isReady(time: Int, amp: Int) = AlfheimConfigHandler.enableMMO
	
	override fun performEffect(target: EntityLivingBase, time: Int) {
		if (AlfheimConfigHandler.enableMMO) target.noClip = true
	}
	
	override fun applyAttributesModifiersToEntity(target: EntityLivingBase, attributes: BaseAttributeMap, amp: Int) {
		if (!AlfheimConfigHandler.enableMMO) return
		if (target is EntityPlayer) {
			target.capabilities.allowFlying = true
			target.capabilities.isFlying = true
			target.onGround = false
			target.sendPlayerAbilities()
		}
		target.noClip = true
	}
	
	override fun removeAttributesModifiersFromEntity(target: EntityLivingBase, attributes: BaseAttributeMap, amp: Int) {
		if (!AlfheimConfigHandler.enableMMO) return
		target.noClip = false
		if (ASJUtilities.isServer) AlfheimCore.network.sendToAll(MessageEffect(target.entityId, this.id, 0, 0))
	}
}
