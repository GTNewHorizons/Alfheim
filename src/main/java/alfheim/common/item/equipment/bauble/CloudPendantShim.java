package alfheim.common.item.equipment.bauble;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;

public abstract class CloudPendantShim extends ItemBauble {

	public CloudPendantShim(String name) {
		super(name);
	}

	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase player) {
		super.onWornTick(stack, player);

		clientWornTick(stack, player);
	}

	public void clientWornTick(ItemStack stack, EntityLivingBase player) {
		// NO-OP
	}

	public int getMaxAllowedJumps() {
		return 2;
	}

}