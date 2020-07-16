package alfheim.api.item.equipment.bauble

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack

/**
 * An bauble item that implements this can provide a mana discount for mana tools.
 * Mana tools are the ones on the main toolset (Pick, Shovel, Axe, Sword and Shovel)
 * as well as Rods.
 */
interface IManaDiscountBauble {
	
	/**
	 * Gets the mana discount that this bauble provides. This is added
	 * together to create the full discount.
	 * Value is to be from 0.0 to 1.0. 0.1 is 10% discount, as an example.
	 * You can also return negative values to make tools cost more.
	 */
	fun getDiscount(stack: ItemStack, slot: Int, player: EntityPlayer): Float
}
