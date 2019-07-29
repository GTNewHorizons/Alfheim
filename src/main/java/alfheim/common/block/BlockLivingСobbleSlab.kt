package alfheim.common.block

import net.minecraft.block.BlockSlab
import vazkii.botania.common.block.decor.slabs.BlockLivingSlab

class BlockLivingСobbleSlab(full: Boolean): BlockLivingSlab(full, AlfheimBlocks.livingcobble, 0) {
	
	override fun getFullBlock() = AlfheimBlocks.livingcobbleSlabFull as BlockSlab
	
	override fun getSingleBlock() = AlfheimBlocks.livingcobbleSlab as BlockSlab
}
