package alfheim.common.lexicon;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.Lists;

import alfheim.AlfheimCore;
import alfheim.api.AlfheimAPI;
import alfheim.api.spell.SpellBase;
import alfheim.common.block.AlfheimMultiblocks;
import alfheim.common.block.BlockElvenOres;
import alfheim.common.core.handler.CardinalSystem.KnowledgeSystem.Knowledge;
import alfheim.common.core.registry.AlfheimAchievements;
import alfheim.common.core.registry.AlfheimBlocks;
import alfheim.common.core.registry.AlfheimItems;
import alfheim.common.core.registry.AlfheimItems.ElvenResourcesMetas;
import alfheim.common.core.registry.AlfheimRecipes;
import alfheim.common.core.util.AlfheimConfig;
import alfheim.common.lexicon.page.PageManaInfusorRecipe;
import alfheim.common.lexicon.page.PageMultiblockLearnable;
import alfheim.common.lexicon.page.PagePureDaisyRecipe;
import alfheim.common.lexicon.page.PageSpell;
import alfheim.common.lexicon.page.PageTextLearnableAchievement;
import alfheim.common.lexicon.page.PageTextLearnableKnowledge;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import scala.tools.nsc.doc.html.page.diagram.DotDiagramGenerator;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.KnowledgeType;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;
import vazkii.botania.api.lexicon.LexiconRecipeMappings;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.common.block.ModMultiblocks;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lexicon.BLexiconCategory;
import vazkii.botania.common.lexicon.BLexiconEntry;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lexicon.RLexiconEntry;
import vazkii.botania.common.lexicon.page.PageCraftingRecipe;
import vazkii.botania.common.lexicon.page.PageElvenRecipe;
import vazkii.botania.common.lexicon.page.PageMultiblock;
import vazkii.botania.common.lexicon.page.PageRuneRecipe;
import vazkii.botania.common.lexicon.page.PageText;

public class AlfheimLexiconData {
	
	public static KnowledgeType kt = (AlfheimCore.enableElvenStory) ? BotaniaAPI.basicKnowledge : BotaniaAPI.elvenKnowledge;
	
	public static LexiconCategory categoryAlfheim;

	/** Lore alfheim page */
	public static LexiconEntry alfheim;
	/** Lore elves page */
	public static LexiconEntry elves;
	
	// Main addon content

	public static LexiconEntry advMana;
	public static LexiconEntry amulCirs;
	public static LexiconEntry amulNimb;
	public static LexiconEntry aniTorch;
	public static LexiconEntry anyavil;
	public static LexiconEntry astrolab;
	public static LexiconEntry auraAlf;
	public static LexiconEntry cloakInv;
	public static LexiconEntry dodgRing;
	public static LexiconEntry elemSet;
	public static LexiconEntry elvenSet;
	public static LexiconEntry elvorium;
	public static LexiconEntry essences;
	public static LexiconEntry excalibr;
	public static LexiconEntry flugel;
	public static LexiconEntry greenRod;
	public static LexiconEntry infuser;
	public static LexiconEntry itemHold;
	public static LexiconEntry mask;
	//public static LexiconEntry mjolnir;
	public static LexiconEntry mobs;
	public static LexiconEntry ores;
	public static LexiconEntry pixie;
	public static LexiconEntry portal;
	public static LexiconEntry pylons;
	public static LexiconEntry reality;
	public static LexiconEntry ruling;
	public static LexiconEntry runes;
	public static LexiconEntry soul;
	public static LexiconEntry trade;
	//public static LexiconEntry trans;		// BACK
	public static LexiconEntry worldgen;

	// Elven Story information
	public static LexiconEntry es;
	public static LexiconEntry races;
	
	// MMO info
	public static LexiconEntry parties;
	public static LexiconEntry spells;
	public static LexiconEntry targets;
	
