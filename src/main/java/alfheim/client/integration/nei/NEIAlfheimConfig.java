package alfheim.client.integration.nei;

import alfheim.api.ModInfo;
import alfheim.client.integration.nei.recipes.*;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEIAlfheimConfig implements IConfigureNEI {

	@Override
	public void loadConfig() {
		API.registerRecipeHandler(new RecipeHandlerManaInfuser());
		API.registerUsageHandler(new RecipeHandlerManaInfuser());
		API.registerRecipeHandler(new RecipeHandlerTradePortal());
		API.registerUsageHandler(new RecipeHandlerTradePortal());
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