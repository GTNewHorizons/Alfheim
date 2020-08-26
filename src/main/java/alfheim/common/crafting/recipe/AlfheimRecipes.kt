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
import alfheim.api.lib.LibOreDict.DREAM_WOOD_LOG
import alfheim.api.lib.LibOreDict.DYES
import alfheim.api.lib.LibOreDict.ELVORIUM_INGOT
import alfheim.api.lib.LibOreDict.ELVORIUM_NUGGET
import alfheim.api.lib.LibOreDict.EMERALD
import alfheim.api.lib.LibOreDict.IFFESAL_DUST
import alfheim.api.lib.LibOreDict.INFUSED_DREAM_TWIG
import alfheim.api.lib.LibOreDict.MAUFTRIUM_INGOT
import alfheim.api.lib.LibOreDict.MAUFTRIUM_NUGGET
import alfheim.api.lib.LibOreDict.MUSPELHEIM_ESSENCE
import alfheim.api.lib.LibOreDict.MUSPELHEIM_POWER_INGOT
import alfheim.api.lib.LibOreDict.NIFLHEIM_ESSENCE
import alfheim.api.lib.LibOreDict.NIFLHEIM_POWER_INGOT
import alfheim.api.lib.LibOreDict.RAINBOW_QUARTZ
import alfheim.api.lib.LibOreDict.TWIG_NETHERWOOD
import alfheim.api.lib.LibOreDict.TWIG_THUNDERWOOD
import alfheim.common.block.AlfheimBlocks.alfStorage
import alfheim.common.block.AlfheimBlocks.alfheimPortal
import alfheim.common.block.AlfheimBlocks.alfheimPylon
import alfheim.common.block.AlfheimBlocks.animatedTorch
import alfheim.common.block.AlfheimBlocks.anyavil
import alfheim.common.block.AlfheimBlocks.barrel
import alfheim.common.block.AlfheimBlocks.corporeaAutocrafter
import alfheim.common.block.AlfheimBlocks.elvenOre
import alfheim.common.block.AlfheimBlocks.elvenSand
import alfheim.common.block.AlfheimBlocks.enderActuator
import alfheim.common.block.AlfheimBlocks.kindling
import alfheim.common.block.AlfheimBlocks.livingcobble
import alfheim.common.block.AlfheimBlocks.manaAccelerator
import alfheim.common.block.AlfheimBlocks.manaInfuser
import alfheim.common.block.AlfheimBlocks.shimmerQuartz
import alfheim.common.block.AlfheimBlocks.tradePortal
import alfheim.common.block.AlfheimFluffBlocks.dreamwoodBarkFence
import alfheim.common.block.AlfheimFluffBlocks.dreamwoodBarkFenceGate
import alfheim.common.block.AlfheimFluffBlocks.dreamwoodFence
import alfheim.common.block.AlfheimFluffBlocks.dreamwoodFenceGate
import alfheim.common.block.AlfheimFluffBlocks.dwarfLantern
import alfheim.common.block.AlfheimFluffBlocks.dwarfPlanks
import alfheim.common.block.AlfheimFluffBlocks.dwarfTrapDoor
import alfheim.common.block.AlfheimFluffBlocks.elfQuartzWall
import alfheim.common.block.AlfheimFluffBlocks.elvenSandstone
import alfheim.common.block.AlfheimFluffBlocks.elvenSandstoneSlab
import alfheim.common.block.AlfheimFluffBlocks.elvenSandstoneSlab2
import alfheim.common.block.AlfheimFluffBlocks.elvenSandstoneStairs
import alfheim.common.block.AlfheimFluffBlocks.elvenSandstoneWalls
import alfheim.common.block.AlfheimFluffBlocks.livingMountain
import alfheim.common.block.AlfheimFluffBlocks.livingcobbleSlab
import alfheim.common.block.AlfheimFluffBlocks.livingcobbleSlab1
import alfheim.common.block.AlfheimFluffBlocks.livingcobbleSlab2
import alfheim.common.block.AlfheimFluffBlocks.livingcobbleStairs
import alfheim.common.block.AlfheimFluffBlocks.livingcobbleStairs1
import alfheim.common.block.AlfheimFluffBlocks.livingcobbleStairs2
import alfheim.common.block.AlfheimFluffBlocks.livingcobbleWall
import alfheim.common.block.AlfheimFluffBlocks.livingrockBrickWall
import alfheim.common.block.AlfheimFluffBlocks.livingrockDark
import alfheim.common.block.AlfheimFluffBlocks.livingrockDarkSlabs
import alfheim.common.block.AlfheimFluffBlocks.livingrockDarkStairs
import alfheim.common.block.AlfheimFluffBlocks.livingrockDarkWalls
import alfheim.common.block.AlfheimFluffBlocks.livingwoodBarkFence
import alfheim.common.block.AlfheimFluffBlocks.livingwoodBarkFenceGate
import alfheim.common.block.AlfheimFluffBlocks.livingwoodFence
import alfheim.common.block.AlfheimFluffBlocks.livingwoodFenceGate
import alfheim.common.block.AlfheimFluffBlocks.roofTile
import alfheim.common.block.AlfheimFluffBlocks.roofTileSlabs
import alfheim.common.block.AlfheimFluffBlocks.roofTileStairs
import alfheim.common.block.AlfheimFluffBlocks.shrineGlass
import alfheim.common.block.AlfheimFluffBlocks.shrineLight
import alfheim.common.block.AlfheimFluffBlocks.shrinePanel
import alfheim.common.block.AlfheimFluffBlocks.shrinePillar
import alfheim.common.block.AlfheimFluffBlocks.shrineRock
import alfheim.common.block.AlfheimFluffBlocks.shrineRockWhiteSlab
import alfheim.common.block.AlfheimFluffBlocks.shrineRockWhiteStairs
import alfheim.common.core.asm.hook.extender.ItemTwigWandExtender
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.integration.tinkersconstruct.TinkersConstructAlfheimConfig
import alfheim.common.item.AlfheimItems.aesirCloak
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
import alfheim.common.item.AlfheimItems.elvenFood
import alfheim.common.item.AlfheimItems.elvenResource
import alfheim.common.item.AlfheimItems.elvoriumBoots
import alfheim.common.item.AlfheimItems.elvoriumChestplate
import alfheim.common.item.AlfheimItems.elvoriumHelmet
import alfheim.common.item.AlfheimItems.elvoriumHelmetRevealing
import alfheim.common.item.AlfheimItems.elvoriumLeggings
import alfheim.common.item.AlfheimItems.enlighter
import alfheim.common.item.AlfheimItems.hyperBucket
import alfheim.common.item.AlfheimItems.invisibilityCloak
import alfheim.common.item.AlfheimItems.livingrockPickaxe
import alfheim.common.item.AlfheimItems.lootInterceptor
import alfheim.common.item.AlfheimItems.manaGlove
import alfheim.common.item.AlfheimItems.manaMirrorImba
import alfheim.common.item.AlfheimItems.manaRingElven
import alfheim.common.item.AlfheimItems.manaRingGod
import alfheim.common.item.AlfheimItems.manaStone
import alfheim.common.item.AlfheimItems.manaStoneGreater
import alfheim.common.item.AlfheimItems.manasteelHoe
import alfheim.common.item.AlfheimItems.multibauble
import alfheim.common.item.AlfheimItems.paperBreak
import alfheim.common.item.AlfheimItems.peacePipe
import alfheim.common.item.AlfheimItems.pendantSuperIce
import alfheim.common.item.AlfheimItems.pixieAttractor
import alfheim.common.item.AlfheimItems.priestCloak
import alfheim.common.item.AlfheimItems.rationBelt
import alfheim.common.item.AlfheimItems.realitySword
import alfheim.common.item.AlfheimItems.ringFeedFlower
import alfheim.common.item.AlfheimItems.ringSpider
import alfheim.common.item.AlfheimItems.rodClicker
import alfheim.common.item.AlfheimItems.rodFire
import alfheim.common.item.AlfheimItems.rodGrass
import alfheim.common.item.AlfheimItems.rodIce
import alfheim.common.item.AlfheimItems.rodPortal
import alfheim.common.item.AlfheimItems.soulHorn
import alfheim.common.item.AlfheimItems.spatiotemporalRing
import alfheim.common.item.AlfheimItems.starPlacer2
import alfheim.common.item.AlfheimItems.thinkingHand
import alfheim.common.item.AlfheimItems.triquetrum
import alfheim.common.item.AlfheimItems.wiltedLotus
import alfheim.common.item.material.ElvenFoodMetas.Lembas
import alfheim.common.item.material.ElvenFoodMetas.Nectar
import alfheim.common.item.material.ElvenResourcesMetas.DasRheingold
import alfheim.common.item.material.ElvenResourcesMetas.ElvoriumIngot
import alfheim.common.item.material.ElvenResourcesMetas.ElvoriumNugget
import alfheim.common.item.material.ElvenResourcesMetas.GrapeLeaf
import alfheim.common.item.material.ElvenResourcesMetas.IffesalDust
import alfheim.common.item.material.ElvenResourcesMetas.InfusedDreamwoodTwig
import alfheim.common.item.material.ElvenResourcesMetas.InterdimensionalGatewayCore
import alfheim.common.item.material.ElvenResourcesMetas.Jug
import alfheim.common.item.material.ElvenResourcesMetas.ManaInfusionCore
import alfheim.common.item.material.ElvenResourcesMetas.MauftriumIngot
import alfheim.common.item.material.ElvenResourcesMetas.MauftriumNugget
import alfheim.common.item.material.ElvenResourcesMetas.MuspelheimEssence
import alfheim.common.item.material.ElvenResourcesMetas.MuspelheimPowerIngot
import alfheim.common.item.material.ElvenResourcesMetas.MuspelheimRune
import alfheim.common.item.material.ElvenResourcesMetas.NiflheimEssence
import alfheim.common.item.material.ElvenResourcesMetas.NiflheimPowerIngot
import alfheim.common.item.material.ElvenResourcesMetas.NiflheimRune
import alfheim.common.item.material.ElvenResourcesMetas.PrimalRune
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
import vazkii.botania.common.block.ModFluffBlocks.*
import vazkii.botania.common.block.tile.mana.TilePool
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.crafting.*
import vazkii.botania.common.item.ModItems.*
import vazkii.botania.common.lib.LibOreDict.*
import net.minecraft.init.Items.dye as justDye
import vazkii.botania.common.item.ModItems.quartz as manaquartz

