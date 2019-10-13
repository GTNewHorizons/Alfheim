package alfheim.api

import alfheim.api.block.tile.SubTileEntity
import alfheim.api.crafting.recipe.RecipeManaInfuser
import alfheim.api.entity.EnumRace
import alfheim.api.lib.LibResourceLocations
import alfheim.api.spell.SpellBase
import com.google.common.collect.Lists
import cpw.mods.fml.common.Loader
import cpw.mods.fml.relauncher.FMLRelaunchLog
import net.minecraft.item.ItemStack
import net.minecraftforge.common.util.EnumHelper
import org.apache.logging.log4j.Level
import vazkii.botania.api.recipe.RecipeElvenTrade
import java.util.*

object AlfheimAPI {
	
	val ELVORIUM = EnumHelper.addArmorMaterial("ELVORIUM", 50, intArrayOf(5, 10, 8, 5), 30)!!
	val ELEMENTAL = EnumHelper.addArmorMaterial("ELEMENTAL", 20, intArrayOf(2, 9, 5, 2), 20)!!
	val REALITY = EnumHelper.addToolMaterial("REALITY", 10, 9000, 3f, 8f, 30)!!
	
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
	val anomalies = HashMap<String, Class<out SubTileEntity>>()
	/** Map of anomaly types and their subtile instances, used for render :o  */
	val anomalyInstances = HashMap<String, SubTileEntity>()
	
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
			.firstOrNull { it.item === item.item && it.itemDamage == item.itemDamage }
			?.let { pinkness[it]!! } ?: 0
	
	/**
	 * Registers spell for some race by affinity
	 * <br></br>
	 * Note:<br></br>
	 * Salamander - Fire<br></br>
	 * Sylph - Wind<br></br>
	 * Cait Sith - Nature<br></br>
	 * Pooka - Sound<br></br>
	 * Gnome - Earth<br></br>
	 * Leprechaun - Tech<br></br>
	 * Spriggan - Illusion<br></br>
	 * Undine - Water<br></br>
	 * Imp - Darkness<br></br>
	 */
	fun registerSpell(spell: SpellBase) {
		require(spell.race != EnumRace.HUMAN) { "Spell race must be one of the elements" }
		require(spell.race != EnumRace.ALV) { "This race is currently not supported" }
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
	
	fun getSpellInstance(name: String) =
		spells.firstOrNull { it.name == name }
	
	fun getSpellByIDs(raceID: Int, spellID: Int): SpellBase? {
		var i = 0
		for (sb in getSpellsFor(EnumRace[raceID])) if (i++ == spellID) return sb
		return null
	}
	
	fun getSpellID(spell: SpellBase): Int {
		var i: Int
		for (race in EnumRace.values()) {
			i = -1
			for (sb in getSpellsFor(race)) {
				++i
				if (sb === spell) return i
			}
		}
		throw IllegalArgumentException("Client-server spells desynchronization. Not found ID for " + spell.name)
	}
	
	fun registerAnomaly(name: String, behavior: Class<out SubTileEntity>) {
		anomalies[name] = behavior
		try {
			anomalyInstances[name] = behavior.newInstance()
		} catch (e: Throwable) {
			FMLRelaunchLog.log(Loader.instance().activeModContainer().modId.toUpperCase(), Level.ERROR, e, "Cannot instantiate anomaly subtile for ${behavior.canonicalName}")
			throw IllegalArgumentException("Uninstantiatable anomaly subtile.")
		}
		
	}
	
	fun getAnomaly(name: String): Class<out SubTileEntity> =
		anomalies[name] ?: FallbackAnomaly::class.java
	
	fun getAnomalyInstance(name: String) =
		anomalyInstances[name] ?: FallbackAnomaly
	
	object FallbackAnomaly: SubTileEntity() {
		override val targets: List<Any> = emptyList()
		override val rarity = EnumAnomalityRarity.COMMON
		override val strip = 31
		override fun performEffect(target: Any) = Unit
		override fun typeBits() = 0
	}
}
