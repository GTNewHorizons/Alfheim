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
	fun matches(rec: AncientWillRecipe, inv: InventoryCrafting, world: World?): Boolean {
		foundWill.fill(false)
		var foundItem = false
		
		for (i in 0 until inv.sizeInventory) {
			val stack = inv.getStackInSlot(i)
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
	fun getCraftingResult(rec: AncientWillRecipe, inv: InventoryCrafting): ItemStack? {
		foundWill.fill(false)
		var item: ItemStack? = null
		
		var stack: ItemStack?
		for (i in 0 until inv.sizeInventory) {
			stack = inv.getStackInSlot(i)
			if (stack != null) {
				if (stack.item is IAncientWillContainer && item == null) {
					item = stack
				} else {
					if (foundWill[stack.itemDamage]) return null
					foundWill[stack.itemDamage] = true
				}
			}
		}
		
		val container = item!!.item as IAncientWillContainer
		stack = item.copy()
		for (i in 0..5) {
			if (foundWill[i]) {
				if (container.hasAncientWill(item, i)) return null
				container.addAncientWill(stack, i)
			}
		}
		return stack
	}
}