package alfheim.client.integration.nei.recipes;

import java.util.List;

import alfheim.api.AlfheimAPI;
import alfheim.api.crafting.recipe.RecipeManaInfuser;
import alfheim.common.core.registry.AlfheimBlocks;
import codechicken.nei.PositionedStack;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.integration.nei.recipe.RecipeHandlerPetalApothecary;
import vazkii.botania.common.block.tile.mana.TilePool;

public class RecipeHandlerManaInfuser extends RecipeHandlerPetalApothecary {

	public class CachedManaInfuserRecipe extends CachedPetalApothecaryRecipe {

		public int manaUsage;

		public CachedManaInfuserRecipe(RecipeManaInfuser recipe) {
			super(recipe, false);
			if(recipe == null) return;
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
		int mana = ((CachedManaInfuserRecipe) arecipes.get(recipe)).manaUsage;
		HUDHandler.renderManaBar(32, 113, 0x0000FF, 0.75F, mana, TilePool.MAX_MANA * 5);
		Minecraft.getMinecraft().fontRenderer.drawString("" + mana, (168 - Minecraft.getMinecraft().fontRenderer.getStringWidth("" + mana)) / 2, 105, 0x0000FF);

	}

	@Override
	public List<? extends RecipePetals> getRecipes() {
		return AlfheimAPI.manaInfuserRecipes;
	}

	@Override
	public CachedPetalApothecaryRecipe getCachedRecipe(RecipePetals recipe) {
		return new CachedManaInfuserRecipe((RecipeManaInfuser) recipe);
	}
}