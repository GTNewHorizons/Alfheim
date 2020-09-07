package alfheim.common.block.tile.sub.flower

import alfheim.api.ModInfo
import alfheim.common.core.helper.IconHelper
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.subtile.*
import vazkii.botania.api.subtile.signature.*
import vazkii.botania.common.block.tile.TileSpecialFlower

class AlfheimSignature(val name: String): SubTileSignature() {
	
	var icon: IIcon? = null
	
	override fun registerIcons(reg: IIconRegister) {
		icon = IconHelper.forName(reg, "flower_$name")
	}
	
	override fun getIconForStack(item: ItemStack?): IIcon? = icon
	
	override fun getUnlocalizedNameForStack(item: ItemStack): String = "tile.${ModInfo.MODID}:$name"
	
	override fun getUnlocalizedLoreTextForStack(item: ItemStack): String = "tile.${ModInfo.MODID}:$name.lore"
	
	override fun addTooltip(stack: ItemStack?, player: EntityPlayer?, tooltip: MutableList<String?>) {
		tooltip.add(EnumChatFormatting.BLUE.toString() + StatCollector.translateToLocal(getType()))
	}
	
	fun getType(): String? {
		val clazz = BotaniaAPI.getSubTileMapping(name) ?: return "uwotm8"
		if (clazz.getAnnotation(PassiveFlower::class.java) != null) return "botania.flowerType.passiveGenerating"
		if (SubTileGenerating::class.java.isAssignableFrom(clazz)) return "botania.flowerType.generating"
		return if (SubTileFunctional::class.java.isAssignableFrom(clazz)) "botania.flowerType.functional" else "botania.flowerType.misc"
	}
	
	companion object {
		val SubTileEntity.isOnSpecialSoil
			get() = (supertile as? TileSpecialFlower)?.isOnSpecialSoil == true
	}
}