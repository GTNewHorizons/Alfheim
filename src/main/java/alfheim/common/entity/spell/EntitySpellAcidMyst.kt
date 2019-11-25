package alfheim.common.entity.spell

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.api.spell.*
import alfheim.client.render.world.VisualEffectHandlerClient.VisualEffects
import alfheim.common.core.handler.CardinalSystem.PartySystem
import alfheim.common.core.handler.VisualEffectHandler
import alfheim.common.core.util.*
import alfheim.common.spell.water.SpellAcidMyst
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.AxisAlignedBB
import net.minecraft.world.World

import java.util.*

class EntitySpellAcidMyst(world: World): Entity(world), ITimeStopSpecific {
	
	var caster: EntityLivingBase? = null
	
	override val isImmune: Boolean
		get() = false
	
	init {
		setSize(1f, 1f)
	}
	
	constructor(world: World, caster: EntityLivingBase): this(world) {
		this.caster = caster
		setPosition(caster.posX, caster.posY, caster.posZ)
		VisualEffectHandler.sendPacket(VisualEffects.ACID, this)
	}
	
	override fun onEntityUpdate() {
		if (!AlfheimCore.enableMMO || caster == null || caster!!.isDead || ticksExisted > SpellAcidMyst.duration) {
			setDead()
			return
		}
		if (isDead || !ASJUtilities.isServer) return
		
		val l = worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, AxisAlignedBB.getBoundingBox(posX, posY, posZ, posX, posY, posZ).expand(SpellAcidMyst.radius)) as MutableList<EntityLivingBase>
		l.remove(caster!!)
		for (e in l) if (!PartySystem.mobsSameParty(caster!!, e) && Vector3.entityDistance(caster!!, e) <= SpellAcidMyst.radius) e.attackEntityFrom(DamageSourceSpell.poison, SpellBase.over(caster, SpellAcidMyst.damage.toDouble()))
	}
	
	fun getTopBlock(world: World, x: Int, z: Int): Int {
		var y = 255
		while (y > 0 && world.isAirBlock(x, y, z)) --y
		return y
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