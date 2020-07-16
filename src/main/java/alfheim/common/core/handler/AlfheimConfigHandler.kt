package alfheim.common.core.handler

import alexsocol.asjlib.*
import alexsocol.asjlib.extendables.ASJConfigHandler
import alexsocol.asjlib.math.Vector3
import net.minecraftforge.common.config.Configuration.*
import java.io.*
import kotlin.math.*

object AlfheimConfigHandler: ASJConfigHandler() {
	
	const val CATEGORY_PRELOAD		= CATEGORY_GENERAL		+ CATEGORY_SPLITTER	+ "preload"
	const val CATEGORY_INTEGRATION	= CATEGORY_GENERAL		+ CATEGORY_SPLITTER	+ "integration"
	const val CATEGORY_INT_NEI		= CATEGORY_INTEGRATION	+ CATEGORY_SPLITTER	+ "notenoughitems"
	const val CATEGORY_INT_OF		= CATEGORY_GENERAL		+ CATEGORY_SPLITTER	+ "optifine"
	const val CATEGORY_INT_TC		= CATEGORY_INTEGRATION	+ CATEGORY_SPLITTER	+ "thaumcraft"
	const val CATEGORY_INT_TiC		= CATEGORY_INTEGRATION	+ CATEGORY_SPLITTER	+ "tconstruct"
	const val CATEGORY_DIMENSION	= CATEGORY_GENERAL		+ CATEGORY_SPLITTER	+ "alfheim"
	const val CATEGORY_WORLDGEN		= CATEGORY_DIMENSION	+ CATEGORY_SPLITTER + "worldgen"
	const val CATEGORY_ENTITIES		= CATEGORY_WORLDGEN		+ CATEGORY_SPLITTER + "entities"
	const val CATEGORY_POTIONS		= CATEGORY_GENERAL		+ CATEGORY_SPLITTER	+ "potions"
	const val CATEGORY_ESMODE		= CATEGORY_GENERAL		+ CATEGORY_SPLITTER	+ "elvenstory"
	const val CATEGORY_MMO			= CATEGORY_ESMODE		+ CATEGORY_SPLITTER + "mmo"
	const val CATEGORY_MMOP			= CATEGORY_MMO			+ CATEGORY_SPLITTER + "potions"
	const val CATEGORY_HUD			= CATEGORY_MMO			+ CATEGORY_SPLITTER + "hud"
	
	var enableElvenStory: Boolean
		get() = _enableElvenStory
		set(value) {
			_enableElvenStory = value
			config.get(CATEGORY_PRELOAD, "enableElvenStory", value, "Set this to false to disable ESM and MMO").set(value)
			config.save()
		}
	
	var enableMMO: Boolean
		get() = _enableMMO
		set(value) {
			_enableMMO = value
			config.get(CATEGORY_PRELOAD, "enableMMO", value, "Set this to false to disable MMO").set(value)
			config.save()
		}
	
	private var _enableElvenStory	= true
	private var _enableMMO			= true
	
	// PRELOAD
	var elementiumClusterMeta	= 22
	var gaiaBarOffset			= 1
	var gaiaNameColor			= 0x00D5FF
	var hpHooks					= true
	var maxParticles			= 4000
	var modularFilename			= ""
	var modularThread			= false
	var modularUpdate			= true
	var modularUpdateConfirm	= false
	var primaryClassTransformer	= true
	
	// DIMENSION
	var biomeIDAlfheim			= 152
	var destroyPortal			= true
	var dimensionIDAlfheim		= -105
	var enableAlfheimRespawn	= true
	
	// WORLDGEN
	var anomaliesDispersion		= 50
	var anomaliesUpdate			= 6000
	var citiesDistance			= 1000
	var oregenMultiplier		= 3
	var winterGrassReadyGen		= true
	
