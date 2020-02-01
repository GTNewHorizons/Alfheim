package alfheim.common.block.schema

import alfheim.common.block.base.BlockMod
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import vazkii.botania.common.block.ModBlocks
import java.util.*

class BlockShemaMarker: BlockMod(Material.wood) {
	
	init {
		val size = 0.1875f
		setBlockBounds(size, size, size, 1f - size, 1f - size, 1f - size)
		setBlockName("schemaMarker")
		setBlockUnbreakable()
	}
	
	override fun getItemDropped(meta: Int, rand: Random, fortune: Int) = null
	
	override fun renderAsNormalBlock() = false
	
	override fun isOpaqueCube() = false
	
	override fun registerBlockIcons(par1IconRegister: IIconRegister) = Unit
	
	override fun getIcon(side: Int, meta: Int) = ModBlocks.manaBeacon.getIcon(side, meta)!!
}
