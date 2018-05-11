package alfheim.common.item.equipment.baubles;

import alfheim.AlfheimCore;
import baubles.api.BaubleType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import vazkii.botania.common.Botania;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;

public class CreativeReachPendant extends ItemBauble {

	public CreativeReachPendant() {
		super("CreativeReachPendant");
		this.setCreativeTab(AlfheimCore.alfheimTab);
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
