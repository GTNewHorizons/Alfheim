package alfheim.common.item.lens;

import net.minecraft.item.ItemStack;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.common.item.lens.Lens;

public class LensMessanger extends Lens {
	
	@Override
	public void apply(ItemStack stack, BurstProperties props) {
		props.maxMana /= 5;
		props.ticksBeforeManaLoss *= 3;
		props.motionModifier *= 3F;
	}
}