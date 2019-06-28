package alexsocol.asjlib

import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.*

class FreeTeleporter(internal val world: WorldServer, internal val x: Double, internal val y: Double, internal val z: Double): Teleporter(world) {
	
	override fun placeInExistingPortal(entity: Entity, x: Double, y: Double, z: Double, rotationYaw: Float): Boolean {
		entity.posX = this.x
		entity.posY = this.y
		entity.posZ = this.z
		if (entity is EntityPlayer) {
			if (entity.capabilities.allowFlying) entity.capabilities.isFlying = true
		}
		return true
	}
}