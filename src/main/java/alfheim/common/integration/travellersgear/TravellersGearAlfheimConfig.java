package alfheim.common.integration.travellersgear;

import alfheim.common.core.registry.AlfheimItems;
import cpw.mods.fml.common.event.FMLInterModComms;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.item.ModItems;

public class TravellersGearAlfheimConfig {

	public static void loadConfig() {
		FMLInterModComms.sendMessage("TravellersGear", "registerTravellersGear_0", new ItemStack(ModItems.holyCloak));
		FMLInterModComms.sendMessage("TravellersGear", "registerTravellersGear_0", new ItemStack(ModItems.unholyCloak));
		FMLInterModComms.sendMessage("TravellersGear", "registerTravellersGear_0", new ItemStack(AlfheimItems.balanceCloak));
	}
}