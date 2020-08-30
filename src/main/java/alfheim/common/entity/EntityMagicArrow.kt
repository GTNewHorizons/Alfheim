package alfheim.common.entity

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alfheim.client.render.world.VisualEffectHandlerClient.VisualEffects
import alfheim.common.achievement.AlfheimAchievements
import alfheim.common.core.asm.hook.AlfheimHookHandler
import alfheim.common.core.handler.VisualEffectHandler
import alfheim.common.item.AlfheimItems
import alfheim.common.item.relic.ItemMoonlightBow
import net.minecraft.entity.EntityLivingBase
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
@Suppress("UNCHECKED_CAST")
class EntityMagicArrow: EntityThrowableCopy {
	
	var banana: Boolean
		get() = dataWatcher.getWatchableObjectInt(27) != 0
		set(banana) = dataWatcher.updateObject(27, if (banana) 1 else 0)
	
	var damage: Float
		get() = dataWatcher.getWatchableObjectFloat(28)
		set(dmg) = dataWatcher.updateObject(28, dmg)
	
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
	constructor(world: World, thrower: EntityPlayer): super(world, thrower)
	
	override fun entityInit() {
		super.entityInit()
		dataWatcher.addObject(27, 0)
		dataWatcher.addObject(28, 0f)
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
		
		val toMoon = damage == -1f
		val fromMoon = damage < 0f
		val banana = !fromMoon && banana
		
		run {
			var r = 0.1f
			var g = 0.85f
			var b = 0.1f
			
			val or = if (banana) 0.95f else r * if (fromMoon) 3 else 1
			val og = if (banana) 0.95f else g
			val ob = if (banana) 0.1f else if (fromMoon) g else b
			
			var size = (damage / (AlfheimItems.moonlightBow as ItemMoonlightBow).maxDmg) * 0.75f
			if (!toMoon && fromMoon) size *= 50
			val life = if (fromMoon) 3f else 1f
			val osize = size
			val savedPosX = posX
			val savedPosY = posY
			val savedPosZ = posZ
			var currentPos = Vector3.fromEntity(this)
			val oldPos = Vector3(prevPosX, prevPosY, prevPosZ)
			var diffVec = oldPos.copy().sub(currentPos)
			val diffVecNorm = diffVec.copy().normalize()
			val distance = 0.095
			val rn = if (fromMoon) 0.1f else 0.25f
			
			AlfheimHookHandler.wispNoclip = true
			
			do {
				r = or + (Math.random().F - 0.5f) * rn
				g = og + (Math.random().F - 0.5f) * rn
				b = ob + (Math.random().F - 0.5f) * rn
				size = osize + (Math.random().F - 0.5f) * 0.065f + sin(Random(uniqueID.mostSignificantBits).nextInt(9001).D).F * 0.4f
				Botania.proxy.wispFX(worldObj, posX, posY, posZ, r, g, b, 0.2f * size, (-motionX).F * 0.01f, (-motionY).F * 0.01f, (-motionZ).F * 0.01f, life)
				this.posX += diffVecNorm.x * distance
				this.posY += diffVecNorm.y * distance
				this.posZ += diffVecNorm.z * distance
				currentPos = Vector3.fromEntity(this)
				diffVec = oldPos.copy().sub(currentPos)
			} while (abs(diffVec.length()) > distance)
			
			Botania.proxy.wispFX(worldObj, posX, posY, posZ, r, g, b, 0.1f * size, (Math.random() - 0.5).F * 0.06f, (Math.random() - 0.5).F * 0.06f, (Math.random() - 0.5).F * 0.06f, life)
			posX = savedPosX
			posY = savedPosY
			posZ = savedPosZ
		}
		
		if (posY >= 256) {
			if (!worldObj.isRemote) {
				if (toMoon) {
					for (i in 1..100) {
						val arrow = EntityMagicArrow(worldObj, thrower as EntityPlayer) // non-null check above
						val yaw = (Math.random() * 360).F
						arrow.shoot(thrower, 90f, yaw, 0f, (Math.random() * 2 + 4).F, 5f)
						arrow.damage = -(Math.random() * 5 + 5).F
						arrow.rotationYaw = thrower.rotationYaw
						arrow.rotation = MathHelper.wrapAngleTo180_float(yaw)
						arrow.life = 256
						
						arrow.setPosition(thrower.posX, 255.0, thrower.posZ)
						
						worldObj.spawnEntityInWorld(arrow)
					}
				}
			}
			
			setDead()
			return
		}
		
		if (ticksExisted > life) {
			if (toMoon)
				explode()
			
			setDead()
			return
		}
		
		if (!worldObj.isRemote) {
			val player = thrower as EntityPlayer
			
			val axis = if (fromMoon) bb.expand(1.0, 1.0, 1.0) else bb.expand(0.5, 0.5, 0.5)
			
			val entities = worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, axis) as List<EntityLivingBase>
			
			for (living in entities) {
				if (living === player) continue
				
				if (toMoon) {
					explode()
					setDead()
					return
				}
				
				if (fromMoon) {
					living.hurtResistantTime = 0
					living.hurtTime = 0
				}
				
				if (!toMoon && !fromMoon && Vector3.entityDistance(living, player) >= 120.0) player.triggerAchievement(AlfheimAchievements.divineMarksman)
				
				attackedFrom(living, player, abs(damage))
			}
		}
	}
	
	val bb: AxisAlignedBB
		get() = AxisAlignedBB.getBoundingBox(min(posX, lastTickPosX), min(posY, lastTickPosY), min(posZ, lastTickPosZ), max(posX, lastTickPosX), max(posY, lastTickPosY), max(posZ, lastTickPosZ))
	
	fun explode() {
		val axis = bb.expand(5.0, 5.0, 5.0)
		val entities = worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, axis) as List<EntityLivingBase>
		entities.forEach {
			attackedFrom(it, thrower, max(20.0, 20 / Vector3.entityDistance(this, it)).F)
		}
		
		VisualEffectHandler.sendPacket(VisualEffects.MOON, this)
	}
	
	fun attackedFrom(target: EntityLivingBase, entity: EntityLivingBase?, dmg: Float) {
		if (entity is EntityPlayer)
			target.attackEntityFrom(DamageSource.causePlayerDamage(entity), dmg)
		else if (entity != null)
			target.attackEntityFrom(DamageSource.causeMobDamage(entity), dmg)
	}
	
	override fun onImpact(pos: MovingObjectPosition) = Unit // NO-OP
	
	override fun writeEntityToNBT(cmp: NBTTagCompound) {
		super.writeEntityToNBT(cmp)
		cmp.setFloat(TAG_DAMAGE, damage)
		cmp.setInteger(TAG_LIFE, life)
		cmp.setFloat(TAG_ROTATION, rotation)
	}
	
	override fun readEntityFromNBT(cmp: NBTTagCompound) {
		super.readEntityFromNBT(cmp)
		damage = cmp.getFloat(TAG_DAMAGE)
		life = cmp.getInteger(TAG_LIFE)
		rotation = cmp.getFloat(TAG_ROTATION)
	}
	
	companion object {
		
		const val TAG_DAMAGE = "damage"
		const val TAG_LIFE = "life"
		const val TAG_ROTATION = "rotation"
		
	}
}
