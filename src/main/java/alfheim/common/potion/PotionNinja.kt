package alfheim.common.potion

import alfheim.common.core.util.AlfheimConfig
import net.minecraft.entity.*
import net.minecraft.entity.ai.attributes.*
import java.util.*

class PotionNinja: PotionAlfheim(AlfheimConfig.potionIDNinja, "ninja", false, 0xCCCCCC) {
	
	override fun applyAttributesModifiersToEntity(target: EntityLivingBase?, map: BaseAttributeMap, mod: Int) {
		super.applyAttributesModifiersToEntity(target, map, mod)
		val m = AttributeModifier(UUID.fromString("3899DCBA-B79F-92AF-727C-2190BBD8ABC5"), "ninja", 0.2, 2)
		target!!.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(m)
		target.getEntityAttribute(SharedMonsterAttributes.movementSpeed).applyModifier(m)
	}
	
	override fun removeAttributesModifiersFromEntity(target: EntityLivingBase?, map: BaseAttributeMap, mod: Int) {
		super.removeAttributesModifiersFromEntity(target, map, mod)
		val m = target!!.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getModifier(UUID.fromString("3899DCBA-B79F-92AF-727C-2190BBD8ABC5"))
		if (m != null) target.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(m)
	}
}
