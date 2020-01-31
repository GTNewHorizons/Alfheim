package alfheim.api.item

import net.minecraft.item.ItemStack

/**
 * @author WireSegal
 * Created at 8:44 PM on 1/11/16.
 *
 * An Item interface that allows the mod author to blacklist a stack from entering the Botanist's Toolbelt.
 */
interface IToolbeltBlacklisted {
	
	/**
	 * @param stack The stack to check.
	 * @return Whether the stack is allowed in the Botanist's Toolbelt.
	 */
	fun allowedInToolbelt(stack: ItemStack): Boolean
}
