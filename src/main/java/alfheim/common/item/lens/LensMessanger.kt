package alfheim.common.item.lens

import net.minecraft.item.ItemStack
import vazkii.botania.api.mana.BurstProperties
import vazkii.botania.common.item.lens.Lens

class LensMessanger: Lens() {
	
	override fun apply(stack: ItemStack?, props: BurstProperties) {
		props.maxMana /= 5
		props.ticksBeforeManaLoss *= 3
		props.motionModifier *= 3f
	}
}