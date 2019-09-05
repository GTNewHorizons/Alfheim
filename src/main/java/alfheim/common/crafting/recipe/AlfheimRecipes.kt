package alfheim.common.crafting.recipe

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.ASJUtilities.addOreDictRecipe
import alexsocol.asjlib.ASJUtilities.addShapelessOreDictRecipe
import alfheim.AlfheimCore
import alfheim.api.*
import alfheim.api.AlfheimAPI.addInfuserRecipe
import alfheim.api.crafting.recipe.RecipeManaInfuser
import alfheim.api.lib.LibOreDict
import alfheim.api.lib.LibOreDict.ARUNE
import alfheim.api.lib.LibOreDict.DYES
import alfheim.api.lib.LibOreDict.ELVORIUM_INGOT
import alfheim.api.lib.LibOreDict.ELVORIUM_NUGGET
import alfheim.api.lib.LibOreDict.IFFESAL_DUST
import alfheim.api.lib.LibOreDict.INFUSED_DREAM_TWIG
import alfheim.api.lib.LibOreDict.MAUFTRIUM_INGOT
import alfheim.api.lib.LibOreDict.MAUFTRIUM_NUGGET
import alfheim.api.lib.LibOreDict.MUSPELHEIM_ESSENCE
import alfheim.api.lib.LibOreDict.MUSPELHEIM_POWER_INGOT
import alfheim.api.lib.LibOreDict.NIFLHEIM_ESSENCE
import alfheim.api.lib.LibOreDict.NIFLHEIM_POWER_INGOT
import alfheim.common.block.AlfheimBlocks.alfStorage
import alfheim.common.block.AlfheimBlocks.alfheimPortal
import alfheim.common.block.AlfheimBlocks.alfheimPylon
import alfheim.common.block.AlfheimBlocks.animatedTorch
import alfheim.common.block.AlfheimBlocks.anyavil
import alfheim.common.block.AlfheimBlocks.dreamLog
import alfheim.common.block.AlfheimBlocks.elvenOres
import alfheim.common.block.AlfheimBlocks.elvenSand
import alfheim.common.block.AlfheimBlocks.livingcobble
import alfheim.common.block.AlfheimBlocks.manaAccelerator
import alfheim.common.block.AlfheimBlocks.manaInfuser
import alfheim.common.block.AlfheimBlocks.tradePortal
import alfheim.common.block.AlfheimFluffBlocks.dreamwoodBarkFence
import alfheim.common.block.AlfheimFluffBlocks.dreamwoodBarkFenceGate
import alfheim.common.block.AlfheimFluffBlocks.dreamwoodFence
import alfheim.common.block.AlfheimFluffBlocks.dreamwoodFenceGate
import alfheim.common.block.AlfheimFluffBlocks.elvenSandstone
import alfheim.common.block.AlfheimFluffBlocks.elvenSandstoneSlab
import alfheim.common.block.AlfheimFluffBlocks.elvenSandstoneStairs
import alfheim.common.block.AlfheimFluffBlocks.livingcobbleSlab
import alfheim.common.block.AlfheimFluffBlocks.livingcobbleStairs
import alfheim.common.block.AlfheimFluffBlocks.livingcobbleWall
import alfheim.common.block.AlfheimFluffBlocks.livingrockBrickWall
import alfheim.common.block.AlfheimFluffBlocks.livingrockTileSlab
import alfheim.common.block.AlfheimFluffBlocks.livingwoodBarkFence
import alfheim.common.block.AlfheimFluffBlocks.livingwoodBarkFenceGate
import alfheim.common.block.AlfheimFluffBlocks.livingwoodFence
import alfheim.common.block.AlfheimFluffBlocks.livingwoodFenceGate
import alfheim.common.block.AlfheimFluffBlocks.shrineGlass
import alfheim.common.block.AlfheimFluffBlocks.shrineLight
import alfheim.common.block.AlfheimFluffBlocks.shrinePanel
import alfheim.common.block.AlfheimFluffBlocks.shrinePillar
import alfheim.common.block.AlfheimFluffBlocks.shrineRock
import alfheim.common.block.AlfheimFluffBlocks.shrineRockWhiteSlab
import alfheim.common.block.AlfheimFluffBlocks.shrineRockWhiteStairs
import alfheim.common.item.AlfheimItems.astrolabe
import alfheim.common.item.AlfheimItems.auraRingElven
import alfheim.common.item.AlfheimItems.auraRingGod
import alfheim.common.item.AlfheimItems.balanceCloak
import alfheim.common.item.AlfheimItems.cloudPendant
import alfheim.common.item.AlfheimItems.cloudPendantSuper
import alfheim.common.item.AlfheimItems.crescentMoonAmulet
import alfheim.common.item.AlfheimItems.dodgeRing
import alfheim.common.item.AlfheimItems.elementalBoots
import alfheim.common.item.AlfheimItems.elementalChestplate
import alfheim.common.item.AlfheimItems.elementalHelmet
import alfheim.common.item.AlfheimItems.elementalHelmetRevealing
import alfheim.common.item.AlfheimItems.elementalLeggings
import alfheim.common.item.AlfheimItems.elementiumHoe
import alfheim.common.item.AlfheimItems.elfFirePendant
import alfheim.common.item.AlfheimItems.elfIcePendant
import alfheim.common.item.AlfheimItems.elvenResource
import alfheim.common.item.AlfheimItems.elvoriumBoots
import alfheim.common.item.AlfheimItems.elvoriumChestplate
import alfheim.common.item.AlfheimItems.elvoriumHelmet
import alfheim.common.item.AlfheimItems.elvoriumHelmetRevealing
import alfheim.common.item.AlfheimItems.elvoriumLeggings
import alfheim.common.item.AlfheimItems.invisibilityCloak
import alfheim.common.item.AlfheimItems.livingrockPickaxe
import alfheim.common.item.AlfheimItems.lootInterceptor
import alfheim.common.item.AlfheimItems.manaRingElven
import alfheim.common.item.AlfheimItems.manaRingGod
import alfheim.common.item.AlfheimItems.manaStone
import alfheim.common.item.AlfheimItems.manaStoneGreater
import alfheim.common.item.AlfheimItems.manasteelHoe
import alfheim.common.item.AlfheimItems.multibauble
import alfheim.common.item.AlfheimItems.paperBreak
import alfheim.common.item.AlfheimItems.peacePipe
import alfheim.common.item.AlfheimItems.pixieAttractor
import alfheim.common.item.AlfheimItems.realitySword
import alfheim.common.item.AlfheimItems.rodFire
import alfheim.common.item.AlfheimItems.rodGrass
import alfheim.common.item.AlfheimItems.rodIce
import alfheim.common.item.AlfheimItems.storyToken
import alfheim.common.item.AlfheimItems.thinkingHand
import alfheim.common.item.equipment.tool.ItemTwigWandExtender
import alfheim.common.item.material.ElvenResourcesMetas
import cpw.mods.fml.common.registry.GameRegistry.*
import net.minecraft.init.Blocks.*
import net.minecraft.init.Items.*
import net.minecraft.item.*
import net.minecraft.item.crafting.*
import net.minecraftforge.oredict.*
import net.minecraftforge.oredict.OreDictionary.WILDCARD_VALUE
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.recipe.*
import vazkii.botania.common.Botania
import vazkii.botania.common.block.ModBlocks.*
import vazkii.botania.common.block.tile.mana.TilePool
import vazkii.botania.common.crafting.*
import vazkii.botania.common.item.ModItems
import vazkii.botania.common.item.ModItems.*
import vazkii.botania.common.lib.LibOreDict.*

