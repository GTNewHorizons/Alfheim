package alfheim.common.core.handler

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.common.core.util.mfloor
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.common.config.Configuration.*
import java.io.*
import kotlin.math.*

object AlfheimConfigHandler {
	lateinit var config: Configuration
	
	const val CATEGORY_PRELOAD		= CATEGORY_GENERAL		+ CATEGORY_SPLITTER	+ "preload"
	const val CATEGORY_INTEGRATION	= CATEGORY_GENERAL		+ CATEGORY_SPLITTER	+ "integration"
	const val CATEGORY_INT_TC		= CATEGORY_INTEGRATION	+ CATEGORY_SPLITTER	+ "Thaumcraft"
	const val CATEGORY_DIMENSION	= CATEGORY_GENERAL		+ CATEGORY_SPLITTER	+ "Alfheim"
	const val CATEGORY_WORLDGEN		= CATEGORY_DIMENSION	+ CATEGORY_SPLITTER + "woldgen"
	const val CATEGORY_SPAWNRATE	= CATEGORY_WORLDGEN		+ CATEGORY_SPLITTER + "spawnrate"
	const val CATEGORY_POTIONS		= CATEGORY_GENERAL		+ CATEGORY_SPLITTER	+ "potions"
	const val CATEGORY_ESMODE		= CATEGORY_GENERAL		+ CATEGORY_SPLITTER	+ "elvenstory"
	const val CATEGORY_MMO			= CATEGORY_ESMODE		+ CATEGORY_SPLITTER + "mmo"
	const val CATEGORY_MMOP			= CATEGORY_MMO			+ CATEGORY_SPLITTER + "potions"
	const val CATEGORY_HUD			= CATEGORY_MMO			+ CATEGORY_SPLITTER + "hud"
	
	// PRELOAD
	var elementiumClusterMeta	= 22
	var hpHooks					= true
	var maxParticles			= 4000
	var modularThread			= false
	var primaryClassTransformer	= true
	
	// DIMENSION
	var biomeIDAlfheim			= 152
	var dimensionIDAlfheim		= -105
	var enableAlfheimRespawn	= true
	
	// WORLDGEN
	var anomaliesDispersion		= 50
	var anomaliesUpdate			= 6000
	var citiesDistance			= 1000
	var oregenMultiplier		= 3
	
	// SPAWNRATE
	var chickSpawn				= intArrayOf(10, 4, 4)
	var cowSpawn				= intArrayOf( 8, 4, 4)
	var elvesSpawn				= intArrayOf(10, 2, 4)
	var sheepSpawn				= intArrayOf(12, 4, 4)
	var pigSpawn				= intArrayOf(10, 4, 4)
	var pixieSpawn				= intArrayOf(10, 1, 2)
	
	// OHTER
	var anyavilBL				= emptyArray<String>()
	var blackLotusDropRate		= 0.05
	var destroyPortal			= true
	var fancies					= true
	var flugelBossBar			= true
	var flugelSwapBL			= emptyArray<String>()
	var info					= true
	var lightningsSpeed			= 20
	var lolicornAlfheimOnly		= true
	var lolicornCost			= 1000
	var lolicornLife			= 600
	var looniumOverseed			= false
	var minimalGraphics			= false
	var numericalMana			= true
	var realLightning			= false
	var searchTabAlfheim		= true
	var searchTabBotania		= true
	var schemaArray				= IntArray(17) { -1 + it }
	var storyLines				= 4
	var tradePortalRate			= 1200
	var voidCreepersBiomeBL		= intArrayOf(8, 9, 14, 15)
	var wireoverpowered			= true
	
	// TC INTEGRATION
	var addAspectsToBotania		= true
	var addTincturemAspect		= true
	var thaumTreeSuffusion		= true
	
	// POTIONS
	var potionSlots				= 1024
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
	var bothSpawnStructures		= false
	var flightTime				= 12000
	var flightRecover			= 1.0
	var wingsBlackList			= IntArray(0)
	val zones					= Array(9) { Vector3(0.0) }
	
	// MMO
	var deathScreenAddTime		= 1200
	var frienldyFire			= false
	var raceManaMult			= 2.0
	var maxPartyMembers			= 5

