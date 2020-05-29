package alfheim.common.entity.spell

import alexsocol.asjlib.*
import alexsocol.asjlib.math.*
import alfheim.AlfheimCore
import alfheim.api.spell.*
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.util.DamageSourceSpell
import alfheim.common.security.InteractionSecurity
import alfheim.common.spell.wind.SpellFenrirStorm
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.AxisAlignedBB
import net.minecraft.world.World
import java.util.*

class EntitySpellFenrirStorm(world: World): Entity(world), ITimeStopSpecific {
	
	val area: OrientedBB?
	var caster: EntityLivingBase? = null
	
	override val isImmune: Boolean
		get() = false
	
	init {
		setSize((SpellFenrirStorm.radius * 4).F, (SpellFenrirStorm.radius * 4).F)
		area = OrientedBB(AxisAlignedBB.getBoundingBox(-0.5, -0.5, -SpellFenrirStorm.radius, 0.5, 0.5, SpellFenrirStorm.radius))
		renderDistanceWeight = SpellFenrirStorm.radius / 2
	}
	
	constructor(world: World, caster: EntityLivingBase): this(world) {
		this.caster = caster
		val l = Vector3(caster.lookVec).mul(0.1)
		setPositionAndRotation(caster.posX + l.x, caster.posY + caster.eyeHeight.D + l.y, caster.posZ + l.z, caster.rotationYaw, caster.rotationPitch)
		
		area!!.translate(caster.posX, caster.posY + caster.eyeHeight, caster.posZ)
		area.rotateOX(-caster.rotationPitch.D)
		area.rotateOY((caster.rotationYaw).D)
		
		val v = Vector3(caster.lookVec).mul(SpellFenrirStorm.radius + 0.5)
		area.translate(v.x, v.y, v.z)
	}
	
	override fun onEntityUpdate() {
		if (!AlfheimConfigHandler.enableMMO || !worldObj.isRemote && (caster == null || area == null || ticksExisted > 12)) {
			setDead()
			return
		}
		if (isDead || !ASJUtilities.isServer) return
		
		if (ticksExisted == 4) {
			val l = worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, area!!.toAABB()) as List<EntityLivingBase>
			for (e in l) if (e !== caster && area.intersectsWith(e.boundingBox) && InteractionSecurity.canHurtEntity(caster ?: continue, e)) e.attackEntityFrom(DamageSourceSpell.lightning(this, caster), SpellBase.over(caster, SpellFenrirStorm.damage.D))
		}
	}
	
	override fun affectedBy(uuid: UUID) = caster!!.uniqueID != uuid
	
	public override fun entityInit() = Unit
	
	public override fun readEntityFromNBT(nbt: NBTTagCompound) {
		if (nbt.hasKey("castername")) caster = worldObj.getPlayerEntityByName(nbt.getString("castername")) else setDead()
		if (caster == null) setDead()
	}
	
	public override fun writeEntityToNBT(nbt: NBTTagCompound) {
		if (caster is EntityPlayer) nbt.setString("castername", caster!!.commandSenderName)
	}
}