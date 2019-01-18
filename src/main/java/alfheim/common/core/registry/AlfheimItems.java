package alfheim.common.core.registry;

import static alexsocol.asjlib.ASJUtilities.*;

import java.util.Arrays;
import java.util.List;

import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.api.lib.LibOreDict;
import alfheim.common.item.ItemAstrolabe;
import alfheim.common.item.ItemFlugelHead;
import alfheim.common.item.ItemHoloProjector;
import alfheim.common.item.ItemPaperBreak;
import alfheim.common.item.ItemPeacePipe;
import alfheim.common.item.TheRodOfTheDebug;
import alfheim.common.item.equipment.armor.elemental.*;
import alfheim.common.item.equipment.armor.elvoruim.*;
import alfheim.common.item.equipment.bauble.*;
import alfheim.common.item.equipment.tool.*;
import alfheim.common.item.equipment.tool.elementuim.ItemElementiumHoe;
import alfheim.common.item.equipment.tool.manasteel.ItemManasteelHoe;
import alfheim.common.item.interaction.thaumcraft.*;
import alfheim.common.item.material.*;
import alfheim.common.item.relic.*;
import alfheim.common.item.rod.*;
import baubles.api.BaubleType;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.bauble.ItemAuraRing;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;
import vazkii.botania.common.item.equipment.bauble.ItemBaubleCosmetic;
import vazkii.botania.common.item.record.ItemModRecord;

public class AlfheimItems {

	public static Item astrolabe;
	public static Item auraRingElven;
	public static Item auraRingGod;
	public static Item cloudPendant;
	public static Item cloudPendantSuper;
	public static Item creativeReachPendant;
	public static Item dodgeRing;
	public static Item elementalBoots;
	public static Item elementalChestplate;
	public static Item elementalHelmet;
	public static Item elementalHelmetRevealing;
	public static Item elementalLeggings;
	public static Item elementiumHoe;
	public static Item elfFirePendant;
	public static Item elfIcePendant;
	public static Item elvenResource = new ItemElvenResource(); // Because it MUST be constructed BEFORE blocks.
	public static Item elvoriumBoots;
	public static Item elvoriumChestplate;
	public static Item elvoriumHelmet;
	public static Item elvoriumHelmetRevealing;
	public static Item elvoriumLeggings;
	public static Item excaliber;
	public static Item flugelDisc;
	public static Item flugelHead;
	public static Item flugelSoul;
	public static Item holoProjector;
	public static Item livingrockPickaxe;
	public static Item manaRingElven;
	public static Item manaRingGod;
	public static Item manasteelHoe;
	public static Item manaStone;
	public static Item manaStoneGreater;
	public static Item mask;
	//public static Item mjolnir;
	public static Item paperBreak;
	public static Item peacePipe;
	public static Item pixieAttractor;
	public static Item realitySword;
	public static Item rodFire;
	public static Item rodGrass;
	public static Item rodIce;
	public static Item thinkingHand;
	
	public static void init() {
		construct();
		reg();
		regOreDict();
	}
	
	// There is some alphabetic mess cause Botania .setUnlocalizedName method includes registration,
	// so I need to construct some items in odd places to get beautiful Creative Tab representation :D
	// and yes, I'm too lazy to just reOverride Vazkii's code :P
	private static void construct() {
		astrolabe = new ItemAstrolabe();
		cloudPendant = new ItemCloudPendant();
		cloudPendantSuper = new ItemSuperCloudPendant();
		dodgeRing = new ItemDodgeRing();
		elementalHelmet = new ItemElementalWaterHelm();
		elementalHelmetRevealing = new ItemElementalWaterHelmRevealing();
		elementalChestplate = new ItemElementalEarthChest();
		elementalLeggings = new ItemElementalFireLeggings();
		elementalBoots = new ItemElementalAirBoots();
		elementiumHoe = new ItemElementiumHoe();
		elvoriumHelmet = new ItemElvoriumHelmet();
		elvoriumHelmetRevealing = new ItemElvoriumHelmetRevealing();
		elvoriumChestplate = new ItemElvoriumArmor(1, "ElvoriumChestplate");
		elvoriumLeggings = new ItemElvoriumArmor(2, "ElvoriumLeggings");
		elvoriumBoots = new ItemElvoriumArmor(3, "ElvoriumBoots");
		flugelHead = new ItemFlugelHead();
		realitySword = new ItemRealitySword();
		excaliber = new ItemExcaliber();
		holoProjector = new ItemHoloProjector();
		//mjolnir = new ItemMjolnir();
		mask = new ItemTankMask();
		flugelSoul = new ItemFlugelSoul();
		flugelDisc = new ItemModRecord("flugel", "FlugelDisc").setCreativeTab(AlfheimCore.alfheimTab);
		elfFirePendant = new ItemPendant("FirePendant");
		elfIcePendant = new ItemPendant("IcePendant");
		creativeReachPendant = new ItemCreativeReachPendant();
		pixieAttractor = new ItemPendant("PixieAttractor");
		livingrockPickaxe = new ItemLivingrockPickaxe();
		manasteelHoe = new ItemManasteelHoe();
		manaStone = new ItemManaStorage("ManaStone", 2.5, (BaubleType) null);
		manaStoneGreater = new ItemManaStorage("ManaStoneGreater", 8, (BaubleType) null);
		manaRingElven = new ItemManaStorage("ManaRingElven", 5, BaubleType.RING);
		auraRingElven = new ItemAuraRingAlfheim("AuraRingElven");
		manaRingGod = new ItemManaStorage("ManaRingGod", 12, BaubleType.RING);
		auraRingGod = new ItemAuraRingAlfheim("AuraRingGod") { public int getDelay() { return 2; } };
		rodFire = new ItemRodElemental("MuspelheimRod", AlfheimBlocks.redFlame);
		rodGrass = new ItemRodGrass();
		rodIce = new ItemRodElemental("NiflheimRod", AlfheimBlocks.poisonIce);
		paperBreak = new ItemPaperBreak();
		peacePipe = new ItemPeacePipe();
		thinkingHand = new ItemThinkingHand();
	}

