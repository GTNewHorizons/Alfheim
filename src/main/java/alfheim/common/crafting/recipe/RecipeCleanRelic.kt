package alfheim.common.crafting.recipe

import alfheim.common.item.AlfheimItems
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.world.World
import vazkii.botania.api.item.IRelic
import vazkii.botania.common.core.helper.ItemNBTHelper

/**
 * @author ExtraMeteorP, CKATEPTb
 */
class RecipeCleanRelic: IRecipe {
	
	override fun matches(inv: InventoryCrafting, world: World?): Boolean {
		var foundRelic = false
		var foundItem = false
		
		for (i in 0 until inv.sizeInventory) {
			val stack = inv.getStackInSlot(i) ?: continue
			if (stack.item is IRelic && !foundRelic)
				foundRelic = true
			else if (!foundItem) {
				if (stack.item === AlfheimItems.relicCleaner)
					foundItem = true
				else
					return false
			}
		}
		
		return foundRelic && foundItem
	}
	
	override fun getCraftingResult(inv: InventoryCrafting): ItemStack? {
		var item: ItemStack? = null
		var cloth: ItemStack? = null
		
		for (i in 0 until inv.sizeInventory) {
			val stack = inv.getStackInSlot(i) ?: continue
			if (stack.item is IRelic)
				item = stack
			else if (stack.item === AlfheimItems.relicCleaner)
				cloth = stack
		}
		
		if (item == null) return null
		
		val copy = item.copy()
		if (ItemNBTHelper.getString(cloth, "uuid", "").isNotEmpty())
			ItemNBTHelper.setString(copy, "soulbind", ItemNBTHelper.getString(cloth, "uuid", ""))
		else
			ItemNBTHelper.setString(copy, "soulbind", "")
		
		return copy
	}
	
	override fun getRecipeSize() = 2
	
	override fun getRecipeOutput() = null
}