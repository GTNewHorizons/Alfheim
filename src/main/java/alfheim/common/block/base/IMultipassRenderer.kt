package alfheim.common.block.base

import net.minecraft.block.Block
import net.minecraft.world.IBlockAccess

interface IMultipassRenderer {
	
	fun innerBlock(world: IBlockAccess, x: Int, y: Int, z: Int): Block = innerBlock(world.getBlockMetadata(x, y, z))
	fun innerBlock(meta: Int): Block
}
