package alfheim.common.entity

import alfheim.common.item.AlfheimItems
import alfheim.common.item.relic.ItemSpearSubspace
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.*
import net.minecraft.world.World
import vazkii.botania.common.entity.EntityThrowableCopy

/**
 * @author ExtraMeteorP, CKATEPTb
 */
class EntitySubspace: EntityThrowableCopy {
	
	var liveTicks: Int
		get() = dataWatcher.getWatchableObjectInt(24)
		set(ticks) {
			dataWatcher.updateObject(24, ticks)
		}
	
	var delay: Int
		get() = dataWatcher.getWatchableObjectInt(25)
		set(delay) {
			dataWatcher.updateObject(25, delay)
		}
	
	var count: Int
		get() = dataWatcher.getWatchableObjectInt(26)
		set(count) {
			dataWatcher.updateObject(26, count)
		}
	
	var type: Int
		get() = dataWatcher.getWatchableObjectInt(27)
		set(type) {
			dataWatcher.updateObject(27, type)
		}
	
	var interval: Int
		get() = dataWatcher.getWatchableObjectInt(28)
		set(interval) {
			dataWatcher.updateObject(28, interval)
		}
	
	var rotation: Float
		get() = dataWatcher.getWatchableObjectFloat(29)
		set(rot) {
			dataWatcher.updateObject(29, rot)
		}
	
	var size: Float
		get() = dataWatcher.getWatchableObjectFloat(30)
		set(size) {
			dataWatcher.updateObject(30, size)
		}
	
	constructor(world: World): super(world)
	
	constructor(world: World, thrower: EntityLivingBase): super(world, thrower)
	
	override fun onUpdate() {
		
		motionX = 0.0
		motionY = 0.0
		motionZ = 0.0
		
		super.onUpdate()
		
		if (ticksExisted < delay)
			return
		
		if (ticksExisted > liveTicks + delay)
			setDead()
		val thrower = thrower
		if (!worldObj.isRemote && (thrower == null || thrower.isDead)) {
			setDead()
			return
		}
		
		if (!worldObj.isRemote)
			if (type == 0) {
				if (ticksExisted % interval == 0 && count < 6 && ticksExisted > delay + 5
					&& ticksExisted < liveTicks - delay - 10) {
					if (thrower !is EntityPlayer)
						setDead()
					val player = getThrower() as EntityPlayer
					
					val burst = (AlfheimItems.subspaceSpear as ItemSpearSubspace).getBurst(player, ItemStack(AlfheimItems.subspaceSpear))
					burst.setPosition(posX, posY, posZ)
					burst.color = 0XFFAF00
					player.worldObj.spawnEntityInWorld(burst)
					count++
				}
			} else if (type == 1) {
				if (ticksExisted > delay + 8 && count < 1) {
					val spear = EntitySubspaceSpear(worldObj, thrower!!)
					
					spear.damage = 8f
					spear.life = 100
					spear.rotationYaw = thrower.rotationYaw
					spear.pitch = -thrower.rotationPitch
					spear.rotation = MathHelper.wrapAngleTo180_float(-thrower.rotationYaw + 180)
					spear.shoot(thrower, thrower.rotationPitch, thrower.rotationYaw, 0f, 1.45f, 1f)
					spear.setPosition(posX, posY, posZ)
					
					thrower.worldObj.spawnEntityInWorld(spear)
					count++
				}
			}
	}
	
	override fun entityInit() {
		setSize(0f, 0f)
		dataWatcher.addObject(24, 0)
		dataWatcher.addObject(25, 0)
		dataWatcher.addObject(26, 0)
		dataWatcher.addObject(27, 0)
		dataWatcher.addObject(28, 0)
		dataWatcher.addObject(29, 0f)
		dataWatcher.setObjectWatched(29)
		dataWatcher.addObject(30, 0f)
	}
	
	override fun readEntityFromNBT(cmp: NBTTagCompound) {
		liveTicks = cmp.getInteger(TAG_LIVE_TICKS)
		delay = cmp.getInteger(TAG_DELAY)
		rotation = cmp.getFloat(TAG_ROTATION)
		size = cmp.getFloat(TAG_SIZE)
		interval = cmp.getInteger(TAG_INTERVAL)
		count = cmp.getInteger(TAG_COUNT)
		type = cmp.getInteger(TAG_TYPE)
	}
	
	override fun writeEntityToNBT(cmp: NBTTagCompound) {
		cmp.setInteger(TAG_LIVE_TICKS, liveTicks)
		cmp.setInteger(TAG_DELAY, delay)
		cmp.setFloat(TAG_ROTATION, rotation)
		cmp.setFloat(TAG_SIZE, size)
		cmp.setInteger(TAG_INTERVAL, interval)
		cmp.setInteger(TAG_COUNT, count)
		cmp.setInteger(TAG_TYPE, type)
	}
	
	override fun onImpact(result: MovingObjectPosition) {
	
	}
	
	companion object {
		const val TAG_LIVE_TICKS = "liveTicks"
		const val TAG_DELAY = "delay"
		const val TAG_ROTATION = "rotation"
		const val TAG_INTERVAL = "interval"
		const val TAG_SIZE = "size"
		const val TAG_COUNT = "count"
		const val TAG_TYPE = "type"
	}
}