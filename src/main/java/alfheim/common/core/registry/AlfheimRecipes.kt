package alfheim.common.core.registry

import alfheim.AlfheimCore
import alfheim.api.AlfheimAPI
import alfheim.api.crafting.recipe.RecipeManaInfuser
import alfheim.common.core.registry.AlfheimItems.*
import alfheim.common.crafting.recipe.*
import alfheim.common.item.equipment.tool.ItemTwigWandExtender
import net.minecraft.item.*
import net.minecraft.item.crafting.*
import net.minecraftforge.oredict.*
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.recipe.*
import vazkii.botania.common.Botania
import vazkii.botania.common.block.tile.mana.TilePool
import vazkii.botania.common.crafting.*
import vazkii.botania.common.item.ModItems

import alexsocol.asjlib.ASJUtilities.*
import alfheim.api.AlfheimAPI.addInfuserRecipe
import alfheim.api.lib.LibOreDict.*
import alfheim.common.core.registry.AlfheimBlocks.*
import cpw.mods.fml.common.registry.GameRegistry.*
import net.minecraft.init.Blocks.*
import net.minecraft.init.Items.*
import vazkii.botania.common.block.ModBlocks.*
import vazkii.botania.common.item.ModItems.*
import vazkii.botania.common.lib.LibOreDict.*

object AlfheimRecipes {
	
	var recipeElvorium: RecipeManaInfuser
	//public static RecipeManaInfuser recipeMuspelheimEssence;
	//public static RecipeManaInfuser recipeNiflheimEssence;
	var recipeTerrasteel: RecipeManaInfuser
	var recipeMauftrium: RecipeManaInfuser
	var recipeManaStone: RecipeManaInfuser
	var recipeManaStoneGreater: RecipeManaInfuser
	
	var recipeAlfheimPortal: IRecipe
	var recipeAnimatedTorch: IRecipe
	var recipeAnyavil: IRecipe
	var recipesApothecary: List<IRecipe>
	var recipeAstrolabe: IRecipe
	var recipeAuraRingElven: IRecipe
	var recipeAuraRingGod: IRecipe
	var recipeBalanceCloak: IRecipe
	var recipeCloudPendant: IRecipe
	var recipeCloudPendantSuper: IRecipe
	var recipeCrescentAmulet: IRecipe
	var recipeDodgeRing: IRecipe
	var recipeEmentalHelmet: IRecipe
	var recipeEmentalChestplate: IRecipe
	var recipeEmentalLeggings: IRecipe
	var recipeEmentalBoots: IRecipe
	var recipeEmentiumHoe: IRecipe
	var recipeElvenPylon: IRecipe
	var recipesElvenWand: List<IRecipe>
	var recipeElvoriumHelmet: IRecipe
	var recipeElvoriumChestplate: IRecipe
	var recipeElvoriumLeggings: IRecipe
	var recipeElvoriumBoots: IRecipe
	var recipeElvoriumPylon: IRecipe
	var recipeFurnace: IRecipe
	var recipeGaiaPylon: IRecipe
	var recipeGreenRod: IRecipe
	var recipeInvisibilityCloak: IRecipe
	var recipeItemHolder: IRecipe
	var recipeLensMessenger: IRecipe
	var recipeLensTripwire: IRecipe
	var recipeLivingcobble: IRecipe
	var recipeLivingrockPickaxe: IRecipe
	var recipeLootInterceptor: IRecipe
	var recipeManaInfusionCore: IRecipe
	var recipeManaInfuser: IRecipe
	var recipeManaRingElven: IRecipe
	var recipeManaRingGod: IRecipe
	var recipeManasteelHoe: IRecipe
	var recipeMuspelheimPendant: IRecipe
	var recipeMuspelheimPowerIngot: IRecipe
	var recipeMuspelheimRod: IRecipe
	var recipeNiflheimPendant: IRecipe
	var recipeNiflheimPowerIngot: IRecipe
	var recipeNiflheimRod: IRecipe
	var recipePaperBreak: IRecipe
	var recipePeacePipe: IRecipe
	var recipePixieAttractor: IRecipe
	var recipesSpark: MutableList<IRecipe>
	var recipeSword: IRecipe
	var recipeThinkingHand: IRecipe
	var recipeTradePortal: IRecipe
	
