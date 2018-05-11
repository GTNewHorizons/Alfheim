package alfheim.common.core.registry;

import static alexsocol.asjlib.ASJUtilities.addOreDictRecipe;
import static alexsocol.asjlib.ASJUtilities.addShapelessOreDictRecipe;
import static alexsocol.asjlib.ASJUtilities.removeRecipe;
import static alfheim.api.AlfheimAPI.addInfuserRecipe;
import static alfheim.api.lib.LibOreDict.ARUNE;
import static alfheim.api.lib.LibOreDict.ELVORIUM_INGOT;
import static alfheim.api.lib.LibOreDict.ELVORIUM_NUGGET;
import static alfheim.api.lib.LibOreDict.IFFESAL_DUST;
import static alfheim.api.lib.LibOreDict.MAUFTRIUM_INGOT;
import static alfheim.api.lib.LibOreDict.MAUFTRIUM_NUGGET;
import static alfheim.api.lib.LibOreDict.MUSPELHEIM_ESSENCE;
import static alfheim.api.lib.LibOreDict.MUSPELHEIM_POWER_INGOT;
import static alfheim.api.lib.LibOreDict.NIFLHEIM_ESSENCE;
import static alfheim.api.lib.LibOreDict.NIFLHEIM_POWER_INGOT;
import static alfheim.common.core.registry.AlfheimBlocks.alfheimPortal;
import static alfheim.common.core.registry.AlfheimBlocks.alfheimPylons;
import static alfheim.common.core.registry.AlfheimBlocks.anyavil;
import static alfheim.common.core.registry.AlfheimBlocks.dreamLeaves;
import static alfheim.common.core.registry.AlfheimBlocks.dreamlog;
import static alfheim.common.core.registry.AlfheimBlocks.elvenOres;
import static alfheim.common.core.registry.AlfheimBlocks.elvenSand;
import static alfheim.common.core.registry.AlfheimBlocks.elvoriumBlock;
import static alfheim.common.core.registry.AlfheimBlocks.livingcobble;
import static alfheim.common.core.registry.AlfheimBlocks.manaInfuser;
import static alfheim.common.core.registry.AlfheimBlocks.mauftriumBlock;
import static alfheim.common.core.registry.AlfheimBlocks.tradePortal;
import static alfheim.common.core.registry.AlfheimItems.elementalBoots;
import static alfheim.common.core.registry.AlfheimItems.elementalChestplate;
import static alfheim.common.core.registry.AlfheimItems.elementalHelmet;
import static alfheim.common.core.registry.AlfheimItems.elementalHelmetRevealing;
import static alfheim.common.core.registry.AlfheimItems.elementalLeggings;
import static alfheim.common.core.registry.AlfheimItems.elfFirePendant;
import static alfheim.common.core.registry.AlfheimItems.elfIcePendant;
import static alfheim.common.core.registry.AlfheimItems.elvenResource;
import static alfheim.common.core.registry.AlfheimItems.elvoriumBoots;
import static alfheim.common.core.registry.AlfheimItems.elvoriumChestplate;
import static alfheim.common.core.registry.AlfheimItems.elvoriumHelmet;
import static alfheim.common.core.registry.AlfheimItems.elvoriumHelmetRevealing;
import static alfheim.common.core.registry.AlfheimItems.elvoriumLeggings;
import static alfheim.common.core.registry.AlfheimItems.livingrockPickaxe;
import static alfheim.common.core.registry.AlfheimItems.manaElvenRing;
import static alfheim.common.core.registry.AlfheimItems.manaElvenRingGreater;
import static alfheim.common.core.registry.AlfheimItems.manaStone;
import static alfheim.common.core.registry.AlfheimItems.manaStoneGreater;
import static alfheim.common.core.registry.AlfheimItems.pixieAttractor;
import static alfheim.common.core.registry.AlfheimItems.realitySword;
import static alfheim.common.core.registry.AlfheimItems.rodFire;
import static alfheim.common.core.registry.AlfheimItems.rodIce;
import static cpw.mods.fml.common.registry.GameRegistry.addRecipe;
import static cpw.mods.fml.common.registry.GameRegistry.addShapelessRecipe;
import static cpw.mods.fml.common.registry.GameRegistry.addSmelting;
import static net.minecraft.init.Blocks.diamond_block;
import static net.minecraft.init.Blocks.furnace;
import static net.minecraft.init.Blocks.ice;
import static net.minecraft.init.Blocks.iron_block;
import static net.minecraft.init.Items.blaze_rod;
import static net.minecraft.init.Items.diamond;
import static net.minecraft.init.Items.ender_pearl;
import static net.minecraft.init.Items.glowstone_dust;
import static net.minecraft.init.Items.gold_ingot;
import static net.minecraft.init.Items.iron_ingot;
import static net.minecraft.init.Items.lava_bucket;
import static net.minecraft.init.Items.nether_star;
import static vazkii.botania.common.block.ModBlocks.dreamwood;
import static vazkii.botania.common.block.ModBlocks.elfGlass;
import static vazkii.botania.common.block.ModBlocks.pylon;
import static vazkii.botania.common.item.ModItems.ancientWill;
import static vazkii.botania.common.item.ModItems.elementiumBoots;
import static vazkii.botania.common.item.ModItems.elementiumChest;
import static vazkii.botania.common.item.ModItems.elementiumHelm;
import static vazkii.botania.common.item.ModItems.elementiumHelmRevealing;
import static vazkii.botania.common.item.ModItems.elementiumLegs;
import static vazkii.botania.common.item.ModItems.icePendant;
import static vazkii.botania.common.item.ModItems.lavaPendant;
import static vazkii.botania.common.item.ModItems.lens;
import static vazkii.botania.common.item.ModItems.manaResource;
import static vazkii.botania.common.item.ModItems.overgrowthSeed;
import static vazkii.botania.common.item.ModItems.rainbowRod;
import static vazkii.botania.common.item.ModItems.spark;
import static vazkii.botania.common.item.ModItems.terrasteelBoots;
import static vazkii.botania.common.item.ModItems.terrasteelChest;
import static vazkii.botania.common.item.ModItems.terrasteelHelm;
import static vazkii.botania.common.item.ModItems.terrasteelHelmRevealing;
import static vazkii.botania.common.item.ModItems.terrasteelLegs;
import static vazkii.botania.common.lib.LibOreDict.DRAGONSTONE;
import static vazkii.botania.common.lib.LibOreDict.DREAMWOOD_TWIG;
import static vazkii.botania.common.lib.LibOreDict.DREAM_WOOD;
import static vazkii.botania.common.lib.LibOreDict.ELEMENTIUM;
import static vazkii.botania.common.lib.LibOreDict.GAIA_INGOT;
import static vazkii.botania.common.lib.LibOreDict.LIFE_ESSENCE;
import static vazkii.botania.common.lib.LibOreDict.LIVING_ROCK;
import static vazkii.botania.common.lib.LibOreDict.MANA_DIAMOND;
import static vazkii.botania.common.lib.LibOreDict.MANA_PEARL;
import static vazkii.botania.common.lib.LibOreDict.MANA_STEEL;
import static vazkii.botania.common.lib.LibOreDict.PETAL;
import static vazkii.botania.common.lib.LibOreDict.PIXIE_DUST;
import static vazkii.botania.common.lib.LibOreDict.RUNE;
import static vazkii.botania.common.lib.LibOreDict.TERRASTEEL_NUGGET;

