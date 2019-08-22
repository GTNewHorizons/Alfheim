package alfheim.common.lexicon

import alfheim.common.block.ShadowFoxBlocks
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.crafting.recipe.ShadowFoxRecipes
import alfheim.common.integration.thaumcraft.ThaumcraftSuffusionRecipes
import alfheim.common.item.*
import alfheim.common.item.block.ItemStarPlacer
import alfheim.common.lexicon.page.PageFurnaceRecipe
import net.minecraft.item.ItemStack
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.lexicon.*
import vazkii.botania.common.brew.ModBrews
import vazkii.botania.common.lexicon.LexiconData
import vazkii.botania.common.lexicon.page.*

@Suppress("JoinDeclarationAndAssignment")
object ShadowFoxLexiconData {
	
	val rainbowFlora: LexiconEntry
	val aurora: LexiconEntry
	val coloredDirt: LexiconEntry
	val irisSapling: LexiconEntry
	val technicolor: LexiconEntry
	val lightningRod: LexiconEntry
	val flameRod: LexiconEntry
	val interdictionRod: LexiconEntry
	val pastoralSeeds: LexiconEntry
	val coatOfArms: LexiconEntry
	val colorOverride: LexiconEntry
	val treeCrafting: LexiconEntry
	val dendrology: ShadowFoxLexiconCategory
	val attribution: LexiconEntry
	val sealCreepers: LexiconEntry
	val kindling: LexiconEntry
	val waveRod: LexiconEntry
	val itemDisplay: LexiconEntry
	val lightningSapling: LexiconEntry
	val livingwoodFunnel: LexiconEntry
	val netherSapling: LexiconEntry
	val circuitSapling: LexiconEntry
	val calicoSapling: LexiconEntry
	val toolbelt: LexiconEntry
	val lamp: LexiconEntry
	val silencer: LexiconEntry
	val amp: LexiconEntry
	val crysanthermum: LexiconEntry
	val specialAxe: LexiconEntry
	val frozenStar: LexiconEntry
	val dagger: LexiconEntry
	val shimmer: LexiconEntry
	val throwablePotions: LexiconEntry
	
	lateinit var tctrees: LexiconEntry
	
