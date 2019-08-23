package alexsocol.asjlib.extendables.block

import alfheim.common.item.block.ItemBlockMod
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.*

class BlockModFenceGate(val src: Block, val meta: Int): BlockFenceGate() {
	
	override fun getIcon(side: Int, meta: Int) = src.getIcon(side, this.meta)!!

	override fun setBlockName(name: String): Block {
		GameRegistry.registerBlock(this, ItemBlockMod::class.java, name)
		return super.setBlockName(name)
	}
}
