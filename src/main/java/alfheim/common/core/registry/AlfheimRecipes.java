package alfheim.common.core.registry;

import static alexsocol.asjlib.ASJUtilities.*;
import static alfheim.api.AlfheimAPI.*;
import static alfheim.api.lib.LibOreDict.*;
import static alfheim.common.core.registry.AlfheimBlocks.*;
import static alfheim.common.core.registry.AlfheimItems.*;
import static cpw.mods.fml.common.registry.GameRegistry.*;
import static net.minecraft.init.Blocks.*;
import static net.minecraft.init.Items.*;
import static vazkii.botania.common.block.ModBlocks.*;
import static vazkii.botania.common.item.ModItems.*;
import static vazkii.botania.common.lib.LibOreDict.*;

import java.util.List;

import alfheim.AlfheimCore;
import alfheim.api.AlfheimAPI;
import alfheim.api.crafting.recipe.RecipeManaInfuser;
import alfheim.common.core.registry.AlfheimItems.ElvenResourcesMetas;
import alfheim.common.crafting.recipe.HelmRevealingAlfheimRecipe;
import alfheim.common.crafting.recipe.LootInterceptorClearRecipe;
import alfheim.common.crafting.recipe.LootInterceptorRecipe;
import alfheim.common.item.equipment.tool.ItemTwigWandExtender;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.api.recipe.RecipePureDaisy;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.crafting.ModCraftingRecipes;
import vazkii.botania.common.crafting.ModRuneRecipes;
import vazkii.botania.common.item.ModItems;

public class AlfheimRecipes {
	
	public static RecipeManaInfuser recipeElvorium;
	//public static RecipeManaInfuser recipeMuspelheimEssence;
	//public static RecipeManaInfuser recipeNiflheimEssence;
	public static RecipeManaInfuser recipeTerrasteel;
	public static RecipeManaInfuser recipeMauftrium;
	public static RecipeManaInfuser recipeManaStone;
	public static RecipeManaInfuser recipeManaStoneGreater;
	
	public static IRecipe recipeAlfheimPortal;
	public static IRecipe recipeAnimatedTorch;
	public static IRecipe recipeAnyavil;
	public static List<IRecipe> recipesApothecary;
	public static IRecipe recipeAstrolabe;
	public static IRecipe recipeAuraRingElven;
	public static IRecipe recipeAuraRingGod;
	public static IRecipe recipeBalanceCloak;
	public static IRecipe recipeCloudPendant;
	public static IRecipe recipeCloudPendantSuper;
	public static IRecipe recipeCrescentAmulet;
	public static IRecipe recipeDodgeRing;
	public static IRecipe recipeEmentalHelmet;
	public static IRecipe recipeEmentalChestplate;
	public static IRecipe recipeEmentalLeggings;
	public static IRecipe recipeEmentalBoots;
	public static IRecipe recipeEmentiumHoe;
	public static IRecipe recipeElvenPylon;
	private static List<IRecipe> recipesElvenWand;
	public static IRecipe recipeElvoriumHelmet;
	public static IRecipe recipeElvoriumChestplate;
	public static IRecipe recipeElvoriumLeggings;
	public static IRecipe recipeElvoriumBoots;
	public static IRecipe recipeElvoriumPylon;
	public static IRecipe recipeFurnace;
	public static IRecipe recipeGaiaPylon;
	public static IRecipe recipeGreenRod;
	public static IRecipe recipeInvisibilityCloak;
	public static IRecipe recipeItemHolder;
	public static IRecipe recipeLensMessenger;
	public static IRecipe recipeLensTripwire;
	public static IRecipe recipeLivingcobble;
	public static IRecipe recipeLivingrockPickaxe;
	public static IRecipe recipeLootInterceptor;
	public static IRecipe recipeManaInfusionCore;
	public static IRecipe recipeManaInfuser;
	public static IRecipe recipeManaRingElven;
	public static IRecipe recipeManaRingGod;
	public static IRecipe recipeManasteelHoe;
	public static IRecipe recipeMuspelheimPendant;
	public static IRecipe recipeMuspelheimPowerIngot;
	public static IRecipe recipeMuspelheimRod;
	public static IRecipe recipeNiflheimPendant;
	public static IRecipe recipeNiflheimPowerIngot;
	public static IRecipe recipeNiflheimRod;
	public static IRecipe recipePaperBreak;
	public static IRecipe recipePeacePipe;
	public static IRecipe recipePixieAttractor;
	public static List<IRecipe> recipesSpark;
	public static IRecipe recipeSword;
	public static IRecipe recipeThinkingHand;
	public static IRecipe recipeTradePortal;
	
