package alfheim.common.potion

import alfheim.common.core.handler.AlfheimConfigHandler
import net.minecraft.entity.*
import net.minecraft.entity.ai.attributes.*
import java.util.*

class PotionBerserk: PotionAlfheim(AlfheimConfigHandler.potionIDBerserk, "berserk", false, 0xAA1111) {
	
	override fun applyAttributesModifiersToEntity(target: EntityLivingBase?, map: BaseAttributeMap, mod: Int) {
		super.applyAttributesModifiersToEntity(target, map, mod)
		val m = AttributeModifier(UUID.fromString("23593E88-FFE3-1707-DE94-6BCD1756B35D"), "berserk", -0.2, 2)
		target!!.getEntityAttribute(SharedMonsterAttributes.maxHealth).removeModifier(m)
		target.getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(m)
		target.health = Math.min(target.health, target.maxHealth)
	}
	
	override fun removeAttributesModifiersFromEntity(target: EntityLivingBase?, map: BaseAttributeMap, mod: Int) {
		super.removeAttributesModifiersFromEntity(target, map, mod)
		val m = target!!.getEntityAttribute(SharedMonsterAttributes.maxHealth).getModifier(UUID.fromString("23593E88-FFE3-1707-DE94-6BCD1756B35D"))
		if (m != null) target.getEntityAttribute(SharedMonsterAttributes.maxHealth).removeModifier(m)
	}
}
