package alfheim.common.integration.minetweaker

import alfheim.api.*
import alfheim.common.integration.minetweaker.MinetweakerAlfheimConfig.getStack
import minetweaker.*
import minetweaker.api.item.IItemStack
import net.minecraft.item.ItemStack
import stanhebben.zenscript.annotations.*

@ZenClass("mods." + ModInfo.MODID + ".TradePortal")
object MTHandlerTradePortal {
	
	@ZenMethod
	@JvmStatic
	fun banRetrade(output: IItemStack) {
		MineTweakerAPI.apply(Ban(getStack(output)))
	}
	
	private class Ban(private val output: ItemStack): IUndoableAction {
		
		override fun apply() {
			AlfheimAPI.banRetrade(output)
		}
		
		override fun canUndo(): Boolean {
			return false
		}
		
		override fun undo() {
			throw IllegalArgumentException("Don't cheat!")
		}
		
		override fun describe(): String {
			return String.format("Removing %s from Alfheim trade portal", output.unlocalizedName)
		}
		
		override fun describeUndo(): String {
			throw IllegalArgumentException("Don't cheat!")
		}
		
		override fun getOverrideKey(): Any? {
			return null
		}
	}
}
