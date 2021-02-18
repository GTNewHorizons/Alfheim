package alfheim.api

import alexsocol.asjlib.ASJUtilities
import alfheim.api.block.tile.SubTileAnomalyBase
import alfheim.api.crafting.recipe.*
import alfheim.api.entity.EnumRace
import alfheim.api.item.ThrowableCollidingItem
import alfheim.api.lib.LibResourceLocations
import alfheim.api.spell.SpellBase
import alfheim.api.trees.*
import alfheim.common.block.tile.TileAnomalyHarvester
import com.google.common.collect.Lists
import cpw.mods.fml.common.Loader
import cpw.mods.fml.relauncher.FMLRelaunchLog
import net.minecraft.block.Block
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.EnumHelper
import org.apache.logging.log4j.Level
import vazkii.botania.api.recipe.RecipeElvenTrade
import java.util.*
import kotlin.collections.HashMap

object AlfheimAPI {
	
	val ElvoriumArmor = EnumHelper.addArmorMaterial("ELVORIUM", 50, intArrayOf(5, 10, 8, 5), 30)
	val elvoriumToolMaterial = EnumHelper.addToolMaterial("ELVORIUM", 4, 2400, 9.5f, 3f, 30)
	val ElementalArmor = EnumHelper.addArmorMaterial("ELEMENTAL", 20, intArrayOf(2, 9, 5, 2), 20)
	val mauftriumToolmaterial = EnumHelper.addToolMaterial("REALITY", 10, 9000, 3f, 8f, 40)
	
	// relic
	val EXCALIBER = EnumHelper.addToolMaterial("EXCALIBER", 3, -1, 6.2f, 6f, 40)
	val FENRIR = EnumHelper.addToolMaterial("FENRIR", 0, 2000, 0f, 3.0f, 14)
	var RUNEAXE = EnumHelper.addToolMaterial("RUNEAXE", 4, 1561, 8f, 2f, 50)
	val SOUL = EnumHelper.addToolMaterial("SOUL", -1, -1, -1f, -1f, -1) // ragnarok sword
	
	/** List of [RecipeElvenTrade] outputs banned for re'trading in Alfheim trade portal  */
	val bannedRetrades = ArrayList<ItemStack>()
	
	/** List of recipies for mana infuser  */
	val manaInfuserRecipes = ArrayList<RecipeManaInfuser>()
	
	/** List of all pink items with their relative pinkness  */
	val pinkness = HashMap<ItemStack, Int>()
	
	/** List of all spells for all races  */
	val spells = HashSet<SpellBase>()
	
	/** Map of elven spells associated with their race (affinity), sorted by name  */
	val spellMapping = HashMap<EnumRace, HashSet<SpellBase>>()
	
	/** Map of anomaly types and their subtiles, specifying their behavior  */
	val anomalies = HashMap<String, Class<out SubTileAnomalyBase>>()
	
	/** Map of anomaly types and their behavior for use in [TileAnomalyHarvester] */
	val anomalyBehaviors = HashMap<String, ((TileAnomalyHarvester) -> Unit)>()
	
	/** Map of anomaly types and their subtile instances, used for render :o  */
	val anomalyInstances = HashMap<String, SubTileAnomalyBase>()
	
	/** Petronia fuels map */
	val fuelMap = HashMap<String, Pair<Int, Int>>()
	
	/** Ores for Orechid Endium */
	var oreWeightsEnd = HashMap<String, Int>()
	
	var treeRecipes: MutableList<RecipeTreeCrafting> = ArrayList()
	var treeVariants: MutableList<IIridescentSaplingVariant> = ArrayList()
	var collidingItemHashMap: MutableMap<String, ThrowableCollidingItem> = LinkedHashMap()
	var fallbackTcl = ThrowableCollidingItem("shadowfox_fallback", ItemStack(Items.blaze_rod)) { _, _ -> }
	
	fun addInfuserRecipe(rec: RecipeManaInfuser?): RecipeManaInfuser? {
		if (rec != null) manaInfuserRecipes.add(rec)
		return rec
	}
	
	fun addInfuserRecipe(result: ItemStack, mana: Int, vararg ingredients: Any): RecipeManaInfuser {
		val rec = RecipeManaInfuser(mana, result, *ingredients)
		manaInfuserRecipes.add(rec)
		return rec
	}
	
	fun removeInfusionRecipe(rec: RecipeManaInfuser?): RecipeManaInfuser? =
		if (rec != null && manaInfuserRecipes.remove(rec)) rec else null
	
