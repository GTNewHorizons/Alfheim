package alfheim.common.items;

import alfheim.AlfheimCore;
import alfheim.Constants;
import alfheim.common.registry.AlfheimItems;
import cpw.mods.fml.common.Optional;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import thaumcraft.api.IGoggles;
import thaumcraft.api.nodes.IRevealer;
import vazkii.botania.client.lib.LibResources;
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
		return Constants.MODID + ":textures/model/armor/ElementalArmor_2.png";
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
		if (armorType == 0 && player.getCurrentArmor(3) != null && player.getCurrentArmor(3).getItem() == AlfheimItems.elementalHelmetRevealing && player.isInWater()) {
			player.addPotionEffect(new PotionEffect(Potion.waterBreathing.id, 1, -1));
			player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 3, -1));
		}
    }
}
