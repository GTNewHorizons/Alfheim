package alfheim.common.item

import alfheim.AlfheimCore
import alfheim.api.ModInfo
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.item.*
import alfheim.common.core.helper.IconHelper

open class ItemMod(name: String): Item() {
	
	init {
		creativeTab = AlfheimCore.baTab
		unlocalizedName = name
	}
	
	override fun setUnlocalizedName(par1Str: String): Item {
		GameRegistry.registerItem(this, par1Str)
		return super.setUnlocalizedName(par1Str)
	}
	
	override fun getItemStackDisplayName(stack: ItemStack) =
		super.getItemStackDisplayName(stack).replace("&".toRegex(), "\u00a7")
	
	override fun getUnlocalizedNameInefficiently(par1ItemStack: ItemStack) =
		super.getUnlocalizedNameInefficiently(par1ItemStack).replace("item\\.".toRegex(), "item.${ModInfo.MODID}:")
	
	@SideOnly(Side.CLIENT)
	override fun registerIcons(par1IconRegister: IIconRegister) {
		itemIcon = IconHelper.forItem(par1IconRegister, this)
	}
}
