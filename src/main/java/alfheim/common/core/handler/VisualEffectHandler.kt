package alfheim.common.core.handler

import alfheim.AlfheimCore
import alfheim.client.render.world.VisualEffectHandlerClient.VisualEffects
import alfheim.common.network.MessageVisualEffect
import net.minecraft.entity.Entity

object VisualEffectHandler {
	
	fun sendPacket(s: VisualEffects, e: Entity) {
		sendPacket(s, e.dimension, e.posX, e.posY, e.posZ)
	}
	
	fun sendPacket(s: VisualEffects, dimension: Int, vararg data: Double) {
		AlfheimCore.network.sendToDimension(MessageVisualEffect(s.ordinal, *data), dimension)
	}
}