	private static void reg() {
		register(flugelHead);
		register(holoProjector);
		register(realitySword);
		register(livingrockPickaxe);
		register(manasteelHoe);
		register(elementiumHoe);
		register(manaStone);
		register(manaStoneGreater);
		register(manaRingElven);
		register(manaRingGod);
		register(cloudPendant);
		register(cloudPendantSuper);
		register(dodgeRing);
		register(astrolabe);
		register(rodFire);
		register(rodIce);
		register(rodGrass);
		register(peacePipe);
		register(paperBreak);
		register(elvenResource);
		register(thinkingHand);
		if (ModInfo.DEV) register(new TheRodOfTheDebug());
	}

	private static void regOreDict() {
		OreDictionary.registerOre(LibOreDict.ELVORIUM_INGOT, new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumIngot));
		OreDictionary.registerOre(LibOreDict.MAUFTRIUM_INGOT, new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumIngot));
		OreDictionary.registerOre(LibOreDict.MUSPELHEIM_POWER_INGOT, new ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimPowerIngot));
		OreDictionary.registerOre(LibOreDict.NIFLHEIM_POWER_INGOT, new ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimPowerIngot));
		OreDictionary.registerOre(LibOreDict.ELVORIUM_NUGGET, new ItemStack(elvenResource, 1, ElvenResourcesMetas.ElvoriumNugget));
		OreDictionary.registerOre(LibOreDict.MAUFTRIUM_NUGGET, new ItemStack(elvenResource, 1, ElvenResourcesMetas.MauftriumNugget));
		OreDictionary.registerOre(LibOreDict.MUSPELHEIM_ESSENCE, new ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimEssence));
		OreDictionary.registerOre(LibOreDict.NIFLHEIM_ESSENCE, new ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimEssence));
		OreDictionary.registerOre(LibOreDict.IFFESAL_DUST, new ItemStack(elvenResource, 1, ElvenResourcesMetas.IffesalDust));
		OreDictionary.registerOre(LibOreDict.ARUNE[0], new ItemStack(elvenResource, 1, ElvenResourcesMetas.PrimalRune));
		OreDictionary.registerOre(LibOreDict.ARUNE[1], new ItemStack(elvenResource, 1, ElvenResourcesMetas.MuspelheimRune));
		OreDictionary.registerOre(LibOreDict.ARUNE[2], new ItemStack(elvenResource, 1, ElvenResourcesMetas.NiflheimRune));
	}
	
	public static class ElvenResourcesMetas {
		public static final int
		InterdimensionalGatewayCore,
		ManaInfusionCore,
		ElvoriumIngot,
		MauftriumIngot,
		MuspelheimPowerIngot,
		NiflheimPowerIngot,
		ElvoriumNugget,
		MauftriumNugget,
		MuspelheimEssence,
		NiflheimEssence,
		IffesalDust,
		PrimalRune,
		MuspelheimRune,
		NiflheimRune
		//Transferer BACK
		;
		static {
			List<String> items = Arrays.asList(ItemElvenResource.subItems);
			InterdimensionalGatewayCore = items.indexOf("InterdimensionalGatewayCore");
			ManaInfusionCore = items.indexOf("ManaInfusionCore");
			ElvoriumIngot = items.indexOf("ElvoriumIngot");
			MauftriumIngot = items.indexOf("MauftriumIngot");
			MuspelheimPowerIngot = items.indexOf("MuspelheimPowerIngot");
			NiflheimPowerIngot = items.indexOf("NiflheimPowerIngot");
			ElvoriumNugget = items.indexOf("ElvoriumNugget");
			MauftriumNugget = items.indexOf("MauftriumNugget");
			MuspelheimEssence = items.indexOf("MuspelheimEssence");
			NiflheimEssence = items.indexOf("NiflheimEssence");
			IffesalDust = items.indexOf("IffesalDust");
			PrimalRune = items.indexOf("PrimalRune");
			MuspelheimRune = items.indexOf("MuspelheimRune");
			NiflheimRune = items.indexOf("NiflheimRune");
			//Transferer = items.indexOf("Transferer"); BACK
		}
	}
}
