package alfheim.api

import alexsocol.asjlib.ASJUtilities
import alfheim.api.block.tile.SubTileAnomalyBase
import alfheim.api.crafting.recipe.RecipeManaInfuser
import alfheim.api.entity.EnumRace
import alfheim.api.lib.LibResourceLocations
import alfheim.api.security.ISecurityManager
import alfheim.api.spell.SpellBase
import alfheim.common.block.tile.TileAnomalyHarvester
import com.google.common.collect.Lists
import cpw.mods.fml.common.Loader
import cpw.mods.fml.relauncher.FMLRelaunchLog
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.EnumHelper
import net.minecraftforge.oredict.OreDictionary
import org.apache.logging.log4j.Level
import vazkii.botania.api.recipe.RecipeElvenTrade
import java.util.*
import kotlin.collections.HashMap

object AlfheimAPI {
	
	val ElvoriumArmor = EnumHelper.addArmorMaterial("ELVORIUM", 50, intArrayOf(5, 10, 8, 5), 30)!!
	val elvoriumToolMaterial = EnumHelper.addToolMaterial("ELVORIUM", 4, 2400, 9.5f, 3f, 30)!!
	val ElementalArmor = EnumHelper.addArmorMaterial("ELEMENTAL", 20, intArrayOf(2, 9, 5, 2), 20)!!
	val mauftriumToolmaterial = EnumHelper.addToolMaterial("REALITY", 10, 9000, 3f, 8f, 40)!!
	
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
	/** Map of security managers by name */
	val securityManagers = HashMap<String, ISecurityManager>()
	/** Petronia fuels map */
	val fuelMap = HashMap<String, Pair<Int, Int>>()
	/** Ores for Orechid Endium */
	var oreWeightsEnd = HashMap<String, Int>()
	
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
	
	fun registerSecurityManager(man: ISecurityManager, name: String) {
		if (name.isBlank()) throw IllegalArgumentException("Name should not be blank")
		if (securityManagers.containsKey(name)) throw IllegalArgumentException("Security Manager \"$name\" is already registered")
		
		ASJUtilities.log("Registering security manager with name \"$name\"")
		securityManagers[name] = man
	}
	
	fun registerFuel(name: String, burnTime: Int, manaPerTick: Int) {
		fuelMap[name] = burnTime to manaPerTick
	}
	
	/**
	 * Maps an ore (ore dictionary key) to it's weight on the End world generation. This
	 * is used for the Orechid Endium flower. Check the static block in the EndiumOrechidAPI class
	 * to get the weights for the vanilla blocks.<br></br>
	 * Alternatively get the values with the OreDetector mod:<br></br>
	 * https://gist.github.com/Vazkii/9493322
	 */
	fun addOreWeightEnd(ore: String, weight: Int) {
		addOreWeightEnd(ore, weight, false)
	}
	
	fun addOreWeightEnd(ore: String, weight: Int, override: Boolean) {
		if (!override && ore.contains("Ender") && OreDictionary.getOres(ore.replace("Ender", "")).size == 0) return
		if (!override && ore.contains("End") && OreDictionary.getOres(ore.replace("End", "")).size == 0) return
		
		oreWeightsEnd[ore] = weight
	}
	
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
		addOreWeightEnd("oreEndBiotite", 500, true) // OreDictionary.registerOre("oreEndBiotite", Biotite.biotite_ore)
		addOreWeightEnd("oreEndDraconium", 200) // OreDictionary.registerOre("oreEndDraconium", ItemStack(DEFeatures.draconiumOre, 1, 2))
		addOreWeightEnd("oreDraconiumEnd", 200) // OreDictionary.registerOre("oreDraconiumEnd", ItemStack(DEFeatures.draconiumOre, 1, 2))
	}
}
