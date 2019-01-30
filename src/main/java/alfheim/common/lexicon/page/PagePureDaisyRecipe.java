package alfheim.common.lexicon.page;

import static org.lwjgl.opengl.GL11.*;

import alfheim.api.lib.LibResourceLocations;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconRecipeMappings;
import vazkii.botania.api.recipe.RecipePureDaisy;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lexicon.page.PageRecipe;
import vazkii.botania.common.lexicon.page.PageText;
import vazkii.botania.common.lib.LibBlockNames;

public class PagePureDaisyRecipe extends PageRecipe {

	private RecipePureDaisy recipe;
	
	public PagePureDaisyRecipe(String unlocalizedName, RecipePureDaisy recipe) {
		super(unlocalizedName);
		this.recipe = recipe;
	}

	@Override
	public void onPageAdded(LexiconEntry entry, int index) {
		LexiconRecipeMappings.map(new ItemStack(recipe.getOutput()), entry, index);
	}
	
	@Override
	public void renderScreen(IGuiLexiconEntry gui, int mx, int my) {
		RecipePureDaisy recipe = this.recipe;
		TextureManager render = Minecraft.getMinecraft().renderEngine;
		FontRenderer font = Minecraft.getMinecraft().fontRenderer;
		
		render.bindTexture(LibResourceLocations.manaInfuserOverlay);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glColor4f(1F, 1F, 1F, 1F);
		((GuiScreen) gui).drawTexturedModalRect(gui.getLeft(), gui.getTop(), 0, 0, gui.getWidth(), gui.getHeight());
		glDisable(GL_BLEND);

		renderItemAtGridPos(gui, 1, 1, new ItemStack(Item.getItemFromBlock((Block) recipe.getInput())), false);
		renderItemAtGridPos(gui, 2, 1, ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_PUREDAISY), false);
		renderItemAtGridPos(gui, 3, 1, new ItemStack(Item.getItemFromBlock(recipe.getOutput())), false);
		
		int width = gui.getWidth() - 30;
		int height = gui.getHeight();
		int x = gui.getLeft() + 16;
		int y = gui.getTop() + height - 40;
		PageText.renderText(x, y - 40, width, height, StatCollector.translateToLocal("botania.page.pureDaisy1"));
		
		super.renderScreen(gui, mx, my);
	}
}