	fun removeInfusionRecipe(result: ItemStack): RecipeManaInfuser? =
		manaInfuserRecipes.indices
			.firstOrNull { ItemStack.areItemStacksEqual(manaInfuserRecipes[it].output, result) }
			?.let { manaInfuserRecipes.removeAt(it) }
	
	/** Remove `output` from Alfheim trade portal  */
	fun banRetrade(output: ItemStack) =
		bannedRetrades.add(output)
	
	/** Check if `output` isn't banned to be obtained through Alfheim trade portal  */
	fun isRetradeable(output: ItemStack) =
		bannedRetrades.none { ItemStack.areItemStacksEqual(output, it) }
	
	/** Map a stack to it's pinkness. Also can override old values  */
	fun addPink(pink: ItemStack, weight: Int) =
		pinkness.put(pink, weight)
	
	fun getPinkness(item: ItemStack) =
		pinkness.keys
			.firstOrNull { ASJUtilities.isItemStackEqualCrafting(it, item) }
			?.let { pinkness[it]!! } ?: 0
	
	/**
	 * Registers spell for some race by affinity
	 *
	 * Note:
	 * Salamander - Fire
	 * Sylph - Wind
	 * Cait Sith - Nature
	 * Pooka - Sound
	 * Gnome - Earth
	 * Leprechaun - Tech
	 * Spriggan - Illusion
	 * Undine - Water
	 * Imp - Darkness
	 */
	fun registerSpell(spell: SpellBase) {
		// here goes hook to disable spell in configs
		require(spell.race != EnumRace.HUMAN) { "Spell race must not be human (spell ${spell.name})" }
		require(spell.race != EnumRace.ALV) { "Alv race is currently not supported (spell ${spell.name})" }
		
		if (spells.add(spell)) {
			checkGet(spell.race).add(spell)
			LibResourceLocations.add(spell.name)
		} else
			FMLRelaunchLog.log(ModInfo.MODID.toUpperCase(), Level.WARN, "Trying to register spell " + spell.name + " twice. Skipping.")
		
	}
	
	private fun checkGet(affinity: EnumRace): HashSet<SpellBase> {
		if (!spellMapping.containsKey(affinity)) spellMapping[affinity] = HashSet(8)
		return spellMapping[affinity]!!
	}
	
	fun getSpellsFor(affinity: EnumRace): ArrayList<SpellBase> {
		val l = Lists.newArrayList(checkGet(affinity))
		l.sortWith(Comparator { s1, s2 -> s1.name.compareTo(s2.name) })
		return l
	}
	
	fun getSpellInstance(name: String) = spells.firstOrNull { it.name == name }
	
	fun getSpellByIDs(raceID: Int, spellID: Int): SpellBase? {
		var i = 0
		for (sb in getSpellsFor(EnumRace[raceID])) if (i++ == spellID) return sb
		return null
	}
	
	fun getSpellID(spell: SpellBase): Int {
		for (race in EnumRace.values()) {
			var i = -1
			for (sb in getSpellsFor(race)) {
				++i
				if (sb === spell) return i
			}
		}
		
		throw IllegalArgumentException("Client-server spells desynchronization. Not found ID for " + spell.name)
	}
	
	/** Register anomaly [subtile] with unique [name] */
	fun registerAnomaly(name: String, subtile: Class<out SubTileAnomalyBase>) {
		if (anomalies.containsKey(name)) throw IllegalArgumentException("Anomaly \"$name\" is already registered")
		anomalies[name] = subtile
		
		try {
			anomalyInstances[name] = subtile.newInstance()
		} catch (e: Throwable) {
			FMLRelaunchLog.log(Loader.instance().activeModContainer().modId.toUpperCase(), Level.ERROR, e, "Cannot instantiate anomaly subtile for ${subtile.canonicalName}")
			throw IllegalArgumentException("Uninstantiatable anomaly subtile $subtile")
		}
	}
	
	fun getAnomaly(name: String): Class<out SubTileAnomalyBase> = anomalies[name] ?: FallbackAnomaly::class.java
	
	fun getAnomalyInstance(name: String) = anomalyInstances[name] ?: FallbackAnomaly
	
	fun registerFuel(name: String, burnTime: Int, manaPerTick: Int) {
		fuelMap[name] = burnTime to manaPerTick
	}
	
	/**
	 * Maps an ore (ore dictionary key) to it's weight on the End world generation. This
	 * is used for the Orechid Endium flower. Check the static block in the EndiumOrechidAPI class
	 * to get the weights for the vanilla blocks.<br></br>
	 * Alternatively get the values with the OreDetector mod:<br></br>
	 * https://gist.github.com/Vazkii/9493322
	 *
	 * Higher weight means higher chance
	 */
	fun addOreWeightEnd(ore: String, weight: Int) {
//		addOreWeightEnd(ore, weight, false)
		
		oreWeightsEnd[ore] = weight
	}
	
