package alexsocol.asjlib.extendables.block

import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.Block
import net.minecraft.block.material.Material

abstract class BlockMod(mat: Material): Block(mat) {
	
	override fun setBlockName(name: String): Block {
		GameRegistry.registerBlock(this, name)
		return super.setBlockName(name)
	}
}