	// ENTITIES
	var globalEntityIDs			= true
	var butterflySpawn			= intArrayOf(10, 1, 2)
	var chickSpawn				= intArrayOf(10, 4, 4)
	var cowSpawn				= intArrayOf( 8, 4, 4)
	var elvesSpawn				= intArrayOf(10, 2, 4)
	var pigSpawn				= intArrayOf(10, 4, 4)
	var pixieSpawn				= intArrayOf(10, 1, 2)
	var sheepSpawn				= intArrayOf(12, 4, 4)
	var voidCreeper				= intArrayOf(4, 1, 3)
	
	// OHTER
	var alfheimSleepExtraCheck	= true
	var anyavilBlackList		= emptyArray<String>()
	var authTimeout				= 200
	var blackLotusDropRate		= 0.05
	var fancies					= true
	var floatingTrapDoors		= true
	var flugelSwapBlackList		= emptyArray<String>()
	var lightningsSpeed			= 20
	var lolicornAlfheimOnly		= true
	var lolicornCost			= 1000
	var lolicornLife			= 600
	var looniumOverseed			= false
	var minimalGraphics			= false
	var moonbowMaxDmg			= 20
	var moonbowVelocity			= 0.5f
	var multibaubleCount		= 6
	var notifications			= true
	var numericalMana			= true
	var realLightning			= false
	var rocketRide				= 2
	var searchTabAlfheim		= true
	var searchTabBotania		= true
	var schemaArray				= IntArray(17) { -1 + it }
	var schemaMaxSize			= 64
	var storyLines				= 4
	var tradePortalRate			= 1200
	var triquetrumBlackList		= emptyArray<String>()
	var triquetrumMaxDiagonal	= -1.0
	var uberSpreaderCapacity	= 24000
	var uberSpreaderSpeed		= 2400
	var voidCreepBiomeBlackList	= intArrayOf(8, 9, 14, 15)
	var wireoverpowered			= true
	
	// INTEGRATION
	var chatLimiters			= "%s"
	var interactionSecurity 	= "default"
	var poolRainbowCapacity		= 1000000 // TilePool.MAX_MANA
	
	// NEI
	var blacklistWither			= true
	
	// OptiFine override
	var clearWater				= false
	var voidFog					= true
	
	// TC INTEGRATION
	var addAspectsToBotania		= true
	var addTincturemAspect		= true
	var thaumTreeSuffusion		= true
	
	// TiC INTEGRATION
	var materialIDs				= intArrayOf(50, 51, 52, 53, 54, 55, 56, 57, 3, 4)
	var modifierIDs				= intArrayOf(20)
	
	// POTIONS
	var potionID___COUNTER		= 30
	var potionIDBerserk			= potionID___COUNTER++
	var potionIDBleeding		= potionID___COUNTER++
	var potionIDButterShield	= potionID___COUNTER++
	var potionIDDeathMark		= potionID___COUNTER++
	var potionIDDecay			= potionID___COUNTER++
	var potionIDEternity		= potionID___COUNTER++
	var potionIDGoldRush		= potionID___COUNTER++
	var potionIDIceLens			= potionID___COUNTER++
	var potionIDLeftFlame		= potionID___COUNTER++
	var potionIDLightningShield	= potionID___COUNTER++
	var potionIDManaVoid		= potionID___COUNTER++
	var potionIDNineLifes		= potionID___COUNTER++
	var potionIDNinja			= potionID___COUNTER++
	var potionIDNoclip			= potionID___COUNTER++
	var potionIDOvermage		= potionID___COUNTER++
	var potionIDPossession		= potionID___COUNTER++
	var potionIDQuadDamage		= potionID___COUNTER++
	var potionIDSacrifice		= potionID___COUNTER++
	var potionIDShowMana		= potionID___COUNTER++
	var potionIDSoulburn		= potionID___COUNTER++
	var potionIDStoneSkin		= potionID___COUNTER++
	var potionIDTank			= potionID___COUNTER++
	var potionIDThrow			= potionID___COUNTER++
	var potionIDWellOLife		= potionID___COUNTER++
	
	// Elven Story
	var bonusChest				= false
	var bothSpawnStructures		= false
	var flightTime				= 12000
	var flightRecover			= 1.0
	var wingsBlackList			= IntArray(0)
	val zones					= Array(9) { Vector3(0.0) }
	
