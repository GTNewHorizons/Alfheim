package alfheim.common.integration.minetweaker

import alfheim.api.*
import alfheim.common.integration.minetweaker.MinetweakerAlfheimConfig.getStack
import minetweaker.*
import minetweaker.api.item.IItemStack
import net.minecraft.item.ItemStack
import stanhebben.zenscript.annotations.*

@ZenClass("mods." + ModInfo.MODID + ".Anyavil")
object MTHandlerAnyavil {
	
	@ZenMethod
	@JvmStatic
	fun pinkify(input: IItemStack, pinkness: Int) {
		MineTweakerAPI.apply(Pinkifier(getStack(input), pinkness))
	}
	
	private class Pinkifier(private val output: ItemStack, private val pinkness: Int): IUndoableAction {
		
		var old = 0
		
		override fun apply() {
			val i = AlfheimAPI.addPink(output, pinkness)
			if (i != null) old = i
		}
		
		override fun canUndo(): Boolean {
			return true
		}
		
		override fun undo() {
			AlfheimAPI.pinkness[output] = old
		}
		
		override fun describe(): String {
			return String.format("Mapping new (%d) pinkness weight for %s", pinkness, output.unlocalizedName)
		}
		
		override fun describeUndo(): String {
			return String.format("Mapping previous (%d) pinkness weight for %s", old, output.unlocalizedName)
		}
		
		override fun getOverrideKey(): Any? {
			return null
		}
	}
}