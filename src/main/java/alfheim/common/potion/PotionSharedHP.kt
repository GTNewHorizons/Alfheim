package alfheim.common.potion

import alfheim.common.core.handler.AlfheimConfigHandler

class PotionSharedHP: PotionAlfheim(AlfheimConfigHandler.potionIDSharedHP, "sharedHP", false, 0xFF0000) {
	
	/*override fun applyAttributesModifiersToEntity(target: EntityLivingBase?, attributes: BaseAttributeMap, ampl: Int) {
		super.applyAttributesModifiersToEntity(target, attributes, ampl)
		if (!AlfheimCore.enableMMO) return
		val m = AttributeModifier(UUID.fromString("53E7B7F2-19BF-40FE-B204-729CE822D188"), "sharedHP", (ampl - target!!.maxHealth).toDouble(), 0)
		target.getEntityAttribute(SharedMonsterAttributes.maxHealth).removeModifier(m)
		target.getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(m)
	}
	
	override fun removeAttributesModifiersFromEntity(target: EntityLivingBase?, attributes: BaseAttributeMap, ampl: Int) {
		super.removeAttributesModifiersFromEntity(target, attributes, ampl)
		if (!AlfheimCore.enableMMO) return
		val m = target!!.getEntityAttribute(SharedMonsterAttributes.maxHealth).getModifier(UUID.fromString("53E7B7F2-19BF-40FE-B204-729CE822D188"))
		if (m != null) target.getEntityAttribute(SharedMonsterAttributes.maxHealth).removeModifier(m)
		target.health = target.health.coerceAtMost(target.maxHealth)
	}*/
}
