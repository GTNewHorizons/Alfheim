package alfheim.client.integration.nei;

import alfheim.ModInfo;
import alfheim.client.integration.nei.recipes.RecipeHandlerManaInfuser;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEIAlfheimConfig implements IConfigureNEI {

	@Override
	public void loadConfig() {
		API.registerRecipeHandler(new RecipeHandlerManaInfuser());
		API.registerUsageHandler(new RecipeHandlerManaInfuser());
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
