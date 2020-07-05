package alfheim.common.integration.minetweaker

import alfheim.api.*
import minetweaker.*
import stanhebben.zenscript.annotations.*

@ZenClass("mods." + ModInfo.MODID + ".Petronia")
object MTHandlerPetronia {
	
	@ZenMethod
	@JvmStatic
	fun registerFuel(name: String, burnTime: Int, manaPerTick: Int) {
		MineTweakerAPI.apply(Register(name, burnTime, manaPerTick))
	}
	
	@ZenMethod
	@JvmStatic
	fun removeFuel(name: String) {
		MineTweakerAPI.apply(Remove(name))
	}
	
	private class Register(val name: String, val burnTime: Int, val manaPerTick: Int): IUndoableAction {
		
		override fun apply() {
			AlfheimAPI.registerFuel(name, burnTime, manaPerTick)
		}
		
		override fun canUndo(): Boolean {
			return true
		}
		
		override fun undo() {
			AlfheimAPI.fuelMap.remove(name)
		}
		
		override fun describe(): String {
			return "Registering fuel $name"
		}
		
		override fun describeUndo(): String {
			return "Deleting fuel $name"
		}
		
		override fun getOverrideKey(): Any? {
			return null
		}
	}
	
	private class Remove(val name: String): IUndoableAction {
		
		var removed: Pair<Int, Int>? = null
		
		override fun apply() {
			removed = AlfheimAPI.fuelMap.remove(name)
		}
		
		override fun canUndo(): Boolean {
			return removed != null
		}
		
		override fun undo() {
			AlfheimAPI.registerFuel(name, removed!!.first, removed!!.second)
		}
		
		override fun describe(): String {
			return "Removing fuel data for $name"
		}
		
		override fun describeUndo(): String {
			return "Re-adding previously removed fuel data for $name"
		}
		
		override fun getOverrideKey(): Any? {
			return null
		}
	}
}
