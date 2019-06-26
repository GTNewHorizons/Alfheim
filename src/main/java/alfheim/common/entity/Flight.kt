package alfheim.common.entity

import alfheim.common.core.registry.AlfheimRegistry
import alfheim.common.core.util.AlfheimConfig
import net.minecraft.entity.player.EntityPlayer

object Flight {
	
	fun register(player: EntityPlayer) {
		player.getAttributeMap().registerAttribute(AlfheimRegistry.FLIGHT)
	}
	
	fun ensureExistence(player: EntityPlayer) {
		if (player.getAttributeMap().getAttributeInstance(AlfheimRegistry.FLIGHT) == null) register(player)
	}
	
	fun max(): Double {
		return AlfheimRegistry.FLIGHT.defaultValue
	}
	
	operator fun get(player: EntityPlayer): Double {
		ensureExistence(player)
		return player.getAttributeMap().getAttributeInstance(AlfheimRegistry.FLIGHT).baseValue
	}
	
	operator fun set(player: EntityPlayer, `val`: Double) {
		ensureExistence(player)
		player.getAttributeMap().getAttributeInstance(AlfheimRegistry.FLIGHT).baseValue = `val`
	}
	
	fun add(player: EntityPlayer, `val`: Double) {
		set(player, Math.max(0.0, Math.min(get(player) + `val`, AlfheimConfig.flightTime.toDouble())))
	}
	
	fun sub(player: EntityPlayer, `val`: Double) {
		add(player, -`val`)
	}
}
