package alfheim.common.item.compat.thaumcraft

import alexsocol.asjlib.meta
import alfheim.common.integration.thaumcraft.ThaumcraftAlfheimModule
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.*
import net.minecraft.util.IIcon
import kotlin.math.*

class ItemAlfheimWandCap: Item() {
	init {
		creativeTab = ThaumcraftAlfheimModule.tcnTab
		setHasSubtypes(true)
		unlocalizedName = "AlfheimCap"
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerIcons(reg: IIconRegister) {
		for (i in textures.indices)
			textures[i] = reg.registerIcon("thaumcraft:AlfCap$i")
	}
	
	@SideOnly(Side.CLIENT)
	override fun getIconFromDamage(meta: Int): IIcon {
		return textures[max(0, min(meta, textures.size - 1))]!!
	}
	
	@SideOnly(Side.CLIENT)
	override fun getSubItems(item: Item, tab: CreativeTabs?, list: MutableList<Any?>) {
		for (i in textures.indices)
			list.add(ItemStack(this, 1, i))
	}
	
	override fun getUnlocalizedName(stack: ItemStack): String {
		return "${super.getUnlocalizedName()}.${stack.meta}"
	}
	
	companion object {
		
		val textures = arrayOfNulls<IIcon>(5)
	}
}