object AlfheimRecipes {
	
	lateinit var recipeElvorium: RecipeManaInfuser
	//public static RecipeManaInfuser recipeMuspelheimEssence;
	//public static RecipeManaInfuser recipeNiflheimEssence;
	lateinit var recipeTerrasteel: RecipeManaInfuser
	lateinit var recipeMauftrium: RecipeManaInfuser
	lateinit var recipeManaStone: RecipeManaInfuser
	lateinit var recipeManaStoneGreater: RecipeManaInfuser
	
	lateinit var recipeAlfheimPortal: IRecipe
	lateinit var recipeAnimatedTorch: IRecipe
	lateinit var recipeAnyavil: IRecipe
	lateinit var recipesApothecary: List<IRecipe>
	lateinit var recipeAstrolabe: IRecipe
	lateinit var recipeAuraRingElven: IRecipe
	lateinit var recipeAuraRingGod: IRecipe
	lateinit var recipeBalanceCloak: IRecipe
	lateinit var recipeCloudPendant: IRecipe
	lateinit var recipeCloudPendantSuper: IRecipe
	lateinit var recipeCrescentAmulet: IRecipe
	lateinit var recipeDodgeRing: IRecipe
	lateinit var recipeElementalHelmet: IRecipe
	lateinit var recipeElementalChestplate: IRecipe
	lateinit var recipeElementalLeggings: IRecipe
	lateinit var recipeElementalBoots: IRecipe
	lateinit var recipeElementiumHoe: IRecipe
	lateinit var recipeElvenPylon: IRecipe
	lateinit var recipesElvenWand: List<IRecipe>
	lateinit var recipeElvoriumHelmet: IRecipe
	lateinit var recipeElvoriumChestplate: IRecipe
	lateinit var recipeElvoriumLeggings: IRecipe
	lateinit var recipeElvoriumBoots: IRecipe
	lateinit var recipeElvoriumPylon: IRecipe
	lateinit var recipeFurnace: IRecipe
	lateinit var recipeGaiaPylon: IRecipe
	lateinit var recipeGreenRod: IRecipe
	lateinit var recipeInvisibilityCloak: IRecipe
	lateinit var recipeItemHolder: IRecipe
	lateinit var recipeLensMessenger: IRecipe
	lateinit var recipeLensTripwire: IRecipe
	lateinit var recipeLivingcobble: IRecipe
	lateinit var recipeLivingrockPickaxe: IRecipe
	lateinit var recipeLootInterceptor: IRecipe
	lateinit var recipeManaInfusionCore: IRecipe
	lateinit var recipeManaInfuser: IRecipe
	lateinit var recipeManaRingElven: IRecipe
	lateinit var recipeManaRingGod: IRecipe
	lateinit var recipeManasteelHoe: IRecipe
	lateinit var recipeMultibauble: IRecipe
	lateinit var recipeMuspelheimPendant: IRecipe
	lateinit var recipeMuspelheimPowerIngot: IRecipe
	lateinit var recipeMuspelheimRod: IRecipe
	lateinit var recipeNiflheimPendant: IRecipe
	lateinit var recipeNiflheimPowerIngot: IRecipe
	lateinit var recipeNiflheimRod: IRecipe
	lateinit var recipePaperBreak: IRecipe
	lateinit var recipePeacePipe: IRecipe
	lateinit var recipePixieAttractor: IRecipe
	lateinit var recipeRelicCleaner: IRecipe
	lateinit var recipesSpark: MutableList<IRecipe>
	//lateinit var recipeSpatiotemporal: IRecipe // FIXME
	lateinit var recipeSword: IRecipe
	lateinit var recipeThinkingHand: IRecipe
	lateinit var recipeTradePortal: IRecipe
	
