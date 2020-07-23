package alfheim.common.entity.spell

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alfheim.api.spell.*
import alfheim.client.render.world.VisualEffectHandlerClient.VisualEffects
import alfheim.common.core.handler.*
import alfheim.common.core.handler.CardinalSystem.PartySystem
import alfheim.common.core.util.DamageSourceSpell
import alfheim.common.security.InteractionSecurity
import alfheim.common.spell.tech.SpellGravityTrap
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.AxisAlignedBB
import net.minecraft.world.World
import java.util.*

class EntitySpellGravityTrap @JvmOverloads constructor(world: World, var caster: EntityLivingBase? = null, x: Double = 0.0, y: Double = 0.0, z: Double = 0.0): Entity(world), ITimeStopSpecific {
	
	override val isImmune: Boolean
		get() = false
	
	init {
		setPosition(x, y, z)
		setSize(SpellGravityTrap.radius.F * 2, 0.01f)
	}
	
	override fun onEntityUpdate() {
		if (!AlfheimConfigHandler.enableMMO || ticksExisted > SpellGravityTrap.duration) {
			setDead()
			return
		}
		if (isDead || ticksExisted < 20 || ASJUtilities.isClient) return
		
		val l = worldObj.getEntitiesWithinAABB(Entity::class.java, AxisAlignedBB.getBoundingBox(posX, posY + 8, posZ, posX, posY + 8, posZ).expand(SpellGravityTrap.radius, 9.0, SpellGravityTrap.radius)) as List<Entity>
		for (e in l) {
			if (e === this || e === caster || e is EntityLivingBase && PartySystem.mobsSameParty(caster, e) && !AlfheimConfigHandler.frienldyFire || e is EntityPlayer && e.capabilities.isCreativeMode) continue
			if (!InteractionSecurity.canDoSomethingWithEntity(caster ?: continue, e)) continue
			
			val dist = Vector3.fromEntity(e).sub(Vector3.fromEntity(this))
			if (Vector3.entityDistancePlane(e, this) <= SpellGravityTrap.radius) {
				e.motionY -= 1.0
				e.motionX -= dist.x / 5
				e.motionZ -= dist.z / 5
				e.attackEntityFrom(DamageSourceSpell.gravity(this, caster), SpellBase.over(caster, SpellGravityTrap.damage.D))
			}
		}
		
		if (worldObj.rand.nextBoolean()) {
			val p = Vector3(posX, posY, posZ).add(Math.random() * 8.0 - 4.0, Math.random() * 3.5, Math.random() * 8.0 - 4.0)
			val m = Vector3.fromEntity(this).sub(p).mul(0.05)
			VisualEffectHandler.sendPacket(VisualEffects.GRAVITY, dimension, p.x, p.y, p.z, m.x, m.y, m.z)
		}
	}
	
	override fun affectedBy(uuid: UUID) = caster!!.uniqueID != uuid
	
	public override fun entityInit() = Unit
	
	public override fun readEntityFromNBT(nbt: NBTTagCompound) {
		if (nbt.hasKey("castername")) caster = worldObj.getPlayerEntityByName(nbt.getString("castername")) else setDead()
		if (caster == null) setDead()
		ticksExisted = nbt.getInteger("ticksExisted")
	}
	
	public override fun writeEntityToNBT(nbt: NBTTagCompound) {
		if (caster is EntityPlayer) nbt.setString("castername", caster!!.commandSenderName)
		nbt.setInteger("ticksExisted", ticksExisted)
	}
}