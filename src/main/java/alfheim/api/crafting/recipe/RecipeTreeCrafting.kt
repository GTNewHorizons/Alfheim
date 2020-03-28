package alfheim.api.crafting.recipe

import alexsocol.asjlib.ASJUtilities
import alfheim.common.core.util.meta
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary
import vazkii.botania.api.recipe.RecipePetals
import java.util.*

/**
 * A recipe for the Dendric Suffuser.
 */
class RecipeTreeCrafting(val manaUsage: Int, val out: ItemStack, val core: ItemStack, vararg inputs: Any): RecipePetals(out, *inputs) {
	
	private val inputs: List<Any>
	var throttle = -1
	
	constructor(mana: Int, out: ItemStack, core: ItemStack, throttle: Int, vararg inputs: Any): this(mana, out, core, *inputs) {
		this.throttle = throttle
	}
	
	init {
		if (inputs.size > 8) throw IllegalArgumentException("Maximal suffusion inputs size is 8")
		
		this.inputs = ArrayList(listOf(*inputs))
	}
	
	fun matches(items: List<ItemStack>, mid: ItemStack): Boolean {
		if (!ASJUtilities.isItemStackEqualCrafting(core, mid)) return false
		
		val inputsMissing = ArrayList(inputs)
		
		for (i in items) {
			for (j in inputsMissing.indices) {
				val inp = inputsMissing[j]
				if (inp is ItemStack && inp.meta == 32767)
					inp.meta = i.meta
				
				if (itemEquals(i, inp)) {
					inputsMissing.removeAt(j)
					break
				}
			}
		}
		return inputsMissing.isEmpty()
	}
	
	override fun getInputs() = this.inputs
	
	private fun itemEquals(input: ItemStack, ingredient: Any): Boolean {
		if (ingredient is String) {
			for (orestack in OreDictionary.getOres(ingredient)) {
				val cstack = orestack.copy()
				
				if (cstack.meta == 32767) cstack.meta = input.meta
				if (input.isItemEqual(cstack)) return true
			}
			
		} else
			return ingredient is ItemStack && ASJUtilities.isItemStackEqualCrafting(ingredient, input)
		return false
	}
	
	override fun toString(): String {
		val s = StringBuilder()
		for (ing in inputs) s.append("$ing + ")
		return "Recipe: ($s + mana*$manaUsage) -> $core => $output"
	}
	
	override fun equals(other: Any?): Boolean {
		if (other is RecipeTreeCrafting)
			return other.manaUsage == manaUsage && ItemStack.areItemStacksEqual(output, other.output) && inputs.containsAll(other.inputs) && other.inputs.containsAll(inputs)
		
		return false
	}
}
