package alfheim.common.item

import alexsocol.asjlib.toItem
import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.api.lib.LibOreDict
import alfheim.common.block.AlfheimBlocks
import alfheim.common.core.handler.WorkInProgressItemsHandler.WIP
import alfheim.common.core.util.AlfheimTab
import alfheim.common.item.block.*
import alfheim.common.item.creator.*
import alfheim.common.item.equipment.armor.elemental.*
import alfheim.common.item.equipment.armor.elvoruim.*
import alfheim.common.item.equipment.armor.fenrir.*
import alfheim.common.item.equipment.bauble.*
import alfheim.common.item.equipment.bauble.faith.ItemRagnarokEmblem
import alfheim.common.item.equipment.tool.*
import alfheim.common.item.equipment.tool.elementuim.ItemElementiumHoe
import alfheim.common.item.equipment.tool.manasteel.ItemManasteelHoe
import alfheim.common.item.interaction.thaumcraft.*
import alfheim.common.item.material.*
import alfheim.common.item.relic.*
import alfheim.common.item.rod.*
import net.minecraft.init.*
import net.minecraft.item.*
import net.minecraftforge.oredict.OreDictionary
import vazkii.botania.common.Botania
import vazkii.botania.common.block.ModBlocks
import vazkii.botania.common.item.ModItems
import vazkii.botania.common.item.record.ItemModRecord
import vazkii.botania.common.item.relic.ItemDice

object AlfheimItems {
	
	val `DEV-NULL`: Item?
	
	val akashicRecords: Item
	val aesirCloak: Item
	val aesirEmblem: Item
	val astrolabe: Item
	val attributionBauble: Item
	val auraRingElven: Item
	val auraRingGod: Item
	val balanceCloak: Item
	val cloudPendant: Item
	val cloudPendantSuper: Item
	val coatOfArms: Item
	val colorOverride: Item
	val creativeReachPendant: Item
	val crescentMoonAmulet: Item
	val daolos: Item
	val dodgeRing: Item
	val elementalBoots: Item
	val elementalChestplate: Item
	val elementalHelmet: Item
	val elementalHelmetRevealing: Item?
	val elementalLeggings: Item
	val elementiumHoe: Item
	val elfFirePendant: Item
	val elfIcePendant: Item
	val elvenFood: Item
	val elvenResource: Item
	val elvoriumBoots: Item
	val elvoriumChestplate: Item
	val elvoriumHelmet: Item
	val elvoriumHelmetRevealing: Item?
	val elvoriumLeggings: Item
	val enlighter: Item
	val excaliber: Item
	val fenrirBoots: Item
	val fenrirChestplate: Item
	val fenrirClaws: Item
	val fenrirHelmet: Item
	val fenrirHelmetRevealing: Item?
	val fenrirLeggings: Item
	val fireGrenade: Item
	val flugelDisc: Item
	val flugelDisc2: Item
	val flugelHead: Item
	val flugelHead2: Item
	val flugelSoul: Item
	val gaiaSlayer: Item
	val gjallarhorn: Item
	val gleipnir: Item
	val gungnir: Item
	val hyperBucket: Item
	val invisibilityCloak: Item
	val invisibleFlameLens: Item
	val irisSeeds: Item
	val livingrockPickaxe: Item
	val lootInterceptor: Item
	val manaGlove: Item
	val manaMirrorImba: Item
	val manaRingElven: Item
	val manaRingGod: Item
	val manasteelHoe: Item
	val manaStone: Item
	val manaStoneGreater: Item
	val mask: Item
	val mjolnir: Item
	val moonlightBow: Item
	val multibauble: Item
	val paperBreak: Item
	val peacePipe: Item
	val pendantSuperIce: Item
	val pixieAttractor: Item
	val priestCloak: Item
	val priestEmblem: Item
	val priestRingHeimdall: Item
	val priestRingNjord: Item
	val priestRingSif: Item
	val ragnarokEmblem: Item
	val rationBelt: Item
	val realitySword: Item
	val ringFeedFlower: Item
	val ringSpider: Item
	val rodBlackHole: Item
	val rodColorfulSkyDirt: Item
	val rodClicker: Item
	val rodFlameStar: Item
	val rodGrass: Item
	val rodInterdiction: Item
	val rodLightning: Item
	val rodMuspelheim: Item
	val rodNiflheim: Item
	val rodPortal: Item
	val rodPrismatic: Item
	val soulHorn: Item
	val soulSword: Item
	val spatiotemporalRing: Item
	val splashPotion: Item
	val starPlacer: Item
	val starPlacer2: Item
	
