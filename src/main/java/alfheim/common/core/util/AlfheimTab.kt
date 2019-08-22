package alfheim.common.core.util

import alfheim.common.block.AlfheimBlocks
import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.*

class AlfheimTab: CreativeTabs("Alfheim") {
	
	override fun getTabIconItem(): Item? = Item.getItemFromBlock(AlfheimBlocks.alfheimPortal)
	
	lateinit var list: MutableList<Any?>
	
	init {
		backgroundImageName = "Alfheim.png"
		setNoTitle()
	}
	
	override fun displayAllReleventItems(list: MutableList<Any?>) {
		this.list = list
		
		
	}
	
	private fun addItem(item: Item) {
		item.getSubItems(item, this, list)
	}
	
	private fun addBlock(block: Block) {
		val stack = ItemStack(block)
		block.getSubBlocks(stack.item, this, list)
	}
	
	private fun addStack(stack: ItemStack) {
		list.add(stack)
	}
}