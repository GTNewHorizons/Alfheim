package alfheim.common.core.util

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.math.Vector3
import alexsocol.asjlib.render.RenderPostShaders
import alfheim.AlfheimCore
import alfheim.api.ModInfo
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.common.config.Configuration.*
import java.io.*
import kotlin.math.*

object AlfheimConfig {
	lateinit var config: Configuration
	
	const val CATEGORY_BA			= CATEGORY_GENERAL		+ CATEGORY_SPLITTER	+ "Iridescence"
	const val CATEGORY_DIMENSION	= CATEGORY_GENERAL		+ CATEGORY_SPLITTER	+ "Alfheim"
	const val CATEGORY_WORLDGEN		= CATEGORY_DIMENSION	+ CATEGORY_SPLITTER	+ "woldgen"
	const val CATEGORY_POTIONS		= CATEGORY_GENERAL		+ CATEGORY_SPLITTER	+ "potions"
	const val CATEGORY_ESMODE		= CATEGORY_GENERAL		+ CATEGORY_SPLITTER	+ "elvenstory"
	const val CATEGORY_MMO			= CATEGORY_ESMODE		+ CATEGORY_SPLITTER	+ "mmo"
	const val CATEGORY_MMOP			= CATEGORY_MMO			+ CATEGORY_SPLITTER	+ "potions"
	const val CATEGORY_HUD			= CATEGORY_MMO			+ CATEGORY_SPLITTER	+ "hud"
	
	// DIMENSION
	var biomeIDAlfheim			= 152
	var dimensionIDAlfheim		= -105
	var enableAlfheimRespawn	= true
	
	// WORLDGEN
	var anomaliesDispersion		= 50
	var anomaliesUpdate			= 6000
	var citiesDistance			= 1000
	var oregenMultiplier		= 3
	
	// OHTER
	var destroyPortal			= true
	var fancies					= true
	var flugelBossBar			= true
	var info					= true
	var lightningsSpeed			= 20
	var lolicornAlfheimOnly		= true
	var lolicornCost			= 1000
	var lolicornLife			= 600
	var looniumOverseed			= false
	var minimalGraphics			= false
	var numericalMana			= true
	var slowDownClients			= false
	var tradePortalRate			= 1200
	
	// BOTANICAL ADDONS
	var realLightning			= false
	var blackLotusDropRate		= 0.05
	var addTincturemAspect		= true
	var addAspectsToBotania		= true
	var addThaumTreeSuffusion	= true
	var schemaArray				= IntArray(17) { -1 + it }
	var voidCreepersBiomeBL		= IntArray(4)
	var grantWireUnlimitedPower = true
	
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
	var potionIDSharedHP		= potionID___COUNTER++
	var potionIDShowMana		= potionID___COUNTER++
	var potionIDSoulburn		= potionID___COUNTER++
	var potionIDStoneSkin		= potionID___COUNTER++
	var potionIDTank			= potionID___COUNTER++
	var potionIDThrow			= potionID___COUNTER++
	var potionIDWellOLife		= potionID___COUNTER++
	
	// Elven Story
	var bothSpawnStructures		= false
	var enableWingsNonAlfheim	= true
	var flightTime				= 1200
	val zones					= Array(9) { Vector3(0.0) }
	
	// MMO
	var deathScreenAddTime		= 1200
	var frienldyFire			= false
	var maxPartyMembers			= 5
	
	// HUD
	var partyHUDScale			= 1.0
	var selfHealthUI			= true
	var targetUI				= true
	
	fun loadConfig(suggestedConfigurationFile: File) {
		config = Configuration(suggestedConfigurationFile)
		
		config.load()
		config.addCustomCategoryComment(CATEGORY_DIMENSION,		"Alfheim dimension settings")
		config.addCustomCategoryComment(CATEGORY_WORLDGEN,		"Alfheim worldgen settings")
		config.addCustomCategoryComment(CATEGORY_POTIONS,		"Potion IDs")
		config.addCustomCategoryComment(CATEGORY_ESMODE,		"Elven Story optional features")
		config.addCustomCategoryComment(CATEGORY_MMO,			"Elven Story optional features")
		config.addCustomCategoryComment(CATEGORY_HUD,			"HUD elements customizations")
		
		syncConfig()
		FMLCommonHandler.instance().bus().register(AlfheimChangeListener())
	}
	