	public static void preInit() {
		BotaniaAPI.addCategory(categoryAlfheim = new BLexiconCategory("Alfheim", 5));

		advMana	= new BLexiconEntry("advMana",	categoryAlfheim);
		alfheim	= new BLexiconEntry("alfheim",	categoryAlfheim);
		amulCirs= new BLexiconEntry("amulCirs",	categoryAlfheim);
		amulNimb= new BLexiconEntry("amulNimb",	categoryAlfheim);
		aniTorch= new BLexiconEntry("aniTorch",	categoryAlfheim);
		anyavil	= new BLexiconEntry("anyavil",	categoryAlfheim);
		astrolab= new BLexiconEntry("astrolab",	categoryAlfheim);
		auraAlf = new BLexiconEntry("auraAlf",	categoryAlfheim);
		cloakInv= new BLexiconEntry("cloakInv",	categoryAlfheim);
		dodgRing= new BLexiconEntry("dodgRing",	categoryAlfheim);
		elemSet	= new BLexiconEntry("elemSet",	categoryAlfheim);
		elvenSet= new BLexiconEntry("elvenSet",	categoryAlfheim);
		elves	= new BLexiconEntry("elves",	categoryAlfheim);
		elvorium= new BLexiconEntry("elvorium",	categoryAlfheim);
		essences= new BLexiconEntry("essences",	categoryAlfheim);
		flugel	= new BLexiconEntry("flugel",	categoryAlfheim);
		greenRod= new BLexiconEntry("greenRod",	categoryAlfheim);
		infuser	= new BLexiconEntry("infuser",	categoryAlfheim);
		itemHold= new BLexiconEntry("itemHold",	categoryAlfheim);
		mobs	= new BLexiconEntry("mobs",		categoryAlfheim);
		ores	= new BLexiconEntry("ores",		categoryAlfheim);
		pixie	= new BLexiconEntry("pixie", 	categoryAlfheim);
		portal	= new BLexiconEntry("portal",	categoryAlfheim);
		pylons	= new BLexiconEntry("pylons",	categoryAlfheim);
		reality	= new BLexiconEntry("reality",	categoryAlfheim);
		ruling	= new BLexiconEntry("ruling",	categoryAlfheim);
		runes	= new BLexiconEntry("runes",	categoryAlfheim);
		trade	= new BLexiconEntry("trade",	categoryAlfheim);
		//trans	= new BLexiconEntry("trans",	categoryAlfheim); BACK
		worldgen= new BLexiconEntry("worldgen",	categoryAlfheim);
		
		if (AlfheimCore.enableElvenStory) preInitElvenStory();
	}
	
	private static void preInitElvenStory() {
		if (es		== null) es		= new BLexiconEntry("es",		categoryAlfheim);
		if (races	== null) races	= new BLexiconEntry("races",	categoryAlfheim);
		
		if (AlfheimCore.enableMMO) preInitMMO();
	}
	
	private static void preInitMMO() {
		if (parties	== null) parties	= new BLexiconEntry("parties",	categoryAlfheim);
		if (spells	== null) spells		= new BLexiconEntry("spells",	categoryAlfheim);
		if (targets	== null) targets	= new BLexiconEntry("targets",	categoryAlfheim);
	}
	
	public static void preInit2() {
		excalibr= new RLexiconEntry("excaliber",categoryAlfheim	, AlfheimAchievements.excaliber);
		mask	= new RLexiconEntry("mask",		categoryAlfheim	, AlfheimAchievements.mask);
		//mjolnir	= new RLexiconEntry("mjolnir",	categoryAlfheim	, AlfheimAchievements.mjolnir);
		soul	= new RLexiconEntry("soul",		categoryAlfheim	, AlfheimAchievements.flugelSoul);
	}
	
