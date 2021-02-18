package alfheim.common.crafting.recipe

import alexsocol.asjlib.*
import alfheim.common.block.AlfheimBlocks
import alfheim.common.item.AlfheimItems
import alfheim.common.item.material.ElvenResourcesMetas
import net.minecraft.init.Items
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.world.World
import vazkii.botania.common.item.ModItems

class RecipeElvenWeed: IRecipe {
	
	override fun matches(crafting: InventoryCrafting, world: World?): Boolean {
		val founds = Array(4) { false }
		
		for (i in 0 until crafting.sizeInventory) {
			val stack = crafting[i] ?: continue
			
			when {
				stack.item === AlfheimItems.elvenResource && stack.meta == ElvenResourcesMetas.IffesalDust -> {
					if (founds[0]) return false
					founds[0] = true
				}
				stack.item === ModItems.manaResource && stack.meta == 8                                    -> {
					if (founds[1]) return false
					founds[1] = true
				}
				stack.item === AlfheimBlocks.rainbowMushroom.toItem()                                      -> {
					if (founds[2]) return false
					founds[2] = true
				}
				stack.item === Items.paper                                                                 -> {
					if (founds[3]) return false
					founds[3] = true
				}
				else                                                                                       -> return false
			}
		}
		
		return founds.all { it }
	}
	
	override fun getCraftingResult(crafting: InventoryCrafting) = recipeOutput
	
	override fun getRecipeOutput() = ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.ElvenWeed)
	
	override fun getRecipeSize() = 4
}