import java.util.List;

import alfheim.api.AlfheimAPI;
import alfheim.api.crafting.recipe.RecipeManaInfuser;
import alfheim.common.core.registry.AlfheimItems.ElvenResourcesMetas;
import alfheim.common.crafting.recipe.HelmRevealingAlfheimRecipe;
import alfheim.common.item.equipment.tools.ItemTwigWandExtender;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.api.recipe.RecipePureDaisy;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.crafting.ModCraftingRecipes;
import vazkii.botania.common.item.ModItems;

public class AlfheimRecipes {
	
	public static RecipeManaInfuser recipeElvorium;
	public static RecipeManaInfuser recipeMuspelheimEssence;
	public static RecipeManaInfuser recipeNiflheimEssence;
	public static RecipeManaInfuser recipeTerrasteel;
	public static RecipeManaInfuser recipeMauftrium;
	public static RecipeManaInfuser recipeManaStone;
	public static RecipeManaInfuser recipeManaStoneGreater;
	
	public static IRecipe recipeAlfheimPortal;
	public static IRecipe recipeAnyavil;
	public static List<IRecipe> recipesApothecary;
	public static IRecipe recipeEmentalHelmet;
	public static IRecipe recipeEmentalChestplate;
	public static IRecipe recipeEmentalLeggings;
	public static IRecipe recipeEmentalBoots;
	public static IRecipe recipeElvenPylon;
	private static List<IRecipe> recipesElvenWand;
	public static IRecipe recipeElvoriumHelmet;
	public static IRecipe recipeElvoriumChestplate;
	public static IRecipe recipeElvoriumLeggings;
	public static IRecipe recipeElvoriumBoots;
	public static IRecipe recipeElvoriumPylon;
	public static IRecipe recipeFurnace;
	public static IRecipe recipeGaiaPylon;
	public static IRecipe recipeGlowstone;
	public static IRecipe recipeLivingcobble;
	public static IRecipe recipeLivingrockPickaxe;
	public static IRecipe recipeManaElvenRing;
	public static IRecipe recipeManaElvenRingGreater1;
	public static IRecipe recipeManaElvenRingGreater2;
	public static IRecipe recipeManaInfusionCore;
	public static IRecipe recipeManaInfuser;
	public static IRecipe recipeMuspelheimPendant;
	public static IRecipe recipeMuspelheimPowerIngot;
	public static IRecipe recipeMuspelheimRod;
	public static IRecipe recipeNiflheimPendant;
	public static IRecipe recipeNiflheimPowerIngot;
	public static IRecipe recipeNiflheimRod;
	public static IRecipe recipePixieAttractor;
	public static List<IRecipe> recipesSpark;
	public static IRecipe recipeSword;
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
		forbidRetrades();
		//if (ModInfo.DEV && FMLCommonHandler.instance().getEffectiveSide().equals(Side.CLIENT)) (new NEIAlfheimConfig()).loadConfig();
	}

	public static void registerCraftingRecipes() {
		addOreDictRecipe(new ItemStack(alfheimPortal, 1),
			"DPD", "GSG", "DTD",
			'D', DREAM_WOOD,
			'G', spark,
			'P', RUNE[8],
			'S', rainbowRod,
			'T', new ItemStack(lens, 1, 9));
		recipeAlfheimPortal = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(anyavil),
				"BGB", " P ", "EDE",
				'P', PIXIE_DUST,
				'E', ELEMENTIUM,
				'D', DRAGONSTONE,
				'B', new ItemStack(ModBlocks.storage, 1, 2),
				'G', new ItemStack(ModBlocks.storage, 1, 4));
		recipeAnyavil = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(alfheimPylons, 1, 0),
			" P ", "EDE", " P ",
			'P', PIXIE_DUST,
			'E', ELEMENTIUM,
			'D', DRAGONSTONE);
		recipeElvenPylon = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(alfheimPylons, 1, 1),
			" E ", "EPE", "III",
			'E', ELVORIUM_NUGGET,
			'P', new ItemStack(alfheimPylons, 1, 0),
			'I', IFFESAL_DUST);
		recipeElvoriumPylon = BotaniaAPI.getLatestAddedRecipe();
		
		for(int i = 0; i < 16; i++)
			addOreDictRecipe(new ItemStack(ModBlocks.altar),
					"SPS", " C ", "CCC",
					'S', livingcobble,
					'P', PETAL[i],
					'C', LIVING_ROCK);
		recipesApothecary = BotaniaAPI.getLatestAddedRecipes(16);
		
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
		
		removeRecipe(ModCraftingRecipes.recipeGaiaPylon.getRecipeOutput());
		addOreDictRecipe(new ItemStack(pylon, 1, 2),
				" E ", "TPT", " E ",
				'T', TERRASTEEL_NUGGET,
				'E', overgrowthSeed,
				'P', new ItemStack(alfheimPylons, 1, 0));
		recipeGaiaPylon = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(elfFirePendant),
			" N ", "NPN", "RN ",
			'N', MAUFTRIUM_NUGGET,
			'R', ARUNE[1],
			'P', lavaPendant);
		recipeMuspelheimPendant = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(elfIcePendant),
			" N ", "NPN", "RN ",
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
		
		// IDK whether this is good source of glowstone or not
		addOreDictRecipe(new ItemStack(glowstone_dust),
			"LLL", "LDL", "LLL",
			'L', dreamLeaves,
			'D', DRAGONSTONE);
		recipeGlowstone = BotaniaAPI.getLatestAddedRecipe(); 
		
		addOreDictRecipe(new ItemStack(livingrockPickaxe),
			"LLL", " S ", " S ",
			'L', livingcobble,
			'S', "stickWood");
		recipeLivingrockPickaxe = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(manaElvenRing),
			"IS ", "S S", " S ",
			'S', ELVORIUM_INGOT,
			'I', manaStone);
		recipeManaElvenRing = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(manaElvenRingGreater),
			"IS ", "S S", " S ",
			'S', MAUFTRIUM_INGOT,
			'I', manaStoneGreater);
		recipeManaElvenRingGreater1 = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(manaElvenRingGreater), 
			"SI", "IR",
			'S', manaStoneGreater,
			'R', manaElvenRing,
			'I', MAUFTRIUM_INGOT);
		recipeManaElvenRingGreater2 = BotaniaAPI.getLatestAddedRecipe();
		
		addOreDictRecipe(new ItemStack(manaInfuser),
			"DCD", "IRI", "SSS",
			'C', new ItemStack(elvenResource, 1, ElvenResourcesMetas.ManaInfusionCore),
			'D', DRAGONSTONE,
			'I', ELEMENTIUM,
			'R', rainbowRod,
			'S', LIVING_ROCK);
		recipeManaInfuser = BotaniaAPI.getLatestAddedRecipe();
		
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
		
		addOreDictRecipe(new ItemStack(rodIce),
			" MR", " BM", "B  ",
			'M', MAUFTRIUM_INGOT,
			'R', ARUNE[2],
			'B', blaze_rod);
		recipeNiflheimRod = BotaniaAPI.getLatestAddedRecipe();
		
		for(int i = 0; i < 16; i++)
			addOreDictRecipe(new ItemStack(ModItems.spark),
				" P ", "BNB", " P ",
				'B', PIXIE_DUST,
				'P', PETAL[i],
				'N', "nuggetGold");
		recipesSpark = BotaniaAPI.getLatestAddedRecipes(16);
		recipesSpark.addAll(ModCraftingRecipes.recipesSpark);
		
		addOreDictRecipe(new ItemStack(tradePortal),
			"LEL", "LEL", "LEL",
			'L', LIVING_ROCK,
			'E', ELVORIUM_NUGGET);
		recipeTradePortal = BotaniaAPI.getLatestAddedRecipe();
	}

	public static void registerShapelessRecipes() {
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
		
		addShapelessOreDictRecipe(new ItemStack(livingcobble), LIVING_ROCK);
		recipeLivingcobble = BotaniaAPI.getLatestAddedRecipe();
		
		addShapelessOreDictRecipe(new ItemStack(manaResource, 4, 5), ancientWill);
	}
	
	public static void registerSmeltingRecipes() {
		addSmelting(new ItemStack(elvenOres, 1, 1), new ItemStack(manaResource, 1, 7), 1.0F);
		addSmelting(new ItemStack(elvenOres, 1, 3), new ItemStack(gold_ingot, 1, 0), 1.0F);
		addSmelting(elvenSand, new ItemStack(elfGlass), 1.0F);
	}

	public static void registerManaInfusionRecipes() {
		// Why is this here?
		/*addRecipe(new ItemStack(elfGlass), 100,
			new ItemStack[] {new ItemStack(Modquartz, 1, 5), new ItemStack(elvenGlass)});*/
		
		recipeMuspelheimEssence = addInfuserRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimEssence),
			TilePool.MAX_MANA / 10,
			LIFE_ESSENCE,
			new ItemStack(lava_bucket, 1, 0));
		
		recipeNiflheimEssence = addInfuserRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimEssence),
			TilePool.MAX_MANA / 10,
			LIFE_ESSENCE,
			new ItemStack(ice, 1, 0));
		
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
			new ItemStack(elvenResource, 2, ElvenResourcesMetas.MuspelheimEssence),
			new ItemStack(elvenResource, 2, ElvenResourcesMetas.NiflheimEssence));
	}
	
	public static void forbidRetrades() {
		AlfheimAPI.addForbiddenRetrade(AlfheimRecipes.recipeInterdimensional.getOutput());
		AlfheimAPI.addForbiddenRetrade(new ItemStack(iron_ingot));
		AlfheimAPI.addForbiddenRetrade(new ItemStack(iron_block));
		AlfheimAPI.addForbiddenRetrade(new ItemStack(ender_pearl));
		AlfheimAPI.addForbiddenRetrade(new ItemStack(diamond));
		AlfheimAPI.addForbiddenRetrade(new ItemStack(diamond_block));
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
		
		recipeInterdimensional = BotaniaAPI.registerElvenTradeRecipe(new ItemStack(elvenResource, 1, ElvenResourcesMetas.InterdimensionalGatewayCore), new ItemStack(nether_star));
		recipeDreamwood = BotaniaAPI.registerPureDaisyRecipe(dreamlog, dreamwood, 0);
		
		addRecipe(new HelmRevealingAlfheimRecipe());
	}

	public static void init() {
		ModCraftingRecipes.recipeGaiaPylon = recipeGaiaPylon;
	}
}
