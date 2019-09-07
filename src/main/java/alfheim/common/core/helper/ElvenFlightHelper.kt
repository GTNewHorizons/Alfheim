package alfheim.common.core.helper

import alfheim.api.ModInfo
import alfheim.common.core.handler.AlfheimConfigHandler
import net.minecraft.entity.ai.attributes.*
import net.minecraft.entity.player.EntityPlayer
import kotlin.math.*

object ElvenFlightHelper {
	
	private val FLIGHT: IAttribute = object: BaseAttribute(ModInfo.MODID.toUpperCase() + ":FLIGHT", AlfheimConfigHandler.flightTime.toDouble()) {
		override fun clampValue(d: Double) = max(0.0, min(AlfheimConfigHandler.flightTime.toDouble(), d))
	}.setShouldWatch(true)
	
	fun register(player: EntityPlayer) {
		player.getAttributeMap().registerAttribute(FLIGHT)
	}
	
	fun ensureExistence(player: EntityPlayer) {
		if (player.getAttributeMap().getAttributeInstance(FLIGHT) == null) register(player)
	}
	
	val max: Double
		get() = AlfheimConfigHandler.flightTime.toDouble()
	
	operator fun get(player: EntityPlayer): Double {
		ensureExistence(player)
		return player.getAttributeMap().getAttributeInstance(FLIGHT).baseValue
	}
	
	operator fun set(player: EntityPlayer, value: Double) {
		ensureExistence(player)
		player.getAttributeMap().getAttributeInstance(FLIGHT).baseValue = value
	}
	
	fun add(player: EntityPlayer, value: Int) {
		player.flight = max(0.0, min(player.flight + value, AlfheimConfigHandler.flightTime.toDouble()))
	}
	
	fun sub(player: EntityPlayer, value: Int) {
		add(player, -value)
	}
}



var EntityPlayer.flight
	set(value) {
		ElvenFlightHelper[this] = value
	}
	get() = ElvenFlightHelper[this]