	var recipeInterdimensional: RecipeElvenTrade
	
	var recipeDreamwood: RecipePureDaisy
	
	var recipeMuspelheimRune: RecipeRuneAltar
	var recipeNiflheimRune: RecipeRuneAltar
	var recipeRealityRune: RecipeRuneAltar
	
	fun preInit() {
		registerCraftingRecipes()
		registerShapelessRecipes()
		registerSmeltingRecipes()
		registerManaInfusionRecipes()
		registerRecipies()
		banRetrades()
		//if (ModInfo.DEV && FMLCommonHandler.instance().getEffectiveSide().equals(Side.CLIENT)) (new NEIAlfheimConfig()).loadConfig();
	}
	
	fun registerCraftingRecipes() {
		addOreDictRecipe(ItemStack(alfheimPortal, 1),
						 "DPD", "GSG", "DTD",
						 'D', DREAM_WOOD,
						 'G', spark,
						 'P', RUNE[8],
						 'S', rainbowRod,
						 'T', ItemStack(lens, 1, 18))
		recipeAlfheimPortal = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(alfheimPylon, 1, 0),
						 " P ", "EDE", " P ",
						 'P', PIXIE_DUST,
						 'E', ELEMENTIUM,
						 'D', DRAGONSTONE)
		recipeElvenPylon = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(alfheimPylon, 1, 1),
						 " E ", "EPE", "III",
						 'E', ELVORIUM_NUGGET,
						 'P', ItemStack(alfheimPylon, 1, 0),
						 'I', IFFESAL_DUST)
		recipeElvoriumPylon = BotaniaAPI.getLatestAddedRecipe()
		
		for (i in 0..15)
			addOreDictRecipe(ItemStack(altar),
							 "SPS", " C ", "CCC",
							 'S', livingcobble,
							 'P', PETAL[i],
							 'C', LIVING_ROCK)
		recipesApothecary = BotaniaAPI.getLatestAddedRecipes(16)
		
		addOreDictRecipe(ItemStack(animatedTorch),
						 "P", "T",
						 'T', redstone_torch,
						 'P', MANA_POWDER)
		recipeAnimatedTorch = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(anyavil),
						 "BGB", " P ", "EDE",
						 'P', PIXIE_DUST,
						 'E', ELEMENTIUM,
						 'D', DRAGONSTONE,
						 'B', ItemStack(storage, 1, 2),
						 'G', ItemStack(storage, 1, 4))
		recipeAnyavil = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(astrolabe),
						 " ES", "EEE", "SED",
						 'E', ELEMENTIUM,
						 'S', LIFE_ESSENCE,
						 'D', DREAM_WOOD)
		recipeAstrolabe = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(balanceCloak),
						 "WWW", "EWE", "ESE",
						 'W', ItemStack(wool, 1, 8),
						 'E', "gemEmerald",
						 'S', LIFE_ESSENCE)
		recipeBalanceCloak = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(cloudPendant),
						 "US ", "S S", "MSA",
						 'U', RUNE[6],
						 'S', MANA_STRING,
						 'M', MANA_STEEL,
						 'A', RUNE[3])
		recipeCloudPendant = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(cloudPendantSuper),
						 "GEG", "GPG", "WSW",
						 'G', ghast_tear,
						 'E', ELEMENTIUM,
						 'P', ItemStack(cloudPendant),
						 'W', ItemStack(wool, 1, 0),
						 'S', LIFE_ESSENCE)
		recipeCloudPendantSuper = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(crescentMoonAmulet),
						 "  M", "MS ", "RM ",
						 'M', MAUFTRIUM_NUGGET,
						 'R', RUNE[13],
						 'S', ItemStack(manaResource, 1, 12))
		recipeCrescentAmulet = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(dodgeRing),
						 "EM ", "M M", " MR",
						 'E', "gemEmerald",
						 'M', MANA_STEEL,
						 'R', RUNE[3])
		recipeDodgeRing = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elementalHelmet),
						 "RTR", "DPD", " M ",
						 'R', RUNE[0],
						 'T', DREAMWOOD_TWIG,
						 'D', IFFESAL_DUST,
						 'P', elementiumHelm,
						 'M', RUNE[8])
		recipeEmentalHelmet = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elementalHelmetRevealing),
						 "RTR", "DPD", " M ",
						 'R', RUNE[0],
						 'T', DREAMWOOD_TWIG,
						 'D', IFFESAL_DUST,
						 'P', elementiumHelmRevealing,
						 'M', RUNE[8])
		
		addOreDictRecipe(ItemStack(elementalChestplate),
						 "RTR", "DPD", " M ",
						 'R', RUNE[2],
						 'T', DREAMWOOD_TWIG,
						 'D', IFFESAL_DUST,
						 'P', elementiumChest,
						 'M', RUNE[8])
		recipeEmentalChestplate = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elementalLeggings),
						 "RTR", "DPD", " M ",
						 'R', RUNE[1],
						 'T', DREAMWOOD_TWIG,
						 'D', IFFESAL_DUST,
						 'P', elementiumLegs,
						 'M', RUNE[8])
		recipeEmentalLeggings = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elementalBoots),
						 "RTR", "DPD", " M ",
						 'R', RUNE[3],
						 'T', DREAMWOOD_TWIG,
						 'D', IFFESAL_DUST,
						 'P', elementiumBoots,
						 'M', RUNE[8])
		recipeEmentalBoots = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elementiumHoe),
						 "EE", " T", " T",
						 'E', ELEMENTIUM,
						 'T', DREAMWOOD_TWIG)
		recipeEmentiumHoe = BotaniaAPI.getLatestAddedRecipe()
		
		removeRecipe(ModCraftingRecipes.recipeGaiaPylon.recipeOutput)
		addOreDictRecipe(ItemStack(pylon, 1, 2),
						 " E ", "TPT", " E ",
						 'T', TERRASTEEL_NUGGET,
						 'E', overgrowthSeed,
						 'P', ItemStack(alfheimPylon, 1, 0))
		recipeGaiaPylon = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elfFirePendant),
						 "  N", "NP ", "RN ",
						 'N', MAUFTRIUM_NUGGET,
						 'R', ARUNE[1],
						 'P', lavaPendant)
		recipeMuspelheimPendant = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elfIcePendant),
						 "  N", "NP ", "RN ",
						 'N', MAUFTRIUM_NUGGET,
						 'R', ARUNE[2],
						 'P', icePendant)
		recipeNiflheimPendant = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elvenResource, 1, ElvenResourcesMetas.ManaInfusionCore),
						 "PGP", "GDG", "PGP",
						 'D', PIXIE_DUST,
						 'G', "ingotGold",
						 'P', IFFESAL_DUST)
		recipeManaInfusionCore = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimPowerIngot),
						 " S ", "SIS", " S ",
						 'S', MUSPELHEIM_ESSENCE,
						 'I', ELVORIUM_INGOT)
		recipeMuspelheimPowerIngot = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimPowerIngot),
						 " S ", "SIS", " S ",
						 'S', NIFLHEIM_ESSENCE,
						 'I', ELVORIUM_INGOT)
		recipeNiflheimPowerIngot = BotaniaAPI.getLatestAddedRecipe()
		
		for (i in 0..15)
			for (j in 0..15) {
				addOreDictRecipe(ItemTwigWandExtender.forColors(i, j, true),
								 " AS", " SB", "S  ",
								 'A', PETAL[i],
								 'B', PETAL[j],
								 'S', DREAMWOOD_TWIG)
			}
		recipesElvenWand = BotaniaAPI.getLatestAddedRecipes(256)
		
		addOreDictRecipe(ItemStack(elvoriumHelmet),
						 "TRT", "EPE", "CMC",
						 'T', DREAMWOOD_TWIG,
						 'R', ARUNE[0],
						 'E', ELVORIUM_INGOT,
						 'P', terrasteelHelm,
						 'C', ItemStack(elvenResource, 1, ElvenResourcesMetas.ManaInfusionCore),
						 'M', MAUFTRIUM_INGOT)
		recipeElvoriumHelmet = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elvoriumHelmetRevealing),
						 "TRT", "EPE", "CMC",
						 'T', DREAMWOOD_TWIG,
						 'R', ARUNE[0],
						 'E', ELVORIUM_INGOT,
						 'P', terrasteelHelmRevealing,
						 'C', ItemStack(elvenResource, 1, ElvenResourcesMetas.ManaInfusionCore),
						 'M', MAUFTRIUM_INGOT)
		
		addOreDictRecipe(ItemStack(elvoriumChestplate),
						 "TRT", "EPE", "CMC",
						 'T', DREAMWOOD_TWIG,
						 'R', ARUNE[0],
						 'E', ELVORIUM_INGOT,
						 'P', terrasteelChest,
						 'C', ItemStack(elvenResource, 1, ElvenResourcesMetas.ManaInfusionCore),
						 'M', MAUFTRIUM_INGOT)
		recipeElvoriumChestplate = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elvoriumLeggings),
						 "TRT", "EPE", "CMC",
						 'T', DREAMWOOD_TWIG,
						 'R', ARUNE[0],
						 'E', ELVORIUM_INGOT,
						 'P', terrasteelLegs,
						 'C', ItemStack(elvenResource, 1, ElvenResourcesMetas.ManaInfusionCore),
						 'M', MAUFTRIUM_INGOT)
		recipeElvoriumLeggings = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elvoriumBoots),
						 "TRT", "EPE", "CMC",
						 'T', DREAMWOOD_TWIG,
						 'R', ARUNE[0],
						 'E', ELVORIUM_INGOT,
						 'P', terrasteelBoots,
						 'C', ItemStack(elvenResource, 1, ElvenResourcesMetas.ManaInfusionCore),
						 'M', MAUFTRIUM_INGOT)
		recipeElvoriumBoots = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(furnace),
						 "SSS", "S S", "SSS",
						 'S', livingcobble)
		recipeFurnace = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(invisibilityCloak),
						 "PWP", "GWG", "GJG",
						 'P', PRISMARINE_SHARD,
						 'W', ItemStack(wool, 1, 0),
						 'G', manaGlass,
						 'J', MANA_PEARL)
		recipeInvisibilityCloak = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(itemHolder),
						 "MLM", "LDL",
						 'D', MANA_DIAMOND,
						 'L', LIVING_ROCK,
						 'M', MANA_PEARL)
		recipeItemHolder = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(lens, 1, 22),
						 " P ", "PLP", " P ",
						 'P', paper,
						 'L', ItemStack(lens, 1, 0))
		recipeLensMessenger = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(livingrockPickaxe),
						 "LLL", " S ", " S ",
						 'L', livingcobble,
						 'S', "stickWood")
		recipeLivingrockPickaxe = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(lootInterceptor),
						 "IHI", "DID",
						 'I', IFFESAL_DUST,
						 'H', blackHoleTalisman,
						 'D', DREAM_WOOD)
		recipeLootInterceptor = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(manaInfuser),
						 "DCD", "IRI", "SSS",
						 'C', ItemStack(elvenResource, 1, ElvenResourcesMetas.ManaInfusionCore),
						 'D', DRAGONSTONE,
						 'I', ELEMENTIUM,
						 'R', rainbowRod,
						 'S', LIVING_ROCK)
		recipeManaInfuser = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(manaRingElven),
						 "IS ", "S S", " S ",
						 'S', ELVORIUM_INGOT,
						 'I', manaStone)
		recipeManaRingElven = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(manasteelHoe),
						 "SS", " T", " T",
						 'S', MANA_STEEL,
						 'T', LIVINGWOOD_TWIG)
		recipeManasteelHoe = BotaniaAPI.getLatestAddedRecipe()
		
		recipePaperBreak = ShapedOreRecipe(ItemStack(paperBreak),
										   "  P", " P ", "P  ",
										   'P', paper)
		
		recipePeacePipe = ShapedOreRecipe(ItemStack(peacePipe),
										  "S  ", " S ", "  S",
										  'S', stick)
		
		if (AlfheimCore.enableMMO) addMMORecipes()
		
		addOreDictRecipe(ItemStack(pixieAttractor),
						 "EDE", "EPE", " S ",
						 'D', DRAGONSTONE,
						 'E', ELEMENTIUM,
						 'P', PIXIE_DUST,
						 'S', RUNE[2])
		recipePixieAttractor = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(realitySword),
						 " M ", "MRM", " S ",
						 'M', MAUFTRIUM_INGOT,
						 'R', ARUNE[0],
						 'S', ItemStack(manaResource, 1, 3))
		recipeSword = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(rodFire),
						 " MR", " BM", "B  ",
						 'M', MAUFTRIUM_INGOT,
						 'R', ARUNE[1],
						 'B', blaze_rod)
		recipeMuspelheimRod = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(rodGrass),
						 "  R", " D ", "S  ",
						 'D', dirtRod,
						 'R', RUNE[4],
						 'S', grassSeeds)
		recipeGreenRod = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(rodIce),
						 " MR", " BM", "B  ",
						 'M', MAUFTRIUM_INGOT,
						 'R', ARUNE[2],
						 'B', blaze_rod)
		recipeNiflheimRod = BotaniaAPI.getLatestAddedRecipe()
		
		for (i in 0..15)
			addOreDictRecipe(ItemStack(spark),
							 " P ", "BNB", " P ",
							 'B', PIXIE_DUST,
							 'P', PETAL[i],
							 'N', "nuggetGold")
		recipesSpark = BotaniaAPI.getLatestAddedRecipes(16)
		recipesSpark.addAll(ModCraftingRecipes.recipesSpark)
		
		addOreDictRecipe(ItemStack(thinkingHand),
						 "PPP", "PSP", "PPP",
						 'P', tinyPotato,
						 'S', MANA_STRING)
		recipeThinkingHand = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(tradePortal),
						 "LEL", "LEL", "LEL",
						 'L', LIVING_ROCK,
						 'E', ELVORIUM_NUGGET)
		recipeTradePortal = BotaniaAPI.getLatestAddedRecipe()
	}
	
	fun registerShapelessRecipes() {
		addShapelessOreDictRecipe(ItemStack(auraRingElven), ELVORIUM_INGOT, auraRingGreater)
		recipeAuraRingElven = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessOreDictRecipe(ItemStack(auraRingGod), MAUFTRIUM_INGOT, auraRingElven)
		recipeAuraRingGod = BotaniaAPI.getLatestAddedRecipe()
		
		if (Botania.thaumcraftLoaded) {
			val goggles = Item.itemRegistry.getObject("Thaumcraft:ItemGoggles") as Item
			addShapelessRecipe(ItemStack(elementalHelmetRevealing), ItemStack(elementalHelmet), goggles)
			addShapelessRecipe(ItemStack(elvoriumHelmetRevealing), ItemStack(elvoriumHelmet), goggles)
		}
		
		addShapelessOreDictRecipe(ItemStack(elvenResource, 9, ElvenResourcesMetas.ElvoriumNugget), ELVORIUM_INGOT)
		addShapelessOreDictRecipe(ItemStack(elvenResource, 9, ElvenResourcesMetas.MauftriumNugget), MAUFTRIUM_INGOT)
		addShapelessRecipe(ItemStack(elvenResource, 9, ElvenResourcesMetas.ElvoriumIngot), elvoriumBlock)
		addShapelessRecipe(ItemStack(elvenResource, 9, ElvenResourcesMetas.MauftriumIngot), mauftriumBlock)
		
		addShapelessOreDictRecipe(ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot), ELVORIUM_NUGGET, ELVORIUM_NUGGET, ELVORIUM_NUGGET, ELVORIUM_NUGGET, ELVORIUM_NUGGET, ELVORIUM_NUGGET, ELVORIUM_NUGGET, ELVORIUM_NUGGET, ELVORIUM_NUGGET)
		addShapelessOreDictRecipe(ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot), MAUFTRIUM_NUGGET, MAUFTRIUM_NUGGET, MAUFTRIUM_NUGGET, MAUFTRIUM_NUGGET, MAUFTRIUM_NUGGET, MAUFTRIUM_NUGGET, MAUFTRIUM_NUGGET, MAUFTRIUM_NUGGET, MAUFTRIUM_NUGGET)
		addShapelessOreDictRecipe(ItemStack(elvoriumBlock), ELVORIUM_INGOT, ELVORIUM_INGOT, ELVORIUM_INGOT, ELVORIUM_INGOT, ELVORIUM_INGOT, ELVORIUM_INGOT, ELVORIUM_INGOT, ELVORIUM_INGOT, ELVORIUM_INGOT)
		addShapelessOreDictRecipe(ItemStack(mauftriumBlock), MAUFTRIUM_INGOT, MAUFTRIUM_INGOT, MAUFTRIUM_INGOT, MAUFTRIUM_INGOT, MAUFTRIUM_INGOT, MAUFTRIUM_INGOT, MAUFTRIUM_INGOT, MAUFTRIUM_INGOT, MAUFTRIUM_INGOT)
		
		addShapelessOreDictRecipe(ItemStack(lens, 1, 23), ItemStack(lens, 1, 0), tripwire_hook, ELEMENTIUM)
		recipeLensTripwire = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessOreDictRecipe(ItemStack(livingcobble), LIVING_ROCK)
		recipeLivingcobble = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessOreDictRecipe(ItemStack(manaResource, 4, 5), ancientWill)
		
		addShapelessOreDictRecipe(ItemStack(manaRingGod), MAUFTRIUM_INGOT, manaStoneGreater)
		recipeManaRingGod = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessRecipe(ItemStack(brown_mushroom), mushroom)
		addShapelessRecipe(ItemStack(red_mushroom), mushroom)
	}
	
	fun registerSmeltingRecipes() {
		addSmelting(ItemStack(elvenOres, 1, 1), ItemStack(manaResource, 1, 7), 1.0f)
		addSmelting(ItemStack(elvenOres, 1, 3), ItemStack(gold_ingot, 1, 0), 1.0f)
		addSmelting(elvenSand, ItemStack(elfGlass), 1.0f)
		addSmelting(manaGlass, ItemStack(glass), 0f)
	}
	
	fun registerManaInfusionRecipes() {
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
		
		recipeTerrasteel = addInfuserRecipe(ItemStack(manaResource, 1, 4),
											TilePool.MAX_MANA / 2,
											MANA_STEEL,
											MANA_PEARL,
											MANA_DIAMOND)
		
		recipeElvorium = addInfuserRecipe(ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot),
										  TilePool.MAX_MANA / 2,
										  ELEMENTIUM,
										  PIXIE_DUST,
										  DRAGONSTONE)
		
		recipeMauftrium = addInfuserRecipe(ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot),
										   TilePool.MAX_MANA,
										   GAIA_INGOT,
										   MUSPELHEIM_POWER_INGOT,
										   NIFLHEIM_POWER_INGOT)
		
		recipeManaStone = addInfuserRecipe(ItemStack(manaStone, 1, 1000),
										   TilePool.MAX_MANA,
										   DRAGONSTONE,
										   ItemStack(elvenResource, 4, ElvenResourcesMetas.IffesalDust))
		
		recipeManaStoneGreater = addInfuserRecipe(ItemStack(manaStoneGreater, 1, 1000),
												  TilePool.MAX_MANA * 4,
												  ItemStack(manaStone, 1, OreDictionary.WILDCARD_VALUE),
												  ItemStack(manaResource, 4, 5),
												  ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimEssence),
												  ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimEssence))
	}
	
	fun banRetrades() {
		AlfheimAPI.banRetrade(AlfheimRecipes.recipeInterdimensional.output)
		AlfheimAPI.banRetrade(ItemStack(iron_ingot))
		AlfheimAPI.banRetrade(ItemStack(iron_block))
		AlfheimAPI.banRetrade(ItemStack(ender_pearl))
		AlfheimAPI.banRetrade(ItemStack(diamond))
		AlfheimAPI.banRetrade(ItemStack(diamond_block))
	}
	
	fun registerRecipies() {
		val costTier1 = 5200
		val costTier2 = 8000
		val costTier3 = 12000
		
		recipeRealityRune = BotaniaAPI.registerRuneAltarRecipe(ItemStack(elvenResource, 1, ElvenResourcesMetas.PrimalRune), costTier3,
															   RUNE[0], RUNE[1], RUNE[2], RUNE[3], RUNE[8], ItemStack(manaResource, 1, 15), MAUFTRIUM_INGOT)
		recipeMuspelheimRune = BotaniaAPI.registerRuneAltarRecipe(ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimRune), costTier3,
																  RUNE[1], RUNE[2], ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimEssence), ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimEssence), IFFESAL_DUST)
		recipeNiflheimRune = BotaniaAPI.registerRuneAltarRecipe(ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimRune), costTier3,
																RUNE[0], RUNE[3], ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimEssence), ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimEssence), IFFESAL_DUST)
		
		ModRuneRecipes.recipesEarthRune.add(BotaniaAPI.registerRuneAltarRecipe(ItemStack(ModItems.rune, 2, 2), costTier1, MANA_POWDER, MANA_STEEL, ItemStack(livingcobble), ItemStack(obsidian), ItemStack(brown_mushroom)))
		ModRuneRecipes.recipesEarthRune.add(BotaniaAPI.registerRuneAltarRecipe(ItemStack(ModItems.rune, 2, 2), costTier1, MANA_POWDER, MANA_STEEL, ItemStack(livingcobble), ItemStack(obsidian), ItemStack(red_mushroom)))
		
		recipeInterdimensional = BotaniaAPI.registerElvenTradeRecipe(ItemStack(elvenResource, 1, ElvenResourcesMetas.InterdimensionalGatewayCore), ItemStack(nether_star))
		recipeDreamwood = BotaniaAPI.registerPureDaisyRecipe(dreamLog, dreamwood, 0)
		
		BotaniaAPI.registerManaInfusionRecipe(ItemStack(elvenResource, 1, ElvenResourcesMetas.InfusedDreamwoodTwig), ItemStack(manaResource, 1, 13), 10000)
		
		addRecipe(HelmRevealingAlfheimRecipe())
		addRecipe(LootInterceptorRecipe())
		addRecipe(LootInterceptorClearRecipe())
	}
	
	fun init() {
		ModCraftingRecipes.recipeGaiaPylon = recipeGaiaPylon
	}
	
	fun addMMORecipes() {
		CraftingManager.getInstance().recipeList.add(recipePaperBreak)
		CraftingManager.getInstance().recipeList.add(recipePeacePipe)
	}
	
	fun removeMMORecipes() {
		CraftingManager.getInstance().recipeList.remove(AlfheimRecipes.recipePaperBreak)
		CraftingManager.getInstance().recipeList.remove(AlfheimRecipes.recipePeacePipe)
	}
}
