package alfheim.common.item.equipment.baubles;

import alfheim.AlfheimCore;
import baubles.api.BaubleType;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;

public class IcePendant extends ItemBauble {

	public IcePendant() {
		super("IcePendant");
		this.setCreativeTab(AlfheimCore.alfheimTab);
	}

	@Override
	public BaubleType getBaubleType(ItemStack stack) {
		return BaubleType.AMULET;
	}
}