	//val storyToken: Item
	val subspaceSpear: Item
	val thinkingHand: Item
	
	//val toolbelt: Item
	val trisDagger: Item
	val triquetrum: Item
	val wiltedLotus: Item
	val wireAxe: Item
	
	val royalStaff: Item
	
	init {
		akashicRecords = ItemAkashicRecords().WIP()
		aesirCloak = ItemAesirCloak()
		aesirEmblem = ItemAesirEmblem()
		astrolabe = ItemAstrolabe()
		attributionBauble = ItemAttributionBauble()
		auraRingElven = ItemAuraRingAlfheim("AuraRingElven")
		auraRingGod = ItemAuraRingAlfheim("AuraRingGod", 2)
		balanceCloak = ItemBalanceCloak()
		cloudPendant = ItemCloudPendant()
		cloudPendantSuper = ItemCloudPendant("SuperCloudPendant", 3)
		coatOfArms = ItemCoatOfArms()
		colorOverride = ItemColorOverride()
		creativeReachPendant = ItemCreativeReachPendant()
		crescentMoonAmulet = ItemCrescentMoonAmulet()
		daolos = ItemDaolos()
		dodgeRing = ItemDodgeRing()
		fireGrenade = ItemFireGrenade()
		elementalHelmet = ItemElementalWaterHelm()
		elementalHelmetRevealing = if (Botania.thaumcraftLoaded) ItemElementalWaterHelmRevealing() else null
		elementalChestplate = ItemElementalEarthChest()
		elementalLeggings = ItemElementalFireLeggings()
		elementalBoots = ItemElementalAirBoots()
		elementiumHoe = ItemElementiumHoe()
		elfFirePendant = ItemPendant("FirePendant")
		elfIcePendant = ItemPendant("IcePendant")
		elvenFood = ItemElvenFood()
		elvenResource = ItemElvenResource()
		elvoriumHelmet = ItemElvoriumHelmet()
		elvoriumHelmetRevealing = if (Botania.thaumcraftLoaded) ItemElvoriumHelmetRevealing() else null
		elvoriumChestplate = ItemElvoriumArmor(1, "ElvoriumChestplate")
		elvoriumLeggings = ItemElvoriumArmor(2, "ElvoriumLeggings")
		elvoriumBoots = ItemElvoriumArmor(3, "ElvoriumBoots")
		enlighter = ItemEnlighter()
		excaliber = ItemExcaliber()
		fenrirHelmet = ItemFenrirArmor(0, "FenrirHelmet")
		fenrirHelmetRevealing = if (Botania.thaumcraftLoaded) ItemFenrirHelmetRevealing() else null
		fenrirChestplate = ItemFenrirArmor(1, "FenrirChestplate")
		fenrirLeggings = ItemFenrirArmor(2, "FenrirLeggings")
		fenrirBoots = ItemFenrirArmor(3, "FenrirBoots")
		fenrirClaws = ItemFenrirClaws()
		flugelDisc = ItemModRecord("flugel", "FlugelDisc").setCreativeTab(AlfheimTab)
		flugelDisc2 = ItemModRecord("miku", "MikuDisc").setCreativeTab(null)
		flugelHead = ItemHeadFlugel()
		flugelHead2 = ItemHeadMiku()
		flugelSoul = ItemFlugelSoul()
		gaiaSlayer = ItemGaiaSlayer()
		gjallarhorn = ItemGjallarhorn().WIP()
		gleipnir = ItemGleipnir()
		gungnir = ItemGungnir()
		hyperBucket = ItemHyperBucket()
		invisibilityCloak = ItemInvisibilityCloak()
		invisibleFlameLens = ItemLensFlashInvisible()
		irisSeeds = ItemColorSeeds()
		livingrockPickaxe = ItemLivingrockPickaxe()
		lootInterceptor = ItemLootInterceptor()
		manaGlove = ItemManaweaveGlove()
		manaMirrorImba = ItemManaMirrorImba()
		manasteelHoe = ItemManasteelHoe()
		manaRingElven = ItemManaStorageRing("ManaRingElven", 5.0)
		manaRingGod = ItemManaStorageRing("ManaRingGod", 10.0)
		manaStone = ItemManaStorage("ManaStone", 3.0)
		manaStoneGreater = ItemManaStorage("ManaStoneGreater", 8.0)
		mask = ItemTankMask()
		mjolnir = ItemMjolnir()
		moonlightBow = ItemMoonlightBow()
		multibauble = ItemMultibauble()
		paperBreak = ItemPaperBreak()
		peacePipe = ItemPeacePipe()
		pendantSuperIce = ItemSuperIcePendant()
		pixieAttractor = ItemPendant("PixieAttractor")
		priestCloak = ItemPriestCloak()
		priestEmblem = ItemPriestEmblem()
		priestRingHeimdall = ItemHeimdallRing()
		priestRingNjord = ItemNjordRing()
		priestRingSif = ItemSifRing()
		ragnarokEmblem = if (AlfheimCore.ENABLE_RAGNAROK) ItemRagnarokEmblem() else Blocks.stone.toItem()!!
		rationBelt = ItemRationBelt()
		realitySword = ItemRealitySword()
		ringFeedFlower = ItemFeedFlowerRing()
		ringSpider = ItemSpiderRing()
		rodBlackHole = ItemBlackHoleRod()
		rodColorfulSkyDirt = ItemRodIridescent()
		rodClicker = ItemRodClicker()
		rodMuspelheim = ItemRodElemental("MuspelheimRod") { AlfheimBlocks.redFlame }
		rodFlameStar = ItemRodFlameStar()
		rodNiflheim = ItemRodElemental("NiflheimRod") { AlfheimBlocks.poisonIce }
		rodInterdiction = ItemRodInterdiction()
		rodLightning = ItemRodLightning()
		rodPortal = ItemRodPortal()
		rodPrismatic = ItemRodPrismatic()
		rodGrass = ItemRodGrass()
		soulHorn = ItemSoulHorn()
		soulSword = ItemSoulSword()
		spatiotemporalRing = ItemSpatiotemporalRing()
		splashPotion = ItemSplashPotion()
		starPlacer = ItemStarPlacer()
		starPlacer2 = ItemStarPlacer2()
		//storyToken = ItemStoryToken()
		subspaceSpear = ItemSpearSubspace()
		thinkingHand = ItemThinkingHand()
		trisDagger = ItemTrisDagger()
		triquetrum = ItemTriquetrum()
		//toolbelt = ItemToolbelt()
		wireAxe = ItemWireAxe()
		wiltedLotus = ItemWiltedLotus()
		
		royalStaff = ItemRoyalStaff()
		`DEV-NULL` = if (ModInfo.DEV && !ModInfo.OBF) TheRodOfTheDebug() else null
		
		// that's ok because there is check on first 6 array elements in the dice
		ItemDice.relicStacks += arrayOf(ItemStack(flugelSoul),
										ItemStack(mask),
										ItemStack(excaliber),
										ItemStack(subspaceSpear),
										ItemStack(moonlightBow),
										ItemStack(mjolnir),
										ItemStack(gungnir),
										ItemStack(priestRingHeimdall),
										ItemStack(priestRingNjord),
										ItemStack(priestRingSif),
										ItemStack(akashicRecords))
	}
	
