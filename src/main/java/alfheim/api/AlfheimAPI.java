package alfheim.api;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import com.google.common.collect.Lists;

import alexsocol.asjlib.ASJUtilities;
import alfheim.api.crafting.recipe.RecipeManaInfuser;
import alfheim.api.entity.EnumRace;
import alfheim.api.lib.LibResourceLocations;
import alfheim.api.spell.SpellBase;
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
	
	/** List of {@link RecipeElvenTrade} outputs forbidden for re'trading from Alfheim trade portal */
	public static List<ItemStack> forbiddenRetrades = new ArrayList();
	/** List of recipies for mana infuser */
	public static List<RecipeManaInfuser> manaInfuserRecipes = new ArrayList<RecipeManaInfuser>();
	/** List of all pink items with their relative pinkness */
	public static HashMap<ItemStack, Integer> pinkness = new HashMap<ItemStack, Integer>();
	/** List of all spells for all races */
	public static HashSet<SpellBase> spells = new HashSet<SpellBase>();
	/** Map of elven spells associated with their race (affinity), sorted by name */
	public static HashMap<EnumRace, HashSet<SpellBase>> spellMapping = new HashMap<EnumRace, HashSet<SpellBase>>();
	
	public static RecipeManaInfuser addInfuserRecipe(RecipeManaInfuser rec) {
		manaInfuserRecipes.add(rec);
		return rec;
	}
	
	public static RecipeManaInfuser addInfuserRecipe(ItemStack result, int mana, Object... ingredients) {
		RecipeManaInfuser rec = new RecipeManaInfuser(mana, result, ingredients);
		manaInfuserRecipes.add(rec);
		return rec;
	}
	
	public static boolean removeInfusionRecipeByResult(ItemStack result) {
		for (int i = 0; i < manaInfuserRecipes.size(); i++) if (ItemStack.areItemStacksEqual(manaInfuserRecipes.get(i).getOutput(), result)) {
			manaInfuserRecipes.remove(i);
			return true;
		}
		return false;
	}
	
	public static void addForbiddenRetrade(ItemStack output) {
		forbiddenRetrades.add(output);
	}
	
	public static boolean isRetradeForbidden(ItemStack output) {
		for (ItemStack out : forbiddenRetrades) if (ItemStack.areItemStacksEqual(output, out)) return true;
		return false;
	}
	
	public static void addPink(ItemStack pink, int weight) {
		pinkness.put(pink, Integer.valueOf(weight));
	}
	
	public static int getPinkness(ItemStack item) {
		for (int i = 0; i < pinkness.size(); i++) {
			ItemStack pink = ASJUtilities.mapGetKeyId(pinkness, i);
			if (pink.getItem().equals(item.getItem()) && pink.getItemDamage() == item.getItemDamage()) return ASJUtilities.mapGetValueId(pinkness, i);
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
		if (spell.getRace() == EnumRace.HUMAN) throw new IllegalArgumentException("Spell race must be one of the elements");
		if (spell.getRace() == EnumRace.ALV) throw new IllegalArgumentException("This race is currently not supported");
		if (spells.add(spell)) {
			checkGet(spell.getRace()).add(spell);
			LibResourceLocations.add(spell.getName());
		}
		else ASJUtilities.warn("Trying to register spell " + spell.getName() + " twice. Skipping.");
		
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
				return s1.getName().compareTo(s2.getName());
			}
		});
		return l;
	}
	
	public static SpellBase getSpellInstance(String name) {
		for (SpellBase spell : spells) if (spell.getName().equals(name)) return spell;
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
		throw new IllegalArgumentException("Client-server spells desynchronization. Not found ID for " + spell.getName());
	}
}
