package alexsocol.asjlib.extendables.block

import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.*
import net.minecraft.block.material.Material

abstract class BlockModContainer(mat: Material): BlockContainer(mat) {
	
	override fun setBlockName(name: String): Block {
		GameRegistry.registerBlock(this, name)
		return super.setBlockName(name)
	}
}