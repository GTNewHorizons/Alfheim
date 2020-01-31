package alfheim.common.potion

import alfheim.common.core.handler.AlfheimConfigHandler
import net.minecraft.entity.*
import net.minecraft.entity.ai.attributes.*
import java.util.*

class PotionBerserk: PotionAlfheim(AlfheimConfigHandler.potionIDBerserk, "berserk", false, 0xAA1111) {
	
	val uuid = UUID.fromString("23593E88-FFE3-1707-DE94-6BCD1756B35D")!!
	
	override fun applyAttributesModifiersToEntity(target: EntityLivingBase?, map: BaseAttributeMap, mod: Int) {
		super.applyAttributesModifiersToEntity(target, map, mod)
		val m = AttributeModifier(uuid, "berserk", -0.2, 2)
		target!!.getEntityAttribute(SharedMonsterAttributes.maxHealth).removeModifier(m)
		target.getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(m)
		target.health = target.health.coerceAtMost(target.maxHealth)
	}
	
	override fun removeAttributesModifiersFromEntity(target: EntityLivingBase?, map: BaseAttributeMap, mod: Int) {
		super.removeAttributesModifiersFromEntity(target, map, mod)
		val m = target!!.getEntityAttribute(SharedMonsterAttributes.maxHealth).getModifier(uuid)
		if (m != null) target.getEntityAttribute(SharedMonsterAttributes.maxHealth).removeModifier(m)
	}
}
