package alfheim.client.integration.nei.recipes

import alexsocol.asjlib.*
import alfheim.api.AlfheimAPI
import alfheim.api.crafting.recipe.RecipeManaInfuser
import alfheim.common.block.AlfheimBlocks
import codechicken.nei.PositionedStack
import net.minecraft.item.ItemStack
import net.minecraft.util.StatCollector
import vazkii.botania.api.recipe.RecipePetals
import vazkii.botania.client.core.handler.HUDHandler
import vazkii.botania.client.integration.nei.recipe.RecipeHandlerPetalApothecary
import vazkii.botania.common.block.tile.mana.TilePool

class RecipeHandlerManaInfuser: RecipeHandlerPetalApothecary() {
	
	inner class CachedManaInfuserRecipe(recipe: RecipeManaInfuser?): CachedPetalApothecaryRecipe(recipe, false) {
		
		var manaUsage: Int = 0
		
		init {
			if (recipe != null) {
				manaUsage = recipe.manaUsage
				inputs.add(PositionedStack(ItemStack(AlfheimBlocks.manaInfuser), 73, 55))
			}
		}
		
	}
	
	override fun getRecipeName() = StatCollector.translateToLocal("alfheim.nei.manainfusion")!!
	
	override fun getRecipeID() = "alfheim.manaInfuser"
	
	@Strictfp
	override fun drawBackground(recipe: Int) {
		super.drawBackground(recipe)
		val mana = (arecipes[recipe] as CachedManaInfuserRecipe).manaUsage
		HUDHandler.renderManaBar(32, 113, 0x0000FF, 0.75f, mana, TilePool.MAX_MANA * 5)
		val m = ("${(mana * 10 / TilePool.MAX_MANA.D / 10)} x mana pool(s)").replace("\\.0 ".toRegex(), " ")
		mc.fontRenderer.drawString(m, (168 - mc.fontRenderer.getStringWidth(m)) / 2, 120, 0x0000FF)
	}
	
	override fun getRecipes() = AlfheimAPI.manaInfuserRecipes
	
	override fun getCachedRecipe(recipe: RecipePetals) = CachedManaInfuserRecipe(recipe as RecipeManaInfuser)
}