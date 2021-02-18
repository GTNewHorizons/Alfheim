package alfheim.api.item

import net.minecraft.item.ItemStack

interface IPriestColorOverride {
	
	fun colorOverride(stack: ItemStack): Int
}
