package alexsocol.asjlib.extendables

import alexsocol.asjlib.extendables.block.BlockModMeta
import alexsocol.asjlib.meta
import net.minecraft.block.Block
import net.minecraft.item.*

class ItemBlockMetaName(block: Block): ItemBlockWithMetadata(block, block) {
	
	init {
		setHasSubtypes(true)
	}
	
	override fun getUnlocalizedName(stack: ItemStack) = "${super.getUnlocalizedName(stack)}${if ((field_150939_a as? BlockModMeta)?.subtypes ?: 16 > 1) stack.meta else ""}"
	
	override fun getMetadata(meta: Int) = meta
}
