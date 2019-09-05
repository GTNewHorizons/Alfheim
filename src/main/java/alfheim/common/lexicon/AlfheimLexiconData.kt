package alfheim.common.lexicon

import alexsocol.asjlib.extendables.block.BlockModMeta
import alfheim.AlfheimCore
import alfheim.api.*
import alfheim.common.achievement.AlfheimAchievements
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.tile.*
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.handler.CardinalSystem.KnowledgeSystem.Knowledge
import alfheim.common.crafting.recipe.AlfheimRecipes
import alfheim.common.item.AlfheimItems
import alfheim.common.item.material.ElvenResourcesMetas
import alfheim.common.lexicon.page.*
import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.IRecipe
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.lexicon.*
import vazkii.botania.api.lexicon.multiblock.MultiblockSet
import vazkii.botania.api.recipe.RecipeRuneAltar
import vazkii.botania.common.block.ModMultiblocks
import vazkii.botania.common.item.ModItems
import vazkii.botania.common.lexicon.*
import vazkii.botania.common.lexicon.page.*
import java.util.*

object AlfheimLexiconData {
	
	var kt = (if (AlfheimCore.enableElvenStory) BotaniaAPI.basicKnowledge else BotaniaAPI.elvenKnowledge)!!
	
	lateinit var categoryAlfheim: LexiconCategory
	
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
	//lateinit var anomaRing: LexiconEntry // FIXME
	lateinit var anyavil: LexiconEntry
	lateinit var astrolab: LexiconEntry
	lateinit var auraAlf: LexiconEntry
	lateinit var cloakInv: LexiconEntry
	lateinit var crescent: LexiconEntry
	lateinit var dasGold: LexiconEntry
	lateinit var dodgRing: LexiconEntry
	lateinit var elemSet: LexiconEntry
	lateinit var elvenSet: LexiconEntry
	lateinit var elvorium: LexiconEntry
	lateinit var essences: LexiconEntry
	lateinit var excalibr: LexiconEntry
	lateinit var flugel: LexiconEntry
	lateinit var greenRod: LexiconEntry
	lateinit var infuser: LexiconEntry
	lateinit var manaAcc: LexiconEntry
	lateinit var lootInt: LexiconEntry
	lateinit var mask: LexiconEntry
	//public static LexiconEntry mjolnir;
	lateinit var mobs: LexiconEntry
	lateinit var moonbow: LexiconEntry
	lateinit var multbaub: LexiconEntry
	lateinit var ores: LexiconEntry
	lateinit var pixie: LexiconEntry
	lateinit var portal: LexiconEntry
	lateinit var pylons: LexiconEntry
	lateinit var reality: LexiconEntry
	lateinit var ruling: LexiconEntry
	lateinit var runes: LexiconEntry
	lateinit var shrines: LexiconEntry
	lateinit var soul: LexiconEntry
	lateinit var stories: LexiconEntry
	lateinit var subspear: LexiconEntry
	lateinit var trade: LexiconEntry
	//public static LexiconEntry trans;		// BACK
	lateinit var worldgen: LexiconEntry
	
	// Elven Story information
	var es: LexiconEntry? = null
	var races: LexiconEntry? = null
	
	// MMO info
	var parties: LexiconEntry? = null
	var spells: LexiconEntry? = null
	var targets: LexiconEntry? = null
	
