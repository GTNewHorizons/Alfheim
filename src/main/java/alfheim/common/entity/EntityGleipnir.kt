package alfheim.common.entity

import alexsocol.asjlib.ASJUtilities
import alfheim.common.core.handler.*
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
	
	override fun onUpdate() {
		super.onUpdate()
		val thrower = thrower
		
		if (thrower == null || !thrower.isEntityAlive || ticksExisted > 300) {
			setDead()
			return
		}
		
		val targets = worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, boundingBox) as MutableList<EntityLivingBase>
		targets.remove(thrower)
		
		if (AlfheimConfigHandler.enableMMO && ASJUtilities.isServer) {
			val pt = CardinalSystem.PartySystem.getParty(thrower)
			targets.removeAll { pt.isMember(it) }
		}
		
		targets.forEach { it.addPotionEffect (PotionEffect(AlfheimConfigHandler.potionIDEternity, 5, 1, true)) }
	}
	
	override fun entityInit() = Unit
	override fun writeEntityToNBT(nbt: NBTTagCompound?) = Unit
	override fun readEntityFromNBT(nbt: NBTTagCompound?) = Unit
}
