package alfheim.common.item.block

import alexsocol.asjlib.meta
import alfheim.api.ModInfo
import alfheim.common.block.base.BlockSlabMod
import alfheim.common.block.colored.rainbow.BlockShimmerQuartzSlab
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.util.StatCollector

open class ItemColoredSlabMod(par1: Block): ItemSlabMod(par1) {
	
	fun addStringToTooltip(s: String, tooltip: MutableList<Any?>?) {
		tooltip!!.add(s.replace("&".toRegex(), "\u00a7"))
	}
	
	override fun addInformation(stack: ItemStack?, player: EntityPlayer?, list: MutableList<Any?>?, advanced: Boolean) {
		if (stack == null) return
		val meta = "\\d+$".toRegex().find(field_150939_a.unlocalizedName)
		addStringToTooltip("&7" + StatCollector.translateToLocal("misc.${ModInfo.MODID}.color." + (meta?.value ?: "16")) + "&r", list)
	}
}

open class ItemSlabMod(val block: Block): ItemSlab(block, (block as BlockSlabMod).getSingleBlock(), block.getFullBlock(), false) {
	
	override fun getUnlocalizedName(stack: ItemStack) =
		field_150939_a.unlocalizedName.replace("tile.".toRegex(), "tile.${ModInfo.MODID}:").replace("\\d+$".toRegex(), "")
}

open class ItemShimmerSlabMod(val block: Block): ItemSlab(block, (block as BlockShimmerQuartzSlab).singleBlock, block.fullBlock, false) {
	
	override fun getUnlocalizedName(stack: ItemStack) =
		field_150939_a.unlocalizedName.replace("tile.".toRegex(), "tile.${ModInfo.MODID}:").replace("\\d+$".toRegex(), "")
}

open class ItemMetaSlabMod(val block: Block): ItemSlab(block, (block as BlockSlabMod).getSingleBlock(), block.getFullBlock(), false) {
	
	override fun getUnlocalizedName(stack: ItemStack) =
		"${field_150939_a.unlocalizedName.replace("tile.".toRegex(), "tile.${ModInfo.MODID}:")}${stack.meta and 0x8.inv()}"
}