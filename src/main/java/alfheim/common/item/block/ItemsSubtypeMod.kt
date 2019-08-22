package alfheim.common.item.block

import alfheim.api.ModInfo
import alfheim.common.block.base.BlockLeavesMod
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.util.StatCollector

open class ItemSubtypedBlockMod(par2Block: Block): ItemBlockWithMetadata(par2Block, par2Block) {
	
	override fun getMetadata(meta: Int): Int {
		if (field_150939_a is BlockLeavesMod) return meta or field_150939_a.decayBit()
		return meta
	}
	
	override fun getUnlocalizedNameInefficiently(par1ItemStack: ItemStack) =
		super.getUnlocalizedNameInefficiently(par1ItemStack).replace("tile.", "tile.${ModInfo.MODID}:").replace("\\d+$".toRegex(), "")
	
	fun addStringToTooltip(s: String, tooltip: MutableList<Any?>?) {
		tooltip!!.add(s.replace("&".toRegex(), "\u00a7"))
	}
	
	override fun addInformation(par1ItemStack: ItemStack?, par2EntityPlayer: EntityPlayer?, par3List: MutableList<Any?>?, par4: Boolean) {
		if (par1ItemStack == null) return
		addStringToTooltip("&7" + StatCollector.translateToLocal("misc.${ModInfo.MODID}.color." + par1ItemStack.itemDamage) + "&r", par3List)
	}
}

class ItemUniqueSubtypedBlockMod(par2Block: Block): ItemBlockWithMetadata(par2Block, par2Block) {
	override fun getMetadata(meta: Int): Int {
		if (field_150939_a is BlockLeavesMod) return meta or field_150939_a.decayBit()
		return meta
	}
	
	override fun getUnlocalizedNameInefficiently(par1ItemStack: ItemStack) =
		super.getUnlocalizedNameInefficiently(par1ItemStack).replace("tile.", "tile.${ModInfo.MODID}:") + par1ItemStack.itemDamage
}