	// In progress order
	public static void init() {
		alfheim	.setPriority()
				.setLexiconPages(new PageText("0"), new PageText("1"))
				.setIcon(new ItemStack(AlfheimBlocks.alfheimPortal));
		
		elves	.setPriority()
				.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"), new PageText("3"), new PageText("4"));
		
		pylons	.setPriority()
				.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", AlfheimRecipes.recipeElvenPylon), new PageCraftingRecipe("2", AlfheimRecipes.recipeGaiaPylon))
				.setIcon(new ItemStack(AlfheimBlocks.alfheimPylons, 1, 0));
		
		portal	.setPriority()
				.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"), new PageText("3"),
								 new PageCraftingRecipe("4", AlfheimRecipes.recipeAlfheimPortal),
								 new PageElvenRecipe("5", AlfheimRecipes.recipeInterdimensional),
								 new PageMultiblock("6", AlfheimMultiblocks.portal),
								 new PageText("7"), new PageText(AlfheimConfig.destroyPortal ? "8" : "8s"));
		
		worldgen.setLexiconPages(new PageTextLearnableKnowledge("0", Knowledge.GLOWSTONE),
								 new PagePureDaisyRecipe("1", AlfheimRecipes.recipeDreamwood),
								 //new PageCraftingRecipe("2", AlfheimRecipes.recipeGlowstone),
								 new PageText("3"),
								 new PageCraftingRecipe("4", AlfheimRecipes.recipeLivingcobble),
								 new PageCraftingRecipe("5", AlfheimRecipes.recipeLivingrockPickaxe),
								 new PageCraftingRecipe("6", AlfheimRecipes.recipeFurnace))
				.setIcon(new ItemStack(AlfheimBlocks.dreamLeaves));
		worldgen.addExtraDisplayedRecipe(new ItemStack(AlfheimBlocks.elvenSand));
		worldgen.addExtraDisplayedRecipe(new ItemStack(AlfheimBlocks.dreamLog));
		worldgen.addExtraDisplayedRecipe(new ItemStack(AlfheimBlocks.dreamLeaves));
		worldgen.addExtraDisplayedRecipe(new ItemStack(AlfheimBlocks.dreamSapling));