object AlfheimRecipes {
	
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
	lateinit var recipeAutocrafter: IRecipe
	lateinit var recipeBalanceCloak: IRecipe
	lateinit var recipeBarrel: IRecipe
	lateinit var recipeCleanPylon: IRecipe
	lateinit var recipeCloakThor: IRecipe
	lateinit var recipeCloakSif: IRecipe
	lateinit var recipeCloakNjord: IRecipe
	lateinit var recipeCloakLoki: IRecipe
	lateinit var recipeCloakHeimdall: IRecipe
	lateinit var recipeCloakOdin: IRecipe
	lateinit var recipeCloakAesir: IRecipe
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
	lateinit var recipeElvorium: RecipeManaInfuser
	lateinit var recipeElvoriumHelmet: IRecipe
	lateinit var recipeElvoriumChestplate: IRecipe
	lateinit var recipeElvoriumLeggings: IRecipe
	lateinit var recipeElvoriumBoots: IRecipe
	lateinit var recipeElvoriumPylon: IRecipe
	lateinit var recipeEnderActuator: IRecipe
	lateinit var recipeEnlighter: IRecipe
	lateinit var recipeFurnace: IRecipe
	lateinit var recipeGaiaPylon: IRecipe
	lateinit var recipeGreenRod: IRecipe
	lateinit var recipeHyperBucket: IRecipe
	lateinit var recipeInvisibilityCloak: IRecipe
	lateinit var recipeItemHolder: IRecipe
	lateinit var recipeJug: IRecipe
	lateinit var recipeLembas: IRecipe
	lateinit var recipeLensMessenger: IRecipe
	lateinit var recipeLensPush: IRecipe
	lateinit var recipeLensSmelt: IRecipe
	lateinit var recipeLensSuperconductor: IRecipe
	lateinit var recipeLensTrack: IRecipe
	lateinit var recipeLensTripwire: IRecipe
	lateinit var recipeLivingcobble: IRecipe
	lateinit var recipeLivingrockPickaxe: IRecipe
	lateinit var recipeLootInterceptor: IRecipe
	lateinit var recipeManaInfusionCore: IRecipe
	lateinit var recipeManaInfuser: IRecipe
	lateinit var recipeManaMirrorImba: IRecipe
	lateinit var recipeManaRingElven: IRecipe
	lateinit var recipeManaRingGod: IRecipe
	lateinit var recipeManasteelHoe: IRecipe
	lateinit var recipeManaweaveGlove: IRecipe
	lateinit var recipeMultibauble: IRecipe
	lateinit var recipeMuspelheimPendant: IRecipe
	lateinit var recipeMuspelheimPowerIngot: IRecipe
	lateinit var recipeMuspelheimRod: IRecipe
	lateinit var recipeNiflheimPendant: IRecipe
	lateinit var recipeNiflheimPowerIngot: IRecipe
	lateinit var recipeNiflheimRod: IRecipe
	lateinit var recipePaperBreak: IRecipe
	lateinit var recipePeacePipe: IRecipe
	lateinit var recipePendantSuperIce: IRecipe
	lateinit var recipePixieAttractor: IRecipe
	lateinit var recipeRationBelt: IRecipe
	lateinit var recipeRelicCleaner: IRecipe
	lateinit var recipeRingSpider: IRecipe
	lateinit var recipeRingFeedFlower: IRecipe
	lateinit var recipeRodClicker: IRecipe
	lateinit var recipeRodPortal: IRecipe
	lateinit var recipeSoulHorn: IRecipe
	lateinit var recipesSpark: MutableList<IRecipe>
	lateinit var recipeSpatiotemporal: IRecipe
	lateinit var recipeSuperLavaPendantNew: IRecipe
	lateinit var recipeSword: IRecipe
	lateinit var recipeThinkingHand: IRecipe
	lateinit var recipeTradePortal: IRecipe
	lateinit var recipeTriquetrum: IRecipe
	lateinit var recipeUberSpreader: IRecipe
	
