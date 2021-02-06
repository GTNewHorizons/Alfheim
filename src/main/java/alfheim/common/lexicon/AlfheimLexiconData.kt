package alfheim.common.lexicon

import alexsocol.asjlib.extendables.block.BlockModMeta
import alfheim.AlfheimCore
import alfheim.api.*
import alfheim.api.lib.LibOreDict
import alfheim.common.achievement.AlfheimAchievements
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.tile.*
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.handler.CardinalSystem.KnowledgeSystem.Knowledge
import alfheim.common.crafting.recipe.AlfheimRecipes
import alfheim.common.integration.thaumcraft.ThaumcraftSuffusionRecipes
import alfheim.common.integration.tinkersconstruct.TinkersConstructAlfheimConfig
import alfheim.common.item.*
import alfheim.common.item.block.ItemStarPlacer
import alfheim.common.item.material.*
import alfheim.common.lexicon.page.*
import net.minecraft.item.ItemStack
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.lexicon.*
import vazkii.botania.common.block.*
import vazkii.botania.common.brew.ModBrews
import vazkii.botania.common.item.ModItems
import vazkii.botania.common.lexicon.LexiconData
import vazkii.botania.common.lexicon.page.*
import java.util.*

object AlfheimLexiconData {
	
	lateinit var categoryAlfheim: LexiconCategory
	lateinit var categotyDendrology: AlfheimLexiconCategory
	lateinit var categoryDivinity: LexiconCategory
	
	/** Lore alfheim page  */
	lateinit var alfheim: LexiconEntry
	
	/** Lore elves page  */
	lateinit var elves: LexiconEntry
	
	// Main addon content
	lateinit var advancedMana: LexiconEntry
	lateinit var amplifier: LexiconEntry
	lateinit var amuletCirus: LexiconEntry
	lateinit var amuletIceberg: LexiconEntry
	lateinit var amuletNimbus: LexiconEntry
	lateinit var amulterCrescent: LexiconEntry
	lateinit var animatedTorch: LexiconEntry
	lateinit var anomaly: LexiconEntry
	lateinit var anyavil: LexiconEntry
	lateinit var astrolabe: LexiconEntry
	lateinit var aurora: LexiconEntry
	lateinit var beltRation: LexiconEntry
	lateinit var calicoSapling: LexiconEntry
	lateinit var circuitSapling: LexiconEntry
	lateinit var cloakInvis: LexiconEntry
	lateinit var coatOfArms: LexiconEntry
	lateinit var colorOverride: LexiconEntry
	lateinit var coloredDirt: LexiconEntry
	lateinit var corpInj: LexiconEntry
	lateinit var corpSeq: LexiconEntry
	lateinit var dagger: LexiconEntry
	lateinit var dasGold: LexiconEntry
	lateinit var elementalSet: LexiconEntry
	lateinit var elvenSet: LexiconEntry
	lateinit var elvorium: LexiconEntry
	lateinit var enderAct: LexiconEntry
	lateinit var essences: LexiconEntry
	lateinit var excaliber: LexiconEntry
	lateinit var flowerCrysanthermum: LexiconEntry
	lateinit var flowerEnderchid: LexiconEntry
	lateinit var flowerPetronia: LexiconEntry
	lateinit var flowerRain: LexiconEntry
	lateinit var flowerSnow: LexiconEntry
	lateinit var flowerStorm: LexiconEntry
	lateinit var flowerWind: LexiconEntry
	lateinit var flugel: LexiconEntry
	lateinit var frozenStar: LexiconEntry
	lateinit var gleipnir: LexiconEntry
	lateinit var gungnir: LexiconEntry
	lateinit var hyperBucket: LexiconEntry
	lateinit var infuser: LexiconEntry
	lateinit var irisSapling: LexiconEntry
	lateinit var itemDisplay: LexiconEntry
	lateinit var kindling: LexiconEntry
	lateinit var lamp: LexiconEntry
	lateinit var lembas: LexiconEntry
	lateinit var lightningSapling: LexiconEntry
	lateinit var livingwoodFunnel: LexiconEntry
	lateinit var lootInt: LexiconEntry
	lateinit var manaAccelerator: LexiconEntry
	lateinit var manaImba: LexiconEntry
	lateinit var manaLamp: LexiconEntry
	lateinit var mask: LexiconEntry
	lateinit var mitten: LexiconEntry
	lateinit var mjolnir: LexiconEntry
	lateinit var mobs: LexiconEntry
	lateinit var moonbow: LexiconEntry
	lateinit var multbauble: LexiconEntry
	lateinit var netherSapling: LexiconEntry
	lateinit var ores: LexiconEntry
	lateinit var pastoralSeeds: LexiconEntry
	lateinit var pixie: LexiconEntry
	lateinit var portal: LexiconEntry
	lateinit var pylons: LexiconEntry
	lateinit var rainbowFlora: LexiconEntry
	lateinit var reality: LexiconEntry
	lateinit var ringAnomaly: LexiconEntry
	lateinit var ringDodge: LexiconEntry
	lateinit var ringManaDrive: LexiconEntry
	lateinit var ringSpider: LexiconEntry
	lateinit var ringsAura: LexiconEntry
	lateinit var rodClick: LexiconEntry
	lateinit var rodGreen: LexiconEntry
	lateinit var rodPrismatic: LexiconEntry
	lateinit var ruling: LexiconEntry
	lateinit var runes: LexiconEntry
	lateinit var sealCreepers: LexiconEntry
	lateinit var shimmer: LexiconEntry
	lateinit var shrines: LexiconEntry
	lateinit var silencer: LexiconEntry
	lateinit var soul: LexiconEntry
	lateinit var soulHorn: LexiconEntry
	lateinit var specialAxe: LexiconEntry
	lateinit var subspear: LexiconEntry
	lateinit var tctrees: LexiconEntry
	lateinit var throwablePotions: LexiconEntry
	lateinit var trade: LexiconEntry
	lateinit var treeCrafting: LexiconEntry
	lateinit var triquetrum: LexiconEntry
	lateinit var uberSpreader: LexiconEntry
	lateinit var winery: LexiconEntry
	lateinit var worldgen: LexiconEntry
	
	// Elven Story information
	var esm: LexiconEntry? = null
	var races: LexiconEntry? = null
	
	// MMO info
	var parties: LexiconEntry? = null
	var spells: LexiconEntry? = null
	var targets: LexiconEntry? = null
	
	// divinity
	lateinit var divIntro: LexiconEntry
	
	lateinit var cloakThor: LexiconEntry
	lateinit var cloakSif: LexiconEntry
	lateinit var cloakNjord: LexiconEntry
	lateinit var cloakLoki: LexiconEntry
	lateinit var cloakHeimdall: LexiconEntry
	lateinit var cloakOdin: LexiconEntry
	
	lateinit var emblemThor: LexiconEntry
	lateinit var emblemSif: LexiconEntry
	lateinit var emblemNjord: LexiconEntry
	lateinit var emblemLoki: LexiconEntry
	lateinit var emblemHeimdall: LexiconEntry
	lateinit var emblemOdin: LexiconEntry
	
	lateinit var ringSif: LexiconEntry
	lateinit var ringNjord: LexiconEntry
	lateinit var ringHeimdall: LexiconEntry
	
	lateinit var rodThor: LexiconEntry
	lateinit var rodSif: LexiconEntry
	lateinit var rodNjord: LexiconEntry
	lateinit var rodLoki: LexiconEntry
	lateinit var rodOdin: LexiconEntry
	
