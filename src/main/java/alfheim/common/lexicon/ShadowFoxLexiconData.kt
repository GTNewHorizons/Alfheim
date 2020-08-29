package alfheim.common.lexicon

import alfheim.api.lib.LibOreDict
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.tile.TileTreeCrafter
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.crafting.recipe.ShadowFoxRecipes
import alfheim.common.integration.thaumcraft.ThaumcraftSuffusionRecipes
import alfheim.common.item.*
import alfheim.common.item.block.ItemStarPlacer
import alfheim.common.item.material.ElvenResourcesMetas
import alfheim.common.lexicon.page.PageFurnaceRecipe
import net.minecraft.item.ItemStack
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.lexicon.*
import vazkii.botania.api.lexicon.multiblock.MultiblockSet
import vazkii.botania.common.brew.ModBrews
import vazkii.botania.common.lexicon.LexiconData
import vazkii.botania.common.lexicon.page.*

@Suppress("JoinDeclarationAndAssignment")
object ShadowFoxLexiconData {
	
	val rainbowFlora: LexiconEntry
	val aurora: LexiconEntry
	val coloredDirt: LexiconEntry
	val irisSapling: LexiconEntry
	val prismaticRod: LexiconEntry
	val pastoralSeeds: LexiconEntry
	val coatOfArms: LexiconEntry
	val colorOverride: LexiconEntry
	val treeCrafting: LexiconEntry
	val dendrology: ShadowFoxLexiconCategory
	val sealCreepers: LexiconEntry
	val kindling: LexiconEntry
	val itemDisplay: LexiconEntry
	val lightningSapling: LexiconEntry
	val livingwoodFunnel: LexiconEntry
	val netherSapling: LexiconEntry
	val circuitSapling: LexiconEntry
	val calicoSapling: LexiconEntry
	//val toolbelt: LexiconEntry
	val lamp: LexiconEntry
	val silencer: LexiconEntry
	val amplifier: LexiconEntry
	val crysanthermum: LexiconEntry
	val specialAxe: LexiconEntry
	val frozenStar: LexiconEntry
	val dagger: LexiconEntry
	val shimmer: LexiconEntry
	val throwablePotions: LexiconEntry
	
	var treeCrafter: MultiblockSet = TileTreeCrafter.makeMultiblockSet()
	
	lateinit var tctrees: LexiconEntry
	
