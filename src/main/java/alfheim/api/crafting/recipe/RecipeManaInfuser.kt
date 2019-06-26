package alfheim.api.crafting.recipe

import net.minecraft.item.ItemStack
import vazkii.botania.api.recipe.RecipePetals

class RecipeManaInfuser(val manaUsage: Int, out: ItemStack, vararg recipe: Any): RecipePetals(out, recipe) {
	
	override fun toString(): String {
		val s = StringBuilder()
		for (ing in inputs) s.append(ing.toString()).append(" + ")
		return "Recipe: $s mana*$manaUsage => $output"
	}
	
	override fun equals(obj: Any?): Boolean {
		if (obj is RecipeManaInfuser) {
			val r = obj as RecipeManaInfuser?
			return r!!.manaUsage == manaUsage && ItemStack.areItemStacksEqual(output, r.output) && inputs.containsAll(r.inputs) && r.inputs.containsAll(inputs)
		}
		return false
	}
}