package alfheim.common.item.equipment.bauble

import alfheim.AlfheimCore
import baubles.api.BaubleType
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import vazkii.botania.api.mana.IManaGivingItem
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.item.equipment.bauble.ItemBauble

open class ItemAuraRingAlfheim(name: String): ItemBauble(name), IManaGivingItem {
	
	open val delay: Int
		get() = 5
	
	init {
		creativeTab = AlfheimCore.alfheimTab
	}
	
	override fun onWornTick(stack: ItemStack, player: EntityLivingBase) {
		super.onWornTick(stack, player)
		if (player is EntityPlayer && player.ticksExisted % delay == 0)
			ManaItemHandler.dispatchManaExact(stack, player, 10, true)
	}
	
	override fun getBaubleType(itemstack: ItemStack): BaubleType {
		return BaubleType.RING
	}
}