package alfheim.common.entity.spell

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.api.spell.ITimeStopSpecific
import alfheim.client.render.world.SpellEffectHandlerClient.Spells
import alfheim.common.core.handler.CardinalSystem.PartySystem
import alfheim.common.core.handler.SpellEffectHandler
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.potion.*
import net.minecraft.world.World
import java.util.*

class EntitySpellHarp(world: World): Entity(world), ITimeStopSpecific {
	
	var caster: EntityLivingBase? = null
	
	override val isImmune: Boolean
		get() = false
	
	init {
		setSize(0.1f, 0.1f)
	}
	
	constructor(world: World, caster: EntityLivingBase, x: Double, y: Double, z: Double): this(world) {
		setPosition(x, y, z)
		this.caster = caster
	}
	
	override fun onUpdate() {
		if (!AlfheimCore.enableMMO || !worldObj.isRemote && (caster == null || caster!!.isDead)) {
			setDead()
		} else {
			if (!ASJUtilities.isServer) return
			super.onUpdate()
			
			if (ticksExisted >= 600) setDead()
			
			val pt = PartySystem.getMobParty(caster)
			if (pt == null || pt.count == 0) {
				setDead()
				return
			}
			
			if (worldObj.rand.nextInt() % (20 / pt.count) == 0) {
				var mr = pt[worldObj.rand.nextInt(pt.count)] ?: return
				if (Vector3.entityDistance(this, mr) > 16) return
				mr.heal(0.5f)
				
				mr = pt[worldObj.rand.nextInt(pt.count)] ?: return
				for (o in mr.activePotionEffects) {
					if (Potion.potionTypes[(o as PotionEffect).potionID].isBadEffect) {
						mr.removePotionEffect(o.potionID)
						break
					}
				}
				
				SpellEffectHandler.sendPacket(Spells.NOTE, this)
			}
		}
	}
	
	override fun affectedBy(uuid: UUID) = caster?.uniqueID != uuid
	
	public override fun entityInit() {}
	
	public override fun readEntityFromNBT(nbt: NBTTagCompound) {
		if (nbt.hasKey("castername")) caster = worldObj.getPlayerEntityByName(nbt.getString("castername")) else setDead()
		if (caster == null) setDead()
	}
	
	public override fun writeEntityToNBT(nbt: NBTTagCompound) {
		if (caster is EntityPlayer) nbt.setString("castername", caster!!.commandSenderName)
	}
}