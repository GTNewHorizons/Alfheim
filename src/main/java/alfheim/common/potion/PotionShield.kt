package alfheim.common.potion

import alfheim.common.core.handler.AlfheimConfigHandler
import net.minecraft.entity.EntityLivingBase

class PotionShield: PotionAlfheim(AlfheimConfigHandler.potionIDShield, "shield", false, 0x6666FF) {
	
	override fun isReady(tick: Int, mod: Int) = true
	
	@Suppress("UNCHECKED_CAST")
	override fun performEffect(entity: EntityLivingBase, mod: Int) {
//		val (x, y, z) = Vector3.fromEntity(entity)
//		val yOff = y + entity.height / 2
//
//		val aabb = entity.boundingBox(2.5)
//
//		val thrO = entity.worldObj.getEntitiesWithinAABB(EntityThrowable::class.java, aabb) as List<EntityThrowable>
//		val thrC = entity.worldObj.getEntitiesWithinAABB(EntityThrowableCopy::class.java, aabb) as List<EntityThrowableCopy>
//
//		SpellForceShield.formShield(entity, x, yOff, z, 0f) { obb ->
//			thrO.forEach { if (obb.intersectsWith( it.boundingBox.expand(0.01, 0.01, 0.01))) it.setDead() }
//			thrC.forEach { if (obb.intersectsWith( it.boundingBox.expand(0.01, 0.01, 0.01))) it.setDead() }
//		}
	}
}
