package alfheim.common.entity.spell

import alexsocol.asjlib.math.Vector3
import alfheim.common.core.util.DamageSourceSpell
import net.minecraft.block.*
import net.minecraft.entity.*
import net.minecraft.entity.monster.IMob
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.*
import net.minecraft.world.World
import vazkii.botania.common.Botania
import vazkii.botania.common.entity.EntityThrowableCopy
import kotlin.math.abs

class EntitySpellIsaacMissile(world: World): EntityThrowableCopy(world) {
	
	internal var time = 0
	
	var isEvil: Boolean
		get() = dataWatcher.getWatchableObjectByte(25).toInt() == 1
		set(evil) = dataWatcher.updateObject(25, (if (evil) 1 else 0).toByte())
	
	val targetEntity: EntityLivingBase?
		get() {
			val id = dataWatcher.getWatchableObjectInt(26)
			val e = worldObj.getEntityByID(id)
			return if (e is EntityLivingBase) e else null
		}
	
	// Just in case...
	val target: Boolean
		get() {
			var target = targetEntity
			if (target != null && target.health > 0 && !target.isDead && worldObj.loadedEntityList.contains(target))
				return true
			if (target != null)
				setTarget(null)
			
			val range = 16.0
			val entities = worldObj.getEntitiesWithinAABB(if (isEvil) EntityPlayer::class.java else IMob::class.java, AxisAlignedBB.getBoundingBox(posX - range, posY - range, posZ - range, posX + range, posY + range, posZ + range))
			while (entities.size > 0) {
				val e = entities[worldObj.rand.nextInt(entities.size)] as Entity
				if (e !is EntityLivingBase || e.isDead) {
					entities.remove(e)
					continue
				}
				
				target = e
				setTarget(target)
				break
			}
			
			return target != null
		}
	
	init {
		setSize(0f, 0f)
	}
	
	constructor(thrower: EntityLivingBase, evil: Boolean): this(thrower.worldObj) {
		this.thrower = thrower
		isEvil = evil
	}
	
	override fun entityInit() {
		dataWatcher.addObject(25, 0.toByte())
		dataWatcher.addObject(26, 0)
	}
	
	fun setTarget(e: EntityLivingBase?) {
		dataWatcher.updateObject(26, e?.entityId ?: -1)
	}
	
	override fun onUpdate() {
		val lastTickPosX = lastTickPosX
		val lastTickPosY = lastTickPosY
		val lastTickPosZ = lastTickPosZ
		
		super.onUpdate()
		
		if (!worldObj.isRemote && (!target || time > 60)) {
			setDead()
			return
		}
		
		val thisVec = Vector3.fromEntityCenter(this)
		val oldPos = Vector3(lastTickPosX, lastTickPosY, lastTickPosZ)
		val diff = thisVec.copy().sub(oldPos)
		val step = diff.copy().normalize().mul(0.05)
		val steps = (diff.length() / step.length()).toInt()
		val particlePos = oldPos.copy()
		
		for (i in 0 until steps) {
			Botania.proxy.sparkleFX(worldObj, particlePos.x, particlePos.y, particlePos.z, 1f, 0.4f, 1f, 0.8f, 2)
			if (worldObj.rand.nextInt(steps) <= 1)
				Botania.proxy.sparkleFX(worldObj, particlePos.x + (Math.random() - 0.5) * 0.4, particlePos.y + (Math.random() - 0.5) * 0.4, particlePos.z + (Math.random() - 0.5) * 0.4, 1f, 0.4f, 1f, 0.8f, 2)
			
			particlePos.add(step)
		}
		
		val target = targetEntity
		if (target != null) {
			val targetVec = Vector3.fromEntityCenter(target)
			val diffVec = targetVec.copy().sub(thisVec)
			val motionVec = diffVec.copy().normalize().mul(0.6)
			motionX = motionVec.x
			motionY = motionVec.y
			if (time < 10)
				motionY = abs(motionY)
			motionZ = motionVec.z
			
			val targetList = worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, AxisAlignedBB.getBoundingBox(posX - 0.5, posY - 0.5, posZ - 0.5, posX + 0.5, posY + 0.5, posZ + 0.5))
			if (targetList.contains(target)) {
				val thrower = thrower
				if (thrower != null)
					target.attackEntityFrom(DamageSourceSpell.missile(this, thrower), 12f)
				else
					target.attackEntityFrom(DamageSource.magic, 12f)
				
				target.hurtResistantTime = 0
				
				setDead()
			}
		}
		
		time++
	}
	
	override fun writeEntityToNBT(cmp: NBTTagCompound) {
		super.writeEntityToNBT(cmp)
		cmp.setInteger(TAG_TIME, time)
	}
	
	override fun readEntityFromNBT(cmp: NBTTagCompound) {
		super.readEntityFromNBT(cmp)
		time = cmp.getInteger(TAG_TIME)
	}
	
	override fun onImpact(pos: MovingObjectPosition) {
		val block = worldObj.getBlock(pos.blockX, pos.blockY, pos.blockZ)
		
		if (block !is BlockBush && block !is BlockLeaves && (pos.entityHit == null || targetEntity === pos.entityHit))
			setDead()
	}
	
	companion object {
		private const val TAG_TIME = "time"
	}
}