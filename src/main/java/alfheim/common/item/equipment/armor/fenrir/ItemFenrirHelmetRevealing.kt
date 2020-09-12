package alfheim.common.item.equipment.armor.fenrir

import alexsocol.asjlib.render.ASJRenderHelper
import alfheim.api.ModInfo
import alfheim.common.core.helper.IconHelper
import alfheim.common.integration.thaumcraft.ThaumcraftAlfheimModule
import alfheim.common.item.AlfheimItems
import cpw.mods.fml.common.Optional
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import thaumcraft.api.IGoggles
import thaumcraft.api.nodes.IRevealer
import vazkii.botania.common.core.handler.ConfigHandler

@Optional.InterfaceList(
	Optional.Interface(modid = "Thaumcraft", iface = "thaumcraft.api.IGoggles", striprefs = true),
	Optional.Interface(modid = "Thaumcraft", iface = "thaumcraft.api.nodes.IRevealer", striprefs = true)
)
class ItemFenrirHelmetRevealing: ItemFenrirArmor(0, "FenrirHelmetRevealing"), IGoggles, IRevealer {
	
	init {
		creativeTab = ThaumcraftAlfheimModule.tcnTab
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerIcons(reg: IIconRegister) {
		overlay = IconHelper.forItem(reg, this, "1")
	}
	
	override fun getIconFromDamage(meta: Int): IIcon {
		if (itemIcon == null)
			itemIcon = AlfheimItems.fenrirHelmet.itemIcon
		
		return itemIcon
	}
	
	@Optional.Method(modid = "Thaumcraft")
	override fun showNodes(itemstack: ItemStack?, player: EntityLivingBase?) = true
	
	@Optional.Method(modid = "Thaumcraft")
	override fun showIngamePopups(itemstack: ItemStack?, player: EntityLivingBase?) = true
	
	override fun getArmorTextureAfterInk(stack: ItemStack?, slot: Int, type: String?): String {
		if (type == "overlay")
			ASJRenderHelper.setGlow()
		
		val t = type?.plus("Revealing") ?: ""
		
		return "${ModInfo.MODID}:textures/model/armor/FenrirArmor${if (ConfigHandler.enableArmorModels) "" else "2"}$t.png"
	}
}