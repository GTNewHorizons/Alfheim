package alfheim.common.crafting.recipe

import alfheim.common.core.registry.AlfheimItems
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.*
import net.minecraft.item.crafting.IRecipe
import net.minecraft.world.World
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.item.ModItems

class HelmRevealingAlfheimRecipe: IRecipe {
	
	override fun matches(var1: InventoryCrafting, var2: World): Boolean {
		val goggles = Item.itemRegistry.getObject("Thaumcraft:ItemGoggles") as Item ?: return false
// NO TC loaded
		
		var foundGoggles = false
		var foundHelm = false
		for (i in 0 until var1.sizeInventory) {
			val stack = var1.getStackInSlot(i)
			if (stack != null) {
				if (checkHelm(stack))
					foundHelm = true
				else if (stack.item === goggles)
					foundGoggles = true
				else
					return false // Found an invalid item, breaking the recipe
			}
		}
		return foundGoggles && foundHelm
	}
	
	override fun getCraftingResult(var1: InventoryCrafting): ItemStack? {
		var helm: ItemStack? = null
		
		for (i in 0 until var1.sizeInventory) {
			val stack = var1.getStackInSlot(i)
			if (stack != null && checkHelm(stack))
				helm = stack
		}
		
		if (helm == null)
			return null
		
		val helmCopy = helm.copy()
		val helmItem = helmCopy.item
		
		val newHelm: ItemStack
		
		if (helmItem === AlfheimItems.elementalHelmet)
			newHelm = ItemStack(AlfheimItems.elementalHelmetRevealing)
		else if (helmItem === AlfheimItems.elvoriumHelmet)
			newHelm = ItemStack(AlfheimItems.elvoriumHelmetRevealing)
		else
			return null
		
		// Copy Ancient Wills
		for (i in 0..5)
			if (ItemNBTHelper.getBoolean(helmCopy, "AncientWill$i", false))
				ItemNBTHelper.setBoolean(newHelm, "AncientWill$i", true)
		
		// Copy Enchantments
		val enchList = ItemNBTHelper.getList(helmCopy, "ench", 10, true)
		if (enchList != null)
			ItemNBTHelper.setList(newHelm, "ench", enchList)
		
		// Copy Runic Hardening
		val runicHardening = ItemNBTHelper.getByte(helmCopy, "RS.HARDEN", 0.toByte())
		ItemNBTHelper.setByte(newHelm, "RS.HARDEN", runicHardening)
		
		return newHelm
	}
	
	override fun getRecipeSize(): Int {
		return 10
	}
	
	override fun getRecipeOutput(): ItemStack {
		return ItemStack(ModItems.manasteelHelmRevealing)
	}
	
	private fun checkHelm(helmStack: ItemStack): Boolean {
		val helmItem = helmStack.item
		return helmItem === AlfheimItems.elementalHelmet || helmItem === AlfheimItems.elvoriumHelmet
	}
	
}
