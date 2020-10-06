package alfheim.common.core.handler

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.client.render.world.VisualEffectHandlerClient.VisualEffects
import alfheim.common.network.MessageVisualEffect
import net.minecraft.entity.Entity

object VisualEffectHandler {
	
	fun sendPacket(s: VisualEffects, e: Entity) {
		sendPacket(s, e.dimension, e.posX, e.posY, e.posZ)
	}
	
	fun sendPacket(s: VisualEffects, dimension: Int, vararg data: Double) {
		if (ASJUtilities.isServer) AlfheimCore.network.sendToDimension(MessageVisualEffect(s.ordinal, *data), dimension)
	}
	
	fun sendError(dim: Int, x: Int, y: Int, z: Int) {
		sendPacket(VisualEffects.WISP, dim, x + 0.5, y + 0.5, z + 0.5, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 5.0, 0.0)
	}
}