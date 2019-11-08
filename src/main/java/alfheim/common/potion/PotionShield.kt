package alfheim.common.potion

import alexsocol.asjlib.math.Vector3
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.util.boundingBox
import alfheim.common.spell.tech.SpellForceShield
import net.minecraft.entity.*
import net.minecraft.entity.projectile.EntityThrowable
import vazkii.botania.common.entity.EntityThrowableCopy

class PotionShield: PotionAlfheim(AlfheimConfigHandler.potionIDShield, "shield", false, 0x6666FF) {
	
	override fun isReady(tick: Int, mod: Int) = true
	
	@Suppress("UNCHECKED_CAST")
	override fun performEffect(entity: EntityLivingBase, mod: Int) {
		// if (mod != -1) return
		
		val (x, y, z) = Vector3.fromEntity(entity)
		val yOff = y + entity.height / 2

		val aabb = entity.boundingBox(2.5)
		
		val proj = entity.worldObj.getEntitiesWithinAABB(IProjectile::class.java, aabb) as List<Entity>
		val thrO = entity.worldObj.getEntitiesWithinAABB(EntityThrowable::class.java, aabb) as List<EntityThrowable>
		val thrC = entity.worldObj.getEntitiesWithinAABB(EntityThrowableCopy::class.java, aabb) as List<EntityThrowableCopy>

		SpellForceShield.formShield(entity, x, yOff, z, 0f) { obb ->
			proj.forEach { if (obb.intersectsWith(it.boundingBox.expand(0.1, 0.1, 0.1))) it.setDead() }
			thrO.forEach { if (obb.intersectsWith(it.boundingBox.expand(0.1, 0.1, 0.1))) it.setDead() }
			thrC.forEach { if (obb.intersectsWith(it.boundingBox.expand(0.1, 0.1, 0.1))) it.setDead() }
		}
	}
}
