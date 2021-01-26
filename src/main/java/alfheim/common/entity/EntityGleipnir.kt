package alfheim.common.entity

import alexsocol.asjlib.security.InteractionSecurity
import alfheim.common.core.handler.*
import cpw.mods.fml.relauncher.*
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.potion.PotionEffect
import net.minecraft.world.World

class EntityGleipnir: Entity {
	
	var thrower: EntityPlayer? = null
	
	constructor(world: World): super(world) {
		setSize(10f, 10f)
	}
	
	constructor(world: World, player: EntityPlayer): this (world) {
		thrower = player
	}
	
	@SideOnly(Side.CLIENT)
	override fun setPositionAndRotation2(x: Double, y: Double, z: Double, yaw: Float, pitch: Float, nope: Int) {
		setPosition(x, y, z)
		setRotation(yaw, pitch)
		// fuck you "push out of blocks"!
	}
	
	override fun onUpdate() {
		super.onUpdate()
		val thrower = thrower
		
		if ((!worldObj.isRemote && (thrower == null || !thrower.isEntityAlive)) || ticksExisted > 300) {
			setDead()
			return
		}
		
		thrower ?: return
		if (worldObj.isRemote) return
		
		val targets = worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, boundingBox) as MutableList<EntityLivingBase>
		targets.remove(thrower)
		
		targets.removeAll { !InteractionSecurity.canDoSomethingWithEntity(thrower, it) }
		
		if (AlfheimConfigHandler.enableMMO) {
			val pt = CardinalSystem.PartySystem.getParty(thrower)
			targets.removeAll { pt.isMember(it) }
		}
		
		targets.forEach { it.addPotionEffect (PotionEffect(AlfheimConfigHandler.potionIDEternity, 5, 1, true)) }
	}
	
	override fun entityInit() = Unit
	override fun writeEntityToNBT(nbt: NBTTagCompound?) = Unit
	override fun readEntityFromNBT(nbt: NBTTagCompound?) = Unit
}
