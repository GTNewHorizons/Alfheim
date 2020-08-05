package alfheim.common.item.equipment.bauble

import alexsocol.asjlib.*
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.util.AlfheimTab
import baubles.api.*
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import vazkii.botania.api.item.IRelic
import vazkii.botania.api.mana.*
import vazkii.botania.common.item.*
import vazkii.botania.common.item.equipment.bauble.ItemBauble

/**
 * @author ExtraMeteorP, CKATEPTb
 */
class ItemMultibauble: ItemBauble("multibauble"), IManaGivingItem {
	
	init {
		creativeTab = AlfheimTab
	}
	
	override fun onWornTick(stack: ItemStack?, player: EntityLivingBase?) {
		super.onWornTick(stack, player)
		if (player !is EntityPlayer) return
		
		val slot = ASJUtilities.getSlotWithItem(ModItems.baubleBox, player.inventory)
		if (slot == -1) return
		
		val baubles = ItemBaubleBox.loadStacks(player.inventory[slot])
		
		baubles.forEachIndexed {
			i, bauble ->
			if (i < AlfheimConfigHandler.multibaubleCount && bauble != null) {
				val item = bauble.item
				if (item is IBauble && item !is ItemMultibauble && item !is IRelic) {
					if (ManaItemHandler.requestManaExact(stack, player, 2, !player.worldObj.isRemote))
						item.onWornTick(bauble, player)
				}
			}
		}
		
		ItemBaubleBox.setStacks(player.inventory[slot], baubles)
	}
	
	override fun getBaubleType(p0: ItemStack?) = BaubleType.RING
}
