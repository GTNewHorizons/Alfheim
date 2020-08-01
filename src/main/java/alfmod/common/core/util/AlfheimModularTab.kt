package alfmod.common.core.util

import alfmod.common.item.AlfheimModularItems
import net.minecraft.block.Block
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.*

object AlfheimModularTab: CreativeTabs("AlfheimModular") {
	
	override fun getTabIconItem() = AlfheimModularItems.snowSword
	
	lateinit var list: MutableList<Any?>
	
	init {
		backgroundImageName = "AlfheimModular.png"
		setNoTitle()
	}
	
	override fun hasSearchBar() = false
	
	override fun displayAllReleventItems(list: MutableList<Any?>) {
		this.list = list
		
		// addBlock(AlfheimModularBlocks.airyVirus)
		
		addItem(AlfheimModularItems.eventResource)
		
		addItem(AlfheimModularItems.snowSword)
		addItem(AlfheimModularItems.snowHelmet)
		addItem(AlfheimModularItems.snowChest)
		addItem(AlfheimModularItems.snowLeggings)
		addItem(AlfheimModularItems.snowBoots)
		
		addItem(AlfheimModularItems.volcanoMace)
		addItem(AlfheimModularItems.volcanoHelmet)
		addItem(AlfheimModularItems.volcanoChest)
		addItem(AlfheimModularItems.volcanoLeggings)
		addItem(AlfheimModularItems.volcanoBoots)
	}
	
	fun addBlock(block: Block) {
		val stack = ItemStack(block)
		block.getSubBlocks(stack.item, this, list)
	}
	
	fun addItem(item: Item) {
		item.getSubItems(item, this, list)
	}
	
	fun addBlock(block: Block, meta: Int) {
		addStack(ItemStack(block, 1, meta))
	}
	
	fun addItem(item: Item, meta: Int) {
		addStack(ItemStack(item, 1, meta))
	}
	
	fun addStack(stack: ItemStack) {
		list.add(stack)
	}
}