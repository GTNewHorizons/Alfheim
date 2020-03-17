package alfheim.common.crafting.recipe

import alfheim.api.*
import alfheim.api.crafting.recipe.RecipeTreeCrafting
import alfheim.api.lib.LibOreDict.ALT_TYPES
import alfheim.api.lib.LibOreDict.COAL_NETHERWOOD
import alfheim.api.lib.LibOreDict.DYES
import alfheim.api.lib.LibOreDict.FLORAL_POWDER
import alfheim.api.lib.LibOreDict.HOLY_PENDANT
import alfheim.api.lib.LibOreDict.IRIS_DIRT
import alfheim.api.lib.LibOreDict.LEAVES
import alfheim.api.lib.LibOreDict.MUSHROOM
import alfheim.api.lib.LibOreDict.PETAL
import alfheim.api.lib.LibOreDict.RAINBOW_DOUBLE_FLOWER
import alfheim.api.lib.LibOreDict.RAINBOW_FLOWER
import alfheim.api.lib.LibOreDict.RAINBOW_PETAL
import alfheim.api.lib.LibOreDict.SPLINTERS_NETHERWOOD
import alfheim.api.lib.LibOreDict.SPLINTERS_THUNDERWOOD
import alfheim.api.lib.LibOreDict.TWIG_NETHERWOOD
import alfheim.api.lib.LibOreDict.TWIG_THUNDERWOOD
import alfheim.api.lib.LibOreDict.WOOD
import alfheim.common.block.AlfheimBlocks.altPlanks
import alfheim.common.block.AlfheimBlocks.altSlabs
import alfheim.common.block.AlfheimBlocks.altStairs
import alfheim.common.block.AlfheimBlocks.altWood0
import alfheim.common.block.AlfheimBlocks.altWood1
import alfheim.common.block.AlfheimBlocks.amplifier
import alfheim.common.block.AlfheimBlocks.auroraDirt
import alfheim.common.block.AlfheimBlocks.auroraPlanks
import alfheim.common.block.AlfheimBlocks.auroraSlab
import alfheim.common.block.AlfheimBlocks.auroraStairs
import alfheim.common.block.AlfheimBlocks.auroraWood
import alfheim.common.block.AlfheimBlocks.calicoPlanks
import alfheim.common.block.AlfheimBlocks.calicoSapling
import alfheim.common.block.AlfheimBlocks.calicoSlabs
import alfheim.common.block.AlfheimBlocks.calicoStairs
import alfheim.common.block.AlfheimBlocks.calicoWood
import alfheim.common.block.AlfheimBlocks.circuitPlanks
import alfheim.common.block.AlfheimBlocks.circuitSapling
import alfheim.common.block.AlfheimBlocks.circuitSlabs
import alfheim.common.block.AlfheimBlocks.circuitStairs
import alfheim.common.block.AlfheimBlocks.circuitWood
import alfheim.common.block.AlfheimBlocks.irisDirt
import alfheim.common.block.AlfheimBlocks.irisGrass
import alfheim.common.block.AlfheimBlocks.irisLamp
import alfheim.common.block.AlfheimBlocks.irisPlanks
import alfheim.common.block.AlfheimBlocks.irisSapling
import alfheim.common.block.AlfheimBlocks.irisSlabs
import alfheim.common.block.AlfheimBlocks.irisStairs
import alfheim.common.block.AlfheimBlocks.irisWood0
import alfheim.common.block.AlfheimBlocks.irisWood1
import alfheim.common.block.AlfheimBlocks.irisWood2
import alfheim.common.block.AlfheimBlocks.irisWood3
import alfheim.common.block.AlfheimBlocks.itemDisplay
import alfheim.common.block.AlfheimBlocks.kindling
import alfheim.common.block.AlfheimBlocks.lightningPlanks
import alfheim.common.block.AlfheimBlocks.lightningSapling
import alfheim.common.block.AlfheimBlocks.lightningSlabs
import alfheim.common.block.AlfheimBlocks.lightningStairs
import alfheim.common.block.AlfheimBlocks.lightningWood
import alfheim.common.block.AlfheimBlocks.livingwoodFunnel
import alfheim.common.block.AlfheimBlocks.netherPlanks
import alfheim.common.block.AlfheimBlocks.netherSapling
import alfheim.common.block.AlfheimBlocks.netherSlabs
import alfheim.common.block.AlfheimBlocks.netherStairs
import alfheim.common.block.AlfheimBlocks.netherWood
import alfheim.common.block.AlfheimBlocks.rainbowDirt
import alfheim.common.block.AlfheimBlocks.rainbowGrass
import alfheim.common.block.AlfheimBlocks.rainbowLeaves
import alfheim.common.block.AlfheimBlocks.rainbowMushroom
import alfheim.common.block.AlfheimBlocks.rainbowPetalBlock
import alfheim.common.block.AlfheimBlocks.rainbowPlanks
import alfheim.common.block.AlfheimBlocks.rainbowSlab
import alfheim.common.block.AlfheimBlocks.rainbowStairs
import alfheim.common.block.AlfheimBlocks.rainbowWood
import alfheim.common.block.AlfheimBlocks.sealingPlanks
import alfheim.common.block.AlfheimBlocks.sealingSapling
import alfheim.common.block.AlfheimBlocks.sealingSlabs
import alfheim.common.block.AlfheimBlocks.sealingStairs
import alfheim.common.block.AlfheimBlocks.sealingWood
import alfheim.common.block.AlfheimBlocks.shimmerQuartz
import alfheim.common.block.AlfheimBlocks.shimmerQuartzSlab
import alfheim.common.block.AlfheimBlocks.shimmerQuartzStairs
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.integration.thaumcraft.ThaumcraftSuffusionRecipes
import alfheim.common.item.AlfheimItems.attributionBauble
import alfheim.common.item.AlfheimItems.coatOfArms
import alfheim.common.item.AlfheimItems.colorOverride
import alfheim.common.item.AlfheimItems.elvenResource
import alfheim.common.item.AlfheimItems.emblem
import alfheim.common.item.AlfheimItems.fireGrenade
import alfheim.common.item.AlfheimItems.invisibleFlameLens
import alfheim.common.item.AlfheimItems.irisSeeds
import alfheim.common.item.AlfheimItems.rodColorfulSkyDirt
import alfheim.common.item.AlfheimItems.rodFlameStar
import alfheim.common.item.AlfheimItems.rodInterdiction
import alfheim.common.item.AlfheimItems.rodLightning
import alfheim.common.item.AlfheimItems.rodPrismatic
import alfheim.common.item.AlfheimItems.splashPotion
import alfheim.common.item.block.*
import alfheim.common.item.material.ElvenResourcesMetas
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.Block
import net.minecraft.init.*
import net.minecraft.item.*
import net.minecraft.item.crafting.*
import net.minecraftforge.oredict.*
import net.minecraftforge.oredict.RecipeSorter.Category
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.recipe.*
import vazkii.botania.common.Botania
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.crafting.ModCraftingRecipes.*
import vazkii.botania.common.crafting.ModManaInfusionRecipes
import vazkii.botania.common.lib.LibItemNames.PESTLE_AND_MORTAR
import vazkii.botania.common.lib.LibOreDict.*
import java.util.*
import vazkii.botania.common.block.ModBlocks as BotaniaBlocks
import vazkii.botania.common.block.ModFluffBlocks as BotaniaDecorBlocks
import vazkii.botania.common.item.ModItems as BotaniaItems
import vazkii.botania.common.lib.LibOreDict.PETAL as PETALS

