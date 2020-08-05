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
	
	private class Register(val name: String, val burnTime: Int, val manaPerTick: Int): IUndoableAction {
		
		var prev: Pair<Int, Int>? = null
		
		override fun apply() {
			prev = AlfheimAPI.fuelMap.remove(name)
			AlfheimAPI.registerFuel(name, burnTime, manaPerTick)
		}
		
		override fun canUndo(): Boolean {
			return prev != null
		}
		
		override fun undo() {
			AlfheimAPI.fuelMap[name] = prev!!
		}
		
		override fun describe(): String {
			return "Setting fuel values for $name"
		}
		
		override fun describeUndo(): String {
			return "Reverting fuel values for $name"
		}
		
		override fun getOverrideKey(): Any? {
			return null
		}
	}
}
