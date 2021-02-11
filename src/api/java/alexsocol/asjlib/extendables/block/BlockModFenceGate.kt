package alexsocol.asjlib.extendables.block

import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.*
import net.minecraft.item.ItemBlock

class BlockModFenceGate(val src: Block, val meta: Int): BlockFenceGate() {
	
	init {
		setCreativeTab(null)
	}
	
	override fun getIcon(side: Int, meta: Int) = src.getIcon(side, this.meta)!!
	
	override fun setBlockName(name: String): Block {
		GameRegistry.registerBlock(this, ItemBlock::class.java, name)
		return super.setBlockName(name)
	}
}
