package alfheim.common.item.block

import alfheim.api.ModInfo
import alfheim.common.block.base.BlockLeavesMod
import alfheim.common.block.colored.BlockAuroraDirt
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.util.StatCollector

class ItemBlockAurora(block: Block): ItemBlock(block) {
	
	override fun getMetadata(meta: Int): Int {
		if (field_150939_a is BlockLeavesMod) return meta or field_150939_a.decayBit()
		return meta
	}
	
	override fun getColorFromItemStack(stack: ItemStack, pass: Int) = BlockAuroraDirt.getItemColor()
	
	fun addStringToTooltip(s: String, tooltip: MutableList<Any?>?) {
		tooltip!!.add(s.replace("&".toRegex(), "\u00a7"))
	}
	
	override fun addInformation(par1ItemStack: ItemStack?, par2EntityPlayer: EntityPlayer?, par3List: MutableList<Any?>?, par4: Boolean) {
		addStringToTooltip("&7" + StatCollector.translateToLocal("misc.${ModInfo.MODID}.color.17") + "&r", par3List)
	}
}