	fun preInit() {
		categoryAlfheim = BLexiconCategory("Alfheim", 5)
		BotaniaAPI.addCategory(categoryAlfheim)
		
		advMana = BLexiconEntry("advMana", categoryAlfheim)
		alfheim = BLexiconEntry("alfheim", categoryAlfheim)
		amulCirs = BLexiconEntry("amulCirs", categoryAlfheim)
		amulNimb = BLexiconEntry("amulNimb", categoryAlfheim)
		aniTorch = BLexiconEntry("aniTorch", categoryAlfheim)
		anomaly = BLexiconEntry("anomaly", categoryAlfheim)
		// anomaRing = BLexiconEntry("anomaRing", categoryAlfheim) // FIXME
		anyavil = BLexiconEntry("anyavil", categoryAlfheim)
		astrolab = BLexiconEntry("astrolab", categoryAlfheim)
		auraAlf = BLexiconEntry("auraAlf", categoryAlfheim)
		cloakInv = BLexiconEntry("cloakInv", categoryAlfheim)
		crescent = BLexiconEntry("crescent", categoryAlfheim)
		dasGold = BLexiconEntry("dasGold", categoryAlfheim)
		dodgRing = BLexiconEntry("dodgRing", categoryAlfheim)
		elemSet = BLexiconEntry("elemSet", categoryAlfheim)
		elvenSet = BLexiconEntry("elvenSet", categoryAlfheim)
		elves = BLexiconEntry("elves", categoryAlfheim)
		elvorium = BLexiconEntry("elvorium", categoryAlfheim)
		essences = BLexiconEntry("essences", categoryAlfheim)
		flugel = BLexiconEntry("flugel", categoryAlfheim)
		greenRod = BLexiconEntry("greenRod", categoryAlfheim)
		infuser = BLexiconEntry("infuser", categoryAlfheim)
		manaAcc = BLexiconEntry("itemHold", categoryAlfheim)
		lootInt = BLexiconEntry("lootInt", categoryAlfheim)
		mobs = BLexiconEntry("mobs", categoryAlfheim)
		moonbow = BLexiconEntry("moonbow", categoryAlfheim)
		multbaub = BLexiconEntry("multbaub", categoryAlfheim)
		ores = BLexiconEntry("ores", categoryAlfheim)
		pixie = BLexiconEntry("pixie", categoryAlfheim)
		portal = BLexiconEntry("portal", categoryAlfheim)
		pylons = BLexiconEntry("pylons", categoryAlfheim)
		reality = BLexiconEntry("reality", categoryAlfheim)
		ruling = BLexiconEntry("ruling", categoryAlfheim)
		runes = BLexiconEntry("runes", categoryAlfheim)
		shrines = BLexiconEntry("shrines", categoryAlfheim)
		stories = BLexiconEntry("stories", categoryAlfheim)
		subspear = BLexiconEntry("subspear", categoryAlfheim)
		trade = BLexiconEntry("trade", categoryAlfheim)
		//trans		= new BLexiconEntry("trans",	categoryAlfheim); BACK
		worldgen = BLexiconEntry("worldgen", categoryAlfheim)
		
		if (AlfheimCore.enableElvenStory) preInitElvenStory()
	}
	
	private fun preInitElvenStory() {
		if (es == null) es = BLexiconEntry("es", categoryAlfheim)
		if (races == null) races = BLexiconEntry("races", categoryAlfheim)
		
		if (AlfheimCore.enableMMO) preInitMMO()
	}
	
	private fun preInitMMO() {
		if (parties == null) parties = BLexiconEntry("parties", categoryAlfheim)
		if (spells == null) spells = BLexiconEntry("spells", categoryAlfheim)
		if (targets == null) targets = BLexiconEntry("targets", categoryAlfheim)
	}
	
