package alexsocol.patcher

import alexsocol.asjlib.extendables.ASJConfigHandler
import net.minecraftforge.common.config.Configuration.*

object PatcherConfigHandler: ASJConfigHandler() {
	
	const val CATEGORY_DANGER = CATEGORY_GENERAL + CATEGORY_SPLITTER + "dangerzone"
	const val CATEGORY_INTEGRATION = CATEGORY_GENERAL + CATEGORY_SPLITTER + "integration"
	
	var addAir = false
	var addBlocks = false
	var textIDs = false
	
	var biomeDuplication = false
	var clearWater = true
	var creativeDamage = false
	var damageMobArmor = true
	var explosions = true
	var floatingTrapDoors = true
	var interactionSecurity = "default"
	var lightningID = 150
	var maxParticles = 4000
	var portalHook = true
	var potionDuplication = false
	var showOreDict = true
	var voidFog = true
	var WEBiomeID = 152
	
	override fun addCategories() {
		addCategory(CATEGORY_DANGER, "[WARNING!] Backup your world before changing something here!")
	}
	
	override fun readProperties() {
		addAir = loadProp(CATEGORY_DANGER, "addAir", addAir, true, "Set this to true to add air item")
		addBlocks = loadProp(CATEGORY_DANGER, "addBlocks", addBlocks, true, "Set this to true to add items for technical blocks")
		textIDs = loadProp(CATEGORY_DANGER, "textIDs", textIDs, false, "Set this to true to enable text item IDs instead of numeric for storing in NBT")
		
		biomeDuplication = loadProp(CATEGORY_GENERAL, "biomeDuplication", biomeDuplication, false, "Set this to true to allow registering duplicated biome ids")
		clearWater = loadProp(CATEGORY_GENERAL, "clearWater", clearWater, false, "Set this to true for clear, transparent water")
		creativeDamage = loadProp(CATEGORY_GENERAL, "creativeDamage", creativeDamage, false, "Set this to true to allow taking damage in creative")
		damageMobArmor = loadProp(CATEGORY_GENERAL, "damageMobArmor", damageMobArmor, false, "Set this to false to prevent mob armor getting destroyed from attacks")
		explosions = loadProp(CATEGORY_GENERAL, "explosions", explosions, false, "Set this to false to disable explosions")
		floatingTrapDoors = loadProp(CATEGORY_GENERAL, "floatingTrapDoors", floatingTrapDoors, false, "Set this to false to forbid trapdoors to remain free-floating (as in vanilla, may break some world structures)")
		interactionSecurity = loadProp(CATEGORY_GENERAL, "interactionSecurity", interactionSecurity, false, "Region security manager")
		lightningID = loadProp(CATEGORY_GENERAL, "lightningID", lightningID, true, "ID for lightning bolt entity")
		maxParticles = loadProp(CATEGORY_GENERAL, "maxParticles", maxParticles, true, "How many [any] particles can there be at one time (defaults to vanilla value)")
		portalHook = loadProp(CATEGORY_GENERAL, "portalHook", portalHook, false, "Set this to true to disable closing GUI when entering nether portal")
		potionDuplication = loadProp(CATEGORY_GENERAL, "potionDuplication", potionDuplication, false, "Set this to true to allow registering duplicated potion ids")
		showOreDict = loadProp(CATEGORY_GENERAL, "showOreDict", showOreDict, false, "Set this to true to show all oredict names in item tooltip when holding SHIFT")
		voidFog = loadProp(CATEGORY_GENERAL, "voidFog", voidFog, false, "Set this to false to disable void fog")
		WEBiomeID = loadProp(CATEGORY_GENERAL, "WEBiomeID", WEBiomeID, true, "ID for standart WorldEngine biome")
	}
}