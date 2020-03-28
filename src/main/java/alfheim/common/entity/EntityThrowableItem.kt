package alfheim.common.entity

import alexsocol.asjlib.*
import alfheim.common.security.InteractionSecurity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntityThrowable
import net.minecraft.init.Blocks
import net.minecraft.util.MovingObjectPosition
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
					val living = iterator.next() as EntityLivingBase
					
					if (getDistanceSqToEntity(living) < 16.0) {
						if (InteractionSecurity.canHurtEntity(thrower ?: continue, living))
							living.setFire(10)
					}
				}
			}
			
			var i = posX.mfloor()
			var j = posY.mfloor()
			var k = posZ.mfloor()
			
			worldObj.playAuxSFX(2002, posX.roundToLong().I, posY.roundToLong().I, posZ.roundToLong().I, 16451) // fire resistance meta
			setDead()
			
			if (InteractionSecurity.canDoSomethingHere(thrower ?: return, i, j, k, worldObj))
			
			if (worldObj.isAirBlock(i, j, k) && Blocks.fire.canPlaceBlockAt(worldObj, i, j, k)) {
				worldObj.setBlock(i, j, k, Blocks.fire)
			}
			
			for (n in 0..36) {
				i = posX.mfloor() + rand.nextInt(6) - 1
				j = posY.mfloor() + rand.nextInt(6) - 1
				k = posZ.mfloor() + rand.nextInt(6) - 1
				
				if (!InteractionSecurity.canDoSomethingHere(thrower ?: continue, i, j, k, worldObj)) continue
				
				if (worldObj.isAirBlock(i, j, k) && Blocks.fire.canPlaceBlockAt(worldObj, i, j, k)) {
					worldObj.setBlock(i, j, k, Blocks.fire)
				}
			}
		}
	}
	
	public override fun func_70183_g() = -10f
	
	public override fun func_70182_d() = 1f
}
