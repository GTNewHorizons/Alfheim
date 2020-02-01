package alfheim.common.block.schema

import alfheim.common.block.base.BlockMod
import net.minecraft.block.material.Material
import java.util.*

/**
 * Created by l0nekitsune on 1/3/16.
 */
class BlockSchemaFiller: BlockMod(Material.wood) {
	
	init {
		//        val size = 0.1875f
		//        this.setBlockBounds(size, size, size, 1f - size, 1f - size, 1f - size)
		setBlockName("schemaFiller")
		setBlockUnbreakable()
	}
	
	override fun getItemDropped(meta: Int, rand: Random, fortune: Int) = null
	
	override fun isOpaqueCube() = false
}
