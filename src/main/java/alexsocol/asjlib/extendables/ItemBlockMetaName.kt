package alexsocol.asjlib.extendables

import net.minecraft.block.Block
import net.minecraft.item.*

class ItemBlockMetaName(block: Block): ItemBlockWithMetadata(block, block) {
	
	init {
		setHasSubtypes(true)
	}
	
	override fun getUnlocalizedName(stack: ItemStack?) = "${super.getUnlocalizedName(stack)}${stack!!.itemDamage}"
	
	override fun getMetadata(meta: Int) = meta
}
