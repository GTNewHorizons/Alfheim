package alfheim.common.integration.minetweaker

import alfheim.common.integration.minetweaker.handler.*
import minetweaker.MineTweakerAPI
import minetweaker.api.item.*
import minetweaker.api.minecraft.MineTweakerMC
import minetweaker.api.oredict.IOreDictEntry
import net.minecraft.item.ItemStack

object MinetweakerAlfheimConfig {
	
	fun loadConfig() {
		MineTweakerAPI.registerClass(MTHandlerAnyavil::class.java)
		MineTweakerAPI.registerClass(MTHandlerManaInfuser::class.java)
		MineTweakerAPI.registerClass(MTHandlerSpells::class.java)
		MineTweakerAPI.registerClass(MTHandlerTradePortal::class.java)
	}
	
	fun getStack(istack: IItemStack): ItemStack {
		return MineTweakerMC.getItemStack(istack)
	}
	
	/// #### original code from modtweaker2.helpers.InputHelper ####
	
	fun getObject(iStack: IIngredient?): Any {
		return if (iStack == null)
			""
		else {
			when (iStack) {
				is IOreDictEntry  -> getString(iStack)
				is IItemStack -> getStack(iStack)
				else      -> ""
			}
		}
	}
	
	fun getObjects(ingredient: Array<out IIngredient?>?): Array<Any> {
		return if (ingredient == null)
			emptyArray()
		else {
			val output = arrayOf<Any>(ingredient.size)
			for (i in ingredient.indices)
				output[i] = if (ingredient[i] != null) getObject(ingredient[i]) else ""
			
			output
		}
	}
	
	fun getString(entry: IOreDictEntry): String {
		return entry.name
	}
}