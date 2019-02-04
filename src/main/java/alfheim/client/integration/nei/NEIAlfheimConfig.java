package alfheim.client.integration.nei;

import alfheim.api.ModInfo;
import alfheim.client.integration.nei.recipes.RecipeHandlerManaInfuser;
import alfheim.client.integration.nei.recipes.RecipeHandlerTradePortal;
import alfheim.common.core.registry.AlfheimBlocks;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.block.ModBlocks;

public class NEIAlfheimConfig implements IConfigureNEI {

	@Override
	public void loadConfig() {
		API.registerRecipeHandler(new RecipeHandlerManaInfuser());
		API.registerUsageHandler(new RecipeHandlerManaInfuser());
		API.registerRecipeHandler(new RecipeHandlerTradePortal());
		API.registerUsageHandler(new RecipeHandlerTradePortal());
		
		API.hideItem(new ItemStack(ModBlocks.gaiaHead));
		API.hideItem(new ItemStack(AlfheimBlocks.anomaly));
		API.hideItem(new ItemStack(AlfheimBlocks.flugelHead));
		API.hideItem(new ItemStack(AlfheimBlocks.poisonIce));
		API.hideItem(new ItemStack(AlfheimBlocks.redFlame));
	}

	@Override
	public String getName() {
		return ModInfo.MODID;
	}

	@Override
	public String getVersion() {
		return ModInfo.VERSION;
	}
}