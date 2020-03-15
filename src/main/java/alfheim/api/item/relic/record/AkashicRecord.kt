package alfheim.api.item.relic.record

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack

abstract class AkashicRecord(val name: String) {
	
	/**
	 * Check if [player] can get this record to [stack]
	 */
	abstract fun canGet(player: EntityPlayer, stack: ItemStack): Boolean
	
	/**
	 * Apply record effect
	 *
	 * @return false if effect cannot be applied by [player] using [stack]
	 */
	abstract fun apply(player: EntityPlayer, stack: ItemStack): Boolean
}