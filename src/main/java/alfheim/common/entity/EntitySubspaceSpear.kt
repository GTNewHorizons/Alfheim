package alfheim.common.entity

import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.*
import net.minecraft.world.World
import vazkii.botania.common.entity.EntityThrowableCopy

/**
 * @author ExtraMeteorP, CKATEPTb
 */
class EntitySubspaceSpear: EntityThrowableCopy {
	
	var liveTicks: Int
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
		dataWatcher.setObjectWatched(30)
	}
	
	fun shoot(entityThrower: Entity, rotationPitchIn: Float, rotationYawIn: Float, pitchOffset: Float, velocity: Float, inaccuracy: Float) {
		val f = -MathHelper.sin(rotationYawIn * 0.017453292f) * MathHelper.cos(rotationPitchIn * 0.017453292f)
		val f1 = -MathHelper.sin((rotationPitchIn + pitchOffset) * 0.017453292f)
		val f2 = MathHelper.cos(rotationYawIn * 0.017453292f) * MathHelper.cos(rotationPitchIn * 0.017453292f)
		setThrowableHeading(f.toDouble(), f1.toDouble(), f2.toDouble(), velocity, inaccuracy)
		motionX += entityThrower.motionX
		motionZ += entityThrower.motionZ
		
		if (!entityThrower.onGround) {
			motionY += entityThrower.motionY
		}
	}
	
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
				
				if (living.hurtTime == 0) {
					dealTrueDamage(this.thrower, living, damage * 0.4f)
					attackedFrom(living, thrower, (damage * 1.5f).toInt())
				}
			}
		}
		super.onUpdate()
		
		if (ticksExisted > life)
			setDead()
	}
	
	override fun onImpact(pos: MovingObjectPosition) {
		val thrower = thrower
		if (pos.entityHit == null || pos.entityHit !== thrower) {
			// TODO
		}
	}
	
	override fun writeEntityToNBT(cmp: NBTTagCompound) {
		super.writeEntityToNBT(cmp)
		cmp.setInteger(TAG_LIVE_TICKS, liveTicks)
		cmp.setFloat(TAG_ROTATION, rotation)
		cmp.setInteger(TAG_LIFE, life)
		cmp.setFloat(TAG_DAMAGE, damage)
		cmp.setFloat(TAG_PITCH, pitch)
	}
	
	override fun readEntityFromNBT(cmp: NBTTagCompound) {
		super.readEntityFromNBT(cmp)
		liveTicks = cmp.getInteger(TAG_LIVE_TICKS)
		rotation = cmp.getFloat(TAG_ROTATION)
		life = cmp.getInteger(TAG_LIFE)
		damage = cmp.getFloat(TAG_DAMAGE)
		pitch = cmp.getFloat(TAG_PITCH)
	}
	
	companion object {
		
		const val TAG_LIVE_TICKS = "liveTicks"
		const val TAG_ROTATION = "rotation"
		const val TAG_DAMAGE = "damage"
		const val TAG_LIFE = "life"
		const val TAG_PITCH = "pitch"
		
		fun dealTrueDamage(player: EntityLivingBase, target: EntityLivingBase?, amount: Float): Float {
			val result = 0f
			
			if (target == null)
				return result
			if (!target.isEntityAlive)
				return result
			if (amount < 0)
				return result
			
			target.attackEntityFrom(DamageSource.magic.setDamageIsAbsolute().setDamageBypassesArmor(), 0.01f)
			if (target is EntityPlayer)
				if (target.capabilities.isCreativeMode)
					return result
			
			target.attackEntityFrom(DamageSource.causeIndirectMagicDamage(player, target), amount)
			
			return result
		}
		
		fun attackedFrom(target: EntityLivingBase, player: EntityLivingBase, i: Int) {
			if (player is EntityPlayer)
				target.attackEntityFrom(DamageSource.causePlayerDamage(player), i.toFloat())
			else
				target.attackEntityFrom(DamageSource.causeMobDamage(player), i.toFloat())
		}
	}
}