	fun preInit() {
		categoryAlfheim		= AlfheimLexiconCategory("Alfheim", 5)
		categoryDivinity	= AlfheimLexiconCategory("Divinity", 5)
		categotyDendrology = AlfheimLexiconCategory("dendrology", 1)
		
		advancedMana		= AlfheimLexiconEntry("advMana", categoryAlfheim)
		alfheim				= AlfheimLexiconEntry("alfheim", categoryAlfheim)
		amuletCirus			= AlfheimLexiconEntry("amulCirs", categoryAlfheim)
		amulterCrescent		= AlfheimLexiconEntry("crescent", categoryAlfheim)
		amuletIceberg		= AlfheimLexiconEntry("iceberg", categoryAlfheim)
		amuletNimbus		= AlfheimLexiconEntry("amulNimb", categoryAlfheim)
		animatedTorch		= AlfheimLexiconEntry("aniTorch", categoryAlfheim)
		anomaly				= AlfheimLexiconEntry("anomaly", categoryAlfheim)
		anyavil				= AlfheimLexiconEntry("anyavil", categoryAlfheim)
		astrolabe			= AlfheimLexiconEntry("astrolab", categoryAlfheim)
		beltRation			= AlfheimLexiconEntry("ration", categoryAlfheim)
		cloakInvis			= AlfheimLexiconEntry("cloakInv", categoryAlfheim)
		corpInj				= AlfheimLexiconEntry("corpInj", categoryAlfheim)
		corpSeq				= AlfheimLexiconEntry("corpSeq", categoryAlfheim)
		dasGold				= AlfheimLexiconEntry("dasGold", categoryAlfheim)
		elementalSet		= AlfheimLexiconEntry("elemSet", categoryAlfheim)
		elvenSet			= AlfheimLexiconEntry("elvenSet", categoryAlfheim)
		elves				= AlfheimLexiconEntry("elves", categoryAlfheim)
		enderAct			= AlfheimLexiconEntry("endAct", categoryAlfheim)
		essences			= AlfheimLexiconEntry("essences", categoryAlfheim)
		elvorium			= AlfheimLexiconEntry("elvorium", categoryAlfheim)
		flowerCrysanthermum	= AlfheimLexiconEntry("crysanthermum", categoryAlfheim)
		flowerEnderchid		= AlfheimLexiconEntry("flowerEnderchid", categoryAlfheim)
		flowerPetronia		= AlfheimLexiconEntry("flowerPetronia", categoryAlfheim)
		flowerRain			= AlfheimLexiconEntry("flowerRain", categoryAlfheim)
		flowerSnow			= AlfheimLexiconEntry("flowerSnow", categoryAlfheim)
		flowerStorm			= AlfheimLexiconEntry("flowerStorm", categoryAlfheim)
		flowerWind			= AlfheimLexiconEntry("flowerWind", categoryAlfheim)
		flugel				= AlfheimLexiconEntry("flugel", categoryAlfheim)
		hyperBucket			= AlfheimLexiconEntry("hyperBuk", categoryAlfheim)
		infuser				= AlfheimLexiconEntry("infuser", categoryAlfheim)
		lembas				= AlfheimLexiconEntry("lembas", categoryAlfheim)
		lootInt				= AlfheimLexiconEntry("lootInt", categoryAlfheim)
		manaAccelerator		= AlfheimLexiconEntry("itemHold", categoryAlfheim)
		manaImba			= AlfheimLexiconEntry("manaImba", categoryAlfheim)
		manaLamp			= AlfheimLexiconEntry("manaLamp", categoryAlfheim)
		mitten				= AlfheimLexiconEntry("mitten", categoryAlfheim)
		mobs				= AlfheimLexiconEntry("mobs", categoryAlfheim)
		multbauble			= AlfheimLexiconEntry("multbaub", categoryAlfheim)
		ores				= AlfheimLexiconEntry("ores", categoryAlfheim)
		pixie				= AlfheimLexiconEntry("pixie", categoryAlfheim)
		portal				= AlfheimLexiconEntry("portal", categoryAlfheim)
		pylons				= AlfheimLexiconEntry("pylons", categoryAlfheim)
		reality				= AlfheimLexiconEntry("reality", categoryAlfheim)
		ringsAura			= AlfheimLexiconEntry("auraAlf", categoryAlfheim)
		ringAnomaly			= AlfheimLexiconEntry("anomaRing", categoryAlfheim)
		ringDodge			= AlfheimLexiconEntry("dodgRing", categoryAlfheim)
		ringManaDrive		= AlfheimLexiconEntry("manaDrive", categoryAlfheim)
		ringSpider			= AlfheimLexiconEntry("spider", categoryAlfheim)
		rodClick			= AlfheimLexiconEntry("rodClick", categoryAlfheim)
		rodGreen			= AlfheimLexiconEntry("greenRod", categoryAlfheim)
		ruling				= AlfheimLexiconEntry("ruling", categoryAlfheim)
		runes				= AlfheimLexiconEntry("runes", categoryAlfheim)
		shrines				= AlfheimLexiconEntry("shrines", categoryAlfheim)
		//stories			= AlfheimLexiconEntry("stories", categoryAlfheim)
		trade				= AlfheimLexiconEntry("trade", categoryAlfheim)
		triquetrum			= AlfheimLexiconEntry("triquetrum", categoryAlfheim)
		uberSpreader		= AlfheimLexiconEntry("uberSpreader", categoryAlfheim)
		winery				= AlfheimLexiconEntry("winery", categoryAlfheim)
		worldgen			= AlfheimLexiconEntry("worldgen", categoryAlfheim)
		
		
		
		divIntro			= AlfheimLexiconEntry("divinity_intro",	categoryDivinity)
		
		cloakThor			= AlfheimLexiconEntry("garb_thor",		categoryDivinity)
		cloakSif			= AlfheimLexiconEntry("garb_sif",			categoryDivinity)
		cloakNjord			= AlfheimLexiconEntry("garb_njord",		categoryDivinity)
		cloakLoki			= AlfheimLexiconEntry("garb_loki",		categoryDivinity)
		cloakHeimdall		= AlfheimLexiconEntry("garb_heimdall",	categoryDivinity)
		cloakOdin			= AlfheimLexiconEntry("garb_odin",		categoryDivinity)
		
		emblemThor			= AlfheimLexiconEntry("thor",				categoryDivinity)
		emblemSif			= AlfheimLexiconEntry("sif",				categoryDivinity)
		emblemNjord			= AlfheimLexiconEntry("njord",			categoryDivinity)
		emblemLoki			= AlfheimLexiconEntry("loki",				categoryDivinity)
		emblemHeimdall		= AlfheimLexiconEntry("heimdall",			categoryDivinity)
		emblemOdin			= AlfheimLexiconEntry("odin",				categoryDivinity)
		
		rodThor				= AlfheimLexiconEntry("rod_thor",			categoryDivinity)
		rodSif				= AlfheimLexiconEntry("rod_sif",			categoryDivinity)
		rodNjord			= AlfheimLexiconEntry("rod_njord",		categoryDivinity)
		rodLoki				= AlfheimLexiconEntry("rod_loki",			categoryDivinity)
		rodOdin				= AlfheimLexiconEntry("rod_odin",			categoryDivinity)
		
		if (AlfheimConfigHandler.enableElvenStory)
			preInitElvenStory()
	}
	
	private fun preInitElvenStory() {
		if (esm == null)	esm		= AlfheimLexiconEntry("es", categoryAlfheim)
		if (races == null)	races	= AlfheimLexiconEntry("races",	categoryAlfheim)
		
		if (AlfheimConfigHandler.enableMMO)
			preInitMMO()
	}
	
	private fun preInitMMO() {
		if (parties == null)	parties	= AlfheimLexiconEntry("parties",	categoryAlfheim)
		if (spells == null)		spells	= AlfheimLexiconEntry("spells",		categoryAlfheim)
		if (targets == null)	targets	= AlfheimLexiconEntry("targets",	categoryAlfheim)
	}
	
