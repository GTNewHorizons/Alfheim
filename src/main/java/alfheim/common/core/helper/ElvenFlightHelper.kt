package alfheim.common.core.helper

import alexsocol.asjlib.D
import alfheim.api.ModInfo
import alfheim.common.core.handler.AlfheimConfigHandler
import cpw.mods.fml.client.event.ConfigChangedEvent
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.eventhandler.*
import net.minecraft.entity.ai.attributes.RangedAttribute
import net.minecraft.entity.player.EntityPlayer

object ElvenFlightHelper {
	
	init {
		FMLCommonHandler.instance().bus().register(this)
	}
	
	private val FLIGHT = RangedAttribute(ModInfo.MODID.toUpperCase() + ":FLIGHT", max, 0.0, max).setShouldWatch(true)
	
	fun register(player: EntityPlayer) {
		player.getAttributeMap().registerAttribute(FLIGHT)
	}
	
	fun ensureExistence(player: EntityPlayer) {
		if (player.getAttributeMap().getAttributeInstance(FLIGHT) == null) register(player)
	}
	
	var max
		get() = AlfheimConfigHandler.flightTime.D
		internal set(value) {
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
		add(player, -value.D)
	}
	
	fun regen(player: EntityPlayer, value: Int) {
		add(player, value * AlfheimConfigHandler.flightRecover)
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	fun onConfigChanged(e: ConfigChangedEvent.OnConfigChangedEvent) {
		if (e.modID == ModInfo.MODID) max = AlfheimConfigHandler.flightTime.D
	}
}

var EntityPlayer.flight
	get() = ElvenFlightHelper[this]
	set(value) {
		ElvenFlightHelper[this] = value
	}
