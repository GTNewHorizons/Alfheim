package alfheim.common.lexicon.page

import alexsocol.asjlib.*
import alfheim.api.crafting.recipe.RecipeManaInfuser
import alfheim.api.lib.LibResourceLocations
import alfheim.common.block.AlfheimBlocks
import cpw.mods.fml.relauncher.*
import net.minecraft.client.gui.GuiScreen
import net.minecraft.item.ItemStack
import net.minecraft.util.StatCollector
import net.minecraftforge.oredict.OreDictionary
import org.lwjgl.opengl.GL11.*
import vazkii.botania.api.internal.IGuiLexiconEntry
import vazkii.botania.api.lexicon.*
import vazkii.botania.client.core.handler.*
import vazkii.botania.common.block.tile.mana.TilePool
import vazkii.botania.common.core.handler.ConfigHandler
import vazkii.botania.common.lexicon.page.PageRecipe

class PageManaInfusorRecipe(unlocalizedName: String, private val recipe: RecipeManaInfuser): PageRecipe(unlocalizedName) {
	
	private var ticksElapsed = 0
	
	override fun onPageAdded(entry: LexiconEntry?, index: Int) = LexiconRecipeMappings.map(recipe.output, entry!!, index)
	
	override fun renderScreen(gui: IGuiLexiconEntry, mx: Int, my: Int) {
		val render = mc.renderEngine
		
		renderItemAtGridPos(gui, 3, 0, recipe.output, false)
		renderItemAtGridPos(gui, 2, 1, ItemStack(AlfheimBlocks.manaInfuser), false)
		
		val inputs = recipe.inputs
		val degreePerInput = (360f / inputs.size).I
		var currentDegree = if (ConfigHandler.lexiconRotatingItems) if (GuiScreen.isShiftKeyDown()) ticksElapsed.F else ticksElapsed + ClientTickHandler.partialTicks else 0f
		
		for (obj in inputs) {
			var input = obj
			if (input is String)
				input = OreDictionary.getOres(input)[0]
			
			renderItemAtAngle(gui, currentDegree, input as ItemStack)
			
			currentDegree += degreePerInput.F
		}
		
		renderManaBar(gui, recipe, mx, my)
		
		render.bindTexture(LibResourceLocations.petalOverlay)
		
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		glColor4f(1f, 1f, 1f, 1f)
		(gui as GuiScreen).drawTexturedModalRect(gui.left, gui.top, 0, 0, gui.getWidth(), gui.getHeight())
		glDisable(GL_BLEND)
		
		super.renderScreen(gui, mx, my)
	}
	
	@SideOnly(Side.CLIENT)
	fun renderManaBar(gui: IGuiLexiconEntry, recipe2: RecipeManaInfuser, mx: Int, my: Int) {
		val font = mc.fontRenderer
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		val manaUsage = StatCollector.translateToLocal("botaniamisc.manaUsage")
		font.drawString(manaUsage, gui.left + (gui.width - font.getStringWidth(manaUsage)) / 2, gui.top + 110, 0x66000000)
		
		var ratio = 5
		val x = gui.left + gui.width / 2 - 50
		val y = gui.top + 120
		
		if (mx > x + 1 && mx <= x + 101 && my > y - 14 && my <= y + 11)
			ratio = 1
		
		HUDHandler.renderManaBar(x, y, 0x0000FF, 0.75f, recipe2.manaUsage, TilePool.MAX_MANA * ratio)
		
		val ratioString = String.format(StatCollector.translateToLocal("botaniamisc.ratio"), 1.0 / ratio)
		val stopStr = StatCollector.translateToLocal("botaniamisc.shiftToStopSpin")
		
		val unicode = font.unicodeFlag
		font.unicodeFlag = true
		font.drawString(stopStr, x + 50 - font.getStringWidth(stopStr) / 2, y + 15, -0x67000000)
		font.drawString(ratioString, x + 50 - font.getStringWidth(ratioString) / 2, y + 5, -0x67000000)
		font.unicodeFlag = unicode
		glDisable(GL_BLEND)
	}
	
	@SideOnly(Side.CLIENT)
	override fun updateScreen() {
		if (GuiScreen.isShiftKeyDown()) return
		++ticksElapsed
	}
	
	override fun getDisplayedRecipes() = listOf(recipe.output)
}
