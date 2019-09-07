package alfheim.common.item

import alfheim.api.ModInfo
import alfheim.api.lib.LibOreDict
import alfheim.common.block.AlfheimBlocks
import alfheim.common.core.util.AlfheimTab
import alfheim.common.item.block.*
import alfheim.common.item.creator.*
import alfheim.common.item.equipment.armor.elemental.*
import alfheim.common.item.equipment.armor.elvoruim.*
import alfheim.common.item.equipment.bauble.*
import alfheim.common.item.equipment.tool.*
import alfheim.common.item.equipment.tool.elementuim.ItemElementiumHoe
import alfheim.common.item.equipment.tool.manasteel.ItemManasteelHoe
import alfheim.common.item.interaction.thaumcraft.*
import alfheim.common.item.material.*
import alfheim.common.item.relic.*
import alfheim.common.item.rod.*
import net.minecraft.item.*
import net.minecraftforge.oredict.OreDictionary
import vazkii.botania.common.Botania
import vazkii.botania.common.block.ModBlocks
import vazkii.botania.common.item.ModItems
import vazkii.botania.common.item.equipment.bauble.ItemBalanceCloak
import vazkii.botania.common.item.record.ItemModRecord

object AlfheimItems {
	
	val `DEV-NULL`: Item?
	
	val attributionBauble: Item
	val aesirEmblem: Item
	val astrolabe: Item
	val auraRingElven: Item
	val auraRingGod: Item
	val balanceCloak: Item
	val cloudPendant: Item
	val cloudPendantSuper: Item
	val coatOfArms: Item
	val colorOverride: Item
	val creativeReachPendant: Item
	val crescentMoonAmulet: Item
	val dodgeRing: Item
	val elementalBoots: Item
	val elementalChestplate: Item
	val elementalHelmet: Item
	val elementalHelmetRevealing: Item?
	val elementalLeggings: Item
	val elementiumHoe: Item
	val elfFirePendant: Item
	val elfIcePendant: Item
	val elvenResource: Item
	val elvoriumBoots: Item
	val elvoriumChestplate: Item
	val elvoriumHelmet: Item
	val elvoriumHelmetRevealing: Item?
	val elvoriumLeggings: Item
	val emblem: Item
	val excaliber: Item
	val fireGrenade: Item
	val flugelDisc: Item
	val flugelDisc2: Item
	val flugelHead: Item
	val flugelHead2: Item
	val flugelSoul: Item
	val invisibilityCloak: Item
	val invisibleFlameLens: Item
	val irisSeeds: Item
	val livingrockPickaxe: Item
	val lootInterceptor: Item
	val manaRingElven: Item
	val manaRingGod: Item
	val manasteelHoe: Item
	val manaStone: Item
	val manaStoneGreater: Item
	val mask: Item
	//val mjolnir: Item
	val moonlightBow: Item
	val multibauble: Item
	val paperBreak: Item
	val peacePipe: Item
	val pixieAttractor: Item
	val realitySword: Item
	val rodColorfulSkyDirt: Item
	val rodFire: Item
	val rodFlameStar: Item
	val rodGrass: Item
	val rodIce: Item
	val rodInterdiction: Item
	val rodLightning: Item
	val rodPrismatic: Item
	val spatiotemporalRing: Item
	val splashPotion: Item
	val starPlacer: Item
	val starPlacer2: Item
	//val storyToken: Item
	val subspaceSpear: Item
	val thinkingHand: Item
	val trisDagger: Item
	val toolbelt: Item
	val wiltedLotus: Item
	val wireAxe: Item
	
	val royalStaff: Item

