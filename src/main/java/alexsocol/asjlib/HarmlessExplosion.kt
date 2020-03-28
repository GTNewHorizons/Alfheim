package alexsocol.asjlib

import net.minecraft.enchantment.EnchantmentProtection
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.*
import net.minecraft.world.World

class HarmlessExplosion(worldObj: World, exploder: EntityPlayer?, explosionX: Double, explosionY: Double, explosionZ: Double, explosionSize: Float) {
	
	init {
		var d5: Double
		var d6: Double
		var d7: Double
		
		worldObj.playSoundEffect(explosionX, explosionY, explosionZ, "random.explode", 4f, (1f + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2f) * 0.7f)
		worldObj.spawnParticle("hugeexplosion", explosionX, explosionY, explosionZ, 1.0, 0.0, 0.0)
		
		val list = worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, getBoundingBox(explosionX, explosionY, explosionZ).expand(explosionSize + 1)) as MutableList<EntityLivingBase>
		exploder?.let { list.remove(it) }
		
		val vec3 = Vec3.createVectorHelper(explosionX, explosionY, explosionZ)
		
		for (entity in list) {
			val d4 = entity.getDistance(explosionX, explosionY, explosionZ) / explosionSize
			
			if (d4 <= 1.0) {
				d5 = entity.posX - explosionX
				d6 = entity.posY + entity.eyeHeight - explosionY
				d7 = entity.posZ - explosionZ
				val d9 = MathHelper.sqrt_double(d5 * d5 + d6 * d6 + d7 * d7).D
				
				if (d9 != 0.0) {
					d5 /= d9
					d6 /= d9
					d7 /= d9
					val d10 = worldObj.getBlockDensity(vec3, entity.boundingBox)
					val d11 = (1.0 - d4) * d10
					entity.attackEntityFrom(DamageSource.setExplosionSource(null), ((d11 * d11 + d11) / 2.0 * 8.0 * explosionSize + 1.0).F)
					val d8 = EnchantmentProtection.func_92092_a(entity, d11)
					entity.motionX += d5 * d8
					entity.motionY += d6 * d8
					entity.motionZ += d7 * d8
				}
			}
		}
	}
}