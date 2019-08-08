package alfheim.common.block

import alfheim.common.core.helper.IconHelper
import net.minecraft.block.BlockSlab
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.util.IIcon
import vazkii.botania.common.block.decor.slabs.BlockLivingSlab

class BlockElvenSandstoneSlab(full: Boolean): BlockLivingSlab(full, AlfheimBlocks.elvenSandstone, 0) {
	
	override fun getFullBlock() = AlfheimBlocks.elvenSandstoneSlabFull as BlockSlab
	
	override fun getSingleBlock() = AlfheimBlocks.elvenSandstoneSlab as BlockSlab
}

class BlockLivingCobbleSlab(full: Boolean): BlockLivingSlab(full, AlfheimBlocks.livingcobble, 0) {
	
	override fun getFullBlock() = AlfheimBlocks.livingcobbleSlabFull as BlockSlab
	
	override fun getSingleBlock() = AlfheimBlocks.livingcobbleSlab as BlockSlab
}

class BlockLivingRockTileSlab(full: Boolean): BlockLivingSlab(full, AlfheimBlocks.livingcobble, 2) {
	
	lateinit var sideIcon: IIcon
	
	override fun getFullBlock() = AlfheimBlocks.livingrockTileSlabFull as BlockSlab
	
	override fun getSingleBlock() = AlfheimBlocks.livingrockTileSlab as BlockSlab
	
	override fun registerBlockIcons(reg: IIconRegister) {
		sideIcon = IconHelper.forName(reg, "Livingsubstone2Slab")
	}
	
	override fun getIcon(side: Int, meta: Int) = if (side > 1) sideIcon else super.getIcon(side, meta)!!
}
