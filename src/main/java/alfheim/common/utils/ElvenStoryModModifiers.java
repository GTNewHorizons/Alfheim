package alfheim.common.utils;

import alfheim.common.crafting.RecipePureDaisyElvenStory;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePureDaisy;
import vazkii.botania.common.block.ModBlocks;

public class ElvenStoryModModifiers {

	public static void postInit() {
		for (int i = 0; i < BotaniaAPI.pureDaisyRecipes.size(); i++) if (((RecipePureDaisy) BotaniaAPI.pureDaisyRecipes.get(i)).getOutput() == ModBlocks.livingwood) BotaniaAPI.pureDaisyRecipes.set(i, new RecipePureDaisyElvenStory("logWood", ModBlocks.livingwood, 0));
	}
}