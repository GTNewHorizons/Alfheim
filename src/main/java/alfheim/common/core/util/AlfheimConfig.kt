package alfheim.common.core.util

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.render.RenderPostShaders
import alfheim.AlfheimCore
import alfheim.api.ModInfo
import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.util.*
import net.minecraftforge.common.config.*

import java.io.*

class AlfheimConfig: Configuration() {
	
	class AlfheimChangeListener {
		@SubscribeEvent
		fun onConfigChanged(e: OnConfigChangedEvent) {
			if (e.modID == ModInfo.MODID) syncConfig()
		}
	}
	
	companion object {
		
		var config: Configuration
		
		val CATEGORY_DIMENSION = Configuration.CATEGORY_GENERAL + Configuration.CATEGORY_SPLITTER + "Alfheim"
		val CATEGORY_WORLDGEN = CATEGORY_DIMENSION + Configuration.CATEGORY_SPLITTER + "woldgen"
		val CATEGORY_POTIONS = Configuration.CATEGORY_GENERAL + Configuration.CATEGORY_SPLITTER + "potions"
		val CATEGORY_ESMODE = Configuration.CATEGORY_GENERAL + Configuration.CATEGORY_SPLITTER + "elvenstory"
		val CATEGORY_MMO = CATEGORY_ESMODE + Configuration.CATEGORY_SPLITTER + "mmo"
		val CATEGORY_MMOP = CATEGORY_MMO + Configuration.CATEGORY_SPLITTER + "potions"
		val CATEGORY_HUD = CATEGORY_MMO + Configuration.CATEGORY_SPLITTER + "hud"
		
		// DIMENSION
		var biomeIDAlfheim = 152
		var dimensionIDAlfheim = -105
		var enableAlfheimRespawn = true
		
		// WORLDGEN
		var anomaliesDispersion = 50
		var anomaliesUpdate = 6000
		var oregenMultiplier = 3
		
		// OHTER
		var destroyPortal = true
		var fancies = true
		var flugelBossBar = true
		var info = true
		var lightningsSpeed = 20
		var looniumOverseed = false
		var minimalGraphics = false
		var numericalMana = true
		var slowDownClients = false
		var tradePortalRate = 1200
		
		// POTIONS
		var potionID___COUNTER = 30
		var potionIDBerserk = potionID___COUNTER++
		var potionIDBleeding = potionID___COUNTER++
		var potionIDButterShield = potionID___COUNTER++
		var potionIDDeathMark = potionID___COUNTER++
		var potionIDDecay = potionID___COUNTER++
		var potionIDGoldRush = potionID___COUNTER++
		var potionIDIceLens = potionID___COUNTER++
		var potionIDLeftFlame = potionID___COUNTER++
		var potionIDNinja = potionID___COUNTER++
		val potionIDNineLifes = potionID___COUNTER++
		var potionIDNoclip = potionID___COUNTER++
		var potionIDOvermage = potionID___COUNTER++
		var potionIDPossession = potionID___COUNTER++
		var potionIDQuadDamage = potionID___COUNTER++
		var potionIDSacrifice = potionID___COUNTER++
		var potionIDSharedHP = potionID___COUNTER++
		var potionIDShowMana = potionID___COUNTER++
		var potionIDSoulburn = potionID___COUNTER++
		var potionIDStoneSkin = potionID___COUNTER++
		var potionIDTank = potionID___COUNTER++
		var potionIDThrow = potionID___COUNTER++
		var potionIDWellOLife = potionID___COUNTER++
		
		// Elven Story
		var bothSpawnStructures = false
		var enableWingsNonAlfheim = true
		var flightTime = 1200
		val zones = arrayOfNulls<Vec3>(9)
		
		// MMO
		var deathScreenAddTime = 1200
		var frienldyFire = false
		var maxPartyMembers = 5
		
		// HUD
		var partyHUDScale = 1.0
		var selfHealthUI = true
		var targetUI = true
		
		fun loadConfig(suggestedConfigurationFile: File) {
			config = Configuration(suggestedConfigurationFile)
			
			config.load()
			config.addCustomCategoryComment(CATEGORY_DIMENSION, "Alfheim dimension settings")
			config.addCustomCategoryComment(CATEGORY_WORLDGEN, "Alfheim worldgen settings")
			config.addCustomCategoryComment(CATEGORY_POTIONS, "Potion IDs")
			config.addCustomCategoryComment(CATEGORY_ESMODE, "Elven Story optional features")
			config.addCustomCategoryComment(CATEGORY_MMO, "Elven Story optional features")
			config.addCustomCategoryComment(CATEGORY_HUD, "HUD elements customizations")
			
			syncConfig()
			FMLCommonHandler.instance().bus().register(AlfheimChangeListener())
		}
		
		fun syncConfig() {
			biomeIDAlfheim = loadProp(CATEGORY_DIMENSION, "biomeIDAlfheim", biomeIDAlfheim, true, "Biome ID for standart biome")
			dimensionIDAlfheim = loadProp(CATEGORY_DIMENSION, "dimensionIDAlfheim", dimensionIDAlfheim, true, "Dimension ID for Alfheim")
			enableAlfheimRespawn = loadProp(CATEGORY_DIMENSION, "enableAlfheimRespawn", enableAlfheimRespawn, false, "Set this to false to disable respawning in Alfheim")
			
			anomaliesDispersion = loadProp(CATEGORY_WORLDGEN, "anomaliesDispersion", anomaliesDispersion, false, "How rare anomalies are (1/(N*2)% to gen in chunk)")
			anomaliesUpdate = loadProp(CATEGORY_WORLDGEN, "anomaliesUpdate", anomaliesUpdate, false, "How many times anomaly will simulate tick while being generated")
			oregenMultiplier = loadProp(CATEGORY_WORLDGEN, "oregenMultiplier", oregenMultiplier, true, "Multiplier for Alfheim oregen")
			
			destroyPortal = loadProp(Configuration.CATEGORY_GENERAL, "destroyPortal", destroyPortal, false, "Set this to false to disable destroying portals in non-zero coords in Alfheim")
			fancies = loadProp(Configuration.CATEGORY_GENERAL, "fancies", fancies, false, "Set this to false to disable fancies rendering on you ([CLIENTSIDE] for contributors only)")
			flugelBossBar = loadProp(Configuration.CATEGORY_GENERAL, "flugelBossBar", flugelBossBar, false, "Set this to false to disable displaying flugel's boss bar")
			info = loadProp(Configuration.CATEGORY_GENERAL, "info", info, false, "Set this to false to disable loading info about addon")
			lightningsSpeed = loadProp(Configuration.CATEGORY_GENERAL, "lightningsSpeed", lightningsSpeed, false, "How many ticks it takes between two lightings are spawned in Lightning Anomaly render")
			looniumOverseed = loadProp(Configuration.CATEGORY_GENERAL, "looniumOverseed", looniumOverseed, true, "Set this to true to make loonium spawn overgrowth seeds (for servers with limited dungeons so all players can craft Gaia pylons)")
			minimalGraphics = loadProp(Configuration.CATEGORY_GENERAL, "minimalGraphics", minimalGraphics, false, "Set this to true to disable .obj models and shaders")
			numericalMana = loadProp(Configuration.CATEGORY_GENERAL, "numericalMana", numericalMana, false, "Set this to false to disable numerical mana representation")
			slowDownClients = loadProp(Configuration.CATEGORY_GENERAL, "slowDownClients", slowDownClients, false, "Set this to true to slowdown players on clients while in anomaly")
			tradePortalRate = loadProp(Configuration.CATEGORY_GENERAL, "tradePortalRate", tradePortalRate, false, "Portal updates every {N} ticks")
			
			potionIDBerserk = loadProp(CATEGORY_POTIONS, "potionIDBerserk", potionIDBerserk, true, "Potion id for Berserk")
			potionIDBleeding = loadProp(CATEGORY_MMOP, "potionIDBleeding", potionIDBleeding, true, "Potion id for Bleeding")
			potionIDButterShield = loadProp(CATEGORY_MMOP, "potionIDButterShield", potionIDButterShield, true, "Potion id for Butterfly Shield")
			potionIDDeathMark = loadProp(CATEGORY_MMOP, "potionIDDeathMark", potionIDDeathMark, true, "Potion id for Death Mark")
			potionIDDecay = loadProp(CATEGORY_MMOP, "potionIDDecay", potionIDDecay, true, "Potion id for Decay")
			potionIDGoldRush = loadProp(CATEGORY_MMOP, "potionIDGoldRush", potionIDGoldRush, true, "Potion id for Gold Rush")
			potionIDIceLens = loadProp(CATEGORY_MMOP, "potionIDIceLens", potionIDIceLens, true, "Potion id for Ice Lense")
			potionIDLeftFlame = loadProp(CATEGORY_MMOP, "potionIDLeftFlame", potionIDLeftFlame, true, "Potion id for Leftover Flame")
			potionIDNinja = loadProp(CATEGORY_POTIONS, "potionIDNinja", potionIDNinja, true, "Potion id for Ninja")
			potionIDNoclip = loadProp(CATEGORY_MMOP, "potionIDNoclip", potionIDNoclip, true, "Potion id for Noclip")
			potionIDOvermage = loadProp(CATEGORY_POTIONS, "potionIDOvermage", potionIDOvermage, true, "Potion id for Overmage")
			potionIDPossession = loadProp(CATEGORY_POTIONS, "potionIDPossession", potionIDPossession, true, "Potion id for Possession")
			potionIDQuadDamage = loadProp(CATEGORY_MMOP, "potionIDQuadDamage", potionIDQuadDamage, true, "Potion id for Quad Damage")
			potionIDSacrifice = loadProp(CATEGORY_MMOP, "potionIDSacrifice", potionIDSacrifice, true, "Potion id for Sacrifice")
			potionIDSharedHP = loadProp(CATEGORY_MMOP, "potionIDSharedHP", potionIDSharedHP, true, "Potion id for Shared Health Pool")
			potionIDShowMana = loadProp(CATEGORY_MMOP, "potionIDShowMana", potionIDShowMana, true, "Potion id for Mana Showing Effect")
			potionIDSoulburn = loadProp(CATEGORY_POTIONS, "potionIDSoulburn", potionIDSoulburn, true, "Potion id for Soulburn")
			potionIDStoneSkin = loadProp(CATEGORY_MMOP, "potionIDStoneSkin", potionIDStoneSkin, true, "Potion id for Stone Skin")
			potionIDTank = loadProp(CATEGORY_POTIONS, "potionIDTank", potionIDTank, true, "Potion id for Tank")
			potionIDThrow = loadProp(CATEGORY_MMOP, "potionIDThrow", potionIDThrow, true, "Potion id for Throw")
			potionIDWellOLife = loadProp(CATEGORY_MMOP, "potionIDWellOLife", potionIDWellOLife, true, "Potion id for Well'o'Life")
			
			bothSpawnStructures = loadProp(CATEGORY_ESMODE, "bothSpawnStructures", bothSpawnStructures, false, "Set this to true to generate both cube and castle (!contains portal!) on zero coords of Alfheim")
			enableWingsNonAlfheim = loadProp(CATEGORY_ESMODE, "enableWingsNonAlfheim", enableWingsNonAlfheim, false, "Set this to false to disable wings in other worlds")
			flightTime = loadProp(CATEGORY_ESMODE, "flightTime", flightTime, true, "How long can you fly as elf")
			
			deathScreenAddTime = loadProp(CATEGORY_MMO, "deathScreenAdditionalTime", deathScreenAddTime, false, "How longer (in ticks) \"Respawn\" button will be unavailable")
			frienldyFire = loadProp(CATEGORY_MMO, "frienldyFire", frienldyFire, false, "Set this to true to enable da,age to party members")
			maxPartyMembers = loadProp(CATEGORY_MMO, "maxPartyMembers", maxPartyMembers, false, "How many people can be in single party at the same time")
			
			partyHUDScale = loadProp(CATEGORY_HUD, "partyHUDScale", partyHUDScale, false, "Party HUD Scale (1 < bigger; 1 > smaller)")
			selfHealthUI = loadProp(CATEGORY_HUD, "selfHealthUI", selfHealthUI, false, "Set this to false to hide player's healthbar")
			targetUI = loadProp(CATEGORY_HUD, "targethUI", targetUI, false, "Set this to false to hide target's healthbar")
			
			if (config.hasChanged()) {
				config.save()
			}
			
			RenderPostShaders.allowShaders = !minimalGraphics
		}
		
		fun loadProp(category: String, propName: String, default_: Int, restart: Boolean, desc: String): Int {
			val prop = config.get(category, propName, default_)
			prop.comment = desc
			prop.setRequiresMcRestart(restart)
			return prop.getInt(default_)
		}
		
		fun loadProp(category: String, propName: String, default_: Double, restart: Boolean, desc: String): Double {
			val prop = config.get(category, propName, default_)
			prop.comment = desc
			prop.setRequiresMcRestart(restart)
			return prop.getDouble(default_)
		}
		
		fun loadProp(category: String, propName: String, default_: Boolean, restart: Boolean, desc: String): Boolean {
			val prop = config.get(category, propName, default_)
			prop.comment = desc
			prop.setRequiresMcRestart(restart)
			return prop.getBoolean(default_)
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
				var angle = 0.0
				s.append("Salamander start city and players spawnpoint coords:\n")
				s.append(writeStandardCoords(angle))
				s.append("Sylph start city and players spawnpoint coords:\n")
				s.append(writeStandardCoords(angle += 40.0))
				s.append("Cait Sith start city and players spawnpoint coords:\n")
				s.append(writeStandardCoords(angle += 40.0))
				s.append("Puca start city and players spawnpoint coords:\n")
				s.append(writeStandardCoords(angle += 40.0))
				s.append("Gnome start city and players spawnpoint coords:\n")
				s.append(writeStandardCoords(angle += 40.0))
				s.append("Leprechaun start city and players spawnpoint coords:\n")
				s.append(writeStandardCoords(angle += 40.0))
				s.append("Spriggan start city and players spawnpoint coords:\n")
				s.append(writeStandardCoords(angle += 40.0))
				s.append("Undine start city and players spawnpoint coords:\n")
				s.append(writeStandardCoords(angle += 40.0))
				s.append("Imp start city and players spawnpoint coords:\n")
				s.append(writeStandardCoords(angle + 40))
				fw.write(s.toString())
				fw.close()
			} catch (e: IOException) {
				ASJUtilities.error("Unable to generate default Alfheim Coords data. Setting all to [0, 300, 0]...")
				e.printStackTrace()
				
				for (i in zones.indices) {
					zones[i] = Vec3.createVectorHelper(0.0, 300.0, 0.0)
				}
			}
			
		}
		
