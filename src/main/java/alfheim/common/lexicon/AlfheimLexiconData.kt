package alfheim.common.lexicon

import alexsocol.asjlib.extendables.block.BlockModMeta
import alfheim.AlfheimCore
import alfheim.api.*
import alfheim.common.achievement.AlfheimAchievements
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.tile.*
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.handler.CardinalSystem.KnowledgeSystem.Knowledge
import alfheim.common.crafting.recipe.*
import alfheim.common.integration.tinkersconstruct.TinkersConstructAlfheimConfig
import alfheim.common.item.AlfheimItems
import alfheim.common.item.material.*
import alfheim.common.lexicon.page.*
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.lexicon.*
import vazkii.botania.api.recipe.RecipeRuneAltar
import vazkii.botania.common.block.*
import vazkii.botania.common.item.ModItems
import vazkii.botania.common.lexicon.*
import vazkii.botania.common.lexicon.page.*
import java.util.*

object AlfheimLexiconData {
	
	val kt get() = (if (AlfheimConfigHandler.enableElvenStory) BotaniaAPI.basicKnowledge else BotaniaAPI.elvenKnowledge)!!
	
	lateinit var categoryAlfheim: LexiconCategory
	
	lateinit var categoryDivinity: LexiconCategory
	
	/** Lore alfheim page  */
	lateinit var alfheim: LexiconEntry
	
	/** Lore elves page  */
	lateinit var elves: LexiconEntry
	
	// Main addon content
	lateinit var advMana: LexiconEntry
	lateinit var amulCirs: LexiconEntry
	lateinit var amulNimb: LexiconEntry
	lateinit var aniTorch: LexiconEntry
	lateinit var anomaly: LexiconEntry
	lateinit var anomaRing: LexiconEntry
	lateinit var anyavil: LexiconEntry
	lateinit var astrolab: LexiconEntry
	lateinit var auraAlf: LexiconEntry
	lateinit var cloakInv: LexiconEntry
	lateinit var corpInj: LexiconEntry
	lateinit var corpSeq: LexiconEntry
	lateinit var crescent: LexiconEntry
	lateinit var dasGold: LexiconEntry
	lateinit var dodgRing: LexiconEntry
	lateinit var elemSet: LexiconEntry
	lateinit var elvenSet: LexiconEntry
	lateinit var elvorium: LexiconEntry
	lateinit var essences: LexiconEntry
	lateinit var excalibr: LexiconEntry
	lateinit var flowerEnderchid: LexiconEntry
	lateinit var flowerPetronia: LexiconEntry
	lateinit var flowerRain: LexiconEntry
	lateinit var flowerSnow: LexiconEntry
	lateinit var flowerStorm: LexiconEntry
	lateinit var flowerWind: LexiconEntry
	lateinit var flugel: LexiconEntry
	lateinit var endAct: LexiconEntry
	lateinit var greenRod: LexiconEntry
	lateinit var gungnir: LexiconEntry
	lateinit var gleipnir: LexiconEntry
	lateinit var hyperBuk: LexiconEntry
	lateinit var iceberg: LexiconEntry
	lateinit var infuser: LexiconEntry
	lateinit var lembas: LexiconEntry
	lateinit var lootInt: LexiconEntry
	lateinit var manaAcc: LexiconEntry
	lateinit var manaDrive: LexiconEntry
	lateinit var manaImba: LexiconEntry
	lateinit var manaLamp: LexiconEntry
	lateinit var mask: LexiconEntry
	lateinit var mitten: LexiconEntry
	lateinit var mjolnir: LexiconEntry
	lateinit var mobs: LexiconEntry
	lateinit var moonbow: LexiconEntry
	lateinit var multbaub: LexiconEntry
	lateinit var ores: LexiconEntry
	lateinit var pixie: LexiconEntry
	lateinit var portal: LexiconEntry
	lateinit var pylons: LexiconEntry
	lateinit var ration: LexiconEntry
	lateinit var reality: LexiconEntry
	lateinit var rodClick: LexiconEntry
	lateinit var ruling: LexiconEntry
	lateinit var runes: LexiconEntry
	lateinit var shrines: LexiconEntry
	lateinit var spider: LexiconEntry
	lateinit var soul: LexiconEntry
	lateinit var soulHorn: LexiconEntry
	
	//lateinit var stories: LexiconEntry
	lateinit var subspear: LexiconEntry
	lateinit var trade: LexiconEntry
	
	lateinit var triquetrum: LexiconEntry
	lateinit var uberSpreader: LexiconEntry
	lateinit var winery: LexiconEntry
	lateinit var worldgen: LexiconEntry
	
	// Elven Story information
	var es: LexiconEntry? = null
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
		categoryAlfheim = BLexiconCategory("Alfheim", 5)
		BotaniaAPI.addCategory(categoryAlfheim)
		
