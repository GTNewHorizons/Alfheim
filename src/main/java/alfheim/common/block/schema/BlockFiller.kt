package alfheim.common.block.schema

import alfheim.common.block.base.BlockMod
import net.minecraft.block.material.Material

/**
 * Created by l0nekitsune on 1/3/16.
 */
class BlockFiller: BlockMod(Material.wood) {
	
	init {
		//        val size = 0.1875f
		//        this.setBlockBounds(size, size, size, 1.0f - size, 1.0f - size, 1.0f - size)
		setBlockName("fillerBlock")
	}
	
	override fun isOpaqueCube() = false
}
