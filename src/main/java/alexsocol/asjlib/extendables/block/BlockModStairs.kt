package alexsocol.asjlib.extendables.block

import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.*

class BlockModStairs(source: Block, meta: Int) : BlockStairs(source, meta) {
	
	init {
		useNeighborBrightness = true
	}
	
	override fun setBlockName(name: String): Block {
		GameRegistry.registerBlock(this, name)
		return super.setBlockName(name)
	}
}