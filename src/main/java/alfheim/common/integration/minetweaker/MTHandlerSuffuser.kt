package alfheim.common.integration.minetweaker

import alfheim.api.*
import alfheim.api.crafting.recipe.RecipeTreeCrafting
import alfheim.common.integration.minetweaker.MinetweakerAlfheimConfig.getObjects
import alfheim.common.integration.minetweaker.MinetweakerAlfheimConfig.getStack
import minetweaker.*
import minetweaker.api.item.*
import net.minecraft.item.ItemStack
import stanhebben.zenscript.annotations.*
import java.util.*

@ZenClass("mods." + ModInfo.MODID + ".Suffuser")
object MTHandlerSuffuser {
	
	@ZenMethod
	@JvmStatic
	fun addRecipe(output: IItemStack, inner: IItemStack, mana: Int, speed: Int, inputs: Array<IIngredient?>) {
		val out = getStack(output)
		val core = getStack(inner)
		MineTweakerAPI.apply(Add(RecipeTreeCrafting(mana, out, core, speed, *getObjects(inputs))))
	}
	
	@ZenMethod
	@JvmStatic
	fun removeRecipe(output: IItemStack) {
		MineTweakerAPI.apply(Remove(getStack(output)))
	}
	
	private class Add(private val recipe: RecipeTreeCrafting): IUndoableAction {
		
		override fun apply() {
			ShadowFoxAPI.addTreeRecipe(recipe)
		}
		
		override fun canUndo(): Boolean {
			return true
		}
		
		override fun undo() {
			ShadowFoxAPI.removeTreeRecipe(recipe.output)
		}
		
		override fun describe(): String {
			return String.format("Adding Tree Suffusion recipe %s", recipe)
		}
		
		override fun describeUndo(): String {
			return String.format("Removing Tree Suffusion recipe %s", recipe)
		}
		
		override fun getOverrideKey(): Any? {
			return null
		}
	}
	
	private class Remove(private val output: ItemStack): IUndoableAction {
		
		internal val removed = ArrayList<RecipeTreeCrafting>()
		
		override fun apply() {
			var rec = ShadowFoxAPI.removeTreeRecipe(output)
			while (rec != null) {
				removed.add(rec)
				rec = ShadowFoxAPI.removeTreeRecipe(output)
			}
		}
		
		override fun canUndo(): Boolean {
			return true
		}
		
		override fun undo() {
			for (rec in removed) ShadowFoxAPI.addTreeRecipe(rec)
		}
		
		override fun describe(): String {
			return String.format("Removing all Tree Suffusion recipes for %s", output.unlocalizedName)
		}
		
		override fun describeUndo(): String {
			return String.format("Re-adding previously removed Tree Suffusion recipes for %s", output.unlocalizedName)
		}
		
		override fun getOverrideKey(): Any? {
			return null
		}
	}
}
