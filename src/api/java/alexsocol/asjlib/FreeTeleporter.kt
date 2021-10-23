package alexsocol.asjlib

import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.*

class FreeTeleporter(val world: WorldServer, val x: Double, val y: Double, val z: Double): Teleporter(world) {
	
	override fun placeInExistingPortal(entity: Entity, i: Double, j: Double, k: Double, rotationYaw: Float): Boolean {
		entity.posX = x
		entity.posY = y
		entity.posZ = z
		if (entity is EntityPlayer && entity.capabilities.allowFlying) entity.capabilities.isFlying = true
		return true
	}
}