	fun preInit2() {
		excalibr = RLexiconEntry("excaliber", categoryAlfheim, AlfheimAchievements.excaliber)
		mask = RLexiconEntry("mask", categoryAlfheim, AlfheimAchievements.mask)
		//mjolnir	= new RLexiconEntry("mjolnir",	categoryAlfheim	, AlfheimAchievements.mjolnir);
		soul = RLexiconEntry("soul", categoryAlfheim, AlfheimAchievements.flugelSoul)
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
			.setLexiconPages(PageText("0"), PageText("1"), PageText("2"), PageText("3"),
							 PageCraftingRecipe("4", AlfheimRecipes.recipeAlfheimPortal),
							 PageElvenRecipe("5", AlfheimRecipes.recipeInterdimensional),
							 PageMultiblock("6", AlfheimMultiblocks.portal),
							 PageText("7"), PageText(if (AlfheimConfigHandler.destroyPortal) "8" else "8s"))
		
		worldgen.setLexiconPages(PageTextLearnableKnowledge("0", Knowledge.GLOWSTONE),
								 PagePureDaisyRecipe("1", AlfheimRecipes.recipeDreamwood),
			//new PageCraftingRecipe("2", AlfheimRecipes.recipeGlowstone),
								 PageText("3"),
								 PageCraftingRecipe("4", AlfheimRecipes.recipeLivingcobble),
								 PageCraftingRecipe("5", AlfheimRecipes.recipeLivingrockPickaxe),
								 PageCraftingRecipe("6", AlfheimRecipes.recipeFurnace)).icon = ItemStack(AlfheimBlocks.dreamLeaves)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.elvenSand), worldgen, 3)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.dreamLog), worldgen, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.dreamLeaves), worldgen, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.dreamSapling), worldgen, 1)
		
		shrines.setLexiconPages(PageText("0")).icon = ItemStack(AlfheimBlocks.powerStone)
		
		stories.setLexiconPages(PageText("0"), PageText("1")).icon = ItemStack(AlfheimItems.storyToken)
		
		aniTorch.setLexiconPages(PageText("0"), PageText("1"), PageText("2"),
								 PageCraftingRecipe("3", AlfheimRecipes.recipeAnimatedTorch))
		
		manaAcc.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeItemHolder))
		
		greenRod.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeGreenRod))
		
		dodgRing.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeDodgeRing))
		
		cloakInv.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeInvisibilityCloak))
		
		amulCirs.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeCloudPendant))
		
		amulNimb.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeCloudPendantSuper))
		
		astrolab.setLexiconPages(PageText("0"), PageText("1"), PageCraftingRecipe("2", AlfheimRecipes.recipeAstrolabe))
		
		lootInt.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeLootInterceptor))
		
		ores.setLexiconPages(PageText("0"), PageText("1"), PageText("2")).icon = ItemStack(AlfheimBlocks.elvenOres, 1, 4)
		for (i in 0 until (AlfheimBlocks.elvenOres as BlockModMeta).subtypes)
			ores.addExtraDisplayedRecipe(ItemStack(AlfheimBlocks.elvenOres, 1, i))
		
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.elvenOres, 1, 1), ores, 1)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.elvenOres, 1, 0), ores, 2)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.elvenOres, 1, 2), ores, 2)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.elvenOres, 1, 3), ores, 2)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.elvenOres, 1, 4), ores, 3)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.IffesalDust), ores, 3)
		
		mobs.setLexiconPages(PageText("0"), PageText("1")).icon = ItemStack(ModItems.manaResource, 1, 8)
		
		pixie.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipePixieAttractor)).icon = ItemStack(AlfheimItems.pixieAttractor)
		
		anyavil.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeAnyavil))
		
		infuser.setLexiconPages(PageText("0"), PageText("1"),
								PageCraftingRecipe("2", AlfheimRecipes.recipeManaInfusionCore),
								PageCraftingRecipe("3", AlfheimRecipes.recipeManaInfuser),
								PageText("4"),
								PageMultiblockLearnable("5", AlfheimMultiblocks.infuserU, AlfheimMultiblocks.infuser, AlfheimAchievements.infuser)).icon = ItemStack(AlfheimBlocks.manaInfuser)
		
		elvorium.setLexiconPages(PageText("0"),
								 PageManaInfusorRecipe("1", AlfheimRecipes.recipeElvorium)).icon = ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot)
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.alfStorage, 1, 0), elvorium, 0)
		
		trade.setLexiconPages(PageText("0"), PageText("1"),
							  PageCraftingRecipe("2", AlfheimRecipes.recipeElvoriumPylon),
							  PageCraftingRecipe("3", AlfheimRecipes.recipeTradePortal),
							  PageMultiblock("4", AlfheimMultiblocks.yordin)).icon = ItemStack(AlfheimBlocks.tradePortal)
		
		val powerRecipes = ArrayList<IRecipe>()
		powerRecipes.add(AlfheimRecipes.recipeMuspelheimPowerIngot)
		powerRecipes.add(AlfheimRecipes.recipeNiflheimPowerIngot)
		essences.setLexiconPages(PageText("0"),
								 PageTextLearnableAchievement("2", AlfheimAchievements.flugelKill),
								 PageText("4"), PageText("5"), PageCraftingRecipe("6", powerRecipes),
								 PageText("7"), PageManaInfusorRecipe("8", AlfheimRecipes.recipeMauftrium)).icon = ItemStack(ModItems.manaResource, 1, 5)
		essences.addExtraDisplayedRecipe(ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.NiflheimPowerIngot))
		essences.addExtraDisplayedRecipe(ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.NiflheimEssence))
		essences.addExtraDisplayedRecipe(ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.MuspelheimEssence))
		LexiconRecipeMappings.map(ItemStack(AlfheimBlocks.alfStorage, 1, 0), essences, 7)
		
		val runeRecipes = ArrayList<RecipeRuneAltar>()
		runeRecipes.add(AlfheimRecipes.recipeMuspelheimRune)
		runeRecipes.add(AlfheimRecipes.recipeNiflheimRune)
		runes.setLexiconPages(PageText("0"), PageRuneRecipe("1", runeRecipes),
							  PageText("2"), PageText("3"), PageRuneRecipe("4", AlfheimRecipes.recipeRealityRune)).icon = ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.PrimalRune)
		runes.addExtraDisplayedRecipe(ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.NiflheimRune))
		
		dasGold.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeRelicCleaner))
		
		multbaub.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeMultibauble))
		
		// anomaRing.setLexiconPages(PageText("0"), PageCraftingRecipe("1", AlfheimRecipes.recipeSpatiotemporal)) // FIXME
		
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
								PageCraftingRecipe("2", AlfheimRecipes.recipeSword)).icon = ItemStack(AlfheimItems.realitySword)
		
		flugel.setLexiconPages(PageText("0"), PageText("1"), PageText("2")).icon = ItemStack(ModItems.flightTiara, 1, 1)
		
		soul.setLexiconPages(PageText("0"), PageText("1"), PageText("3"),
							 PageMultiblock("4", AlfheimMultiblocks.soul),
							 PageText("5")).icon = ItemStack(AlfheimItems.flugelSoul)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.flugelSoul), soul, 1)
		
		mask.setLexiconPages(PageText("0")).icon = ItemStack(AlfheimItems.mask)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.mask), mask, 1)
		
		excalibr.setLexiconPages(PageTextLearnableAchievement("0", AlfheimAchievements.excaliber)).icon = ItemStack(AlfheimItems.excaliber)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.excaliber), excalibr, 1)
		
		moonbow.setLexiconPages(PageText("0")).icon = ItemStack(AlfheimItems.moonlightBow)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.moonlightBow), moonbow, 1)
		
		subspear.setLexiconPages(PageText("0")).icon = ItemStack(AlfheimItems.subspaceSpear)
		LexiconRecipeMappings.map(ItemStack(AlfheimItems.subspaceSpear), subspear, 1)
		
		/*mjolnir.setLexiconPages(new PageText("0"))
				.setIcon(new ItemStack(AlfheimItems.mjolnir));*/
		
		if (AlfheimCore.enableElvenStory) initElvenStory()
		
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
		LexiconData.lenses.pages.add(14, pg)
		pg.onPageAdded(LexiconData.lenses, 14)
		pg = PageCraftingRecipe("botania.page.lens39", AlfheimRecipes.recipeLensMessenger)
		LexiconData.lenses.pages.add(15, pg)
		pg.onPageAdded(LexiconData.lenses, 15)
		LexiconRecipeMappings.map(ItemStack(ModItems.lens, 1, 22), LexiconData.lenses, 14)
		
		pg = PageText("botania.page.elvenLenses11")
		LexiconData.elvenLenses.pages.add(pg)
		pg.onPageAdded(LexiconData.elvenLenses, 11)
		pg = PageCraftingRecipe("botania.page.elvenLenses12", AlfheimRecipes.recipeLensTripwire)
		LexiconData.elvenLenses.pages.add(pg)
		pg.onPageAdded(LexiconData.elvenLenses, 12)
		LexiconRecipeMappings.map(ItemStack(ModItems.lens, 1, 23), LexiconData.elvenLenses, 11)
		
		pg = PageText("botania.page.judgementCloaks1n")
		LexiconData.judgementCloaks.pages[1] = pg
		pg.onPageAdded(LexiconData.judgementCloaks, 1)
		pg = PageCraftingRecipe("botania.page.judgementCloaks4", AlfheimRecipes.recipeBalanceCloak)
		LexiconData.judgementCloaks.pages.add(pg)
		pg.onPageAdded(LexiconData.judgementCloaks, 4)
		
		setKnowledgeTypes()
	}
	
	private fun initElvenStory() {
		if (es!!.pages.isEmpty())
			es!!.setPriority()
				.setLexiconPages(PageText("0"))
		
		if (races!!.pages.isEmpty())
			races!!.setPriority()
				.setLexiconPages(PageText("0"), PageText("1"), PageText("2"))
		
		if (AlfheimCore.enableMMO) initMMO()
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
		if (AlfheimCore.enableMMO) postInitMMO()
	}*/
	
	private fun postInitMMO() {
		val l = ArrayList(AlfheimAPI.spells)
		l.sortWith(Comparator { s1, s2 -> s1.name.compareTo(s2.name) })
		for (spell in l) spells!!.addPage(PageSpell(spell))
	}
	
	private fun setKnowledgeTypes() {
		kt()
		
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
		// anomaRing.knowledgeType = kt // FIXME
		elvenSet.knowledgeType = kt
		elemSet.knowledgeType = kt
		advMana.knowledgeType = kt
		auraAlf.knowledgeType = kt
		ruling.knowledgeType = kt
		crescent.knowledgeType = kt
		reality.knowledgeType = kt
		flugel.knowledgeType = kt
		soul.knowledgeType = BotaniaAPI.relicKnowledge
		mask.knowledgeType = BotaniaAPI.relicKnowledge
		excalibr.knowledgeType = BotaniaAPI.relicKnowledge
		//mjolnir	.setKnowledgeType(BotaniaAPI.relicKnowledge);
		
		if (AlfheimCore.enableElvenStory) {
			es!!.knowledgeType = BotaniaAPI.basicKnowledge
			races!!.knowledgeType = BotaniaAPI.basicKnowledge
			
			if (AlfheimCore.enableMMO) {
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
		if (AlfheimCore.enableElvenStory) {
			preInitElvenStory()
			initElvenStory()
		}
		if (AlfheimCore.enableMMO) {
			preInitMMO()
			initMMO()
			postInitMMO()
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
		if (AlfheimCore.enableElvenStory) {
			preInitElvenStory()
			initElvenStory()
		}
		if (AlfheimCore.enableMMO) {
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
	
	private fun kt() {
		kt = if (AlfheimCore.enableElvenStory) BotaniaAPI.basicKnowledge else BotaniaAPI.elvenKnowledge
	}
}

object AlfheimMultiblocks {
	
	val infuser: MultiblockSet
	val infuserU: MultiblockSet
	val portal: MultiblockSet
	val soul: MultiblockSet
	val yordin: MultiblockSet
	
	init {
		infuser = TileManaInfuser.makeMultiblockSet()
		infuserU = TileManaInfuser.makeMultiblockSetUnknown()
		portal = TileAlfheimPortal.makeMultiblockSet()
		soul = TileManaInfuser.makeMultiblockSetSoul()
		yordin = TileTradePortal.makeMultiblockSet()
	}
}