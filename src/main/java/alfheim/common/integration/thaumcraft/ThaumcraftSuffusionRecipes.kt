package alfheim.common.integration.thaumcraft

import alfheim.api.AlfheimAPI
import alfheim.api.crafting.recipe.RecipeTreeCrafting
import alfheim.api.lib.LibOreDict.LEAVES
import alfheim.common.block.AlfheimBlocks
import net.minecraft.block.Block
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import thaumcraft.common.config.*
import vazkii.botania.common.lib.LibOreDict.*

/**
 * @author WireSegal
 * Created at 9:18 PM on 1/19/16.
 */
object ThaumcraftSuffusionRecipes {
	
	var recipesLoaded = false
	
	lateinit var greatwoodRecipe: RecipeTreeCrafting
	lateinit var silverwoodRecipe: RecipeTreeCrafting
	lateinit var shimmerleafRecipe: RecipeTreeCrafting
	lateinit var cinderpearlRecipe: RecipeTreeCrafting
	lateinit var vishroomRecipe: RecipeTreeCrafting
	
	lateinit var plantBlock: Block
	lateinit var balanceShard: ItemStack
	lateinit var silverwoodLeaves: ItemStack
	
	fun initRecipes() {
		plantBlock = ConfigBlocks.blockCustomPlant
		balanceShard = ItemStack(ConfigItems.itemShard, 1, 6)
		silverwoodLeaves = ItemStack(ConfigBlocks.blockMagicalLeaves, 1, 1)
		
		recipesLoaded = true
		
		greatwoodRecipe = AlfheimAPI.addTreeRecipe(10000, ItemStack(plantBlock), ItemStack(AlfheimBlocks.irisSapling), 70, ELEMENTIUM, ELEMENTIUM, ELEMENTIUM, RUNE[4]/*Spring*/, LEAVES[13], LEAVES[13], LEAVES[13]/*Green*/, "shardEarth")
		silverwoodRecipe = AlfheimAPI.addTreeRecipe(30000, ItemStack(plantBlock, 1, 1), ItemStack(AlfheimBlocks.irisSapling), 210, ELEMENTIUM, ELEMENTIUM, ELEMENTIUM, RUNE[7]/*Winter*/, LEAVES[3], LEAVES[3], LEAVES[3]/*Light Blue*/, balanceShard)
		shimmerleafRecipe = AlfheimAPI.addTreeRecipe(5000, ItemStack(plantBlock, 1, 2), ItemStack(AlfheimBlocks.irisSapling), 100, silverwoodLeaves, "shardOrder", PIXIE_DUST)
		cinderpearlRecipe = AlfheimAPI.addTreeRecipe(5000, ItemStack(plantBlock, 1, 3), ItemStack(AlfheimBlocks.irisSapling), 100, "powderBlaze", "shardFire", PIXIE_DUST)
		vishroomRecipe = AlfheimAPI.addTreeRecipe(5000, ItemStack(plantBlock, 1, 5), ItemStack(AlfheimBlocks.irisSapling), 100, ItemStack(Blocks.brown_mushroom), ItemStack(Blocks.red_mushroom), PIXIE_DUST)
	}
}
