package alfheim.common.lexicon.page

import alexsocol.asjlib.mc
import alfheim.api.ModInfo
import cpw.mods.fml.relauncher.*
import net.minecraft.client.gui.GuiScreen
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.FurnaceRecipes
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import vazkii.botania.api.internal.IGuiLexiconEntry
import vazkii.botania.api.lexicon.*
import vazkii.botania.client.lib.LibResources
import vazkii.botania.common.lexicon.page.PageRecipe
import java.util.*
import kotlin.math.*

class PageFurnaceRecipe: PageRecipe {
	
	private class StackPair(val input: ItemStack, val output: ItemStack)
	
	private val manaInfusionOverlay = ResourceLocation(LibResources.GUI_MANA_INFUSION_OVERLAY)
	private val furnaceFlame = ResourceLocation("${ModInfo.MODID}:textures/gui/furnaceFlames.png")
	
	private var recipes: MutableList<StackPair>
	var ticksElapsed = 0
	var renderTicksElapsed = 0
	var rTPS = 50
	var recipeAt = 0
	
	constructor(unlocalizedName: String, inputs: MutableList<ItemStack>): super(unlocalizedName) {
		recipes = ArrayList()
		for (inp in inputs) {
			val output = FurnaceRecipes.smelting().getSmeltingResult(inp)
						 ?: throw IllegalArgumentException("Invalid input")
			recipes.add(StackPair(inp, output))
		}
	}
	
	constructor(unlocalizedName: String, input: ItemStack): super(unlocalizedName) {
		val output = FurnaceRecipes.smelting().getSmeltingResult(input)
					 ?: throw IllegalArgumentException("Invalid input")
		recipes = arrayListOf(StackPair(input, output))
	}
	
	override fun onPageAdded(entry: LexiconEntry, index: Int) {
		for (recipe in recipes)
			LexiconRecipeMappings.map(recipe.output, entry, index)
	}
	
	@SideOnly(Side.CLIENT)
	override fun renderRecipe(gui: IGuiLexiconEntry, mx: Int, my: Int) {
		val recipe = recipes[recipeAt]
		val render = mc.renderEngine
		
		renderItemAtGridPos(gui, 1, 1, recipe.input, false)
		renderItemAtGridPos(gui, 2, 1, ItemStack(Blocks.furnace), false)
		renderItemAtGridPos(gui, 3, 1, recipe.output, false)
		
		renderTicksElapsed++
		
		render.bindTexture(furnaceFlame)
		val flameCornerX = gui.left + gui.width / 2 - 8
		val flameCornerY = gui.top + gui.height / 2 + 5
		GL11.glEnable(GL11.GL_BLEND)
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
		(gui as GuiScreen).drawTexturedModalRect(flameCornerX, flameCornerY, 0, 0, 16, 16)
		val progress = max(min(renderTicksElapsed * 13 / rTPS, 13), 0)
		gui.drawTexturedModalRect(flameCornerX, flameCornerY + progress, 16, progress, 16, 16 - progress)
		GL11.glDisable(GL11.GL_BLEND)
		
		render.bindTexture(manaInfusionOverlay)
		GL11.glEnable(GL11.GL_BLEND)
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
		GL11.glColor4f(1F, 1F, 1F, 1F)
		gui.drawTexturedModalRect(gui.left, gui.top, 0, 0, gui.getWidth(), gui.getHeight())
		GL11.glDisable(GL11.GL_BLEND)
	}
	
	@SideOnly(Side.CLIENT)
	override fun updateScreen() {
		if (ticksElapsed % 20 == 0) {
			recipeAt++
			if (renderTicksElapsed != 0) rTPS = renderTicksElapsed * 1
			renderTicksElapsed = 0
			
			if (recipeAt == recipes.size)
				recipeAt = 0
		}
		++ticksElapsed
	}
}
