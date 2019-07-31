package alfheim.common.item.block

import alfheim.common.block.colored.BlockAuroraDirt
import net.minecraft.block.Block
import net.minecraft.item.*

class ItemBlockAurora(block: Block): ItemBlock(block) {
	override fun getColorFromItemStack(stack: ItemStack, pass: Int) = BlockAuroraDirt.getItemColor()
}