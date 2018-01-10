package alfheim.common.lexicon;

import java.util.ArrayList;
import java.util.List;

import alfheim.AlfheimCore;
import alfheim.common.block.BlockElvenOres;
import alfheim.common.block.tile.TileAlfheimPortal;
import alfheim.common.block.tile.TileManaInfuser;
import alfheim.common.core.registry.AlfheimBlocks;
import alfheim.common.core.registry.AlfheimItems;
import alfheim.common.core.registry.AlfheimRecipes;
import alfheim.common.core.registry.AlfheimItems.ElvenResourcesMetas;
import alfheim.common.core.utils.AlfheimConfig;
import alfheim.common.lexicon.page.PageManaInfusorRecipe;
import alfheim.common.lexicon.page.PagePureDaisyRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.KnowledgeType;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconRecipeMappings;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lexicon.BLexiconCategory;
import vazkii.botania.common.lexicon.BLexiconEntry;
import vazkii.botania.common.lexicon.page.PageCraftingRecipe;
import vazkii.botania.common.lexicon.page.PageElvenRecipe;
import vazkii.botania.common.lexicon.page.PageMultiblock;
import vazkii.botania.common.lexicon.page.PageRuneRecipe;
import vazkii.botania.common.lexicon.page.PageText;

public class AlfheimLexiconCategory {
	
	public static final KnowledgeType kt = (AlfheimCore.enableElvenStory) ? BotaniaAPI.basicKnowledge : BotaniaAPI.elvenKnowledge;
	
	public static LexiconCategory categoryAlfheim;

	/** Lore alfheim page */
	public static LexiconEntry alfheim;
	/** Lore elves page */
	public static LexiconEntry elves;
	
	// Main addon content
	public static LexiconEntry portal;
	public static LexiconEntry worldgen;
	public static LexiconEntry ores;
	public static LexiconEntry mobs;
	public static LexiconEntry infuser;
	public static LexiconEntry elvorium;
	public static LexiconEntry essences;
	public static LexiconEntry runes;
	public static LexiconEntry elvenSet;
	public static LexiconEntry elemSet;
	public static LexiconEntry advMana;
	public static LexiconEntry ruling;
	public static LexiconEntry reality;

	// Elven Story information
	public static LexiconEntry es;
	public static LexiconEntry races;
	
	public static void preInit() {
		BotaniaAPI.addCategory(categoryAlfheim = new BLexiconCategory("alfheim", 5));

		alfheim	= new BLexiconEntry("alfheim", categoryAlfheim);
		elves	= new BLexiconEntry("elves", categoryAlfheim);
		portal	= new BLexiconEntry("portal", categoryAlfheim);
		worldgen= new BLexiconEntry("worldgen", categoryAlfheim);
		ores	= new BLexiconEntry("ores", categoryAlfheim);
		mobs	= new BLexiconEntry("mobs", categoryAlfheim);
		infuser	= new BLexiconEntry("infuser", categoryAlfheim);
		elvorium= new BLexiconEntry("elvorium", categoryAlfheim);
		essences= new BLexiconEntry("essences", categoryAlfheim);
		runes	= new BLexiconEntry("runes", categoryAlfheim);
		elvenSet= new BLexiconEntry("elvenSet", categoryAlfheim);
		elemSet	= new BLexiconEntry("elemSet", categoryAlfheim);
		advMana	= new BLexiconEntry("advMana", categoryAlfheim);
		ruling	= new BLexiconEntry("ruling", categoryAlfheim);
		reality	= new BLexiconEntry("reality", categoryAlfheim);
		
		if (AlfheimCore.enableElvenStory) preInitElvenStory();
	}
	
	private static void preInitElvenStory() {
		es		= new BLexiconEntry("es", categoryAlfheim);
		races	= new BLexiconEntry("races", categoryAlfheim);
	}
	