	// MMO
	var deathScreenAddTime		= 1200
	var disabledSpells			= emptyArray<String>()
	var frienldyFire			= false
	var raceManaMult			= 2.toByte()
	var maxPartyMembers			= 5
	var superSpellBosses		= false
	
	// MMO HUD
	var partyHUDScale			= 1.0
	var selfHealthUI			= true
	var spellsFadeOut			= false
	var targetUI				= true
	
	override fun addCategories() {
		addCategory(CATEGORY_PRELOAD, "Alfheim coremod and preload settings")
		addCategory(CATEGORY_DIMENSION, "Alfheim dimension settings")
		addCategory(CATEGORY_WORLDGEN, "Alfheim worldgen settings")
		addCategory(CATEGORY_ENTITIES, "Alfheim mobs spawnrate settings")
		addCategory(CATEGORY_POTIONS, "Potion IDs")
		addCategory(CATEGORY_INTEGRATION, "Cross-mods and modpacks integration")
		addCategory(CATEGORY_INT_TC, "Thaumcraft integration")
		addCategory(CATEGORY_ESMODE, "Elvenstory Mode optional features")
		addCategory(CATEGORY_MMO, "MMO Mode optional features")
		addCategory(CATEGORY_HUD, "HUD elements customizations")
		addCategory(CATEGORY_MMOP, "Potion IDs")
	}
	