	lateinit var recipeInterdimensional: RecipeElvenTrade
	lateinit var recipeStoryToken: RecipeElvenTrade
	
	lateinit var recipeDreamwood: RecipePureDaisy
	
	lateinit var recipeMuspelheimRune: RecipeRuneAltar
	lateinit var recipeNiflheimRune: RecipeRuneAltar
	lateinit var recipeRealityRune: RecipeRuneAltar
	
	init {
		registerCraftingRecipes()
		registerShapelessRecipes()
		registerSmeltingRecipes()
		registerManaInfusionRecipes()
		registerRecipies()
		banRetrades()
		//if (ModInfo.DEV && FMLCommonHandler.instance().getEffectiveSide().equals(Side.CLIENT)) (new NEIAlfheimConfig()).loadConfig();
	}
	
	private fun registerCraftingRecipes() {
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
			addOreDictRecipe(ItemStack(altar, 1, 9),
							 "SPS", " C ", "CCC",
							 'S', livingcobbleSlab,
							 'P', PETAL[i],
							 'C', ItemStack(livingcobble, 1, 0))
		
		addOreDictRecipe(ItemStack(altar, 1, 9), "SPS", " C ", "CCC", 'S', livingcobbleSlab, 'P', LibOreDict.RAINBOW_PETAL, 'C', ItemStack(livingcobble, 1, 0))
		
		recipesApothecary = BotaniaAPI.getLatestAddedRecipes(17)
		
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
						 'T', INFUSED_DREAM_TWIG,
						 'D', IFFESAL_DUST,
						 'P', elementiumHelm,
						 'M', RUNE[8])
		recipeElementalHelmet = BotaniaAPI.getLatestAddedRecipe()
		
