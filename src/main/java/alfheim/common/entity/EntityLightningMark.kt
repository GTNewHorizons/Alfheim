package alfheim.common.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World

class EntityLightningMark @JvmOverloads constructor(world: World, x: Double = 0.0, y: Double = 0.0, z: Double = 0.0): Entity(world) {
	
	init {
		setSize(1.5f, 0.0001f)
		setPosition(x, y, z)
	}
	
	override fun onEntityUpdate() {
		if (ticksExisted > 50) {
			if (!worldObj.isRemote)
				worldObj.addWeatherEffect(EntityLightningBolt(worldObj, posX, posY, posZ))
			
			setDead()
		}
	}
	
	override fun entityInit() = Unit
	override fun readEntityFromNBT(nbt: NBTTagCompound) = Unit
	override fun writeEntityToNBT(nbt: NBTTagCompound) = Unit
}