	override fun syncConfig() {
		_enableElvenStory = loadProp(CATEGORY_PRELOAD, "enableElvenStory", _enableElvenStory, true, "Set this to false to disable ESM and MMO")
		_enableMMO = _enableElvenStory && loadProp(CATEGORY_PRELOAD, "enableMMO", _enableMMO, true, "Set this to false to disable MMO")
		
		elementiumClusterMeta = loadProp(CATEGORY_PRELOAD, "elementiumClusterMeta", elementiumClusterMeta, true, "Effective only if Thaumcraft is installed. Change this if some other mod adds own clusters (max value is 63); also please, edit and spread modified .lang files")
		gaiaBarOffset = loadProp(CATEGORY_PRELOAD, "gaiaBarOffset", gaiaBarOffset, true, "Gaia hp and bg boss bar variant (from default texture pairs)")
		gaiaNameColor = loadProp(CATEGORY_PRELOAD, "gaiaNameColor", gaiaNameColor, false, "Gaia name color on boss bar")
		hpHooks = loadProp(CATEGORY_PRELOAD, "hpHooks", hpHooks, true, "Toggles hooks to vanilla health system. Set this to false if you have any issues with other systems")
		maxParticles = loadProp(CATEGORY_PRELOAD, "maxParticles", maxParticles, true, "How many [any] particles can there be at one time (defaults to vanilla value)")
		modularFilename = loadProp(CATEGORY_PRELOAD, "modularFilename", modularFilename, true, "Custom name for Alfheim Modular .jar file")
		modularThread = loadProp(CATEGORY_PRELOAD, "modularThread", modularThread, true, "Set this to true if you want Alfheim Modular to download in separate thread")
		modularUpdate = loadProp(CATEGORY_PRELOAD, "modularUpdate", modularUpdate, true, "[HIGHLY !NOT! RECOMMENDED - can cause me be angry at you] Set this to false if you REALLY don't want Alfheim Modular to be downloaded/updated automatically")
		modularUpdateConfirm = loadProp(CATEGORY_PRELOAD, "modularUpdateConfirm", modularUpdateConfirm, true, "Set this to true if you are totally 146% sure you don't want modular auto updates")
		primaryClassTransformer = loadProp(CATEGORY_PRELOAD, "primaryClassTransformer", primaryClassTransformer, true, "Set this to false if some mod in your modpack is also using GloomyFolken's hooklib and there are conflicts")
		
		biomeIDAlfheim = loadProp(CATEGORY_DIMENSION, "biomeIDAlfheim", biomeIDAlfheim, true, "Biome ID for standart biome")
		destroyPortal = loadProp(CATEGORY_DIMENSION, "destroyPortal", destroyPortal, false, "Set this to false to disable destroying portals in non-zero coords in Alfheim")
		dimensionIDAlfheim = loadProp(CATEGORY_DIMENSION, "dimensionIDAlfheim", dimensionIDAlfheim, true, "Dimension ID for Alfheim")
		enableAlfheimRespawn = loadProp(CATEGORY_DIMENSION, "enableAlfheimRespawn", enableAlfheimRespawn, false, "Set this to false to disable respawning in Alfheim")
		
		anomaliesDispersion = loadProp(CATEGORY_WORLDGEN, "anomaliesDispersion", anomaliesDispersion, false, "How rare anomalies are (lower numbers means higher chance)")
		anomaliesUpdate = loadProp(CATEGORY_WORLDGEN, "anomaliesUpdate", anomaliesUpdate, false, "How many times anomaly will simulate tick while being generated")
		citiesDistance = loadProp(CATEGORY_WORLDGEN, "citiesDistance", citiesDistance, true, "Distance between any elven city and worlds center")
		oregenMultiplier = loadProp(CATEGORY_WORLDGEN, "oregenMultiplier", oregenMultiplier, true, "Multiplier for Alfheim oregen")
		winterGrassReadyGen = loadProp(CATEGORY_WORLDGEN, "winterGrassReadyGen", winterGrassReadyGen, false, "Set this to false to prevent ready generation snow grass instead of regular")
		
		globalEntityIDs = loadProp(CATEGORY_ENTITIES, "globalEntityIDs", globalEntityIDs, true, "Set this to false to use local mod entity IDs")
		butterflySpawn = loadProp(CATEGORY_ENTITIES, "butterflySpawn", butterflySpawn, false, "Butterfly spawn weight (chance), min and max group count")
		cowSpawn = loadProp(CATEGORY_ENTITIES, "cowSpawn", cowSpawn, false, "Cows spawn weight (chance), min and max group count")
		chickSpawn = loadProp(CATEGORY_ENTITIES, "chickSpawn", chickSpawn, false, "Chicken spawn weight (chance), min and max group count")
		elvesSpawn = loadProp(CATEGORY_ENTITIES, "elvesSpawn", elvesSpawn, false, "Elves spawn weight (chance), min and max group count")
		pigSpawn = loadProp(CATEGORY_ENTITIES, "pigSpawn", pigSpawn, false, "Pig spawn weight (chance), min and max group count")
		pixieSpawn = loadProp(CATEGORY_ENTITIES, "pixieSpawn", pixieSpawn, false, "Pixie spawn weight (chance), min and max group count")
		sheepSpawn = loadProp(CATEGORY_ENTITIES, "sheepSpawn", sheepSpawn, false, "Sheep spawn weight (chance), min and max group count")
		voidCreeper = loadProp(CATEGORY_ENTITIES, "voidCreeper", voidCreeper, false, "Manaseal Creeper spawn weight (chance), min and max group count")
		
		alfheimSleepExtraCheck = loadProp(CATEGORY_GENERAL, "alfheimSleepExtraCheck", alfheimSleepExtraCheck, false, "Set this to false if you are skipping whole day while sleeping")
		anyavilBlackList = loadProp(CATEGORY_GENERAL, "anyavilBlackList", anyavilBlackList, false, "Blacklist of items anyavil can accept [modid:name]", false)
		authTimeout = loadProp(CATEGORY_GENERAL, "authTimeout", authTimeout, false, "Time limit for client to send authentication credentials", 100, 600)
		blackLotusDropRate = loadProp(CATEGORY_GENERAL, "blackLotusDropRate", blackLotusDropRate, false, "Rate of black loti dropping from Manaseal Creepers")
		fancies = loadProp(CATEGORY_GENERAL, "fancies", fancies, false, "Set this to false to locally disable fancies rendering on you (for contributors only)")
		floatingTrapDoors = loadProp(CATEGORY_GENERAL, "floatingTrapDoors", floatingTrapDoors, false, "Set this to false forbid trapdoors to remain free-floating (as in vanilla, may break some world structures)")
		flugelSwapBlackList = loadProp(CATEGORY_GENERAL, "flugelSwapBlackList", flugelSwapBlackList, false, "Blacklist for items that flugel can't swap [modid:name]", false)
		lightningsSpeed = loadProp(CATEGORY_GENERAL, "lightningsSpeed", lightningsSpeed, false, "How many ticks it takes between two lightings are spawned in Lightning Anomaly render")
		lolicornAlfheimOnly = loadProp(CATEGORY_GENERAL, "lolicornAlfheimOnly", lolicornAlfheimOnly, false, "Set this to false to make lolicorn summonable in any dimension")
		lolicornCost = loadProp(CATEGORY_GENERAL, "lolicornCost", lolicornCost, false, "How much mana lolicorn consumes on summoning (not teleporting)")
		lolicornLife = loadProp(CATEGORY_GENERAL, "lolicornLife", lolicornLife, false, "How many ticks lolicorn can stay unmounted")
		looniumOverseed = loadProp(CATEGORY_GENERAL, "looniumOverseed", looniumOverseed, true, "Set this to true to make loonium spawn overgrowth seeds (for servers with limited dungeons so all players can craft Gaia pylons)")
		minimalGraphics = loadProp(CATEGORY_GENERAL, "minimalGraphics", minimalGraphics, true, "Set this to true to disable .obj models and shaders")
		moonbowMaxDmg = loadProp(CATEGORY_GENERAL, "moonbowMaxDmg", moonbowMaxDmg, false, "Max base damage for Phoebus Catastrophe")
		moonbowVelocity = loadProp(CATEGORY_GENERAL, "moonbowVelocity", moonbowVelocity.D, false, "Phoebus Catastrophe charge speed").F
		multibaubleCount = loadProp(CATEGORY_GENERAL, "multibaubleCount", multibaubleCount, false, "How many bauble box slots will be activated by Ring of Elven King")
		notifications = loadProp(CATEGORY_GENERAL, "notifications", notifications, false, "Set this to false to disable custom notifications and version check")
		numericalMana = loadProp(CATEGORY_GENERAL, "numericalMana", numericalMana, false, "Set this to false to disable numerical mana representation")
		realLightning = loadProp(CATEGORY_GENERAL, "realLightning", realLightning, false, "Set this to true to make lightning rod summon real (weather) lightning")
		rocketRide = loadProp(CATEGORY_GENERAL, "rocketRide", rocketRide, false, "Rocket ride [-1 - not players, 0 - none, 1 - players, 2 - anyone]")
		searchTabAlfheim = loadProp(CATEGORY_GENERAL, "searchTabAlfheim", searchTabAlfheim, false, "Set this to false to disable searchbar in Alfheim Tab")
		searchTabBotania = loadProp(CATEGORY_GENERAL, "searchTabBotania", searchTabBotania, false, "Set this to false to disable searchbar in Botania Tab")
		schemaArray = loadProp(CATEGORY_GENERAL, "schemaArray", schemaArray, false, "Which schemas are allowed to be generated", false)
		schemaMaxSize = loadProp(CATEGORY_GENERAL, "schemaMaxSize", schemaMaxSize, false, "Max schema cuboid side length")
		storyLines = loadProp(CATEGORY_GENERAL, "storyLines", storyLines, false, "Number of lines for story token")
		tradePortalRate = loadProp(CATEGORY_GENERAL, "tradePortalRate", tradePortalRate, false, "Portal updates every [N] ticks")
		triquetrumBlackList = loadProp(CATEGORY_GENERAL, "triquetrumBlackList", triquetrumBlackList, false, "Blacklist for blocks that triquetrum can't swap [modid:name]", false)
		triquetrumMaxDiagonal = loadProp(CATEGORY_GENERAL, "triquetrumMaxDiagonal", triquetrumMaxDiagonal, false, "Change this to limit triquetrum area")
		uberSpreaderCapacity = loadProp(CATEGORY_GENERAL, "uberSpreaderCapacity", uberSpreaderCapacity, false, "Mauftrium Spreader max mana cap")
		uberSpreaderSpeed = loadProp(CATEGORY_GENERAL, "uberSpreaderSpeed", uberSpreaderSpeed, false, "Mauftrium Spreader mana per shot")
		voidCreepBiomeBlackList = loadProp(CATEGORY_GENERAL, "voidCreepersBiomeBlackList", voidCreepBiomeBlackList, true, "Biome blacklist for Manaseal Creepers", false)
		wireoverpowered = loadProp(CATEGORY_GENERAL, "wire.overpowered", wireoverpowered, false, "Allow WireSegal far more power than any one person should have")
		
		chatLimiters = loadProp(CATEGORY_INTEGRATION, "chatLimiters", chatLimiters, false, "Chat limiters for formtatting special chat lines when using chat plugins")
		interactionSecurity = loadProp(CATEGORY_INTEGRATION, "interactionSecurity", interactionSecurity, false, "Region security manager. Visit Alfheim wiki for more info")
		poolRainbowCapacity = loadProp(CATEGORY_INTEGRATION, "poolRainbowCapacity", poolRainbowCapacity, false, "Fabulous manapool capacity (for custom modpacks with A LOT of mana usage. Can be applied only to NEW pools)")
		
		clearWater = loadProp(CATEGORY_INT_OF, "clearWater", clearWater, false, "[OF override] Set this to true for clear, transparent water")
		voidFog = loadProp(CATEGORY_INT_OF, "voidFog", voidFog, false, "[OF override] Set this to false to disable void fog")
		
		blacklistWither = loadProp(CATEGORY_INT_NEI, "NEI.blacklistWither", blacklistWither, true, "[NEI] Set this to false to make Wither spawner visible")
		
		addAspectsToBotania = loadProp(CATEGORY_INT_TC, "TC.botaniaAspects", addAspectsToBotania, true, "[TC] Set this to false to disable adding aspects to Botania")
		addTincturemAspect = loadProp(CATEGORY_INT_TC, "TC.tincturem", addTincturemAspect, true, "[TC] Set this to false to use Sensus instead of Color aspect")
		thaumTreeSuffusion = loadProp(CATEGORY_INT_TC, "TC.treeCrafting", thaumTreeSuffusion, true, "[TC] [GoG] Set this to false to remove Thaumcraft plants Dendric Suffusion")
		
		materialIDs = loadProp(CATEGORY_INT_TiC, "TiC.materialIDs", materialIDs, true, "[TiC] IDs for Elementium, Elvorium, Manasteel, Mauftrium, Terrasteel, Livingwood, Dreamwood, Redstring, Manastring materials respectively")
		modifierIDs = loadProp(CATEGORY_INT_TiC, "TiC.modifierIDs", modifierIDs, true, "[TiC] IDs for ManaCore modifiers respectively")
		
		potionIDBerserk = loadProp(CATEGORY_POTIONS, "potionIDBerserk", potionIDBerserk, true, "Potion id for Berserk")
		potionIDBleeding = loadProp(CATEGORY_MMOP, "potionIDBleeding", potionIDBleeding, true, "Potion id for Bleeding")
		potionIDButterShield = loadProp(CATEGORY_MMOP, "potionIDButterShield", potionIDButterShield, true, "Potion id for Butterfly Shield")
		potionIDDeathMark = loadProp(CATEGORY_MMOP, "potionIDDeathMark", potionIDDeathMark, true, "Potion id for Death Mark")
		potionIDDecay = loadProp(CATEGORY_MMOP, "potionIDDecay", potionIDDecay, true, "Potion id for Decay")
		potionIDEternity = loadProp(CATEGORY_POTIONS, "potionIDEternity", potionIDEternity, true, "Potion id for Eternity")
		potionIDGoldRush = loadProp(CATEGORY_MMOP, "potionIDGoldRush", potionIDGoldRush, true, "Potion id for Gold Rush")
		potionIDIceLens = loadProp(CATEGORY_MMOP, "potionIDIceLens", potionIDIceLens, true, "Potion id for Ice Lense")
		potionIDLeftFlame = loadProp(CATEGORY_MMOP, "potionIDLeftFlame", potionIDLeftFlame, true, "Potion id for Leftover Flame")
		potionIDLightningShield = loadProp(CATEGORY_POTIONS, "potionIDLightningShield", potionIDLightningShield, true, "Potion id for Lightning Shield")
		potionIDManaVoid = loadProp(CATEGORY_POTIONS, "potionIDManaVoid", potionIDManaVoid, true, "Potion id for Mana Void")
		potionIDNineLifes = loadProp(CATEGORY_MMOP, "potionIDNineLifes", potionIDNineLifes, true, "Potion id for Nine Lifes")
		potionIDNinja = loadProp(CATEGORY_POTIONS, "potionIDNinja", potionIDNinja, true, "Potion id for Ninja")
		potionIDNoclip = loadProp(CATEGORY_MMOP, "potionIDNoclip", potionIDNoclip, true, "Potion id for Noclip")
		potionIDOvermage = loadProp(CATEGORY_POTIONS, "potionIDOvermage", potionIDOvermage, true, "Potion id for Overmage")
		potionIDPossession = loadProp(CATEGORY_POTIONS, "potionIDPossession", potionIDPossession, true, "Potion id for Possession")
		potionIDQuadDamage = loadProp(CATEGORY_MMOP, "potionIDQuadDamage", potionIDQuadDamage, true, "Potion id for Quad Damage")
		potionIDSacrifice = loadProp(CATEGORY_MMOP, "potionIDSacrifice", potionIDSacrifice, true, "Potion id for Sacrifice")
		potionIDShowMana = loadProp(CATEGORY_MMOP, "potionIDShowMana", potionIDShowMana, true, "Potion id for Mana Showing Effect")
		potionIDSoulburn = loadProp(CATEGORY_POTIONS, "potionIDSoulburn", potionIDSoulburn, true, "Potion id for Soulburn")
		potionIDStoneSkin = loadProp(CATEGORY_MMOP, "potionIDStoneSkin", potionIDStoneSkin, true, "Potion id for Stone Skin")
		potionIDTank = loadProp(CATEGORY_POTIONS, "potionIDTank", potionIDTank, true, "Potion id for Tank")
		potionIDThrow = loadProp(CATEGORY_MMOP, "potionIDThrow", potionIDThrow, true, "Potion id for Throw")
		potionIDWellOLife = loadProp(CATEGORY_MMOP, "potionIDWellOLife", potionIDWellOLife, true, "Potion id for Well'o'Life")
		
		bonusChest = loadProp(CATEGORY_WORLDGEN, "bonusChest", bonusChest, false, "Set this to true to generate bonus chest in ESM sky box")
		bothSpawnStructures = loadProp(CATEGORY_ESMODE, "bothSpawnStructures", bothSpawnStructures, false, "Set this to true to generate both room in the skies and castle below (!contains portal!) on zero coords of Alfheim")
		flightTime = loadProp(CATEGORY_ESMODE, "flightTime", flightTime, false, "Elven flight fly points (faster you move - more you spend)")
		flightRecover = loadProp(CATEGORY_ESMODE, "flightRecover", flightRecover, false, "Flight recover efficiency")
		wingsBlackList = loadProp(CATEGORY_ESMODE, "wingsBlackList", wingsBlackList, false, "Wings will be unavailable in this dimension(s)", false)
		
		deathScreenAddTime = loadProp(CATEGORY_MMO, "deathScreenAdditionalTime", deathScreenAddTime, false, "Duration of death screen timer (in ticks)")
		disabledSpells = loadProp(CATEGORY_MMO, "disabledSpells", disabledSpells, true, "List of spell name IDs that won't be registered", false)
		frienldyFire = loadProp(CATEGORY_MMO, "frienldyFire", frienldyFire, false, "Set this to true to enable damage to party members")
		raceManaMult = loadProp(CATEGORY_MMO, "raceManaMult", raceManaMult.I, false, "Mana cost multiplier for spells with not your affinity").toByte()
		maxPartyMembers = loadProp(CATEGORY_MMO, "maxPartyMembers", maxPartyMembers, false, "How many people can be in single party at the same time")
		superSpellBosses = loadProp(CATEGORY_MMO, "superSpellBoss", superSpellBosses, false, "Set this to true to make bosses vulnerable to legendary spells")
		
		partyHUDScale = loadProp(CATEGORY_HUD, "partyHUDScale", partyHUDScale, false, "Party HUD Scale (1 < bigger; 1 > smaller)")
		selfHealthUI = loadProp(CATEGORY_HUD, "selfHealthUI", selfHealthUI, false, "Set this to false to hide player's healthbar")
		spellsFadeOut = loadProp(CATEGORY_HUD, "spellsFadeOut", spellsFadeOut, false, "Set this to true to make spell UI fade out when not active")
		targetUI = loadProp(CATEGORY_HUD, "targethUI", targetUI, false, "Set this to false to hide target's healthbar")
		
		if (config.hasChanged()) {
			config.save()
		}
	}
	