		private fun writeStandardCoords(angle: Double): String {
			val v = mkVecLenRotMine(1000.0, angle)
			return MathHelper.floor_double(v.xCoord).toString() + " : 300 : " + MathHelper.floor_double(v.zCoord) + "\n"
		}
		
		private fun makeVectorFromString(s: String): Vec3 {
			val ss = s.split(" : ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
			if (ss.size != 3) throw IllegalArgumentException(String.format("Wrong coords count. Expected 3 got %d", ss.size))
			return Vec3.createVectorHelper(Integer.valueOf(ss[0]).toDouble(), Integer.valueOf(ss[1]).toDouble(), Integer.valueOf(ss[2]).toDouble())
		}
		
		private fun mkVecLenRotMine(length: Double, angle: Double): Vec3 {
			return makeVectorOfLengthRotated(length, angle + 90)
		}
		
		private fun makeVectorOfLengthRotated(length: Double, angle: Double): Vec3 {
			return Vec3.createVectorHelper(Math.cos(Math.toRadians(angle)) * length, 64.0, Math.sin(Math.toRadians(angle)) * length)
		}
		
		fun readModes() {
			val f = File("config/Alfheim/ElvenStoryMode.cfg")
			if (!f.exists()) return
			try {
				val fr = FileReader(f)
				val br = BufferedReader(fr)
				br.readLine()
				val flags = br.readLine().split(" ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
				
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
					if (flags[1] == "false")
						AlfheimCore.enableMMO = false
					else if (flags[1] == "true")
						AlfheimCore.enableMMO = true
					else
						throw IllegalArgumentException(String.format("Unknown param value for MMO mode: %s", flags[1]))
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
}