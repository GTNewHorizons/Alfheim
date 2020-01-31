package alfheim.common.item.block

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
	
	override fun addInformation(par1ItemStack: ItemStack?, par2EntityPlayer: EntityPlayer?, par3List: MutableList<Any?>?, par4: Boolean) {
		if (par1ItemStack == null) return
		val meta = "\\d+$".toRegex().find(field_150939_a.unlocalizedName)
		addStringToTooltip("&7" + StatCollector.translateToLocal("misc.${ModInfo.MODID}.color." + (meta?.value ?: "16")) + "&r", par3List)
	}
}

open class ItemSlabMod(val par1: Block): ItemSlab(par1, (par1 as BlockSlabMod).getSingleBlock(), par1.getFullBlock(), false) {
	override fun getUnlocalizedName(par1ItemStack: ItemStack) =
        field_150939_a.unlocalizedName.replace("tile.".toRegex(), "tile.${ModInfo.MODID}:").replace("\\d+$".toRegex(), "")
}

open class ItemShimmerSlabMod(val par1: Block): ItemSlab(par1, (par1 as BlockShimmerQuartzSlab).singleBlock, par1.fullBlock, false) {
	override fun getUnlocalizedName(par1ItemStack: ItemStack) =
        field_150939_a.unlocalizedName.replace("tile.".toRegex(), "tile.${ModInfo.MODID}:").replace("\\d+$".toRegex(), "")
}

open class ItemRegularSlabMod(val par1: Block): ItemSlab(par1, (par1 as BlockSlabMod).getSingleBlock(), par1.getFullBlock(), false) {
	override fun getUnlocalizedName(par1ItemStack: ItemStack) =
        field_150939_a.unlocalizedName.replace("tile.".toRegex(), "tile.${ModInfo.MODID}:")
}
