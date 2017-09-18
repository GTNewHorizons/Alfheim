package alfheim.client.integration.nei;

import alfheim.Constants;
import alfheim.client.integration.nei.recipes.RecipeHandlerManaInfuser;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

public class NEIAlfheimConfig implements IConfigureNEI {

	@Override
	public void loadConfig() {
		Constants.debug("NEI config Loaded!");
		API.registerRecipeHandler(new RecipeHandlerManaInfuser());
		API.registerUsageHandler(new RecipeHandlerManaInfuser());
	}

	@Override
	public String getName() {
		return Constants.MODID;
	}

	@Override
	public String getVersion() {
		return Constants.VERSION;
	}

}