		categoryDivinity = BLexiconCategory("Divinity", 5)
		BotaniaAPI.addCategory(categoryDivinity)
		
		advMana = BLexiconEntry("advMana", categoryAlfheim)
		alfheim = BLexiconEntry("alfheim", categoryAlfheim)
		amulCirs = BLexiconEntry("amulCirs", categoryAlfheim)
		amulNimb = BLexiconEntry("amulNimb", categoryAlfheim)
		aniTorch = BLexiconEntry("aniTorch", categoryAlfheim)
		anomaly = BLexiconEntry("anomaly", categoryAlfheim)
		anomaRing = BLexiconEntry("anomaRing", categoryAlfheim)
		anyavil = BLexiconEntry("anyavil", categoryAlfheim)
		astrolab = BLexiconEntry("astrolab", categoryAlfheim)
		auraAlf = BLexiconEntry("auraAlf", categoryAlfheim)
		cloakInv = BLexiconEntry("cloakInv", categoryAlfheim)
		corpInj = BLexiconEntry("corpInj", categoryAlfheim)
		corpSeq = BLexiconEntry("corpSeq", categoryAlfheim)
		crescent = BLexiconEntry("crescent", categoryAlfheim)
		dasGold = BLexiconEntry("dasGold", categoryAlfheim)
		dodgRing = BLexiconEntry("dodgRing", categoryAlfheim)
		elemSet = BLexiconEntry("elemSet", categoryAlfheim)
		elvenSet = BLexiconEntry("elvenSet", categoryAlfheim)
		elves = BLexiconEntry("elves", categoryAlfheim)
		elvorium = BLexiconEntry("elvorium", categoryAlfheim)
		endAct = BLexiconEntry("endAct", categoryAlfheim)
		essences = BLexiconEntry("essences", categoryAlfheim)
		flowerEnderchid = BLexiconEntry("flowerEnderchid", categoryAlfheim)
		flowerPetronia = BLexiconEntry("flowerPetronia", categoryAlfheim)
		flowerRain = BLexiconEntry("flowerRain", categoryAlfheim)
		flowerSnow = BLexiconEntry("flowerSnow", categoryAlfheim)
		flowerStorm = BLexiconEntry("flowerStorm", categoryAlfheim)
		flowerWind = BLexiconEntry("flowerWind", categoryAlfheim)
		flugel = BLexiconEntry("flugel", categoryAlfheim)
		greenRod = BLexiconEntry("greenRod", categoryAlfheim)
		hyperBuk = BLexiconEntry("hyperBuk", categoryAlfheim)
		infuser = BLexiconEntry("infuser", categoryAlfheim)
		iceberg = BLexiconEntry("iceberg", categoryAlfheim)
		lembas = BLexiconEntry("lembas", categoryAlfheim)
		lootInt = BLexiconEntry("lootInt", categoryAlfheim)
		manaAcc = BLexiconEntry("itemHold", categoryAlfheim)
		manaDrive = BLexiconEntry("manaDrive", categoryAlfheim)
		manaImba = BLexiconEntry("manaImba", categoryAlfheim)
		manaLamp = BLexiconEntry("manaLamp", categoryAlfheim)
		mitten = BLexiconEntry("mitten", categoryAlfheim)
		mobs = BLexiconEntry("mobs", categoryAlfheim)
		multbaub = BLexiconEntry("multbaub", categoryAlfheim)
		ores = BLexiconEntry("ores", categoryAlfheim)
		pixie = BLexiconEntry("pixie", categoryAlfheim)
		portal = BLexiconEntry("portal", categoryAlfheim)
		pylons = BLexiconEntry("pylons", categoryAlfheim)
		ration = BLexiconEntry("ration", categoryAlfheim)
		reality = BLexiconEntry("reality", categoryAlfheim)
		rodClick = BLexiconEntry("rodClick", categoryAlfheim)
		ruling = BLexiconEntry("ruling", categoryAlfheim)
		runes = BLexiconEntry("runes", categoryAlfheim)
		shrines = BLexiconEntry("shrines", categoryAlfheim)
		spider = BLexiconEntry("spider", categoryAlfheim)
		//stories = BLexiconEntry("stories", categoryAlfheim)
		trade = BLexiconEntry("trade", categoryAlfheim)
		triquetrum = BLexiconEntry("triquetrum", categoryAlfheim)
		uberSpreader = BLexiconEntry("uberSpreader", categoryAlfheim)
		winery = BLexiconEntry("winery", categoryAlfheim)
		worldgen = BLexiconEntry("worldgen", categoryAlfheim)
		
		
		
		divIntro		= BLexiconEntry("divinity_intro",	categoryDivinity)
		