	fun syncConfig() {
		voidCreepersBiomeBL[0] = 8
		voidCreepersBiomeBL[1] = 9
		voidCreepersBiomeBL[2] = 14
		voidCreepersBiomeBL[3] = 15
		
		biomeIDAlfheim			= loadProp(CATEGORY_DIMENSION,	"biomeIDAlfheim",				biomeIDAlfheim,			true,	"Biome ID for standart biome")
		dimensionIDAlfheim		= loadProp(CATEGORY_DIMENSION,	"dimensionIDAlfheim",			dimensionIDAlfheim,		true,	"Dimension ID for Alfheim")
		enableAlfheimRespawn	= loadProp(CATEGORY_DIMENSION,	"enableAlfheimRespawn",			enableAlfheimRespawn,	false,	"Set this to false to disable respawning in Alfheim")
		
		anomaliesDispersion		= loadProp(CATEGORY_WORLDGEN,	"anomaliesDispersion",			anomaliesDispersion,	false,	"How rare anomalies are (1/(N*2)% to gen in chunk)")
		anomaliesUpdate			= loadProp(CATEGORY_WORLDGEN,	"anomaliesUpdate",				anomaliesUpdate,		false,	"How many times anomaly will simulate tick while being generated")
		citiesDistance			= loadProp(CATEGORY_WORLDGEN,	"citiesDistance",				citiesDistance,			true,	"Distance between any elven city and worlds center")
		oregenMultiplier		= loadProp(CATEGORY_WORLDGEN,	"oregenMultiplier",				oregenMultiplier,		true,	"Multiplier for Alfheim oregen")
		
		destroyPortal			= loadProp(CATEGORY_GENERAL,	"destroyPortal",				destroyPortal,			false,	"Set this to false to disable destroying portals in non-zero coords in Alfheim")
		fancies					= loadProp(CATEGORY_GENERAL,	"fancies",						fancies,				false,	"Set this to false to disable fancies rendering on you ([CLIENTSIDE] for contributors only)")
		flugelBossBar			= loadProp(CATEGORY_GENERAL,	"flugelBossBar",				flugelBossBar,			false,	"Set this to false to disable displaying flugel's boss bar")
		info					= loadProp(CATEGORY_GENERAL,	"info",							info,					false,	"Set this to false to disable loading info about addon")
		lightningsSpeed			= loadProp(CATEGORY_GENERAL,	"lightningsSpeed",				lightningsSpeed,		false,	"How many ticks it takes between two lightings are spawned in Lightning Anomaly render")
		lolicornAlfheimOnly		= loadProp(CATEGORY_GENERAL,	"lolicornAlfheimOnly",			lolicornAlfheimOnly,	false,	"Set this to false to make lolicorn summonable in any dimension")
		lolicornCost			= loadProp(CATEGORY_GENERAL,	"lolicornCost",					lolicornCost,			false,	"How much mana lolicorn consumes on summoning (not teleporting)")
		lolicornLife			= loadProp(CATEGORY_GENERAL,	"lolicornLife",					lolicornLife,			false,	"How long lolicorn can stay unmounted")
		looniumOverseed			= loadProp(CATEGORY_GENERAL,	"looniumOverseed",				looniumOverseed,		true,	"Set this to true to make loonium spawn overgrowth seeds (for servers with limited dungeons so all players can craft Gaia pylons)")
		minimalGraphics			= loadProp(CATEGORY_GENERAL,	"minimalGraphics",				minimalGraphics,		false,	"Set this to true to disable .obj models and shaders")
		numericalMana			= loadProp(CATEGORY_GENERAL,	"numericalMana",				numericalMana,			false,	"Set this to false to disable numerical mana representation")
		slowDownClients			= loadProp(CATEGORY_GENERAL,	"slowDownClients",				slowDownClients,		false,	"Set this to true to slowdown players on clients while in anomaly")
		tradePortalRate			= loadProp(CATEGORY_GENERAL,	"tradePortalRate",				tradePortalRate,		false,	"Portal updates every {N} ticks")
		
		realLightning			= loadProp(CATEGORY_BA,			"realLightning",				realLightning,			false,	"Set this to true to make lightning rod summon real (weather) lightning")
		addTincturemAspect		= loadProp(CATEGORY_BA,			"TC.tincturem",					addTincturemAspect,		true,	"[TC] Set this to false to use Sensus instead of Color aspect")
		addAspectsToBotania		= loadProp(CATEGORY_BA,			"TC.botaniaAspects",			addAspectsToBotania,	true,	"[TC] Set this to false to disable adding aspects to Botania")
		addThaumTreeSuffusion	= loadProp(CATEGORY_BA,			"TC.treeCrafting",				addThaumTreeSuffusion,	true,	"[TC] [GoG] Set this to false to remove Thaumcraft plants Dendric Suffusion")
		grantWireUnlimitedPower = loadProp(CATEGORY_BA,			"wire.overpowered",				grantWireUnlimitedPower,true,	"Allow WireSegal far more power than any one person should have")
		blackLotusDropRate		= loadProp(CATEGORY_BA,			"blackLotusDropRate",			blackLotusDropRate,		false,	"Rate of black loti dropping from Manaseal Creepers")
		schemaArray				= loadProp(CATEGORY_BA,			"schemaArray",					schemaArray,			true,	"Which schemas are allowed to be generated")
		voidCreepersBiomeBL		= loadProp(CATEGORY_BA,			"voidCreepersBiomeBL",			voidCreepersBiomeBL,	true,	"Biome blacklist for Manaseal Creepers")
		
		potionSlots				= loadProp(CATEGORY_POTIONS,	"potionSlots",					potionSlots,			true,	"Available potions ids in range [0-potionSlots)")
		potionIDBerserk			= loadProp(CATEGORY_POTIONS,	"potionIDBerserk",				potionIDBerserk,		true,	"Potion id for Berserk")
		potionIDBleeding		= loadProp(CATEGORY_MMOP,		"potionIDBleeding",				potionIDBleeding,		true,	"Potion id for Bleeding")
		potionIDButterShield	= loadProp(CATEGORY_MMOP,		"potionIDButterShield",			potionIDButterShield,	true,	"Potion id for Butterfly Shield")
		potionIDDeathMark		= loadProp(CATEGORY_MMOP,		"potionIDDeathMark",			potionIDDeathMark,		true,	"Potion id for Death Mark")
		potionIDDecay			= loadProp(CATEGORY_MMOP,		"potionIDDecay",				potionIDDecay,			true,	"Potion id for Decay")
		potionIDEternity		= loadProp(CATEGORY_POTIONS,	"potionIDEternity",				potionIDEternity,		true,	"Potion id for Eternity")
		potionIDGoldRush		= loadProp(CATEGORY_MMOP,		"potionIDGoldRush",				potionIDGoldRush,		true,	"Potion id for Gold Rush")
		potionIDIceLens			= loadProp(CATEGORY_MMOP,		"potionIDIceLens",				potionIDIceLens,		true,	"Potion id for Ice Lense")
		potionIDLeftFlame		= loadProp(CATEGORY_MMOP,		"potionIDLeftFlame",			potionIDLeftFlame,		true,	"Potion id for Leftover Flame")
		potionIDManaVoid		= loadProp(CATEGORY_POTIONS,	"potionIDManaVoid",				potionIDManaVoid,		true,	"Potion id for Mana Void")
		potionIDNineLifes		= loadProp(CATEGORY_POTIONS,	"potionIDNineLifes",			potionIDNineLifes,		true,	"Potion id for Nine Lifes")
		potionIDNinja			= loadProp(CATEGORY_POTIONS,	"potionIDNinja",				potionIDNinja,			true,	"Potion id for Ninja")
		potionIDNoclip			= loadProp(CATEGORY_MMOP,		"potionIDNoclip",				potionIDNoclip,			true,	"Potion id for Noclip")
		potionIDOvermage		= loadProp(CATEGORY_POTIONS,	"potionIDOvermage",				potionIDOvermage,		true,	"Potion id for Overmage")
		potionIDPossession		= loadProp(CATEGORY_POTIONS,	"potionIDPossession",			potionIDPossession,		true,	"Potion id for Possession")
		potionIDQuadDamage		= loadProp(CATEGORY_MMOP,		"potionIDQuadDamage",			potionIDQuadDamage,		true,	"Potion id for Quad Damage")
		potionIDSacrifice		= loadProp(CATEGORY_MMOP,		"potionIDSacrifice",			potionIDSacrifice,		true,	"Potion id for Sacrifice")
		potionIDSharedHP		= loadProp(CATEGORY_MMOP,		"potionIDSharedHP",				potionIDSharedHP,		true,	"Potion id for Shared Health Pool")
		potionIDShowMana		= loadProp(CATEGORY_MMOP,		"potionIDShowMana",				potionIDShowMana,		true,	"Potion id for Mana Showing Effect")
		potionIDSoulburn		= loadProp(CATEGORY_POTIONS,	"potionIDSoulburn",				potionIDSoulburn,		true,	"Potion id for Soulburn")
		potionIDStoneSkin		= loadProp(CATEGORY_MMOP,		"potionIDStoneSkin",			potionIDStoneSkin,		true,	"Potion id for Stone Skin")
		potionIDTank			= loadProp(CATEGORY_POTIONS,	"potionIDTank",					potionIDTank,			true,	"Potion id for Tank")
		potionIDThrow			= loadProp(CATEGORY_MMOP,		"potionIDThrow",				potionIDThrow,			true,	"Potion id for Throw")
		potionIDWellOLife		= loadProp(CATEGORY_MMOP,		"potionIDWellOLife",			potionIDWellOLife,		true,	"Potion id for Well'o'Life")
		
		bothSpawnStructures		= loadProp(CATEGORY_ESMODE,		"bothSpawnStructures",			bothSpawnStructures,	false,	"Set this to true to generate both cube and castle (!contains portal!) on zero coords of Alfheim")
		enableWingsNonAlfheim	= loadProp(CATEGORY_ESMODE,		"enableWingsNonAlfheim",		enableWingsNonAlfheim,	false,	"Set this to false to disable wings in other worlds")
		flightTime				= loadProp(CATEGORY_ESMODE,		"flightTime",					flightTime,				true,	"How long can you fly as elf")
		
		deathScreenAddTime		= loadProp(CATEGORY_MMO,		"deathScreenAdditionalTime",	deathScreenAddTime,		false,	"How longer (in ticks) 'Respawn' button will be unavailable")
		frienldyFire			= loadProp(CATEGORY_MMO,		"frienldyFire",					frienldyFire,			false,	"Set this to true to enable da,age to party members")
		maxPartyMembers			= loadProp(CATEGORY_MMO,		"maxPartyMembers",				maxPartyMembers,		false,	"How many people can be in single party at the same time")
		
		partyHUDScale			= loadProp(CATEGORY_HUD,		"partyHUDScale",				partyHUDScale,			false,	"Party HUD Scale (1 < bigger; 1 > smaller)")
		selfHealthUI			= loadProp(CATEGORY_HUD,		"selfHealthUI",					selfHealthUI,			false,	"Set this to false to hide player's healthbar")
		targetUI				= loadProp(CATEGORY_HUD,		"targethUI",					targetUI,				false,	"Set this to false to hide target's healthbar")
		
		if (config.hasChanged()) {
			config.save()
		}
		
		RenderPostShaders.allowShaders = !minimalGraphics
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
		if (ss.size != 3) throw IllegalArgumentException(String.format("Wrong coords count. Expected 3 got %d", ss.size))
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
			
			if (flags.size != 2) throw IllegalArgumentException(String.format("Wrong flags count. Expected 2 got %d", flags.size))
			
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
		if (!AlfheimCore.enableElvenStory && AlfheimCore.enableMMO) throw IllegalArgumentException("Unable to write modes state when ESM is disabled and MMO is enabled")
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

class AlfheimChangeListener {
	@SubscribeEvent
	fun onConfigChanged(e: OnConfigChangedEvent) {
		if (e.modID == ModInfo.MODID) AlfheimConfig.syncConfig()
	}
}