		if (elementalHelmetRevealing != null)
			addOreDictRecipe(ItemStack(elementalHelmetRevealing),
							 "RTR", "DPD", " M ",
							 'R', RUNE[0],
							 'T', INFUSED_DREAM_TWIG,
							 'D', IFFESAL_DUST,
							 'P', elementiumHelmRevealing,
							 'M', RUNE[8])
		
		addOreDictRecipe(ItemStack(elementalChestplate),
						 "RTR", "DPD", " M ",
						 'R', RUNE[2],
						 'T', INFUSED_DREAM_TWIG,
						 'D', IFFESAL_DUST,
						 'P', elementiumChest,
						 'M', RUNE[8])
		recipeElementalChestplate = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elementalLeggings),
						 "RTR", "DPD", " M ",
						 'R', RUNE[1],
						 'T', INFUSED_DREAM_TWIG,
						 'D', IFFESAL_DUST,
						 'P', elementiumLegs,
						 'M', RUNE[8])
		recipeElementalLeggings = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elementalBoots),
						 "RTR", "DPD", " M ",
						 'R', RUNE[3],
						 'T', INFUSED_DREAM_TWIG,
						 'D', IFFESAL_DUST,
						 'P', elementiumBoots,
						 'M', RUNE[8])
		recipeElementalBoots = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elementiumHoe),
						 "EE", " T", " T",
						 'E', ELEMENTIUM,
						 'T', DREAMWOOD_TWIG)
		recipeElementiumHoe = BotaniaAPI.getLatestAddedRecipe()
		
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
		
		addOreDictRecipe(ItemStack(elvenResource, 1, ElvenResourcesMetas.DasRheingold),
						 "SCS", "CGC", "SCS",
						 'G', "ingotGold",
						 'S', LIFE_ESSENCE,
						 'C', spellCloth)
		recipeRelicCleaner = BotaniaAPI.getLatestAddedRecipe()
		
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
		
		addOreDictRecipe(ItemStack(elvoriumChestplate),
						 "TRT", "EPE", "CMC",
						 'T', INFUSED_DREAM_TWIG,
						 'R', ARUNE[0],
						 'E', ELVORIUM_INGOT,
						 'P', terrasteelChest,
						 'C', ItemStack(elvenResource, 1, ElvenResourcesMetas.ManaInfusionCore),
						 'M', MAUFTRIUM_INGOT)
		recipeElvoriumChestplate = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elvoriumLeggings),
						 "TRT", "EPE", "CMC",
						 'T', INFUSED_DREAM_TWIG,
						 'R', ARUNE[0],
						 'E', ELVORIUM_INGOT,
						 'P', terrasteelLegs,
						 'C', ItemStack(elvenResource, 1, ElvenResourcesMetas.ManaInfusionCore),
						 'M', MAUFTRIUM_INGOT)
		recipeElvoriumLeggings = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elvoriumBoots),
						 "TRT", "EPE", "CMC",
						 'T', INFUSED_DREAM_TWIG,
						 'R', ARUNE[0],
						 'E', ELVORIUM_INGOT,
						 'P', terrasteelBoots,
						 'C', ItemStack(elvenResource, 1, ElvenResourcesMetas.ManaInfusionCore),
						 'M', MAUFTRIUM_INGOT)
		recipeElvoriumBoots = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(furnace),
						 "SSS", "S S", "SSS",
						 'S', ItemStack(livingcobble, 1, 0))
		recipeFurnace = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(invisibilityCloak),
						 "PWP", "GWG", "GJG",
						 'P', PRISMARINE_SHARD,
						 'W', ItemStack(wool, 1, 0),
						 'G', manaGlass,
						 'J', MANA_PEARL)
		recipeInvisibilityCloak = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(manaAccelerator),
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
						 'L', ItemStack(livingcobble, 1, 0),
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
						 'S', ItemStack(livingrock, 1, 4))
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
		
		addOreDictRecipe(ItemStack(multibauble),
						 "QT ", "T E", " E ",
						 'E', ELEMENTIUM,
						 'T', TERRA_STEEL,
						 'Q', ItemStack(ModItems.quartz, 1, 5))
		recipeMultibauble = BotaniaAPI.getLatestAddedRecipe()
		
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
		
		ASJUtilities.removeRecipe(ModCraftingRecipes.recipeGaiaPylon.recipeOutput)
		addOreDictRecipe(ItemStack(pylon, 1, 2),
						 " E ", "TPT", " E ",
						 'T', TERRASTEEL_NUGGET,
						 'E', overgrowthSeed,
						 'P', ItemStack(alfheimPylon, 1, 0))
		recipeGaiaPylon = BotaniaAPI.getLatestAddedRecipe()
		
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
		
		/*addOreDictRecipe(ItemStack(spatiotemporalRing),
						 "GES", "E E", "SE ",
						 'G', hourglass,
						 'E', ELEMENTIUM,
						 'S', LIFE_ESSENCE)
		recipeSpatiotemporal = BotaniaAPI.getLatestAddedRecipe()*/ // FIXME
		
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
		
		// ################ DECO BLOCKS ################
		
		addShapedRecipe(ItemStack(elvenSandstone, 1, 0), "SS", "SS", 'S', elvenSand)
		
		addShapedRecipe(ItemStack(elvenSandstone, 1, 1), "S", "S", 'S', elvenSandstoneSlab)
		
		addShapedRecipe(ItemStack(elvenSandstone, 4, 2), "SS", "SS", 'S', ItemStack(elvenSandstone, 1, 0))
		
		addShapedRecipe(ItemStack(elvenSandstone, 4, 3), "SS", "SS", 'S', ItemStack(elvenSandstone, 1, 2))
		
		addOreDictRecipe(ItemStack(elvenSandstoneStairs, 4), true, "S  ", "SS ", "SSS", 'S', ItemStack(elvenSandstone, 1, WILDCARD_VALUE))
		
		addOreDictRecipe(ItemStack(elvenSandstoneSlab, 6), "SSS", 'S', ItemStack(elvenSandstone, 1, WILDCARD_VALUE))
		
		addOreDictRecipe(ItemStack(livingcobbleStairs, 4), true, "L  ", "LL ", "LLL", 'L', ItemStack(livingcobble))
		
		addShapedRecipe(ItemStack(livingcobbleSlab, 6), "LLL", 'L', ItemStack(livingcobble, 1, 0))
		
		addShapedRecipe(ItemStack(livingrockTileSlab, 6), "LLL", 'L', ItemStack(livingcobble, 1, 2))
		
		addShapedRecipe(ItemStack(livingcobbleWall, 6), "LLL", "LLL", 'L', ItemStack(livingcobble))
		
		addShapedRecipe(ItemStack(livingrockBrickWall, 6), "LLL", "LLL", 'L', ItemStack(livingrock, 1, 1))
		
		addShapedRecipe(ItemStack(livingwoodFence, 6), "LLL", "LLL", 'L', ItemStack(livingwood, 1, 1))
		
		addOreDictRecipe(ItemStack(livingwoodFenceGate, 1), "LPL", "LPL", 'L', LIVINGWOOD_TWIG, 'P', ItemStack(livingwood, 1, 1))
		
		addOreDictRecipe(ItemStack(livingwoodBarkFence, 6), "LLL", "LLL", 'L', LIVINGWOOD_TWIG)
		
		addOreDictRecipe(ItemStack(livingwoodBarkFenceGate, 1), "LPL", "LPL", 'L', LIVINGWOOD_TWIG, 'P', ItemStack(livingwood, 1, 0))
		
		addShapedRecipe(ItemStack(dreamwoodFence, 6), "LLL", "LLL", 'L', ItemStack(dreamwood, 1, 1))
		
		addOreDictRecipe(ItemStack(dreamwoodFenceGate, 1), "LPL", "LPL", 'L', DREAMWOOD_TWIG, 'P', ItemStack(dreamwood, 1, 1))
		
		addOreDictRecipe(ItemStack(dreamwoodBarkFence, 6), "LLL", "LLL", 'L', DREAMWOOD_TWIG)
		
		addOreDictRecipe(ItemStack(dreamwoodBarkFenceGate, 1), "LPL", "LPL", 'L', DREAMWOOD_TWIG, 'P', ItemStack(dreamwood, 1, 0))
		
		val out = arrayOf(5, 9, 10, 11, 13)
		for (i in 0..15) {
			if (i in out) continue
			addOreDictRecipe(ItemStack(shrineRock, 8, i),
							 "LLL", "LDL", "LLL",
							 'L', LIVING_ROCK,
							 'D', DYES[i])
		}
		
		addOreDictRecipe(ItemStack(shrineRock, 8, 5),
						 "LLL", "LSL", "LLL",
						 'L', LIVING_ROCK,
						 'S', sugar)
		
		addOreDictRecipe(ItemStack(shrineRock, 8, 9),
						 "LL", "LL",
						 'L', ItemStack(shrineRock, 8, 0))
		
		addOreDictRecipe(ItemStack(shrineRock, 8, 10),
						 "LLL", "LML", "LLL",
						 'L', LIVING_ROCK,
						 'M', ItemStack(mushroom, 1, 0))
		
		addOreDictRecipe(ItemStack(shrineRock, 8, 11),
						 "LLL", "LML", "LLL",
						 'L', LIVING_ROCK,
						 'M', ItemStack(mushroom, 1, 14))
		
		addOreDictRecipe(ItemStack(shrineRock, 8, 13),
						 "LLL", "LDL", "LLL",
						 'L', LIVING_ROCK,
						 'D', DYES[16])
		
		addOreDictRecipe(ItemStack(shrineRockWhiteStairs, 4),
						 "L  ", "LL ", "LLL",
						 'L', ItemStack(shrineRock, 1, 0))
		
		addOreDictRecipe(ItemStack(shrineRockWhiteSlab, 6),
						 "LLL",
						 'L', ItemStack(shrineRock, 1, 0))
		
		for (i in 0..3) {
			addOreDictRecipe(ItemStack(shrineLight, 8, i),
							 "LLL", "LDL", "LLL",
							 'L', "glowstone",
							 'D', DYES[if (i == 0) 14 else i])
		}
		
		addOreDictRecipe(ItemStack(shrinePillar, 2), "S", "S", 'S', ItemStack(shrineRock, 1, 0))
		
		addOreDictRecipe(ItemStack(shrineGlass, 8, 0),
						 "GGG", "GDG", "GGG",
						 'G', elfGlass,
						 'D', DYES[0])
		
		addOreDictRecipe(ItemStack(shrineGlass, 8, 1),
						 "GGG", "GDG", "GGG",
						 'G', elfGlass,
						 'D', DYES[14])
		
		addOreDictRecipe(ItemStack(livingcobble, 4, 1),
						 "LL", "LL",
						 'L', ItemStack(livingcobble, 1, 2))
		
		addOreDictRecipe(ItemStack(livingcobble, 8, 2),
						 "LLL", "L L", "LLL",
						 'L', LIVING_ROCK)
		
		val dyes = arrayOf(4, 1, 14, 11)
		for (i in 0..3) {
			addOreDictRecipe(ItemStack(shrinePanel, 16, i),
							 "GGG", "DDD", "GGG",
							 'G', ItemStack(shrineGlass, 1, 0),
							 'D', DYES[dyes[i]])
		}
	}
	
	private fun registerShapelessRecipes() {
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
		addShapelessRecipe(ItemStack(elvenResource, 9, ElvenResourcesMetas.ElvoriumIngot), ItemStack(alfStorage, 1, 0))
		addShapelessRecipe(ItemStack(elvenResource, 9, ElvenResourcesMetas.MauftriumIngot), ItemStack(alfStorage, 1, 1))
		
		// NNN-NNN-NNN-N!!!
		addOreDictRecipe(ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot), "NNN", "NNN", "NNN", 'N', ELVORIUM_NUGGET)
		addOreDictRecipe(ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot), "NNN", "NNN", "NNN", 'N', MAUFTRIUM_NUGGET)
		addOreDictRecipe(ItemStack(alfStorage, 1, 0), "NNN", "NNN", "NNN", 'N', ELVORIUM_INGOT)
		addOreDictRecipe(ItemStack(alfStorage, 1, 1), "NNN", "NNN", "NNN", 'N', MAUFTRIUM_INGOT)
		
		addShapelessOreDictRecipe(ItemStack(lens, 1, 23), ItemStack(lens, 1, 0), tripwire_hook, ELEMENTIUM)
		recipeLensTripwire = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessOreDictRecipe(ItemStack(livingcobble), LIVING_ROCK)
		recipeLivingcobble = BotaniaAPI.getLatestAddedRecipe()
		
		for (i in 0..5)
			addShapelessOreDictRecipe(ItemStack(manaResource, 4, 5), ItemStack(ancientWill, 1, i))
		
		addShapelessOreDictRecipe(ItemStack(manaRingGod), MAUFTRIUM_INGOT, manaStoneGreater)
		recipeManaRingGod = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessRecipe(ItemStack(brown_mushroom), mushroom)
		addShapelessRecipe(ItemStack(red_mushroom), mushroom)
	}
	
	private fun registerSmeltingRecipes() {
		addSmelting(ItemStack(elvenOres, 1, 1), ItemStack(manaResource, 1, 7), 1.0f)
		addSmelting(ItemStack(elvenOres, 1, 3), ItemStack(gold_ingot, 1, 0), 1.0f)
		addSmelting(elvenSand, ItemStack(elfGlass), 1.0f)
		addSmelting(elvenSandstone, ItemStack(elvenSandstone, 1, 4), 1.0f)
		addSmelting(manaGlass, ItemStack(glass), 0f)
	}
	
	private fun registerManaInfusionRecipes() {
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
												  ItemStack(manaStone, 1, WILDCARD_VALUE),
												  ItemStack(manaResource, 4, 5),
												  ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimEssence),
												  ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimEssence))
	}
	
	private fun banRetrades() {
		AlfheimAPI.banRetrade(recipeInterdimensional.output)
		AlfheimAPI.banRetrade(recipeStoryToken.output)
		AlfheimAPI.banRetrade(ItemStack(iron_ingot))
		AlfheimAPI.banRetrade(ItemStack(iron_block))
		AlfheimAPI.banRetrade(ItemStack(ender_pearl))
		AlfheimAPI.banRetrade(ItemStack(diamond))
		AlfheimAPI.banRetrade(ItemStack(diamond_block))
	}
	
	private fun registerRecipies() {
		val costTier1 = 5200
		val costTier2 = 8000
		val costTier3 = 12000
		
		recipeRealityRune = BotaniaAPI.registerRuneAltarRecipe(ItemStack(elvenResource, 1, ElvenResourcesMetas.PrimalRune), costTier3,
															   RUNE[0], RUNE[1], RUNE[2], RUNE[3], RUNE[8], ItemStack(manaResource, 1, 15), MAUFTRIUM_INGOT)
		recipeMuspelheimRune = BotaniaAPI.registerRuneAltarRecipe(ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimRune), costTier3,
																  RUNE[1], RUNE[2], ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimEssence), ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimEssence), IFFESAL_DUST)
		recipeNiflheimRune = BotaniaAPI.registerRuneAltarRecipe(ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimRune), costTier3,
																RUNE[0], RUNE[3], ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimEssence), ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimEssence), IFFESAL_DUST)
		
		BotaniaAPI.runeAltarRecipes.remove(ModRuneRecipes.recipeSummerRune)
		ModRuneRecipes.recipeSummerRune = BotaniaAPI.registerRuneAltarRecipe(ItemStack(rune, 1, 5), costTier2, RUNE[2], RUNE[3], "sand", "sand", ItemStack(slime_ball), ItemStack(melon))
		
		ModRuneRecipes.recipesEarthRune.add(BotaniaAPI.registerRuneAltarRecipe(ItemStack(rune, 2, 2), costTier1, MANA_POWDER, MANA_STEEL, ItemStack(livingcobble), ItemStack(obsidian), ItemStack(brown_mushroom)))
		ModRuneRecipes.recipesEarthRune.add(BotaniaAPI.registerRuneAltarRecipe(ItemStack(rune, 2, 2), costTier1, MANA_POWDER, MANA_STEEL, ItemStack(livingcobble), ItemStack(obsidian), ItemStack(red_mushroom)))
		
		recipeInterdimensional = BotaniaAPI.registerElvenTradeRecipe(ItemStack(elvenResource, 1, ElvenResourcesMetas.InterdimensionalGatewayCore), ItemStack(nether_star))
		recipeStoryToken = BotaniaAPI.registerElvenTradeRecipe(ItemStack(storyToken, 1, 1), ItemStack(storyToken, 1, 0))
		
		recipeDreamwood = BotaniaAPI.registerPureDaisyRecipe(dreamLog, dreamwood, 0)
		BotaniaAPI.registerPureDaisyRecipe("cobblestone", livingcobble, 0)
		
		BotaniaAPI.registerManaInfusionRecipe(ItemStack(elvenResource, 1, ElvenResourcesMetas.InfusedDreamwoodTwig), ItemStack(manaResource, 1, 13), 10000)
		
		addRecipe(RecipeHelmetElvorium(elvoriumHelmet, terrasteelHelm))
		recipeElvoriumHelmet = BotaniaAPI.getLatestAddedRecipe()
		
		if (Botania.thaumcraftLoaded && elvoriumHelmetRevealing != null) {
			addRecipe(RecipeHelmetElvorium(elvoriumHelmetRevealing!!, terrasteelHelmRevealing))
			addRecipe(RecipeHelmRevealingAlfheim())
		}
		RecipeSorter.register("${ModInfo.MODID}:elvhelm", RecipeHelmetElvorium::class.java, RecipeSorter.Category.SHAPED, "after:forge:shapedore")
		
		addRecipe(RecipeLootInterceptor())
		RecipeSorter.register("${ModInfo.MODID}:looter", RecipeLootInterceptor::class.java, RecipeSorter.Category.SHAPELESS, "")
		addRecipe(RecipeLootInterceptorClear())
		RecipeSorter.register("${ModInfo.MODID}:looterclean", RecipeLootInterceptorClear::class.java, RecipeSorter.Category.SHAPELESS, "")
		addRecipe(RecipeCleanRelic())
		RecipeSorter.register("${ModInfo.MODID}:cleanrelic", RecipeCleanRelic::class.java, RecipeSorter.Category.SHAPELESS, "")
	}
	
	fun postInit() {
		ModCraftingRecipes.recipeGaiaPylon = recipeGaiaPylon
	}
	
	fun addMMORecipes() {
		CraftingManager.getInstance().recipeList.add(recipePaperBreak)
		CraftingManager.getInstance().recipeList.add(recipePeacePipe)
	}
	
	fun removeMMORecipes() {
		CraftingManager.getInstance().recipeList.remove(recipePaperBreak)
		CraftingManager.getInstance().recipeList.remove(recipePeacePipe)
	}
}