	public static RecipeElvenTrade recipeInterdimensional;
	
	public static RecipePureDaisy recipeDreamwood;
	
	public static RecipeRuneAltar recipeMuspelheimRune;
	public static RecipeRuneAltar recipeNiflheimRune;
	public static RecipeRuneAltar recipeRealityRune;
	
	public static void preInit() {
		registerCraftingRecipes();
		registerShapelessRecipes();
		registerSmeltingRecipes();
		registerManaInfusionRecipes();
		registerRecipies();
		banRetrades();
		//if (ModInfo.DEV && FMLCommonHandler.instance().getEffectiveSide().equals(Side.CLIENT)) (new NEIAlfheimConfig()).loadConfig();
	}
	
	public static void registerCraftingRecipes() {
		addOreDictRecipe(new ItemStack(alfheimPortal, 1),
			"DPD", "GSG", "DTD",
			'D', DREAM_WOOD,
			'G', spark,
			'P', RUNE[8],
			'S', rainbowRod,
			'T', new ItemStack(lens, 1, 18));
		recipeAlfheimPortal = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(alfheimPylon, 1, 0),
			" P ", "EDE", " P ",
			'P', PIXIE_DUST,
			'E', ELEMENTIUM,
			'D', DRAGONSTONE);
		recipeElvenPylon = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(alfheimPylon, 1, 1),
			" E ", "EPE", "III",
			'E', ELVORIUM_NUGGET,
			'P', new ItemStack(alfheimPylon, 1, 0),
			'I', IFFESAL_DUST);
		recipeElvoriumPylon = BotaniaAPI.getLatestAddedRecipe();
		
		for(int i = 0; i < 16; i++)
			addOreDictRecipe(new ItemStack(altar),
				"SPS", " C ", "CCC",
				'S', livingcobble,
				'P', PETAL[i],
				'C', LIVING_ROCK);
		recipesApothecary = BotaniaAPI.getLatestAddedRecipes(16);
		
		addOreDictRecipe(new ItemStack(animatedTorch),
			"P", "T",
			'T', redstone_torch,
			'P', MANA_POWDER);
		recipeAnimatedTorch = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(anyavil),
			"BGB", " P ", "EDE",
			'P', PIXIE_DUST,
			'E', ELEMENTIUM,
			'D', DRAGONSTONE,
			'B', new ItemStack(storage, 1, 2),
			'G', new ItemStack(storage, 1, 4));
		recipeAnyavil = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(astrolabe),
			" ES", "EEE", "SED",
			'E', ELEMENTIUM,
			'S', LIFE_ESSENCE,
			'D', DREAM_WOOD);
		recipeAstrolabe = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(balanceCloak),
			"WWW", "EWE", "ESE",
			'W', new ItemStack(wool, 1, 8),
			'E', "gemEmerald",
			'S', LIFE_ESSENCE);
		recipeBalanceCloak = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(cloudPendant),
			"US ", "S S", "MSA",
			'U', RUNE[6],
			'S', MANA_STRING,
			'M', MANA_STEEL,
			'A', RUNE[3]);
		recipeCloudPendant = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(cloudPendantSuper),
			"GEG", "GPG", "WSW",
			'G', ghast_tear,
			'E', ELEMENTIUM,
			'P', new ItemStack(cloudPendant),
			'W', new ItemStack(wool, 1, 0),
			'S', LIFE_ESSENCE);
		recipeCloudPendantSuper = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(crescentMoonAmulet),
			"  M", "MS ", "RM ",
			'M', MAUFTRIUM_NUGGET,
			'R', RUNE[13],
			'S', new ItemStack(manaResource, 1, 12));
		recipeCrescentAmulet = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(dodgeRing),
			"EM ", "M M", " MR",
			'E', "gemEmerald",
			'M', MANA_STEEL,
			'R', RUNE[3]);
		recipeDodgeRing = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(elementalHelmet),
			"RTR", "DPD", " M ",
			'R', RUNE[0],
			'T', DREAMWOOD_TWIG,
			'D', IFFESAL_DUST,
			'P', elementiumHelm,
			'M', RUNE[8]);
		recipeEmentalHelmet = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(elementalHelmetRevealing),
			"RTR", "DPD", " M ",
			'R', RUNE[0],
			'T', DREAMWOOD_TWIG,
			'D', IFFESAL_DUST,
			'P', elementiumHelmRevealing,
			'M', RUNE[8]);
		
		addOreDictRecipe(new ItemStack(elementalChestplate),
			"RTR", "DPD", " M ",
			'R', RUNE[2],
			'T', DREAMWOOD_TWIG,
			'D', IFFESAL_DUST,
			'P', elementiumChest,
			'M', RUNE[8]);
		recipeEmentalChestplate = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(elementalLeggings),
			"RTR", "DPD", " M ",
			'R', RUNE[1],
			'T', DREAMWOOD_TWIG,
			'D', IFFESAL_DUST,
			'P', elementiumLegs,
			'M', RUNE[8]);
		recipeEmentalLeggings = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(elementalBoots),
			"RTR", "DPD", " M ",
			'R', RUNE[3],
			'T', DREAMWOOD_TWIG,
			'D', IFFESAL_DUST,
			'P', elementiumBoots,
			'M', RUNE[8]);
		recipeEmentalBoots = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(elementiumHoe),
			"EE", " T", " T",
			'E', ELEMENTIUM,
			'T', DREAMWOOD_TWIG);
		recipeEmentiumHoe = BotaniaAPI.getLatestAddedRecipe();
		
		removeRecipe(ModCraftingRecipes.recipeGaiaPylon.getRecipeOutput());
		addOreDictRecipe(new ItemStack(pylon, 1, 2),
				" E ", "TPT", " E ",
				'T', TERRASTEEL_NUGGET,
				'E', overgrowthSeed,
				'P', new ItemStack(alfheimPylon, 1, 0));
		recipeGaiaPylon = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(elfFirePendant),
			"  N", "NP ", "RN ",
			'N', MAUFTRIUM_NUGGET,
			'R', ARUNE[1],
			'P', lavaPendant);
		recipeMuspelheimPendant = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(elfIcePendant),
			"  N", "NP ", "RN ",
			'N', MAUFTRIUM_NUGGET,
			'R', ARUNE[2],
			'P', icePendant);
		recipeNiflheimPendant = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.ManaInfusionCore),
			"PGP", "GDG", "PGP",
			'D', PIXIE_DUST,
			'G', "ingotGold",
			'P', IFFESAL_DUST);
		recipeManaInfusionCore = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimPowerIngot),
			" S ", "SIS", " S ",
			'S', MUSPELHEIM_ESSENCE,
			'I', ELVORIUM_INGOT);
		recipeMuspelheimPowerIngot = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimPowerIngot),
			" S ", "SIS", " S ",
			'S', NIFLHEIM_ESSENCE,
			'I', ELVORIUM_INGOT);
		recipeNiflheimPowerIngot = BotaniaAPI.getLatestAddedRecipe();
		
		for(int i = 0; i < 16; i++)
			for(int j = 0; j < 16; j++) {
				addOreDictRecipe(ItemTwigWandExtender.forColors(i, j, true),
						" AS", " SB", "S  ",
						'A', PETAL[i],
						'B', PETAL[j],
						'S', DREAMWOOD_TWIG);
			}
		recipesElvenWand = BotaniaAPI.getLatestAddedRecipes(256);
		
		addOreDictRecipe(new ItemStack(elvoriumHelmet),
			"TRT", "EPE", "CMC",
			'T', DREAMWOOD_TWIG,
			'R', ARUNE[0],
			'E', ELVORIUM_INGOT,
			'P', terrasteelHelm,
			'C', new ItemStack(elvenResource, 1, ElvenResourcesMetas.ManaInfusionCore),
			'M', MAUFTRIUM_INGOT);
		recipeElvoriumHelmet = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(elvoriumHelmetRevealing),
			"TRT", "EPE", "CMC",
			'T', DREAMWOOD_TWIG,
			'R', ARUNE[0],
			'E', ELVORIUM_INGOT,
			'P', terrasteelHelmRevealing,
			'C', new ItemStack(elvenResource, 1, ElvenResourcesMetas.ManaInfusionCore),
			'M', MAUFTRIUM_INGOT);
		
		addOreDictRecipe(new ItemStack(elvoriumChestplate),
			"TRT", "EPE", "CMC",
			'T', DREAMWOOD_TWIG,
			'R', ARUNE[0],
			'E', ELVORIUM_INGOT,
			'P', terrasteelChest,
			'C', new ItemStack(elvenResource, 1, ElvenResourcesMetas.ManaInfusionCore),
			'M', MAUFTRIUM_INGOT);
		recipeElvoriumChestplate = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(elvoriumLeggings),
			"TRT", "EPE", "CMC",
			'T', DREAMWOOD_TWIG,
			'R', ARUNE[0],
			'E', ELVORIUM_INGOT,
			'P', terrasteelLegs,
			'C', new ItemStack(elvenResource, 1, ElvenResourcesMetas.ManaInfusionCore),
			'M', MAUFTRIUM_INGOT);
		recipeElvoriumLeggings = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(elvoriumBoots),
			"TRT", "EPE", "CMC",
			'T', DREAMWOOD_TWIG,
			'R', ARUNE[0],
			'E', ELVORIUM_INGOT,
			'P', terrasteelBoots,
			'C', new ItemStack(elvenResource, 1, ElvenResourcesMetas.ManaInfusionCore),
			'M', MAUFTRIUM_INGOT);
		recipeElvoriumBoots = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(furnace),
			"SSS", "S S", "SSS",
			'S', livingcobble);
		recipeFurnace = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(invisibilityCloak),
			"PWP", "GWG", "GJG",
			'P', PRISMARINE_SHARD,
			'W', new ItemStack(wool, 1, 0),
			'G', manaGlass,
			'J', MANA_PEARL);
		recipeInvisibilityCloak = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(itemHolder),
			"MLM", "LDL",
			'D', MANA_DIAMOND,
			'L', LIVING_ROCK,
			'M', MANA_PEARL);
		recipeItemHolder = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(lens, 1, 22),
			" P ", "PLP", " P ",
			'P', paper,
			'L', new ItemStack(lens, 1, 0));
		recipeLensMessenger = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(livingrockPickaxe),
			"LLL", " S ", " S ",
			'L', livingcobble,
			'S', "stickWood");
		recipeLivingrockPickaxe = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(lootInterceptor),
			"IHI", "DID",
			'I', IFFESAL_DUST,
			'H', blackHoleTalisman,
			'D', DREAM_WOOD);
		recipeLootInterceptor = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(manaInfuser),
			"DCD", "IRI", "SSS",
			'C', new ItemStack(elvenResource, 1, ElvenResourcesMetas.ManaInfusionCore),
			'D', DRAGONSTONE,
			'I', ELEMENTIUM,
			'R', rainbowRod,
			'S', LIVING_ROCK);
		recipeManaInfuser = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(manaRingElven),
			"IS ", "S S", " S ",
			'S', ELVORIUM_INGOT,
			'I', manaStone);
		recipeManaRingElven = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(manasteelHoe),
			"SS", " T", " T",
			'S', MANA_STEEL,
			'T', LIVINGWOOD_TWIG);
		recipeManasteelHoe = BotaniaAPI.getLatestAddedRecipe();
		
		recipePaperBreak = new
		ShapedOreRecipe(new ItemStack(paperBreak),
			"  P", " P ", "P  ",
			'P', paper);
		
		recipePeacePipe = new
		ShapedOreRecipe(new ItemStack(peacePipe),
			"S  ", " S ", "  S",
			'S', stick);
		
		if (AlfheimCore.enableMMO) addMMORecipes();
			
		addOreDictRecipe(new ItemStack(pixieAttractor),
			"EDE", "EPE", " S ",
			'D', DRAGONSTONE,
			'E', ELEMENTIUM,
			'P', PIXIE_DUST,
			'S', RUNE[2]);
		recipePixieAttractor = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(realitySword),
			" M ", "MRM", " S ",
			'M', MAUFTRIUM_INGOT,
			'R', ARUNE[0],
			'S', new ItemStack(manaResource, 1, 3));
		recipeSword = BotaniaAPI.getLatestAddedRecipe();
			
		addOreDictRecipe(new ItemStack(rodFire),
			" MR", " BM", "B  ",
			'M', MAUFTRIUM_INGOT,
			'R', ARUNE[1],
			'B', blaze_rod);
		recipeMuspelheimRod = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(rodGrass),
			"  R", " D ", "S  ",
			'D', dirtRod,
			'R', RUNE[4],
			'S', grassSeeds);
		recipeGreenRod = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(rodIce),
			" MR", " BM", "B  ",
			'M', MAUFTRIUM_INGOT,
			'R', ARUNE[2],
			'B', blaze_rod);
		recipeNiflheimRod = BotaniaAPI.getLatestAddedRecipe();
		
		for(int i = 0; i < 16; i++)
			addOreDictRecipe(new ItemStack(spark),
				" P ", "BNB", " P ",
				'B', PIXIE_DUST,
				'P', PETAL[i],
				'N', "nuggetGold");
		recipesSpark = BotaniaAPI.getLatestAddedRecipes(16);
		recipesSpark.addAll(ModCraftingRecipes.recipesSpark);
		
		addOreDictRecipe(new ItemStack(thinkingHand),
			"PPP", "PSP", "PPP",
			'P', tinyPotato,
			'S', MANA_STRING);
		recipeThinkingHand = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(tradePortal),
			"LEL", "LEL", "LEL",
			'L', LIVING_ROCK,
			'E', ELVORIUM_NUGGET);
		recipeTradePortal = BotaniaAPI.getLatestAddedRecipe();
	}
	
	public static void registerShapelessRecipes() {
		addShapelessOreDictRecipe(new ItemStack(auraRingElven), ELVORIUM_INGOT, auraRingGreater);
		recipeAuraRingElven = BotaniaAPI.getLatestAddedRecipe();
		
		addShapelessOreDictRecipe(new ItemStack(auraRingGod), MAUFTRIUM_INGOT, auraRingElven);
		recipeAuraRingGod = BotaniaAPI.getLatestAddedRecipe();
		
		if (Botania.thaumcraftLoaded) {
			Item goggles = (Item) Item.itemRegistry.getObject("Thaumcraft:ItemGoggles");
			addShapelessRecipe(new ItemStack(elementalHelmetRevealing), new ItemStack(elementalHelmet), goggles);
			addShapelessRecipe(new ItemStack(elvoriumHelmetRevealing), new ItemStack(elvoriumHelmet), goggles);
		}
		
		addShapelessOreDictRecipe(new ItemStack(elvenResource, 9, ElvenResourcesMetas.ElvoriumNugget), ELVORIUM_INGOT);
		addShapelessOreDictRecipe(new ItemStack(elvenResource, 9, ElvenResourcesMetas.MauftriumNugget), MAUFTRIUM_INGOT);
		addShapelessRecipe(new ItemStack(elvenResource, 9, ElvenResourcesMetas.ElvoriumIngot), elvoriumBlock);
		addShapelessRecipe(new ItemStack(elvenResource, 9, ElvenResourcesMetas.MauftriumIngot), mauftriumBlock);
		
		addShapelessOreDictRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot), ELVORIUM_NUGGET, ELVORIUM_NUGGET, ELVORIUM_NUGGET, ELVORIUM_NUGGET, ELVORIUM_NUGGET, ELVORIUM_NUGGET, ELVORIUM_NUGGET, ELVORIUM_NUGGET, ELVORIUM_NUGGET);
		addShapelessOreDictRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot), MAUFTRIUM_NUGGET, MAUFTRIUM_NUGGET, MAUFTRIUM_NUGGET, MAUFTRIUM_NUGGET, MAUFTRIUM_NUGGET, MAUFTRIUM_NUGGET, MAUFTRIUM_NUGGET, MAUFTRIUM_NUGGET, MAUFTRIUM_NUGGET); 
		addShapelessOreDictRecipe(new ItemStack(elvoriumBlock), ELVORIUM_INGOT, ELVORIUM_INGOT, ELVORIUM_INGOT, ELVORIUM_INGOT, ELVORIUM_INGOT, ELVORIUM_INGOT, ELVORIUM_INGOT, ELVORIUM_INGOT, ELVORIUM_INGOT);
		addShapelessOreDictRecipe(new ItemStack(mauftriumBlock), MAUFTRIUM_INGOT, MAUFTRIUM_INGOT, MAUFTRIUM_INGOT, MAUFTRIUM_INGOT, MAUFTRIUM_INGOT, MAUFTRIUM_INGOT, MAUFTRIUM_INGOT, MAUFTRIUM_INGOT, MAUFTRIUM_INGOT);
		
		addShapelessOreDictRecipe(new ItemStack(lens, 1, 23), new ItemStack(lens, 1, 0), tripwire_hook, ELEMENTIUM);
		recipeLensTripwire = BotaniaAPI.getLatestAddedRecipe();
		
		addShapelessOreDictRecipe(new ItemStack(livingcobble), LIVING_ROCK);
		recipeLivingcobble = BotaniaAPI.getLatestAddedRecipe();
		
		addShapelessOreDictRecipe(new ItemStack(manaResource, 4, 5), ancientWill);
		
		addShapelessOreDictRecipe(new ItemStack(manaRingGod), MAUFTRIUM_INGOT, manaStoneGreater);
		recipeManaRingGod = BotaniaAPI.getLatestAddedRecipe();
		
		addShapelessRecipe(new ItemStack(brown_mushroom), mushroom);
		addShapelessRecipe(new ItemStack(red_mushroom), mushroom);
	}
	
	public static void registerSmeltingRecipes() {
		addSmelting(new ItemStack(elvenOres, 1, 1), new ItemStack(manaResource, 1, 7), 1.0F);
		addSmelting(new ItemStack(elvenOres, 1, 3), new ItemStack(gold_ingot, 1, 0), 1.0F);
		addSmelting(elvenSand, new ItemStack(elfGlass), 1.0F);
		addSmelting(manaGlass, new ItemStack(glass), 0);
	}
	
	public static void registerManaInfusionRecipes() {
		// Why is this here?
		/*addRecipe(new ItemStack(elfGlass), 100,
			new ItemStack[] {new ItemStack(Modquartz, 1, 5), new ItemStack(elvenGlass)});*/
		
		/*recipeMuspelheimEssence = addInfuserRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimEssence),
			TilePool.MAX_MANA / 10,
			LIFE_ESSENCE,
			new ItemStack(lava_bucket, 1, 0));
		
		recipeNiflheimEssence = addInfuserRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimEssence),
			TilePool.MAX_MANA / 10,
			LIFE_ESSENCE,
			new ItemStack(ice, 1, 0));*/
		
		recipeTerrasteel = addInfuserRecipe(new ItemStack(manaResource, 1, 4),
			TilePool.MAX_MANA / 2,
			MANA_STEEL,
			MANA_PEARL,
			MANA_DIAMOND);
		
		recipeElvorium = addInfuserRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot),
			TilePool.MAX_MANA / 2,
			ELEMENTIUM,
			PIXIE_DUST,
			DRAGONSTONE);
		
		recipeMauftrium = addInfuserRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot),
			TilePool.MAX_MANA,
			GAIA_INGOT,
			MUSPELHEIM_POWER_INGOT,
			NIFLHEIM_POWER_INGOT);
		
		recipeManaStone = addInfuserRecipe(new ItemStack(manaStone, 1, 1000),
			TilePool.MAX_MANA,
			DRAGONSTONE,
			new ItemStack(elvenResource, 4, ElvenResourcesMetas.IffesalDust));
		
		recipeManaStoneGreater = addInfuserRecipe(new ItemStack(manaStoneGreater, 1, 1000),
			TilePool.MAX_MANA * 4,
			new ItemStack(manaStone, 1, OreDictionary.WILDCARD_VALUE),
			new ItemStack(manaResource, 4, 5),
			new ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimEssence),
			new ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimEssence));
	}
	
	public static void banRetrades() {
		AlfheimAPI.banRetrade(AlfheimRecipes.recipeInterdimensional.getOutput());
		AlfheimAPI.banRetrade(new ItemStack(iron_ingot));
		AlfheimAPI.banRetrade(new ItemStack(iron_block));
		AlfheimAPI.banRetrade(new ItemStack(ender_pearl));
		AlfheimAPI.banRetrade(new ItemStack(diamond));
		AlfheimAPI.banRetrade(new ItemStack(diamond_block));
	}
	
	public static void registerRecipies() {
		final int costTier1 = 5200;
		final int costTier2 = 8000;
		final int costTier3 = 12000;
		
		recipeRealityRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.PrimalRune), costTier3,
				RUNE[0], RUNE[1], RUNE[2], RUNE[3], RUNE[8], new ItemStack(manaResource, 1, 15), MAUFTRIUM_INGOT);
		recipeMuspelheimRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimRune), costTier3,
				RUNE[1], RUNE[2], new ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimEssence), new ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimEssence), IFFESAL_DUST);
		recipeNiflheimRune = BotaniaAPI.registerRuneAltarRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimRune), costTier3,
				RUNE[0], RUNE[3], new ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimEssence), new ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimEssence), IFFESAL_DUST);
		
		ModRuneRecipes.recipesEarthRune.add(BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.rune, 2, 2), costTier1, MANA_POWDER, MANA_STEEL, new ItemStack(livingcobble), new ItemStack(obsidian), new ItemStack(brown_mushroom)));
		ModRuneRecipes.recipesEarthRune.add(BotaniaAPI.registerRuneAltarRecipe(new ItemStack(ModItems.rune, 2, 2), costTier1, MANA_POWDER, MANA_STEEL, new ItemStack(livingcobble), new ItemStack(obsidian), new ItemStack(red_mushroom)));
		
		recipeInterdimensional = BotaniaAPI.registerElvenTradeRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.InterdimensionalGatewayCore), new ItemStack(nether_star));
		recipeDreamwood = BotaniaAPI.registerPureDaisyRecipe(dreamLog, dreamwood, 0);
		
		BotaniaAPI.registerManaInfusionRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.InfusedDreamwoodTwig), new ItemStack(manaResource, 1, 13), 10000);
		
		addRecipe(new HelmRevealingAlfheimRecipe());
		addRecipe(new LootInterceptorRecipe());
		addRecipe(new LootInterceptorClearRecipe());
	}
	
	public static void init() {
		ModCraftingRecipes.recipeGaiaPylon = recipeGaiaPylon;
	}
	
	public static void addMMORecipes() {
		CraftingManager.getInstance().getRecipeList().add(recipePaperBreak);
		CraftingManager.getInstance().getRecipeList().add(recipePeacePipe);
	}
	
	public static void removeMMORecipes() {
		CraftingManager.getInstance().getRecipeList().remove(AlfheimRecipes.recipePaperBreak);
		CraftingManager.getInstance().getRecipeList().remove(AlfheimRecipes.recipePeacePipe);
	}
}
