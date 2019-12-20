package alfheim.common.entity.spell

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.api.spell.*
import alfheim.client.render.world.VisualEffectHandlerClient.VisualEffects
import alfheim.common.core.handler.CardinalSystem.PartySystem
import alfheim.common.core.handler.VisualEffectHandler
import alfheim.common.core.util.*
import alfheim.common.spell.tech.SpellDriftingMine
import cpw.mods.fml.relauncher.*
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.*
import net.minecraft.world.World

import java.util.*
import kotlin.math.atan2

class EntitySpellDriftingMine(world: World): Entity(world), ITimeStopSpecific {
	
	var caster: EntityLivingBase? = null
	
	override val isImmune: Boolean
		get() = false
	
	init {
		setSize(1f, 1f)
	}
	
	constructor(world: World, shooter: EntityLivingBase): this(world) {
		caster = shooter
		setPositionAndRotation(caster!!.posX, caster!!.posY + caster!!.height * 0.75, caster!!.posZ, caster!!.rotationYaw, caster!!.rotationPitch)
		if (caster!!.isSneaking) return
		val m = Vector3(caster!!.lookVec).mul(SpellDriftingMine.efficiency)
		motionX = m.x
		motionY = m.y
		motionZ = m.z
	}
	
	fun onImpact(mop: MovingObjectPosition?) {
		if (!worldObj.isRemote) {
			if (mop?.entityHit != null) mop.entityHit.attackEntityFrom(DamageSourceSpell.explosion(this, caster), SpellBase.over(caster, SpellDriftingMine.damage.toDouble()))
			for (o in worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, AxisAlignedBB.getBoundingBox(posX, posY, posZ, posX, posY, posZ).expand(SpellDriftingMine.radius))) {
				val e = o as EntityLivingBase
				if (!PartySystem.mobsSameParty(e, caster)) e.attackEntityFrom(DamageSourceSpell.explosion(this, caster), SpellBase.over(caster, SpellDriftingMine.damage.toDouble()))
			}
			worldObj.playSoundEffect(posX, posY, posZ, "random.explode", 4.0f, (1.0f + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.2f) * 0.7f)
			VisualEffectHandler.sendPacket(VisualEffects.EXPL, this)
			setDead()
		}
	}
	
	override fun onUpdate() {
		if (!AlfheimCore.enableMMO || !worldObj.isRemote && (caster != null && caster!!.isDead || !worldObj.blockExists(posX.toInt(), posY.toInt(), posZ.toInt()))) {
			setDead()
		} else {
			moveEntity(motionX, motionY, motionZ)
			
			if (!ASJUtilities.isServer) return
			super.onUpdate()
			
			if (ticksExisted == SpellDriftingMine.duration) onImpact(null)
			
			val vec3 = Vec3.createVectorHelper(posX, posY, posZ)
			val vec31 = Vec3.createVectorHelper(posX + motionX, posY + motionY, posZ + motionZ)
			var movingobjectposition: MovingObjectPosition? = worldObj.rayTraceBlocks(vec3, vec31)
			
			if (movingobjectposition == null) {
				val l = worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, boundingBox.addCoord(motionX, motionY, motionZ).expand(1.0, 1.0, 1.0)) as  MutableList<EntityLivingBase>
				l.remove(caster)
				
				for (e in l)
					if (e.canBeCollidedWith() && !PartySystem.mobsSameParty(caster, e) && Vector3.entityDistance(this, e) < 3) {
						movingobjectposition = MovingObjectPosition(e)
						break
					}
			}
			
			if (movingobjectposition != null) onImpact(movingobjectposition)
			
			val f1 = MathHelper.sqrt_double(motionX * motionX + motionZ * motionZ)
			rotationYaw = (atan2(motionZ, motionX) * 180.0 / Math.PI).toFloat() + 90.0f
			
			rotationPitch = (atan2(f1.toDouble(), motionY) * 180.0 / Math.PI).toFloat() - 90.0f
			while (rotationPitch - prevRotationPitch < -180.0f) prevRotationPitch -= 360.0f
			while (rotationPitch - prevRotationPitch >= 180.0f) prevRotationPitch += 360.0f
			while (rotationYaw - prevRotationYaw < -180.0f) prevRotationYaw -= 360.0f
			while (rotationYaw - prevRotationYaw >= 180.0f) prevRotationYaw += 360.0f
			
			rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch) * 0.2f
			rotationYaw = prevRotationYaw + (rotationYaw - prevRotationYaw) * 0.2f
			
		}
	}
	
	override fun canBeCollidedWith() = true
	
	override fun getCollisionBorderSize() = 0.5f
	
	@SideOnly(Side.CLIENT)
	override fun getShadowSize() = 0.0f
	
	override fun affectedBy(uuid: UUID) = caster!!.uniqueID != uuid
	
	public override fun entityInit() {}
	
	public override fun readEntityFromNBT(nbt: NBTTagCompound) {
		if (nbt.hasKey("castername")) caster = worldObj.getPlayerEntityByName(nbt.getString("castername")) else setDead()
		if (caster == null) setDead()
	}
	
	public override fun writeEntityToNBT(nbt: NBTTagCompound) {
		if (caster is EntityPlayer) nbt.setString("castername", caster!!.commandSenderName)
	}
}