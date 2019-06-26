package alfheim.common.core.registry

import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.api.lib.LibOreDict
import alfheim.common.integration.thaumcraft.ThaumcraftAlfheimModule
import alfheim.common.item.*
import alfheim.common.item.equipment.armor.elemental.*
import alfheim.common.item.equipment.armor.elvoruim.*
import alfheim.common.item.equipment.bauble.*
import alfheim.common.item.equipment.tool.*
import alfheim.common.item.equipment.tool.elementuim.ItemElementiumHoe
import alfheim.common.item.equipment.tool.manasteel.ItemManasteelHoe
import alfheim.common.item.interaction.thaumcraft.*
import alfheim.common.item.material.ItemElvenResource
import alfheim.common.item.relic.*
import alfheim.common.item.rod.*
import baubles.api.BaubleType
import net.minecraft.item.*
import net.minecraftforge.oredict.OreDictionary
import vazkii.botania.common.Botania
import vazkii.botania.common.item.ModItems
import vazkii.botania.common.item.equipment.bauble.ItemBalanceCloak
import vazkii.botania.common.item.record.ItemModRecord

import java.util.*

import alexsocol.asjlib.ASJUtilities.register

object AlfheimItems {
	
	var astrolabe: Item
	var auraRingElven: Item
	var auraRingGod: Item
	var balanceCloak: Item
	var cloudPendant: Item
	var cloudPendantSuper: Item
	var creativeReachPendant: Item
	var crescentMoonAmulet: Item
	var dodgeRing: Item
	var elementalBoots: Item
	var elementalChestplate: Item
	var elementalHelmet: Item
	var elementalHelmetRevealing: Item
	var elementalLeggings: Item
	var elementiumHoe: Item
	var elfFirePendant: Item
	var elfIcePendant: Item
	val elvenResource: Item = ItemElvenResource() // Because it MUST be constructed BEFORE blocks.
	var elvoriumBoots: Item
	var elvoriumChestplate: Item
	var elvoriumHelmet: Item
	var elvoriumHelmetRevealing: Item
	var elvoriumLeggings: Item
	var excaliber: Item
	var flugelDisc: Item
	var flugelHead: Item
	var flugelSoul: Item
	var holoProjector: Item
	var invisibilityCloak: Item
	var livingrockPickaxe: Item
	var lootInterceptor: Item
	var manaRingElven: Item
	var manaRingGod: Item
	var manasteelHoe: Item
	var manaStone: Item
	var manaStoneGreater: Item
	var mask: Item
	//public static Item mjolnir;
	var paperBreak: Item
	var peacePipe: Item
	var pixieAttractor: Item
	var realitySword: Item
	var rodFire: Item
	var rodGrass: Item
	var rodIce: Item
	var thinkingHand: Item
	
	fun init() {
		construct()
		reg()
		regOreDict()
	}
	
