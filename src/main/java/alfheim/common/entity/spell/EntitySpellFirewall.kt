package alfheim.common.entity.spell

import alexsocol.asjlib.math.*
import alfheim.AlfheimCore
import alfheim.api.spell.*
import alfheim.common.core.handler.CardinalSystem.PartySystem
import alfheim.common.core.util.*
import cpw.mods.fml.relauncher.*
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.*
import net.minecraft.world.World
import vazkii.botania.common.Botania

import java.util.*

class EntitySpellFirewall(world: World): Entity(world), ITimeStopSpecific {
	
	var caster: EntityLivingBase? = null
	var obb: OrientedBB? = null
	
	override val isImmune: Boolean
		get() = false
	
	init {
		this.setSize(0f, 0f)
	}
	
	constructor(world: World, caster: EntityLivingBase): this(world) {
		this.caster = caster
		val v = Vector3(caster.lookVec)
		v.set(v.x, 0.0, v.z).normalize().mul(5.0)
		setLocationAndAngles(caster.posX + v.x, caster.posY, caster.posZ + v.z, caster.rotationYaw, caster.rotationPitch)
	}
	
	constructor(world: World, x: Double, y: Double, z: Double): this(world) {
		setLocationAndAngles(x, y, z, rotationYaw, rotationPitch)
	}
	
	override fun attackEntityFrom(source: DamageSource?, damage: Float): Boolean {
		return false
	}
	
	override fun onUpdate() {
		if (!AlfheimCore.enableMMO || !worldObj.isRemote && caster != null && caster!!.isDead) {
			setDead()
		} else {
			//if (!ASJUtilities.isServer()) return;
			super.onUpdate()
			
			if (ticksExisted >= 600) {
				this.setDead()
				return
			}
			
			if (obb == null) {
				obb = OrientedBB(AxisAlignedBB.getBoundingBox(-3.0, -1.0, -0.5, 3.0, 4.0, 0.5)).translate(posX, posY, posZ).rotateOY(rotationYaw.toDouble())
			}
			
			val list = worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, obb!!.toAABB()) as List<EntityLivingBase>
			for (e in list) {
				if (e !== caster && obb!!.intersectsWith(e.boundingBox)) {
					e.attackEntityFrom(DamageSourceSpell.firewall(this, caster), SpellBase.over(caster, 1.0))
					if (!PartySystem.mobsSameParty(caster, e) || AlfheimConfig.frienldyFire) e.setFire(3)
				}
			}
			
			val a = obb!!.a.copy().sub(obb!!.pos).rotate((rotationYaw * -2).toDouble(), Vector3.oY).add(obb!!.pos)
			val b = obb!!.b.copy().sub(obb!!.pos).rotate((rotationYaw * -2).toDouble(), Vector3.oY).add(obb!!.pos)
			val d = obb!!.d.copy().sub(obb!!.pos).rotate((rotationYaw * -2).toDouble(), Vector3.oY).add(obb!!.pos)
			val v = d.copy().sub(d.copy().sub(a).mul(0.5))
			
			val sources = 20
			val power = 5
			for (i in 0 until sources) {
				v.sub(a.copy().sub(b.copy()).mul(1.0 / sources))
				for (j in 0 until power) Botania.proxy.wispFX(worldObj, v.x + Math.random() * 0.5, v.y + Math.random() * 2, v.z + Math.random() * 0.5, 1f, Math.random().toFloat() * 0.25f, Math.random().toFloat() * 0.075f, 0.5f + Math.random().toFloat() * 0.5f, -0.15f, Math.random().toFloat() * 1.5f)
			}
		}
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