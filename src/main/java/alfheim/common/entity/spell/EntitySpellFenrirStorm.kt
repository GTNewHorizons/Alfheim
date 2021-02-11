package alfheim.common.entity.spell

import alexsocol.asjlib.*
import alexsocol.asjlib.math.*
import alfheim.api.spell.*
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.util.DamageSourceSpell
import alfheim.common.spell.wind.SpellFenrirStorm
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.AxisAlignedBB
import net.minecraft.world.World
import java.util.*

class EntitySpellFenrirStorm(world: World): Entity(world), ITimeStopSpecific {
	
	val area: OrientedBB
	var caster: EntityLivingBase? = null
	var mjolnir: Boolean
		get() = dataWatcher.getWatchableObjectInt(2) != 0
		set(value) = dataWatcher.updateObject(2, if (value) 1 else 0)
	
	override val isImmune: Boolean
		get() = false
	
	init {
		setSize(0.25f, 0.25f)
		area = OrientedBB(AxisAlignedBB.getBoundingBox(-0.5, -0.5, -SpellFenrirStorm.radius, 0.5, 0.5, SpellFenrirStorm.radius))
		renderDistanceWeight = SpellFenrirStorm.radius / 2
	}
	
	constructor(world: World, caster: EntityLivingBase, mjolnir: Boolean = false): this(world) {
		this.caster = caster
		val l = Vector3(caster.lookVec).mul(0.1)
		setPositionAndRotation(caster.posX + l.x, caster.posY + caster.eyeHeight.D + l.y, caster.posZ + l.z, caster.rotationYaw, caster.rotationPitch)
		
		area.translate(caster.posX, caster.posY + caster.eyeHeight, caster.posZ)
		area.rotateOX(-caster.rotationPitch.D)
		area.rotateOY((caster.rotationYaw).D)
		
		val v = Vector3(caster.lookVec).mul(SpellFenrirStorm.radius + 0.5)
		area.translate(v.x, v.y, v.z)
		
		this.mjolnir = mjolnir
	}
	
	override fun onEntityUpdate() {
		if ((!AlfheimConfigHandler.enableMMO && !mjolnir) || (!worldObj.isRemote && caster == null) || ticksExisted > 12) {
			setDead()
			return
		}
		if (isDead || ASJUtilities.isClient) return
		
		val caster = caster ?: return
		
		if (mjolnir) {
			rotationYaw = caster.rotationYaw
			rotationPitch = caster.rotationPitch
			
			area.fromAABB(AxisAlignedBB.getBoundingBox(-0.5, -0.5, -SpellFenrirStorm.radius, 0.5, 0.5, SpellFenrirStorm.radius))
			area.translate(caster.posX, caster.posY + caster.eyeHeight, caster.posZ)
			area.rotateOX(-caster.rotationPitch.D)
			area.rotateOY((caster.rotationYaw).D)
			
			val v = Vector3(caster.lookVec).mul(SpellFenrirStorm.radius + 0.5)
			area.translate(v.x, v.y, v.z)
		}
		
		boundingBox.setBB(area.toAABB())
		
		if (ticksExisted == 4 || mjolnir) {
			val l = worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, area.toAABB()) as List<EntityLivingBase>
			for (e in l)
				if (e !== caster && area.intersectsWith(e.boundingBox))
					e.attackEntityFrom(DamageSourceSpell.lightning(this, caster), SpellBase.over(caster, SpellFenrirStorm.damage.D))
		}
	}
	
	override fun affectedBy(uuid: UUID) = caster!!.uniqueID != uuid
	
	public override fun entityInit() {
		dataWatcher.addObject(2, 0)
	}
	
	public override fun readEntityFromNBT(nbt: NBTTagCompound) {
		if (nbt.hasKey("castername")) caster = worldObj.getPlayerEntityByName(nbt.getString("castername")) else setDead()
		if (caster == null) setDead()
	}
	
	public override fun writeEntityToNBT(nbt: NBTTagCompound) {
		if (caster is EntityPlayer) nbt.setString("castername", caster!!.commandSenderName)
	}
}