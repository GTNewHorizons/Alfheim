package alfheim.common.item.compat.thaumcraft

import alfheim.common.integration.thaumcraft.ThaumcraftAlfheimModule
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon

class ItemAlfheimWandRod: Item() {
	init {
		creativeTab = ThaumcraftAlfheimModule.tcnTab
		setHasSubtypes(true)
		unlocalizedName = "AlfheimRod"
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerIcons(reg: IIconRegister) {
		for (i in textures.indices)
			textures[i] = reg.registerIcon("thaumcraft:AlfRod$i")
	}
	
	@SideOnly(Side.CLIENT)
	override fun getIconFromDamage(meta: Int): IIcon {
		return textures[Math.max(0, Math.min(meta, textures.size - 1))]
	}
	
	@SideOnly(Side.CLIENT)
	override fun getSubItems(item: Item, tab: CreativeTabs?, list: MutableList<*>) {
		for (i in textures.indices)
			list.add(ItemStack(this, 1, i))
	}
	
	override fun getUnlocalizedName(par1ItemStack: ItemStack): String {
		return super.getUnlocalizedName() + "." + par1ItemStack.itemDamage
	}
	
	companion object {
		
		val textures = arrayOfNulls<IIcon>(3)
	}
}
