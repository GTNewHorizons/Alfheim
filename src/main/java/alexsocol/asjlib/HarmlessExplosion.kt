package alexsocol.asjlib

import net.minecraft.enchantment.EnchantmentProtection
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.*
import net.minecraft.world.World

class HarmlessExplosion(private val worldObj: World, val exploder: EntityPlayer?, val explosionX: Double, val explosionY: Double, val explosionZ: Double, var explosionSize: Float) {
	
	init {
		doExplosionA()
	}
	
	/**
	 * Does the first part of the explosion (destroy blocks)
	 */
	fun doExplosionA() {
		val f = explosionSize
		var d5: Double
		var d6: Double
		var d7: Double
		
		worldObj.playSoundEffect(explosionX, explosionY, explosionZ, "random.explode", 4.0f, (1.0f + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2f) * 0.7f)
		worldObj.spawnParticle("hugeexplosion", explosionX, explosionY, explosionZ, 1.0, 0.0, 0.0)
		
		explosionSize *= 2.0f
		val i = MathHelper.floor_double(explosionX - explosionSize.toDouble() - 1.0)
		val j = MathHelper.floor_double(explosionX + explosionSize.toDouble() + 1.0)
		val k = MathHelper.floor_double(explosionY - explosionSize.toDouble() - 1.0)
		val i2 = MathHelper.floor_double(explosionY + explosionSize.toDouble() + 1.0)
		val l = MathHelper.floor_double(explosionZ - explosionSize.toDouble() - 1.0)
		val j2 = MathHelper.floor_double(explosionZ + explosionSize.toDouble() + 1.0)
		
		val list = if (exploder != null) worldObj.getEntitiesWithinAABBExcludingEntity(exploder, AxisAlignedBB.getBoundingBox(i.toDouble(), k.toDouble(), l.toDouble(), j.toDouble(), i2.toDouble(), j2.toDouble())) else worldObj.getEntitiesWithinAABB(EntityLiving::class.java, AxisAlignedBB.getBoundingBox(i.toDouble(), k.toDouble(), l.toDouble(), j.toDouble(), i2.toDouble(), j2.toDouble()))
		val vec3 = Vec3.createVectorHelper(explosionX, explosionY, explosionZ)
		
		for (o in list) {
			val entity = o as Entity
			val d4 = entity.getDistance(explosionX, explosionY, explosionZ) / explosionSize.toDouble()
			
			if (d4 <= 1.0) {
				d5 = entity.posX - explosionX
				d6 = entity.posY + entity.eyeHeight.toDouble() - explosionY
				d7 = entity.posZ - explosionZ
				val d9 = MathHelper.sqrt_double(d5 * d5 + d6 * d6 + d7 * d7).toDouble()
				
				if (d9 != 0.0) {
					d5 /= d9
					d6 /= d9
					d7 /= d9
					val d10 = worldObj.getBlockDensity(vec3, entity.boundingBox).toDouble()
					val d11 = (1.0 - d4) * d10
					entity.attackEntityFrom(DamageSource.setExplosionSource(null), ((d11 * d11 + d11) / 2.0 * 8.0 * explosionSize.toDouble() + 1.0).toInt().toFloat())
					val d8 = EnchantmentProtection.func_92092_a(entity, d11)
					entity.motionX += d5 * d8
					entity.motionY += d6 * d8
					entity.motionZ += d7 * d8
				}
			}
		}
		explosionSize = f
	}
}