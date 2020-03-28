package alfheim.common.item.block

import alfheim.api.ModInfo
import alfheim.common.block.base.BlockLeavesMod
import net.minecraft.block.Block
import net.minecraft.item.*

open class ItemBlockMod(block: Block): ItemBlock(block) {
	
	override fun setUnlocalizedName(name: String): ItemBlock? {
		(this as Item).unlocalizedName = name
		return this
	}
	
	override fun getMetadata(meta: Int): Int {
		if (field_150939_a is BlockLeavesMod) return meta or field_150939_a.decayBit()
		return meta
	}
	
	override fun getUnlocalizedNameInefficiently(stack: ItemStack) =
		getUnlocalizedNameInefficiently_(stack).replace("tile.", "tile.${ModInfo.MODID}:")
	
	fun getUnlocalizedNameInefficiently_(stack: ItemStack) = super.getUnlocalizedNameInefficiently(stack)!!
}
