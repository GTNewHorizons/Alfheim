package alfheim.common.entity.spell

import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.api.spell.*
import alfheim.client.render.world.SpellEffectHandlerClient.Spells
import alfheim.common.core.handler.CardinalSystem.PartySystem
import alfheim.common.core.handler.SpellEffectHandler
import alfheim.common.core.util.DamageSourceSpell
import cpw.mods.fml.relauncher.*
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.*
import net.minecraft.world.World
import vazkii.botania.common.Botania

import java.util.*
import kotlin.math.atan2

class EntitySpellFireball(world: World): Entity(world), ITimeStopSpecific {
	
	var accelerationX: Double = 0.toDouble()
	var accelerationY: Double = 0.toDouble()
	var accelerationZ: Double = 0.toDouble()
	var caster: EntityLivingBase? = null
	
	override val isImmune: Boolean
		get() = false
	
	init {
		setSize(0f, 0f)
	}
	
	constructor(world: World, x: Double, y: Double, z: Double, accX: Double, accY: Double, accZ: Double): this(world) {
		setLocationAndAngles(x, y, z, rotationYaw, rotationPitch)
		val d = MathHelper.sqrt_double(accX * accX + accY * accY + accZ * accZ).toDouble()
		accelerationX = accX / d * 0.1
		accelerationY = accY / d * 0.1
		accelerationZ = accZ / d * 0.1
	}
	
	constructor(world: World, shooter: EntityLivingBase): this(world, shooter.posX, shooter.posY + shooter.eyeHeight, shooter.posZ, shooter.lookVec.xCoord, shooter.lookVec.yCoord, shooter.lookVec.zCoord) {
		this.caster = shooter
		setRotation(shooter.rotationYaw, shooter.rotationPitch)
	}
	
	override fun attackEntityFrom(source: DamageSource?, damage: Float): Boolean {
		return false
	}
	
	fun onImpact(mop: MovingObjectPosition?) {
		if (!worldObj.isRemote) {
			if (mop?.entityHit != null) mop.entityHit.attackEntityFrom(DamageSourceSpell.fireball(this, caster), SpellBase.over(caster, 6.0))
			for (o in worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, AxisAlignedBB.getBoundingBox(posX, posY, posZ, posX, posY, posZ).expand(2.0, 2.0, 2.0))) {
				val e = o as EntityLivingBase
				if (!PartySystem.mobsSameParty(e, caster)) e.attackEntityFrom(DamageSourceSpell.fireball(this, caster), SpellBase.over(caster, 6.0))
			}
			worldObj.playSoundEffect(posX, posY, posZ, "random.explode", 4.0f, (1.0f + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2f) * 0.7f)
			SpellEffectHandler.sendPacket(Spells.EXPL, this)
			setDead()
		}
	}
	
	override fun onUpdate() {
		if (!AlfheimCore.enableMMO || !worldObj.isRemote && (caster != null && caster!!.isDead || !worldObj.blockExists(posX.toInt(), posY.toInt(), posZ.toInt()))) {
			setDead()
		} else {
			//if (!ASJUtilities.isServer()) return;
			super.onUpdate()
			
			if (ticksExisted == 600) onImpact(null)
			
			val vec3 = Vec3.createVectorHelper(posX, posY, posZ)
			val vec31 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ)
			var movingobjectposition: MovingObjectPosition? = worldObj.rayTraceBlocks(vec3, vec31)
			
			if (movingobjectposition == null) {
				val l = worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, boundingBox.addCoord(motionX, motionY, motionZ).expand(0.1, 0.1, 0.1)) as MutableList<EntityLivingBase>
				l.remove(caster)
				
				for (e in l)
					if (e.canBeCollidedWith() && !PartySystem.mobsSameParty(caster, e)) {
						movingobjectposition = MovingObjectPosition(e)
						break
					}
			}
			
			if (movingobjectposition != null) onImpact(movingobjectposition)
			
			posX += motionX
			posY += motionY
			posZ += motionZ
			val f1 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ)
			rotationYaw = (atan2(motionZ, motionX) * 180.0 / Math.PI).toFloat() + 90.0f
			
			rotationPitch = (atan2(f1.toDouble(), motionY) * 180.0 / Math.PI).toFloat() - 90.0f
			while (rotationPitch - prevRotationPitch < -180.0f) prevRotationPitch -= 360.0f
			while (rotationPitch - prevRotationPitch >= 180.0f) prevRotationPitch += 360.0f
			while (rotationYaw - prevRotationYaw < -180.0f) prevRotationYaw -= 360.0f
			while (rotationYaw - prevRotationYaw >= 180.0f) prevRotationYaw += 360.0f
			
			rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2f
			rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2f
			var f2 = 0.95f
			
			if (isInWater) {
				for (j in 0..3) {
					val f3 = 0.25f
					worldObj.spawnParticle("bubble", posX - motionX * f3.toDouble(), posY - motionY * f3.toDouble(), posZ - motionZ * f3.toDouble(), motionX, motionY, motionZ)
				}
				
				f2 = 0.8f
			}
			
			motionX += accelerationX
			motionY += accelerationY
			motionZ += accelerationZ
			motionX *= f2.toDouble()
			motionY *= f2.toDouble()
			motionZ *= f2.toDouble()
			setPosition(posX, posY, posZ)
			
			for (i in 0..4) {
				val v = Vector3(motionX, motionY, motionZ)//.normalize().multiply(0.05);
				Botania.proxy.wispFX(worldObj, posX, posY - 0.2, posZ, 1f, Math.random().toFloat() * 0.25f, Math.random().toFloat() * 0.075f, 0.65f + Math.random().toFloat() * 0.45f, v.x.toFloat(), v.y.toFloat(), v.z.toFloat(), 0.1f)
				
				Botania.proxy.wispFX(worldObj, posX + Math.random() * 0.5 - 0.25, posY + Math.random() * 0.5 - 0.25, posZ + Math.random() * 0.5 - 0.25, 1f, Math.random().toFloat() * 0.25f, Math.random().toFloat() * 0.075f, Math.random().toFloat() * 0.25f, 0f, 0.5f)
				// smoke
				val gs = Math.random().toFloat() * 0.15f
				Botania.proxy.wispFX(worldObj, posX, posY - 0.25, posZ, gs, gs, gs, 2f, -0.15f)
			}
		}
	}
	
	override fun canBeCollidedWith(): Boolean {
		return true
	}
	
	override fun getCollisionBorderSize(): Float {
		return 1.0f
	}
	
	@SideOnly(Side.CLIENT)
	override fun getShadowSize(): Float {
		return 0.0f
	}
	
	override fun affectedBy(uuid: UUID): Boolean {
		return caster!!.uniqueID != uuid
	}
	
	public override fun entityInit() {}
	
	public override fun readEntityFromNBT(nbt: NBTTagCompound) {
		if (nbt.hasKey("castername")) caster = worldObj.getPlayerEntityByName(nbt.getString("castername")) else setDead()
		if (caster == null) setDead()
	}
	
	public override fun writeEntityToNBT(nbt: NBTTagCompound) {
		if (caster is EntityPlayer) nbt.setString("castername", caster!!.commandSenderName)
	}
}