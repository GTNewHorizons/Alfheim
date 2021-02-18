package alfheim.common.integration.minetweaker

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
	@JvmStatic
	fun addRecipe(output: IItemStack, mana: Int, inputs: Array<IIngredient?>) {
		MineTweakerAPI.apply(Add(RecipeManaInfuser(mana, getStack(output), *getObjects(inputs))))
	}
	
	@ZenMethod
	@JvmStatic
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
			return "Adding Mana Infuser recipe $recipe"
		}
		
		override fun describeUndo(): String {
			return "Removing Mana Infuser recipe $recipe"
		}
		
		override fun getOverrideKey(): Any? {
			return null
		}
	}
	
	private class Remove(private val output: ItemStack): IUndoableAction {
		
		val removed = ArrayList<RecipeManaInfuser>()
		
		override fun apply() {
			var rec = AlfheimAPI.removeInfusionRecipe(output)
			while (rec != null) {
				removed.add(rec)
				rec = AlfheimAPI.removeInfusionRecipe(output)
			}
		}
		
		override fun canUndo(): Boolean {
			return true
		}
		
		override fun undo() {
			for (rec in removed) AlfheimAPI.addInfuserRecipe(rec)
		}
		
		override fun describe(): String {
			return "Removing all Mana Infuser recipes for ${output.unlocalizedName}"
		}
		
		override fun describeUndo(): String {
			return "Re-adding previously removed Mana Infuser recipes for ${output.unlocalizedName}"
		}
		
		override fun getOverrideKey(): Any? {
			return null
		}
	}
}