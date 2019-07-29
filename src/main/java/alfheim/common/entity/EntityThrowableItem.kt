package alfheim.common.entity

import net.minecraft.block.material.Material
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntityThrowable
import net.minecraft.init.Blocks
import net.minecraft.util.*
import net.minecraft.world.World
import kotlin.math.roundToLong

class EntityThrowableItem: EntityThrowable {
	
	constructor(world: World) : super(world)
	
	constructor(player: EntityPlayer) : super(player.worldObj, player)
	
	override fun onImpact(movingObject: MovingObjectPosition?) {
		if (!worldObj.isRemote && movingObject != null) {
			val axisalignedbb = boundingBox.expand(8.0, 2.0, 8.0)
			val list1 = worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, axisalignedbb)
			
			if (list1 != null && list1.isNotEmpty()) {
				val iterator = list1.iterator()
				
				while (iterator.hasNext()) {
					val entitylivingbase = iterator.next() as EntityLivingBase
					val d0 = getDistanceSqToEntity(entitylivingbase)
					
					if (d0 < 16.0) {
						entitylivingbase.setFire(10)
					}
				}
			}
			
			var i = MathHelper.floor_double(posX)
			var j = MathHelper.floor_double(posY)
			var k = MathHelper.floor_double(posZ)
			
			if (worldObj.getBlock(i, j, k).material === Material.air && Blocks.fire.canPlaceBlockAt(worldObj, i, j, k)) {
				worldObj.setBlock(i, j, k, Blocks.fire)
			}
			
			for (n in 0..36) {
				i = MathHelper.floor_double(posX) + rand.nextInt(6) - 1
				j = MathHelper.floor_double(posY) + rand.nextInt(6) - 1
				k = MathHelper.floor_double(posZ) + rand.nextInt(6) - 1
				
				if (worldObj.getBlock(i, j, k).material === Material.air && Blocks.fire.canPlaceBlockAt(worldObj, i, j, k)) {
					worldObj.setBlock(i, j, k, Blocks.fire)
				}
			}
		}
		
		worldObj.playAuxSFX(2002, posX.roundToLong().toInt(), posY.roundToLong().toInt(), posZ.roundToLong().toInt(), 16451) // fire resistance meta
		setDead()
	}
}