	// HUD
	var partyHUDScale			= 1.0
	var selfHealthUI			= true
	var targetUI				= true
	
	fun loadConfig(suggestedConfigurationFile: File) {
		config = Configuration(suggestedConfigurationFile)
		
		config.load()
		config.addCustomCategoryComment(CATEGORY_PRELOAD, "${CATEGORY_PRELOAD}.tooltip")
		config.addCustomCategoryComment(CATEGORY_INTEGRATION, "${CATEGORY_INTEGRATION}.tooltip")
		config.addCustomCategoryComment(CATEGORY_INT_TC, "${CATEGORY_INT_TC}.tooltip")
		config.addCustomCategoryComment(CATEGORY_DIMENSION, "${CATEGORY_DIMENSION}.tooltip")
		config.addCustomCategoryComment(CATEGORY_WORLDGEN, "${CATEGORY_WORLDGEN}.tooltip")
		config.addCustomCategoryComment(CATEGORY_SPAWNRATE, "${CATEGORY_SPAWNRATE}.tooltip")
		config.addCustomCategoryComment(CATEGORY_POTIONS, "${CATEGORY_POTIONS}.tooltip")
		config.addCustomCategoryComment(CATEGORY_ESMODE, "${CATEGORY_ESMODE}.tooltip")
		config.addCustomCategoryComment(CATEGORY_MMO, "${CATEGORY_MMO}.tooltip")
		config.addCustomCategoryComment(CATEGORY_MMOP, "${CATEGORY_MMOP}.tooltip")
		config.addCustomCategoryComment(CATEGORY_HUD, "${CATEGORY_HUD}.tooltip")
		
		syncConfig()
	}
	
