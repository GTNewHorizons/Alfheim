package alfheim.common.item.lens

import alexsocol.asjlib.expand
import alexsocol.asjlib.math.Vector3
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.projectile.EntityThrowable
import net.minecraft.item.ItemStack
import net.minecraft.util.AxisAlignedBB
import vazkii.botania.api.internal.IManaBurst
import vazkii.botania.common.item.lens.Lens

class LensTrack: Lens() {
	
	private val TAG_HOME_ID = "homeID"
	
	override fun updateBurst(burst: IManaBurst, entity: EntityThrowable, stack: ItemStack?) {
		val axis: AxisAlignedBB = AxisAlignedBB.getBoundingBox(entity.posX, entity.posY, entity.posZ, entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).expand(3)
		val entities = entity.worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, axis) as MutableList<EntityLivingBase>
		val homeID = entity.entityData.getInteger(TAG_HOME_ID)
		
		for (living in entities) {
			entity.entityData.setInteger(TAG_HOME_ID, living.entityId)
			break
		}
		
		val result = entity.worldObj.getEntityByID(homeID)
		if (result != null && result.getDistanceToEntity(entity) < 3f && !burst.isFake) {
			val vecEntity = Vector3.fromEntityCenter(result)
			val vecThis = Vector3.fromEntityCenter(entity)
			val vecMotion = vecEntity.sub(vecThis)
			val vecCurrentMotion = Vector3(entity.motionX, entity.motionY, entity.motionZ)
			vecMotion.normalize().mul(vecCurrentMotion.length())
			burst.setMotion(vecMotion.x, vecMotion.y, vecMotion.z)
		}
	}
}
