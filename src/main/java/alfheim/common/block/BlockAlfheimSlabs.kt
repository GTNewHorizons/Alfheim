package alfheim.common.block

import alexsocol.asjlib.safeGet
import alfheim.common.core.helper.IconHelper
import net.minecraft.block.BlockSlab
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.util.IIcon
import vazkii.botania.common.block.decor.slabs.BlockLivingSlab

class BlockRockShrineWhiteSlab(full: Boolean): BlockLivingSlab(full, AlfheimFluffBlocks.shrineRock, 0) {
	
	override fun getFullBlock() = AlfheimFluffBlocks.shrineRockWhiteSlabFull as BlockSlab
	
	override fun getSingleBlock() = AlfheimFluffBlocks.shrineRockWhiteSlab as BlockSlab
}

class BlockElvenSandstoneSlab(full: Boolean): BlockLivingSlab(full, AlfheimFluffBlocks.elvenSandstone, 0) {
	
	override fun getFullBlock() = AlfheimFluffBlocks.elvenSandstoneSlabFull as BlockSlab
	
	override fun getSingleBlock() = AlfheimFluffBlocks.elvenSandstoneSlab as BlockSlab
}

class BlockLivingCobbleSlab(full: Boolean): BlockLivingSlab(full, AlfheimBlocks.livingcobble, 0) {
	
	override fun getFullBlock() = AlfheimFluffBlocks.livingcobbleSlabFull as BlockSlab
	
	override fun getSingleBlock() = AlfheimFluffBlocks.livingcobbleSlab as BlockSlab
}

class BlockLivingrockDarkSlab(full: Boolean, val meta: Int): BlockLivingSlab(full, AlfheimFluffBlocks.livingrockDark, meta) {
	
	override fun getFullBlock() = AlfheimFluffBlocks.livingrockDarkSlabsFull.safeGet(meta) as BlockSlab
	
	override fun getSingleBlock() = AlfheimFluffBlocks.livingrockDarkSlabs.safeGet(meta) as BlockSlab
}

class BlockLivingRockTileSlab(full: Boolean): BlockLivingSlab(full, AlfheimBlocks.livingcobble, 2) {
	
	lateinit var sideIcon: IIcon
	
	override fun getFullBlock() = AlfheimFluffBlocks.livingrockTileSlabFull as BlockSlab
	
	override fun getSingleBlock() = AlfheimFluffBlocks.livingrockTileSlab as BlockSlab
	
	override fun registerBlockIcons(reg: IIconRegister) {
		sideIcon = IconHelper.forName(reg, "LivingCobble2Slab")
	}
	
	override fun getIcon(side: Int, meta: Int) = if (side > 1) sideIcon else super.getIcon(side, meta)!!
}