	init {
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
		elvenResource = ItemElvenResource()
		elvoriumHelmet = ItemElvoriumHelmet()
		elvoriumHelmetRevealing = if (Botania.thaumcraftLoaded) ItemElvoriumHelmetRevealing() else null
		elvoriumChestplate = ItemElvoriumArmor(1, "ElvoriumChestplate")
		elvoriumLeggings = ItemElvoriumArmor(2, "ElvoriumLeggings")
		elvoriumBoots = ItemElvoriumArmor(3, "ElvoriumBoots")
		emblem = ItemPriestEmblem()
		excaliber = ItemExcaliber()
		flugelDisc = ItemModRecord("flugel", "FlugelDisc").setCreativeTab(AlfheimTab)
		flugelDisc2 = ItemModRecord("miku", "MikuDisc").setCreativeTab(null)
		flugelHead = ItemHeadFlugel()
		flugelHead2 = ItemHeadMiku()
		flugelSoul = ItemFlugelSoul()
		invisibilityCloak = ItemInvisibilityCloak()
		invisibleFlameLens = ItemLensFlashInvisible()
		irisSeeds = ItemColorSeeds()
		livingrockPickaxe = ItemLivingrockPickaxe()
		lootInterceptor = ItemLootInterceptor()
		manasteelHoe = ItemManasteelHoe()
		manaRingElven = ItemManaStorageRing("ManaRingElven", 5.0)
		manaRingGod = ItemManaStorageRing("ManaRingGod", 10.0)
		manaStone = ItemManaStorage("ManaStone", 3.0)
		manaStoneGreater = ItemManaStorage("ManaStoneGreater", 8.0)
		mask = ItemTankMask()
		//mjolnir = new ItemMjolnir();
		moonlightBow = ItemMoonlightBow()
		multibauble = ItemMultibauble()
		paperBreak = ItemPaperBreak()
		peacePipe = ItemPeacePipe()
		pixieAttractor = ItemPendant("PixieAttractor")
		realitySword = ItemRealitySword()
		rodColorfulSkyDirt = ItemRodIridescent()
		rodFire = ItemRodElemental("MuspelheimRod", AlfheimBlocks.redFlame)
		rodFlameStar = ItemRodFlameStar()
		rodIce = ItemRodElemental("NiflheimRod", AlfheimBlocks.poisonIce)
		rodInterdiction = ItemRodInterdiction()
		rodLightning = ItemRodLightning()
		rodPrismatic = ItemRodPrismatic()
		rodGrass = ItemRodGrass()
		spatiotemporalRing = ItemSpatiotemporalRing()
		splashPotion = ItemSplashPotion()
		starPlacer = ItemStarPlacer()
		starPlacer2 = ItemStarPlacer2()
		//storyToken = ItemStoryToken()
		subspaceSpear = ItemSpearSubspace()
		thinkingHand = ItemThinkingHand()
		trisDagger = ItemTrisDagger()
		toolbelt = ItemToolbelt()
		wireAxe = ItemWireAxe()
		wiltedLotus = ItemWiltedLotus()
		
		royalStaff = ItemRoyalStaff()
		`DEV-NULL` = TheRodOfTheDebug() // if (ModInfo.DEV) TheRodOfTheDebug() else null
		
		regOreDict()
	}
	
	private fun regOreDict() {
		OreDictionary.registerOre(LibOreDict.ELVORIUM_INGOT, ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot))
		OreDictionary.registerOre(LibOreDict.MAUFTRIUM_INGOT, ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot))
		OreDictionary.registerOre(LibOreDict.MUSPELHEIM_POWER_INGOT, ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimPowerIngot))
		OreDictionary.registerOre(LibOreDict.NIFLHEIM_POWER_INGOT, ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimPowerIngot))
		OreDictionary.registerOre(LibOreDict.ELVORIUM_NUGGET, ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumNugget))
		OreDictionary.registerOre(LibOreDict.MAUFTRIUM_NUGGET, ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumNugget))
		OreDictionary.registerOre(LibOreDict.MUSPELHEIM_ESSENCE, ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimEssence))
		OreDictionary.registerOre(LibOreDict.NIFLHEIM_ESSENCE, ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimEssence))
		OreDictionary.registerOre(LibOreDict.IFFESAL_DUST, ItemStack(elvenResource, 1, ElvenResourcesMetas.IffesalDust))
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
		OreDictionary.registerOre(LibOreDict.PETAL, ItemStack(elvenResource, 1, ElvenResourcesMetas.RainbowPetal))
		
		OreDictionary.registerOre(LibOreDict.HOLY_PENDANT, ItemStack(attributionBauble, 1, OreDictionary.WILDCARD_VALUE))
		
		OreDictionary.registerOre(LibOreDict.DYES[16], ItemStack(ModBlocks.bifrostPerm))
		OreDictionary.registerOre(LibOreDict.FLORAL_POWDER, ItemStack(ModItems.dye, 1, OreDictionary.WILDCARD_VALUE))
		OreDictionary.registerOre(LibOreDict.PETAL, ItemStack(ModItems.petal, 1, OreDictionary.WILDCARD_VALUE))
	}
}
