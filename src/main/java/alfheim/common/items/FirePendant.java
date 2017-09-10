package alfheim.common.items;

import alfheim.AlfheimCore;
import baubles.api.BaubleType;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;
import vazkii.botania.common.item.equipment.bauble.ItemSuperLavaPendant;

public class FirePendant extends ItemBauble {

	public FirePendant() {
		super("FirePendant");
		this.setCreativeTab(AlfheimCore.alfheimTab);
		//ItemSuperLavaPendant
	}

	@Override
	public BaubleType getBaubleType(ItemStack stack) {
		return BaubleType.AMULET;
	}

}
