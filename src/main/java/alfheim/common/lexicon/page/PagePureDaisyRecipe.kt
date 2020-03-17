package alfheim.common.lexicon.page

import alfheim.api.lib.LibResourceLocations
import alfheim.client.core.util.mc
import alfheim.common.core.util.toBlock
import net.minecraft.block.Block
import net.minecraft.client.gui.GuiScreen
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.StatCollector
import net.minecraftforge.oredict.OreDictionary
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
		val recipe = recipe
		val render = mc.renderEngine
		
		render.bindTexture(LibResourceLocations.manaInfuserOverlay)
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		glColor4f(1f, 1f, 1f, 1f)
		(gui as GuiScreen).drawTexturedModalRect(gui.left, gui.top, 0, 0, gui.getWidth(), gui.getHeight())
		glDisable(GL_BLEND)
		
		var input = recipe.input as? Block
		if (input == null) run {
			val name = recipe.input as? String
			
			if (name == null) {
				input = Blocks.fire
				return@run
			}
			
			input = if (OreDictionary.doesOreNameExist(name)) {
				OreDictionary.getOres(name).firstOrNull { it.item.toBlock() != null }?.item?.toBlock()
			} else {
				Blocks.fire
			}
		}
		
		if (input == null)
			input = Blocks.fire
		
		renderItemAtGridPos(gui, 1, 1, ItemStack(input), false)
		renderItemAtGridPos(gui, 2, 1, ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_PUREDAISY), false)
		renderItemAtGridPos(gui, 3, 1, ItemStack(recipe.output), false)
		
		val width = gui.getWidth() - 30
		val height = gui.getHeight()
		val x = gui.left + 16
		val y = gui.top + height - 40
		PageText.renderText(x, y - 40, width, height, StatCollector.translateToLocal("botania.page.pureDaisy1"))
		
		super.renderScreen(gui, mx, my)
	}
}