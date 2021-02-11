package alfheim.common.integration.travellersgear

import cpw.mods.fml.common.Optional
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import travellersgear.api.ITravellersGear
import vazkii.botania.common.core.helper.ItemNBTHelper

@Optional.Interface(modid = "TravellersGear", iface = "travellersgear.api.ITravellersGear", striprefs = true)
interface ITravellersGearSynced: ITravellersGear {
	
	override fun onTravelGearEquip(player: EntityPlayer, stack: ItemStack) {
		ItemNBTHelper.setBoolean(stack, TAG_EQUIPPED, true)
	}
	
	override fun onTravelGearTick(player: EntityPlayer, stack: ItemStack) {
		// because for some reason it gets called AFTER unequip :/
		if (ItemNBTHelper.getBoolean(stack, TAG_EQUIPPED, false))
			onTravelGearTickSynced(player, stack)
	}
	
	fun onTravelGearTickSynced(player: EntityPlayer, stack: ItemStack) = Unit
	
	override fun onTravelGearUnequip(player: EntityPlayer, stack: ItemStack) {
		ItemNBTHelper.setBoolean(stack, TAG_EQUIPPED, false)
	}
	
	companion object {
		
		const val TAG_EQUIPPED = "equipped" // damn it, next time check your sync!
	}
}
