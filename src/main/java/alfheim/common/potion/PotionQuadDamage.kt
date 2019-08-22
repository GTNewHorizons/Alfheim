package alfheim.common.potion

import alfheim.AlfheimCore
import alfheim.client.render.world.SpellEffectHandlerClient.Spells
import alfheim.common.core.handler.SpellEffectHandler
import alfheim.common.core.handler.AlfheimConfigHandler
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.ai.attributes.BaseAttributeMap

class PotionQuadDamage: PotionAlfheim(AlfheimConfigHandler.potionIDQuadDamage, "quadDamage", false, 0x22FFFF) {
	
	override fun applyAttributesModifiersToEntity(target: EntityLivingBase?, attributes: BaseAttributeMap, ampl: Int) {
		super.applyAttributesModifiersToEntity(target, attributes, ampl)
		if (AlfheimCore.enableMMO) SpellEffectHandler.sendPacket(Spells.QUAD, target!!)
	}
}
