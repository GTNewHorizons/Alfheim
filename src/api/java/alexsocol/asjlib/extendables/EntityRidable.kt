package alexsocol.asjlib.extendables

import alexsocol.asjlib.*
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.Potion
import net.minecraft.util.DamageSource
import net.minecraft.world.World
import net.minecraftforge.common.ForgeHooks
import kotlin.math.sqrt

@Suppress("LeakingThis")
abstract class EntityRidable(world: World): EntityCreature(world) {
	
	var rider: EntityPlayer? = null
	var walkSpeed = 0.25
	
	override fun attackEntityFrom(src: DamageSource, dmg: Float) = if (rider != null && src.entity === rider) false else super.attackEntityFrom(src, dmg)
	
	override fun applyEntityAttributes() {
		super.applyEntityAttributes()
		getEntityAttribute(SharedMonsterAttributes.maxHealth).baseValue = 36.0
		getEntityAttribute(SharedMonsterAttributes.movementSpeed).baseValue = walkSpeed
	}
	
	override fun onLivingUpdate() {
		rider = if (riddenByEntity != null && riddenByEntity is EntityPlayer) riddenByEntity as EntityPlayer else null
		
		if (rider != null && rider!!.isJumping) jumpHelper.setJumping()
		
		super.onLivingUpdate()
	}
	
	override fun canBeCollidedWith() = !isDead
	
	override fun canBePushed() = riddenByEntity == null
	
	override fun getShadowSize() = 0f
	
	override fun canDespawn() = false
	
	override fun interact(player: EntityPlayer?): Boolean {
		if (worldObj.isRemote || player == null) return false
		
		return if (rider == null) {
			mount(player)
			if (riddenByEntity != null && riddenByEntity is EntityPlayer) rider = riddenByEntity as EntityPlayer
			true
		} else true
	}
	
	override fun moveEntityWithHeading(mS: Float, mF: Float) {
		var par1 = mS
		var par2 = mF
		if (rider != null) {
			rotationYaw = rider!!.rotationYaw
			prevRotationYaw = rotationYaw
			rotationPitch = rider!!.rotationPitch * 0.5f
			setRotation(rotationYaw, rotationPitch)
			renderYawOffset = rotationYaw
			rotationYawHead = renderYawOffset
			par1 = rider!!.moveStrafing * 0.5f
			par2 = rider!!.moveForward
			if (!worldObj.isRemote) {
				this.aiMoveSpeed = walkSpeed.F
				super.moveEntityWithHeading(par1, par2)
			} else {
				super.moveEntityWithHeading(par1, par2)
			}
			
			prevLimbSwingAmount = limbSwingAmount
			val d0 = posX - prevPosX
			val d1 = posZ - prevPosZ
			var f4 = sqrt(d0 * d0 + d1 * d1).F * 4f
			if (f4 > 1f) {
				f4 = 1f
			}
			
			limbSwingAmount += (f4 - limbSwingAmount) * 0.4f
			limbSwing += limbSwingAmount
		} else {
			super.moveEntityWithHeading(par1, par2)
		}
	}
	
	override fun jump() {
		motionY = 0.5
		
		getActivePotionEffect(Potion.jump.id)?.let {
			motionY += (it.amplifier + 1) * 0.1
		}
		
		isAirBorne = true
		ForgeHooks.onLivingJump(this)
	}
	
	open fun mount(player: EntityPlayer) {
		player.rotationYaw = rotationYaw
		player.rotationPitch = rotationPitch
		if (!worldObj.isRemote) {
			player.mountEntity(this)
		}
	}
	
	override fun shouldDismountInWater(rider: Entity?) = false
	
	override fun isSprinting() = if (rider == null) false else rider!!.moveForward > 0.5f
}
