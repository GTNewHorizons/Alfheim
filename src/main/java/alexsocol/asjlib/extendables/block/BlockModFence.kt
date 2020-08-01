package alexsocol.asjlib.extendables.block

import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.item.ItemBlock
import net.minecraft.world.IBlockAccess

open class BlockModFence(texture: String, mat: Material, val gate: Block?): BlockFence(texture, mat) {
	
	init {
		setCreativeTab(null)
	}
	
	override fun canConnectFenceTo(world: IBlockAccess, x: Int, y: Int, z: Int): Boolean {
		val block = world.getBlock(x, y, z)
		if (super.canConnectFenceTo(world, x, y, z)) return true
		return if (block is BlockFence) true else if (block !== this && block !== gate) if (block.material.isOpaque && block.renderAsNormalBlock()) block.material !== Material.gourd else false else true
	}
	
	override fun setBlockName(name: String): Block {
		GameRegistry.registerBlock(this, ItemBlock::class.java, name)
		return super.setBlockName(name)
	}
}