	init {
		
		dendrology = ShadowFoxLexiconCategory("dendrology", 1)
		
		rainbowFlora = ShadowfoxLexiconEntry("rainbowFlora", AlfheimLexiconData.categoryAlfheim, ItemStack(AlfheimBlocks.rainbowGrass, 1, 2))
		rainbowFlora.setLexiconPages(PageText("0"),
									 PageCraftingRecipe("1", ShadowFoxRecipes.recipesRainbowPetal),
									 PageCraftingRecipe("2", ShadowFoxRecipes.recipeRainbowPetalGrinding),
									 PageCraftingRecipe("3", ShadowFoxRecipes.recipeRainbowPetalBlock))
		
		coloredDirt = ShadowfoxLexiconEntry("coloredDirt", AlfheimLexiconData.categoryAlfheim, AlfheimBlocks.rainbowDirt)
		coloredDirt.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ShadowFoxRecipes.recipesColoredDirt))
		
		aurora = ShadowfoxLexiconEntry("aurora", AlfheimLexiconData.categoryAlfheim, AlfheimBlocks.auroraDirt)
		aurora.setLexiconPages(PageText("0"),
							   PageCraftingRecipe("1", ShadowFoxRecipes.recipeAuroraDirt),
							   PageCraftingRecipe("2", ShadowFoxRecipes.recipeAuroraPlanks),
							   PageCraftingRecipe("3", ShadowFoxRecipes.recipeAuroraStairs),
							   PageCraftingRecipe("4", ShadowFoxRecipes.recipeAuroraSlabs),
							   PageCraftingRecipe("5", ShadowFoxRecipes.recipeAuroraSlabsToPlanks))
		
		prismaticRod = ShadowfoxLexiconEntry("rodPrismatic", AlfheimLexiconData.categoryAlfheim, AlfheimItems.rodPrismatic)
		prismaticRod.setLexiconPages(PageText("0"),
									 PageCraftingRecipe("1", ShadowFoxRecipes.recipesRainbowRod))
		
		irisSapling = ShadowfoxLexiconEntry("irisSapling", dendrology, AlfheimBlocks.irisSapling)
		irisSapling.setPriority().setLexiconPages(PageText("0"),
												  PageCraftingRecipe("1", ShadowFoxRecipes.recipesWoodPanel),
												  PageCraftingRecipe("2", ShadowFoxRecipes.recipesStairs),
												  PageCraftingRecipe("3", ShadowFoxRecipes.recipesSlabs),
												  PageCraftingRecipe("4", ShadowFoxRecipes.recipesSlabsFull),
												  PageCraftingRecipe("5", ShadowFoxRecipes.recipesLeafDyes))
		
		pastoralSeeds = ShadowfoxLexiconEntry("irisSeeds", AlfheimLexiconData.categoryAlfheim, AlfheimBlocks.rainbowGrass)
		pastoralSeeds.setLexiconPages(PageText("0"),
									  PageCraftingRecipe("1", ShadowFoxRecipes.recipesRedstoneRoot),
									  PageManaInfusionRecipe("2", ShadowFoxRecipes.recipesPastoralSeeds))
		
		coatOfArms = ShadowfoxLexiconEntry("coatOfArms", AlfheimLexiconData.categoryAlfheim, ItemStack(AlfheimItems.coatOfArms, 1, 16))
		coatOfArms.setLexiconPages(PageText("0"),
								   PageCraftingRecipe("1", ShadowFoxRecipes.recipesCoatOfArms))
		
		colorOverride = ShadowfoxLexiconEntry("colorOverride", AlfheimLexiconData.categoryAlfheim, AlfheimItems.colorOverride)
		colorOverride.setLexiconPages(PageText("0"),
									  PageCraftingRecipe("1", ShadowFoxRecipes.recipesColorOverride))
		
		treeCrafting = ShadowfoxLexiconEntry("treeCrafting", dendrology, AlfheimBlocks.treeCrafterBlockRB)
		treeCrafting.setPriority().setLexiconPages(PageText("0"),
												   PageText("1"),
												   PageMultiblock("2", treeCrafter))
		
		sealCreepers = ShadowfoxLexiconEntry("sealCreepers", AlfheimLexiconData.categoryAlfheim, AlfheimItems.wiltedLotus)
		if (AlfheimConfigHandler.blackLotusDropRate > 0.0)
			sealCreepers.setLexiconPages(PageText("0"),
										 PageText("1Drop"))
		else
			sealCreepers.setLexiconPages(PageText("0"),
										 PageText("1NoDrop"))
		
		kindling = ShadowfoxLexiconEntry("kindling", AlfheimLexiconData.categoryAlfheim, AlfheimBlocks.kindling)
		kindling.setLexiconPages(PageText("0"),
								 PageCraftingRecipe("1", ShadowFoxRecipes.recipesKindling))
		
		itemDisplay = ShadowfoxLexiconEntry("itemDisplay", AlfheimLexiconData.categoryAlfheim, ItemStack(AlfheimBlocks.itemDisplay, 1, 1))
		itemDisplay.setLexiconPages(PageText("0"),
									PageCraftingRecipe("1", ShadowFoxRecipes.recipesItemDisplay))
		
		livingwoodFunnel = ShadowfoxLexiconEntry("livingwoodFunnel", AlfheimLexiconData.categoryAlfheim, ItemStack(AlfheimBlocks.livingwoodFunnel, 1))
		livingwoodFunnel.setLexiconPages(PageText("0"),
										 PageCraftingRecipe("1", ShadowFoxRecipes.recipesLivingwoodFunnel))
		
		lightningSapling = ShadowfoxLexiconEntry("lightningSapling", dendrology, AlfheimBlocks.lightningSapling)
		lightningSapling.setLexiconPages(PageText("0"),
										 PageTreeCrafting("1", ShadowFoxRecipes.recipesLightningTree),
										 PageCraftingRecipe("2", ShadowFoxRecipes.recipesThunderousPlanks),
										 PageCraftingRecipe("3", ShadowFoxRecipes.recipesThunderousStairs),
										 PageCraftingRecipe("4", ShadowFoxRecipes.recipesThunderousSlabs),
										 PageCraftingRecipe("5", ShadowFoxRecipes.recipesThunderousTwig),
										 PageFurnaceRecipe("6", ItemStack(AlfheimBlocks.lightningPlanks)))
		
		netherSapling = ShadowfoxLexiconEntry("infernalSapling", dendrology, AlfheimBlocks.netherSapling)
		netherSapling.setLexiconPages(PageText("0"),
									  PageTreeCrafting("1", ShadowFoxRecipes.recipesInfernalTree),
									  PageCraftingRecipe("2", ShadowFoxRecipes.recipesInfernalPlanks),
									  PageCraftingRecipe("3", ShadowFoxRecipes.recipesInfernalStairs),
									  PageCraftingRecipe("4", ShadowFoxRecipes.recipesInfernalSlabs),
									  PageCraftingRecipe("5", ShadowFoxRecipes.recipesInfernalTwig),
									  PageFurnaceRecipe("6", ItemStack(AlfheimBlocks.netherWood)),
									  PageFurnaceRecipe("7", ItemStack(AlfheimBlocks.netherPlanks)))
		
		circuitSapling = ShadowfoxLexiconEntry("circuitSapling", dendrology, AlfheimBlocks.circuitSapling)
		circuitSapling.setLexiconPages(PageText("0"),
									   PageTreeCrafting("1", ShadowFoxRecipes.recipesCircuitTree),
									   PageCraftingRecipe("2", ShadowFoxRecipes.recipesCircuitPlanks),
									   PageCraftingRecipe("3", ShadowFoxRecipes.recipesCircuitStairs),
									   PageCraftingRecipe("4", ShadowFoxRecipes.recipesCircuitSlabs))
		
		calicoSapling = ShadowfoxLexiconEntry("calicoSapling", dendrology, AlfheimBlocks.calicoSapling)
		calicoSapling.setLexiconPages(PageText("0"),
									  PageTreeCrafting("1", ShadowFoxRecipes.recipesCalicoTree),
									  PageCraftingRecipe("2", ShadowFoxRecipes.recipesCalicoPlanks),
									  PageCraftingRecipe("3", ShadowFoxRecipes.recipesCalicoStairs),
									  PageCraftingRecipe("4", ShadowFoxRecipes.recipesCalicoSlabs))
		
		/*toolbelt = ShadowfoxLexiconEntry("toolbelt", AlfheimLexiconData.categoryAlfheim, AlfheimItems.toolbelt)
		toolbelt.setLexiconPages(PageText("0"),
								 PageCraftingRecipe("1", ShadowFoxRecipes.recipesToolbelt))*/
		
		lamp = ShadowfoxLexiconEntry("lamp", AlfheimLexiconData.categoryAlfheim, AlfheimBlocks.irisLamp)
		lamp.setLexiconPages(PageText("0"),
							 PageCraftingRecipe("1", ShadowFoxRecipes.recipesLamp))
		
		silencer = ShadowfoxLexiconEntry("silencer", dendrology, AlfheimBlocks.sealingSapling)
		silencer.setLexiconPages(PageText("0"),
								 PageTreeCrafting("1", ShadowFoxRecipes.recipesSealingTree),
								 PageCraftingRecipe("2", ShadowFoxRecipes.recipesSealingPlanks),
								 PageCraftingRecipe("3", ShadowFoxRecipes.recipesSealingStairs),
								 PageCraftingRecipe("4", ShadowFoxRecipes.recipesSealingSlabs))
		
		amplifier = ShadowfoxLexiconEntry("amplifier", AlfheimLexiconData.categoryAlfheim, AlfheimBlocks.amplifier)
		amplifier.setLexiconPages(PageText("0"),
								  PageCraftingRecipe("1", ShadowFoxRecipes.recipesAmplifier))
		
		crysanthermum = ShadowfoxLexiconEntry("crysanthermum", AlfheimLexiconData.categoryAlfheim, BotaniaAPI.internalHandler.getSubTileAsStack("crysanthermum"))
		crysanthermum.setLexiconPages(PageText("0"), PageText("1"), PageText("2"),
									  PagePetalRecipe("3", ShadowFoxRecipes.recipeCrysanthermum))
		
		specialAxe = ShadowFoxRelicEntry("andmyaxe", AlfheimLexiconData.categoryAlfheim, AlfheimItems.wireAxe).setKnowledgeType(BotaniaAPI.relicKnowledge)
		specialAxe.setLexiconPages(PageText("0"),
								   PageText("1"))
		
		dagger = ShadowFoxRelicEntry("dagger", AlfheimLexiconData.categoryAlfheim, AlfheimItems.trisDagger).setKnowledgeType(BotaniaAPI.relicKnowledge)
		dagger.setLexiconPages(PageText("0"))
		
		frozenStar = ShadowfoxLexiconEntry("starBlock", AlfheimLexiconData.categoryAlfheim, ItemStarPlacer.forColor(16))
		frozenStar.setLexiconPages(PageText("0"),
								   PageCraftingRecipe("1", ShadowFoxRecipes.recipesStar),
								   PageText("2"),
								   PageCraftingRecipe("3", ShadowFoxRecipes.recipesStar2))
		
		
		shimmer = ShadowfoxLexiconEntry("shimmer", AlfheimLexiconData.categoryAlfheim, ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.RainbowQuartz))
		shimmer.setLexiconPages(PageText("0"),
								PageCraftingRecipe("1", ShadowFoxRecipes.recipeShimmerQuartz))
		
		throwablePotions = ShadowfoxLexiconEntry("throwablePotions", AlfheimLexiconData.categoryAlfheim, (AlfheimItems.splashPotion as ItemSplashPotion).getItemForBrew(ModBrews.absorption, null))
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
		
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.irisSapling), irisSapling, 0)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.rodPrismatic), prismaticRod, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.colorOverride), colorOverride, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.irisLamp), lamp, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.wireAxe), specialAxe, 0)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.trisDagger), dagger, 0)
		
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.attributionBauble, 1, 1), memes, 1)
		
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.wiltedLotus, 1, 0), sealCreepers, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.wiltedLotus, 1, 1), sealCreepers, 1)
		
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.treeCrafterBlock), treeCrafting, 2)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.treeCrafterBlockRB), treeCrafting, 2)
		
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.lightningSapling), lightningSapling, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.lightningWood), lightningSapling, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.lightningLeaves), lightningSapling, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.lightningPlanks), lightningSapling, 2)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.lightningSlabs), lightningSapling, 3)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.lightningStairs), lightningSapling, 4)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.ThunderwoodTwig), lightningSapling, 5)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.ThunderwoodSplinters), lightningSapling, 6)
		
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.netherSapling), netherSapling, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.netherWood), netherSapling, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.netherLeaves), netherSapling, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.netherPlanks), netherSapling, 2)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.netherSlabs), netherSapling, 3)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.netherStairs), netherSapling, 4)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.NetherwoodTwig), netherSapling, 5)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.NetherwoodSplinters), netherSapling, 6)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.NetherwoodCoal), netherSapling, 7)
		
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.circuitWood), circuitSapling, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.circuitLeaves), circuitSapling, 1)
		
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.calicoWood), calicoSapling, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.calicoLeaves), calicoSapling, 1)
		
		for (i in 0..2)
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.shimmerQuartz, 1, i), shimmer, 0)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.shimmerQuartzSlab), shimmer, 0)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.shimmerQuartzStairs), shimmer, 0)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.RainbowQuartz), shimmer, 1)
		
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.sealingSapling), silencer, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.sealingWood), silencer, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.sealingLeaves), silencer, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.sealingPlanks), silencer, 2)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.sealingStairs), silencer, 3)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.sealingSlabs), silencer, 4)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.amplifier), amplifier, 1)
		
		//LexiconRecipeMappings.map(ItemStack(AlfheimItems.toolbelt), toolbelt, 1)
		
		for (i in 0..2)
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.itemDisplay, 1, i), itemDisplay, 1)
		
		for (i in 0..3) {
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.irisWood0, 1, i), irisSapling, 1)
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.irisWood1, 1, i), irisSapling, 1)
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.irisWood2, 1, i), irisSapling, 1)
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.irisWood3, 1, i), irisSapling, 1)
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.altWood0, 1, i), irisSapling, 1)
		}
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.altWood1, 1, 0), irisSapling, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.altWood1, 1, 1), irisSapling, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.rainbowWood), irisSapling, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.auroraWood), aurora, 0)
		for (i in 0..15) {
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.irisPlanks, 1, i), irisSapling, 1)
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.irisSlabs[i], 1), irisSapling, 2)
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.irisStairs[i], 1), irisSapling, 3)
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.irisDirt, 1, i), coloredDirt, 1)
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.irisGrass, 1, i), pastoralSeeds, 0)
		}
		for (i in 0 until LibOreDict.ALT_TYPES.size - 2) {
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.altPlanks, 1, i), irisSapling, 1)
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.altSlabs, 1, i), irisSapling, 2)
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.altStairs[i], 1), irisSapling, 3)
		}
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.auroraLeaves), aurora, 0)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.rainbowLeaves), irisSapling, 5)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.rainbowPlanks), irisSapling, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.rainbowSlab), irisSapling, 2)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.rainbowStairs), irisSapling, 3)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.rainbowDirt), coloredDirt, 1)
		for (i in 0..1)
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.rainbowGrass, 1, i), pastoralSeeds, 0)
		
		for (i in 0..16) {
			LexiconRecipeMappings.map(ItemStack(AlfheimItems.irisSeeds, 1, i), pastoralSeeds, 2)
			LexiconRecipeMappings.map(ItemStack(AlfheimItems.coatOfArms, 1, i), coatOfArms, 1)
		}
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.coatOfArms, 1, 18), coatOfArms, 1)
		for (i in 0..7) {
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.irisLeaves0, 1, i), irisSapling, 0)
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.irisLeaves1, 1, i), irisSapling, 0)
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.irisTallGrass0, 1, i), pastoralSeeds, 0)
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.irisTallGrass1, 1, i), pastoralSeeds, 0)
		}
		
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.rainbowLeaves), irisSapling, 0)
		
		for (i in 0..1)
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.rainbowTallGrass, 1, i), pastoralSeeds, 0)
		
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.invisibleFlameLens), LexiconData.lenses, 0)
		
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.rainbowMushroom), LexiconData.mushrooms, 1)
	}
	
	fun setKnowledgeTypes(kt: KnowledgeType) {
		colorOverride.knowledgeType = kt
		prismaticRod.knowledgeType = kt
		//toolbelt.knowledgeType = kt
		lamp.knowledgeType = kt
		silencer.knowledgeType = kt
		amplifier.knowledgeType = kt
		shimmer.knowledgeType = kt
		
		if (ThaumcraftSuffusionRecipes.recipesLoaded) {
			tctrees.knowledgeType = kt
		}
	}
}
