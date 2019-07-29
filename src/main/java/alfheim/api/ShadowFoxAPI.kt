package alfheim.api

import alfheim.api.crafting.recipe.RecipeTreeCrafting
import alfheim.api.item.ThrowableCollidingItem
import alfheim.api.trees.*
import net.minecraft.block.Block
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.EnumHelper
import java.util.*

object ShadowFoxAPI {
	
	var RUNEAXE = EnumHelper.addToolMaterial("RUNEAXE", 4, 1561, 8f, 2f, 50)!!
	
	var treeRecipes: MutableList<RecipeTreeCrafting> = ArrayList()
	var treeVariants: MutableList<IIridescentSaplingVariant> = ArrayList()
	var collidingItemHashMap: MutableMap<String, ThrowableCollidingItem> = LinkedHashMap()
	var fallbackTcl = ThrowableCollidingItem("shadowfox_fallback", ItemStack(Items.blaze_rod)) { _, _ -> }
	
	/**
	 * Adds a tree crafting recipe to the registry.
	 *
	 * @param recipe - The recipe to add.
	 * @return The recipe that was added to the registry.
	 */
	fun addTreeRecipe(recipe: RecipeTreeCrafting) =
		recipe.also { treeRecipes.add(it) }
	
	/**
	 * Adds a tree crafting recipe with the specified parameters to the registry.
	 *
	 * @param mana        - The mana cost for the recipe.
	 * @param outputBlock - The block that is created from the recipe.
	 * @param meta        - The metadata of the created block.
	 * @param inputs      - The items used in the infusion.
	 * @return The recipe that was added to the registry.
	 */
	fun addTreeRecipe(mana: Int, outputBlock: Block, meta: Int, vararg inputs: Any) =
		addTreeRecipe(RecipeTreeCrafting(mana, outputBlock, meta, *inputs))
	
	/**
	 * Adds a tree crafting recipe with the specified parameters to the registry.
	 *
	 * @param mana        - The mana cost for the recipe.
	 * @param outputBlock - The block that is created from the recipe.
	 * @param meta        - The metadata of the created block.
	 * @param inputs      - The items used in the infusion.
	 * @param throttle    - The maximum mana that can be absorbed per tick for this recipe.
	 * @return The recipe that was added to the registry.
	 */
	fun addTreeRecipe(mana: Int, outputBlock: Block, meta: Int, throttle: Int, vararg inputs: Any) =
		addTreeRecipe(RecipeTreeCrafting(mana, outputBlock, meta, throttle, *inputs))
	
	/**
	 * Adds an Iridescent Sapling variant to the registry.
	 *
	 * @param variant - The variant to add.
	 * @return The variant added to the registry.
	 */
	fun addTreeVariant(variant: IIridescentSaplingVariant) =
		variant.also { treeVariants.add(it) }
	
	/**
	 * Adds an Iridescent Sapling variant with the specified parameters to the registry, ignoring metadata.
	 *
	 * @param soil   - The soil block the variant uses.
	 * @param wood   - The wood block the variant uses.
	 * @param leaves - The leaves block the variant uses.
	 * @return The variant that was added to the registry.
	 */
	fun addTreeVariant(soil: Block, wood: Block, leaves: Block) =
		addTreeVariant(IridescentSaplingBaseVariant(soil, wood, leaves))
	
	/**
	 * Adds an Iridescent Sapling variant with the specified parameters to the registry, with a specific metadata.
	 *
	 * @param soil   - The soil block the variant uses.
	 * @param wood   - The wood block the variant uses.
	 * @param leaves - The leaves block the variant uses.
	 * @param meta   - The metadata of the soil the variant uses.
	 * @return The variant that was added to the registry.
	 */
	fun addTreeVariant(soil: Block, wood: Block, leaves: Block, meta: Int) =
		addTreeVariant(IridescentSaplingBaseVariant(soil, wood, leaves, meta))
	
	/**
	 * Adds an Iridescent Sapling variant with the specified parameters to the registry, using a range of metadata.
	 *
	 * @param soil    - The soil block the variant uses.
	 * @param wood    - The wood block the variant uses.
	 * @param leaves  - The leaves block the variant uses.
	 * @param metaMin - The minimum meta value of the soil the variant uses.
	 * @param metaMax - The maximum meta value of the soil the variant uses.
	 * @return The variant that was added to the registry.
	 */
	fun addTreeVariant(soil: Block, wood: Block, leaves: Block, metaMin: Int, metaMax: Int) =
		addTreeVariant(IridescentSaplingBaseVariant(soil, wood, leaves, metaMin, metaMax))
	
	/**
	 * Adds an Iridescent Sapling variant with the specified parameters to the registry, using a range of metadata.
	 *
	 * @param soil      - The soil block the variant uses.
	 * @param wood      - The wood block the variant uses.
	 * @param leaves    - The leaves block the variant uses.
	 * @param metaMin   - The minimum meta value of the soil the variant uses.
	 * @param metaMax   - The maximum meta value of the soil the variant uses.
	 * @param metaShift - The amount to subtract from the soil's metadata value to make the leaf metadata.
	 * @return The variant that was added to the registry.
	 */
	fun addTreeVariant(soil: Block, wood: Block, leaves: Block, metaMin: Int, metaMax: Int, metaShift: Int) =
		addTreeVariant(IridescentSaplingBaseVariant(soil, wood, leaves, metaMin, metaMax, metaShift))
	
	fun registerThrowable(tcl: ThrowableCollidingItem) =
		tcl.also { collidingItemHashMap[it.key] = it }
	
	fun getThrowableFromKey(key: String) =
		collidingItemHashMap[key] ?: fallbackTcl
	
	/**
	 * Gets a list of all acceptable Iridescent Sapling soils.
	 *
	 * @return A list of all Iridescent Sapling soils.
	 */
	fun getIridescentSoils(): List<Block> {
		val soils = ArrayList<Block>()
		for (variant in treeVariants) {
			soils.addAll(variant.acceptableSoils)
		}
		return soils
	}
	
	/**
	 * Gets the variant for a given soil.
	 *
	 * @param soil - The block the sapling is placed on.
	 * @param meta - The meta of the block the sapling is on.
	 * @return The variant, if there is one.
	 */
	fun getTreeVariant(soil: Block, meta: Int) =
		treeVariants.firstOrNull { it.matchesSoil(soil, meta) }
}
