package alfheim.common.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World

class EntityLightningMark @JvmOverloads constructor(world: World, x: Double = 0.0, y: Double = 0.0, z: Double = 0.0): Entity(world) {
	
	var timer: Int
		get() = dataWatcher.getWatchableObjectInt(20)
		set(time) = dataWatcher.updateObject(20, time)
	
	init {
		setSize(1.5f, 0.0001f)
		setPosition(x, y, z)
	}
	
	override fun onEntityUpdate() {
		motionZ = 0.0
		motionY = motionZ
		motionX = motionY
		timer = timer + 1
		if (timer > 50) {
			if (!worldObj.isRemote) worldObj.addWeatherEffect(EntityLightningBolt(worldObj, posX, posY, posZ))
			this.setDead()
		}
	}
	
	public override fun entityInit() {
		dataWatcher.addObject(20, 0)
	}
	
	public override fun readEntityFromNBT(nbt: NBTTagCompound) {
		timer = nbt.getInteger(TAG_TIMER)
	}
	
	public override fun writeEntityToNBT(nbt: NBTTagCompound) {
		nbt.setInteger(TAG_TIMER, timer)
	}
	
	companion object {
		
		val TAG_TIMER = "timer"
	}
}