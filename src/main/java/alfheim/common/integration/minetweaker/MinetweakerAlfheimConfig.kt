package alfheim.common.integration.minetweaker

import minetweaker.MineTweakerAPI
import minetweaker.api.item.*
import minetweaker.api.minecraft.MineTweakerMC
import minetweaker.api.oredict.IOreDictEntry

object MinetweakerAlfheimConfig {
	
	fun loadConfig() {
		MineTweakerAPI.registerClass(MTHandlerAnyavil::class.java)
		MineTweakerAPI.registerClass(MTHandlerManaInfuser::class.java)
		MineTweakerAPI.registerClass(MTHandlerPetronia::class.java)
		MineTweakerAPI.registerClass(MTHandlerSpells::class.java)
		MineTweakerAPI.registerClass(MTHandlerSuffuser::class.java)
		MineTweakerAPI.registerClass(MTHandlerTradePortal::class.java)
	}
	
	fun getStack(istack: IItemStack) = MineTweakerMC.getItemStack(istack)!!
	
	/// #### original code from modtweaker2.helpers.InputHelper ####
	
	fun getObject(iStack: IIngredient?): Any {
		return if (iStack == null)
			""
		else {
			when (iStack) {
				is IOreDictEntry -> getString(iStack)
				is IItemStack    -> getStack(iStack)
				else             -> ""
			}
		}
	}
	
	fun getObjects(ingredient: Array<IIngredient?>?): Array<Any> {
		return if (ingredient == null)
			emptyArray()
		else {
			val output = ArrayList<Any>()
			for (i in ingredient.indices)
				output.add(if (ingredient[i] != null) getObject(ingredient[i]) else "")
			
			output.toArray()
		}
	}
	
	fun getString(entry: IOreDictEntry) = entry.name!!
}