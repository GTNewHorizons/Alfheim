package alfheim.common.block.tile.sub.flower

import alfheim.api.ModInfo
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import vazkii.botania.api.subtile.signature.SubTileSignature

class ShadowFoxSignature(val name: String): SubTileSignature() {
	
	var icon: IIcon? = null
	override fun registerIcons(reg: IIconRegister) {
		icon = reg.registerIcon("${ModInfo.MODID}:$name")
	}
	
	override fun getIconForStack(item: ItemStack?): IIcon? = icon
	
	override fun getUnlocalizedNameForStack(item: ItemStack): String = "tile.${ModInfo.MODID}:$name"
	
	override fun getUnlocalizedLoreTextForStack(item: ItemStack): String = "tile.${ModInfo.MODID}:$name.lore"
}
