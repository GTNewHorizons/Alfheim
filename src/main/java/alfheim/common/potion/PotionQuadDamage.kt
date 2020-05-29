package alfheim.common.potion

import alfheim.AlfheimCore
import alfheim.client.render.world.VisualEffectHandlerClient.VisualEffects
import alfheim.common.core.handler.*
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.ai.attributes.BaseAttributeMap

class PotionQuadDamage: PotionAlfheim(AlfheimConfigHandler.potionIDQuadDamage, "quadDamage", false, 0x22FFFF) {
	
	override fun applyAttributesModifiersToEntity(target: EntityLivingBase?, attributes: BaseAttributeMap, ampl: Int) {
		super.applyAttributesModifiersToEntity(target, attributes, ampl)
		if (AlfheimConfigHandler.enableMMO) VisualEffectHandler.sendPacket(VisualEffects.QUAD, target!!)
	}
}
