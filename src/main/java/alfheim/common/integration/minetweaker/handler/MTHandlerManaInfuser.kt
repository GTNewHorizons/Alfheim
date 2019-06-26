package alfheim.common.integration.minetweaker.handler

import alfheim.common.integration.minetweaker.MinetweakerAlfheimConfig.*

import java.util.ArrayList

import alfheim.api.AlfheimAPI
import alfheim.api.ModInfo
import alfheim.api.crafting.recipe.RecipeManaInfuser
import minetweaker.IUndoableAction
import minetweaker.MineTweakerAPI
import minetweaker.api.item.IIngredient
import minetweaker.api.item.IItemStack
import net.minecraft.item.ItemStack
import stanhebben.zenscript.annotations.ZenClass
import stanhebben.zenscript.annotations.ZenMethod

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
			val rec: RecipeManaInfuser?
			do {
				removed.add(rec = AlfheimAPI.removeInfusionRecipe(output))
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