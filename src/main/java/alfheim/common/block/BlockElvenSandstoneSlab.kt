package alfheim.common.block

import net.minecraft.block.BlockSlab
import vazkii.botania.common.block.decor.slabs.BlockLivingSlab

class BlockElvenSandstoneSlab(full: Boolean): BlockLivingSlab(full, AlfheimBlocks.elvenSandstone, 0) {
	
	override fun getFullBlock() = AlfheimBlocks.elvenSandstoneSlabFull as BlockSlab
	
	override fun getSingleBlock() = AlfheimBlocks.elvenSandstoneSlab as BlockSlab
}