@Suppress("SameParameterValue")
object ShadowFoxRecipes {
	
	val recipeAuroraDirt: IRecipe
	val recipeAuroraPlanks: IRecipe
	val recipeAuroraStairs: IRecipe
	val recipeAuroraSlabs: IRecipe
	val recipeAuroraSlabsToPlanks: IRecipe
	val recipesColoredDirt: List<IRecipe>
	val recipesWoodPanel: List<IRecipe>
	val recipesSlabs: List<IRecipe>
	val recipesStairs: List<IRecipe>
	val recipesSlabsFull: List<IRecipe>
	val recipesColoredSkyDirtRod: List<IRecipe>
	val recipesPriestOfSif: IRecipe
	val recipesCoatOfArms: List<IRecipe>
	val recipesLightningRod: IRecipe
	val recipesPriestOfThor: IRecipe
	val recipesFlameRod: IRecipe
	val recipesPriestOfLoki: IRecipe
	val recipesLeafDyes: List<IRecipe>
	val recipesInterdictionRod: IRecipe
	val recipesPriestOfNjord: IRecipe
	val recipesRedstoneRoot: List<IRecipe>
	val recipesColorOverride: IRecipe
	val recipesinvisibleLens: IRecipe
	val recipesinvisibleLensUndo: IRecipe
	val recipesAttribution: IRecipe
	val recipesKindling: IRecipe
	val recipesRainbowRod: IRecipe
	val recipesItemDisplay: List<IRecipe>
	val recipesItemDisplayElven: IRecipe
	val recipesThunderousPlanks: IRecipe
	val recipesThunderousStairs: IRecipe
	val recipesThunderousSlabs: IRecipe
	val recipesThunderousTwig: IRecipe
	val recipesLivingwoodFunnel: IRecipe
	val recipesInfernalPlanks: IRecipe
	val recipesInfernalStairs: IRecipe
	val recipesInfernalSlabs: IRecipe
	val recipesInfernalTwig: IRecipe
	val recipesCalicoPlanks: IRecipe
	val recipesCalicoStairs: IRecipe
	val recipesCalicoSlabs: IRecipe
	val recipesCircuitPlanks: IRecipe
	val recipesCircuitStairs: IRecipe
	val recipesCircuitSlabs: IRecipe
	val recipesSixTorches: IRecipe
	val recipesAltPlanks: List<IRecipe>
	val recipesAltSlabs: List<IRecipe>
	val recipesAltStairs: List<IRecipe>
	//val recipesToolbelt: IRecipe
	val recipesLamp: IRecipe
	val recipesSealingPlanks: IRecipe
	val recipesSealingStairs: IRecipe
	val recipesSealingSlabs: IRecipe
	val recipesAmplifier: IRecipe
	val recipesStar: List<IRecipe>
	val recipesStar2: List<IRecipe>
	
	val recipeShimmerQuartz: IRecipe
	
	val recipeRainbowPetalGrinding: IRecipe
	val recipesRainbowPetal: List<IRecipe>
	val recipeRainbowPetalBlock: IRecipe
	
	val recipesPastoralSeeds: List<RecipeManaInfusion>
	
	val recipesAttributionHeads: List<RecipePetals>
	
	val recipePlainDirt: RecipePureDaisy
	val recipeIrisSapling: RecipePureDaisyExclusion
	