	public static void init() {
		
		alfheim	.setPriority().setKnowledgeType(BotaniaAPI.basicKnowledge)
				.setLexiconPages(new PageText("0"), new PageText("1"))
				.setIcon(new ItemStack(AlfheimBlocks.alfheimPortal));
		
		elves	.setPriority().setKnowledgeType(kt)
				.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"), new PageText("3"), new PageText("4"));
		
		portal	.setPriority().setKnowledgeType(kt)
				.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"), new PageText("3"),
								 new PageCraftingRecipe("4", AlfheimRecipes.recipeAlfheimPortal),
								 new PageElvenRecipe("5", AlfheimRecipes.recipeInterdimensional),
								 new PageMultiblock("6", TileAlfheimPortal.makeMultiblockSet()),
								 new PageText("7"), new PageText(AlfheimConfig.destroyPortal ? "8" : "8s"));
		
		worldgen.setKnowledgeType(kt)
				.setLexiconPages(new PageText("0"),
								 new PagePureDaisyRecipe("1", AlfheimRecipes.recipeDreamwood),
								 new PageCraftingRecipe("2", AlfheimRecipes.recipeGlowstone),
								 new PageText("3"),
								 new PageCraftingRecipe("4", AlfheimRecipes.recipeLivingcobble),
								 new PageCraftingRecipe("5", AlfheimRecipes.recipeLivingrockPickaxe),
								 new PageCraftingRecipe("6", AlfheimRecipes.recipeFurnace))
				.setIcon(new ItemStack(AlfheimBlocks.dreamLeaves));
		
