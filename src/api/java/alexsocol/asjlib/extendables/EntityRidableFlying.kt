package alexsocol.asjlib.extendables

import alexsocol.asjlib.*
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.MathHelper
import net.minecraft.world.World
import kotlin.math.*

abstract class EntityRidableFlying(world: World): EntityRidable(world) {
	
	var flySpeed = 0.85f
	
	override fun fall(f: Float) = Unit // NO-OP
	
	override fun moveEntityWithHeading(mS: Float, mF: Float) {
		onGround = !worldObj.isAirBlock(posX.mfloor(), MathHelper.ceiling_double_int(posY - 1.0), posZ.mfloor())
		
		if (rider != null) {
			rotationYaw = riddenByEntity.rotationYaw
			prevRotationYaw = rotationYaw
			rotationPitch = riddenByEntity.rotationPitch * 0.5f
			setRotation(rotationYaw, rotationPitch)
			renderYawOffset = rotationYaw
			rotationYawHead = renderYawOffset
			if (onGround && !isJumping) {
				if (!worldObj.isRemote) {
					super.moveEntityWithHeading(mS, mF)
				}
			} else if (riddenByEntity is EntityLivingBase) {
				var par2 = (riddenByEntity as EntityLivingBase).moveForward
				
				aiMoveSpeed = flySpeed
				if (!worldObj.isRemote) {
					if (isJumping) {
						motionY = flySpeed * 0.5
					} else if (!onGround) {
						motionY = 0.0
					}
					
					if (par2 != 0f && !isJumping) {
						val rad = Math.toRadians(rider!!.rotationPitch.D)
						motionY = -sin(rad) * flySpeed
						
						par2 = abs(cos(rad)).F * flySpeed
						if (par2 < 0.05) par2 = 0f
					}
					
					val f2 = flySpeed
					val f3 = 0.1627714f / (f2 * f2 * f2)
					moveFlying(0f, par2, if (onGround) 0.1f * f3 else 0.085f)
					moveEntity(motionX, motionY, motionZ)
					motionX *= f2
					motionY *= f2
					motionZ *= f2
				}
			}
		} else if (!worldObj.isRemote) {
			super.moveEntityWithHeading(mS, mF)
		}
		
		prevLimbSwingAmount = limbSwingAmount
		val var10 = posX - prevPosX
		val var9 = posZ - prevPosZ
		var var7 = sqrt(var10 * var10 + var9 * var9).F * 4f
		if (var7 > 1f) {
			var7 = 1f
		}
		limbSwingAmount += (var7 - limbSwingAmount) * 0.4f
		limbSwing += limbSwingAmount
	}
}