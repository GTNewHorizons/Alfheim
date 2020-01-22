package alfheim.common.core.helper

import alfheim.api.ModInfo
import alfheim.common.core.handler.AlfheimConfigHandler
import net.minecraft.entity.ai.attributes.BaseAttribute
import net.minecraft.entity.player.EntityPlayer
import kotlin.math.*

object ElvenFlightHelper {
	
	private val FLIGHT = object: BaseAttribute(ModInfo.MODID.toUpperCase() + ":FLIGHT", max) {
		override fun clampValue(d: Double) = max(0.0, min(max, d))
	}.setShouldWatch(true)
	
	fun register(player: EntityPlayer) {
		player.getAttributeMap().registerAttribute(FLIGHT)
	}
	
	fun ensureExistence(player: EntityPlayer) {
		if (player.getAttributeMap().getAttributeInstance(FLIGHT) == null) register(player)
	}
	
	var max
		get() = AlfheimConfigHandler.flightTime.toDouble()
		internal set(value) {
			AlfheimConfigHandler.flightTime = value.toInt()
			FLIGHT.defaultValue = value
		}
	
	operator fun get(player: EntityPlayer): Double {
		ensureExistence(player)
		return FLIGHT.clampValue(player.getAttributeMap().getAttributeInstance(FLIGHT).baseValue)
	}
	
	operator fun set(player: EntityPlayer, value: Double) {
		ensureExistence(player)
		player.getAttributeMap().getAttributeInstance(FLIGHT).baseValue = FLIGHT.clampValue(value)
	}
	
	fun add(player: EntityPlayer, value: Double) {
		player.flight =
			if (!player.capabilities.isCreativeMode)
				FLIGHT.clampValue(player.flight + value)
			else
				max
	}
	
	fun sub(player: EntityPlayer, value: Int) {
		add(player, -value.toDouble())
	}
	
	fun regen(player: EntityPlayer, value: Int) {
		add(player, value * AlfheimConfigHandler.flightRecover)
	}
}



var EntityPlayer.flight
	get() = ElvenFlightHelper[this]
	set(value) {
		ElvenFlightHelper[this] = value
	}