		ores	.setKnowledgeType(kt)
				.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"))
				.setIcon(new ItemStack(AlfheimBlocks.elvenOres, 1, 4));
		for (int i = 0; i < ((BlockElvenOres) AlfheimBlocks.elvenOres).names.length; i++) ores.addExtraDisplayedRecipe(new ItemStack(AlfheimBlocks.elvenOres, 1, i));
		LexiconRecipeMappings.map(new ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.IffesalDust), ores, 2);
		
		mobs	.setKnowledgeType(kt)
				.setLexiconPages(new PageText("0"), new PageText("1"))
				.setIcon(new ItemStack(ModItems.manaResource, 1, 8));
		
		infuser	.setKnowledgeType(kt)
				.setLexiconPages(new PageText("0"), new PageText("1"),
								 new PageCraftingRecipe("2", AlfheimRecipes.recipeManaInfusionCore),
								 new PageCraftingRecipe("3", AlfheimRecipes.recipeManaInfuser),
								 new PageText("4"),
								 new PageMultiblock("5", TileManaInfuser.makeMultiblockSet()))
				.setIcon(new ItemStack(AlfheimBlocks.manaInfuser));
		
		elvorium.setKnowledgeType(kt)
				.setLexiconPages(new PageText("0"),
								 new PageManaInfusorRecipe("1", AlfheimRecipes.recipeElvorium))
				.setIcon(new ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot));
		LexiconRecipeMappings.map(new ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.ElvoriumNugget), elvorium, 1);
		LexiconRecipeMappings.map(new ItemStack(AlfheimBlocks.elvoriumBlock), elvorium, 1);
		
		List<IRecipe> powerRecipes = new ArrayList();
		powerRecipes.add(AlfheimRecipes.recipeMuspelheimPowerIngot);
		powerRecipes.add(AlfheimRecipes.recipeNiflheimPowerIngot);
		essences.setKnowledgeType(kt)
				.setLexiconPages(new PageText("0"), new PageManaInfusorRecipe("1d", AlfheimRecipes.recipeMuspelheimEssence),	// TODO replace this when there will be boss
								 new PageText("2"), new PageManaInfusorRecipe("3d", AlfheimRecipes.recipeNiflheimEssence),		// TODO replace this when there will be boss
								 new PageText("4"), new PageText("5"), new PageCraftingRecipe("6", powerRecipes),
								 new PageText("7"), new PageManaInfusorRecipe("8", AlfheimRecipes.recipeMauftrium))
				.setIcon(new ItemStack(ModItems.manaResource, 1, 5));
		essences.addExtraDisplayedRecipe(new ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.NiflheimPowerIngot));
		LexiconRecipeMappings.map(new ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.MauftriumNugget), essences, 1);
		LexiconRecipeMappings.map(new ItemStack(AlfheimBlocks.mauftriumBlock), essences, 1);
		
		List<RecipeRuneAltar> runeRecipes = new ArrayList();
		runeRecipes.add(AlfheimRecipes.recipeMuspelheimRune);
		runeRecipes.add(AlfheimRecipes.recipeNiflheimRune);
		runes	.setKnowledgeType(kt)
				.setLexiconPages(new PageText("0"), new PageRuneRecipe("1", runeRecipes),
								 new PageText("2"), new PageText("3"), new PageRuneRecipe("4", AlfheimRecipes.recipeRealityRune))
				.setIcon(new ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.PrimalRune));
		runes.addExtraDisplayedRecipe(new ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.NiflheimRune));
		
		elvenSet.setKnowledgeType(kt)
				.setLexiconPages(new PageText("0"),
								 new PageCraftingRecipe("1", AlfheimRecipes.recipeElvoriumHelmet),
								 new PageCraftingRecipe("2", AlfheimRecipes.recipeElvoriumChestplate),
								 new PageCraftingRecipe("3", AlfheimRecipes.recipeElvoriumLeggings),
								 new PageCraftingRecipe("4", AlfheimRecipes.recipeElvoriumBoots))
				.setIcon(new ItemStack(AlfheimItems.elvoriumHelmet));
		
		elemSet	.setKnowledgeType(kt)
				.setLexiconPages(new PageText("0"),
								 new PageCraftingRecipe("1", AlfheimRecipes.recipeEmentalHelmet),
								 new PageCraftingRecipe("2", AlfheimRecipes.recipeEmentalChestplate),
								 new PageCraftingRecipe("3", AlfheimRecipes.recipeEmentalLeggings),
								 new PageCraftingRecipe("4", AlfheimRecipes.recipeEmentalBoots))
				.setIcon(new ItemStack(AlfheimItems.elementalHelmet));
		
		List<IRecipe> ringRecipes = new ArrayList();
		ringRecipes.add(AlfheimRecipes.recipeManaElvenRingGreater1);
		ringRecipes.add(AlfheimRecipes.recipeManaElvenRingGreater2);
		advMana	.setKnowledgeType(kt)
				.setLexiconPages(new PageText("0"), new PageText("1"),
								 new PageManaInfusorRecipe("2", AlfheimRecipes.recipeManaStone),
								 new PageManaInfusorRecipe("3", AlfheimRecipes.recipeManaStoneGreater),
								 new PageText("4"),
								 new PageCraftingRecipe("5", AlfheimRecipes.recipeManaElvenRing),
								 new PageCraftingRecipe("6", ringRecipes))
				.setIcon(new ItemStack(AlfheimItems.manaStone));
		
		List<IRecipe> rodRecipes = new ArrayList();
		rodRecipes.add(AlfheimRecipes.recipeMuspelheimRod);
		rodRecipes.add(AlfheimRecipes.recipeNiflheimRod);
		List<IRecipe> amuletRecipes = new ArrayList();
		amuletRecipes.add(AlfheimRecipes.recipeMuspelheimPendant);
		amuletRecipes.add(AlfheimRecipes.recipeNiflheimPendant);
		ruling	.setKnowledgeType(kt)
				.setLexiconPages(new PageText("0"), new PageText("1"),
								 new PageCraftingRecipe("2", rodRecipes),
								 new PageText("3"), new PageCraftingRecipe("4", amuletRecipes))
				.setIcon(new ItemStack(AlfheimItems.rodFire));
		
		reality	.setKnowledgeType(kt)
				.setLexiconPages(new PageText("0"), new PageText("1"),
								 new PageCraftingRecipe("2", AlfheimRecipes.recipeSword))
				.setIcon(new ItemStack(AlfheimItems.realitySword));
		
		if (AlfheimCore.enableElvenStory) initElvenStory();
	}
	
	private static void initElvenStory() {
		es		.setKnowledgeType(BotaniaAPI.basicKnowledge).setPriority()
				.setLexiconPages(new PageText("0"));
		
		races	.setKnowledgeType(BotaniaAPI.basicKnowledge).setPriority()
				.setLexiconPages(new PageText("0"), new PageText("1"));
	}
}