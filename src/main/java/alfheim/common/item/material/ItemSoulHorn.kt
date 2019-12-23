package alfheim.common.item.material

import alfheim.common.core.util.meta
import alfheim.common.entity.boss.EntityFlugel
import alfheim.common.item.ItemMod
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.world.World

class ItemSoulHorn: ItemMod("SoulHorn") {
	
	override fun onItemUse(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		val block = world.getBlock(x, y, z)
		if (block === Blocks.beacon) {
			if (player.isSneaking && stack.meta == 0) {
				val success = EntityFlugel.spawn(player, stack, world, x, y, z, true, true)
				if (success) stack.meta = 1
				return success
			}
		}
		return false
	}
	
	override fun getColorFromItemStack(stack: ItemStack, renderPass: Int) = if (stack.meta == 0) -0x1 else -0x222223 // 0xFFDDDDDD
}