package alfheim.common.entity.spell

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.api.spell.*
import alfheim.client.render.world.VisualEffectHandlerClient.VisualEffects
import alfheim.common.core.handler.*
import alfheim.common.core.handler.CardinalSystem.PartySystem
import alfheim.common.core.util.DamageSourceSpell
import alfheim.common.security.InteractionSecurity
import alfheim.common.spell.water.SpellAcidMyst
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
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
		if (!AlfheimConfigHandler.enableMMO || caster == null || caster!!.isDead || ticksExisted > SpellAcidMyst.duration) {
			setDead()
			return
		}
		if (isDead || !ASJUtilities.isServer) return
		
		if (ticksExisted % 20 == 0) VisualEffectHandler.sendPacket(VisualEffects.ACID, this)
		
		val l = worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, alexsocol.asjlib.getBoundingBox(posX, posY, posZ).expand(SpellAcidMyst.radius)) as MutableList<EntityLivingBase>
		l.remove(caster!!)
		for (e in l)
			if (!PartySystem.mobsSameParty(caster!!, e) && Vector3.entityDistance(caster!!, e) <= SpellAcidMyst.radius && InteractionSecurity.canHurtEntity(caster ?: continue, e))
				e.attackEntityFrom(DamageSourceSpell.poison, SpellBase.over(caster, SpellAcidMyst.damage.D))
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