	fun syncConfig() {
		elementiumClusterMeta = loadProp(CATEGORY_PRELOAD, "elementiumClusterMeta", elementiumClusterMeta, true, "Effective only if Thaumcraft is installed. Change this if some other mod adds own clusters (max value is 63); also please, edit and spread modified .lang files")
		hpHooks = loadProp(CATEGORY_PRELOAD, "hpHooks", hpHooks, true, "Toggles hooks to vanilla health system. Set this to false if you have any issues with other systems")
		maxParticles = loadProp(CATEGORY_PRELOAD, "maxParticles", maxParticles, true, "How many [any] particles can there be at one time (defaults to vanilla value)")
		modularThread = loadProp(CATEGORY_PRELOAD, "modularThread", modularThread, true, "Set this to true if you want Alfheim Modular to download in separate thread")
		primaryClassTransformer = loadProp(CATEGORY_PRELOAD, "primaryClassTransformer", primaryClassTransformer, true, "Set this to false if some mod in your modpack is also using GloomyFolken's hooklib and there are conflicts")
		
		biomeIDAlfheim = loadProp(CATEGORY_DIMENSION, "biomeIDAlfheim", biomeIDAlfheim, true, "Biome ID for standart biome")
		destroyPortal = loadProp(CATEGORY_DIMENSION, "destroyPortal", destroyPortal, false, "Set this to false to disable destroying portals in non-zero coords in Alfheim")
		dimensionIDAlfheim = loadProp(CATEGORY_DIMENSION, "dimensionIDAlfheim", dimensionIDAlfheim, true, "Dimension ID for Alfheim")
		enableAlfheimRespawn = loadProp(CATEGORY_DIMENSION, "enableAlfheimRespawn", enableAlfheimRespawn, false, "Set this to false to disable respawning in Alfheim")
		
		anomaliesDispersion = loadProp(CATEGORY_WORLDGEN, "anomaliesDispersion", anomaliesDispersion, false, "How rare anomalies are (lower numbers means higher chance)")
		anomaliesUpdate = loadProp(CATEGORY_WORLDGEN, "anomaliesUpdate", anomaliesUpdate, false, "How many times anomaly will simulate tick while being generated")
		citiesDistance = loadProp(CATEGORY_WORLDGEN, "citiesDistance", citiesDistance, true, "Distance between any elven city and worlds center")
		oregenMultiplier = loadProp(CATEGORY_WORLDGEN, "oregenMultiplier", oregenMultiplier, true, "Multiplier for Alfheim oregen")
		
		cowSpawn = loadProp(CATEGORY_SPAWNRATE, "cowSpawn", cowSpawn, false, "Cows spawn weight (chance), min and max group count")
		chickSpawn = loadProp(CATEGORY_SPAWNRATE, "chickSpawn", chickSpawn, false, "Chicken spawn weight (chance), min and max group count")
		elvesSpawn = loadProp(CATEGORY_SPAWNRATE, "elvesSpawn", elvesSpawn, false, "Elves spawn weight (chance), min and max group count")
		pigSpawn = loadProp(CATEGORY_SPAWNRATE, "pigSpawn", pigSpawn, false, "Pig spawn weight (chance), min and max group count")
		pixieSpawn = loadProp(CATEGORY_SPAWNRATE, "pixieSpawn", pixieSpawn, false, "Pixie spawn weight (chance), min and max group count")
		sheepSpawn = loadProp(CATEGORY_SPAWNRATE, "sheepSpawn", sheepSpawn, false, "Sheep spawn weight (chance), min and max group count")
		
		anyavilBL = loadProp(CATEGORY_GENERAL, "anyavilBL", anyavilBL, false, "Blacklist of items anyavil can accept")
		blackLotusDropRate = loadProp(CATEGORY_GENERAL, "blackLotusDropRate", blackLotusDropRate, false, "Rate of black loti dropping from Manaseal Creepers")
		fancies = loadProp(CATEGORY_GENERAL, "fancies", fancies, false, "Set this to false to locally disable fancies rendering on you (for contributors only)")
		flugelBossBar = loadProp(CATEGORY_GENERAL, "flugelBossBar", flugelBossBar, false, "Set this to false to disable displaying flugel's boss bar")
		flugelSwapBL = loadProp(CATEGORY_GENERAL, "flugelSwapBL", flugelSwapBL, false, "Blacklist for items that flugel can't swap")
		info = loadProp(CATEGORY_GENERAL, "info", info, false, "Set this to false to disable loading news and version check")
		lightningsSpeed = loadProp(CATEGORY_GENERAL, "lightningsSpeed", lightningsSpeed, false, "How many ticks it takes between two lightings are spawned in Lightning Anomaly render")
		lolicornAlfheimOnly = loadProp(CATEGORY_GENERAL, "lolicornAlfheimOnly", lolicornAlfheimOnly, false, "Set this to false to make lolicorn summonable in any dimension")
		lolicornCost = loadProp(CATEGORY_GENERAL, "lolicornCost", lolicornCost, false, "How much mana lolicorn consumes on summoning (not teleporting)")
		lolicornLife = loadProp(CATEGORY_GENERAL, "lolicornLife", lolicornLife, false, "How long lolicorn can stay unmounted")
		looniumOverseed = loadProp(CATEGORY_GENERAL, "looniumOverseed", looniumOverseed, true, "Set this to true to make loonium spawn overgrowth seeds (for servers with limited dungeons so all players can craft Gaia pylons)")
		minimalGraphics = loadProp(CATEGORY_GENERAL, "minimalGraphics", minimalGraphics, false, "Set this to true to disable .obj models and shaders")
		numericalMana = loadProp(CATEGORY_GENERAL, "numericalMana", numericalMana, false, "Set this to false to disable numerical mana representation")
		realLightning = loadProp(CATEGORY_GENERAL, "realLightning", realLightning, false, "Set this to true to make lightning rod summon real (weather) lightning")
		searchTabAlfheim = loadProp(CATEGORY_GENERAL, "searchTabAlfheim", searchTabAlfheim, false, "Set this to false to disable searchbar in Alfheim Tab")
		searchTabBotania = loadProp(CATEGORY_GENERAL, "searchTabBotania", searchTabBotania, false, "Set this to false to disable searchbar in Botania Tab")
		schemaArray = loadProp(CATEGORY_GENERAL, "schemaArray", schemaArray, true, "Which schemas are allowed to be generated")
		storyLines = loadProp(CATEGORY_GENERAL, "storyLines", storyLines, false, "Number of lines for story token")
		tradePortalRate = loadProp(CATEGORY_GENERAL, "tradePortalRate", tradePortalRate, false, "Portal updates every {N} ticks")
		voidCreepersBiomeBL = loadProp(CATEGORY_GENERAL, "voidCreepersBiomeBL", voidCreepersBiomeBL, true, "Biome blacklist for Manaseal Creepers")
		wireoverpowered = loadProp(CATEGORY_GENERAL, "wire.overpowered", wireoverpowered, true, "Allow WireSegal far more power than any one person should have")
		
		addAspectsToBotania = loadProp(CATEGORY_INT_TC, "TC.botaniaAspects", addAspectsToBotania, true, "[TC] Set this to false to disable adding aspects to Botania")
		addTincturemAspect = loadProp(CATEGORY_INT_TC, "TC.tincturem", addTincturemAspect, true, "[TC] Set this to false to use Sensus instead of Color aspect")
		thaumTreeSuffusion = loadProp(CATEGORY_INT_TC, "TC.treeCrafting", thaumTreeSuffusion, true, "[TC] [GoG] Set this to false to remove Thaumcraft plants Dendric Suffusion")
		
		potionSlots = loadProp(CATEGORY_POTIONS, "potionSlots", potionSlots, true, "Available potions ids in range [0-potionSlots)")
		potionIDBerserk = loadProp(CATEGORY_POTIONS, "potionIDBerserk", potionIDBerserk, true, "Potion id for Berserk")
		potionIDBleeding = loadProp(CATEGORY_MMOP, "potionIDBleeding", potionIDBleeding, true, "Potion id for Bleeding")
		potionIDButterShield = loadProp(CATEGORY_MMOP, "potionIDButterShield", potionIDButterShield, true, "Potion id for Butterfly Shield")
		potionIDDeathMark = loadProp(CATEGORY_MMOP, "potionIDDeathMark", potionIDDeathMark, true, "Potion id for Death Mark")
		potionIDDecay = loadProp(CATEGORY_MMOP, "potionIDDecay", potionIDDecay, true, "Potion id for Decay")
		potionIDEternity = loadProp(CATEGORY_POTIONS, "potionIDEternity", potionIDEternity, true, "Potion id for Eternity")
		potionIDGoldRush = loadProp(CATEGORY_MMOP, "potionIDGoldRush", potionIDGoldRush, true, "Potion id for Gold Rush")
		potionIDIceLens = loadProp(CATEGORY_MMOP, "potionIDIceLens", potionIDIceLens, true, "Potion id for Ice Lense")
		potionIDLeftFlame = loadProp(CATEGORY_MMOP, "potionIDLeftFlame", potionIDLeftFlame, true, "Potion id for Leftover Flame")
		potionIDManaVoid = loadProp(CATEGORY_POTIONS, "potionIDManaVoid", potionIDManaVoid, true, "Potion id for Mana Void")
		potionIDNineLifes = loadProp(CATEGORY_POTIONS, "potionIDNineLifes", potionIDNineLifes, true, "Potion id for Nine Lifes")
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
		
		bothSpawnStructures = loadProp(CATEGORY_ESMODE, "bothSpawnStructures", bothSpawnStructures, false, "Set this to true to generate both room in the skies and castle below (!contains portal!) on zero coords of Alfheim")
		flightTime = loadProp(CATEGORY_ESMODE, "flightTime", flightTime, true, "How long can you fly as elf")
		flightRecover = loadProp(CATEGORY_ESMODE, "flightRecover", flightRecover, true, "Flight recover efficiency")
		wingsBlackList = loadProp(CATEGORY_ESMODE, "wingsBlackList", wingsBlackList, false, "Wings will be unavailable in this dimension(s)")
		
		deathScreenAddTime = loadProp(CATEGORY_MMO, "deathScreenAdditionalTime", deathScreenAddTime, false, "Duration of death screen timer (in ticks)")
		frienldyFire = loadProp(CATEGORY_MMO, "frienldyFire", frienldyFire, false, "Set this to true to enable damage to party members")
		raceManaMult = loadProp(CATEGORY_MMO, "raceManaMult", raceManaMult, false, "Mana cost multiplier for spells with not your affinity")
		maxPartyMembers = loadProp(CATEGORY_MMO, "maxPartyMembers", maxPartyMembers, false, "How many people can be in single party at the same time")

		partyHUDScale = loadProp(CATEGORY_HUD, "partyHUDScale", partyHUDScale, false, "Party HUD Scale (1 < bigger; 1 > smaller)")
		selfHealthUI = loadProp(CATEGORY_HUD, "selfHealthUI", selfHealthUI, false, "Set this to false to hide player's healthbar")
		targetUI = loadProp(CATEGORY_HUD, "targethUI", targetUI, false, "Set this to false to hide target's healthbar")
		
		if (config.hasChanged()) {
			config.save()
		}
	}
	
