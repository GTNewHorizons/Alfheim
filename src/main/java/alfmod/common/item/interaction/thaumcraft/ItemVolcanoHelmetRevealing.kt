package alfmod.common.item.interaction.thaumcraft

import alfheim.common.integration.thaumcraft.ThaumcraftAlfheimModule
import alfmod.AlfheimModularCore
import alfmod.common.item.equipment.armor.ItemSnowArmor
import cpw.mods.fml.common.Optional
import cpw.mods.fml.common.Optional.InterfaceList
import net.minecraft.entity.EntityLivingBase
import net.minecraft.item.ItemStack
import thaumcraft.api.IGoggles
import thaumcraft.api.nodes.IRevealer
import vazkii.botania.common.core.handler.ConfigHandler

@InterfaceList(
	Optional.Interface(modid = "Thaumcraft", iface = "thaumcraft.api.IGoggles", striprefs = true),
	Optional.Interface(modid = "Thaumcraft", iface = "thaumcraft.api.nodes.IRevealer", striprefs = true)
)
class ItemVolcanoHelmetRevealing: ItemSnowArmor(0, "VolcanoHelmetRevealing"), IGoggles, IRevealer {
	
	init {
		creativeTab = ThaumcraftAlfheimModule.tcnTab
	}
	
	@Optional.Method(modid = "Thaumcraft")
	override fun showNodes(itemstack: ItemStack?, player: EntityLivingBase?) = true
	
	@Optional.Method(modid = "Thaumcraft")
	override fun showIngamePopups(itemstack: ItemStack?, player: EntityLivingBase?) = true
	
	override fun getArmorTextureAfterInk(stack: ItemStack?, slot: Int) =
		"${AlfheimModularCore.MODID}:textures/model/volcano${if (ConfigHandler.enableArmorModels) "New" else "2"}.png"
}