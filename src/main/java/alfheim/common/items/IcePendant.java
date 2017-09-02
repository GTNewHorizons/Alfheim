package alfheim.common.items;

import baubles.api.BaubleType;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;

public class IcePendant extends ItemBauble {

	public IcePendant() {
		super("IcePendant");
	}

	@Override
	public BaubleType getBaubleType(ItemStack stack) {
		return BaubleType.AMULET;
	}
}