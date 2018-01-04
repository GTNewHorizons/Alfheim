package alfheim.common.registry.lexicon;

import java.util.ArrayList;
import java.util.List;

import alfheim.AlfheimCore;
import alfheim.Constants;
import alfheim.common.blocks.tileentity.AlfheimPortalTileEntity;
import alfheim.common.blocks.tileentity.ManaInfuserTileEntity;
import alfheim.common.crafting.PageManaInfusorRecipe;
import alfheim.common.crafting.PagePureDaisyRecipe;
import alfheim.common.registry.AlfheimBlocks;
import alfheim.common.registry.AlfheimItems;
import alfheim.common.registry.AlfheimItems.ElvenResourcesMetas;
import alfheim.common.registry.AlfheimRecipes;
import alfheim.common.registry.AlfheimRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.KnowledgeType;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lexicon.BLexiconCategory;
import vazkii.botania.common.lexicon.BLexiconEntry;
import vazkii.botania.common.lexicon.page.PageCraftingRecipe;
import vazkii.botania.common.lexicon.page.PageElvenRecipe;
import vazkii.botania.common.lexicon.page.PageMultiblock;
import vazkii.botania.common.lexicon.page.PageRuneRecipe;
import vazkii.botania.common.lexicon.page.PageText;

public class AlfheimCategory {
	
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
	public static LexiconEntry elvenSet;
	public static LexiconEntry elemSet;
	public static LexiconEntry advMana;
	public static LexiconEntry ruling;
	public static LexiconEntry reality;

	// Elven Story information
	public static LexiconEntry es;
	public static LexiconEntry races;
	
	public static void init() {
		BotaniaAPI.addCategory(categoryAlfheim = new BLexiconCategory("alfheim", 5));
		
		alfheim	= new BLexiconEntry("alfheim", categoryAlfheim);
		alfheim	.setPriority().setKnowledgeType(kt)
				.setLexiconPages(new PageText("0"), new PageText("1"))
				.setIcon(new ItemStack(AlfheimBlocks.alfheimPortal));
		
		elves	= new BLexiconEntry("elves", categoryAlfheim);
		elves	.setPriority().setKnowledgeType(kt)
				.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"), new PageText("3"), new PageText("4"));
		
		portal	= new BLexiconEntry("portal", categoryAlfheim);
		portal	.setPriority().setKnowledgeType(kt)
				.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"), new PageText("3"),
								 new PageCraftingRecipe("4", AlfheimRecipes.recipeAlfheimPortal),
								 new PageElvenRecipe("5", AlfheimRecipes.recipeInterdimensional),
								 new PageMultiblock("6", AlfheimPortalTileEntity.makeMultiblockSet()),
								 new PageText("7"), new PageText("8"));
		
		worldgen= new BLexiconEntry("worldgen", categoryAlfheim);
		worldgen.setKnowledgeType(kt)
				.setLexiconPages(new PageText("0"),
								 new PagePureDaisyRecipe("1", AlfheimRecipes.recipeDreamwood),
								 new PageCraftingRecipe("2", AlfheimRecipes.recipeGlowstone),
								 new PageText("3"),
								 new PageCraftingRecipe("4", AlfheimRecipes.recipeLivingcobble),
								 new PageCraftingRecipe("5", AlfheimRecipes.recipeLivingrockPickaxe),
								 new PageCraftingRecipe("6", AlfheimRecipes.recipeFurnace))
				.setIcon(new ItemStack(AlfheimBlocks.dreamLeaves));
		
		ores	= new BLexiconEntry("ores", categoryAlfheim);
		ores	.setKnowledgeType(kt)
				.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"))
				.setIcon(new ItemStack(AlfheimBlocks.elvenOres, 1, 4));
		
		mobs	= new BLexiconEntry("mobs", categoryAlfheim);
		mobs	.setKnowledgeType(kt)
				.setLexiconPages(new PageText("0"), new PageText("1"))
				.setIcon(new ItemStack(ModItems.manaResource, 1, 8));
		
		infuser	= new BLexiconEntry("infuser", categoryAlfheim);
		infuser	.setKnowledgeType(kt)
				.setLexiconPages(new PageText("0"), new PageText("1"),
								 new PageCraftingRecipe("2", AlfheimRecipes.recipeManaInfusionCore),
								 new PageCraftingRecipe("3", AlfheimRecipes.recipeManaInfuser),
								 new PageText("4"),
								 new PageMultiblock("5", ManaInfuserTileEntity.makeMultiblockSet()))
				.setIcon(new ItemStack(AlfheimBlocks.manaInfuser));
		
