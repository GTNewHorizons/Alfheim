package alfmod.common.core.asm

import alfmod.common.core.handler.WRATH_OF_THE_WINTER
import alfmod.common.entity.EntitySnowSprite
import gloomyfolken.hooklib.asm.Hook
import net.minecraft.entity.projectile.EntitySnowball
import net.minecraft.util.MovingObjectPosition

object AlfheimModularHookHandler {
	
	@JvmStatic
	@Hook(injectOnExit = true)
	fun onImpact(ball: EntitySnowball, mop: MovingObjectPosition) {
		if (WRATH_OF_THE_WINTER) {
			if (!ball.worldObj.isRemote && mop.entityHit == null && ball.worldObj.isRaining && ball.worldObj.rand.nextInt(32) == 0)
				EntitySnowSprite(ball.worldObj).also {
					it.setPosition(ball.posX, ball.posY + 1, ball.posZ)
				}.also {
					if (it.canSpawnHere)
						ball.worldObj.spawnEntityInWorld(it)
				}
		}
	}
}