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
			val stack = inv[i] ?: continue
			
			if (stack.item is ItemLootInterceptor && !inter)
				inter = true
			else if (inter)
				return true
		}
		
		return false
	}
	
	override fun getCraftingResult(inv: InventoryCrafting): ItemStack? {
		var id = -1
		
		for (i in 0 until inv.sizeInventory)
			if (inv[i]?.item is ItemLootInterceptor) {
				id = i
				break
			}
		
		if (id == -1) return null
		val inter = inv[id]?.copy() ?: return null
		
		for (i in 0 until inv.sizeInventory) {
			if (i == id) continue
			
			val stack = inv[i] ?: continue
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