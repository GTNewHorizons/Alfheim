package alfheim.common.item.equipment.bauble

import alfheim.AlfheimCore
import alfheim.api.item.equipment.bauble.IManaDiscountBauble
import alfheim.common.integration.travellersgear.*
import baubles.api.BaubleType
import cpw.mods.fml.common.Optional
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import vazkii.botania.common.item.equipment.bauble.ItemBauble

@Optional.Interface(modid = "TravellersGear", iface = "alfheim.common.integration.travellersgear.ITravellersGearSynced", striprefs = true)
class ItemManaweaveGloves: ItemBauble("ManaweaveGloves"), IManaDiscountBauble, ITravellersGearSynced {
	
	override fun getDiscount(stack: ItemStack, slot: Int, player: EntityPlayer) = 0.075f
	
	override fun getBaubleType(stack: ItemStack) =
		if (AlfheimCore.TravellersGearLoaded) null else BaubleType.RING
	
	override fun getSlot(stack: ItemStack?) = 2
	
	override fun onTravelGearEquip(player: EntityPlayer, stack: ItemStack) {
		super.onTravelGearEquip(player, stack)
		onEquippedOrLoadedIntoWorld(stack, player)
	}
	
	override fun onTravelGearTickSynced(player: EntityPlayer, stack: ItemStack) {
		onWornTick(stack, player)
	}
	
	override fun onTravelGearUnequip(player: EntityPlayer, stack: ItemStack) {
		super.onTravelGearUnequip(player, stack)
		onUnequipped(stack, player)
	}
	
	override fun addHiddenTooltip(stack: ItemStack, player: EntityPlayer, tooltip: MutableList<Any?>, adv: Boolean) {
		TravellerBaubleTooltipHandler.addHiddenTooltip(this, stack, tooltip)
	}
}