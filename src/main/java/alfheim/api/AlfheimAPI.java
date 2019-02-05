package alfheim.api;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.logging.log4j.Level;

import com.google.common.collect.Lists;

import alfheim.api.block.tile.SubTileEntity;
import alfheim.api.crafting.recipe.RecipeManaInfuser;
import alfheim.api.entity.EnumRace;
import alfheim.api.lib.LibResourceLocations;
import alfheim.api.spell.SpellBase;
import cpw.mods.fml.relauncher.FMLRelaunchLog;
import net.minecraft.entity.ai.attributes.BaseAttribute;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;
import vazkii.botania.api.recipe.RecipeElvenTrade;

public class AlfheimAPI {
	public static final ArmorMaterial ELVORIUM = EnumHelper.addArmorMaterial("ELVORIUM", 50, new int[] {5, 10, 8, 5}, 30);
	public static final ArmorMaterial ELEMENTAL = EnumHelper.addArmorMaterial("ELEMENTAL", 20, new int[] {2, 9, 5, 2}, 20);
	public static final ToolMaterial REALITY = EnumHelper.addToolMaterial("REALITY", 10, 9000, 3, 8, 30);
	
	public static final IAttribute RACE = new BaseAttribute(ModInfo.MODID.toUpperCase() + ":RACE", 0) {
		@Override
		public double clampValue(double d) {
			return d;
		}
	}.setShouldWatch(true);
	
	/** List of {@link RecipeElvenTrade} outputs banned for re'trading in Alfheim trade portal */
	public static ArrayList<ItemStack> bannedRetrades = new ArrayList();
	/** List of recipies for mana infuser */
	public static ArrayList<RecipeManaInfuser> manaInfuserRecipes = new ArrayList<RecipeManaInfuser>();
	/** List of all pink items with their relative pinkness */
	public static HashMap<ItemStack, Integer> pinkness = new HashMap<ItemStack, Integer>();
	/** List of all spells for all races */
	public static HashSet<SpellBase> spells = new HashSet<SpellBase>();
	/** Map of elven spells associated with their race (affinity), sorted by name */
	public static HashMap<EnumRace, HashSet<SpellBase>> spellMapping = new HashMap<EnumRace, HashSet<SpellBase>>();
	/** Map of anomaly types and their subtiles, specifying their behavior */
	public static HashMap<String, Class<? extends SubTileEntity>> anomalies = new HashMap<String, Class<? extends SubTileEntity>>();
	
	public static RecipeManaInfuser addInfuserRecipe(RecipeManaInfuser rec) {
		if (rec != null) manaInfuserRecipes.add(rec);
		return rec;
	}
	
	public static RecipeManaInfuser addInfuserRecipe(ItemStack result, int mana, Object... ingredients) {
		RecipeManaInfuser rec = new RecipeManaInfuser(mana, result, ingredients);
		manaInfuserRecipes.add(rec);
		return rec;
	}
	
	public static RecipeManaInfuser removeInfusionRecipe(RecipeManaInfuser rec) {
		return rec != null && manaInfuserRecipes.remove(rec) ? rec : null;
	}
	
	public static RecipeManaInfuser removeInfusionRecipe(ItemStack result) {
		for (int i = 0; i < manaInfuserRecipes.size(); i++)
			if (ItemStack.areItemStacksEqual(manaInfuserRecipes.get(i).getOutput(), result)) 
				return manaInfuserRecipes.remove(i);
		return null;
	}
	
	/** Remove {@code output} from Alfheim trade portal */
	public static void banRetrade(ItemStack output) {
		bannedRetrades.add(output);
	}
	
	/** Check if {@code output} isn't banned to be obtained through Alfheim trade portal */
	public static boolean isRetradeable(ItemStack output) {
		for (ItemStack out : bannedRetrades) if (ItemStack.areItemStacksEqual(output, out)) return false;
		return true;
	}
	
	/** Map a stack to it's pinkness. Also can override old values */
	public static Integer addPink(ItemStack pink, int weight) {
		return pinkness.put(pink, Integer.valueOf(weight));
	}
	
	public static int getPinkness(ItemStack item) {
		for (ItemStack pink : pinkness.keySet())
			if (pink.getItem() == item.getItem() && pink.getItemDamage() == item.getItemDamage()) {
				return pinkness.get(pink);
			}
		return 0;
	}
	
	/**
	 * Registers spell for some race by affinity
	 * @param spell.getRace() Which race this spell suits best
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
	public static void registerSpell(SpellBase spell) {
		if (spell.race == EnumRace.HUMAN) throw new IllegalArgumentException("Spell race must be one of the elements");
		if (spell.race == EnumRace.ALV) throw new IllegalArgumentException("This race is currently not supported");
		if (spells.add(spell)) {
			checkGet(spell.race).add(spell);
			LibResourceLocations.add(spell.name);
		}
		else FMLRelaunchLog.log(ModInfo.MODID.toUpperCase(), Level.WARN, "Trying to register spell " + spell.name + " twice. Skipping.");
		
	}

	private static HashSet<SpellBase> checkGet(EnumRace affinity) {
		if (!spellMapping.containsKey(affinity)) spellMapping.put(affinity, new HashSet<SpellBase>(8));
		return spellMapping.get(affinity);
	}
	
	public static ArrayList<SpellBase> getSpellsFor(EnumRace affinity) {
		ArrayList<SpellBase> l = Lists.newArrayList(checkGet(affinity));
		l.sort(new Comparator<SpellBase>() {
			@Override
			public int compare(SpellBase s1, SpellBase s2) {
				return s1.name.compareTo(s2.name);
			}
		});
		return l;
	}
	
	public static SpellBase getSpellInstance(String name) {
		for (SpellBase spell : spells) if (spell.name.equals(name)) return spell;
		return null;
	}
	
	public static SpellBase getSpellByIDs(int raceID, int spellID) {
		int i = 0;
		for (SpellBase sb : getSpellsFor(EnumRace.getByID(raceID))) if (i++ == spellID) return sb; 
		return null;
	}
	
	public static int getSpellID(SpellBase spell) {
		int i;
		for(EnumRace race : EnumRace.values()) {
			i = -1;
			for (SpellBase sb : getSpellsFor(race)) {
				++i;
				if (sb == spell) return i; 
			}
		}
		throw new IllegalArgumentException("Client-server spells desynchronization. Not found ID for " + spell.name);
	}
	
	public static void registerAnomaly(String name, Class<? extends SubTileEntity> behavior) {
		anomalies.put(name, behavior);
	}
	
	public static Class<? extends SubTileEntity> getAnomaly(String name) {
		return anomalies.get(name);
	}
}
