package alfheim.api.crafting.recipe

import alfheim.common.core.util.meta
import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import net.minecraftforge.oredict.OreDictionary
import vazkii.botania.api.recipe.RecipePetals
import java.util.*

/**
 * A recipe for the Dendric Suffuser.
 */
class RecipeTreeCrafting(val manaUsage: Int, val outputBlock: Block, val meta: Int, vararg inputs: Any): RecipePetals(ItemStack(outputBlock, 1, meta), *inputs) {
	
	private val inputs: List<Any>
	var throttle = -1
	
	init {
		this.inputs = ArrayList(listOf(*inputs))
	}
	
	constructor(mana: Int, outputBlock: Block, meta: Int, throttle: Int, vararg inputs: Any): this(mana, outputBlock, meta, *inputs) {
		this.throttle = throttle
	}
	
	fun matches(items: List<ItemStack>): Boolean {
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
	
	private fun itemEquals(stack: ItemStack, stack2: Any): Boolean {
		if (stack2 is String) {
			
			for (orestack in OreDictionary.getOres(stack2)) {
				val cstack = orestack.copy()
				
				if (cstack.meta == 32767) cstack.meta = stack.meta
				if (stack.isItemEqual(cstack)) return true
			}
			
		} else
			return stack2 is ItemStack && simpleAreStacksEqual(stack, stack2)
		return false
	}
	
	private fun simpleAreStacksEqual(stack: ItemStack, stack2: ItemStack) =
		stack.item === stack2.item && stack.meta == stack2.meta
}
