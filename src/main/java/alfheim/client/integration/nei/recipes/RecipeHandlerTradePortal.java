package alfheim.client.integration.nei.recipes;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import alfheim.api.AlfheimAPI;
import alfheim.api.ModInfo;
import alfheim.common.block.BlockTradePortal;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.client.lib.LibResources;

public class RecipeHandlerTradePortal extends TemplateRecipeHandler {

	public class CachedTradePortalRecipe extends CachedRecipe {

		public List<PositionedStack> outputs = new ArrayList<PositionedStack>();
		public PositionedStack input;

		public CachedTradePortalRecipe(RecipeElvenTrade recipe) {
			if(recipe == null)
				return;

			setIngredients(recipe.getInputs());
			input = new PositionedStack(recipe.getOutput(), 107, 46);
		}

		public void setIngredients(List<Object> inputs) {
			int i = 0;
			for(Object o : inputs) {
				if(o instanceof String)
					this.outputs.add(new PositionedStack(OreDictionary.getOres((String) o), 60 + i * 18, 6));
				else this.outputs.add(new PositionedStack(o, 60 + i * 18, 6));

				i++;
			}
		}

		@Override
		public List<PositionedStack> getIngredients() {
			return getCycledIngredients(cycleticks / 20, outputs);
		}

		@Override
		public PositionedStack getResult() {
			return input;
		}

	}

	public String getRecipeID() {
		return "alfheim.tradeportal";
	}
	
	@Override
	public String getRecipeName() {
		return StatCollector.translateToLocal("alfheim.nei.tradeportal");
	}

	@Override
	public String getGuiTexture() {
		return LibResources.GUI_NEI_BLANK;
	}

	@Override
	public void loadTransferRects() {
		transferRects.add(new RecipeTransferRect(new Rectangle(35, 30, 48, 48), getRecipeID()));
	}

	@Override
	public int recipiesPerPage() {
		return 1;
	}

	@Override
	public void drawBackground(int recipe) {
		super.drawBackground(recipe);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.7F);
		GuiDraw.changeTexture(ModInfo.MODID + ":textures/gui/TradePortalOverlay.png");
		GuiDraw.drawTexturedModalRect(30, 10, 17, 17, 100, 80);
		GL11.glDisable(GL11.GL_BLEND);
		GuiDraw.changeTexture(TextureMap.locationBlocksTexture);
		RenderItem.getInstance().renderIcon(35, 29, BlockTradePortal.textures[2], 48, 48);
	}

	private static boolean hasElvenKnowledge() {
		/*EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		if (player != null) {
			for (ItemStack stack : player.inventory.mainInventory) {
				if (stack != null && stack.getItem() instanceof ILexicon) {
					ILexicon lexicon = (ILexicon) stack.getItem();
					if (lexicon.isKnowledgeUnlocked(stack, BotaniaAPI.elvenKnowledge)) {
						return true;
					}
				}
			}
		}
		return false;*/
		return true;
	}

	@Override
	public void loadCraftingRecipes(String outputId, Object... results) {
		if(outputId.equals(getRecipeID()) && hasElvenKnowledge()) {
			for (RecipeElvenTrade recipe : BotaniaAPI.elvenTradeRecipes) {
				if (recipe == null || !AlfheimAPI.isRetradeable(recipe.getOutput()))
					continue;

				arecipes.add(new CachedTradePortalRecipe(recipe));
			}
		} else super.loadCraftingRecipes(outputId, results);
	}

	@Override
	public void loadCraftingRecipes(ItemStack result) {
		if(hasElvenKnowledge()) {
			for(RecipeElvenTrade recipe : BotaniaAPI.elvenTradeRecipes) {
				if(recipe == null || !AlfheimAPI.isRetradeable(recipe.getOutput()))
					continue;

				CachedTradePortalRecipe crecipe = new CachedTradePortalRecipe(recipe);
				if(crecipe.contains(crecipe.outputs, result))
					arecipes.add(crecipe);
			}
		}
	}

	@Override
	public void loadUsageRecipes(ItemStack ingredient) {
		if(hasElvenKnowledge()) {
			for(RecipeElvenTrade recipe : BotaniaAPI.elvenTradeRecipes) {
				if(recipe == null || !AlfheimAPI.isRetradeable(recipe.getOutput()))
					continue;

				if(NEIServerUtils.areStacksSameTypeCrafting(recipe.getOutput(), ingredient))
					arecipes.add(new CachedTradePortalRecipe(recipe));
			}
		}
	}
}