	// wtf was this
//	fun addOreWeightEnd(ore: String, weight: Int, override: Boolean) {
//		if (!override && ore.contains("Ender") && OreDictionary.getOres(ore.replace("Ender", "")).size == 0) return
//		if (!override && ore.contains("End") && OreDictionary.getOres(ore.replace("End", "")).size == 0) return
//
//		oreWeightsEnd[ore] = weight
//	}
	
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
	 * @param mana   - The mana cost for the recipe.
	 * @param out    - The block that is created from the recipe.
	 * @param core   - The core block in center that will be changed.
	 * @param inputs - The items used in the infusion.
	 * @return The recipe that was added to the registry.
	 */
	fun addTreeRecipe(mana: Int, out: ItemStack, core: ItemStack, vararg inputs: Any) =
		addTreeRecipe(RecipeTreeCrafting(mana, out, core, *inputs))
	
	/**
	 * Adds a tree crafting recipe with the specified parameters to the registry.
	 *
	 * @param mana     - The mana cost for the recipe.
	 * @param out      - The block that is created from the recipe.
	 * @param core     - The core block in center that will be changed.
	 * @param inputs   - The items used in the infusion.
	 * @param throttle - The maximum mana that can be absorbed per tick for this recipe.
	 * @return The recipe that was added to the registry.
	 */
	fun addTreeRecipe(mana: Int, out: ItemStack, core: ItemStack, throttle: Int, vararg inputs: Any) =
		addTreeRecipe(RecipeTreeCrafting(mana, out, core, throttle, *inputs))
	
	fun removeTreeRecipe(rec: RecipeTreeCrafting?): RecipeTreeCrafting? =
		if (rec != null && treeRecipes.remove(rec)) rec else null
	
	fun removeTreeRecipe(result: ItemStack): RecipeTreeCrafting? =
		treeRecipes.indices
			.firstOrNull { ItemStack.areItemStacksEqual(treeRecipes[it].output, result) }
			?.let { treeRecipes.removeAt(it) }
	
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
	
	object FallbackAnomaly: SubTileAnomalyBase() {
		
		override val targets: List<Any> = emptyList()
		override val rarity = EnumAnomalityRarity.COMMON
		override val strip = 31
		override fun performEffect(target: Any) = Unit
		override fun typeBits() = 0
	}
	
	init {
		addOreWeightEnd("oreEndCoal", 9000)
		addOreWeightEnd("oreEndCopper", 4700)
		addOreWeightEnd("oreEndDiamond", 500)
		addOreWeightEnd("oreEndEmerald", 500)
		addOreWeightEnd("oreEndGold", 3635)
		addOreWeightEnd("oreEndIron", 5790)
		addOreWeightEnd("oreEndLapis", 3250)
		addOreWeightEnd("oreEndLead", 2790)
		addOreWeightEnd("oreEndNickel", 1790)
		addOreWeightEnd("oreEndPlatinum", 350)
		addOreWeightEnd("oreEndRedstone", 5600)
		addOreWeightEnd("oreEndSilver", 1550)
		addOreWeightEnd("oreEndSteel", 1690)
		addOreWeightEnd("oreEndMithril", 1000)
		addOreWeightEnd("oreEndCertusQuartz", 2000)
		addOreWeightEnd("oreEndChargedCertusQuartz", 950)
		addOreWeightEnd("oreEndUranium", 2000)
		addOreWeightEnd("oreEndArdite", 1000)
		addOreWeightEnd("oreEndCobalt", 1000)
		addOreWeightEnd("oreEndOsmium", 1000)
		addOreWeightEnd("oreEndIridium", 850)
		addOreWeightEnd("oreEndYellorite", 3000)
		addOreWeightEnd("oreClathrateEnder", 800)
		addOreWeightEnd("oreEndProsperity", 200)
		addOreWeightEnd("oreEndTin", 3750)
		addOreWeightEnd("oreEndInferium", 500)
		addOreWeightEnd("oreEndBiotite", 500) // OreDictionary.registerOre("oreEndBiotite", Biotite.biotite_ore)
		addOreWeightEnd("oreEndDraconium", 200) // OreDictionary.registerOre("oreEndDraconium", ItemStack(DEFeatures.draconiumOre, 1, 2))
		addOreWeightEnd("oreDraconiumEnd", 200) // OreDictionary.registerOre("oreDraconiumEnd", ItemStack(DEFeatures.draconiumOre, 1, 2))
	}
}
