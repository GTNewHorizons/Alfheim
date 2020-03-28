package alfheim.api.crafting.recipe

import net.minecraft.item.ItemStack
import vazkii.botania.api.recipe.RecipePetals

class RecipeManaInfuser(val manaUsage: Int, out: ItemStack, vararg recipe: Any): RecipePetals(out, *recipe) {
	
	override fun toString(): String {
		val s = StringBuilder()
		for (ing in inputs) s.append("$ing + ")
		return "Recipe: $s mana*$manaUsage => $output"
	}
	
	override fun equals(other: Any?): Boolean {
		if (other is RecipeManaInfuser) {
			return other.manaUsage == manaUsage && ItemStack.areItemStacksEqual(output, other.output) && inputs.containsAll(other.inputs) && other.inputs.containsAll(inputs)
		}
		return false
	}
}