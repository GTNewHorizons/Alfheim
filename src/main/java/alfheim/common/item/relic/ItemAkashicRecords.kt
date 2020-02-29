package alfheim.common.item.relic

import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import vazkii.botania.common.core.helper.ItemNBTHelper.*
import vazkii.botania.common.item.relic.ItemRelic
import kotlin.math.max

class ItemAkashicRecords: ItemRelic("AkashicRecords") {
	
	override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack {
		setBoolean(stack, TAG_SWITCH, true)
		return stack
	}
	
	override fun onUpdate(stack: ItemStack, world: World?, entity: Entity?, slot: Int, inHand: Boolean) {
		var frame = (getInt(stack, TAG_FRAME, 0) + getInt(stack, TAG_MULT, -1))
		
		if (frame == 160) frame = 60
		
		if ((frame % 100 == 60 || frame == -1) && getBoolean(stack, TAG_SWITCH, false)) {
			setInt(stack, TAG_MULT, getInt(stack, TAG_MULT, -1) * -1)
			setBoolean(stack, TAG_SWITCH, false)
			setInt(stack, TAG_FRAME, if (frame != -1) 60 else 1)
		} else {
			setInt(stack, TAG_FRAME, max(0, frame))
		}
	}
	
	companion object {
		const val TAG_FRAME = "frame"
		const val TAG_MULT = "mult"
		const val TAG_SWITCH = "switch"
	}
}