		elvorium= new BLexiconEntry("elvorium", categoryAlfheim);
		elvorium.setKnowledgeType(kt)
				.setLexiconPages(new PageText("0"),
								 new PageManaInfusorRecipe("1", AlfheimRecipes.recipeElvorium))
				.setIcon(new ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot));
		
		essences= new BLexiconEntry("essences", categoryAlfheim);
		List<IRecipe> powerRecipes = new ArrayList();
		powerRecipes.add(AlfheimRecipes.recipeMuspelheimPowerIngot);
		powerRecipes.add(AlfheimRecipes.recipeNiflheimPowerIngot);
		List<RecipeRuneAltar> runeRecipes = new ArrayList();
		runeRecipes.add(AlfheimRecipes.recipeMuspelheimRune);
		runeRecipes.add(AlfheimRecipes.recipeNiflheimRune);
		essences.setKnowledgeType(kt)
				.setLexiconPages(new PageText("0"), new PageManaInfusorRecipe("1d", AlfheimRecipes.recipeMuspelheimEssence),	// TODO replace this when there will be boss
								 new PageText("2"), new PageManaInfusorRecipe("3d", AlfheimRecipes.recipeNiflheimEssence),		// TODO replace this when there will be boss
								 new PageText("4"), new PageText("5"), new PageCraftingRecipe("6", powerRecipes),
								 new PageText("7"), new PageManaInfusorRecipe("8", AlfheimRecipes.recipeMauftrium),
								 new PageText("9"), new PageRuneRecipe("10", runeRecipes),
								 new PageText("11"), new PageText("12"), new PageRuneRecipe("13", AlfheimRecipes.recipeRealityRune))
				.setIcon(new ItemStack(ModItems.manaResource, 1, 5));
		
		elvenSet= new BLexiconEntry("elvenSet", categoryAlfheim);
		elvenSet.setKnowledgeType(kt)
				.setLexiconPages(new PageText("0"),
								 new PageCraftingRecipe("1", AlfheimRecipes.recipeElvoriumHelmet),
								 new PageCraftingRecipe("2", AlfheimRecipes.recipeElvoriumChestplate),
								 new PageCraftingRecipe("3", AlfheimRecipes.recipeElvoriumLeggings),
								 new PageCraftingRecipe("4", AlfheimRecipes.recipeElvoriumBoots))
				.setIcon(new ItemStack(AlfheimItems.elvoriumHelmet));
		
		elemSet	= new BLexiconEntry("elemSet", categoryAlfheim);
		elemSet	.setKnowledgeType(kt)
				.setLexiconPages(new PageText("0"),
								 new PageCraftingRecipe("1", AlfheimRecipes.recipeEmentalHelmet),
								 new PageCraftingRecipe("2", AlfheimRecipes.recipeEmentalChestplate),
								 new PageCraftingRecipe("3", AlfheimRecipes.recipeEmentalLeggings),
								 new PageCraftingRecipe("4", AlfheimRecipes.recipeEmentalBoots))
				.setIcon(new ItemStack(AlfheimItems.elementalHelmet));
		
		advMana	= new BLexiconEntry("advMana", categoryAlfheim);
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
		
		ruling	= new BLexiconEntry("ruling", categoryAlfheim);
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
				.setIcon(new ItemStack(AlfheimItems.rod));
		
		reality	= new BLexiconEntry("reality", categoryAlfheim);
		reality	.setKnowledgeType(kt)
				.setLexiconPages(new PageText("0"), new PageText("1"),
								 new PageCraftingRecipe("2", AlfheimRecipes.recipeSword))
				.setIcon(new ItemStack(AlfheimItems.realitySword));
		
		if (AlfheimCore.enableElvenStory) addElvenStory();
	}
	
	private static void addElvenStory() {
		Constants.log("Registering ES Pages!");
		es		= new BLexiconEntry("es", categoryAlfheim);
		es		.setKnowledgeType(BotaniaAPI.basicKnowledge).setPriority()
				.setLexiconPages(new PageText("0"));
		
		races	= new BLexiconEntry("races", categoryAlfheim);
		races	.setKnowledgeType(BotaniaAPI.basicKnowledge).setPriority()
				.setLexiconPages(new PageText("0"), new PageText("1"));
	}
}