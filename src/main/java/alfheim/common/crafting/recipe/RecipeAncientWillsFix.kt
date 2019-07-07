package alfheim.common.crafting.recipe

import gloomyfolken.hooklib.asm.*
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import vazkii.botania.api.item.IAncientWillContainer
import vazkii.botania.common.crafting.recipe.AncientWillRecipe
import vazkii.botania.common.item.ModItems

@Suppress("UNUSED_PARAMETER")
object RecipeAncientWillsFix {
	
	val foundWill = arrayOf(false, false, false, false, false, false)
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	fun matches(rec: AncientWillRecipe, var1: InventoryCrafting, var2: World?): Boolean {
		foundWill.fill(false)
		var foundItem = false
		
		for (i in 0 until var1.sizeInventory) {
			val stack = var1.getStackInSlot(i)
			if (stack != null) {
				if (stack.item === ModItems.ancientWill) {
					val meta = stack.itemDamage
					if (meta !in 0..5) return false
						if (foundWill[meta]) return false else foundWill[meta] = true
					
				} else if (!foundItem) {
					if (stack.item !is IAncientWillContainer) {
						return false
					}
					
					foundItem = true
				}
			}
		}
		
		return foundWill.any { it } && foundItem
	}
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	fun getCraftingResult(rec: AncientWillRecipe, var1: InventoryCrafting): ItemStack? {
		foundWill.fill(false)
		var item: ItemStack? = null
		
		var stack: ItemStack?
		for (i in 0 until var1.sizeInventory) {
			stack = var1.getStackInSlot(i)
			if (stack != null) {
				if (stack.item is IAncientWillContainer && item == null) {
					item = stack
				} else {
					if (foundWill[stack.itemDamage]) return null
					else foundWill[stack.itemDamage] = true
				}
			}
		}
		
		val container = item!!.item as IAncientWillContainer
		stack = item.copy()
		for (i in 0..5) {
			if (foundWill[i]) {
				if (container.hasAncientWill(item, i)) {
					return null
				} else {
					container.addAncientWill(stack, i)
				}
			}
		}
		return stack
	}
}