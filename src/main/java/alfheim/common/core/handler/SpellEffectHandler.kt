package alfheim.common.core.handler

import alfheim.AlfheimCore
import alfheim.client.render.world.SpellEffectHandlerClient.Spells
import alfheim.common.network.MessageParticles
import net.minecraft.entity.Entity

object SpellEffectHandler {
	
	fun sendPacket(s: Spells, e: Entity) {
		sendPacket(s, e.dimension, e.posX, e.posY, e.posZ)
	}
	
	fun sendPacket(s: Spells, dimension: Int, x: Double, y: Double, z: Double) {
		AlfheimCore.network.sendToDimension(MessageParticles(s.ordinal, x, y, z), dimension)
	}
	
	fun sendPacket(s: Spells, dimension: Int, x: Double, y: Double, z: Double, x2: Double, y2: Double, z2: Double) {
		AlfheimCore.network.sendToDimension(MessageParticles(s.ordinal, x, y, z, x2, y2, z2), dimension)
	}
}