	init {
		
		dendrology = ShadowFoxLexiconCategory("dendrology", 1)
		
		rainbowFlora = ShadowfoxLexiconEntry("rainbowFlora", AlfheimLexiconData.categoryAlfheim, ItemStack(ShadowFoxBlocks.rainbowGrass, 1, 2))
		rainbowFlora.setLexiconPages(PageText("0"),
									 PageCraftingRecipe("1", ShadowFoxRecipes.recipesRainbowPetal),
									 PageCraftingRecipe("2", ShadowFoxRecipes.recipeRainbowPetalGrinding),
									 PageCraftingRecipe("3", ShadowFoxRecipes.recipeRainbowPetalBlock))
		
		coloredDirt = ShadowfoxLexiconEntry("coloredDirt", BotaniaAPI.categoryMisc, ShadowFoxBlocks.rainbowDirtBlock)
		coloredDirt.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ShadowFoxRecipes.recipesColoredDirt))
		
		aurora = ShadowfoxLexiconEntry("aurora", BotaniaAPI.categoryMisc, ShadowFoxBlocks.auroraDirt)
		aurora.setLexiconPages(PageText("0"),
							   PageCraftingRecipe("1", ShadowFoxRecipes.recipeAuroraDirt),
							   PageCraftingRecipe("2", ShadowFoxRecipes.recipeAuroraPlanks),
							   PageCraftingRecipe("3", ShadowFoxRecipes.recipeAuroraStairs),
							   PageCraftingRecipe("4", ShadowFoxRecipes.recipeAuroraSlabs),
							   PageCraftingRecipe("5", ShadowFoxRecipes.recipeAuroraSlabsToPlanks))
		
		technicolor = ShadowfoxLexiconEntry("technicolorRod", BotaniaAPI.categoryTools, ItemStack(ShadowFoxItems.colorfulSkyDirtRod, 1, 16))
		technicolor.setLexiconPages(PageText("0"),
									PageCraftingRecipe("1", ShadowFoxRecipes.recipesColoredSkyDirtRod),
									PageText("2"),
									PageCraftingRecipe("3", ShadowFoxRecipes.recipesPriestOfSif))
		
		lightningRod = ShadowfoxLexiconEntry("lightningRod", BotaniaAPI.categoryTools, ShadowFoxItems.lightningRod)
		lightningRod.setLexiconPages(PageText("0"),
									 PageCraftingRecipe("1", ShadowFoxRecipes.recipesLightningRod),
									 PageText("2"),
									 PageCraftingRecipe("3", ShadowFoxRecipes.recipesPriestOfThor))
		
		flameRod = ShadowfoxLexiconEntry("flameRod", BotaniaAPI.categoryTools, ShadowFoxItems.flameRod)
		flameRod.setLexiconPages(PageText("0"),
								 PageCraftingRecipe("1", ShadowFoxRecipes.recipesFlameRod),
								 PageText("2"),
								 PageCraftingRecipe("3", ShadowFoxRecipes.recipesPriestOfLoki))
		
		irisSapling = ShadowfoxLexiconEntry("irisSapling", dendrology, ShadowFoxBlocks.irisSapling)
		irisSapling.setPriority().setLexiconPages(PageText("0"),
												  PageCraftingRecipe("1", ShadowFoxRecipes.recipesWoodPanel),
												  PageCraftingRecipe("2", ShadowFoxRecipes.recipesStairs),
												  PageCraftingRecipe("3", ShadowFoxRecipes.recipesSlabs),
												  PageCraftingRecipe("4", ShadowFoxRecipes.recipesSlabsFull),
												  PageCraftingRecipe("5", ShadowFoxRecipes.recipesLeafDyes))
		
		pastoralSeeds = ShadowfoxLexiconEntry("irisSeeds", BotaniaAPI.categoryTools, ShadowFoxBlocks.rainbowGrass)
		pastoralSeeds.setLexiconPages(PageText("0"),
									  PageCraftingRecipe("1", ShadowFoxRecipes.recipesRedstoneRoot),
									  PageManaInfusionRecipe("2", ShadowFoxRecipes.recipesPastoralSeeds))
		
		coatOfArms = ShadowfoxLexiconEntry("coatOfArms", BotaniaAPI.categoryMisc, ItemStack(ShadowFoxItems.coatOfArms, 1, 16))
		coatOfArms.setLexiconPages(PageText("0"),
								   PageCraftingRecipe("1", ShadowFoxRecipes.recipesCoatOfArms))
		
		colorOverride = ShadowfoxLexiconEntry("colorOverride", BotaniaAPI.categoryMisc, ShadowFoxItems.colorOverride)
		colorOverride.setLexiconPages(PageText("0"),
									  PageCraftingRecipe("1", ShadowFoxRecipes.recipesColorOverride))
		
		interdictionRod = ShadowfoxLexiconEntry("interdictionRod", BotaniaAPI.categoryTools, ShadowFoxItems.interdictionRod)
		interdictionRod.setLexiconPages(PageText("0"),
										PageCraftingRecipe("1", ShadowFoxRecipes.recipesInterdictionRod),
										PageText("2"),
										PageCraftingRecipe("3", ShadowFoxRecipes.recipesPriestOfNjord))
		
		treeCrafting = ShadowfoxLexiconEntry("treeCrafting", dendrology, ShadowFoxBlocks.treeCrafterBlockRB)
		treeCrafting.setPriority().setLexiconPages(PageText("0"),
												   PageText("1"),
												   PageMultiblock("2", ShadowFoxBlocks.treeCrafter))
		
		attribution = ShadowfoxLexiconEntry("attribution", BotaniaAPI.categoryBaubles, ShadowFoxItems.attributionBauble)
		attribution.setLexiconPages(PageText("0"),
									PageCraftingRecipe("1", ShadowFoxRecipes.recipesAttribution))
		
		sealCreepers = ShadowfoxLexiconEntry("sealCreepers", BotaniaAPI.categoryBasics, ShadowFoxItems.wiltedLotus)
		if (AlfheimConfigHandler.blackLotusDropRate > 0.0)
			sealCreepers.setLexiconPages(PageText("0"),
										 PageText("1Drop"))
		else
			sealCreepers.setLexiconPages(PageText("0"),
										 PageText("1NoDrop"))
		
		kindling = ShadowfoxLexiconEntry("kindling", BotaniaAPI.categoryMisc, ShadowFoxBlocks.kindling)
		kindling.setLexiconPages(PageText("0"),
								 PageCraftingRecipe("1", ShadowFoxRecipes.recipesKindling))
		
		waveRod = ShadowfoxLexiconEntry("waveRod", BotaniaAPI.categoryTools, ShadowFoxItems.prismaticRod)
		waveRod.setLexiconPages(PageText("0"),
								PageCraftingRecipe("1", ShadowFoxRecipes.recipesRainbowRod))
		
		itemDisplay = ShadowfoxLexiconEntry("itemDisplay", BotaniaAPI.categoryMisc, ItemStack(ShadowFoxBlocks.itemDisplay, 1, 1))
		itemDisplay.setLexiconPages(PageText("0"),
									PageCraftingRecipe("1", ShadowFoxRecipes.recipesItemDisplay))
		
		livingwoodFunnel = ShadowfoxLexiconEntry("livingwoodFunnel", BotaniaAPI.categoryMisc, ItemStack(ShadowFoxBlocks.livingwoodFunnel, 1))
		livingwoodFunnel.setLexiconPages(PageText("0"),
										 PageCraftingRecipe("1", ShadowFoxRecipes.recipesLivingwoodFunnel))
		
		lightningSapling = ShadowfoxLexiconEntry("lightningSapling", dendrology, ShadowFoxBlocks.lightningSapling)
		lightningSapling.setLexiconPages(PageText("0"),
										 PageTreeCrafting("1", ShadowFoxRecipes.recipesLightningTree),
										 PageCraftingRecipe("2", ShadowFoxRecipes.recipesThunderousPlanks),
										 PageCraftingRecipe("3", ShadowFoxRecipes.recipesThunderousStairs),
										 PageCraftingRecipe("4", ShadowFoxRecipes.recipesThunderousSlabs),
										 PageCraftingRecipe("5", ShadowFoxRecipes.recipesThunderousTwig),
										 PageFurnaceRecipe("6", ItemStack(ShadowFoxBlocks.lightningPlanks)))
		
		netherSapling = ShadowfoxLexiconEntry("infernalSapling", dendrology, ShadowFoxBlocks.netherSapling)
		netherSapling.setLexiconPages(PageText("0"),
									  PageTreeCrafting("1", ShadowFoxRecipes.recipesInfernalTree),
									  PageCraftingRecipe("2", ShadowFoxRecipes.recipesInfernalPlanks),
									  PageCraftingRecipe("3", ShadowFoxRecipes.recipesInfernalStairs),
									  PageCraftingRecipe("4", ShadowFoxRecipes.recipesInfernalSlabs),
									  PageCraftingRecipe("5", ShadowFoxRecipes.recipesInfernalTwig),
									  PageFurnaceRecipe("6", ItemStack(ShadowFoxBlocks.netherWood)),
									  PageFurnaceRecipe("7", ItemStack(ShadowFoxBlocks.netherPlanks)))
		
		circuitSapling = ShadowfoxLexiconEntry("circuitSapling", dendrology, ShadowFoxBlocks.circuitSapling)
		circuitSapling.setLexiconPages(PageText("0"),
									   PageTreeCrafting("1", ShadowFoxRecipes.recipesCircuitTree),
									   PageCraftingRecipe("2", ShadowFoxRecipes.recipesCircuitPlanks),
									   PageCraftingRecipe("3", ShadowFoxRecipes.recipesCircuitStairs),
									   PageCraftingRecipe("4", ShadowFoxRecipes.recipesCircuitSlabs))
		
		calicoSapling = ShadowfoxLexiconEntry("calicoSapling", dendrology, ShadowFoxBlocks.calicoSapling)
		calicoSapling.setLexiconPages(PageText("0"),
									  PageTreeCrafting("1", ShadowFoxRecipes.recipesCalicoTree),
									  PageCraftingRecipe("2", ShadowFoxRecipes.recipesCalicoPlanks),
									  PageCraftingRecipe("3", ShadowFoxRecipes.recipesCalicoStairs),
									  PageCraftingRecipe("4", ShadowFoxRecipes.recipesCalicoSlabs))
		
		toolbelt = ShadowfoxLexiconEntry("toolbelt", BotaniaAPI.categoryBaubles, ShadowFoxItems.toolbelt)
		toolbelt.setLexiconPages(PageText("0"),
								 PageCraftingRecipe("1", ShadowFoxRecipes.recipesToolbelt))
		
		lamp = ShadowfoxLexiconEntry("lamp", BotaniaAPI.categoryMisc, ShadowFoxBlocks.irisLamp)
		lamp.setLexiconPages(PageText("0"),
							 PageCraftingRecipe("1", ShadowFoxRecipes.recipesLamp))
		
		silencer = ShadowfoxLexiconEntry("silencer", dendrology, ShadowFoxBlocks.sealingSapling)
		silencer.setLexiconPages(PageText("0"),
								 PageTreeCrafting("1", ShadowFoxRecipes.recipesSealingTree),
								 PageCraftingRecipe("2", ShadowFoxRecipes.recipesSealingPlanks),
								 PageCraftingRecipe("3", ShadowFoxRecipes.recipesSealingStairs),
								 PageCraftingRecipe("4", ShadowFoxRecipes.recipesSealingSlabs))
		
		amp = ShadowfoxLexiconEntry("amp", BotaniaAPI.categoryMisc, ShadowFoxBlocks.amp)
		amp.setLexiconPages(PageText("0"),
							PageCraftingRecipe("1", ShadowFoxRecipes.recipesAmplifier))
		
		crysanthermum = ShadowfoxLexiconEntry("crysanthermum", BotaniaAPI.categoryGenerationFlowers, BotaniaAPI.internalHandler.getSubTileAsStack("crysanthermum"))
		crysanthermum.setLexiconPages(PageText("0"),
									  PageText("1"),
									  PagePetalRecipe("2", ShadowFoxRecipes.recipeCrysanthermum))
		
		specialAxe = ShadowFoxRelicEntry("andmyaxe", BotaniaAPI.categoryAlfhomancy, ShadowFoxItems.wireAxe).setKnowledgeType(BotaniaAPI.relicKnowledge)
		specialAxe.setLexiconPages(PageText("0"),
								   PageText("1"))
		
		dagger = ShadowFoxRelicEntry("dagger", BotaniaAPI.categoryAlfhomancy, ShadowFoxItems.trisDagger).setKnowledgeType(BotaniaAPI.relicKnowledge)
		dagger.setLexiconPages(PageText("0"))
		
		frozenStar = ShadowfoxLexiconEntry("star", BotaniaAPI.categoryEnder, ItemStarPlacer.forColor(16))
		frozenStar.setLexiconPages(PageText("0"),
								   PageCraftingRecipe("1", ShadowFoxRecipes.recipesStar),
								   PageText("2"),
								   PageCraftingRecipe("3", ShadowFoxRecipes.recipesStar2))
		
		
		shimmer = ShadowfoxLexiconEntry("shimmer", BotaniaAPI.categoryMisc, ItemStack(ShadowFoxItems.resource, 1, 5))
		shimmer.setLexiconPages(PageText("0"),
								PageCraftingRecipe("1", ShadowFoxRecipes.recipeShimmerQuartz))
		
		throwablePotions = ShadowfoxLexiconEntry("throwablePotions", BotaniaAPI.categoryMisc, (ShadowFoxItems.splashPotion as ItemSplashPotion).getItemForBrew(ModBrews.absorption, null))
		throwablePotions.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ShadowFoxRecipes.recipesSplashPotions))
		
		if (ThaumcraftSuffusionRecipes.recipesLoaded) {
			tctrees = object: ShadowfoxLexiconEntry("tctrees", dendrology, ItemStack(ThaumcraftSuffusionRecipes.plantBlock)) {
				override fun getSubtitle() = "[Iridescence x Thaumcraft]"
			}
			tctrees.setLexiconPages(PageText("0"),
									PageTreeCrafting("1", ThaumcraftSuffusionRecipes.greatwoodRecipe),
									PageTreeCrafting("2", ThaumcraftSuffusionRecipes.silverwoodRecipe),
									PageText("3"),
									PageTreeCrafting("4", ThaumcraftSuffusionRecipes.shimmerleafRecipe),
									PageTreeCrafting("5", ThaumcraftSuffusionRecipes.cinderpearlRecipe),
									PageTreeCrafting("6", ThaumcraftSuffusionRecipes.vishroomRecipe))
			
			LexiconRecipeMappings.map(ItemStack(ThaumcraftSuffusionRecipes.plantBlock, 1, 0), tctrees, 1)
			LexiconRecipeMappings.map(ItemStack(ThaumcraftSuffusionRecipes.plantBlock, 1, 1), tctrees, 2)
			LexiconRecipeMappings.map(ItemStack(ThaumcraftSuffusionRecipes.plantBlock, 1, 2), tctrees, 4)
			LexiconRecipeMappings.map(ItemStack(ThaumcraftSuffusionRecipes.plantBlock, 1, 3), tctrees, 5)
			LexiconRecipeMappings.map(ItemStack(ThaumcraftSuffusionRecipes.plantBlock, 1, 5), tctrees, 6)
		}
		
		var memes = LexiconData.tinyPotato
		for (entry in BotaniaAPI.getAllEntries())
			if (entry.getUnlocalizedName() == "botania.entry.wrap")
				memes = entry
		
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.irisSapling), irisSapling, 0)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxItems.lightningRod), lightningRod, 1)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxItems.interdictionRod), interdictionRod, 1)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxItems.prismaticRod), waveRod, 1)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxItems.emblem, 1, 0), lightningRod, 3)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxItems.emblem, 1, 1), technicolor, 3)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxItems.emblem, 1, 2), interdictionRod, 3)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxItems.colorOverride), colorOverride, 1)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.irisLamp), lamp, 1)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxItems.wireAxe), specialAxe, 0)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxItems.trisDagger), dagger, 0)
		
		LexiconRecipeMappings.map(ItemStack(ShadowFoxItems.attributionBauble, 1, 0), attribution, 1)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxItems.attributionBauble, 1, 1), memes, 1)
		
		LexiconRecipeMappings.map(ItemStack(ShadowFoxItems.wiltedLotus, 1, 0), sealCreepers, 1)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxItems.wiltedLotus, 1, 1), sealCreepers, 1)
		
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.treeCrafterBlock), treeCrafting, 2)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.treeCrafterBlockRB), treeCrafting, 2)
		
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.lightningSapling), lightningSapling, 1)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.lightningWood), lightningSapling, 1)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.lightningLeaves), lightningSapling, 1)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.lightningPlanks), lightningSapling, 2)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.lightningSlabs), lightningSapling, 3)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.lightningStairs), lightningSapling, 4)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxItems.resource, 1, 0), lightningSapling, 5)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxItems.resource, 1, 1), lightningSapling, 6)
		
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.netherSapling), netherSapling, 1)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.netherWood), netherSapling, 1)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.netherLeaves), netherSapling, 1)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.netherPlanks), netherSapling, 2)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.netherSlabs), netherSapling, 3)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.netherStairs), netherSapling, 4)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxItems.resource, 1, 2), netherSapling, 5)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxItems.resource, 1, 3), netherSapling, 6)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxItems.resource, 1, 4), netherSapling, 7)
		
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.circuitWood), circuitSapling, 1)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.circuitLeaves), circuitSapling, 1)
		
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.calicoWood), calicoSapling, 1)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.calicoLeaves), calicoSapling, 1)
		
		for (i in 0..2)
			LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.shimmerQuartz, 1, i), shimmer, 0)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.shimmerQuartzSlab), shimmer, 0)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.shimmerQuartzStairs), shimmer, 0)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxItems.resource, 1, 5), shimmer, 1)
		
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.sealingSapling), silencer, 1)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.sealingWood), silencer, 1)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.sealingLeaves), silencer, 1)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.sealingPlanks), silencer, 2)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.sealingStairs), silencer, 3)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.sealingSlabs), silencer, 4)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.amp), amp, 1)
		
		LexiconRecipeMappings.map(ItemStack(ShadowFoxItems.toolbelt), toolbelt, 1)
		
		for (i in 0..2)
			LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.itemDisplay, 1, i), itemDisplay, 1)
		
		for (i in 0..3) {
			LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.irisWood0, 1, i), irisSapling, 1)
			LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.irisWood1, 1, i), irisSapling, 1)
			LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.irisWood2, 1, i), irisSapling, 1)
			LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.irisWood3, 1, i), irisSapling, 1)
			LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.altWood0, 1, i), irisSapling, 1)
		}
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.altWood1, 1, 0), irisSapling, 1)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.altWood1, 1, 1), irisSapling, 1)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.rainbowWood), irisSapling, 1)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.auroraWood), aurora, 0)
		for (i in 0..15) {
			LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.coloredPlanks, 1, i), irisSapling, 1)
			LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.coloredSlabs[i], 1), irisSapling, 2)
			LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.coloredStairs[i], 1), irisSapling, 3)
			LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.coloredDirtBlock, 1, i), coloredDirt, 1)
			LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.irisGrass, 1, i), pastoralSeeds, 0)
		}
		for (i in 0..5) {
			LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.altPlanks, 1, i), irisSapling, 1)
			LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.altSlabs[i], 1), irisSapling, 2)
			LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.altStairs[i], 1), irisSapling, 3)
		}
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.auroraLeaves), aurora, 0)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.rainbowLeaves), irisSapling, 5)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.rainbowPlanks), irisSapling, 1)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.rainbowSlabs), irisSapling, 2)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.rainbowStairs), irisSapling, 3)
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.rainbowDirtBlock), coloredDirt, 1)
		for (i in 0..1)
			LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.rainbowGrass, 1, i), pastoralSeeds, 0)
		
		for (i in 0..16) {
			LexiconRecipeMappings.map(ItemStack(ShadowFoxItems.colorfulSkyDirtRod, 1, i), technicolor, 1)
			LexiconRecipeMappings.map(ItemStack(ShadowFoxItems.irisSeeds, 1, i), pastoralSeeds, 2)
			LexiconRecipeMappings.map(ItemStack(ShadowFoxItems.coatOfArms, 1, i), coatOfArms, 1)
		}
		for (i in 0..7) {
			LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.irisLeaves0, 1, i), irisSapling, 0)
			LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.irisLeaves1, 1, i), irisSapling, 0)
			LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.irisTallGrass0, 1, i), pastoralSeeds, 0)
			LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.irisTallGrass1, 1, i), pastoralSeeds, 0)
		}
		
		LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.rainbowLeaves), irisSapling, 0)
		
		for (i in 0..1)
			LexiconRecipeMappings.map(ItemStack(ShadowFoxBlocks.rainbowTallGrass, 1, i), pastoralSeeds, 0)
		
		LexiconRecipeMappings.map(ItemStack(ShadowFoxItems.invisibleFlameLens), LexiconData.lenses, 0)
	}
	
	fun setKnowledgeTypes(kt: KnowledgeType) {
		technicolor.knowledgeType = kt
		lightningRod.knowledgeType = kt
		flameRod.knowledgeType = kt
		interdictionRod.knowledgeType = kt
		colorOverride.knowledgeType = kt
		waveRod.knowledgeType = kt
		toolbelt.knowledgeType = kt
		lamp.knowledgeType = kt
		silencer.knowledgeType = kt
		amp.knowledgeType = kt
		shimmer.knowledgeType = kt
		
		if (ThaumcraftSuffusionRecipes.recipesLoaded) {
			tctrees.knowledgeType = kt
		}
	}
}
