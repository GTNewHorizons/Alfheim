package alfheim.common.items;

import alfheim.Constants;
import cpw.mods.fml.common.Optional;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import thaumcraft.api.IGoggles;
import thaumcraft.api.nodes.IRevealer;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.handler.ConfigHandler;

@Optional.InterfaceList({
	@Optional.Interface(modid = "Thaumcraft", iface = "thaumcraft.api.IGoggles", striprefs = true),
	@Optional.Interface(modid = "Thaumcraft", iface = "thaumcraft.api.nodes.IRevealer", striprefs = true) })
public class ElvoriumHelmetRevealing extends ElvoriumHelmet implements IGoggles, IRevealer {

	public ElvoriumHelmetRevealing() {
		super("ElvoriumHelmetRevealing");
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
		return ConfigHandler.enableArmorModels ? Constants.MODID + ":textures/model/ElvoriumArmor.png" : LibResources.MODEL_MANASTEEL_2;
	}
}
