package alfheim.common.potion

import alfheim.common.core.handler.AlfheimConfigHandler
import net.minecraft.entity.*
import net.minecraft.entity.ai.attributes.*
import java.util.*

class PotionTank: PotionAlfheim(AlfheimConfigHandler.potionIDTank, "tank", false, 0xFFDD00) {
	
	val uuid = UUID.fromString("4107C0A4-DB3F-A48D-D9AA-C2701B32B143")!!
	
	override fun applyAttributesModifiersToEntity(target: EntityLivingBase?, map: BaseAttributeMap, mod: Int) {
		super.applyAttributesModifiersToEntity(target, map, mod)
		val m = AttributeModifier(uuid, "tank", -0.2, 2)
		target!!.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(m)
		target.getEntityAttribute(SharedMonsterAttributes.movementSpeed).applyModifier(m)
	}
	
	override fun removeAttributesModifiersFromEntity(target: EntityLivingBase?, map: BaseAttributeMap, mod: Int) {
		super.removeAttributesModifiersFromEntity(target, map, mod)
		val m = target!!.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getModifier(uuid)
		if (m != null) target.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(m)
	}
}
