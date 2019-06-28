package alfheim.common.item.block

import alfheim.common.block.BlockElvenOres
import net.minecraft.block.Block
import net.minecraft.item.*

class ItemBlockElvenOres(block: Block): ItemBlock(block) {
	
	init {
		this.setHasSubtypes(true)
	}
	
	override fun getUnlocalizedName(stack: ItemStack): String {
		return "tile." + BlockElvenOres.names[Math.max(0, Math.min(stack.itemDamage, BlockElvenOres.names.size - 1))] + super.getUnlocalizedName().substring(5)
	}
	
	override fun getMetadata(meta: Int): Int {
		return meta
	}
}
