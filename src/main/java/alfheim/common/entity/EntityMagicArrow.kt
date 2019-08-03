package alfheim.common.entity

import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.*
import net.minecraft.world.World
import vazkii.botania.common.Botania
import vazkii.botania.common.entity.EntityThrowableCopy
import kotlin.math.*

/**
 * @author ExtraMeteorP, CKATEPTb
 */
class EntityMagicArrow: EntityThrowableCopy {
	
	var damage: Int
		get() = dataWatcher.getWatchableObjectInt(28)
		set(delay) = dataWatcher.updateObject(28, delay)
	
	var life: Int
		get() = dataWatcher.getWatchableObjectInt(29)
		set(delay) = dataWatcher.updateObject(29, delay)
	
	var rotation: Float
		get() = dataWatcher.getWatchableObjectFloat(30)
		set(rot) = dataWatcher.updateObject(30, rot)
	
	constructor(worldIn: World): super(worldIn)
	constructor(world: World, thrower: EntityLivingBase): super(world, thrower)
	
	override fun entityInit() {
		super.entityInit()
		setSize(0f, 0f)
		dataWatcher.addObject(28, 0)
		dataWatcher.addObject(29, 0)
		dataWatcher.addObject(30, 0f)
	}
	
	override fun getGravityVelocity() = 0f
	
	override fun onUpdate() {
		val thrower = thrower
		if (!worldObj.isRemote && (thrower == null || thrower !is EntityPlayer || thrower.isDead)) {
			setDead()
			return
		}
		
		super.onUpdate()
		
		for (i in 0..5)
			Botania.proxy.wispFX(worldObj, posX + Math.random() * 0.5 - 0.25, posY + Math.random() * 0.5 - 0.25, posZ + Math.random() * 0.5 - 0.25, 0.1f, 0.85f, 0.1f, 0.2f, 0f)
		
		if (ticksExisted > life) setDead()
		
		if (worldObj.isRemote) return
		
		val player = thrower as EntityPlayer
		
		if (!worldObj.isRemote) {
			val axis = AxisAlignedBB.getBoundingBox(min(posX, lastTickPosX), min(posY, lastTickPosY), min(posZ, lastTickPosZ), max(posX, lastTickPosX), max(posY, lastTickPosY), max(posZ, lastTickPosZ)).expand(0.5, 0.5, 0.5)
			
			val entities = worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, axis) as List<EntityLivingBase>
			for (living in entities) {
				if (living === thrower) continue
				val attribute = player.getEntityAttribute(SharedMonsterAttributes.attackDamage).attributeValue
				if (living.hurtTime == 0) {
					attackedFrom(living, player, (damage + attribute).toInt())
				}
			}
		}
	}
	
	fun attackedFrom(target: EntityLivingBase, player: EntityPlayer?, i: Int) {
		if (player != null)
			target.attackEntityFrom(DamageSource.causePlayerDamage(player), i.toFloat())
		else
			target.attackEntityFrom(DamageSource.generic, i.toFloat())
	}
	
	override fun onImpact(pos: MovingObjectPosition) {
		val thrower = thrower
		if (pos.entityHit == null || pos.entityHit !== thrower) {
			// NO-OP ???
		}
	}
	
	override fun writeEntityToNBT(cmp: NBTTagCompound) {
		super.writeEntityToNBT(cmp)
		cmp.setInteger(TAG_LIFE, life)
		cmp.setInteger(TAG_DAMAGE, damage)
		cmp.setFloat(TAG_ROTATION, rotation)
	}
	
	override fun readEntityFromNBT(cmp: NBTTagCompound) {
		super.readEntityFromNBT(cmp)
		life = cmp.getInteger(TAG_LIFE)
		damage = cmp.getInteger(TAG_DAMAGE)
		rotation = cmp.getFloat(TAG_ROTATION)
	}
	
	companion object {
		
		const val TAG_DAMAGE = "damage"
		const val TAG_LIFE = "life"
		const val TAG_ROTATION = "rotation"
		
	}
}