	lateinit var recipeOrechidEndium: RecipePetals
	lateinit var recipePetronia: RecipePetals
	lateinit var recipeRainFlower: RecipePetals
	lateinit var recipeSnowFlower: RecipePetals
	lateinit var recipeStormFlower: RecipePetals
	lateinit var recipeWindFlower: RecipePetals
	
	lateinit var recipeInterdimensional: RecipeElvenTrade
	//lateinit var recipeStoryToken: RecipeElvenTrade
	
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
		ASJUtilities.removeRecipe(ModCraftingRecipes.recipeGaiaPylon.recipeOutput)
		
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
						 'E', EMERALD,
						 'S', LIFE_ESSENCE)
		recipeBalanceCloak = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(barrel),
						 "PSP", "P P", "PPP",
						 'P', ItemStack(planks, 1, 5),
						 'S', ItemStack(wooden_slab, 1, 5))
		recipeBarrel = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(pylon, 1, 2),
						 "EEE", "EPE", "EEE",
						 'E', LIFE_ESSENCE,
						 'P', ItemStack(alfheimPylon, 1, 2))
		recipeCleanPylon = BotaniaAPI.getLatestAddedRecipe()
		
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
		
		addOreDictRecipe(ItemStack(corporeaAutocrafter),
						 " H ", "RSI", " F ",
						 'H', hopper,
						 'R', corporeaRetainer,
						 'I', corporeaInterceptor,
						 'F', corporeaFunnel,
						 'S', corporeaSpark)
		recipeAutocrafter = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(crescentMoonAmulet),
						 "  M", "MS ", "RM ",
						 'M', MAUFTRIUM_NUGGET,
						 'R', RUNE[13],
						 'S', ItemStack(manaResource, 1, 12))
		recipeCrescentAmulet = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(dodgeRing),
						 "EM ", "M M", " MR",
						 'E', EMERALD,
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
		
		if (Botania.thaumcraftLoaded)
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
		
		addOreDictRecipe(ItemStack(elvenResource, 1, DasRheingold),
						 "SCS", "CGC", "SCS",
						 'G', "ingotGold",
						 'S', LIFE_ESSENCE,
						 'C', spellCloth)
		recipeRelicCleaner = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elvenResource, 1, ManaInfusionCore),
						 "PGP", "GDG", "PGP",
						 'D', PIXIE_DUST,
						 'G', "ingotGold",
						 'P', IFFESAL_DUST)
		recipeManaInfusionCore = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elvenResource, 1, MuspelheimPowerIngot),
						 " S ", "SIS", " S ",
						 'S', MUSPELHEIM_ESSENCE,
						 'I', ELVORIUM_INGOT)
		recipeMuspelheimPowerIngot = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elvenResource, 1, NiflheimPowerIngot),
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
						 'C', ItemStack(elvenResource, 1, ManaInfusionCore),
						 'M', MAUFTRIUM_INGOT)
		recipeElvoriumChestplate = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elvoriumLeggings),
						 "TRT", "EPE", "CMC",
						 'T', INFUSED_DREAM_TWIG,
						 'R', ARUNE[0],
						 'E', ELVORIUM_INGOT,
						 'P', terrasteelLegs,
						 'C', ItemStack(elvenResource, 1, ManaInfusionCore),
						 'M', MAUFTRIUM_INGOT)
		recipeElvoriumLeggings = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elvoriumBoots),
						 "TRT", "EPE", "CMC",
						 'T', INFUSED_DREAM_TWIG,
						 'R', ARUNE[0],
						 'E', ELVORIUM_INGOT,
						 'P', terrasteelBoots,
						 'C', ItemStack(elvenResource, 1, ManaInfusionCore),
						 'M', MAUFTRIUM_INGOT)
		recipeElvoriumBoots = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(enderActuator),
						 "PBE", "BCB", "ELP",
						 'P', MANA_PEARL,
						 'B', DYES[15],
						 'E', ender_eye,
						 'L', ItemStack(wiltedLotus, 1, 1),
						 'C', ender_chest)
		recipeEnderActuator = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(enlighter),
						 "GGG", "GSG", "EEE",
						 'G', managlassPane,
						 'S', starPlacer2,
						 'E', ELVORIUM_NUGGET)
		recipeEnlighter = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(furnace, 1, 8),
						 "SSS", "S S", "SSS",
						 'S', ItemStack(livingcobble, 1, 0))
		recipeFurnace = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(hyperBucket),
						 "III", "EBE", "MMM",
						 'B', openBucket,
						 'E', ELVORIUM_INGOT,
						 'I', IFFESAL_DUST,
						 'M', MAUFTRIUM_NUGGET)
		recipeHyperBucket = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(invisibilityCloak),
						 "PWP", "GWG", "GJG",
						 'P', PRISMARINE_SHARD,
						 'W', ItemStack(wool, 1, 0),
						 'G', manaGlass,
						 'J', MANA_PEARL)
		recipeInvisibilityCloak = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elvenResource, 1, Jug),
						 "B B", "B B", " B ",
						 'B', brick)
		recipeJug = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(lens, 1, 22),
						 " P ", "PLP", " P ",
						 'P', paper,
						 'L', ItemStack(lens, 1, 0))
		recipeLensMessenger = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(lens, 1, 26),
						 "IWI", "RLR", "IWI",
						 'L', ItemStack(lens),
						 'W', RUNE[0], // water
						 'R', RUNE[13], // wrath
						 'I', IFFESAL_DUST)
		recipeLensSuperconductor = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(elvenFood, 1, Lembas),
						 " LB", "NBN", "BL ",
						 'N', ItemStack(elvenFood, 1, Nectar),
						 'L', ItemStack(elvenResource, 1, GrapeLeaf),
						 'B', bread)
		recipeLembas = BotaniaAPI.getLatestAddedRecipe()
		
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
		
		addOreDictRecipe(ItemStack(manaAccelerator),
						 "MLM", "LDL",
						 'D', MANA_DIAMOND,
						 'L', LIVING_ROCK,
						 'M', MANA_PEARL)
		recipeItemHolder = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(manaInfuser),
						 "DCD", "IRI", "SSS",
						 'C', ItemStack(elvenResource, 1, ManaInfusionCore),
						 'D', DRAGONSTONE,
						 'I', ELEMENTIUM,
						 'R', rainbowRod,
						 'S', ItemStack(livingrock, 1, 4))
		recipeManaInfuser = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(manaMirrorImba),
						 "IMI", "EWE", "IMI",
						 'M', MAUFTRIUM_INGOT,
						 'E', ELVORIUM_INGOT,
						 'I', IFFESAL_DUST,
						 'W', ItemStack(lens, 1, 18))
		recipeManaMirrorImba = BotaniaAPI.getLatestAddedRecipe()
		
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
		
		addOreDictRecipe(ItemStack(manaGlove),
						 "MM ", "MPM", " M ",
						 'M', MANAWEAVE_CLOTH,
						 'P', MANA_PEARL)
		recipeManaweaveGlove = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(multibauble),
						 "QT ", "T E", " E ",
						 'E', ELEMENTIUM,
						 'T', TERRA_STEEL,
						 'Q', ItemStack(manaquartz, 1, 5))
		recipeMultibauble = BotaniaAPI.getLatestAddedRecipe()
		
		recipePaperBreak = ShapelessOreRecipe(ItemStack(paperBreak, 4), leather, ItemStack(wooden_sword, 1, WILDCARD_VALUE))
		
		recipePeacePipe = ShapedOreRecipe(ItemStack(peacePipe),
										  "  P", " SD", "S  ",
										  'S', stick,
										  'D', DYES[1],
										  'P', ItemStack(planks, 1, 5))
		
		if (AlfheimConfigHandler.enableMMO) addMMORecipes()
		
		ASJUtilities.removeRecipe(superLavaPendant)
		addOreDictRecipe(ItemStack(superLavaPendant),
						 "MMM", "MPM", "ISI",
						 'M', blaze_rod,
						 'P', lavaPendant,
						 'I', nether_brick,
						 'S', MUSPELHEIM_ESSENCE)
		recipeSuperLavaPendantNew = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(pendantSuperIce),
						 "MMM", "MPM", "ISI",
						 'M', MANA_STEEL,
						 'P', icePendant,
						 'I', packed_ice,
						 'S', NIFLHEIM_ESSENCE)
		recipePendantSuperIce = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(pixieAttractor),
						 "EDE", "EPE", " S ",
						 'D', DRAGONSTONE,
						 'E', ELEMENTIUM,
						 'P', PIXIE_DUST,
						 'S', RUNE[2])
		recipePixieAttractor = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(priestCloak, 1, 0),
						 "WGW", "TWT", "ITI",
						 'W', ItemStack(wool, 1, 15),
						 'G', ItemStack(wool, 1, 5),
						 'T', TERRASTEEL_NUGGET,
						 'I', TWIG_THUNDERWOOD)
		recipeCloakThor = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(priestCloak, 1, 1),
						 "WGW", "NLN", "TOT",
						 'W', ItemStack(wool, 1, 15),
						 'G', ItemStack(wool, 1, 4),
						 'N', "nuggetGold",
						 'L', ItemStack(livingwood, 1, 5),
						 'O', overgrowthSeed,
						 'T', LIVINGWOOD_TWIG)
		recipeCloakSif = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(priestCloak, 1, 2),
						 "WGW", "MPM", "TST",
						 'W', ItemStack(wool, 1, 0),
						 'G', ItemStack(wool, 1, 3),
						 'P', PRISMARINE_SHARD,
						 'M', MANASTEEL_NUGGET,
						 'S', ItemStack(potionitem, 1, 16418),
						 'T', INFUSED_DREAM_TWIG)
		recipeCloakNjord = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(priestCloak, 1, 3),
						 "WGW", "NKN", "TNT",
						 'W', ItemStack(wool, 1, 15),
						 'G', ItemStack(wool, 1, 1),
						 'N', "nuggetGold",
						 'K', kindling,
						 'T', TWIG_NETHERWOOD)
		recipeCloakLoki = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(priestCloak, 1, 4),
						 "RGB", "QPQ", "IQI",
						 'R', ItemStack(wool, 1, 14),
						 'G', ItemStack(wool, 1, 13),
						 'B', ItemStack(wool, 1, 11),
						 'P', ender_eye,
						 'Q', RAINBOW_QUARTZ,
						 'I', bifrostPerm)
		recipeCloakHeimdall = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(priestCloak, 1, 5),
						 "WWW", "MGM", "IMI",
						 'W', ItemStack(wool, 1, 14),
						 'G', ItemStack(elvenResource, 1, DasRheingold),
						 'M', MAUFTRIUM_NUGGET,
						 'I', IFFESAL_DUST)
		recipeCloakOdin = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(aesirCloak),
						 "TSN", "LHO",
						 'T', ItemStack(priestCloak, 1, 0),
						 'S', ItemStack(priestCloak, 1, 1),
						 'N', ItemStack(priestCloak, 1, 2), // TODO remove - not AEsir
						 'L', ItemStack(priestCloak, 1, 3),
						 'H', ItemStack(priestCloak, 1, 4),
						 'O', ItemStack(priestCloak, 1, 5))
		recipeCloakAesir = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(pylon, 1, 2),
						 " E ", "TPT", " E ",
						 'T', TERRASTEEL_NUGGET,
						 'E', overgrowthSeed,
						 'P', ItemStack(alfheimPylon, 1, 0))
		recipeGaiaPylon = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(rationBelt),
						 "GL ", "L L", "ELS",
						 'G', RUNE[10],
						 'L', leather,
						 'E', ELEMENTIUM,
						 'S', RUNE[12])
		recipeRationBelt = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(realitySword),
						 " M ", "MRM", " S ",
						 'M', MAUFTRIUM_INGOT,
						 'R', ARUNE[0],
						 'S', ItemStack(manaResource, 1, 3))
		recipeSword = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(ringFeedFlower),
						 "D  ", " R ", "  P",
						 'D', distributor,
						 'R', manaRing,
						 'P', pump)
		recipeRingFeedFlower = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(ringSpider),
						 "RMW", "M M", "WM ",
						 'R', RUNE[11], // greed
						 'M', MANA_STEEL,
						 'W', web)
		recipeRingSpider = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(rodClicker),
						 "GEG", " TE", "T G",
						 'G', DRAGONSTONE,
						 'T', INFUSED_DREAM_TWIG,
						 'E', ELEMENTIUM_NUGGET)
		recipeRodClicker = BotaniaAPI.getLatestAddedRecipe()
		
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
		
		addOreDictRecipe(ItemStack(rodPortal),
						 "IER", " FN", "B I",
						 'E', end_stone,
						 'R', ARUNE[0],
						 'F', rainbowRod,
						 'N', netherrack,
						 'I', IFFESAL_DUST,
						 'B', blaze_rod)
		recipeRodPortal = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(soulHorn),
						 "MIM", "EIE", " E ",
						 'M', MAUFTRIUM_INGOT,
						 'E', ELVORIUM_INGOT,
						 'I', IFFESAL_DUST)
		recipeSoulHorn = BotaniaAPI.getLatestAddedRecipe()
		
		for (i in 0..15)
			addOreDictRecipe(ItemStack(spark),
							 " P ", "BNB", " P ",
							 'B', PIXIE_DUST,
							 'P', PETAL[i],
							 'N', "nuggetGold")
		recipesSpark = BotaniaAPI.getLatestAddedRecipes(16)
		recipesSpark.addAll(ModCraftingRecipes.recipesSpark)
		
		addOreDictRecipe(ItemStack(spatiotemporalRing),
						 "GES", "E E", "SE ",
						 'G', hourglass,
						 'E', ELEMENTIUM,
						 'S', LIFE_ESSENCE)
		recipeSpatiotemporal = BotaniaAPI.getLatestAddedRecipe()
		
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
		
		addOreDictRecipe(ItemStack(triquetrum),
						 "NLN", " NL", " II",
						 'N', TERRASTEEL_NUGGET,
						 'L', LIVINGWOOD_TWIG,
						 'I', TERRA_STEEL)
		
		recipeTriquetrum = BotaniaAPI.getLatestAddedRecipe()
		
		val s = AlfheimCore.stupidMode
		
		// if no TiC || if Avaritia loaded || if molten Mauftrium is disabled
		if (!AlfheimCore.TiCLoaded || s || AlfheimConfigHandler.materialIDs[TinkersConstructAlfheimConfig.MAUFTRIUM] == -1) {
			addOreDictRecipe(ItemStack(spreader, 1, 4),
							 "MMM", "ESI", "MMM",
							 'M', if (s) ItemStack(alfStorage, 1, 1) else MAUFTRIUM_INGOT,
							 'E', if (s) ItemStack(alfStorage, 1, 0) else ELVORIUM_INGOT,
							 'S', ItemStack(spreader, 1, 3),
							 'I', if (s) ItemStack(elvenResource, 1, ManaInfusionCore) else IFFESAL_DUST)
			recipeUberSpreader = BotaniaAPI.getLatestAddedRecipe()
		}
		
		// #############################################################################################################
		// ################################################ DECO BLOCKS ################################################
		// #############################################################################################################
		
		addShapedRecipe(ItemStack(elvenSandstone, 1, 0), "SS", "SS", 'S', elvenSand)
		
		//addShapedRecipe(ItemStack(elvenSandstone, 1, 0), "S", "S", 'S', elvenSandstoneSlab)
		
		addShapedRecipe(ItemStack(elvenSandstone, 2, 1), "S", "S", 'S', elvenSandstoneSlab)
		
		//addShapedRecipe(ItemStack(elvenSandstone, 1, 2), "S", "S", 'S', elvenSandstoneSlab2)
		
		addShapedRecipe(ItemStack(elvenSandstone, 4, 2), "SS", "SS", 'S', ItemStack(elvenSandstone, 1, 0))
		
		addShapedRecipe(ItemStack(elvenSandstone, 4, 3), "SS", "SS", 'S', ItemStack(elvenSandstone, 1, 2))
		
		addOreDictRecipe(ItemStack(elvenSandstoneStairs[0], 4), true, "S  ", "SS ", "SSS", 'S', ItemStack(elvenSandstone, 1, 0))
		
		addOreDictRecipe(ItemStack(elvenSandstoneStairs[1], 4), true, "S  ", "SS ", "SSS", 'S', ItemStack(elvenSandstone, 1, 2))
		
		addOreDictRecipe(ItemStack(elvenSandstoneSlab, 6), "SSS", 'S', ItemStack(elvenSandstone, 1, 0))
		
		addOreDictRecipe(ItemStack(elvenSandstoneSlab2, 6), "SSS", 'S', ItemStack(elvenSandstone, 1, 2))
		
		addOreDictRecipe(ItemStack(elvenSandstoneWalls[0], 6), "SSS", "SSS", 'S', ItemStack(elvenSandstone, 1, 0))
		
		addOreDictRecipe(ItemStack(elvenSandstoneWalls[1], 6), "SSS", "SSS", 'S', ItemStack(elvenSandstone, 1, 2))
		
		addOreDictRecipe(ItemStack(livingcobbleStairs, 4), true, "L  ", "LL ", "LLL", 'L', ItemStack(livingcobble))
		
		addOreDictRecipe(ItemStack(livingcobbleStairs1, 4), true, "L  ", "LL ", "LLL", 'L', ItemStack(livingcobble, 1, 1))
		
		addOreDictRecipe(ItemStack(livingcobbleStairs2, 4), true, "L  ", "LL ", "LLL", 'L', ItemStack(livingcobble, 1, 2))
		
		addShapedRecipe(ItemStack(livingcobbleSlab, 6), "LLL", 'L', ItemStack(livingcobble, 1, 0))
		
		addShapedRecipe(ItemStack(livingcobbleSlab1, 6), "LLL", 'L', ItemStack(livingcobble, 1, 1))
		
		addShapedRecipe(ItemStack(livingcobbleSlab2, 6), "LLL", 'L', ItemStack(livingcobble, 1, 2))
		
		addShapedRecipe(ItemStack(livingcobble, 1, 0), "L", "L", 'L', ItemStack(livingcobbleSlab))
		
		addShapedRecipe(ItemStack(livingcobble, 1, 1), "L", "L", 'L', ItemStack(livingcobbleSlab1))
		
		addShapedRecipe(ItemStack(livingcobble, 1, 2), "L", "L", 'L', ItemStack(livingcobbleSlab2))
		
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
		
		addOreDictRecipe(ItemStack(dwarfPlanks, 4), " P ", "PMP", " P ", 'P', ItemStack(planks, 1, 5), 'M', MANA_POWDER)
		
		addOreDictRecipe(ItemStack(dwarfLantern, 8),
						 "LCL", "CSC", "LCL",
						 'L', ItemStack(livingrock, 1, 1),
						 'C', ItemStack(livingrock, 1, 4),
						 'S', ItemStack(shrineLight, 1, 1))
		
		for (i in (0..15) - 5 - 9 - 10 - 11 - 13) {
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
		
		// ################################################################
		
		addShapedRecipe(ItemStack(livingrockDark, 4, 1),
						"LL", "LL",
						'L', ItemStack(livingrockDark))
		
		addShapedRecipe(ItemStack(livingrockDark, 4, 2),
						"LL", "LL",
						'L', ItemStack(livingrockDark, 1, 1))
		
		addShapedRecipe(ItemStack(livingrockDark, 4, 3),
						"LL", "LL",
						'L', ItemStack(livingrockDark, 1, 2))
		
		addShapedRecipe(ItemStack(livingrockDark),
						"L", "L",
						'L', ItemStack(livingrockDarkSlabs[0]))
		
		addShapedRecipe(ItemStack(livingrockDark, 1, 1),
						"L", "L",
						'L', ItemStack(livingrockDarkSlabs[1]))
		
		addShapedRecipe(ItemStack(livingrockDark, 1, 3),
						"L", "L",
						'L', ItemStack(livingrockDarkSlabs[2]))
		
		addShapedRecipe(ItemStack(livingrockDarkStairs[0], 4),
						"L  ", "LL ", "LLL",
						'L', ItemStack(livingrockDark))
		
		addShapedRecipe(ItemStack(livingrockDarkStairs[1], 4),
						"L  ", "LL ", "LLL",
						'L', ItemStack(livingrockDark, 1, 1))
		
		addShapedRecipe(ItemStack(livingrockDarkStairs[2], 4),
						"L  ", "LL ", "LLL",
						'L', ItemStack(livingrockDark, 1, 3))
		
		addShapedRecipe(ItemStack(livingrockDarkSlabs[0], 6),
						"LLL",
						'L', ItemStack(livingrockDark))
		
		addShapedRecipe(ItemStack(livingrockDarkSlabs[1], 6),
						"LLL",
						'L', ItemStack(livingrockDark, 1, 1))
		
		addShapedRecipe(ItemStack(livingrockDarkSlabs[2], 6),
						"LLL",
						'L', ItemStack(livingrockDark, 1, 3))
		
		addShapedRecipe(ItemStack(livingrockDarkWalls[0], 6),
						"LLL", "LLL",
						'L', ItemStack(livingrockDark))
		
		addShapedRecipe(ItemStack(livingrockDarkWalls[1], 6),
						"LLL", "LLL",
						'L', ItemStack(livingrockDark, 1, 1))
		
		// ################################################################
		
		addShapedRecipe(ItemStack(shrineRockWhiteStairs, 4),
						"L  ", "LL ", "LLL",
						'L', ItemStack(shrineRock, 1, 0))
		
		addShapedRecipe(ItemStack(shrineRockWhiteSlab, 6),
						"LLL",
						'L', ItemStack(shrineRock, 1, 0))
		
		for (i in 0..5) {
			addOreDictRecipe(ItemStack(shrineLight, 8, i),
							 "LLL", "LDL", "LLL",
							 'L', "glowstone",
							 'D', DYES[if (i == 0) 14 else i])
		}
		
		addShapedRecipe(ItemStack(shrinePillar, 2), "S", "S", 'S', ItemStack(shrineRock, 1, 0))
		
		addOreDictRecipe(ItemStack(shrineGlass, 8, 0),
						 "GGG", "GDG", "GGG",
						 'G', elfGlass,
						 'D', DYES[0])
		
		addOreDictRecipe(ItemStack(shrineGlass, 8, 1),
						 "GGG", "GDG", "GGG",
						 'G', elfGlass,
						 'D', DYES[14])
		
		addOreDictRecipe(ItemStack(shrineGlass, 8, 2),
						 "GGG", "GDG", "GGG",
						 'G', ItemStack(shrineGlass, 8, 0),
						 'D', DYES[9])
		
		addOreDictRecipe(ItemStack(shrineGlass, 8, 3),
						 "GGG", "GDG", "GGG",
						 'G', ItemStack(shrineGlass, 8, 0),
						 'D', DYES[5])
		
		addOreDictRecipe(ItemStack(shrineGlass, 8, 4),
						 "GGG", "GDG", "GGG",
						 'G', ItemStack(shrineGlass, 8, 0),
						 'D', DYES[14])
		
		addShapedRecipe(ItemStack(livingcobble, 4, 1),
						"LL", "LL",
						'L', ItemStack(livingcobble, 1, 2))
		
		addOreDictRecipe(ItemStack(livingcobble, 8, 2),
						 "LLL", "L L", "LLL",
						 'L', LIVING_ROCK)
		
		addShapelessOreDictRecipe(ItemStack(livingcobble, 1, 3), ItemStack(livingcobble), vineBall)
		
		addShapedRecipe(ItemStack(livingMountain, 9),
						"CRC", "RCR", "CRC",
						'C', ItemStack(livingcobble),
						'R', ItemStack(livingrock))
		
		addShapelessOreDictRecipe(ItemStack(roofTile), ItemStack(customBrick, 1, 3), DYES[10], DYES[7])
		addShapelessOreDictRecipe(ItemStack(roofTile, 1, 1), ItemStack(customBrick, 1, 3), DYES[13], DYES[11], DYES[7])
		addShapelessOreDictRecipe(ItemStack(roofTile, 1, 2), ItemStack(customBrick, 1, 3), DYES[13])
		
		roofTileSlabs.forEachIndexed { meta, slab ->
			addShapedRecipe(ItemStack(slab, 6), "RRR", 'R', ItemStack(roofTile, 1, meta))
			addShapedRecipe(ItemStack(roofTile, 1, meta), "R", "R", 'R', ItemStack(slab))
		}
		
		roofTileStairs.forEachIndexed { meta, stair ->
			addShapedRecipe(ItemStack(stair, 4), "R  ", "RR ", "RRR", 'R', ItemStack(roofTile, 1, meta))
		}
		
		val dyes = arrayOf(4, 1, 14, 11)
		for (i in 0..3) {
			addOreDictRecipe(ItemStack(shrinePanel, 16, i),
							 "GGG", "DDD", "GGG",
							 'G', ItemStack(shrineGlass, 1, 0),
							 'D', DYES[dyes[i]])
		}
		
		addShapedRecipe(ItemStack(dwarfTrapDoor),
						"WWW", "WWW",
						'W', ItemStack(dwarfPlanks))
		
		val quartzs = arrayOf(quartz_block, blazeQuartz, darkQuartz, elfQuartz, lavenderQuartz, manaQuartz, redQuartz, sunnyQuartz, shimmerQuartz)
		
		for (q in quartzs) {
			addShapelessOreDictRecipe(ItemStack(q, 1, 5), ItemStack(q), if (q === darkQuartz) DYES[0] else DYES[15])
			addOreDictRecipe(ItemStack(q, 4, 6), "QQ", "QQ", 'Q', ItemStack(q))
		}
		
		addShapelessOreDictRecipe(ItemStack(elfQuartz, 1, 1), ItemStack(elfQuartz, 1, 7))
		addShapelessOreDictRecipe(ItemStack(elfQuartz, 1, 7), ItemStack(elfQuartz, 1, 1))
		addOreDictRecipe(ItemStack(elfQuartz, 2, 8), "S", "P", "S", 'S', elfQuartzSlab, 'P', ItemStack(elfQuartz, 1, 9))
		addShapelessOreDictRecipe(ItemStack(elfQuartz, 1, 9), ItemStack(elfQuartz))
		addOreDictRecipe(ItemStack(elfQuartz, 1, 10), "PP", "PP", 'P', ItemStack(elfQuartz, 1, 9))
		addOreDictRecipe(ItemStack(elfQuartzWall, 16), "QQQ", "QQQ", 'Q', ItemStack(elfQuartz))
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
		
		addShapelessOreDictRecipe(ItemStack(elvenResource, 9, ElvoriumNugget), ELVORIUM_INGOT)
		addShapelessOreDictRecipe(ItemStack(elvenResource, 9, MauftriumNugget), MAUFTRIUM_INGOT)
		
		addShapelessRecipe(ItemStack(elvenResource, 9, ElvoriumIngot), ItemStack(alfStorage, 1, 0))
		addShapelessRecipe(ItemStack(elvenResource, 9, MauftriumIngot), ItemStack(alfStorage, 1, 1))
		addShapelessRecipe(ItemStack(elvenResource, 9, MuspelheimPowerIngot), ItemStack(alfStorage, 1, 2))
		addShapelessRecipe(ItemStack(elvenResource, 9, NiflheimPowerIngot), ItemStack(alfStorage, 1, 3))
		
		addShapelessOreDictRecipe(ItemStack(elvenResource, 1, ElvoriumIngot), *Array(9) { ELVORIUM_NUGGET })
		addShapelessOreDictRecipe(ItemStack(elvenResource, 1, MauftriumIngot), *Array(9) { MAUFTRIUM_NUGGET })
		
		addShapelessOreDictRecipe(ItemStack(alfStorage, 1, 0), *Array(9) { ELVORIUM_INGOT })
		addShapelessOreDictRecipe(ItemStack(alfStorage, 1, 1), *Array(9) { MAUFTRIUM_INGOT })
		addShapelessOreDictRecipe(ItemStack(alfStorage, 1, 2), *Array(9) { MUSPELHEIM_POWER_INGOT })
		addShapelessOreDictRecipe(ItemStack(alfStorage, 1, 3), *Array(9) { NIFLHEIM_POWER_INGOT })
		
		for (i in 0..5) {
			val enh: Any = if (i < 3) MAUFTRIUM_INGOT else ItemStack(alfStorage, 1, 1)
			addShapelessOreDictRecipe(ItemStack(hyperBucket, 1, i + 1), ItemStack(hyperBucket, 1, i), enh)
		}
		
		addShapelessOreDictRecipe(ItemStack(lens, 1, 23), ItemStack(lens, 1, 0), tripwire_hook, ELEMENTIUM)
		recipeLensTripwire = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessOreDictRecipe(ItemStack(lens, 1, 24), ItemStack(lens, 1, 0), RUNE[2], MANA_POWDER)
		recipeLensPush = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessOreDictRecipe(ItemStack(lens, 1, 25), ItemStack(lens, 1, 0), RUNE[1], MANA_POWDER)
		recipeLensSmelt = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessOreDictRecipe(ItemStack(lens, 1, 27), ItemStack(lens, 1, 0), RUNE[11], MANA_POWDER)
		recipeLensTrack = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessOreDictRecipe(ItemStack(livingcobble), LIVING_ROCK)
		recipeLivingcobble = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessOreDictRecipe(ItemStack(livingrockDark), livingrock, "coal")
		addShapelessOreDictRecipe(ItemStack(livingrockDark, 1, 1), ItemStack(livingrock, 1, 1), "coal")
		addShapelessOreDictRecipe(ItemStack(livingrockDark, 1, 2), ItemStack(livingrock, 1, 4), "coal")
		
		addShapelessOreDictRecipe(ItemStack(livingrockDarkStairs[0]), ItemStack(livingrockStairs), "coal")
		addShapelessOreDictRecipe(ItemStack(livingrockDarkStairs[1]), ItemStack(livingrockBrickStairs), "coal")
		
		addShapelessOreDictRecipe(ItemStack(livingrockDarkSlabs[0]), ItemStack(livingrockSlab), "coal")
		addShapelessOreDictRecipe(ItemStack(livingrockDarkSlabs[1]), ItemStack(livingrockBrickSlab), "coal")
		
		addShapelessOreDictRecipe(ItemStack(livingrockDarkWalls[0]), ItemStack(livingrockWall), "coal")
		addShapelessOreDictRecipe(ItemStack(livingrockDarkWalls[1]), ItemStack(livingrockBrickWall), "coal")
		
		for (i in 0..5)
			addShapelessOreDictRecipe(ItemStack(manaResource, 4, 5), ItemStack(ancientWill, 1, i))
		
		addShapelessOreDictRecipe(ItemStack(manaRingGod), MAUFTRIUM_INGOT, manaStoneGreater)
		recipeManaRingGod = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessRecipe(ItemStack(brown_mushroom), mushroom)
		addShapelessRecipe(ItemStack(red_mushroom), mushroom)
	}
	
	private fun registerSmeltingRecipes() {
		addSmelting(ItemStack(elvenOre, 1, 0), ItemStack(manaResource, 1, 9), 1f)
		addSmelting(ItemStack(elvenOre, 1, 1), ItemStack(manaResource, 1, 7), 1f)
		addSmelting(ItemStack(elvenOre, 1, 2), ItemStack(manaquartz, 1, 5), 1f)
		addSmelting(ItemStack(elvenOre, 1, 3), ItemStack(gold_ingot, 1, 0), 1f)
		addSmelting(ItemStack(elvenOre, 1, 4), ItemStack(elvenResource, 1, IffesalDust), 1f)
		addSmelting(ItemStack(elvenOre, 1, 5), ItemStack(justDye, 1, 4), 0.2f)
		
		addSmelting(elvenSand, ItemStack(elfGlass), 1f)
		addSmelting(elvenSandstone, ItemStack(elvenSandstone, 1, 4), 1f)
		addSmelting(manaGlass, ItemStack(glass), 0f)
	}
	
	private fun registerManaInfusionRecipes() {
		// Why is this here?
		/*addRecipe(new ItemStack(elfGlass), 100,
			new ItemStack[] {new ItemStack(Modquartz, 1, 5), new ItemStack(elvenGlass)});*/
		
		/*recipeMuspelheimEssence = addInfuserRecipe(new ItemStack(elvenResource, 1, MuspelheimEssence),
			TilePool.MAX_MANA / 10,
			LIFE_ESSENCE,
			new ItemStack(lava_bucket, 1, 0));
		
		recipeNiflheimEssence = addInfuserRecipe(new ItemStack(elvenResource, 1, NiflheimEssence),
			TilePool.MAX_MANA / 10,
			LIFE_ESSENCE,
			new ItemStack(ice, 1, 0));*/
		
		recipeTerrasteel = addInfuserRecipe(ItemStack(manaResource, 1, 4),
											TilePool.MAX_MANA / 2,
											MANA_STEEL,
											MANA_PEARL,
											MANA_DIAMOND)
		
		recipeElvorium = addInfuserRecipe(ItemStack(elvenResource, 1, ElvoriumIngot),
										  TilePool.MAX_MANA / 2,
										  ELEMENTIUM,
										  PIXIE_DUST,
										  DRAGONSTONE)
		
		recipeMauftrium = addInfuserRecipe(ItemStack(elvenResource, 1, MauftriumIngot),
										   TilePool.MAX_MANA,
										   GAIA_INGOT,
										   MUSPELHEIM_POWER_INGOT,
										   NIFLHEIM_POWER_INGOT)
		
		recipeManaStone = addInfuserRecipe(ItemStack(manaStone, 1, 1000),
										   TilePool.MAX_MANA,
										   DRAGONSTONE,
										   ItemStack(elvenResource, 4, IffesalDust))
		
		recipeManaStoneGreater = addInfuserRecipe(ItemStack(manaStoneGreater, 1, 1000),
												  TilePool.MAX_MANA * 4,
												  ItemStack(manaStone, 1, WILDCARD_VALUE).also { ItemNBTHelper.setBoolean(it, ASJUtilities.TAG_ASJIGNORENBT, true) },
												  ItemStack(manaResource, 4, 5),
												  ItemStack(elvenResource, 1, MuspelheimEssence),
												  ItemStack(elvenResource, 1, NiflheimEssence))
	}
	
	private fun banRetrades() {
		AlfheimAPI.banRetrade(recipeInterdimensional.output)
		//AlfheimAPI.banRetrade(recipeStoryToken.output)
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
		
		recipeOrechidEndium = BotaniaAPI.registerPetalRecipe(BotaniaAPI.internalHandler.getSubTileAsStack("orechidEndium"),
															 PETAL[4],  // Yellow
															 PETAL[10], // Purple
															 ItemStack(manaResource, 1, 15), // Ender Air
															 PETAL[10], // Purple
															 PETAL[6],  // Pink
															 RUNE[15],  // Pride
															 RUNE[11],  // Greed
															 REDSTONE_ROOT,
															 PIXIE_DUST)
		
		recipePetronia = BotaniaAPI.registerPetalRecipe(BotaniaAPI.internalHandler.getSubTileAsStack("petronia"),
														REDSTONE_ROOT,
														RUNE[0],   // Water
														RUNE[1],   // Fire
														PETAL[1],  // Orange
														PETAL[15], // Black
														PETAL[12], // Brown
														DRAGONSTONE)
		
		recipeRainFlower = BotaniaAPI.registerPetalRecipe(BotaniaAPI.internalHandler.getSubTileAsStack("rainFlower"),
														  *Array(4) { PETAL[11] }, // Blue
														  PETAL[3], PETAL[3], // Light Blue
														  PETAL[4]) // Yellow
		
		recipeSnowFlower = BotaniaAPI.registerPetalRecipe(BotaniaAPI.internalHandler.getSubTileAsStack("snowFlower"),
														  *Array(4) { PETAL[3] }, // Light Blue
														  *Array(3) { PETAL[0] }) // White
		
		recipeStormFlower = BotaniaAPI.registerPetalRecipe(BotaniaAPI.internalHandler.getSubTileAsStack("stormFlower"),
														   *Array(4) { PETAL[3] }, // Light Blue
														   PETAL[11], // Blue
														   RUNE[13])  // Wrath
		
		recipeWindFlower = BotaniaAPI.registerPetalRecipe(BotaniaAPI.internalHandler.getSubTileAsStack("windFlower"),
														  PETAL[4], PETAL[4], // Yellow
														  PETAL[5], PETAL[5], // Lime
														  RUNE[6]) // Autumn
		
		recipeRealityRune = BotaniaAPI.registerRuneAltarRecipe(ItemStack(elvenResource, 1, PrimalRune), costTier3,
															   RUNE[0], RUNE[1], RUNE[2], RUNE[3], RUNE[8], ItemStack(manaResource, 1, 15), MAUFTRIUM_INGOT)
		recipeMuspelheimRune = BotaniaAPI.registerRuneAltarRecipe(ItemStack(elvenResource, 1, MuspelheimRune), costTier3,
																  RUNE[1], RUNE[2], ItemStack(elvenResource, 1, MuspelheimEssence), ItemStack(elvenResource, 1, MuspelheimEssence), IFFESAL_DUST)
		recipeNiflheimRune = BotaniaAPI.registerRuneAltarRecipe(ItemStack(elvenResource, 1, NiflheimRune), costTier3,
																RUNE[0], RUNE[3], ItemStack(elvenResource, 1, NiflheimEssence), ItemStack(elvenResource, 1, NiflheimEssence), IFFESAL_DUST)
		
		BotaniaAPI.runeAltarRecipes.remove(ModRuneRecipes.recipeSummerRune)
		ModRuneRecipes.recipeSummerRune = BotaniaAPI.registerRuneAltarRecipe(ItemStack(rune, 1, 5), costTier2, RUNE[2], RUNE[3], "sand", "sand", ItemStack(slime_ball), ItemStack(melon))
		
		ModRuneRecipes.recipesEarthRune.add(BotaniaAPI.registerRuneAltarRecipe(ItemStack(rune, 2, 2), costTier1, MANA_POWDER, MANA_STEEL, ItemStack(livingcobble), ItemStack(obsidian), ItemStack(brown_mushroom)))
		ModRuneRecipes.recipesEarthRune.add(BotaniaAPI.registerRuneAltarRecipe(ItemStack(rune, 2, 2), costTier1, MANA_POWDER, MANA_STEEL, ItemStack(livingcobble), ItemStack(obsidian), ItemStack(red_mushroom)))
		
		recipeInterdimensional = BotaniaAPI.registerElvenTradeRecipe(ItemStack(elvenResource, 1, InterdimensionalGatewayCore), ItemStack(nether_star))
		//recipeStoryToken = BotaniaAPI.registerElvenTradeRecipe(ItemStack(storyToken, 1, 1), ItemStack(storyToken, 1, 0))
		
		recipeDreamwood = BotaniaAPI.registerPureDaisyRecipe(DREAM_WOOD_LOG, dreamwood, 0)
		BotaniaAPI.registerPureDaisyRecipe("cobblestone", livingcobble, 0)
		
		BotaniaAPI.registerManaInfusionRecipe(ItemStack(elvenResource, 1, InfusedDreamwoodTwig), ItemStack(manaResource, 1, 13), 10000)
		
		addRecipe(RecipeHelmetElvorium(elvoriumHelmet, terrasteelHelm))
		recipeElvoriumHelmet = BotaniaAPI.getLatestAddedRecipe()
		
		if (Botania.thaumcraftLoaded) {
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
		addRecipe(RecipeElvenWeed())
	}
	
	fun postInit() {
		ModCraftingRecipes.recipeGaiaPylon = recipeGaiaPylon
		ModCraftingRecipes.recipeSuperLavaPendant = recipeSuperLavaPendantNew
	}
	
	fun addMMORecipes() {
		CraftingManager.getInstance().recipeList.add(recipePaperBreak)
		CraftingManager.getInstance().recipeList.add(recipePeacePipe)
	}
	
	fun removeMMORecipes() {
		ASJUtilities.removeRecipe(paperBreak, 4)
		ASJUtilities.removeRecipe(peacePipe)
	}
}