	fun loadProp(category: String, propName: String, default: Int, restart: Boolean, desc: String): Int {
		val prop = config.get(category, propName, default, desc)
		prop.setRequiresMcRestart(restart)
		return prop.getInt(default)
	}
	
	fun loadProp(category: String, propName: String, default: Double, restart: Boolean, desc: String): Double {
		val prop = config.get(category, propName, default, desc)
		prop.setRequiresMcRestart(restart)
		return prop.getDouble(default)
	}
	
	fun loadProp(category: String, propName: String, default: Boolean, restart: Boolean, desc: String): Boolean {
		val prop = config.get(category, propName, default, desc)
		prop.setRequiresMcRestart(restart)
		return prop.getBoolean(default)
	}
	
	fun loadProp(category: String, propName: String, default: IntArray, restart: Boolean, desc: String): IntArray {
		val prop = config.get(category, propName, default, desc)
		prop.setRequiresMcRestart(restart)
		return prop.intList
	}
	
	fun loadProp(category: String, propName: String, default: Array<String>, restart: Boolean, desc: String): Array<String> {
		val prop = config.get(category, propName, default, desc)
		prop.setRequiresMcRestart(restart)
		return prop.stringList
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
	
	fun readModes() {
		val f = File("config/Alfheim/ElvenStoryMode.cfg")
		if (!f.exists()) return
		try {
			val fr = FileReader(f)
			val br = BufferedReader(fr)
			br.readLine()
			val flags = br.readLine().split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
			
			br.close()
			fr.close()
			
			require(flags.size == 2) { String.format("Wrong flags count. Expected 2 got %d", flags.size) }
			
			if (flags[0] == "false") {
				if (flags[1] == "false") {
					AlfheimCore.enableMMO = false
					AlfheimCore.enableElvenStory = AlfheimCore.enableMMO
				} else
					throw IllegalArgumentException(if (flags[1] == "true") "Unable to enable MMO mode without ESM" else String.format("Unknown param value for MMO mode: %s", flags[1]))
			} else if (flags[0] == "true") {
				AlfheimCore.enableElvenStory = true
				when {
					flags[1] == "false" -> AlfheimCore.enableMMO = false
					flags[1] == "true"  -> AlfheimCore.enableMMO = true
					else                -> throw IllegalArgumentException(String.format("Unknown param value for MMO mode: %s", flags[1]))
				}
			} else
				throw IllegalArgumentException(String.format("Unknown param value for ESM mode: %s", flags[0]))
		} catch (e: IOException) {
			e.printStackTrace()
		}
		
	}
	
	fun writeModes() {
		require(!(!AlfheimCore.enableElvenStory && AlfheimCore.enableMMO)) { "Unable to write modes state when ESM is disabled and MMO is enabled" }
		val f = File("config/Alfheim/ElvenStoryMode.cfg")
		try {
			val fw = FileWriter(f)
			fw.write(String.format("ESM mode, MMO mode (second requires first). Default \"true true\"\n%s %s", if (AlfheimCore.enableElvenStory) "true" else "false", if (AlfheimCore.enableMMO) "true" else "false"))
			fw.close()
		} catch (e: IOException) {
			ASJUtilities.error("Unable to write modes state")
			e.printStackTrace()
		}
	}
}