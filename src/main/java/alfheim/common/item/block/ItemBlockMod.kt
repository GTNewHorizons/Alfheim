package alfheim.common.item.block

import alfheim.api.ModInfo
import alfheim.common.block.base.BlockLeavesMod
import net.minecraft.block.Block
import net.minecraft.item.*

open class ItemBlockMod(block: Block): ItemBlock(block) {
 
	override fun setUnlocalizedName(p_77655_1_: String?): ItemBlock? {
		(this as Item).unlocalizedName = p_77655_1_
		return this
	}
	
	override fun getMetadata(meta: Int): Int {
		if (field_150939_a is BlockLeavesMod) return meta or field_150939_a.decayBit()
		return meta
	}
	
	override fun getUnlocalizedNameInefficiently(par1ItemStack: ItemStack) =
		getUnlocalizedNameInefficiently_(par1ItemStack).replace("tile.", "tile.${ModInfo.MODID}:")
	
	fun getUnlocalizedNameInefficiently_(stack: ItemStack) = super.getUnlocalizedNameInefficiently(stack)!!
}
