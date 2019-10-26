package alfheim.common.core.handler

import alfheim.AlfheimCore
import alfheim.client.render.world.VisualEffectHandlerClient.VisualEffects
import alfheim.common.network.MessageVisualEffect
import net.minecraft.entity.Entity

object VisualEffectHandler {
	
	fun sendPacket(s: VisualEffects, e: Entity) {
		sendPacket(s, e.dimension, e.posX, e.posY, e.posZ)
	}
	
	fun sendPacket(s: VisualEffects, dimension: Int, x: Double, y: Double, z: Double) {
		AlfheimCore.network.sendToDimension(MessageVisualEffect(s.ordinal, x, y, z), dimension)
	}
	
	fun sendPacket(s: VisualEffects, dimension: Int, x: Double, y: Double, z: Double, x2: Double, y2: Double, z2: Double) {
		AlfheimCore.network.sendToDimension(MessageVisualEffect(s.ordinal, x, y, z, x2, y2, z2), dimension)
	}
}