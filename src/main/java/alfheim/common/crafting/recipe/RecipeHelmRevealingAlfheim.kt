package alfheim.common.crafting.recipe

import alexsocol.asjlib.get
import alfheim.common.item.AlfheimItems
import net.minecraft.inventory.InventoryCrafting
import net.minecraft.item.*
import net.minecraft.item.crafting.IRecipe
import net.minecraft.world.World
import vazkii.botania.common.core.helper.ItemNBTHelper

class RecipeHelmRevealingAlfheim: IRecipe {

	override fun matches(inv: InventoryCrafting, world: World?): Boolean {
		val goggles = Item.itemRegistry.getObject("Thaumcraft:ItemGoggles") ?: return false // NO TC loaded

		var foundGoggles = false
		var foundHelm = false
		for (i in 0 until inv.sizeInventory) {
			val stack = inv[i]
			if (stack != null) {
				when {
					checkHelm(stack)       -> foundHelm = true
					stack.item === goggles -> foundGoggles = true
					else                   -> return false
				} // Found an invalid item, breaking the recipe
			}
		}
		return foundGoggles && foundHelm
	}

	override fun getCraftingResult(var1: InventoryCrafting): ItemStack? {
		var helm: ItemStack? = null

		for (i in 0 until var1.sizeInventory) {
			val stack = var1[i]
			if (stack != null && checkHelm(stack))
				helm = stack
		}

		if (helm == null)
			return null

		val helmCopy = helm.copy()
		val newHelm: ItemStack

		newHelm = when {
			helmCopy.item === AlfheimItems.elementalHelmet -> AlfheimItems.elementalHelmetRevealing?.let { ItemStack(it) } ?: return null
			helmCopy.item === AlfheimItems.elvoriumHelmet  -> AlfheimItems.elvoriumHelmetRevealing?.let { ItemStack(it) } ?: return null
			else                                           -> return null
		}

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
		return ItemStack(AlfheimItems.elvoriumHelmetRevealing)
	}

	private fun checkHelm(helmStack: ItemStack): Boolean {
		val helmItem = helmStack.item
		return helmItem === AlfheimItems.elementalHelmet || helmItem === AlfheimItems.elvoriumHelmet
	}

}
