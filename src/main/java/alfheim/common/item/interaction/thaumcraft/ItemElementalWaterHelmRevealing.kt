package alfheim.common.item.interaction.thaumcraft

import alfheim.api.ModInfo
import alfheim.common.integration.thaumcraft.ThaumcraftAlfheimModule
import alfheim.common.item.equipment.armor.elemental.ItemElementalWaterHelm
import cpw.mods.fml.common.Optional
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import thaumcraft.api.IGoggles
import thaumcraft.api.nodes.IRevealer
import vazkii.botania.common.core.handler.ConfigHandler

@Optional.InterfaceList(Optional.Interface(modid = "Thaumcraft", iface = "thaumcraft.api.IGoggles", striprefs = true), Optional.Interface(modid = "Thaumcraft", iface = "thaumcraft.api.nodes.IRevealer", striprefs = true))
class ItemElementalWaterHelmRevealing: ItemElementalWaterHelm("ElementalWaterHelmRevealing"), IGoggles, IRevealer {
	
	init {
		creativeTab = ThaumcraftAlfheimModule.tcnTab
	}
	
	override fun showNodes(itemstack: ItemStack, player: EntityLivingBase): Boolean {
		return true
	}
	
	override fun showIngamePopups(itemstack: ItemStack, player: EntityLivingBase): Boolean {
		return true
	}
	
	override fun getArmorTextureAfterInk(stack: ItemStack?, slot: Int): String {
		return ModInfo.MODID + ":textures/model/armor/ElementalArmor_" + (if (ConfigHandler.enableArmorModels) "new" else "2") + ".png"
	}
	
	override fun onArmorTick(world: World, player: EntityPlayer, stack: ItemStack?) {
		super.onArmorTick(world, player, stack)
	}
}