		cloakThor		= BLexiconEntry("garb_thor",		categoryDivinity)
		cloakSif		= BLexiconEntry("garb_sif",			categoryDivinity)
		cloakNjord		= BLexiconEntry("garb_njord",		categoryDivinity)
		cloakLoki		= BLexiconEntry("garb_loki",		categoryDivinity)
		cloakHeimdall	= BLexiconEntry("garb_heimdall",	categoryDivinity)
		cloakOdin		= BLexiconEntry("garb_odin",		categoryDivinity)
		
		emblemThor		= BLexiconEntry("thor",				categoryDivinity)
		emblemSif		= BLexiconEntry("sif",				categoryDivinity)
		emblemNjord		= BLexiconEntry("njord",			categoryDivinity)
		emblemLoki		= BLexiconEntry("loki",				categoryDivinity)
		emblemHeimdall	= BLexiconEntry("heimdall",			categoryDivinity)
		emblemOdin		= BLexiconEntry("odin",				categoryDivinity)
		
		rodThor			= BLexiconEntry("rod_thor",			categoryDivinity)
		rodSif			= BLexiconEntry("rod_sif",			categoryDivinity)
		rodNjord		= BLexiconEntry("rod_njord",		categoryDivinity)
		rodLoki			= BLexiconEntry("rod_loki",			categoryDivinity)
		rodOdin			= BLexiconEntry("rod_odin",			categoryDivinity)
		