	fun init() {
		advancedMana.setLexiconPages(PageText("0"), PageText("1"),
									 PageManaInfusorRecipe("2", AlfheimRecipes.recipeManaStone),
									 PageManaInfusorRecipe("3", AlfheimRecipes.recipeManaStoneGreater),
									 PageText("4"),
									 PageCraftingRecipe("5", AlfheimRecipes.recipeManaRingElven),
									 PageCraftingRecipe("6", AlfheimRecipes.recipeManaRingGod)).icon = ItemStack(AlfheimItems.manaStone)
		
		alfheim.setLexiconPages(PageText("0"), PageText("1")).setPriority()
		
		amplifier = AlfheimLexiconEntry("amplifier", categoryAlfheim, AlfheimBlocks.amplifier)
		amplifier.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeAmplifier))
		
		amuletCirus.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeCloudPendant))
		
		amulterCrescent.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeCrescentAmulet))
		
		amuletIceberg.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipePendantSuperIce))
		
		amuletNimbus.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeCloudPendantSuper))
		
		animatedTorch.setLexiconPages(PageText("0"), PageText("1"), PageText("2"), PageCraftingRecipe("3", AlfheimRecipes.recipeAnimatedTorch))
		
		anomaly.setLexiconPages(PageText("0")).setPriority()
		
		var index = -1
		for (name in AlfheimAPI.anomalies.keys) {
			if (name == "Lightning") index = anomaly.pages.size + 1
			anomaly.setLexiconPages(PageImage("$name.t", ModInfo.MODID + ":textures/gui/entries/Anomaly" + name + ".png"), PageText("$name.d"))
		}
		
		var pg: LexiconPage = PageTextLearnableKnowledge("${ModInfo.MODID}.page.anomalyLightning.d", Knowledge.PYLONS)
		anomaly.pages[index] = pg
		pg.onPageAdded(anomaly, index)
		
		anyavil.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeAnyavil))
		
		astrolabe.setLexiconPages(PageText("0"), PageText("1"), PageCraftingRecipe("2", AlfheimRecipes.recipeAstrolabe))
		
		aurora = AlfheimLexiconEntry("aurora", categoryAlfheim, AlfheimBlocks.auroraDirt)
		aurora.setLexiconPages(PageText("0"),
							   PageCraftingRecipe("1", AlfheimRecipes.recipeAuroraDirt),
							   PageCraftingRecipe("2", AlfheimRecipes.recipeAuroraPlanks),
							   PageCraftingRecipe("3", AlfheimRecipes.recipeAuroraStairs),
							   PageCraftingRecipe("4", AlfheimRecipes.recipeAuroraSlabs),
							   PageCraftingRecipe("5", AlfheimRecipes.recipeAuroraPlanksFromSlabs))
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.auroraLeaves), aurora, 0)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.auroraWood), aurora, 0)
		
		beltRation.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeRationBelt))
		
		cloakInvis.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeInvisibilityCloak))
		
		coatOfArms = AlfheimLexiconEntry("coatOfArms", categoryAlfheim, ItemStack(AlfheimItems.coatOfArms, 1, 16))
		coatOfArms.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipesCoatOfArms))
		for (i in 0..18)
			LexiconRecipeMappings.map(ItemStack(AlfheimItems.coatOfArms, 1, i), coatOfArms, 1)
		
		coloredDirt = AlfheimLexiconEntry("coloredDirt", categoryAlfheim, AlfheimBlocks.rainbowDirt)
		coloredDirt.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipesColoredDirt))
		for (i in 0..15)
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.irisDirt, 1, i), coloredDirt, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.rainbowDirt), coloredDirt, 1)
		
		colorOverride = AlfheimLexiconEntry("colorOverride", categoryAlfheim, AlfheimItems.colorOverride)
		colorOverride.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeColorOverride))
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.colorOverride), colorOverride, 1)
		
		corpInj.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeInjector))
		
		corpSeq.setLexiconPages(PageText("0"), PageText("1"), PageText("2"), PageCraftingRecipe("3", AlfheimRecipes.recipeAutocrafter))
		
		dagger = AlfheimRelicLexiconEntry("dagger", categoryAlfheim, AlfheimItems.trisDagger)
		dagger.setLexiconPages(PageText("0"))
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.trisDagger), dagger, 0)
		
		dasGold.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeRelicCleaner))
		
		elementalSet.setLexiconPages(PageText("0"),
									 PageCraftingRecipe("1", AlfheimRecipes.recipeElementalHelmet),
									 PageCraftingRecipe("2", AlfheimRecipes.recipeElementalChestplate),
									 PageCraftingRecipe("3", AlfheimRecipes.recipeElementalLeggings),
									 PageCraftingRecipe("4", AlfheimRecipes.recipeElementalBoots)).icon = ItemStack(AlfheimItems.elementalHelmet)
		AlfheimItems.elementalHelmetRevealing?.let { elementalSet.addExtraDisplayedRecipe(ItemStack(it)) }
		
		elvenSet.setLexiconPages(PageText("0"),
								 PageCraftingRecipe("1", AlfheimRecipes.recipeElvoriumHelmet),
								 PageCraftingRecipe("2", AlfheimRecipes.recipeElvoriumChestplate),
								 PageCraftingRecipe("3", AlfheimRecipes.recipeElvoriumLeggings),
								 PageCraftingRecipe("4", AlfheimRecipes.recipeElvoriumBoots)).icon = ItemStack(AlfheimItems.elvoriumHelmet)
		AlfheimItems.elvoriumHelmetRevealing?.let { elvenSet.addExtraDisplayedRecipe(ItemStack(it)) }
		
		elves.setLexiconPages(PageText("0"), PageText("1"), PageText("2"), PageText("3"), PageText("4")).setPriority()
		
		elvorium.setLexiconPages(PageText("0"), PageManaInfusorRecipe("1", AlfheimRecipes.recipeElvorium)).icon = ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.alfStorage, 1, 0), elvorium, 0)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.ElvoriumNugget), elvorium, 0)
		
		enderAct.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeEnderActuator))
		
		essences.setLexiconPages(PageText("0"),
								 PageTextLearnableAchievement("2", AlfheimAchievements.flugelHardKill),
								 PageText("4"), PageText("5"), PageCraftingRecipe("6", listOf(AlfheimRecipes.recipeMuspelheimPowerIngot, AlfheimRecipes.recipeNiflheimPowerIngot)),
								 PageText("7"), PageManaInfusorRecipe("8", AlfheimRecipes.recipeMauftrium)).icon = ItemStack(ModItems.manaResource, 1, 5)
		essences.addExtraDisplayedRecipe(ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.NiflheimPowerIngot))
		essences.addExtraDisplayedRecipe(ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.NiflheimEssence))
		essences.addExtraDisplayedRecipe(ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.MuspelheimEssence))
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.alfStorage, 1, 1), essences, 6)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.alfStorage, 1, 2), essences, 4)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.alfStorage, 1, 3), essences, 4)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.MuspelheimEssence), essences, 2)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.NiflheimEssence), essences, 2)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.MauftriumNugget), essences, 6)
		
		flowerCrysanthermum	.setLexiconPages(PageText("0"), PageText("1"), PageText("2"), PagePetalRecipe("3", AlfheimRecipes.recipeCrysanthermum))
		flowerEnderchid		.setLexiconPages(PageText("0"), PagePetalRecipe("1", AlfheimRecipes.recipeOrechidEndium)).icon = BotaniaAPI.internalHandler.getSubTileAsStack("orechidEndium")
		flowerPetronia		.setLexiconPages(PageText("0"), PagePetalRecipe("1", AlfheimRecipes.recipePetronia)).icon = BotaniaAPI.internalHandler.getSubTileAsStack("petronia")
		flowerRain			.setLexiconPages(PageText("0"), PagePetalRecipe("1", AlfheimRecipes.recipeRainFlower)).icon = BotaniaAPI.internalHandler.getSubTileAsStack("rainFlower")
		flowerSnow			.setLexiconPages(PageText("0"), PagePetalRecipe("1", AlfheimRecipes.recipeSnowFlower)).icon = BotaniaAPI.internalHandler.getSubTileAsStack("snowFlower")
		flowerStorm			.setLexiconPages(PageText("0"), PagePetalRecipe("1", AlfheimRecipes.recipeStormFlower)).icon = BotaniaAPI.internalHandler.getSubTileAsStack("stormFlower")
		flowerWind			.setLexiconPages(PageText("0"), PagePetalRecipe("1", AlfheimRecipes.recipeWindFlower)).icon = BotaniaAPI.internalHandler.getSubTileAsStack("windFlower")
		
		flugel.setLexiconPages(PageText("0"), PageText("1"), PageText("2")).icon = ItemStack(ModItems.flightTiara, 1, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.flugelDisc), flugel, 0)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.flugelHead), flugel, 0)
		
		frozenStar = AlfheimLexiconEntry("starBlock", categoryAlfheim, ItemStarPlacer.forColor(16))
		frozenStar.setLexiconPages(PageText("0"),
								   PageCraftingRecipe("1", AlfheimRecipes.recipesStar),
								   PageText("2"),
								   PageCraftingRecipe("3", AlfheimRecipes.recipesStar2))
		
		hyperBucket.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeHyperBucket))
		
		irisSapling = AlfheimLexiconEntry("irisSapling", categotyDendrology, AlfheimBlocks.irisSapling)
		irisSapling.setPriority().setLexiconPages(PageText("0"),
												  PageCraftingRecipe("1", AlfheimRecipes.recipesColoredPlanks),
												  PageCraftingRecipe("2", AlfheimRecipes.recipesColoredStairs),
												  PageCraftingRecipe("3", AlfheimRecipes.recipesColoredSlabs),
												  PageCraftingRecipe("4", AlfheimRecipes.recipesColoredPlanksFromSlabs),
												  PageCraftingRecipe("5", AlfheimRecipes.recipesLeafDyes))
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.irisSapling), irisSapling, 0)
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
		for (i in 0..15) {
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.irisPlanks, 1, i), irisSapling, 1)
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.irisSlabs[i], 1), irisSapling, 2)
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.irisStairs[i], 1), irisSapling, 3)
		}
		for (i in 0 until LibOreDict.ALT_TYPES.size - 2) {
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.altPlanks, 1, i), irisSapling, 1)
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.altSlabs, 1, i), irisSapling, 2)
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.altStairs[i], 1), irisSapling, 3)
		}
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.rainbowLeaves), irisSapling, 5)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.rainbowPlanks), irisSapling, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.rainbowSlab), irisSapling, 2)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.rainbowStairs), irisSapling, 3)
		for (i in 0..7) {
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.irisLeaves0, 1, i), irisSapling, 0)
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.irisLeaves1, 1, i), irisSapling, 0)
		}
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.rainbowLeaves), irisSapling, 0)
		
		infuser.setLexiconPages(PageText("0"), PageText("1"),
								PageCraftingRecipe("2", AlfheimRecipes.recipeManaInfusionCore),
								PageCraftingRecipe("3", AlfheimRecipes.recipeManaInfuser),
								PageText("4"),
								PageMultiblockLearnable("5", AlfheimMultiblocks.infuserU, AlfheimMultiblocks.infuser, AlfheimAchievements.infuser)).icon = ItemStack(AlfheimBlocks.manaInfuser)
		
		itemDisplay = AlfheimLexiconEntry("itemDisplay", categoryAlfheim, ItemStack(AlfheimBlocks.itemDisplay, 1, 1))
		itemDisplay.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipesItemDisplay))
		for (i in 0..2)
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.itemDisplay, 1, i), itemDisplay, 1)
		
		kindling = AlfheimLexiconEntry("kindling", categoryAlfheim, AlfheimBlocks.kindling)
		kindling.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeKindling))
		
		lamp = AlfheimLexiconEntry("lamp", categoryAlfheim, AlfheimBlocks.irisLamp)
		lamp.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeLamp))
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.irisLamp), lamp, 1)
		
		lembas.setLexiconPages(PageText("0"), PageText("1"), PageCraftingRecipe("2", AlfheimRecipes.recipeLembas))
		
		livingwoodFunnel = AlfheimLexiconEntry("livingwoodFunnel", categoryAlfheim, ItemStack(AlfheimBlocks.livingwoodFunnel, 1))
		livingwoodFunnel.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeLivingwoodFunnel))
		
		lootInt.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeLootInterceptor))
		
		manaAccelerator.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeItemHolder))
		
		manaImba.setLexiconPages(PageText("0"), PageText("1"), PageText("2"), PageCraftingRecipe("3", AlfheimRecipes.recipeManaMirrorImba))
		
		manaLamp.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeEnlighter))
		
		mitten.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeManaweaveGlove))
		
		mobs.setLexiconPages(PageText("0"), PageText("1")).icon = ItemStack(ModItems.manaResource, 1, 8)
		
		multbauble.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeMultibauble))
		
		ores.setLexiconPages(PageText("0"), PageText("1"), PageText("2")).icon = ItemStack(AlfheimBlocks.elvenOre, 1, 4)
		for (i in 0 until (AlfheimBlocks.elvenOre as BlockModMeta).subtypes)
			ores.addExtraDisplayedRecipe(ItemStack(AlfheimBlocks.elvenOre, 1, i))
		
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.elvenOre, 1, 1), ores, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.elvenOre, 1, 0), ores, 2)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.elvenOre, 1, 2), ores, 2)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.elvenOre, 1, 3), ores, 2)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.elvenOre, 1, 4), ores, 3)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.elvenOre, 1, 5), ores, 2)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.IffesalDust), ores, 3)
		
		pastoralSeeds = AlfheimLexiconEntry("irisSeeds", categoryAlfheim, AlfheimBlocks.rainbowGrass)
		pastoralSeeds.setLexiconPages(PageText("0"),
									  PageCraftingRecipe("1", AlfheimRecipes.recipesRedstoneRoot),
									  PageManaInfusionRecipe("2", AlfheimRecipes.recipesPastoralSeeds))
		for (i in 0..1) {
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.rainbowGrass, 1, i), pastoralSeeds, 0)
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.rainbowTallGrass, 1, i), pastoralSeeds, 0)
		}
		for (i in 0..7) {
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.irisTallGrass0, 1, i), pastoralSeeds, 0)
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.irisTallGrass1, 1, i), pastoralSeeds, 0)
		}
		for (i in 0..15)
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.irisGrass, 1, i), pastoralSeeds, 0)
		for (i in 0..16)
			LexiconRecipeMappings.map(ItemStack(AlfheimItems.irisSeeds, 1, i), pastoralSeeds, 2)
		
		pixie.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipePixieAttractor)).icon = ItemStack(AlfheimItems.pixieAttractor)
		
		portal.setLexiconPages(PageText("0"), PageText("1"), PageText("2"),
							 PageCraftingRecipe("3", AlfheimRecipes.recipeAlfheimPortal),
							 PageText("4"), PageElvenRecipe("5", AlfheimRecipes.recipeInterdimensional),
							 PageMultiblock("6", AlfheimMultiblocks.portal),
							 PageText("7"), PageText(if (AlfheimConfigHandler.destroyPortal) "8" else "8s")).setPriority()
		
		pylons.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeElvenPylon), PageCraftingRecipe("2", AlfheimRecipes.recipeGaiaPylon)).setPriority().icon = ItemStack(AlfheimBlocks.alfheimPylon, 1, 0)
		
		rainbowFlora = AlfheimLexiconEntry("rainbowFlora", categoryAlfheim, ItemStack(AlfheimBlocks.rainbowGrass, 1, 2))
		rainbowFlora.setLexiconPages(PageText("0"),
									 PageCraftingRecipe("1", AlfheimRecipes.recipesRainbowPetal),
									 PageCraftingRecipe("2", AlfheimRecipes.recipeRainbowPetalGrinding),
									 PageCraftingRecipe("3", AlfheimRecipes.recipeRainbowPetalBlock))
		
		reality.setLexiconPages(PageText("0"), PageText("1"), PageCraftingRecipe("2", AlfheimRecipes.recipeSword))
		
		ringAnomaly.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeSpatiotemporal))
		
		ringsAura.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeAuraRingElven), PageCraftingRecipe("2", AlfheimRecipes.recipeAuraRingGod)).icon = ItemStack(AlfheimItems.auraRingElven)
		
		ringDodge.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeDodgeRing))
		
		ringManaDrive.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeRingFeedFlower))
		
		ringSpider.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeRingSpider))
		
		rodClick.setLexiconPages(PageText("0"), PageText("1"), PageText("2"), PageCraftingRecipe("3", AlfheimRecipes.recipeRodClicker))
		
		rodGreen.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeRodGreen))
		
		rodPrismatic = AlfheimLexiconEntry("rodPrismatic", categoryAlfheim, AlfheimItems.rodPrismatic)
		rodPrismatic.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeRodPrismatic))
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.rodPrismatic), rodPrismatic, 1)
		
		ruling.setLexiconPages(PageText("0"), PageText("1"),
							   PageCraftingRecipe("2", AlfheimRecipes.recipeRodMuspelheim),
							   PageCraftingRecipe("3", AlfheimRecipes.recipeRodNiflheim),
							   PageText("4"), PageCraftingRecipe("5", listOf(AlfheimRecipes.recipeMuspelheimPendant, AlfheimRecipes.recipeNiflheimPendant))).icon = ItemStack(AlfheimItems.rodMuspelheim)
		
		runes.setLexiconPages(PageText("0"), PageRuneRecipe("1", listOf(AlfheimRecipes.recipeMuspelheimRune, AlfheimRecipes.recipeNiflheimRune)),
							  PageText("2"), PageText("3"), PageRuneRecipe("4", AlfheimRecipes.recipeRealityRune)).icon = ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.PrimalRune)
		runes.addExtraDisplayedRecipe(ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.NiflheimRune))
		
		sealCreepers = AlfheimLexiconEntry("sealCreepers", categoryAlfheim, AlfheimItems.wiltedLotus)
		sealCreepers.setLexiconPages(PageText("0"), PageText("1${ if (AlfheimConfigHandler.blackLotusDropRate > 0.0) "" else "No" }Drop"))
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.wiltedLotus, 1, 0), sealCreepers, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.wiltedLotus, 1, 1), sealCreepers, 1)
		
		shimmer = AlfheimLexiconEntry("shimmer", categoryAlfheim, ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.RainbowQuartz))
		shimmer.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeShimmerQuartz))
		for (i in 0..2)
			LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.shimmerQuartz, 1, i), shimmer, 0)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.shimmerQuartzSlab), shimmer, 0)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.shimmerQuartzStairs), shimmer, 0)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.RainbowQuartz), shimmer, 1)
		
		shrines.setLexiconPages(PageText("0"), PageText("1")).icon = ItemStack(AlfheimBlocks.powerStone)
		
		specialAxe = AlfheimRelicLexiconEntry("andmyaxe", categoryAlfheim, AlfheimItems.wireAxe)
		specialAxe.setLexiconPages(PageText("0"), PageText("1"))
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.wireAxe), specialAxe, 0)
		
		//stories.setLexiconPages(PageText("0"), PageText("1"), PageText("2"), PageText("3")).icon = ItemStack(AlfheimItems.storyToken)
		
		throwablePotions = AlfheimLexiconEntry("throwablePotions", categoryAlfheim, (AlfheimItems.splashPotion as ItemSplashPotion).getItemForBrew(ModBrews.absorption, null))
		throwablePotions.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeSplashPotions))
		
		trade.setLexiconPages(PageText("0"), PageText("1"),
							  PageCraftingRecipe("2", AlfheimRecipes.recipeElvoriumPylon),
							  PageCraftingRecipe("3", AlfheimRecipes.recipeTradePortal),
							  PageMultiblock("4", AlfheimMultiblocks.yordin)).icon = ItemStack(AlfheimBlocks.tradePortal)
		
		triquetrum.setLexiconPages(PageText("0"), PageText("1"), PageCraftingRecipe("2", AlfheimRecipes.recipeTriquetrum))
		
		uberSpreader.setLexiconPages(PageText("0"), PageText("1"),
									 if (AlfheimCore.TiCLoaded && !AlfheimCore.stupidMode && AlfheimConfigHandler.materialIDs[TinkersConstructAlfheimConfig.MAUFTRIUM] != -1) PageText("2t")
									 else PageCraftingRecipe(if (AlfheimCore.stupidMode) "2s" else "2", AlfheimRecipes.recipeUberSpreader)).icon = ItemStack(ModBlocks.spreader, 1, 4)
		LexiconRecipeMappings.map(ItemStack(ModBlocks.spreader, 1, 4), uberSpreader, 2)
		
		winery.setLexiconPages(PageText("0"), PageText("1"), PageText("2"), PageText("3"),
							   PageText("4"), PageText("5"), PageText("6"), PageText("7"),
							   PageText("8"), PageText("9"), PageText("10"), PageText("11"),
							   PageCraftingRecipe("12", AlfheimRecipes.recipeBarrel),
							   PageCraftingRecipe("13", AlfheimRecipes.recipeJug))
		winery.addExtraDisplayedRecipe(ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.GrapeLeaf))
		winery.addExtraDisplayedRecipe(ItemStack(AlfheimItems.elvenFood, 1, ElvenFoodMetas.Nectar))
		winery.addExtraDisplayedRecipe(ItemStack(AlfheimItems.elvenFood, 1, ElvenFoodMetas.RedGrapes))
		winery.addExtraDisplayedRecipe(ItemStack(AlfheimItems.elvenFood, 1, ElvenFoodMetas.WhiteGrapes))
		winery.addExtraDisplayedRecipe(ItemStack(AlfheimItems.elvenFood, 1, ElvenFoodMetas.RedWine))
		winery.addExtraDisplayedRecipe(ItemStack(AlfheimItems.elvenFood, 1, ElvenFoodMetas.WhiteWine))
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.GrapeLeaf), winery, 13)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.elvenFood, 1, ElvenFoodMetas.Nectar), winery, 13)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.elvenFood, 1, ElvenFoodMetas.RedGrapes), winery, 13)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.elvenFood, 1, ElvenFoodMetas.WhiteGrapes), winery, 13)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.elvenFood, 1, ElvenFoodMetas.RedWine), winery, 13)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.elvenFood, 1, ElvenFoodMetas.WhiteWine), winery, 13)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.grapesRed[0]), winery, 13)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.grapesRed[1]), winery, 13)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.grapesRed[2]), winery, 13)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.grapesRedPlanted), winery, 13)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.grapesWhite), winery, 13)
		
		worldgen.setLexiconPages(PageTextLearnableKnowledge("0", Knowledge.GLOWSTONE),
								 PagePureDaisyRecipe("1", AlfheimRecipes.recipeDreamwood),
								 // PageCraftingRecipe("2", AlfheimRecipes.recipeGlowstone),
								 PageText("3"),
								 PageCraftingRecipe("4", AlfheimRecipes.recipeLivingcobble),
								 PageCraftingRecipe("5", AlfheimRecipes.recipeLivingrockPickaxe),
								 PageCraftingRecipe("6", AlfheimRecipes.recipeFurnace)).icon = ItemStack(AlfheimBlocks.altLeaves, 1, 7)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.elvenSand), worldgen, 3)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.altWood1, 1, 3), worldgen, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.altLeaves, 1, 7), worldgen, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.dreamSapling), worldgen, 1)
		
		if (AlfheimConfigHandler.enableElvenStory) initElvenStory()
		
		// ################################################################
		
		treeCrafting = AlfheimLexiconEntry("treeCrafting", categotyDendrology, AlfheimBlocks.treeCrafterBlockRB)
		treeCrafting.setPriority().setLexiconPages(PageText("0"),
												   PageText("1"),
												   PageMultiblock("2", AlfheimMultiblocks.treeCrafter))
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.treeCrafterBlock), treeCrafting, 2)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.treeCrafterBlockRB), treeCrafting, 2)
		
		lightningSapling = AlfheimLexiconEntry("lightningSapling", categotyDendrology, AlfheimBlocks.lightningSapling)
		lightningSapling.setLexiconPages(PageText("0"),
										 PageTreeCrafting("1", AlfheimRecipes.recipeLightningTree),
										 PageCraftingRecipe("2", AlfheimRecipes.recipeThunderousPlanks),
										 PageCraftingRecipe("3", AlfheimRecipes.recipeThunderousStairs),
										 PageCraftingRecipe("4", AlfheimRecipes.recipeThunderousSlabs),
										 PageCraftingRecipe("5", AlfheimRecipes.recipeThunderousTwig),
										 PageFurnaceRecipe("6", ItemStack(AlfheimBlocks.lightningPlanks)))
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.lightningSapling), lightningSapling, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.lightningWood), lightningSapling, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.lightningLeaves), lightningSapling, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.lightningPlanks), lightningSapling, 2)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.lightningSlabs), lightningSapling, 3)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.lightningStairs), lightningSapling, 4)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.ThunderwoodTwig), lightningSapling, 5)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.ThunderwoodSplinters), lightningSapling, 6)
		
		netherSapling = AlfheimLexiconEntry("infernalSapling", categotyDendrology, AlfheimBlocks.netherSapling)
		netherSapling.setLexiconPages(PageText("0"),
									  PageTreeCrafting("1", AlfheimRecipes.recipeInfernalTree),
									  PageCraftingRecipe("2", AlfheimRecipes.recipeInfernalPlanks),
									  PageCraftingRecipe("3", AlfheimRecipes.recipeInfernalStairs),
									  PageCraftingRecipe("4", AlfheimRecipes.recipeInfernalSlabs),
									  PageCraftingRecipe("5", AlfheimRecipes.recipeInfernalTwig),
									  PageFurnaceRecipe("6", ItemStack(AlfheimBlocks.netherWood)),
									  PageFurnaceRecipe("7", ItemStack(AlfheimBlocks.netherPlanks)))
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.netherSapling), netherSapling, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.netherWood), netherSapling, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.netherLeaves), netherSapling, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.netherPlanks), netherSapling, 2)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.netherSlabs), netherSapling, 3)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.netherStairs), netherSapling, 4)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.NetherwoodTwig), netherSapling, 5)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.NetherwoodSplinters), netherSapling, 6)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.NetherwoodCoal), netherSapling, 7)
		
		circuitSapling = AlfheimLexiconEntry("circuitSapling", categotyDendrology, AlfheimBlocks.circuitSapling)
		circuitSapling.setLexiconPages(PageText("0"),
									   PageTreeCrafting("1", AlfheimRecipes.recipeCircuitTree),
									   PageCraftingRecipe("2", AlfheimRecipes.recipeCircuitPlanks),
									   PageCraftingRecipe("3", AlfheimRecipes.recipeCircuitStairs),
									   PageCraftingRecipe("4", AlfheimRecipes.recipeCircuitSlabs))
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.circuitWood), circuitSapling, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.circuitLeaves), circuitSapling, 1)
		
		calicoSapling = AlfheimLexiconEntry("calicoSapling", categotyDendrology, AlfheimBlocks.calicoSapling)
		calicoSapling.setLexiconPages(PageText("0"),
									  PageTreeCrafting("1", AlfheimRecipes.recipeCalicoTree),
									  PageCraftingRecipe("2", AlfheimRecipes.recipeCalicoPlanks),
									  PageCraftingRecipe("3", AlfheimRecipes.recipeCalicoStairs),
									  PageCraftingRecipe("4", AlfheimRecipes.recipeCalicoSlabs))
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.calicoWood), calicoSapling, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.calicoLeaves), calicoSapling, 1)
		
		silencer = AlfheimLexiconEntry("silencer", categotyDendrology, AlfheimBlocks.sealingSapling)
		silencer.setLexiconPages(PageText("0"),
								 PageTreeCrafting("1", AlfheimRecipes.recipeSealingTree),
								 PageCraftingRecipe("2", AlfheimRecipes.recipeSealingPlanks),
								 PageCraftingRecipe("3", AlfheimRecipes.recipeSealingStairs),
								 PageCraftingRecipe("4", AlfheimRecipes.recipeSealingSlabs))
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.sealingSapling), silencer, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.sealingWood), silencer, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.sealingLeaves), silencer, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.sealingPlanks), silencer, 2)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.sealingStairs), silencer, 3)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.sealingSlabs), silencer, 4)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.amplifier), amplifier, 1)
		
		if (ThaumcraftSuffusionRecipes.recipesLoaded) {
			tctrees = object: AlfheimLexiconEntry("tctrees", categotyDendrology, ItemStack(ThaumcraftSuffusionRecipes.plantBlock)) {
				override fun getSubtitle() = "[Alfheim x Thaumcraft]"
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
		
		// ################################################################
		
		divIntro		.setLexiconPages(PageText("0"), PageText("1"), PageCraftingRecipe("2", AlfheimRecipes.recipeAttribution), PageText("3")).setPriority()
		
		cloakThor		.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeCloakThor)		)
		cloakSif		.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeCloakSif)		)
		cloakNjord		.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeCloakNjord)	)
		cloakLoki		.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeCloakLoki)		)
		cloakHeimdall	.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeCloakHeimdall)	)
		cloakOdin		.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeCloakOdin)		)
		
		emblemThor		.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipePriestOfThor)		)
		emblemSif		.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipePriestOfSif)		)
		emblemNjord		.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipePriestOfNjord)		)
		emblemLoki		.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipePriestOfLoki)		)
		emblemHeimdall	.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipePriestOfHeimdall)	)
		emblemOdin		.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipePriestOfOdin)		)
		
		rodThor			.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeRodLightning),			PageText("2"))
		rodSif			.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipesRodColoredSkyDirt),	PageText("2")).icon = ItemStack(AlfheimItems.rodColorfulSkyDirt, 1, 16)
		rodNjord		.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeRodInterdiction),		PageText("2"))
		rodLoki			.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeRodFlame),				PageText("2"))
		rodOdin			.setLexiconPages(PageText("0"), PageText("1"), PageCraftingRecipe("2", AlfheimRecipes.recipeRodPortal), PageText("3"))
		
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.rodColorfulSkyDirt), rodSif, 1)
		
