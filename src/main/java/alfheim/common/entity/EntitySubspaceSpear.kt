package alfheim.common.entity

import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.*
import net.minecraft.world.World
import vazkii.botania.common.entity.EntityThrowableCopy

/**
 * @author ExtraMeteorP, CKATEPTb
 */
class EntitySubspaceSpear: EntityThrowableCopy {
	
	var type: Int
		get() = dataWatcher.getWatchableObjectInt(26)
		set(ticks) = dataWatcher.updateObject(26, ticks)
	
	var rotation: Float
		get() = dataWatcher.getWatchableObjectFloat(27)
		set(rot) = dataWatcher.updateObject(27, rot)
	
	var pitch: Float
		get() = dataWatcher.getWatchableObjectFloat(28)
		set(rot) = dataWatcher.updateObject(28, rot)
	
	var life: Int
		get() = dataWatcher.getWatchableObjectInt(29)
		set(delay) = dataWatcher.updateObject(29, delay)
	
	var damage: Float
		get() = dataWatcher.getWatchableObjectFloat(30)
		set(delay) = dataWatcher.updateObject(30, delay)
	
	constructor(worldIn: World): super(worldIn)
	
	constructor(world: World, thrower: EntityLivingBase): super(world, thrower)
	
	override fun entityInit() {
		super.entityInit()
		setSize(0f, 0f)
		dataWatcher.addObject(26, 0)
		dataWatcher.addObject(27, 0f)
		dataWatcher.setObjectWatched(27)
		dataWatcher.addObject(28, 0f)
		dataWatcher.addObject(29, 0)
		dataWatcher.addObject(30, 0f)
	}
	
	override fun onImpact(mop: MovingObjectPosition?) = Unit
	
	override fun getGravityVelocity() = 0f
	
	override fun onUpdate() {
		val thrower = thrower
		if (!worldObj.isRemote && (thrower == null || thrower.isDead)) {
			setDead()
			return
		}
		
		if (!worldObj.isRemote) {
			val axis = AxisAlignedBB.getBoundingBox(posX - 1f, posY - 0.45f, posZ - 1f, lastTickPosX + 1f, lastTickPosY + 0.45f, lastTickPosZ + 1f)
			
			@Suppress("UNCHECKED_CAST")
			val entities = worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, axis) as List<EntityLivingBase>
			for (living in entities) {
				if (living === thrower)
					continue
				
				attackedFrom(living, thrower, damage * 1.5f)
			}
		}
		super.onUpdate()
		
		if (ticksExisted > life)
			setDead()
	}
	
	override fun writeEntityToNBT(cmp: NBTTagCompound) {
		super.writeEntityToNBT(cmp)
		cmp.setInteger(TAG_type, type)
		cmp.setFloat(TAG_ROTATION, rotation)
		cmp.setInteger(TAG_LIFE, life)
		cmp.setFloat(TAG_DAMAGE, damage)
		cmp.setFloat(TAG_PITCH, pitch)
	}
	
	override fun readEntityFromNBT(cmp: NBTTagCompound) {
		super.readEntityFromNBT(cmp)
		type = cmp.getInteger(TAG_type)
		rotation = cmp.getFloat(TAG_ROTATION)
		life = cmp.getInteger(TAG_LIFE)
		damage = cmp.getFloat(TAG_DAMAGE)
		pitch = cmp.getFloat(TAG_PITCH)
	}
	
	companion object {
		
		const val TAG_type = "type"
		const val TAG_ROTATION = "rotation"
		const val TAG_DAMAGE = "damage"
		const val TAG_LIFE = "life"
		const val TAG_PITCH = "pitch"
		
		fun attackedFrom(target: EntityLivingBase, player: EntityLivingBase, i: Float) {
			target.attackEntityFrom((if (player is EntityPlayer) DamageSource.causePlayerDamage(player) else DamageSource.causeMobDamage(player)).setDamageBypassesArmor(), i)
		}
	}
}