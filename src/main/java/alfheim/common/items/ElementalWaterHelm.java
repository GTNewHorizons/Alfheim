package alfheim.common.items;

import java.util.List;

import alfheim.AlfheimCore;
import alfheim.common.registry.AlfheimItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaDiscountArmor;

public class ElementalWaterHelm extends ElementalArmor implements IManaDiscountArmor {
	
	public ElementalWaterHelm() {
		super(0, "ElementalWaterHelm");
		this.setCreativeTab(AlfheimCore.alfheimTab);
	}

	public ElementalWaterHelm(String name) {
		super(0, name);
		this.setCreativeTab(AlfheimCore.alfheimTab);
	}

	@Override
	public float getPixieChance(ItemStack stack) {
		return 0.11F;
	}

	@Override
	public float getDiscount(ItemStack stack, int slot, EntityPlayer player) {
		return hasArmorSet(player) ? 0.1F : 0F;
	}

	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack stack) {
		if (armorType == 0 && player.getCurrentArmor(3) != null && player.getCurrentArmor(3).getItem() == AlfheimItems.elementalHelmet && player.isInWater()) {
			player.addPotionEffect(new PotionEffect(Potion.waterBreathing.id, 1, -1));
			player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 3, -1));
		}
    }
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean b) {
		list.add(StatCollector.translateToLocal("item.ElementalArmor.desc4"));
		super.addInformation(stack, player, list, b);
	}
}