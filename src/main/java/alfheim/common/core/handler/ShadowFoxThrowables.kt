package alfheim.common.core.handler

object ShadowFoxThrowables {
	
	init {
//		ShadowFoxAPI.registerThrowable(ThrowableCollidingItem(
//			"shadowfox_fire_grenade",
//			ItemStack(AlfheimItems.fireGrenade)
//		) { throwable, _ ->
//			if (!throwable.worldObj.isRemote) {
//				val axisalignedbb = throwable.boundingBox.expand(8.0, 2.0, 8.0)
//				val list1 = throwable.worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, axisalignedbb)
//
//				if (list1 != null && list1.isNotEmpty()) {
//					val iterator = list1.iterator()
//
//					while (iterator.hasNext()) {
//						val entitylivingbase = iterator.next() as EntityLivingBase
//						val d0 = throwable.getDistanceSqToEntity(entitylivingbase)
//
//						if (d0 < 16.0) {
//							entitylivingbase.setFire(10)
//						}
//					}
//				}
//
//				var i = MathHelper.floor_double(throwable.posX)
//				var j = MathHelper.floor_double(throwable.posY)
//				var k = MathHelper.floor_double(throwable.posZ)
//
//				if (throwable.worldObj.getBlock(i, j, k).material === Material.air &&
//					Blocks.fire.canPlaceBlockAt(throwable.worldObj, i, j, k)) {
//					throwable.worldObj.setBlock(i, j, k, Blocks.fire)
//				}
//
//				for (n in 0..36) {
//					i = MathHelper.floor_double(throwable.posX) + throwable.worldObj.rand.nextInt(6) - 1
//					j = MathHelper.floor_double(throwable.posY) + throwable.worldObj.rand.nextInt(6) - 1
//					k = MathHelper.floor_double(throwable.posZ) + throwable.worldObj.rand.nextInt(6) - 1
//
//					if (throwable.worldObj.getBlock(i, j, k).material === Material.air &&
//						Blocks.fire.canPlaceBlockAt(throwable.worldObj, i, j, k)) {
//						throwable.worldObj.setBlock(i, j, k, Blocks.fire)
//					}
//				}
//			}
//
//			throwable.worldObj.playAuxSFX(2002, throwable.posX.roundToLong().I, throwable.posY.roundToLong().I, throwable.posZ.roundToLong().I, 0)
//		})
	}
}
