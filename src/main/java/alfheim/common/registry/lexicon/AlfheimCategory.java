package alfheim.common.registry.lexicon;

import alfheim.common.registry.AlfheimBlocks;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.lexicon.BLexiconCategory;
import vazkii.botania.common.lexicon.BLexiconEntry;
import vazkii.botania.common.lexicon.page.PageText;

public class AlfheimCategory {
	
	public static LexiconCategory categoryAlfheim;
	public static LexiconCategory categoryElvenStory;

	/** Lore alfheim page */
	public static LexiconEntry alfheim;
	/** Lore elvenstory page */
	public static LexiconEntry elvenstory;
	
	// Main addon content
	public static LexiconEntry portal;		// Lore, recipe, how to build, Interdimensional Gateway Core
	public static LexiconEntry worldgen;	// New trees, structures, glowstone recipe, elven sand and living cobble, livingrock pickaxe
	public static LexiconEntry ores;		// Oregen
	public static LexiconEntry mobs;		// pixies and elves
	public static LexiconEntry infuser;		// Infuser, Mana Infusion Core
	public static LexiconEntry elvorium;	// Better terrasteel, How to create
	public static LexiconEntry essences;	// Infusing Gaia with elements
	public static LexiconEntry runes;		// New types of runes
	public static LexiconEntry mauftrium;	// Metall of Gods
	public static LexiconEntry elvenSet;	// Improved TerraSet
	public static LexiconEntry elementalSet;// Improved Elementium with abilities
	public static LexiconEntry advancedMana;// Managems and Manarings
	public static LexiconEntry rulingPower;	// Use power of other worlds (new pendants and rods)
	public static LexiconEntry realitySword;// Control all the elements

	public static LexiconEntry races;		// All about races and wings
	
	public static void init() {
		BotaniaAPI.addCategory(categoryAlfheim = new BLexiconCategory("Alfheim", 5));
		BotaniaAPI.addCategory(categoryElvenStory = new BLexiconCategory("ElvenStory", 5));
		
		alfheim = new BLexiconEntry("alfheim", categoryAlfheim);
		alfheim.setPriority()
				.setLexiconPages(new PageText("0"))
				.setIcon(new ItemStack(AlfheimBlocks.alfheimPortal));
	}
}