//		var memes = LexiconData.tinyPotato
//		for (entry in BotaniaAPI.getAllEntries())
//			if (entry.getUnlocalizedName() == "botania.entry.wrap")
//				memes = entry
//
//		LexiconRecipeMappings.map(ItemStack(AlfheimItems.attributionBauble, 1, 1), memes, 1)
		
		LexiconData.thorRing.apply {
			pages[0].unlocalizedName += "n"
			category.entries.remove(this)
			category = categoryDivinity
			categoryDivinity.entries.add(this)
		}
		
		LexiconData.lokiRing.apply {
			pages[0].unlocalizedName += "n"
			pages[3].unlocalizedName += "n"
			
			category.entries.remove(this)
			category = categoryDivinity
			categoryDivinity.entries.add(this)
		}
		
		LexiconData.odinRing.apply {
			pages[0].unlocalizedName += "n"
			category.entries.remove(this)
			category = categoryDivinity
			categoryDivinity.entries.add(this)
		}
		
		// ################################################################
		// ################################################################
		// ################################################################
		
		LexiconData.gaiaRitual.pages.clear()
		LexiconData.gaiaRitual.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeGaiaPylon),
											   PageMultiblock("2", ModMultiblocks.gaiaRitual), PageText("3"), PageText("4"),
											   PageText("5"))
		
		LexiconData.sparks.pages.clear()
		LexiconData.sparks.setLexiconPages(PageText("0"), PageText("1"),
										   PageCraftingRecipe("2", AlfheimRecipes.recipesSpark),
										   PageText("3"))
		
		LexiconData.cosmeticBaubles.setLexiconPages(PageCraftingRecipe("34", AlfheimRecipes.recipeThinkingHand))
		
		pg = PageText("botania.page.lens38")
		LexiconData.lenses.pages.add(pg)
		pg.onPageAdded(LexiconData.lenses, 38)
		pg = PageCraftingRecipe("botania.page.lens39", AlfheimRecipes.recipeLensMessenger)
		LexiconData.lenses.pages.add(pg)
		pg.onPageAdded(LexiconData.lenses, 39)
		LexiconRecipeMappings.map(ItemStack(ModItems.lens, 1, 22), LexiconData.lenses, 38)
		
		pg = PageText("botania.page.elvenLenses11")
		LexiconData.elvenLenses.pages.add(pg)
		pg.onPageAdded(LexiconData.elvenLenses, 11)
		pg = PageCraftingRecipe("botania.page.elvenLenses12", AlfheimRecipes.recipeLensTripwire)
		LexiconData.elvenLenses.pages.add(pg)
		pg.onPageAdded(LexiconData.elvenLenses, 12)
		LexiconRecipeMappings.map(ItemStack(ModItems.lens, 1, 23), LexiconData.elvenLenses, 11)
		
		pg = PageText("botania.page.lens40")
		LexiconData.lenses.pages.add(pg)
		pg.onPageAdded(LexiconData.lenses, 40)
		pg = PageCraftingRecipe("botania.page.lens41", AlfheimRecipes.recipeLensPush)
		LexiconData.lenses.pages.add(pg)
		pg.onPageAdded(LexiconData.lenses, 41)
		LexiconRecipeMappings.map(ItemStack(ModItems.lens, 1, 24), LexiconData.lenses, 40)
		
		pg = PageText("botania.page.lens42")
		LexiconData.lenses.pages.add(pg)
		pg.onPageAdded(LexiconData.lenses, 42)
		pg = PageCraftingRecipe("botania.page.lens43", AlfheimRecipes.recipeLensSmelt)
		LexiconData.lenses.pages.add(pg)
		pg.onPageAdded(LexiconData.lenses, 43)
		LexiconRecipeMappings.map(ItemStack(ModItems.lens, 1, 25), LexiconData.lenses, 42)
		
		pg = PageText("botania.page.lens44")
		LexiconData.lenses.pages.add(pg)
		pg.onPageAdded(LexiconData.lenses, 44)
		pg = PageCraftingRecipe("botania.page.lens45", AlfheimRecipes.recipeLensSuperconductor)
		LexiconData.lenses.pages.add(pg)
		pg.onPageAdded(LexiconData.lenses, 45)
		LexiconRecipeMappings.map(ItemStack(ModItems.lens, 1, 26), LexiconData.lenses, 44)
		
		pg = PageText("botania.page.lens46")
		LexiconData.lenses.pages.add(pg)
		pg.onPageAdded(LexiconData.lenses, 46)
		pg = PageCraftingRecipe("botania.page.lens47", AlfheimRecipes.recipeLensTrack)
		LexiconData.lenses.pages.add(pg)
		pg.onPageAdded(LexiconData.lenses, 47)
		LexiconRecipeMappings.map(ItemStack(ModItems.lens, 1, 27), LexiconData.lenses, 46)
		
		pg = PageText("botania.page.judgementCloaks1n")
		LexiconData.judgementCloaks.pages[1] = pg
		pg.onPageAdded(LexiconData.judgementCloaks, 1)
		pg = PageCraftingRecipe("botania.page.judgementCloaks4", AlfheimRecipes.recipeBalanceCloak)
		LexiconData.judgementCloaks.pages.add(pg)
		pg.onPageAdded(LexiconData.judgementCloaks, 4)
		
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.invisibleFlameLens), LexiconData.lenses, 0)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.rainbowMushroom), LexiconData.mushrooms, 1)
		
		setKnowledgeTypes()
	}
	
	fun initRelics() {
		excaliber		= AlfheimRelicLexiconEntry("excaliber",		categoryAlfheim,	AlfheimItems.excaliber)
		excaliber.setLexiconPages(PageText("0")).icon = ItemStack(AlfheimItems.excaliber)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.excaliber), excaliber, 1)
		
		gleipnir		= AlfheimRelicLexiconEntry("gleipnir",		categoryDivinity,	AlfheimItems.gleipnir)
		gleipnir.setLexiconPages(PageText("0")).icon = ItemStack(AlfheimItems.gleipnir)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.gleipnir), gleipnir, 1)
		
		gungnir			= AlfheimRelicLexiconEntry("gungnir",		categoryDivinity,	AlfheimItems.gungnir)
		gungnir.setLexiconPages(PageText("0")).icon = ItemStack(AlfheimItems.gungnir)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.gungnir), gungnir, 1)
		
		mask			= AlfheimRelicLexiconEntry("mask",			categoryAlfheim,	AlfheimItems.mask)
		mask.setLexiconPages(PageText("0")).icon = ItemStack(AlfheimItems.mask)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.mask), mask, 1)
		
		mjolnir			= AlfheimRelicLexiconEntry("mjolnir",		categoryDivinity,	AlfheimItems.mjolnir)
		mjolnir.setLexiconPages(PageText("0")).icon = ItemStack(AlfheimItems.mjolnir)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.mjolnir), mjolnir, 1)
		
		moonbow			= AlfheimRelicLexiconEntry("moonbow",		categoryAlfheim,	AlfheimItems.moonlightBow)
		moonbow.setLexiconPages(PageText("0")).icon = ItemStack(AlfheimItems.moonlightBow)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.moonlightBow), moonbow, 1)
		
		ringHeimdall	= AlfheimRelicLexiconEntry("ring_heimdall",	categoryDivinity,	AlfheimItems.priestRingHeimdall)
		ringHeimdall.setLexiconPages(PageText("0")).icon = ItemStack(AlfheimItems.priestRingHeimdall)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.priestRingHeimdall), ringHeimdall, 1)
		
		ringNjord		= AlfheimRelicLexiconEntry("ring_njord",	categoryDivinity,	AlfheimItems.priestRingNjord)
		ringNjord.setLexiconPages(PageText("0")).icon = ItemStack(AlfheimItems.priestRingNjord)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.priestRingNjord), ringNjord, 1)
		
		ringSif			= AlfheimRelicLexiconEntry("ring_sif",		categoryDivinity,	AlfheimItems.priestRingSif)
		ringSif.setLexiconPages(PageText("0")).icon = ItemStack(AlfheimItems.priestRingSif)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.priestRingSif), ringSif, 1)
		
		soul			= AlfheimRelicLexiconEntry("soul",			categoryAlfheim,	AlfheimItems.flugelSoul)
		soul.setLexiconPages(PageText("0"), PageText("1"), PageText("2"),
							 PageText("3"), PageText("4"), PageText("5"),
							 PageText("6"), PageText("7"), PageText("8"),
							 PageMultiblock("9", AlfheimMultiblocks.soul),
							 PageText("10"), PageCraftingRecipe("11", AlfheimRecipes.recipeCleanPylon)).icon = ItemStack(AlfheimItems.flugelSoul)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.flugelSoul), soul, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.alfheimPylon, 1, 2), soul, 7)
		
		soulHorn		= AlfheimRelicLexiconEntry("soulHorn",		categoryAlfheim,	AlfheimItems.soulHorn)
		soulHorn.setLexiconPages(PageText("0"), PageText("1"), PageCraftingRecipe("2", AlfheimRecipes.recipeSoulHorn), PageText("3")).icon = ItemStack(AlfheimItems.soulHorn)
		
		subspear		= AlfheimRelicLexiconEntry("subspear",		categoryAlfheim,	AlfheimItems.subspaceSpear)
		subspear.setLexiconPages(PageText("0")).icon = ItemStack(AlfheimItems.subspaceSpear)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.subspaceSpear), subspear, 1)
	}
	
	private fun initElvenStory() {
		if (esm!!.pages.isEmpty())
			esm!!.setPriority()
				.setLexiconPages(PageText("0"))
		
		if (races!!.pages.isEmpty())
			races!!.setPriority()
				.setLexiconPages(PageText("0"), PageText("1"), PageText("2"), PageText("3"), PageText("4"), PageText("5"))
		
		if (AlfheimConfigHandler.enableMMO) initMMO()
	}
	
	private fun initMMO() {
		if (parties!!.pages.isEmpty())
			parties!!.setPriority()
				.setLexiconPages(PageText("0"), PageText("1"), PageCraftingRecipe("2", AlfheimRecipes.recipePeacePipe),
								 PageText("3"), PageCraftingRecipe("4", AlfheimRecipes.recipePaperBreak)).icon = null
		
		if (spells!!.pages.isEmpty()) {
			spells!!.setPriority()
				.setLexiconPages(PageText("0"), PageText("1"), PageText("2"), PageText("3"))
			
			postInitMMO()
		}
		
		if (targets!!.pages.isEmpty())
			targets!!.setPriority()
				.setLexiconPages(PageText("0"), PageText("1"))
	}
	
	/*fun postInit() {
		if (AlfheimConfigHandler.enableMMO) postInitMMO()
	}*/
	
	private fun postInitMMO() {
		val l = ArrayList(AlfheimAPI.spells)
		l.sortWith(Comparator { s1, s2 -> s1.name.compareTo(s2.name) })
		for (spell in l) spells!!.addPage(PageSpell(spell))
	}
	
	private fun setKnowledgeTypes() {
		advancedMana.knowledgeType = BotaniaAPI.elvenKnowledge
		amplifier.knowledgeType = BotaniaAPI.elvenKnowledge
		amuletIceberg.knowledgeType = BotaniaAPI.elvenKnowledge
		amuletNimbus.knowledgeType = BotaniaAPI.elvenKnowledge
		amulterCrescent.knowledgeType = BotaniaAPI.elvenKnowledge
		anomaly.knowledgeType = BotaniaAPI.elvenKnowledge
		anyavil.knowledgeType = BotaniaAPI.elvenKnowledge
		astrolabe.knowledgeType = BotaniaAPI.elvenKnowledge
		beltRation.knowledgeType = BotaniaAPI.elvenKnowledge
		corpInj.knowledgeType = BotaniaAPI.elvenKnowledge
		corpSeq.knowledgeType = BotaniaAPI.elvenKnowledge
		colorOverride.knowledgeType = BotaniaAPI.elvenKnowledge
		elementalSet.knowledgeType = BotaniaAPI.elvenKnowledge
		elvenSet.knowledgeType = BotaniaAPI.elvenKnowledge
		elves.knowledgeType = BotaniaAPI.elvenKnowledge
		elvorium.knowledgeType = BotaniaAPI.elvenKnowledge
		essences.knowledgeType = BotaniaAPI.elvenKnowledge
		flowerEnderchid.knowledgeType = BotaniaAPI.elvenKnowledge
		flowerPetronia.knowledgeType = BotaniaAPI.elvenKnowledge
		flugel.knowledgeType = BotaniaAPI.elvenKnowledge
		hyperBucket.knowledgeType = BotaniaAPI.elvenKnowledge
		infuser.knowledgeType = BotaniaAPI.elvenKnowledge
		lamp.knowledgeType = BotaniaAPI.elvenKnowledge
		lembas.knowledgeType = BotaniaAPI.elvenKnowledge
		lootInt.knowledgeType = BotaniaAPI.elvenKnowledge
		manaImba.knowledgeType = BotaniaAPI.elvenKnowledge
		manaLamp.knowledgeType = BotaniaAPI.elvenKnowledge
		mobs.knowledgeType = BotaniaAPI.elvenKnowledge
		multbauble.knowledgeType = BotaniaAPI.elvenKnowledge
		ores.knowledgeType = BotaniaAPI.elvenKnowledge
		pixie.knowledgeType = BotaniaAPI.elvenKnowledge
		portal.knowledgeType = BotaniaAPI.elvenKnowledge
		pylons.knowledgeType = BotaniaAPI.elvenKnowledge
		rainbowFlora.knowledgeType = BotaniaAPI.elvenKnowledge
		reality.knowledgeType = BotaniaAPI.elvenKnowledge
		ringAnomaly.knowledgeType = BotaniaAPI.elvenKnowledge
		ringsAura.knowledgeType = BotaniaAPI.elvenKnowledge
		rodClick.knowledgeType = BotaniaAPI.elvenKnowledge
		rodPrismatic.knowledgeType = BotaniaAPI.elvenKnowledge
		ruling.knowledgeType = BotaniaAPI.elvenKnowledge
		runes.knowledgeType = BotaniaAPI.elvenKnowledge
		shimmer.knowledgeType = BotaniaAPI.elvenKnowledge
		shrines.knowledgeType = BotaniaAPI.elvenKnowledge
		silencer.knowledgeType = BotaniaAPI.elvenKnowledge
		trade.knowledgeType = BotaniaAPI.elvenKnowledge
		uberSpreader.knowledgeType = BotaniaAPI.elvenKnowledge
		winery.knowledgeType = BotaniaAPI.elvenKnowledge
		worldgen.knowledgeType = BotaniaAPI.elvenKnowledge
		
		if (ThaumcraftSuffusionRecipes.recipesLoaded) {
			tctrees.knowledgeType = BotaniaAPI.elvenKnowledge
		}
	}
	
	fun disableESM() {
		setKnowledgeTypes()
		
		removeEntry(esm, categoryAlfheim)
		removeEntry(races, categoryAlfheim)
	}
	
	fun reEnableESM() {
		if (AlfheimConfigHandler.enableElvenStory) {
			preInitElvenStory()
			initElvenStory()
		}
		if (AlfheimConfigHandler.enableMMO) {
			preInitMMO()
			initMMO()
		}
		
		if (!categoryAlfheim.entries.contains(esm)) BotaniaAPI.addEntry(esm, categoryAlfheim)
		if (!categoryAlfheim.entries.contains(races)) BotaniaAPI.addEntry(races, categoryAlfheim)
		
		setKnowledgeTypes()
	}
	
	fun disableMMO() {
		setKnowledgeTypes()
		
		removeEntry(parties, categoryAlfheim)
		removeEntry(spells, categoryAlfheim)
		removeEntry(targets, categoryAlfheim)
	}
	
	fun reEnableMMO() {
		if (AlfheimConfigHandler.enableElvenStory) {
			preInitElvenStory()
			initElvenStory()
		}
		if (AlfheimConfigHandler.enableMMO) {
			preInitMMO()
			initMMO()
		}
		
		if (!categoryAlfheim.entries.contains(parties)) BotaniaAPI.addEntry(parties, categoryAlfheim)
		if (!categoryAlfheim.entries.contains(spells)) BotaniaAPI.addEntry(spells, categoryAlfheim)
		if (!categoryAlfheim.entries.contains(targets)) BotaniaAPI.addEntry(targets, categoryAlfheim)
		
		setKnowledgeTypes()
	}
	
	private fun removeEntry(entry: LexiconEntry?, category: LexiconCategory) {
		BotaniaAPI.getAllEntries().remove(entry)
		category.entries.remove(entry)
	}
}

object AlfheimMultiblocks {
	val infuser = TileManaInfuser.makeMultiblockSet()
	val infuserU = TileManaInfuser.makeMultiblockSetUnknown()
	val portal = TileAlfheimPortal.makeMultiblockSet()
	val soul = TileManaInfuser.makeMultiblockSetSoul()
	var treeCrafter = TileTreeCrafter.makeMultiblockSet()
	val yordin = TileTradePortal.makeMultiblockSet()
}