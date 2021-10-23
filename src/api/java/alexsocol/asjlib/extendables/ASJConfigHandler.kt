package alexsocol.asjlib.extendables

import cpw.mods.fml.client.event.ConfigChangedEvent
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.eventhandler.*
import net.minecraftforge.common.config.Configuration
import java.io.File

abstract class ASJConfigHandler {
	
	lateinit var config: Configuration
	
	/**
	 * Function called once in [FMLPreInitializationEvent]
	 * to initialize categories and properties
	 */
	fun loadConfig(cfg: File) {
		config = Configuration(cfg)
		config.load()
		addCategories()
		syncConfig()
	}
	
	/**
	 * Call this if you have config GUI for your mod
	 */
	fun registerChangeHandler(modid: String) {
		FMLCommonHandler.instance().bus().register(object {
			@SubscribeEvent(priority = EventPriority.HIGHEST)
			fun onConfigChanged(e: ConfigChangedEvent.OnConfigChangedEvent) {
				if (e.modID == modid) syncConfig()
			}
		})
	}
	
	fun addCategory(cat: String, comment: String) {
		config.addCustomCategoryComment(cat, comment)
		config.getCategory(cat).setLanguageKey(cat)
	}
	
	fun syncConfig() {
		readProperties()
		
		if (config.hasChanged()) config.save()
	}
	
	open fun addCategories() = Unit
	
	abstract fun readProperties()
	
	fun loadProp(category: String, propName: String, default: Boolean, restart: Boolean, desc: String?): Boolean {
		val prop = config.get(category, propName, default, desc)
		prop.setRequiresMcRestart(restart)
		return prop.getBoolean(default)
	}
	
	fun loadProp(category: String, propName: String, default: Int, restart: Boolean, desc: String?, min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE): Int {
		val prop = config.get(category, propName, default, desc, min, max)
		prop.setRequiresMcRestart(restart)
		return prop.getInt(default)
	}
	
	fun loadProp(category: String, propName: String, default: IntArray, restart: Boolean, desc: String?, ensureLength: Boolean = true): IntArray {
		val prop = config.get(category, propName, default, desc)
		prop.setRequiresMcRestart(restart)
		return prop.intList.also { if (ensureLength && it.size < default.size) throw IllegalArgumentException("Array $propName is not of suitable length (${it.size}), must be at least ${default.size}") }
	}
	
	fun loadProp(category: String, propName: String, default: Double, restart: Boolean, desc: String?): Double {
		val prop = config.get(category, propName, default, desc)
		prop.setRequiresMcRestart(restart)
		return prop.getDouble(default)
	}
	
	fun loadProp(category: String, propName: String, default: DoubleArray, restart: Boolean, desc: String?, ensureLength: Boolean = true): DoubleArray {
		val prop = config.get(category, propName, default, desc)
		prop.setRequiresMcRestart(restart)
		return prop.doubleList.also { if (ensureLength && it.size < default.size) throw IllegalArgumentException("Array $propName is not of suitable length (${it.size}), must be at least ${default.size}") }
	}
	
	fun loadProp(category: String, propName: String, default: String, restart: Boolean, desc: String?): String {
		val prop = config.get(category, propName, default, desc)
		prop.setRequiresMcRestart(restart)
		return prop.string
	}
	
	fun loadProp(category: String, propName: String, default: Array<String>, restart: Boolean, desc: String?, ensureLength: Boolean = true): Array<String> {
		val prop = config.get(category, propName, default, desc)
		prop.setRequiresMcRestart(restart)
		return prop.stringList.also { if (ensureLength && it.size < default.size) throw IllegalArgumentException("Array $propName is not of suitable length (${it.size}), must be at least ${default.size}") }
	}
}