		aniTorch.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"),
								 new PageCraftingRecipe("3", AlfheimRecipes.recipeAnimatedTorch));
		
		itemHold.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", AlfheimRecipes.recipeItemHolder));
		
		greenRod.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", AlfheimRecipes.recipeGreenRod));
		
		dodgRing.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", AlfheimRecipes.recipeDodgeRing));
		
		cloakInv.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", AlfheimRecipes.recipeInvisibilityCloak));
		
		amulCirs.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", AlfheimRecipes.recipeCloudPendant));
		
		amulNimb.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", AlfheimRecipes.recipeCloudPendantSuper));
		
		astrolab.setLexiconPages(new PageText("0"), new PageText("1"), new PageCraftingRecipe("2", AlfheimRecipes.recipeAstrolabe));
		
		ores	.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"))
				.setIcon(new ItemStack(AlfheimBlocks.elvenOres, 1, 4));
		for (int i = 0; i < ((BlockElvenOres) AlfheimBlocks.elvenOres).names.length; i++) ores.addExtraDisplayedRecipe(new ItemStack(AlfheimBlocks.elvenOres, 1, i));
		LexiconRecipeMappings.map(new ItemStack(AlfheimBlocks.elvenOres, 1, 1), ores, 1);
		LexiconRecipeMappings.map(new ItemStack(AlfheimBlocks.elvenOres, 1, 0), ores, 2);
		LexiconRecipeMappings.map(new ItemStack(AlfheimBlocks.elvenOres, 1, 2), ores, 2);
		LexiconRecipeMappings.map(new ItemStack(AlfheimBlocks.elvenOres, 1, 3), ores, 2);
		LexiconRecipeMappings.map(new ItemStack(AlfheimBlocks.elvenOres, 1, 4), ores, 3);
		LexiconRecipeMappings.map(new ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.IffesalDust), ores, 3);
		
		mobs	.setLexiconPages(new PageText("0"), new PageText("1"))
				.setIcon(new ItemStack(ModItems.manaResource, 1, 8));
		
		pixie	.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", AlfheimRecipes.recipePixieAttractor))
				.setIcon(new ItemStack(AlfheimItems.pixieAttractor));
		
		anyavil	.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", AlfheimRecipes.recipeAnyavil));
		
		infuser	.setLexiconPages(new PageText("0"), new PageText("1"),
								 new PageCraftingRecipe("2", AlfheimRecipes.recipeManaInfusionCore),
								 new PageCraftingRecipe("3", AlfheimRecipes.recipeManaInfuser),
								 new PageText("4"),
								 new PageMultiblockLearnable("5", AlfheimMultiblocks.infuserU, AlfheimMultiblocks.infuser, AlfheimAchievements.infuser))
				.setIcon(new ItemStack(AlfheimBlocks.manaInfuser));
		
		elvorium.setLexiconPages(new PageText("0"),
								 new PageManaInfusorRecipe("1", AlfheimRecipes.recipeElvorium))
				.setIcon(new ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot));
		
		trade	.setLexiconPages(new PageText("0"), new PageText("1"),
								 new PageCraftingRecipe("2", AlfheimRecipes.recipeElvoriumPylon),
								 new PageCraftingRecipe("3", AlfheimRecipes.recipeTradePortal),
								 new PageMultiblock("4", AlfheimMultiblocks.yordin))
				.setIcon(new ItemStack(AlfheimBlocks.tradePortal));
		
		List<IRecipe> powerRecipes = new ArrayList();
		powerRecipes.add(AlfheimRecipes.recipeMuspelheimPowerIngot);
		powerRecipes.add(AlfheimRecipes.recipeNiflheimPowerIngot);
		essences.setLexiconPages(new PageText("0"),
								 new PageTextLearnableAchievement("2", AlfheimAchievements.flugelKill),
								 new PageText("4"), new PageText("5"), new PageCraftingRecipe("6", powerRecipes),
								 new PageText("7"), new PageManaInfusorRecipe("8", AlfheimRecipes.recipeMauftrium))
				.setIcon(new ItemStack(ModItems.manaResource, 1, 5));
		essences.addExtraDisplayedRecipe(new ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.NiflheimPowerIngot));
		essences.addExtraDisplayedRecipe(new ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.NiflheimEssence));
		essences.addExtraDisplayedRecipe(new ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.MuspelheimEssence));
		
		List<RecipeRuneAltar> runeRecipes = new ArrayList();
		runeRecipes.add(AlfheimRecipes.recipeMuspelheimRune);
		runeRecipes.add(AlfheimRecipes.recipeNiflheimRune);
		runes	.setLexiconPages(new PageText("0"), new PageRuneRecipe("1", runeRecipes),
								 new PageText("2"), new PageText("3"), new PageRuneRecipe("4", AlfheimRecipes.recipeRealityRune))
				.setIcon(new ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.PrimalRune));
		runes	.addExtraDisplayedRecipe(new ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.NiflheimRune));
		
		elvenSet.setLexiconPages(new PageText("0"),
								 new PageCraftingRecipe("1", AlfheimRecipes.recipeElvoriumHelmet),
								 new PageCraftingRecipe("2", AlfheimRecipes.recipeElvoriumChestplate),
								 new PageCraftingRecipe("3", AlfheimRecipes.recipeElvoriumLeggings),
								 new PageCraftingRecipe("4", AlfheimRecipes.recipeElvoriumBoots))
				.setIcon(new ItemStack(AlfheimItems.elvoriumHelmet));
		elvenSet.addExtraDisplayedRecipe(new ItemStack(AlfheimItems.elvoriumHelmetRevealing));
		
		elemSet	.setLexiconPages(new PageText("0"),
								 new PageCraftingRecipe("1", AlfheimRecipes.recipeEmentalHelmet),
								 new PageCraftingRecipe("2", AlfheimRecipes.recipeEmentalChestplate),
								 new PageCraftingRecipe("3", AlfheimRecipes.recipeEmentalLeggings),
								 new PageCraftingRecipe("4", AlfheimRecipes.recipeEmentalBoots))
				.setIcon(new ItemStack(AlfheimItems.elementalHelmet));
		elemSet	.addExtraDisplayedRecipe(new ItemStack(AlfheimItems.elementalHelmetRevealing));
		
		List<IRecipe> ringRecipes = new ArrayList();
		ringRecipes.add(AlfheimRecipes.recipeManaRingGod1);
		ringRecipes.add(AlfheimRecipes.recipeManaRingGod2);
		advMana	.setLexiconPages(new PageText("0"), new PageText("1"),
								 new PageManaInfusorRecipe("2", AlfheimRecipes.recipeManaStone),
								 new PageManaInfusorRecipe("3", AlfheimRecipes.recipeManaStoneGreater),
								 new PageText("4"),
								 new PageCraftingRecipe("5", AlfheimRecipes.recipeManaRingElven),
								 new PageCraftingRecipe("6", ringRecipes))
				.setIcon(new ItemStack(AlfheimItems.manaStone));
		
		auraAlf	.setLexiconPages(new PageText("0"),
								 new PageCraftingRecipe("1", AlfheimRecipes.recipeAuraRingElven),
								 new PageCraftingRecipe("2", AlfheimRecipes.recipeAuraRingGod))
				.setIcon(new ItemStack(AlfheimItems.auraRingElven));
		
		List<IRecipe> amuletRecipes = new ArrayList();
		amuletRecipes.add(AlfheimRecipes.recipeMuspelheimPendant);
		amuletRecipes.add(AlfheimRecipes.recipeNiflheimPendant);
		ruling	.setLexiconPages(new PageText("0"), new PageText("1"),
								 new PageCraftingRecipe("2", AlfheimRecipes.recipeMuspelheimRod),
								 new PageCraftingRecipe("3", AlfheimRecipes.recipeNiflheimRod),
								 new PageText("4"), new PageCraftingRecipe("5", amuletRecipes))
				.setIcon(new ItemStack(AlfheimItems.rodFire));
		
		reality	.setLexiconPages(new PageText("0"), new PageText("1"),
								 new PageCraftingRecipe("2", AlfheimRecipes.recipeSword))
				.setIcon(new ItemStack(AlfheimItems.realitySword));
		
		flugel	.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"))
				.setIcon(new ItemStack(ModItems.flightTiara, 1, 1));
		
		soul	.setLexiconPages(new PageText("0"), new PageText("1"), // FIXME add re-soul lore
								 new PageCraftingRecipe("2", AlfheimRecipes.recipeAntiPylon), new PageText("3"),
								 new PageMultiblock("4", AlfheimMultiblocks.soul),
								 new PageText("5"))
				.setIcon(new ItemStack(AlfheimItems.flugelSoul));
		
		mask	.setLexiconPages(new PageText("0"))
				.setIcon(new ItemStack(AlfheimItems.mask));
		
		excalibr.setLexiconPages(new PageTextLearnableAchievement("0", AlfheimAchievements.excaliber))
				.setIcon(new ItemStack(AlfheimItems.excaliber));
		
		/*mjolnir.setLexiconPages(new PageText("0"))
				.setIcon(new ItemStack(AlfheimItems.mjolnir));*/
		
		if (AlfheimCore.enableElvenStory) initElvenStory();
		
		LexiconData.gaiaRitual.pages.clear();
		LexiconData.gaiaRitual.setLexiconPages(new PageText("0"), new PageCraftingRecipe("1", AlfheimRecipes.recipeGaiaPylon),
				new PageMultiblock("2", ModMultiblocks.gaiaRitual), new PageText("3"), new PageText("4"),
				new PageText("5"));
		
		LexiconData.sparks.pages.clear();
		LexiconData.sparks.setLexiconPages(new PageText("0"), new PageText("1"),
				new PageCraftingRecipe("2", AlfheimRecipes.recipesSpark),
				new PageText("3"));
		
		LexiconData.cosmeticBaubles.setLexiconPages(new PageCraftingRecipe("34", AlfheimRecipes.recipeThinkingHand));

		LexiconPage pg = new PageText("botania.page.lens38");
		LexiconData.lenses.pages.add(14, pg);
		pg.onPageAdded(LexiconData.lenses, 14);
		pg = new PageCraftingRecipe("botania.page.lens39", AlfheimRecipes.recipeLensMessenger);
		LexiconData.lenses.pages.add(15, pg);
		pg.onPageAdded(LexiconData.lenses, 15);
		LexiconRecipeMappings.map(new ItemStack(ModItems.lens, 1, 22), LexiconData.lenses, 14);
		
		pg = new PageText("botania.page.elvenLenses11");
		LexiconData.elvenLenses.pages.add(pg);
		pg.onPageAdded(LexiconData.elvenLenses, 11);
		pg = new PageCraftingRecipe("botania.page.elvenLenses12", AlfheimRecipes.recipeLensTripwire);
		LexiconData.elvenLenses.pages.add(pg);
		pg.onPageAdded(LexiconData.elvenLenses, 12);
		LexiconRecipeMappings.map(new ItemStack(ModItems.lens, 1, 23), LexiconData.elvenLenses, 11);
		
		pg = new PageText("botania.page.judgementCloaks1n");
		LexiconData.judgementCloaks.pages.set(1, pg);
		pg.onPageAdded(LexiconData.judgementCloaks, 1);
		pg = new PageCraftingRecipe("botania.page.judgementCloaks4", AlfheimRecipes.recipeBalanceCloak);
		LexiconData.judgementCloaks.pages.add(pg);
		pg.onPageAdded(LexiconData.judgementCloaks, 4);
	}
	
	private static void initElvenStory() {
		es		.setPriority()
				.setLexiconPages(new PageText("0"));
		
		races	.setPriority()
				.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"));
		
		if (AlfheimCore.enableMMO) initMMO();
	}
	
	private static void initMMO() {
		parties	.setPriority()
				.setLexiconPages(new PageText("0"), new PageText("1"), new PageCraftingRecipe("2", AlfheimRecipes.recipePeacePipe),
								 new PageText("3"), new PageCraftingRecipe("4", AlfheimRecipes.recipePaperBreak))
				.setIcon(null);
		
		spells	.setPriority()
				.setLexiconPages(new PageText("0"), new PageText("1"), new PageText("2"), new PageText("3"));
		
		targets	.setPriority()
				.setLexiconPages(new PageText("0"), new PageText("1"));
	}
	
	public static void postInit() {
		if (AlfheimCore.enableMMO) postInitMMO();
	}
	
	private static void postInitMMO() {
		ArrayList<SpellBase> l = Lists.newArrayList(AlfheimAPI.spells);
		l.sort(new Comparator<SpellBase>() {
			@Override
			public int compare(SpellBase s1, SpellBase s2) {
				return s1.name.compareTo(s2.name);
			}
		});
		for (SpellBase spell : l) spells.addPage(new PageSpell(spell));
	}
	
	private static void setKnowledgeTypes() {
		kt();
		
		alfheim	.setKnowledgeType(BotaniaAPI.basicKnowledge);
		elves	.setKnowledgeType(kt);
		pylons	.setKnowledgeType(BotaniaAPI.basicKnowledge);
		portal	.setKnowledgeType(kt);
		worldgen.setKnowledgeType(kt);
		
		aniTorch.setKnowledgeType(BotaniaAPI.basicKnowledge);
		itemHold.setKnowledgeType(BotaniaAPI.basicKnowledge);
		greenRod.setKnowledgeType(BotaniaAPI.basicKnowledge);
		dodgRing.setKnowledgeType(BotaniaAPI.basicKnowledge);
		cloakInv.setKnowledgeType(BotaniaAPI.basicKnowledge);
		amulCirs.setKnowledgeType(BotaniaAPI.basicKnowledge);
		amulNimb.setKnowledgeType(kt);
		astrolab.setKnowledgeType(kt);
		
		ores	.setKnowledgeType(kt);
		mobs	.setKnowledgeType(kt);
		pixie	.setKnowledgeType(kt);
		anyavil	.setKnowledgeType(kt);
		infuser	.setKnowledgeType(kt);
		elvorium.setKnowledgeType(kt);
		trade	.setKnowledgeType(kt);
		essences.setKnowledgeType(kt);
		runes	.setKnowledgeType(kt);
		elvenSet.setKnowledgeType(kt);
		elemSet	.setKnowledgeType(kt);
		advMana	.setKnowledgeType(kt);
		auraAlf	.setKnowledgeType(kt);
		ruling	.setKnowledgeType(kt);
		reality	.setKnowledgeType(kt);
		flugel	.setKnowledgeType(kt);
		soul	.setKnowledgeType(BotaniaAPI.relicKnowledge);
		mask	.setKnowledgeType(BotaniaAPI.relicKnowledge);
		excalibr.setKnowledgeType(BotaniaAPI.relicKnowledge);
		//mjolnir	.setKnowledgeType(BotaniaAPI.relicKnowledge);
		
		if (AlfheimCore.enableElvenStory) {
			es		.setKnowledgeType(BotaniaAPI.basicKnowledge);
			races	.setKnowledgeType(BotaniaAPI.basicKnowledge);
			
			if (AlfheimCore.enableMMO) {
				parties	.setKnowledgeType(BotaniaAPI.basicKnowledge);
				spells	.setKnowledgeType(BotaniaAPI.basicKnowledge);
				targets	.setKnowledgeType(BotaniaAPI.basicKnowledge);
			}
		}
	}
	
	public static void disableESM() {
		setKnowledgeTypes();
		
		removeEntry(es		, categoryAlfheim);
		removeEntry(races	, categoryAlfheim);
	}
	
	public static void reEnableESM() {
		if (AlfheimCore.enableElvenStory) preInitElvenStory();
		if (AlfheimCore.enableMMO) preInitMMO();
		
		if (!categoryAlfheim.entries.contains(es))		BotaniaAPI.addEntry(es		, categoryAlfheim);
		if (!categoryAlfheim.entries.contains(races))	BotaniaAPI.addEntry(races	, categoryAlfheim);
		
		setKnowledgeTypes();
	}
	
	public static void disableMMO() {
		setKnowledgeTypes();
		
		removeEntry(parties	, categoryAlfheim);
		removeEntry(spells	, categoryAlfheim);
		removeEntry(targets	, categoryAlfheim);
	}
	
	public static void reEnableMMO() {
		if (AlfheimCore.enableElvenStory) preInitElvenStory();
		if (AlfheimCore.enableMMO) preInitMMO();
		
		if (!categoryAlfheim.entries.contains(parties))	BotaniaAPI.addEntry(parties	, categoryAlfheim);
		if (!categoryAlfheim.entries.contains(spells))	BotaniaAPI.addEntry(spells	, categoryAlfheim);
		if (!categoryAlfheim.entries.contains(targets))	BotaniaAPI.addEntry(targets	, categoryAlfheim);
		
		setKnowledgeTypes();
	}
	
	private static void removeEntry(LexiconEntry entry, LexiconCategory category) {
		BotaniaAPI.getAllEntries().remove(entry);
		category.entries.remove(entry);
	}
	
	private static void kt() {
		kt = (AlfheimCore.enableElvenStory) ? BotaniaAPI.basicKnowledge : BotaniaAPI.elvenKnowledge;
	}
}