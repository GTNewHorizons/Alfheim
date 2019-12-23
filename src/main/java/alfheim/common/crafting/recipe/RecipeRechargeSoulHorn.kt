package alfheim.common.crafting.recipe

import alfheim.common.core.util.meta
import alfheim.common.item.AlfheimItems
import alfheim.common.item.relic.ItemFlugelSoul
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.world.World

class RecipeRechargeSoulHorn: IRecipe {
	
	override fun matches(inv: InventoryCrafting, world: World?): Boolean {
		var foundHorn = false
		var foundSoul = false
		
		for (i in 0 until inv.sizeInventory) {
			val stack = inv.getStackInSlot(i) ?: continue
			if (stack.item === AlfheimItems.soulHorn && stack.meta == 1 && !foundHorn)
				foundHorn = true
			else if (!foundSoul) {
				if (stack.item === AlfheimItems.flugelSoul)
					foundSoul = true
				else
					return false
			}
		}
		
		return foundHorn && foundSoul
	}
	
	override fun getCraftingResult(inv: InventoryCrafting): ItemStack? {
		var horn: ItemStack? = null
		var soul: ItemStack? = null
		
		for (i in 0 until inv.sizeInventory) {
			val stack = inv.getStackInSlot(i) ?: continue
			if (stack.item === AlfheimItems.soulHorn && stack.meta == 1)
				horn = stack
			else if (stack.item === AlfheimItems.flugelSoul)
				soul = stack
		}
		
		if (horn == null || soul == null) return null
		if (ItemFlugelSoul.getBlocked(soul) != 0) return null
		
		for (i in 0 until ItemFlugelSoul.SEGMENTS) ItemFlugelSoul.setDisabled(soul, i, true)
		
		val copy = horn.copy()
		copy.meta = 0
		
		return copy
	}
	
	override fun getRecipeOutput() = ItemStack(AlfheimItems.soulHorn)
	
	override fun getRecipeSize() = 2
}