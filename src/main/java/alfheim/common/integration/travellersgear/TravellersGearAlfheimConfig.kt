package alfheim.common.integration.travellersgear

import alfheim.common.item.AlfheimItems
import cpw.mods.fml.common.event.FMLInterModComms
import net.minecraft.item.ItemStack
import vazkii.botania.common.item.ModItems

object TravellersGearAlfheimConfig {
	
	fun loadConfig() {
		FMLInterModComms.sendMessage("TravellersGear", "registerTravellersGear_0", ItemStack(ModItems.holyCloak))
		FMLInterModComms.sendMessage("TravellersGear", "registerTravellersGear_0", ItemStack(ModItems.unholyCloak))
		FMLInterModComms.sendMessage("TravellersGear", "registerTravellersGear_0", ItemStack(AlfheimItems.balanceCloak))
	}
}