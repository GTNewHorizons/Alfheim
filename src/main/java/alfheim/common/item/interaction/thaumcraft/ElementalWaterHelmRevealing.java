package alfheim.common.item.interaction.thaumcraft;

import alfheim.AlfheimCore;
import alfheim.Constants;
import alfheim.common.item.equipment.armor.elemental.ElementalWaterHelm;
import cpw.mods.fml.common.Optional;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.api.IGoggles;
import thaumcraft.api.nodes.IRevealer;
import vazkii.botania.common.core.handler.ConfigHandler;

@Optional.InterfaceList({
		@Optional.Interface(modid = "Thaumcraft", iface = "thaumcraft.api.IGoggles", striprefs = true),
		@Optional.Interface(modid = "Thaumcraft", iface = "thaumcraft.api.nodes.IRevealer", striprefs = true) })
public class ElementalWaterHelmRevealing extends ElementalWaterHelm implements IGoggles, IRevealer {
	
	public ElementalWaterHelmRevealing() {
		super("ElementalWaterHelmRevealing");
		this.setCreativeTab(AlfheimCore.alfheimTab);
	}

	@Override
	public boolean showNodes(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}

	@Override
	public boolean showIngamePopups(ItemStack itemstack, EntityLivingBase player) {
		return true;
	}

	@Override
	public String getArmorTextureAfterInk(ItemStack stack, int slot) {
		return Constants.MODID + ":textures/model/armor/ElementalArmor_" + (ConfigHandler.enableArmorModels ? "new" : "2") + ".png";
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
		super.onArmorTick(world, player, stack);
    }
}
