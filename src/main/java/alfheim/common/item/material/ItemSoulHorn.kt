package alfheim.common.item.material

import alexsocol.asjlib.meta
import alfheim.common.entity.boss.EntityFlugel
import alfheim.common.item.ItemMod
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World

class ItemSoulHorn: ItemMod("SoulHorn") {
	
	init {
		maxStackSize = 1
	}
	
	override fun onItemUse(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		// Stupid Et Futurum
		if (player.isSneaking && stack.meta == 1) {
			val success = EntityFlugel.spawn(player, stack, world, x, y, z, true, true)
			if (success) stack.meta = 0
			return success
		}
		return false
	}
	
	override fun getColorFromItemStack(stack: ItemStack, renderPass: Int) = if (stack.meta == 1) -0x1 else -0x222223 // 0xFFDDDDDD
}