	val recipesLightningTree: RecipeTreeCrafting
	val recipesInfernalTree: RecipeTreeCrafting
	val recipesSealingTree: RecipeTreeCrafting
	val recipesCalicoTree: RecipeTreeCrafting
	val recipesCircuitTree: RecipeTreeCrafting
	
	val recipeCrysanthermum: RecipePetals
	
	val recipesSplashPotions: IRecipe
	
	init {
		GameRegistry.addRecipe(RecipeRingDyes())
		RecipeSorter.register("${ModInfo.MODID}:ringdye", RecipeRingDyes::class.java, Category.SHAPELESS, "")
		GameRegistry.addRecipe(RecipeRainbowLensDye())
		RecipeSorter.register("${ModInfo.MODID}:lensdye", RecipeRainbowLensDye::class.java, Category.SHAPELESS, "")
		
		for (i in 0..15)
			addOreDictRecipe(ItemStack(irisDirt, 8, i),
							 "DDD",
							 "DPD",
							 "DDD", 'P', DYES[i], 'D', ItemStack(Blocks.dirt, 1))
		
		addOreDictRecipe(ItemStack(rainbowDirt, 8),
						 "DDD",
						 "DPD",
						 "DDD", 'P', DYES[16], 'D', ItemStack(Blocks.dirt, 1))
		
		recipesColoredDirt = BotaniaAPI.getLatestAddedRecipes(17)
		
		addOreDictRecipe(ItemStack(auroraDirt, 8),
						 "DDD",
						 "DPD", "DDD", 'P', MANA_PEARL, 'D', ItemStack(Blocks.dirt, 1))
		
		recipeAuroraDirt = BotaniaAPI.getLatestAddedRecipe()
		
		for (i in 0..15)
			addShapelessOreDictRecipe(ItemStack(BotaniaItems.dye, 1, i), LEAVES[i], PESTLE_AND_MORTAR)
		addShapelessOreDictRecipe(ItemStack(elvenResource, 1, ElvenResourcesMetas.RainbowDust), LEAVES[16], PESTLE_AND_MORTAR)
		
		recipesLeafDyes = BotaniaAPI.getLatestAddedRecipes(17)
		for (i in 0..15)
			addShapelessOreDictRecipe(ItemStack(irisPlanks, 4, i), WOOD[i])
		addShapelessOreDictRecipe(ItemStack(rainbowPlanks, 4), rainbowWood)
		
		recipesWoodPanel = BotaniaAPI.getLatestAddedRecipes(17)
		
		addShapelessOreDictRecipe(ItemStack(auroraPlanks, 4), auroraWood)
		
		recipeAuroraPlanks = BotaniaAPI.getLatestAddedRecipe()
		
		for (i in 0..15)
			GameRegistry.addRecipe(ItemStack(irisSlabs[i], 6),
								   "QQQ",
								   'Q', ItemStack(irisPlanks, 1, i))
		GameRegistry.addRecipe(ItemStack(rainbowSlab, 6),
							   "QQQ",
							   'Q', ItemStack(rainbowPlanks))
		
		recipesSlabs = BotaniaAPI.getLatestAddedRecipes(17)
		
		GameRegistry.addRecipe(ItemStack(auroraSlab, 6),
							   "QQQ",
							   'Q', ItemStack(auroraPlanks))
		
		recipeAuroraSlabs = BotaniaAPI.getLatestAddedRecipe()
		
		for (i in 0..15)
			addOreDictRecipe(ItemStack(irisStairs[i], 4), true,
							 "Q  ", "QQ ", "QQQ",
							 'Q', ItemStack(irisPlanks, 1, i))
		
		addOreDictRecipe(ItemStack(rainbowStairs, 4), true,
							   "Q  ", "QQ ", "QQQ",
							   'Q', ItemStack(rainbowPlanks))
		
		recipesStairs = BotaniaAPI.getLatestAddedRecipes(17)
		
		addOreDictRecipe(ItemStack(auroraStairs, 4), true,
							   "Q  ", "QQ ", "QQQ",
							   'Q', ItemStack(auroraPlanks))
		
		recipeAuroraStairs = BotaniaAPI.getLatestAddedRecipe()
		
		for (i in 0..15)
			GameRegistry.addShapelessRecipe(ItemStack(irisPlanks, 1, i),
											ItemStack(irisSlabs[i], 1), ItemStack(irisSlabs[i], 1))
		GameRegistry.addShapelessRecipe(ItemStack(rainbowPlanks),
										ItemStack(rainbowSlab), ItemStack(rainbowSlab))
		
		recipesSlabsFull = BotaniaAPI.getLatestAddedRecipes(17)
		
		GameRegistry.addShapelessRecipe(ItemStack(auroraPlanks),
										ItemStack(auroraSlab), ItemStack(auroraSlab))
		
		recipeAuroraSlabsToPlanks = BotaniaAPI.getLatestAddedRecipe()
		
		for (i in 0..15)
			addOreDictRecipe(ItemStack(rodColorfulSkyDirt, 1, i),
							 " PD",
							 " RP",
							 "S  ",
							 'D', ItemStack(irisDirt, 1, i),
							 'R', ItemStack(BotaniaItems.skyDirtRod, 1),
							 'P', PIXIE_DUST,
							 'S', DRAGONSTONE)
		
		addOreDictRecipe(ItemStack(rodColorfulSkyDirt, 1, 16),
						 " PD",
						 " RP",
						 "S  ",
						 'D', ItemStack(rainbowDirt),
						 'R', ItemStack(BotaniaItems.skyDirtRod, 1),
						 'P', PIXIE_DUST,
						 'S', DRAGONSTONE)
		
		addOreDictRecipe(ItemStack(rodColorfulSkyDirt, 1, 17),
						 " PD",
						 " RP",
						 "S  ",
						 'D', ItemStack(auroraDirt),
						 'R', ItemStack(BotaniaItems.skyDirtRod, 1),
						 'P', PIXIE_DUST,
						 'S', DRAGONSTONE)
		
		recipesColoredSkyDirtRod = BotaniaAPI.getLatestAddedRecipes(17)
		
		addOreDictRecipe(ItemStack(rodLightning, 1),
						 " EW",
						 " SD",
						 "S  ",
						 'E', SPLINTERS_THUNDERWOOD,
						 'D', DRAGONSTONE,
						 'S', TWIG_THUNDERWOOD,
						 'W', RUNE[13]) // Wrath
		
		recipesLightningRod = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(rodFlameStar, 1),
						 " EW",
						 " SD",
						 "S  ",
						 'E', SPLINTERS_NETHERWOOD,
						 'D', COAL_NETHERWOOD,
						 'S', TWIG_NETHERWOOD,
						 'W', RUNE[1]) // Fire
		
