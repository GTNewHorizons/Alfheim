package alfheim.common.crafting.recipe

import alexsocol.asjlib.*
import alfheim.common.item.ItemLootInterceptor
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.world.World

class RecipeLootInterceptor: IRecipe {
	
	override fun matches(inv: InventoryCrafting, world: World?): Boolean {
		var inter = false
		
		for (i in 0 until inv.sizeInventory) {
			val stack = inv.getStackInSlot(i)
			if (stack != null) {
				if (stack.item is ItemLootInterceptor)
					inter = true
				else if (inter) return true
			}
		}
		
		return false
	}
	
	override fun getCraftingResult(inv: InventoryCrafting): ItemStack? {
		var inter: ItemStack? = null
		
		for (i in 0 until inv.sizeInventory) {
			val stack = inv.getStackInSlot(i)
			if (stack != null && stack.item is ItemLootInterceptor) {
				if (inter == null)
					inter = stack.copy()
				else
					return null
			}
		}
		
		if (inter == null) return null
		
		for (i in 0 until inv.sizeInventory) {
			val stack = inv.getStackInSlot(i)
			if (stack != null && stack.item !is ItemLootInterceptor)
				ItemLootInterceptor.add(inter, stack.item.id, stack.meta)
		}
		
		return inter
	}
	
	override fun getRecipeSize(): Int {
		return 10
	}
	
	override fun getRecipeOutput(): ItemStack? {
		return null
	}
}