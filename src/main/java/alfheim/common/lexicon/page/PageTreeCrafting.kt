package vazkii.botania.common.lexicon.page

import alfheim.api.crafting.recipe.RecipeTreeCrafting
import alfheim.common.block.ShadowFoxBlocks
import cpw.mods.fml.relauncher.*
import net.minecraft.client.Minecraft
import net.minecraft.item.ItemStack
import net.minecraft.util.StatCollector
import org.lwjgl.opengl.GL11
import vazkii.botania.api.internal.IGuiLexiconEntry
import vazkii.botania.client.core.handler.HUDHandler
import vazkii.botania.common.block.tile.mana.TilePool

class PageTreeCrafting: PagePetalRecipe<RecipeTreeCrafting> {
	
	constructor(unlocalizedName: String, recipes: List<RecipeTreeCrafting>): super(unlocalizedName, recipes)
	
	constructor(unlocalizedName: String, recipes: RecipeTreeCrafting): super(unlocalizedName, recipes)
	
	override fun getMiddleStack() = ItemStack(ShadowFoxBlocks.treeCrafterBlockRB)
	
	@SideOnly(Side.CLIENT)
	override fun renderManaBar(gui: IGuiLexiconEntry, recipe: RecipeTreeCrafting, mx: Int, my: Int) {
		val font = Minecraft.getMinecraft().fontRenderer
		GL11.glEnable(GL11.GL_BLEND)
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
		val manaUsage = StatCollector.translateToLocal("botaniamisc.manaUsage")
		font.drawString(manaUsage, gui.left + gui.width / 2 - font.getStringWidth(manaUsage) / 2, gui.top + 110, 1711276032)
		var ratio = 10
		val x = gui.left + gui.width / 2 - 50
		val y = gui.top + 120
		if (mx > x + 1 && mx <= x + 101 && my > y - 14 && my <= y + 11)
			ratio = 1
		HUDHandler.renderManaBar(x, y, 255, 0.75f, recipe.manaUsage, TilePool.MAX_MANA / ratio)
		val ratioString = StatCollector.translateToLocal("botaniamisc.ratio").format(ratio)
		val stopStr = StatCollector.translateToLocal("botaniamisc.shiftToStopSpin")
		val unicode = font.unicodeFlag
		font.unicodeFlag = true
		font.drawString(stopStr, x + 50 - font.getStringWidth(stopStr) / 2, y + 15, -1728053248)
		font.drawString(ratioString, x + 50 - font.getStringWidth(ratioString) / 2, y + 5, -1728053248)
		font.unicodeFlag = unicode
		GL11.glDisable(GL11.GL_BLEND)
	}
}
