package alfheim.common.integration.minetweaker

import alfheim.common.integration.minetweaker.handler.*
import minetweaker.MineTweakerAPI
import minetweaker.api.item.IIngredient
import minetweaker.api.item.IItemStack
import minetweaker.api.minecraft.MineTweakerMC
import minetweaker.api.oredict.IOreDictEntry
import net.minecraft.item.ItemStack

object MinetweakerAlfheimConfig {
	
	fun loadConfig() {
		MineTweakerAPI.registerClass(MTHandlerAnyavil::class.java!!)
		MineTweakerAPI.registerClass(MTHandlerManaInfuser::class.java!!)
		MineTweakerAPI.registerClass(MTHandlerSpells::class.java!!)
		MineTweakerAPI.registerClass(MTHandlerTradePortal::class.java!!)
	}
	
	fun getStack(istack: IItemStack): ItemStack {
		return MineTweakerMC.getItemStack(istack)
	}
	
	/// #### original code from modtweaker2.helpers.InputHelper ####
	
	fun getObject(iStack: IIngredient?): Any? {
		return if (iStack == null)
			null
		else {
			if (iStack is IOreDictEntry) {
				getString((iStack as IOreDictEntry?)!!)
			} else if (iStack is IItemStack) {
				getStack(iStack as IItemStack?)
			} else
				null
		}
	}
	
	fun getObjects(ingredient: Array<IIngredient>?): Array<Any>? {
		if (ingredient == null)
			return null
		else {
			val output = arrayOfNulls<Any>(ingredient.size)
			for (i in ingredient.indices) {
				if (ingredient[i] != null) {
					output[i] = getObject(ingredient[i])
				} else
					output[i] = ""
			}
			
			return output
		}
	}
	
	fun getString(entry: IOreDictEntry): String {
		return entry.name
	}
}