package alexsocol.patcher

import alexsocol.asjlib.extendables.ASJConfigHandler
import net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL

object PatcherConfigHandler: ASJConfigHandler() {
	
	var clearWater = true
	var interactionSecurity = "default"
	var maxParticles = 4000
	var portalHook = true
	var voidFog = true
	var WEBiomeID = 152
	
	override fun addCategories() = Unit
	
	override fun readProperties() {
		clearWater = loadProp(CATEGORY_GENERAL, "clearWater", clearWater, false, "Set this to true for clear, transparent water")
		interactionSecurity = loadProp(CATEGORY_GENERAL, "interactionSecurity", interactionSecurity, false, "Region security manager. Visit Alfheim wiki for more info")
		maxParticles = loadProp(CATEGORY_GENERAL, "maxParticles", maxParticles, true, "How many [any] particles can there be at one time (defaults to vanilla value)")
		portalHook = loadProp(CATEGORY_GENERAL, "portalHook", portalHook, false, "Set this to true to disable closing GUI when entering nether portal")
		voidFog = loadProp(CATEGORY_GENERAL, "voidFog", voidFog, false, "Set this to false to disable void fog")
		WEBiomeID = loadProp(CATEGORY_GENERAL, "WEBiomeID", WEBiomeID, true, "ID for standart WorldEngine biome")
	}
}