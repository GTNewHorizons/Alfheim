package alfheim.common.potion

import alfheim.common.core.util.AlfheimConfig
import net.minecraft.entity.*
import net.minecraft.entity.ai.attributes.*
import java.util.*

class PotionTank: PotionAlfheim(AlfheimConfig.potionIDTank, "tank", false, 0xFFDD00) {
	
	override fun applyAttributesModifiersToEntity(target: EntityLivingBase?, map: BaseAttributeMap, mod: Int) {
		super.applyAttributesModifiersToEntity(target, map, mod)
		val m = AttributeModifier(UUID.fromString("4107C0A4-DB3F-A48D-D9AA-C2701B32B143"), "tank", -0.2, 2)
		target!!.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(m)
		target.getEntityAttribute(SharedMonsterAttributes.movementSpeed).applyModifier(m)
	}
	
	override fun removeAttributesModifiersFromEntity(target: EntityLivingBase?, map: BaseAttributeMap, mod: Int) {
		super.removeAttributesModifiersFromEntity(target, map, mod)
		val m = target!!.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getModifier(UUID.fromString("4107C0A4-DB3F-A48D-D9AA-C2701B32B143"))
		if (m != null) target.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(m)
	}
}
