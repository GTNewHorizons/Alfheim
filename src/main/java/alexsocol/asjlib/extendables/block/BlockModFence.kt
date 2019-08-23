package alexsocol.asjlib.extendables.block

import alfheim.common.item.block.ItemBlockMod
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.world.IBlockAccess

class BlockModFence(texture: String, mat: Material, val gate: Block): BlockFence(texture, mat) {
	
	override fun canConnectFenceTo(world: IBlockAccess, x: Int, y: Int, z: Int): Boolean {
		val block = world.getBlock(x, y, z)
		return if (block !== this && block !== gate) if (block.material.isOpaque && block.renderAsNormalBlock()) block.material !== Material.gourd else false else true
	}

	override fun setBlockName(name: String): Block {
		GameRegistry.registerBlock(this, ItemBlockMod::class.java, name)
		return super.setBlockName(name)
	}
}