	fun regOreDict() {
		OreDictionary.registerOre(LibOreDict.ELVORIUM_INGOT, ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot))
		OreDictionary.registerOre(LibOreDict.MAUFTRIUM_INGOT, ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot))
		OreDictionary.registerOre(LibOreDict.MUSPELHEIM_POWER_INGOT, ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimPowerIngot))
		OreDictionary.registerOre(LibOreDict.NIFLHEIM_POWER_INGOT, ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimPowerIngot))
		OreDictionary.registerOre(LibOreDict.ELVORIUM_NUGGET, ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumNugget))
		OreDictionary.registerOre(LibOreDict.MAUFTRIUM_NUGGET, ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumNugget))
		OreDictionary.registerOre(LibOreDict.MUSPELHEIM_ESSENCE, ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimEssence))
		OreDictionary.registerOre(LibOreDict.NIFLHEIM_ESSENCE, ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimEssence))
		OreDictionary.registerOre(LibOreDict.IFFESAL_DUST, ItemStack(elvenResource, 1, ElvenResourcesMetas.IffesalDust))
		OreDictionary.registerOre(LibOreDict.FENRIR_FUR, ItemStack(elvenResource, 1, ElvenResourcesMetas.FenrirFur))
		OreDictionary.registerOre(LibOreDict.ARUNE[0], ItemStack(elvenResource, 1, ElvenResourcesMetas.PrimalRune))
		OreDictionary.registerOre(LibOreDict.ARUNE[1], ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimRune))
		OreDictionary.registerOre(LibOreDict.ARUNE[2], ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimRune))
		OreDictionary.registerOre(LibOreDict.INFUSED_DREAM_TWIG, ItemStack(elvenResource, 1, ElvenResourcesMetas.InfusedDreamwoodTwig))
		
		// Iridescense
		
		OreDictionary.registerOre(LibOreDict.TWIG_THUNDERWOOD, ItemStack(elvenResource, 1, ElvenResourcesMetas.ThunderwoodTwig))
		OreDictionary.registerOre(LibOreDict.SPLINTERS_THUNDERWOOD, ItemStack(elvenResource, 1, ElvenResourcesMetas.ThunderwoodSplinters))
		OreDictionary.registerOre(LibOreDict.TWIG_NETHERWOOD, ItemStack(elvenResource, 1, ElvenResourcesMetas.NetherwoodTwig))
		OreDictionary.registerOre(LibOreDict.SPLINTERS_NETHERWOOD, ItemStack(elvenResource, 1, ElvenResourcesMetas.NetherwoodSplinters))
		OreDictionary.registerOre(LibOreDict.COAL_NETHERWOOD, ItemStack(elvenResource, 1, ElvenResourcesMetas.NetherwoodCoal))
		OreDictionary.registerOre(LibOreDict.DYES[16], ItemStack(elvenResource, 1, ElvenResourcesMetas.RainbowDust))
		OreDictionary.registerOre(LibOreDict.FLORAL_POWDER, ItemStack(elvenResource, 1, ElvenResourcesMetas.RainbowDust))
		OreDictionary.registerOre(LibOreDict.RAINBOW_PETAL, ItemStack(elvenResource, 1, ElvenResourcesMetas.RainbowPetal))
		OreDictionary.registerOre(LibOreDict.RAINBOW_QUARTZ, ItemStack(elvenResource, 1, ElvenResourcesMetas.RainbowQuartz))
		OreDictionary.registerOre(LibOreDict.PETAL_ANY, ItemStack(elvenResource, 1, ElvenResourcesMetas.RainbowPetal))
		
		OreDictionary.registerOre(LibOreDict.HOLY_PENDANT, ItemStack(attributionBauble, 1, OreDictionary.WILDCARD_VALUE))
		
		OreDictionary.registerOre(LibOreDict.DYES[16], ItemStack(ModBlocks.bifrostPerm))
		OreDictionary.registerOre(LibOreDict.FLORAL_POWDER, ItemStack(ModItems.dye, 1, OreDictionary.WILDCARD_VALUE))
		OreDictionary.registerOre(LibOreDict.PETAL_ANY, ItemStack(ModItems.petal, 1, OreDictionary.WILDCARD_VALUE))
		
		
		
		OreDictionary.registerOre("coal", ItemStack(Items.coal))
		OreDictionary.registerOre("coal", ItemStack(Items.coal, 1, 1))
	}
}
