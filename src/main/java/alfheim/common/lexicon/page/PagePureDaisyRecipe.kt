package alfheim.common.lexicon.page

import alfheim.api.lib.LibResourceLocations
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.item.*
import net.minecraft.util.StatCollector
import org.lwjgl.opengl.GL11.*
import vazkii.botania.api.internal.IGuiLexiconEntry
import vazkii.botania.api.lexicon.*
import vazkii.botania.api.recipe.RecipePureDaisy
import vazkii.botania.common.item.block.ItemBlockSpecialFlower
import vazkii.botania.common.lexicon.page.*
import vazkii.botania.common.lib.LibBlockNames

class PagePureDaisyRecipe(unlocalizedName: String, private val recipe: RecipePureDaisy): PageRecipe(unlocalizedName) {
	
	override fun onPageAdded(entry: LexiconEntry?, index: Int) {
		LexiconRecipeMappings.map(ItemStack(recipe.output), entry!!, index)
	}
	
	override fun renderScreen(gui: IGuiLexiconEntry, mx: Int, my: Int) {
		val recipe = this.recipe
		val render = Minecraft.getMinecraft().renderEngine
		val font = Minecraft.getMinecraft().fontRenderer
		
		render.bindTexture(LibResourceLocations.manaInfuserOverlay)
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		glColor4f(1f, 1f, 1f, 1f)
		(gui as GuiScreen).drawTexturedModalRect(gui.left, gui.top, 0, 0, gui.getWidth(), gui.getHeight())
		glDisable(GL_BLEND)
		
		renderItemAtGridPos(gui, 1, 1, ItemStack(Item.getItemFromBlock(recipe.input as Block)), false)
		renderItemAtGridPos(gui, 2, 1, ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_PUREDAISY), false)
		renderItemAtGridPos(gui, 3, 1, ItemStack(Item.getItemFromBlock(recipe.output)), false)
		
		val width = gui.getWidth() - 30
		val height = gui.getHeight()
		val x = gui.left + 16
		val y = gui.top + height - 40
		PageText.renderText(x, y - 40, width, height, StatCollector.translateToLocal("botania.page.pureDaisy1"))
		
		super.renderScreen(gui, mx, my)
	}
}