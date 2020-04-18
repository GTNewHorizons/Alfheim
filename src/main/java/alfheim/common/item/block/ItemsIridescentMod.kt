package alfheim.common.item.block

import alexsocol.asjlib.meta
import alfheim.api.ModInfo
import alfheim.common.block.base.BlockLeavesMod
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.util.StatCollector

open class ItemIridescentBlockMod(par2Block: Block): ItemBlockWithMetadata(par2Block, par2Block) {
	
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
		val meta = "\\d+$".toRegex().find(field_150939_a.unlocalizedName)
		addStringToTooltip("&7" + StatCollector.translateToLocal("misc.${ModInfo.MODID}.color." + (meta?.value ?: "16")) + "&r", par3List)
	}
}

class ItemIridescentWoodMod(par2Block: Block): ItemIridescentBlockMod(par2Block) {
	
	override fun addInformation(par1ItemStack: ItemStack?, par2EntityPlayer: EntityPlayer?, par3List: MutableList<Any?>?, par4: Boolean) {
		if (par1ItemStack == null) return
		val metaMatch = "\\d+$".toRegex().find(field_150939_a.unlocalizedName)
		val meta = if (metaMatch == null) 16 else metaMatch.value.toInt() * 4 + par1ItemStack.meta
		addStringToTooltip("&7" + StatCollector.translateToLocal("misc.${ModInfo.MODID}.color.$meta") + "&r", par3List)
	}
}

class ItemIridescentLeavesMod(par2Block: Block): ItemIridescentBlockMod(par2Block) {
	
	override fun getMetadata(meta: Int): Int {
		if (field_150939_a is BlockLeavesMod) return meta or field_150939_a.decayBit()
		return meta
	}
	
	override fun addInformation(par1ItemStack: ItemStack?, par2EntityPlayer: EntityPlayer?, par3List: MutableList<Any?>?, par4: Boolean) {
		if (par1ItemStack == null) return
		val metaMatch = "\\d+$".toRegex().find(field_150939_a.unlocalizedName)
		val meta = if (metaMatch == null) 16 else metaMatch.value.toInt() * 8 + par1ItemStack.meta % 8
		addStringToTooltip("&7" + StatCollector.translateToLocal("misc.${ModInfo.MODID}.color.$meta") + "&r", par3List)
	}
}