	// There is some alphabetic mess cause Botania .setUnlocalizedName method includes registration,
	// so I need to construct some items in odd places to get beautiful Creative Tab representation :D
	// and yes, I'm too lazy to just reOverride Vazkii's code :P
	private fun construct() {
		astrolabe = ItemAstrolabe()
		elementalHelmet = ItemElementalWaterHelm()
		if (Botania.thaumcraftLoaded) elementalHelmetRevealing = ItemElementalWaterHelmRevealing()
		elementalChestplate = ItemElementalEarthChest()
		elementalLeggings = ItemElementalFireLeggings()
		elementalBoots = ItemElementalAirBoots()
		elementiumHoe = ItemElementiumHoe()
		elvoriumHelmet = ItemElvoriumHelmet()
		if (Botania.thaumcraftLoaded) elvoriumHelmetRevealing = ItemElvoriumHelmetRevealing()
		elvoriumChestplate = ItemElvoriumArmor(1, "ElvoriumChestplate")
		elvoriumLeggings = ItemElvoriumArmor(2, "ElvoriumLeggings")
		elvoriumBoots = ItemElvoriumArmor(3, "ElvoriumBoots")
		flugelHead = ItemFlugelHead()
		realitySword = ItemRealitySword()
		excaliber = ItemExcaliber()
		holoProjector = ItemHoloProjector()
		//mjolnir = new ItemMjolnir();
		mask = ItemTankMask()
		flugelSoul = ItemFlugelSoul()
		flugelDisc = ItemModRecord("flugel", "FlugelDisc").setCreativeTab(AlfheimCore.alfheimTab)
		elfFirePendant = ItemPendant("FirePendant")
		elfIcePendant = ItemPendant("IcePendant")
		creativeReachPendant = ItemCreativeReachPendant()
		pixieAttractor = ItemPendant("PixieAttractor")
		livingrockPickaxe = ItemLivingrockPickaxe()
		lootInterceptor = ItemLootInterceptor()
		manasteelHoe = ItemManasteelHoe()
		manaStone = ItemManaStorage("ManaStone", 3.0, null)
		manaStoneGreater = ItemManaStorage("ManaStoneGreater", 8.0, null)
		manaRingElven = ItemManaStorage("ManaRingElven", 5.0, BaubleType.RING)
		auraRingElven = ItemAuraRingAlfheim("AuraRingElven")
		manaRingGod = ItemManaStorage("ManaRingGod", 10.0, BaubleType.RING)
		auraRingGod = object: ItemAuraRingAlfheim("AuraRingGod") {
			override val delay: Int
				get() = 2
		}
		dodgeRing = ItemDodgeRing()
		cloudPendant = ItemCloudPendant()
		cloudPendantSuper = ItemCloudPendant("SuperCloudPendant", 3)
		balanceCloak = ItemBalanceCloak()
		invisibilityCloak = ItemInvisibilityCloak()
		crescentMoonAmulet = ItemCrescentMoonAmulet()
		rodFire = ItemRodElemental("MuspelheimRod", AlfheimBlocks.redFlame)
		rodGrass = ItemRodGrass()
		rodIce = ItemRodElemental("NiflheimRod", AlfheimBlocks.poisonIce)
		paperBreak = ItemPaperBreak()
		peacePipe = ItemPeacePipe()
		thinkingHand = ItemThinkingHand()
		
		if (Botania.thaumcraftLoaded) {
			ModItems.elementiumHelmRevealing.creativeTab = ThaumcraftAlfheimModule.tcnTab
			ModItems.manasteelHelmRevealing.creativeTab = ThaumcraftAlfheimModule.tcnTab
			ModItems.terrasteelHelmRevealing.creativeTab = ThaumcraftAlfheimModule.tcnTab
		}
	}
	
	private fun reg() {
		register(flugelHead)
		register(holoProjector)
		register(realitySword)
		register(livingrockPickaxe)
		register(manasteelHoe)
		register(elementiumHoe)
		register(manaStone)
		register(manaStoneGreater)
		register(manaRingElven)
		register(manaRingGod)
		register(astrolabe)
		register(lootInterceptor)
		register(rodFire)
		register(rodIce)
		register(rodGrass)
		register(peacePipe)
		register(paperBreak)
		register(elvenResource)
		if (ModInfo.DEV) register(TheRodOfTheDebug())
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
	}
	
	object ElvenResourcesMetas {
		val InterdimensionalGatewayCore: Int
		val ManaInfusionCore: Int
		val ElvoriumIngot: Int
		val MauftriumIngot: Int
		val MuspelheimPowerIngot: Int
		val NiflheimPowerIngot: Int
		val ElvoriumNugget: Int
		val MauftriumNugget: Int
		val MuspelheimEssence: Int
		val NiflheimEssence: Int
		val IffesalDust: Int
		val PrimalRune: Int
		val MuspelheimRune: Int
		val NiflheimRune: Int
		val InfusedDreamwoodTwig: Int//Transferer BACK
		
		init {
			val items = Arrays.asList(*ItemElvenResource.subItems)
			InterdimensionalGatewayCore = items.indexOf("InterdimensionalGatewayCore")
			ManaInfusionCore = items.indexOf("ManaInfusionCore")
			ElvoriumIngot = items.indexOf("ElvoriumIngot")
			MauftriumIngot = items.indexOf("MauftriumIngot")
			MuspelheimPowerIngot = items.indexOf("MuspelheimPowerIngot")
			NiflheimPowerIngot = items.indexOf("NiflheimPowerIngot")
			ElvoriumNugget = items.indexOf("ElvoriumNugget")
			MauftriumNugget = items.indexOf("MauftriumNugget")
			MuspelheimEssence = items.indexOf("MuspelheimEssence")
			NiflheimEssence = items.indexOf("NiflheimEssence")
			IffesalDust = items.indexOf("IffesalDust")
			PrimalRune = items.indexOf("PrimalRune")
			MuspelheimRune = items.indexOf("MuspelheimRune")
			NiflheimRune = items.indexOf("NiflheimRune")
			InfusedDreamwoodTwig = items.indexOf("InfusedDreamwoodTwig")
			//Transferer = items.indexOf("Transferer"); BACK
		}
	}
}
