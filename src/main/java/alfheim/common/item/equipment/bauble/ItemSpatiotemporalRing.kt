package alfheim.common.item.equipment.bauble

import alfheim.common.core.util.AlfheimTab
import baubles.api.BaubleType
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.StatCollector
import net.minecraft.world.World
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.item.equipment.bauble.ItemBauble

class ItemSpatiotemporalRing: ItemBauble("spatiotemporalRing") {
	
	init {
		creativeTab = AlfheimTab
	}
	
	override fun getBaubleType(p0: ItemStack?) = BaubleType.RING
	
	fun isActive(stack: ItemStack, player: EntityPlayer) = !(!ItemNBTHelper.getBoolean(stack, TAG_ALWAYS_ON, true) && player.isSneaking)
	
	override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack {
		if (player.isSneaking)
			ItemNBTHelper.setBoolean(stack, TAG_ALWAYS_ON, !ItemNBTHelper.getBoolean(stack, TAG_ALWAYS_ON, true))
		
		return super.onItemRightClick(stack, world, player)
	}
	
	override fun addHiddenTooltip(stack: ItemStack?, player: EntityPlayer?, list: MutableList<Any?>, adv: Boolean) {
		super.addHiddenTooltip(stack, player, list, adv)
		if (!ItemNBTHelper.getBoolean(stack, TAG_ALWAYS_ON, true)) list.add(StatCollector.translateToLocal("item.botania:spatiotemporalRing.desc"))
	}
	
	companion object {
		const val TAG_ALWAYS_ON = "alwaysOn"
	}
}