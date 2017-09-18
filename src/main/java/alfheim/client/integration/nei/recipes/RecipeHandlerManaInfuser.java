package alfheim.client.integration.nei.recipes;

import java.util.List;

import alfheim.common.crafting.IManaInfusionRecipe;
import alfheim.common.crafting.ManaInfusionRecipies;
import alfheim.common.registry.AlfheimBlocks;
import codechicken.nei.PositionedStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.integration.nei.recipe.RecipeHandlerPetalApothecary;
import vazkii.botania.common.block.tile.mana.TilePool;

public class RecipeHandlerManaInfuser extends RecipeHandlerPetalApothecary {

	public class CachedManaInfuserRecipe extends CachedPetalApothecaryRecipe {

		public int manaUsage;

		public CachedManaInfuserRecipe(IManaInfusionRecipe recipe) {
			super(recipe, false);
			if(recipe == null)
				return;
			manaUsage = recipe.getManaUsage();
			inputs.add(new PositionedStack(new ItemStack(AlfheimBlocks.manaInfuser), 73, 55));
		}

	}

	@Override
	public String getRecipeName() {
		return StatCollector.translateToLocal("tile.ManaInfuser.name");
	}

	@Override
	public String getRecipeID() {
		return "alfheim.manaInfuser";
	}

	@Override
	public void drawBackground(int recipe) {
		super.drawBackground(recipe);
		HUDHandler.renderManaBar(32, 113, 0x0000FF, 0.75F, ((CachedManaInfuserRecipe) arecipes.get(recipe)).manaUsage, TilePool.MAX_MANA / 10);
	}

	@Override
	public List<? extends RecipePetals> getRecipes() {
		return ManaInfusionRecipies.recipes;
	}

	@Override
	public CachedPetalApothecaryRecipe getCachedRecipe(RecipePetals recipe) {
		return new CachedManaInfuserRecipe((IManaInfusionRecipe) recipe);
	}

}
