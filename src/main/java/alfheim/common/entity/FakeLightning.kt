package alfheim.common.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.AxisAlignedBB
import net.minecraft.world.World
import net.minecraftforge.event.ForgeEventFactory

/**
 * Like real lightning but less fire
 */
class FakeLightning(world: World, x: Double, y: Double, z: Double): EntityLightningBolt(world, x, y, z) {
	
	private var lightningState: Int = 0
	private var boltLivingTime: Int = 0
	
	init {
		setLocationAndAngles(x, y, z, 0f, 0f)
		this.lightningState = 2
		boltVertex = rand.nextLong()
		this.boltLivingTime = rand.nextInt(3) + 1
	}
	
	override fun onUpdate() {
		super.onUpdate()
		
		if (this.lightningState == 2) {
			worldObj.playSoundEffect(posX, posY, posZ, "ambient.weather.thunder", 10000f, 0.8f + rand.nextFloat() * 0.2f)
			worldObj.playSoundEffect(posX, posY, posZ, "random.explode", 2f, 0.5f + rand.nextFloat() * 0.2f)
		}
		
		--this.lightningState
		
		if (this.lightningState < 0) {
			if (this.boltLivingTime == 0) {
				setDead()
			} else if (this.lightningState < -rand.nextInt(10)) {
				--this.boltLivingTime
				this.lightningState = 1
				boltVertex = rand.nextLong()
			}
		}
		
		if (this.lightningState >= 0) {
			if (worldObj.isRemote) {
				worldObj.lastLightningBolt = 2
			} else {
				val d0 = 3.0
				val list = worldObj.getEntitiesWithinAABBExcludingEntity(this, AxisAlignedBB.getBoundingBox(posX - d0, posY - d0, posZ - d0, posX + d0, posY + 6.0 + d0, posZ + d0))
				
				for (l in list.indices) {
					val entity = list[l] as Entity
					if (!ForgeEventFactory.onEntityStruckByLightning(entity, this))
						entity.onStruckByLightning(this)
				}
			}
		}
	}
	
	override fun entityInit() = Unit
	override fun readEntityFromNBT(tag: NBTTagCompound) = Unit
	override fun writeEntityToNBT(tag: NBTTagCompound) = Unit
}