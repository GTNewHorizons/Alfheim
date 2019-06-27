package alfheim.common.integration.minetweaker.handler

import alfheim.api.*
import alfheim.api.crafting.recipe.RecipeManaInfuser
import alfheim.common.integration.minetweaker.MinetweakerAlfheimConfig.getObjects
import alfheim.common.integration.minetweaker.MinetweakerAlfheimConfig.getStack
import minetweaker.*
import minetweaker.api.item.*
import net.minecraft.item.ItemStack
import stanhebben.zenscript.annotations.*
import java.util.*

@ZenClass("mods." + ModInfo.MODID + ".ManaInfuser")
object MTHandlerManaInfuser {
	
	@ZenMethod
	fun addRecipe(output: IItemStack, mana: Int, vararg inputs: IIngredient) {
		MineTweakerAPI.apply(Add(RecipeManaInfuser(mana, getStack(output), *getObjects(inputs))))
	}
	
	@ZenMethod
	fun removeRecipe(output: IItemStack) {
		MineTweakerAPI.apply(Remove(getStack(output)))
	}
	
	private class Add(private val recipe: RecipeManaInfuser): IUndoableAction {
		
		override fun apply() {
			AlfheimAPI.addInfuserRecipe(recipe)
		}
		
		override fun canUndo(): Boolean {
			return true
		}
		
		override fun undo() {
			AlfheimAPI.removeInfusionRecipe(recipe)
		}
		
		override fun describe(): String {
			return String.format("Adding Mana Infuser recipe %s", recipe)
		}
		
		override fun describeUndo(): String {
			return String.format("Removing Mana Infuser recipe %s", recipe)
		}
		
		override fun getOverrideKey(): Any? {
			return null
		}
	}
	
	private class Remove(private val output: ItemStack): IUndoableAction {
		internal val removed = ArrayList<RecipeManaInfuser>()
		
		override fun apply() {
			var rec: RecipeManaInfuser?
			do {
				rec = AlfheimAPI.removeInfusionRecipe(output)
				removed.add(rec!!)
			} while (rec != null)
		}
		
		override fun canUndo(): Boolean {
			return true
		}
		
		override fun undo() {
			for (rec in removed) AlfheimAPI.addInfuserRecipe(rec)
		}
		
		override fun describe(): String {
			return String.format("Removing all Mana Infuser recipes for %s", output.unlocalizedName)
		}
		
		override fun describeUndo(): String {
			return String.format("Re-adding previously removed Mana Infuser recipes for %s", output.unlocalizedName)
		}
		
		override fun getOverrideKey(): Any? {
			return null
		}
	}
}