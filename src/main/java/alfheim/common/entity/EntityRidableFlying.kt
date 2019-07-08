package alfheim.common.entity

import net.minecraft.entity.EntityLivingBase
import net.minecraft.server.MinecraftServer
import net.minecraft.util.MathHelper
import net.minecraft.world.World

open class EntityRidableFlying(world: World): EntityRidable(world) {
	
	var flySpeed = 0.91f
	
	init {
		walkSpeed = 0.14f
	}
	
	override fun fall(f: Float) = Unit // NO-OP
	
	override fun moveEntityWithHeading(mS: Float, mF: Float) {
		var par1 = mS
		var par2 = mF
		onGround = !worldObj.isAirBlock(MathHelper.floor_double(posX), MathHelper.ceiling_double_int(posY - 1.0), MathHelper.floor_double(posZ))
		var bo = !worldObj.gameRules.getGameRuleBooleanValue("animalbikesFlying")
		if (!worldObj.isRemote && !MinecraftServer.getServer().worldServerForDimension(0).gameRules.getGameRuleBooleanValue("animalbikesFlying")) {
			bo = true
		}
		if (rider == null || bo) {
			if (!worldObj.isRemote) {
				super.moveEntityWithHeading(par1, par2)
			}
		} else {
			rotationYaw = riddenByEntity.rotationYaw
			prevRotationYaw = rotationYaw
			rotationPitch = riddenByEntity.rotationPitch * 0.5f
			setRotation(rotationYaw, rotationPitch)
			renderYawOffset = rotationYaw
			rotationYawHead = renderYawOffset
			if (onGround && !isJumping) {
				if (!worldObj.isRemote) {
					super.moveEntityWithHeading(par1, par2)
				}
			} else if (riddenByEntity is EntityLivingBase) {
				par1 = (riddenByEntity as EntityLivingBase).moveStrafing * 0.5f
				par2 = (riddenByEntity as EntityLivingBase).moveForward
				
				aiMoveSpeed = flySpeed
				if (!worldObj.isRemote) {
					if (isJumping) {
						motionY = (0.5f * flySpeed).toDouble()
					} else if (!onGround) {
						motionY = (-0.2f * flySpeed).toDouble()
					}
					val f2 = flySpeed
					val f3 = 0.1627714f / (f2 * f2 * f2)
					moveFlying(par1, par2, if (onGround) 0.1f * f3 else 0.085f)
					moveEntity(motionX, motionY, motionZ)
					motionX *= f2.toDouble()
					motionY *= f2.toDouble()
					motionZ *= f2.toDouble()
				}
			}
		}
		prevLimbSwingAmount = limbSwingAmount
		val var10 = posX - prevPosX
		val var9 = posZ - prevPosZ
		var var7 = MathHelper.sqrt_double(var10 * var10 + var9 * var9) * 4.0f
		if (var7 > 1.0f) {
			var7 = 1.0f
		}
		limbSwingAmount += (var7 - limbSwingAmount) * 0.4f
		limbSwing += limbSwingAmount
	}
}