		recipesFlameRod = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(emblem, 1, 0),
						 "EGE",
						 "TAT",
						 " W ",
						 'E', ENDER_AIR_BOTTLE,
						 'T', TERRASTEEL_NUGGET,
						 'G', LIFE_ESSENCE,
						 'W', RUNE[13], // Wrath
						 'A', HOLY_PENDANT)
		
		recipesPriestOfThor = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(emblem, 1, 1),
						 "DGD",
						 "NAN",
						 " P ",
						 'D', DRAGONSTONE,
						 'N', "nuggetGold",
						 'G', LIFE_ESSENCE,
						 'P', RUNE[2], // Earth
						 'A', HOLY_PENDANT)
		
		recipesPriestOfSif = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(emblem, 1, 3),
						 "WGO",
						 "NAN",
						 " P ",
						 'O', RUNE[6],
						 'W', RUNE[7],
						 'N', ELEMENTIUM_NUGGET,
						 'G', LIFE_ESSENCE,
						 'P', RUNE[8], // Mana
						 'A', HOLY_PENDANT)
		
		recipesPriestOfLoki = BotaniaAPI.getLatestAddedRecipe()
		
		for (i in 0..15)
			addOreDictRecipe(ItemStack(coatOfArms, 1, i),
							 "LLL",
							 "LSL",
							 "LLL",
							 'L', LEAVES[i],
							 'S', MANA_STRING)
		
		addOreDictRecipe(ItemStack(coatOfArms, 1, 16),
						 "LLL",
						 "LSL",
						 "LLL",
						 'L', ItemStack(rainbowLeaves),
						 'S', MANA_STRING)
		
		recipesCoatOfArms = BotaniaAPI.getLatestAddedRecipes(17)
		
		addOreDictRecipe(ItemStack(colorOverride),
						 "PE ",
						 "E E",
						 " E ",
						 'P', ItemStack(BotaniaItems.lens, 1, 14), // Paintslinger's Lens
						 'E', ELEMENTIUM)
		
		recipesColorOverride = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(rodInterdiction),
						 " AS",
						 " DA",
						 "P  ",
						 'P', RUNE[15], // Pride
						 'A', RUNE[3], // Air
						 'S', ItemStack(BotaniaItems.tornadoRod),
						 'D', DREAMWOOD_TWIG)
		
		recipesInterdictionRod = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(emblem, 1, 2),
						 "RGR",
						 "NAN",
						 " P ",
						 'P', RUNE[15], // Pride
						 'N', MANASTEEL_NUGGET,
						 'G', LIFE_ESSENCE,
						 'R', RUNE[3], // Air
						 'A', HOLY_PENDANT)
		
		recipesPriestOfNjord = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(attributionBauble),
						 "S S",
						 "Q Q",
						 " G ",
						 'G', "ingotGold",
						 'Q', "gemQuartz",
						 'S', MANA_STRING)
		
		recipesAttribution = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(kindling),
						 " S ",
						 "SBS",
						 " S ",
						 'B', "powderBlaze",
						 'S', MANA_STRING)
		
		recipesKindling = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessOreDictRecipe(ItemStack(invisibleFlameLens),
								  ItemStack(BotaniaItems.lens, 1, 17), BotaniaItems.phantomInk)
		
		recipesinvisibleLens = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessOreDictRecipe(ItemStack(BotaniaItems.lens, 1, 17),
								  ItemStack(invisibleFlameLens), BotaniaItems.phantomInk)
		
		recipesinvisibleLensUndo = BotaniaAPI.getLatestAddedRecipe()
		
		for (i in 0..15)
			addShapelessOreDictRecipe(ItemStack(BotaniaItems.manaResource, 1, 6), "dustRedstone", ItemStack(irisGrass, 1, i))
		for (i in 0..1)
			addShapelessOreDictRecipe(ItemStack(BotaniaItems.manaResource, 1, 6), "dustRedstone", ItemStack(rainbowGrass, 1, i))
		
		recipesRedstoneRoot = BotaniaAPI.getLatestAddedRecipes(18)
		
		recipesPastoralSeeds = ArrayList()
		for (i in 0..15)
			recipesPastoralSeeds.add(BotaniaAPI.registerManaInfusionRecipe(ItemStack(irisSeeds, 1, i), ItemStack(irisGrass, 1, i), 2500))
		for (i in 0..1)
		recipesPastoralSeeds.add(BotaniaAPI.registerManaInfusionRecipe(ItemStack(irisSeeds, 1, 16+i), ItemStack(rainbowGrass, 1, i), 2500))
		
		recipePlainDirt = BotaniaAPI.registerPureDaisyRecipe(IRIS_DIRT, Blocks.dirt, 0)
		
		recipeIrisSapling = RecipePureDaisyExclusion("treeSapling", irisSapling, 0)
		BotaniaAPI.pureDaisyRecipes.add(recipeIrisSapling as RecipePureDaisy)
		
		addOreDictRecipe(ItemStack(rodPrismatic),
						 " GB",
						 " DG",
						 "D  ",
						 'G', "glowstone",
						 'B', DYES[16],
						 'D', DREAMWOOD_TWIG)
		
		recipesRainbowRod = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(itemDisplay),
						 "N",
						 "W",
						 'N', MANASTEEL_NUGGET,
						 'W', ItemStack(BotaniaDecorBlocks.livingwoodSlab))
		addOreDictRecipe(ItemStack(itemDisplay, 1, 1),
						 "N",
						 "W",
						 'N', TERRASTEEL_NUGGET,
						 'W', ItemStack(BotaniaDecorBlocks.livingwoodSlab))
		
		recipesItemDisplay = BotaniaAPI.getLatestAddedRecipes(2)
		
		addOreDictRecipe(ItemStack(itemDisplay, 1, 2),
						 "N",
						 "W",
						 'N', ELEMENTIUM_NUGGET,
						 'W', ItemStack(BotaniaDecorBlocks.dreamwoodSlab))
		
		recipesItemDisplayElven = BotaniaAPI.getLatestAddedRecipe()
		
		recipesLightningTree = ShadowFoxAPI.addTreeRecipe(50000,
														  lightningSapling, 0,
														  350,
														  MANA_STEEL, MANA_STEEL, MANA_STEEL,
														  RUNE[13], // Wrath
														  LEAVES[10], LEAVES[10], LEAVES[10], // Purple
														  ItemStack(BotaniaBlocks.teruTeruBozu))
		
		addShapelessOreDictRecipe(ItemStack(lightningPlanks, 4), lightningWood)
		
		recipesThunderousPlanks = BotaniaAPI.getLatestAddedRecipe()
		
		GameRegistry.addRecipe(ItemStack(lightningSlabs, 6),
							   "QQQ",
							   'Q', ItemStack(lightningPlanks))
		
		recipesThunderousSlabs = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(lightningStairs, 4), true,
							   "Q  ", "QQ ", "QQQ",
							   'Q', ItemStack(lightningPlanks))
		
		recipesThunderousStairs = BotaniaAPI.getLatestAddedRecipe()
		
		GameRegistry.addRecipe(ItemStack(elvenResource, 1, ElvenResourcesMetas.ThunderwoodTwig),
							   "Q",
							   "Q",
							   'Q', ItemStack(lightningWood))
		
		recipesThunderousTwig = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(livingwoodFunnel, 1),
						 "L L",
						 "LCL",
						 " L ",
						 'L', LIVING_WOOD, 'C', ItemStack(Blocks.chest))
		
		recipesLivingwoodFunnel = BotaniaAPI.getLatestAddedRecipe()
		
		recipesInfernalTree = ShadowFoxAPI.addTreeRecipe(10000,
														 netherSapling, 0,
														 70,
														 "ingotBrickNether", "ingotBrickNether", "ingotBrickNether",
														 RUNE[1], // Fire
														 LEAVES[1], LEAVES[1], LEAVES[1], // orange
														 BLAZE_BLOCK)
		
		addShapelessOreDictRecipe(ItemStack(netherPlanks, 4), netherWood)
		
		recipesInfernalPlanks = BotaniaAPI.getLatestAddedRecipe()
		
		GameRegistry.addRecipe(ItemStack(netherSlabs, 6),
							   "QQQ",
							   'Q', ItemStack(netherPlanks))
		
		recipesInfernalSlabs = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(netherStairs, 4), true,
							   "Q  ", "QQ ", "QQQ",
							   'Q', ItemStack(netherPlanks))
		
		recipesInfernalStairs = BotaniaAPI.getLatestAddedRecipe()
		
		recipesCalicoTree = ShadowFoxAPI.addTreeRecipe(50000,
													   calicoSapling, 0,
													   70,
													   ItemStack(Blocks.soul_sand), ItemStack(Blocks.soul_sand), ItemStack(Blocks.soul_sand),
													   RUNE[11], // Greed
													   LEAVES[1], LEAVES[0], LEAVES[12], // Orange White Brown
													   ItemStack(Blocks.obsidian))
		
		addShapelessOreDictRecipe(ItemStack(calicoPlanks, 4), calicoWood)
		
		recipesCalicoPlanks = BotaniaAPI.getLatestAddedRecipe()
		
		GameRegistry.addRecipe(ItemStack(calicoSlabs, 6),
							   "QQQ",
							   'Q', ItemStack(calicoPlanks))
		
		recipesCalicoSlabs = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(calicoStairs, 4), true,
							   "Q  ", "QQ ", "QQQ",
							   'Q', ItemStack(calicoPlanks))
		
		recipesCalicoStairs = BotaniaAPI.getLatestAddedRecipe()
		
		recipesCircuitTree = ShadowFoxAPI.addTreeRecipe(10000,
														circuitSapling, 0,
														70,
														ItemStack(Items.repeater), ItemStack(Items.comparator), ItemStack(Items.repeater),
														RUNE[9], // Lust
														LEAVES[14], LEAVES[14], LEAVES[14], // Red
														"blockRedstone")
		
		addShapelessOreDictRecipe(ItemStack(circuitPlanks, 4), circuitWood)
		
		recipesCircuitPlanks = BotaniaAPI.getLatestAddedRecipe()
		
		GameRegistry.addRecipe(ItemStack(circuitSlabs, 6),
							   "QQQ",
							   'Q', ItemStack(circuitPlanks))
		
		recipesCircuitSlabs = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(circuitStairs, 4), true,
							   "Q  ", "QQ ", "QQQ",
							   'Q', ItemStack(circuitPlanks))
		
		recipesCircuitStairs = BotaniaAPI.getLatestAddedRecipe()
		
		GameRegistry.addRecipe(ItemStack(elvenResource, 1, ElvenResourcesMetas.NetherwoodTwig),
							   "Q",
							   "Q",
							   'Q', ItemStack(netherWood))
		
		recipesInfernalTwig = BotaniaAPI.getLatestAddedRecipe()
		
		GameRegistry.addRecipe(ItemStack(Blocks.torch, 6),
							   "C",
							   "S",
							   'C', ItemStack(elvenResource, 1, ElvenResourcesMetas.NetherwoodCoal),
							   'S', ItemStack(Items.stick))
		
		recipesSixTorches = BotaniaAPI.getLatestAddedRecipe()
		
		for (i in 0..3)
			addShapelessOreDictRecipe(ItemStack(altPlanks, 4, i), ItemStack(altWood0, 1, i))
		
		addShapelessOreDictRecipe(ItemStack(altPlanks, 4, 4), ItemStack(altWood1, 1, 0))
		addShapelessOreDictRecipe(ItemStack(altPlanks, 4, 5), ItemStack(altWood1, 1, 1))
		
		recipesAltPlanks = BotaniaAPI.getLatestAddedRecipes(6)
		
		for (i in 0 until ALT_TYPES.size - 1)
			GameRegistry.addRecipe(ItemStack(altSlabs, 6, i),
								   "QQQ",
								   'Q', ItemStack(altPlanks, 1, i))
		
		recipesAltSlabs = BotaniaAPI.getLatestAddedRecipes(6)
		
		for (i in 0 until ALT_TYPES.size - 1)
			addOreDictRecipe(ItemStack(altStairs[i], 4), true,
								   "Q  ", "QQ ", "QQQ",
								   'Q', ItemStack(altPlanks, 1, i))
		
		recipesAltStairs = BotaniaAPI.getLatestAddedRecipes(6)
		
		/*addOreDictRecipe(ItemStack(toolbelt),
						 "CL ",
						 "L L",
						 "PLS",
						 'P', PIXIE_DUST,
						 'L', ItemStack(Items.leather),
						 'C', ItemStack(Blocks.chest),
						 'S', RUNE[12]) // Sloth
		
		recipesToolbelt = BotaniaAPI.getLatestAddedRecipe()*/
		
		addOreDictRecipe(ItemStack(irisLamp),
						 " B ",
						 "BLB",
						 " B ",
						 'L', ItemStack(Blocks.redstone_lamp),
						 'B', DYES[16])
		
		recipesLamp = BotaniaAPI.getLatestAddedRecipe()
		
		if (Botania.thaumcraftLoaded && ((Botania.gardenOfGlassLoaded && AlfheimConfigHandler.thaumTreeSuffusion) || ModInfo.DEV)) {
			ThaumcraftSuffusionRecipes.initRecipes()
		}
		
		recipesSealingTree = ShadowFoxAPI.addTreeRecipe(50000,
														sealingSapling, 0,
														350,
														ELEMENTIUM, ELEMENTIUM, ELEMENTIUM,
														RUNE[7], // Winter
														LEAVES[0], LEAVES[0], LEAVES[0], // White
														ItemStack(Blocks.wool, 1, OreDictionary.WILDCARD_VALUE))
		
		addShapelessOreDictRecipe(ItemStack(sealingPlanks, 4), sealingWood)
		
		recipesSealingPlanks = BotaniaAPI.getLatestAddedRecipe()
		
		GameRegistry.addRecipe(ItemStack(sealingSlabs, 6),
							   "QQQ",
							   'Q', ItemStack(sealingPlanks))
		
		recipesSealingSlabs = BotaniaAPI.getLatestAddedRecipe()
		
		addOreDictRecipe(ItemStack(sealingStairs, 4), true,
							   "Q  ", "QQ ", "QQQ",
							   'Q', ItemStack(sealingPlanks))
		
		recipesSealingStairs = BotaniaAPI.getLatestAddedRecipe()
		
		GameRegistry.addRecipe(ItemStack(amplifier),
							   " N ",
							   "NRN",
							   " N ",
							   'N', ItemStack(Blocks.noteblock),
							   'R', ItemStack(sealingPlanks))
		
		recipesAmplifier = BotaniaAPI.getLatestAddedRecipe()
		
		for (i in 0..16) {
			val stack = ItemStarPlacer.forColor(i)
			stack.stackSize = 3
			addOreDictRecipe(stack,
							 " E ",
							 "GDG",
							 " G ",
							 'E', ENDER_AIR_BOTTLE,
							 'G', "dustGlowstone",
							 'D', DYES[i])
		}
		
		recipesStar = BotaniaAPI.getLatestAddedRecipes(17)
		
		for (i in 0..16) {
			val stack = ItemStarPlacer2.forColor(i)
			stack.stackSize = 6
			addOreDictRecipe(stack,
							 " E ",
							 "GDG",
							 " G ",
							 'E', ENDER_AIR_BOTTLE,
							 'G', MANA_PEARL,
							 'D', DYES[i])
		}
		
		recipesStar2 = BotaniaAPI.getLatestAddedRecipes(17)
		
		addShapelessOreDictRecipe(ItemStack(BotaniaItems.fertilizer, if (Botania.gardenOfGlassLoaded) 3 else 1), ItemStack(Items.dye, 1, 15), FLORAL_POWDER, FLORAL_POWDER, FLORAL_POWDER, FLORAL_POWDER)
		CraftingManager.getInstance().recipeList.remove(recipeFertilizerPowder)
		recipeFertilizerPowder = BotaniaAPI.getLatestAddedRecipe()
		
		ModManaInfusionRecipes.manaPowderRecipes.add(BotaniaAPI.registerManaInfusionRecipe(ItemStack(BotaniaItems.manaResource, 1, 23), ItemStack(elvenResource, 1, ElvenResourcesMetas.RainbowDust), 400))
		
		addShapelessOreDictRecipe(ItemStack(BotaniaBlocks.shimmerrock), "livingrock", DYES[16])
		CraftingManager.getInstance().recipeList.remove(recipeShimmerrock)
		recipeShimmerrock = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessOreDictRecipe(ItemStack(BotaniaBlocks.shimmerwoodPlanks), ItemStack(BotaniaBlocks.dreamwood, 1, 1), DYES[16])
		CraftingManager.getInstance().recipeList.remove(recipeShimmerwoodPlanks)
		recipeShimmerwoodPlanks = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessOreDictRecipe(ItemStack(Items.mushroom_stew), MUSHROOM, MUSHROOM, ItemStack(Items.bowl))
		
		addOreDictRecipe(ItemStack(BotaniaBlocks.altar), "SPS", " C ", "CCC", 'S', "slabCobblestone", 'P', RAINBOW_PETAL, 'C', "cobblestone")
		recipesApothecary.add(BotaniaAPI.getLatestAddedRecipe())
		
		addOreDictRecipe(ItemStack(BotaniaBlocks.spreader), "WWW", "GP ", "WWW", 'W', LIVING_WOOD, 'P', RAINBOW_PETAL, 'G', if (Botania.gardenOfGlassLoaded) LIVING_WOOD else "ingotGold")
		recipesSpreader.add(BotaniaAPI.getLatestAddedRecipe())
		
		addOreDictRecipe(ItemStack(BotaniaBlocks.spreader, 1, 2), "WWW", "EP ", "WWW", 'W', DREAM_WOOD, 'P', RAINBOW_PETAL, 'E', ELEMENTIUM)
		recipesDreamwoodSpreader.add(BotaniaAPI.getLatestAddedRecipe())
		
		addOreDictRecipe(ItemStack(BotaniaItems.flowerBag), "WPW", "W W", " W ", 'P', PETAL, 'W', ItemStack(Blocks.wool, 1, 32767))
		CraftingManager.getInstance().recipeList.remove(recipeFlowerBag)
		recipeFlowerBag = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessOreDictRecipe(ItemStack(elvenResource, 1, ElvenResourcesMetas.RainbowDust), RAINBOW_PETAL, PESTLE_AND_MORTAR)
		recipeRainbowPetalGrinding = BotaniaAPI.getLatestAddedRecipe()
		
		addShapelessOreDictRecipe(ItemStack(elvenResource, 2, ElvenResourcesMetas.RainbowPetal), RAINBOW_FLOWER)
		addShapelessOreDictRecipe(ItemStack(elvenResource, 4, ElvenResourcesMetas.RainbowPetal), RAINBOW_DOUBLE_FLOWER)
		recipesRainbowPetal = BotaniaAPI.getLatestAddedRecipes(2)
		
		addOreDictRecipe(ItemStack(rainbowPetalBlock), "PPP", "PPP", "PPP", 'P', ItemStack(elvenResource, 1, ElvenResourcesMetas.RainbowPetal))
		recipeRainbowPetalBlock = BotaniaAPI.getLatestAddedRecipe()
		
		recipeShimmerQuartz = addQuartzRecipes(shimmerQuartz, shimmerQuartzStairs, shimmerQuartzSlab)
		
		recipesAttributionHeads = ArrayList()
		recipesAttributionHeads.add(attributionSkull("yrsegal", irisSeeds, 16)) // Bifrost Seeds
		// Wire - I just love rainbows, what can I say?
		recipesAttributionHeads.add(attributionSkull("l0nekitsune", elvenResource, ElvenResourcesMetas.NetherwoodCoal))
		// L0ne - "hot stuff" (because I'm classy like that)
		recipesAttributionHeads.add(attributionSkull("Tristaric", coatOfArms, 6)) // Irish Shield
		// Tris - The only item that remotely fits me.
		
		recipeCrysanthermum = BotaniaAPI.registerPetalRecipe(BotaniaAPI.internalHandler.getSubTileAsStack("crysanthermum"),
															 PETALS[1], PETALS[1], // Orange
															 PETALS[15], // Black
															 PETALS[3], PETALS[3], // Light Blue
															 RUNE[7], // Winter
															 RUNE[5]) // Summer
		
		GameRegistry.addSmelting(ItemStack(altWood1, 1, 3), ItemStack(Items.coal, 1, 1), 0.15F)
		GameRegistry.addSmelting(irisWood0, ItemStack(Items.coal, 1, 1), 0.15F)
		GameRegistry.addSmelting(irisWood1, ItemStack(Items.coal, 1, 1), 0.15F)
		GameRegistry.addSmelting(irisWood2, ItemStack(Items.coal, 1, 1), 0.15F)
		GameRegistry.addSmelting(irisWood3, ItemStack(Items.coal, 1, 1), 0.15F)
		GameRegistry.addSmelting(rainbowWood, ItemStack(Items.coal, 1, 1), 0.15F)
		GameRegistry.addSmelting(auroraWood, ItemStack(Items.coal, 1, 1), 0.15F)
		GameRegistry.addSmelting(lightningWood, ItemStack(Items.coal, 1, 1), 0.15F)
		GameRegistry.addSmelting(altWood0, ItemStack(Items.coal, 1, 1), 0.15F)
		GameRegistry.addSmelting(altWood1, ItemStack(Items.coal, 1, 1), 0.15F)
		GameRegistry.addSmelting(sealingWood, ItemStack(Items.coal, 1, 1), 0.15F)
		GameRegistry.addSmelting(netherWood, ItemStack(elvenResource, 1, ElvenResourcesMetas.NetherwoodCoal), 0.15F)
		GameRegistry.addSmelting(calicoWood, ItemStack(Items.coal, 1, 1), 0.15F)
		GameRegistry.addSmelting(circuitWood, ItemStack(Items.coal, 1, 1), 0.15F)
		GameRegistry.addSmelting(lightningPlanks, ItemStack(elvenResource, 2, ElvenResourcesMetas.ThunderwoodSplinters), 0.1F)
		GameRegistry.addSmelting(netherPlanks, ItemStack(elvenResource, 2, ElvenResourcesMetas.NetherwoodSplinters), 0.1F)
		
		recipesSplashPotions = ShapelessOreRecipe(ItemStack(splashPotion), BotaniaItems.brewVial, Items.gunpowder)
		GameRegistry.addRecipe(RecipeThrowablePotion())
		RecipeSorter.register("${ModInfo.MODID}:throwpotion", RecipeThrowablePotion::class.java, Category.SHAPELESS, "")
		
		addShapelessOreDictRecipe(ItemStack(fireGrenade), BotaniaItems.vial, Items.fire_charge, Items.gunpowder)
	}
	
	private fun addQuartzRecipes(block: Block, stairs: Block, slab: Block): IRecipe {
		GameRegistry.addRecipe(ItemStack(block),
							   "QQ",
							   "QQ",
							   'Q', ItemStack(elvenResource, 1, ElvenResourcesMetas.RainbowQuartz))
		BotaniaAPI.registerManaAlchemyRecipe(ItemStack(elvenResource, 4, ElvenResourcesMetas.RainbowQuartz), ItemStack(block, 1, 32767), 25)
		GameRegistry.addRecipe(ItemStack(block, 2, 2),
							   "Q",
							   "Q",
							   'Q', block)
		GameRegistry.addRecipe(ItemStack(block, 1, 1),
							   "Q",
							   "Q",
							   'Q', slab)
		addStairsAndSlabs(block, 0, stairs, slab)
		
		GameRegistry.addRecipe(ShapedOreRecipe(ItemStack(elvenResource, 8, ElvenResourcesMetas.RainbowQuartz),
											   "QQQ",
											   "QCQ",
											   "QQQ",
											   'Q', "gemQuartz",
											   'C', DYES[16]))
		return BotaniaAPI.getLatestAddedRecipe()
	}
	
	private fun addStairsAndSlabs(block: Block, meta: Int, stairs: Block, slab: Block) {
		GameRegistry.addRecipe(ItemStack(slab, 6),
							   "QQQ",
							   'Q', ItemStack(block, 1, meta))
		GameRegistry.addRecipe(ItemStack(stairs, 4),
							   "Q  ",
							   "QQ ",
							   "QQQ",
							   'Q', ItemStack(block, 1, meta))
	}
	
	fun postInit() {
		addShapelessOreDictRecipe(ItemStack(rainbowGrass, 1, 3), "dustGlowstone", "dustGlowstone", ItemStack(rainbowGrass, 1, 2))
		recipesShinyFlowers.add(BotaniaAPI.getLatestAddedRecipe())
		
		GameRegistry.addShapelessRecipe(ItemStack(rainbowMushroom), ItemStack(Blocks.red_mushroom), ItemStack(elvenResource, 1, ElvenResourcesMetas.RainbowDust))
		GameRegistry.addShapelessRecipe(ItemStack(rainbowMushroom), ItemStack(Blocks.brown_mushroom), ItemStack(elvenResource, 1, ElvenResourcesMetas.RainbowDust))
		
		recipesMushrooms.addAll(BotaniaAPI.getLatestAddedRecipes(2))
	}
	
	fun skullStack(name: String): ItemStack {
		val stack = ItemStack(Items.skull, 1, 3)
		ItemNBTHelper.setString(stack, "SkullOwner", name)
		return stack
	}
	
	private fun attributionSkull(name: String, item: Item, meta: Int) =
		BotaniaAPI.registerPetalRecipe(skullStack(name), *Array(16) { ItemStack(item, 1, meta) })
	
	private fun addOreDictRecipe(output: ItemStack, vararg recipe: Any) =
		GameRegistry.addRecipe(ShapedOreRecipe(output, *recipe))
	
	private fun addShapelessOreDictRecipe(output: ItemStack, vararg recipe: Any) =
		GameRegistry.addRecipe(ShapelessOreRecipe(output, *recipe))
	
}
