package alfheim.common.crafting.recipe

import alexsocol.asjlib.ASJUtilities
import alfheim.api.lib.LibOreDict
import alfheim.common.block.AlfheimBlocks
import alfheim.common.core.util.*
import alfheim.common.item.AlfheimItems
import net.minecraft.block.Block
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.world.World

class RecipeEnhanceBucket: IRecipe {
	
	override fun matches(inv: InventoryCrafting, world: World?): Boolean {
		var foundBucket = false
		var foundMauf = false
		var meta = 0
		
		for (i in 0 until inv.sizeInventory) {
			val stack = inv.getStackInSlot(i) ?: continue
			if (stack.item === AlfheimItems.hyperBucket && !foundBucket) {
				meta = stack.meta
				foundBucket = true
				break
			}
		}
		
		if (!foundBucket) return false
		foundBucket = false
		
		for (i in 0 until inv.sizeInventory) {
			val stack = inv.getStackInSlot(i) ?: continue
			if (stack.item === AlfheimItems.hyperBucket && !foundBucket)
				foundBucket = true
			else if (!foundMauf) {
				if (checkRequirements(meta, stack))
					foundMauf = true
				else
					return false
			}
		}
		
		return foundBucket && foundMauf
	}
	
	override fun getCraftingResult(inv: InventoryCrafting): ItemStack? {
		var bucket: ItemStack? = null
		var mauf: ItemStack? = null
		
		for (i in 0 until inv.sizeInventory) {
			val stack = inv.getStackInSlot(i) ?: continue
			if (stack.item === AlfheimItems.hyperBucket)
				bucket = stack
			else
				mauf = stack
		}
		
		if (bucket == null || mauf == null) return null
		
		val copy = bucket.copy()
		copy.meta++
		
		return copy
	}
	
	fun checkRequirements(meta: Int, stack: ItemStack): Boolean {
		return when (meta) {
			in 0..2	-> ASJUtilities.isOre(stack, LibOreDict.MAUFTRIUM_INGOT)
			in 3..5	-> stack.item.toBlock() === AlfheimBlocks.alfStorage && stack.meta == 1
			else	-> false
		}
	}
	
	override fun getRecipeOutput() = ItemStack(AlfheimItems.hyperBucket)
	
	override fun getRecipeSize() = 2
}