		if (AlfheimConfigHandler.enableElvenStory) preInitElvenStory()
	}
	
	private fun preInitElvenStory() {
		if (es == null) es = BLexiconEntry("es", categoryAlfheim)
		if (races == null) races = BLexiconEntry("races", categoryAlfheim)
		
		if (AlfheimConfigHandler.enableMMO) preInitMMO()
	}
	
	private fun preInitMMO() {
		if (parties == null) parties = BLexiconEntry("parties", categoryAlfheim)
		if (spells == null) spells = BLexiconEntry("spells", categoryAlfheim)
		if (targets == null) targets = BLexiconEntry("targets", categoryAlfheim)
	}
	
	fun preInitRelics() {
		excalibr		= RLexiconEntry("excaliber",		categoryAlfheim,	AlfheimAchievements.excaliber)
		gleipnir		= RLexiconEntry("gleipnir",			categoryDivinity,	AlfheimAchievements.gleipnir)
		gungnir			= RLexiconEntry("gungnir",			categoryDivinity,	AlfheimAchievements.gungnir)
		mask			= RLexiconEntry("mask",				categoryAlfheim,	AlfheimAchievements.mask)
		mjolnir			= RLexiconEntry("mjolnir",			categoryDivinity,	AlfheimAchievements.mjolnir)
		moonbow			= RLexiconEntry("moonbow",			categoryAlfheim,	AlfheimAchievements.moonlightBow)
		soul			= RLexiconEntry("soul",				categoryAlfheim,	AlfheimAchievements.flugelSoul)
		subspear		= RLexiconEntry("subspear",			categoryAlfheim,	AlfheimAchievements.subspace)
		soulHorn		= RLexiconEntry("soulHorn",			categoryAlfheim,	AlfheimAchievements.flugelHardKill)
		
		ringSif			= RLexiconEntry("ring_sif",			categoryDivinity,	AlfheimAchievements.ringSif)
		ringNjord		= RLexiconEntry("ring_njord",		categoryDivinity,	AlfheimAchievements.ringNjord)
		ringHeimdall	= RLexiconEntry("ring_heimdall",	categoryDivinity,	AlfheimAchievements.ringHeimdall)
	}
	
	// In progress order
	fun init() {
		alfheim.setPriority()
			.setLexiconPages(PageText("0"), PageText("1"))
		
		anomaly.setPriority()
			.setLexiconPages(PageText("0"))
		
		var index = -1
		for (name in AlfheimAPI.anomalies.keys) {
			if (name == "Lightning") index = anomaly.pages.size + 1
			anomaly.setLexiconPages(PageImage("$name.t", ModInfo.MODID + ":textures/gui/entries/Anomaly" + name + ".png"), PageText("$name.d"))
		}
		
		var pg: LexiconPage = PageTextLearnableKnowledge("botania.page.anomalyLightning.d", Knowledge.PYLONS)
		anomaly.pages[index] = pg
		pg.onPageAdded(anomaly, index)
		
		elves.setPriority()
			.setLexiconPages(PageText("0"), PageText("1"), PageText("2"), PageText("3"), PageText("4"))
		
		pylons.setPriority()
			.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeElvenPylon), PageCraftingRecipe("2", AlfheimRecipes.recipeGaiaPylon)).icon = ItemStack(AlfheimBlocks.alfheimPylon, 1, 0)
		
		portal.setPriority()
			.setLexiconPages(PageText("0"), PageText("1"), PageText("2"),
							 PageCraftingRecipe("3", AlfheimRecipes.recipeAlfheimPortal),
							 PageText("4"), PageElvenRecipe("5", AlfheimRecipes.recipeInterdimensional),
							 PageMultiblock("6", AlfheimMultiblocks.portal),
							 PageText("7"), PageText(if (AlfheimConfigHandler.destroyPortal) "8" else "8s"))
		
		worldgen.setLexiconPages(PageTextLearnableKnowledge("0", Knowledge.GLOWSTONE),
								 PagePureDaisyRecipe("1", AlfheimRecipes.recipeDreamwood),
			//new PageCraftingRecipe("2", AlfheimRecipes.recipeGlowstone),
								 PageText("3"),
								 PageCraftingRecipe("4", AlfheimRecipes.recipeLivingcobble),
								 PageCraftingRecipe("5", AlfheimRecipes.recipeLivingrockPickaxe),
								 PageCraftingRecipe("6", AlfheimRecipes.recipeFurnace)).icon = ItemStack(AlfheimBlocks.altLeaves, 1, 7)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.elvenSand), worldgen, 3)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.altWood1, 1, 3), worldgen, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.altLeaves, 1, 7), worldgen, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.dreamSapling), worldgen, 1)
		
		shrines.setLexiconPages(PageText("0"), PageText("1")).icon = ItemStack(AlfheimBlocks.powerStone)
		
		//stories.setLexiconPages(PageText("0"), PageText("1"), PageText("2"), PageText("3")).icon = ItemStack(AlfheimItems.storyToken)
		
		flowerEnderchid.setLexiconPages(PageText("0"), PagePetalRecipe("1", AlfheimRecipes.recipeOrechidEndium)).icon = BotaniaAPI.internalHandler.getSubTileAsStack("orechidEndium")
		
		flowerPetronia.setLexiconPages(PageText("0"), PagePetalRecipe("1", AlfheimRecipes.recipePetronia)).icon = BotaniaAPI.internalHandler.getSubTileAsStack("petronia")
		
		flowerRain.setLexiconPages(PageText("0"), PagePetalRecipe("1", AlfheimRecipes.recipeRainFlower)).icon = BotaniaAPI.internalHandler.getSubTileAsStack("rainFlower")
		
		flowerSnow.setLexiconPages(PageText("0"), PagePetalRecipe("1", AlfheimRecipes.recipeSnowFlower)).icon = BotaniaAPI.internalHandler.getSubTileAsStack("snowFlower")
		
		flowerStorm.setLexiconPages(PageText("0"), PagePetalRecipe("1", AlfheimRecipes.recipeStormFlower)).icon = BotaniaAPI.internalHandler.getSubTileAsStack("stormFlower")
		
		flowerWind.setLexiconPages(PageText("0"), PagePetalRecipe("1", AlfheimRecipes.recipeWindFlower)).icon = BotaniaAPI.internalHandler.getSubTileAsStack("windFlower")
		
		aniTorch.setLexiconPages(PageText("0"), PageText("1"), PageText("2"),
								 PageCraftingRecipe("3", AlfheimRecipes.recipeAnimatedTorch))
		
		manaAcc.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeItemHolder))
		
		greenRod.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeGreenRod))
		
		rodClick.setLexiconPages(PageText("0"), PageText("1"), PageText("2"), PageCraftingRecipe("3", AlfheimRecipes.recipeRodClicker))
		
		dodgRing.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeDodgeRing))
		
		spider.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeRingSpider))
		
		manaDrive.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeRingFeedFlower))
		
		iceberg.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipePendantSuperIce))
		
		ration.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeRationBelt))
		
		cloakInv.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeInvisibilityCloak))
		
		amulCirs.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeCloudPendant))
		
		amulNimb.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeCloudPendantSuper))
		
		mitten.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeManaweaveGlove))
		
		astrolab.setLexiconPages(PageText("0"), PageText("1"), PageCraftingRecipe("2", AlfheimRecipes.recipeAstrolabe))
		
		lootInt.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeLootInterceptor))
		
		manaLamp.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeEnlighter))
		
		lembas.setLexiconPages(PageText("0"), PageText("1"), PageCraftingRecipe("2", AlfheimRecipes.recipeLembas))
		
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
		
		mobs.setLexiconPages(PageText("0"), PageText("1")).icon = ItemStack(ModItems.manaResource, 1, 8)
		
		pixie.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipePixieAttractor)).icon = ItemStack(AlfheimItems.pixieAttractor)
		
		anyavil.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeAnyavil))
		
		triquetrum.setLexiconPages(PageText("0"), PageText("1"), PageCraftingRecipe("2", AlfheimRecipes.recipeTriquetrum))
		
		corpSeq.setLexiconPages(PageText("0"), PageText("1"), PageText("2"), PageCraftingRecipe("3", AlfheimRecipes.recipeAutocrafter))
		
		corpInj.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeInjector))
		
		endAct.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeEnderActuator))
		
		infuser.setLexiconPages(PageText("0"), PageText("1"),
								PageCraftingRecipe("2", AlfheimRecipes.recipeManaInfusionCore),
								PageCraftingRecipe("3", AlfheimRecipes.recipeManaInfuser),
								PageText("4"),
								PageMultiblockLearnable("5", AlfheimMultiblocks.infuserU, AlfheimMultiblocks.infuser, AlfheimAchievements.infuser)).icon = ItemStack(AlfheimBlocks.manaInfuser)
		
		elvorium.setLexiconPages(PageText("0"),
								 PageManaInfusorRecipe("1", AlfheimRecipes.recipeElvorium)).icon = ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.alfStorage, 1, 0), elvorium, 0)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.ElvoriumNugget), elvorium, 0)
		
		trade.setLexiconPages(PageText("0"), PageText("1"),
							  PageCraftingRecipe("2", AlfheimRecipes.recipeElvoriumPylon),
							  PageCraftingRecipe("3", AlfheimRecipes.recipeTradePortal),
							  PageMultiblock("4", AlfheimMultiblocks.yordin)).icon = ItemStack(AlfheimBlocks.tradePortal)
		
		val powerRecipes = ArrayList<IRecipe>()
		powerRecipes.add(AlfheimRecipes.recipeMuspelheimPowerIngot)
		powerRecipes.add(AlfheimRecipes.recipeNiflheimPowerIngot)
		essences.setLexiconPages(PageText("0"),
								 PageTextLearnableAchievement("2", AlfheimAchievements.flugelHardKill),
								 PageText("4"), PageText("5"), PageCraftingRecipe("6", powerRecipes),
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
		
		val runeRecipes = ArrayList<RecipeRuneAltar>()
		runeRecipes.add(AlfheimRecipes.recipeMuspelheimRune)
		runeRecipes.add(AlfheimRecipes.recipeNiflheimRune)
		runes.setLexiconPages(PageText("0"), PageRuneRecipe("1", runeRecipes),
							  PageText("2"), PageText("3"), PageRuneRecipe("4", AlfheimRecipes.recipeRealityRune)).icon = ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.PrimalRune)
		runes.addExtraDisplayedRecipe(ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.NiflheimRune))
		
		dasGold.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeRelicCleaner))
		
		multbaub.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeMultibauble))
		
		anomaRing.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeSpatiotemporal))
		
		elvenSet.setLexiconPages(PageText("0"),
								 PageCraftingRecipe("1", AlfheimRecipes.recipeElvoriumHelmet),
								 PageCraftingRecipe("2", AlfheimRecipes.recipeElvoriumChestplate),
								 PageCraftingRecipe("3", AlfheimRecipes.recipeElvoriumLeggings),
								 PageCraftingRecipe("4", AlfheimRecipes.recipeElvoriumBoots)).icon = ItemStack(AlfheimItems.elvoriumHelmet)
		AlfheimItems.elvoriumHelmetRevealing?.let { elvenSet.addExtraDisplayedRecipe(ItemStack(it)) }
		
		elemSet.setLexiconPages(PageText("0"),
								PageCraftingRecipe("1", AlfheimRecipes.recipeElementalHelmet),
								PageCraftingRecipe("2", AlfheimRecipes.recipeElementalChestplate),
								PageCraftingRecipe("3", AlfheimRecipes.recipeElementalLeggings),
								PageCraftingRecipe("4", AlfheimRecipes.recipeElementalBoots)).icon = ItemStack(AlfheimItems.elementalHelmet)
		AlfheimItems.elementalHelmetRevealing?.let { elemSet.addExtraDisplayedRecipe(ItemStack(it)) }
		
		advMana.setLexiconPages(PageText("0"), PageText("1"),
								PageManaInfusorRecipe("2", AlfheimRecipes.recipeManaStone),
								PageManaInfusorRecipe("3", AlfheimRecipes.recipeManaStoneGreater),
								PageText("4"),
								PageCraftingRecipe("5", AlfheimRecipes.recipeManaRingElven),
								PageCraftingRecipe("6", AlfheimRecipes.recipeManaRingGod)).icon = ItemStack(AlfheimItems.manaStone)
		
		auraAlf.setLexiconPages(PageText("0"),
								PageCraftingRecipe("1", AlfheimRecipes.recipeAuraRingElven),
								PageCraftingRecipe("2", AlfheimRecipes.recipeAuraRingGod)).icon = ItemStack(AlfheimItems.auraRingElven)
		
		val amuletRecipes = ArrayList<IRecipe>()
		amuletRecipes.add(AlfheimRecipes.recipeMuspelheimPendant)
		amuletRecipes.add(AlfheimRecipes.recipeNiflheimPendant)
		ruling.setLexiconPages(PageText("0"), PageText("1"),
							   PageCraftingRecipe("2", AlfheimRecipes.recipeMuspelheimRod),
							   PageCraftingRecipe("3", AlfheimRecipes.recipeNiflheimRod),
							   PageText("4"), PageCraftingRecipe("5", amuletRecipes)).icon = ItemStack(AlfheimItems.rodFire)
		
		crescent.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeCrescentAmulet))
		
		reality.setLexiconPages(PageText("0"), PageText("1"),
								PageCraftingRecipe("2", AlfheimRecipes.recipeSword))
		
		manaImba.setLexiconPages(PageText("0"), PageText("1"), PageText("2"),
								 PageCraftingRecipe("3", AlfheimRecipes.recipeManaMirrorImba))
		
		hyperBuk.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeHyperBucket))
		
		uberSpreader.setLexiconPages(PageText("0"), PageText("1"),
									 if (AlfheimCore.TiCLoaded && !AlfheimCore.stupidMode && AlfheimConfigHandler.materialIDs[TinkersConstructAlfheimConfig.MAUFTRIUM] != -1) PageText("2t")
									 else PageCraftingRecipe(if (AlfheimCore.stupidMode) "2s" else "2", AlfheimRecipes.recipeUberSpreader)).icon = ItemStack(ModBlocks.spreader, 1, 4)
		LexiconRecipeMappings.map(ItemStack(ModBlocks.spreader, 1, 4), uberSpreader, 2)
		
		flugel.setLexiconPages(PageText("0"), PageText("1"), PageText("2")).icon = ItemStack(ModItems.flightTiara, 1, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.flugelDisc), flugel, 0)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.flugelHead), flugel, 0)
		
		soulHorn.setLexiconPages(PageText("0"), PageText("1"), PageCraftingRecipe("2", AlfheimRecipes.recipeSoulHorn), PageText("3")).icon = ItemStack(AlfheimItems.soulHorn)
		
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
		
		if (AlfheimConfigHandler.enableElvenStory) initElvenStory()
		
		// ################################################################
		
		divIntro		.setLexiconPages(PageText("0"), PageText("1"), PageCraftingRecipe("2", ShadowFoxRecipes.recipesAttribution), PageText("3")).setPriority()
		
		cloakThor		.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeCloakThor)		)
		cloakSif		.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeCloakSif)		)
		cloakNjord		.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeCloakNjord)	)
		cloakLoki		.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeCloakLoki)		)
		cloakHeimdall	.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeCloakHeimdall)	)
		cloakOdin		.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeCloakOdin)		)
		
		emblemThor		.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ShadowFoxRecipes.recipesPriestOfThor)		)
		emblemSif		.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ShadowFoxRecipes.recipesPriestOfSif)		)
		emblemNjord		.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ShadowFoxRecipes.recipesPriestOfNjord)		)
		emblemLoki		.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ShadowFoxRecipes.recipesPriestOfLoki)		)
		emblemHeimdall	.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ShadowFoxRecipes.recipesPriestOfHeimdall)	)
		emblemOdin		.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ShadowFoxRecipes.recipesPriestOfOdin)		)
		
		rodThor			.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ShadowFoxRecipes.recipesLightningRod)		, PageText("2"))
		rodSif			.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ShadowFoxRecipes.recipesColoredSkyDirtRod)	, PageText("2")).icon = ItemStack(AlfheimItems.rodColorfulSkyDirt, 1, 16)
		rodNjord		.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ShadowFoxRecipes.recipesInterdictionRod)	, PageText("2"))
		rodLoki			.setLexiconPages(PageText("0"), PageCraftingRecipe("1", ShadowFoxRecipes.recipesFlameRod)			, PageText("2"))
		rodOdin			.setLexiconPages(PageText("0"), PageText("1"), PageCraftingRecipe("2", AlfheimRecipes.recipeRodPortal), PageText("3"))
		
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.rodColorfulSkyDirt), rodSif, 1)
		
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
		
		setKnowledgeTypes()
	}
	
	fun initRelics() {
		soul.setLexiconPages(PageText("0"), PageText("1"), PageText("2"),
							 PageText("3"), PageText("4"), PageText("5"),
							 PageText("6"), PageText("7"), PageText("8"),
							 PageMultiblock("9", AlfheimMultiblocks.soul),
							 PageText("10"), PageCraftingRecipe("11", AlfheimRecipes.recipeCleanPylon)).icon = ItemStack(AlfheimItems.flugelSoul)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.flugelSoul), soul, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.alfheimPylon, 1, 2), soul, 7)
		
		mask.setLexiconPages(PageText("0")).icon = ItemStack(AlfheimItems.mask)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.mask), mask, 1)
		
		excalibr.setLexiconPages(PageText("0")).icon = ItemStack(AlfheimItems.excaliber)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.excaliber), excalibr, 1)
		
		gleipnir.setLexiconPages(PageText("0")).icon = ItemStack(AlfheimItems.gleipnir)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.gleipnir), gleipnir, 1)
		
		gungnir.setLexiconPages(PageText("0")).icon = ItemStack(AlfheimItems.gungnir)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.gungnir), gungnir, 1)
		
		mjolnir.setLexiconPages(PageText("0")).icon = ItemStack(AlfheimItems.mjolnir)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.mjolnir), mjolnir, 1)
		
		moonbow.setLexiconPages(PageText("0")).icon = ItemStack(AlfheimItems.moonlightBow)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.moonlightBow), moonbow, 1)
		
		subspear.setLexiconPages(PageText("0")).icon = ItemStack(AlfheimItems.subspaceSpear)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.subspaceSpear), subspear, 1)
		
		ringSif.setLexiconPages(PageText("0")).icon = ItemStack(AlfheimItems.priestRingSif)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.priestRingSif), ringSif, 1)
		
		ringNjord.setLexiconPages(PageText("0")).icon = ItemStack(AlfheimItems.priestRingNjord)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.priestRingNjord), ringNjord, 1)
		
		ringHeimdall.setLexiconPages(PageText("0")).icon = ItemStack(AlfheimItems.priestRingHeimdall)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.priestRingHeimdall), ringHeimdall, 1)
		
		soul.knowledgeType = BotaniaAPI.relicKnowledge
		mask.knowledgeType = BotaniaAPI.relicKnowledge
		excalibr.knowledgeType = BotaniaAPI.relicKnowledge
		gleipnir.knowledgeType = BotaniaAPI.relicKnowledge
		gungnir.knowledgeType = BotaniaAPI.relicKnowledge
		mjolnir.knowledgeType = BotaniaAPI.relicKnowledge
		moonbow.knowledgeType = BotaniaAPI.relicKnowledge
		subspear.knowledgeType = BotaniaAPI.relicKnowledge
		
		ringSif.knowledgeType = BotaniaAPI.relicKnowledge
		ringNjord.knowledgeType = BotaniaAPI.relicKnowledge
		ringHeimdall.knowledgeType = BotaniaAPI.relicKnowledge
	}
	
	private fun initElvenStory() {
		if (es!!.pages.isEmpty())
			es!!.setPriority()
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
		alfheim.knowledgeType = BotaniaAPI.basicKnowledge
		anomaly.knowledgeType = kt
		elves.knowledgeType = kt
		pylons.knowledgeType = BotaniaAPI.basicKnowledge
		portal.knowledgeType = kt
		worldgen.knowledgeType = kt
		shrines.knowledgeType = kt
		
		aniTorch.knowledgeType = BotaniaAPI.basicKnowledge
		manaAcc.knowledgeType = BotaniaAPI.basicKnowledge
		greenRod.knowledgeType = BotaniaAPI.basicKnowledge
		dodgRing.knowledgeType = BotaniaAPI.basicKnowledge
		cloakInv.knowledgeType = BotaniaAPI.basicKnowledge
		amulCirs.knowledgeType = BotaniaAPI.basicKnowledge
		amulNimb.knowledgeType = kt
		astrolab.knowledgeType = kt
		lootInt.knowledgeType = kt
		
		ores.knowledgeType = kt
		mobs.knowledgeType = kt
		pixie.knowledgeType = kt
		anyavil.knowledgeType = kt
		infuser.knowledgeType = kt
		elvorium.knowledgeType = kt
		trade.knowledgeType = kt
		essences.knowledgeType = kt
		runes.knowledgeType = kt
		multbaub.knowledgeType = kt
		anomaRing.knowledgeType = kt
		elvenSet.knowledgeType = kt
		elemSet.knowledgeType = kt
		advMana.knowledgeType = kt
		auraAlf.knowledgeType = kt
		ruling.knowledgeType = kt
		crescent.knowledgeType = kt
		reality.knowledgeType = kt
		uberSpreader.knowledgeType = kt
		flugel.knowledgeType = kt
		
		if (AlfheimConfigHandler.enableElvenStory) {
			es!!.knowledgeType = BotaniaAPI.basicKnowledge
			races!!.knowledgeType = BotaniaAPI.basicKnowledge
			
			if (AlfheimConfigHandler.enableMMO) {
				parties!!.knowledgeType = BotaniaAPI.basicKnowledge
				spells!!.knowledgeType = BotaniaAPI.basicKnowledge
				targets!!.knowledgeType = BotaniaAPI.basicKnowledge
			}
		}
		
		LexiconData.elvenLenses.knowledgeType = kt
		LexiconData.dreamwoodSpreader.knowledgeType = kt
		LexiconData.prism.knowledgeType = kt
		LexiconData.sparkUpgrades.knowledgeType = kt
		LexiconData.sparkChanger.knowledgeType = kt
		LexiconData.dandelifeon.knowledgeType = kt
		LexiconData.kekimurus.knowledgeType = kt
		LexiconData.rafflowsia.knowledgeType = kt
		LexiconData.spectrolus.knowledgeType = kt
		LexiconData.bubbell.knowledgeType = kt
		LexiconData.heiseiDream.knowledgeType = kt
		LexiconData.loonium.knowledgeType = kt
		LexiconData.orechid.knowledgeType = kt
		LexiconData.orechidIgnem.knowledgeType = kt
		LexiconData.spectranthemum.knowledgeType = kt
		LexiconData.flasks.knowledgeType = kt
		LexiconData.cocoon.knowledgeType = kt
		LexiconData.conjurationCatalyst.knowledgeType = kt
		LexiconData.craftCrate.knowledgeType = kt
		LexiconData.gatherDrum.knowledgeType = kt
		LexiconData.manaBomb.knowledgeType = kt
		LexiconData.spectralPlatform.knowledgeType = kt
		LexiconData.ghostRail.knowledgeType = kt
		LexiconData.crystalBow.knowledgeType = kt
		LexiconData.elfGear.knowledgeType = kt
		LexiconData.virus.knowledgeType = kt
		LexiconData.openBucket.knowledgeType = kt
		LexiconData.fireChakram.knowledgeType = kt
		LexiconData.clip.knowledgeType = kt
		LexiconData.rainbowRod.knowledgeType = kt
		LexiconData.skyDirtRod.knowledgeType = kt
		LexiconData.gravityRod.knowledgeType = kt
		LexiconData.missileRod.knowledgeType = kt
		LexiconData.laputaShard.knowledgeType = kt
		LexiconData.slimeBottle.knowledgeType = kt
		LexiconData.regenIvy.knowledgeType = kt
		LexiconData.worldSeed.knowledgeType = kt
		LexiconData.divaCharm.knowledgeType = kt
		LexiconData.judgementCloaks.knowledgeType = kt
		LexiconData.superLavaPendant.knowledgeType = kt
		LexiconData.superTravelBelt.knowledgeType = kt
		LexiconData.goldLaurel.knowledgeType = kt
		LexiconData.pixieRing.knowledgeType = kt
		LexiconData.reachRing.knowledgeType = kt
		LexiconData.corporea.knowledgeType = kt
		LexiconData.blackHoleTalisman.knowledgeType = kt
		LexiconData.corporeaCrystalCube.knowledgeType = kt
		LexiconData.corporeaFunnel.knowledgeType = kt
		LexiconData.corporeaIndex.knowledgeType = kt
		LexiconData.corporeaInterceptor.knowledgeType = kt
		LexiconData.corporeaRetainer.knowledgeType = kt
		LexiconData.flightTiara.knowledgeType = kt
		LexiconData.spawnerClaw.knowledgeType = kt
		LexiconData.spawnerMover.knowledgeType = kt
		LexiconData.luminizerTransport.knowledgeType = kt
		LexiconData.redString.knowledgeType = kt
		LexiconData.keepIvy.knowledgeType = kt
		LexiconData.starSword.knowledgeType = kt
		LexiconData.thunderSword.knowledgeType = kt
		LexiconData.elvenMessage.knowledgeType = kt
		LexiconData.elvenResources.knowledgeType = kt
		LexiconData.elvenLore.knowledgeType = kt
		LexiconData.gaiaRitual.knowledgeType = kt
		LexiconData.gaiaRitualHardmode.knowledgeType = kt
		if (LexiconData.relics != null) LexiconData.relics.knowledgeType = kt
		LexiconData.headCreating.knowledgeType = kt
		LexiconData.starfield.knowledgeType = kt
		
		ShadowFoxLexiconData.setKnowledgeTypes(kt)
	}
	
	fun disableESM() {
		setKnowledgeTypes()
		
		removeEntry(es, categoryAlfheim)
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
		
		if (!categoryAlfheim.entries.contains(es)) BotaniaAPI.addEntry(es, categoryAlfheim)
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
	val yordin = TileTradePortal.makeMultiblockSet()
}