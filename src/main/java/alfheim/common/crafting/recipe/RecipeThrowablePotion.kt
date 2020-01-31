package alfheim.common.crafting.recipe

import alfheim.common.item.AlfheimItems.splashPotion
import alfheim.common.item.ItemSplashPotion
import net.minecraft.init.Items
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import net.minecraft.world.World
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.common.brew.ModBrews
import vazkii.botania.common.item.ModItems
import vazkii.botania.common.item.brew.ItemBrewVial

class RecipeThrowablePotion: IRecipe {
	
	override fun matches(inv: InventoryCrafting, world: World?): Boolean {
		var foundGunpowder = false
		var foundVial = false
		
		for (i in 0 until inv.sizeInventory) {
			val stack = inv.getStackInSlot(i)
			if (stack != null) {
				when {
					stack.item === Items.gunpowder   -> foundGunpowder = true
					stack.item === ModItems.brewVial -> foundVial = true
					else                             -> return false // Found an invalid item, breaking the recipe
				}
			}
		}
		
		return foundGunpowder && foundVial
	}
	
	override fun getCraftingResult(inv: InventoryCrafting): ItemStack? {
		var vial: ItemStack? = null
		
		for (i in 0 until inv.sizeInventory) {
			val stack = inv.getStackInSlot(i)
			if (stack?.item === ModItems.brewVial)
				vial = stack
		}
		
		if (vial == null) return null
		val item = vial.item as ItemBrewVial
		val brew = item.getBrew(vial)
		
		if (brew === BotaniaAPI.fallbackBrew) return null
		
		return (splashPotion as ItemSplashPotion).getItemForBrew(brew, vial)
	}
	
	override fun getRecipeOutput() = (splashPotion as ItemSplashPotion).getItemForBrew(ModBrews.absorption, null)
	
	override fun getRecipeSize() = 2
}
