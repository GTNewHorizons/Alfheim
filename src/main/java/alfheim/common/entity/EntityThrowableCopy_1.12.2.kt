package alfheim.common.entity

import alexsocol.asjlib.D
import net.minecraft.entity.Entity
import net.minecraft.util.MathHelper
import vazkii.botania.common.entity.EntityThrowableCopy

fun EntityThrowableCopy.shoot(entityThrower: Entity, rotationPitchIn: Float, rotationYawIn: Float, pitchOffset: Float, velocity: Float, inaccuracy: Float) {
	val f = -MathHelper.sin(rotationYawIn * 0.017453292f) * MathHelper.cos(rotationPitchIn * 0.017453292f)
	val f1 = -MathHelper.sin((rotationPitchIn + pitchOffset) * 0.017453292f)
	val f2 = MathHelper.cos(rotationYawIn * 0.017453292f) * MathHelper.cos(rotationPitchIn * 0.017453292f)
	setThrowableHeading(f.D, f1.D, f2.D, velocity, inaccuracy)
	motionX += entityThrower.motionX
	motionZ += entityThrower.motionZ
	
	if (!entityThrower.onGround) {
		motionY += entityThrower.motionY
	}
}