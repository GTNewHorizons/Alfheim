package alfheim.common.items;

import baubles.api.BaubleType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;
import vazkii.botania.common.item.equipment.bauble.ItemReachRing;

public class CreativeReachPendant extends ItemBauble {

	public CreativeReachPendant() {
		super("CreativeReachPendant");
	}

	@Override
	public BaubleType getBaubleType(ItemStack stack) {
		return BaubleType.AMULET;
	}

	@Override
	public void onEquippedOrLoadedIntoWorld(ItemStack stack, EntityLivingBase player) {
		Botania.proxy.setExtraReach(player, 100F);
	}

	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase player) {
		Botania.proxy.setExtraReach(player, -100F);
	}

	@Override
	public String getUnlocalizedNameInefficiently(ItemStack stack) {
		String s = this.getUnlocalizedName(stack);
        return s == null ? "" : StatCollector.translateToLocal(s);
	}
}
