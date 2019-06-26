package alfheim.common.entity.spell

import java.util.UUID

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.api.spell.ITimeStopSpecific
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.SpellEffectHandlerClient.Spells
import alfheim.common.core.handler.SpellEffectHandler
import alfheim.common.core.util.DamageSourceSpell
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.MovingObjectPosition
import net.minecraft.util.MovingObjectPosition.MovingObjectType
import net.minecraft.world.World

class EntitySpellAquaStream(world: World): Entity(world), ITimeStopSpecific {
	
	var caster: EntityLivingBase? = null
	
	override val isImmune: Boolean
		get() = false
	
	init {
		setSize(1f, 1f)
	}
	
	constructor(world: World, caster: EntityLivingBase): this(world) {
		this.caster = caster
		setPosition(caster.posX, caster.posY, caster.posZ)
	}
	
	override fun onEntityUpdate() {
		if (!AlfheimCore.enableMMO || caster == null || caster!!.isDead || caster!!.posX != posX || caster!!.posY != posY || caster!!.posZ != posZ || ticksExisted > 50) {
			setDead()
			return
		}
		if (this.isDead || !ASJUtilities.isServer) return
		
		var mop = ASJUtilities.getMouseOver(caster, 16.0, true)
		if (mop == null) mop = ASJUtilities.getSelectedBlock(caster, 16.0, true)
		
		val hp: Vector3
		val look = Vector3(caster!!.lookVec)
		if (mop != null && mop.hitVec != null) {
			hp = Vector3(mop.hitVec)
			if (mop.typeOfHit == MovingObjectType.ENTITY) {
				mop.entityHit.attackEntityFrom(DamageSourceSpell.water(caster), SpellBase.over(caster, 1.0))
			}
		} else {
			hp = look.copy().extend(15.0).add(Vector3.fromEntity(caster!!)).add(0.0, caster!!.eyeHeight.toDouble(), 0.0)
		}
		
		val d = 0.75
		SpellEffectHandler.sendPacket(Spells.AQUASTREAM, dimension, look.x + caster!!.posX, look.y + caster!!.posY + caster!!.eyeHeight.toDouble(), look.z + caster!!.posZ, look.x / d, look.y / d, look.z / d)
		SpellEffectHandler.sendPacket(Spells.AQUASTREAM_HIT, dimension, hp.x, hp.y, hp.z)
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