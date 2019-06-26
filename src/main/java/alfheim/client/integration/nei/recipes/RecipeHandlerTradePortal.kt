package alfheim.client.integration.nei.recipes

import org.lwjgl.opengl.GL11.*

import java.awt.Rectangle
import java.util.ArrayList

import alfheim.api.AlfheimAPI
import alfheim.api.ModInfo
import alfheim.common.block.BlockTradePortal
import codechicken.lib.gui.GuiDraw
import codechicken.nei.NEIServerUtils
import codechicken.nei.PositionedStack
import codechicken.nei.recipe.TemplateRecipeHandler
import net.minecraft.client.renderer.entity.RenderItem
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.item.ItemStack
import net.minecraft.util.StatCollector
import net.minecraftforge.oredict.OreDictionary
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.recipe.RecipeElvenTrade
import vazkii.botania.client.lib.LibResources

class RecipeHandlerTradePortal: TemplateRecipeHandler() {
	
	val recipeID: String
		get() = "alfheim.tradeportal"
	
	inner class CachedTradePortalRecipe(recipe: RecipeElvenTrade?): TemplateRecipeHandler.CachedRecipe() {
		
		val outputs: MutableList<PositionedStack> = ArrayList()
		var input: PositionedStack
		
		init {
			if (recipe == null)
				return
			
			setIngredients(recipe!!.inputs)
			input = PositionedStack(recipe.output, 107, 46)
		}
		
		fun setIngredients(inputs: List<Any>) {
			var i = 0
			for (o in inputs) {
				if (o is String)
					this.outputs.add(PositionedStack(OreDictionary.getOres(o), 60 + i * 18, 6))
				else
					this.outputs.add(PositionedStack(o, 60 + i * 18, 6))
				
				i++
			}
		}
		
		override fun getIngredients(): List<PositionedStack> {
			return getCycledIngredients(cycleticks / 20, outputs)
		}
		
		override fun getResult(): PositionedStack {
			return input
		}
		
	}
	
	override fun getRecipeName(): String {
		return StatCollector.translateToLocal("alfheim.nei.tradeportal")
	}
	
	override fun getGuiTexture(): String {
		return LibResources.GUI_NEI_BLANK
	}
	
	override fun loadTransferRects() {
		transferRects.add(TemplateRecipeHandler.RecipeTransferRect(Rectangle(35, 30, 48, 48), recipeID))
	}
	
	override fun recipiesPerPage(): Int {
		return 1
	}
	
	override fun drawBackground(recipe: Int) {
		super.drawBackground(recipe)
		glEnable(GL_BLEND)
		glColor4f(1.0f, 1.0f, 1.0f, 0.7f)
		GuiDraw.changeTexture(ModInfo.MODID + ":textures/gui/TradePortalOverlay.png")
		GuiDraw.drawTexturedModalRect(30, 10, 17, 17, 100, 80)
		glDisable(GL_BLEND)
		GuiDraw.changeTexture(TextureMap.locationBlocksTexture)
		RenderItem.getInstance().renderIcon(35, 29, BlockTradePortal.textures[2], 48, 48)
	}
	
	private fun hasElvenKnowledge(): Boolean {
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
		return true
	}
	
	override fun loadCraftingRecipes(outputId: String, vararg results: Any) {
		if (outputId == recipeID && hasElvenKnowledge()) {
			for (recipe in BotaniaAPI.elvenTradeRecipes) {
				if (recipe == null || !AlfheimAPI.isRetradeable(recipe.output))
					continue
				
				arecipes.add(CachedTradePortalRecipe(recipe))
			}
		} else
			super.loadCraftingRecipes(outputId, *results)
	}
	
	override fun loadCraftingRecipes(result: ItemStack?) {
		if (hasElvenKnowledge()) {
			for (recipe in BotaniaAPI.elvenTradeRecipes) {
				if (recipe == null || !AlfheimAPI.isRetradeable(recipe.output))
					continue
				
				val crecipe = CachedTradePortalRecipe(recipe)
				if (crecipe.contains(crecipe.outputs, result))
					arecipes.add(crecipe)
			}
		}
	}
	
	override fun loadUsageRecipes(ingredient: ItemStack?) {
		if (hasElvenKnowledge()) {
			for (recipe in BotaniaAPI.elvenTradeRecipes) {
				if (recipe == null || !AlfheimAPI.isRetradeable(recipe.output))
					continue
				
				if (NEIServerUtils.areStacksSameTypeCrafting(recipe.output, ingredient))
					arecipes.add(CachedTradePortalRecipe(recipe))
			}
		}
	}
}