	fun initWorldCoordsForElvenStory(save: String) {
		val f = File("$save/data/AlfheimCoords.txt")
		if (!f.exists()) makeDefaultWorldCoords(save)
		
		try {
			val fr = FileReader("$save/data/AlfheimCoords.txt")
			val br = BufferedReader(fr)
			for (i in zones.indices) {
				br.readLine()
				try {
					zones[i] = makeVectorFromString(br.readLine())
				} catch (e: IllegalArgumentException) {
					br.close()
					fr.close()
					throw e
				}
				
			}
			br.close()
			fr.close()
		} catch (e: IOException) {
			System.err.println("Unable to read Alfheim Coords data. Creating default...")
			e.printStackTrace()
			makeDefaultWorldCoords(save)
		}
		
	}
	
	private fun makeDefaultWorldCoords(save: String) {
		try {
			val fw = FileWriter("$save/data/AlfheimCoords.txt")
			
			val s = StringBuilder()
			s.append("Salamander start city and players spawnpoint coords:\n")
			s.append(writeStandardCoords(0.000))
			s.append("Sylph start city and players spawnpoint coords:\n")
			s.append(writeStandardCoords(40.00))
			s.append("Cait Sith start city and players spawnpoint coords:\n")
			s.append(writeStandardCoords(80.00))
			s.append("Puca start city and players spawnpoint coords:\n")
			s.append(writeStandardCoords(120.0))
			s.append("Gnome start city and players spawnpoint coords:\n")
			s.append(writeStandardCoords(160.0))
			s.append("Leprechaun start city and players spawnpoint coords:\n")
			s.append(writeStandardCoords(200.0))
			s.append("Spriggan start city and players spawnpoint coords:\n")
			s.append(writeStandardCoords(240.0))
			s.append("Undine start city and players spawnpoint coords:\n")
			s.append(writeStandardCoords(280.0))
			s.append("Imp start city and players spawnpoint coords:\n")
			s.append(writeStandardCoords(320.0))
			fw.write("$s")
			fw.close()
		} catch (e: IOException) {
			ASJUtilities.error("Unable to generate default Alfheim Coords data. Setting all to [0, 300, 0]...")
			e.printStackTrace()
			
			for (i in zones.indices) {
				zones[i].set(0.0, 300.0, 0.0)
			}
		}
		
	}
	
	private fun writeStandardCoords(angle: Double): String {
		val v = mkVecLenRotMine(citiesDistance, angle)
		return "${v.x.mfloor()} : 300 : ${v.z.mfloor()}\n"
	}
	
	private fun makeVectorFromString(s: String): Vector3 {
		val ss = s.split(" : ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
		require(ss.size == 3) { String.format("Wrong coords count. Expected 3 got %d", ss.size) }
		return Vector3(ss[0].toDouble(), ss[1].toDouble(), ss[2].toDouble())
	}
	
	private fun mkVecLenRotMine(length: Int, angle: Double) =
		makeVectorOfLengthRotated(length, angle + 90)
	
	private fun makeVectorOfLengthRotated(length: Int, angle: Double) =
		Vector3(cos(Math.toRadians(angle)) * length, 64.0, sin(Math.toRadians(angle)) * length)
}