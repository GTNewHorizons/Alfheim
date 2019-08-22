package alexsocol.asjlib.extendables.block

import net.minecraft.block.*

class BlockModFenceGate(val src: Block, val meta: Int): BlockFenceGate() {
	
	override fun getIcon(side: Int, meta: Int) = src.getIcon(side, this.meta)!!
}
