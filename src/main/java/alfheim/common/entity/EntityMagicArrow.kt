package alfheim.common.entity

import alexsocol.asjlib.math.Vector3
import alfheim.common.item.AlfheimItems
import alfheim.common.item.relic.ItemSunrayBow
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.*
import net.minecraft.world.World
import vazkii.botania.common.Botania
import vazkii.botania.common.entity.EntityThrowableCopy
import java.util.*
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
	
	init {
		setSize(0f, 0f)
	}
	
	constructor(worldIn: World): super(worldIn)
	constructor(world: World, thrower: EntityLivingBase): super(world, thrower)
	
	override fun entityInit() {
		super.entityInit()
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
		
//		for (i in 0..5)
//			Botania.proxy.wispFX(worldObj, posX + Math.random() * 0.2 - 0.1, posY + Math.random() * 0.2 - 0.1, posZ + Math.random() * 0.2 - 0.1, 0.1f, 0.85f, 0.1f, 0.2f, 0f)
		
		run {
			var r = 0.1f
			var g = 0.85f
			var b = 0.1f
			val or = r
			val og = g
			val ob = b
			var size = (damage.toFloat() / (AlfheimItems.sunrayBow as ItemSunrayBow).maxDmg) * 0.75f
			val osize = size
			val savedPosX = posX
			val savedPosY = posY
			val savedPosZ = posZ
			var currentPos = Vector3.fromEntity(this)
			val oldPos = Vector3(prevPosX, prevPosY, prevPosZ)
			var diffVec = oldPos.copy().sub(currentPos)
			val diffVecNorm = diffVec.copy().normalize()
			val distance = 0.095
			
			do {
				r = or + (Math.random().toFloat() - 0.5f) * 0.25f
				g = og + (Math.random().toFloat() - 0.5f) * 0.25f
				b = ob + (Math.random().toFloat() - 0.5f) * 0.25f
				size = osize + (Math.random().toFloat() - 0.5f) * 0.065f + sin(Random(entityUniqueID.mostSignificantBits).nextInt(9001).toDouble()).toFloat() * 0.4f
				Botania.proxy.wispFX(worldObj, posX, posY, posZ, r, g, b, 0.2f * size, (-motionX).toFloat() * 0.01f, (-motionY).toFloat() * 0.01f, (-motionZ).toFloat() * 0.01f)
				this.posX += diffVecNorm.x * distance
				this.posY += diffVecNorm.y * distance
				this.posZ += diffVecNorm.z * distance
				currentPos = Vector3.fromEntity(this)
				diffVec = oldPos.copy().sub(currentPos)
			} while (abs(diffVec.length()) > distance)
			
			Botania.proxy.wispFX(worldObj, posX, posY, posZ, r, g, b, 0.1f * size, (Math.random() - 0.5).toFloat() * 0.06f, (Math.random() - 0.5).toFloat() * 0.06f, (Math.random() - 0.5).toFloat() * 0.06f)
			posX = savedPosX
			posY = savedPosY
			posZ = savedPosZ
		}
		
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
