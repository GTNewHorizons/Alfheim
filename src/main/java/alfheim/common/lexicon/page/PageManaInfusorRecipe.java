package alfheim.common.lexicon.page;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import alfheim.client.integration.nei.recipes.RecipeHandlerManaInfuser.CachedManaInfuserRecipe;
import alfheim.common.core.registry.AlfheimBlocks;
import alfheim.common.crafting.recipe.IManaInfusionRecipe;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconRecipeMappings;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lexicon.page.PagePetalRecipe;
import vazkii.botania.common.lexicon.page.PageRecipe;
import vazkii.botania.common.lexicon.page.PageRuneRecipe;

public class PageManaInfusorRecipe extends PageRecipe {

	private static final ResourceLocation manaInfusorOverlay = new ResourceLocation(LibResources.GUI_PETAL_OVERLAY);
	private IManaInfusionRecipe recipe;
	private int ticksElapsed = 0;

	public PageManaInfusorRecipe(String unlocalizedName, IManaInfusionRecipe recipe) {
		super(unlocalizedName);
		this.recipe = recipe;
	}

	@Override
	public void onPageAdded(LexiconEntry entry, int index) {
		LexiconRecipeMappings.map(recipe.getOutput(), entry, index);
	}
	
	@Override
	public void renderScreen(IGuiLexiconEntry gui, int mx, int my) {
		TextureManager render = Minecraft.getMinecraft().renderEngine;

		renderItemAtGridPos(gui, 3, 0, recipe.getOutput(), false);
		renderItemAtGridPos(gui, 2, 1, new ItemStack(AlfheimBlocks.manaInfuser), false);

		List<Object> inputs = recipe.getInputs();
		int degreePerInput = (int) (360F / inputs.size());
		float currentDegree = ConfigHandler.lexiconRotatingItems ? GuiScreen.isShiftKeyDown() ? ticksElapsed : (float) (ticksElapsed + ClientTickHandler.partialTicks) : 0;

		for(Object obj : inputs) {
			Object input = obj;
			if(input instanceof String)
				input = OreDictionary.getOres((String) input).get(0);

			renderItemAtAngle(gui, currentDegree, (ItemStack) input);

			currentDegree += degreePerInput;
		}

		renderManaBar(gui, recipe, mx, my);

		render.bindTexture(manaInfusorOverlay);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		((GuiScreen) gui).drawTexturedModalRect(gui.getLeft(), gui.getTop(), 0, 0, gui.getWidth(), gui.getHeight());
		GL11.glDisable(GL11.GL_BLEND);
		
		super.renderScreen(gui, mx, my);
	}
	
	@SideOnly(Side.CLIENT)
	public void renderManaBar(IGuiLexiconEntry gui, IManaInfusionRecipe recipe2, int mx, int my) {
		FontRenderer font = Minecraft.getMinecraft().fontRenderer;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		String manaUsage = StatCollector.translateToLocal("botaniamisc.manaUsage") + ": ";
		int x1 = gui.getLeft() + gui.getWidth() / 2 - font.getStringWidth(manaUsage + recipe2.getManaUsage()) / 2;
		font.drawString(manaUsage, x1, gui.getTop() + 110, 0x66000000);
		font.drawString("" + recipe2.getManaUsage(), x1 + font.getStringWidth(manaUsage), gui.getTop() + 110, 0x0000FF);

		int ratio = 5;
		int x = gui.getLeft() + gui.getWidth() / 2 - 50;
		int y = gui.getTop() + 120;

		if(mx > x + 1 && mx <= x + 101 && my > y - 14 && my <= y + 11)
			ratio = 1;

		HUDHandler.renderManaBar(x, y, 0x0000FF, 0.75F, recipe2.getManaUsage(), TilePool.MAX_MANA * ratio);

		String ratioString = String.format(StatCollector.translateToLocal("botaniamisc.ratio"), 1.0 / ratio);
		String stopStr = StatCollector.translateToLocal("botaniamisc.shiftToStopSpin");

		boolean unicode = font.getUnicodeFlag();
		font.setUnicodeFlag(true);
		font.drawString(stopStr, x + 50 - font.getStringWidth(stopStr) / 2, y + 15, 0x99000000);
		font.drawString(ratioString, x + 50 - font.getStringWidth(ratioString) / 2, y + 5, 0x99000000);
		font.setUnicodeFlag(unicode);
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateScreen() {
		if(GuiScreen.isShiftKeyDown()) return;
		++ticksElapsed;
	}
	
	@Override
	public List<ItemStack> getDisplayedRecipes() {
		ArrayList<ItemStack> list = new ArrayList();
		list.add(recipe.getOutput());
		return list;
	}
}
