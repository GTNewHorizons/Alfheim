package alfmod.common.crafting.recipe

import alexsocol.asjlib.ASJUtilities.addOreDictRecipe
import alfheim.api.lib.LibOreDict
import alfheim.common.block.AlfheimBlocks
import alfmod.common.block.AlfheimModularBlocks.airyVirus
import alfmod.common.item.AlfheimModularItems
import alfmod.common.item.material.EventResourcesMetas
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
		
		
		
		addOreDictRecipe(ItemStack(AlfheimModularItems.snowSword),
						 "B", "B", "I",
						 'I', LibOreDict.NIFLHEIM_POWER_INGOT,
						 'B', ItemStack(AlfheimBlocks.alfStorage, 1, 3))
		
		addOreDictRecipe(ItemStack(AlfheimModularItems.snowHelmet),
						 "BBB", "B B",
						 'B', ItemStack(AlfheimBlocks.alfStorage, 1, 3))
		
		addOreDictRecipe(ItemStack(AlfheimModularItems.snowChest),
						 "B B", "BBB", "BBB",
						 'B', ItemStack(AlfheimBlocks.alfStorage, 1, 3))
		
		addOreDictRecipe(ItemStack(AlfheimModularItems.snowLeggings),
						 "BBB", "B B", "B B",
						 'B', ItemStack(AlfheimBlocks.alfStorage, 1, 3))
		
		addOreDictRecipe(ItemStack(AlfheimModularItems.snowBoots),
						 "B B", "B B",
						 'B', ItemStack(AlfheimBlocks.alfStorage, 1, 3))
		
		addOreDictRecipe(ItemStack(AlfheimModularItems.eventResource, 1, EventResourcesMetas.SnowRelic),
						 "HBC", "BSB", "LBT",
						 'H', AlfheimModularItems.snowHelmet,
						 'C', AlfheimModularItems.snowChest,
						 'L', AlfheimModularItems.snowLeggings,
						 'T', AlfheimModularItems.snowBoots,
						 'S', AlfheimModularItems.snowSword,
						 'B', ItemStack(AlfheimBlocks.alfStorage, 1, 3))
		
		
		
		addOreDictRecipe(ItemStack(AlfheimModularItems.volcanoMace),
						 "B", "B", "I",
						 'I', LibOreDict.MUSPELHEIM_POWER_INGOT,
						 'B', ItemStack(AlfheimBlocks.alfStorage, 1, 2))
		
		addOreDictRecipe(ItemStack(AlfheimModularItems.volcanoHelmet),
						 "BBB", "B B",
						 'B', ItemStack(AlfheimBlocks.alfStorage, 1, 2))
		
		addOreDictRecipe(ItemStack(AlfheimModularItems.volcanoChest),
						 "B B", "BBB", "BBB",
						 'B', ItemStack(AlfheimBlocks.alfStorage, 1, 2))
		
		addOreDictRecipe(ItemStack(AlfheimModularItems.volcanoLeggings),
						 "BBB", "B B", "B B",
						 'B', ItemStack(AlfheimBlocks.alfStorage, 1, 2))
		
		addOreDictRecipe(ItemStack(AlfheimModularItems.volcanoBoots),
						 "B B", "B B",
						 'B', ItemStack(AlfheimBlocks.alfStorage, 1, 2))
		
		addOreDictRecipe(ItemStack(AlfheimModularItems.eventResource, 1, EventResourcesMetas.VolcanoRelic),
						 "HBC", "BMB", "LBT",
						 'H', AlfheimModularItems.volcanoHelmet,
						 'C', AlfheimModularItems.volcanoChest,
						 'L', AlfheimModularItems.volcanoLeggings,
						 'T', AlfheimModularItems.volcanoBoots,
						 'M', AlfheimModularItems.volcanoMace,
						 'B', ItemStack(AlfheimBlocks.alfStorage, 1, 2))
	}
}