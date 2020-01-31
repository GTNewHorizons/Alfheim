package alfmod.common.crafting.recipe

import alexsocol.asjlib.ASJUtilities.addOreDictRecipe
import alfheim.api.lib.LibOreDict
import alfmod.common.block.AlfheimModularBlocks.airyVirus
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.common.block.ModBlocks
import vazkii.botania.common.item.ModItems

object AlfheimModularRecipes {
	
	lateinit var recipeAiryVirus: IRecipe
	lateinit var recipeAiryAntivirus: IRecipe
	
	init {
		registerCraftingRecipes()
	}
	
	private fun registerCraftingRecipes() {
		
		addOreDictRecipe(ItemStack(airyVirus, 3),
									  "RGR", "EVE", "AAA",
									  'R', LibOreDict.REDSTONE_DUST,
									  'G', LibOreDict.GLOWSTONE_DUST,
									  'E', LibOreDict.MUSPELHEIM_ESSENCE,
									  'V', ModItems.virus,
									  'A', ItemStack(ModBlocks.altGrass, 1, 3))
		recipeAiryVirus = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(airyVirus, 3, 1),
									  "IMI", "ECE", "AAA",
									  'I', LibOreDict.IFFESAL_DUST,
									  'M', vazkii.botania.common.lib.LibOreDict.MANA_POWDER,
									  'E', LibOreDict.NIFLHEIM_ESSENCE,
									  'C', ModBlocks.cellBlock,
									  'A', ItemStack(ModBlocks.altGrass, 1, 4))
		recipeAiryAntivirus = BotaniaAPI.getLatestAddedRecipe()
	}
}