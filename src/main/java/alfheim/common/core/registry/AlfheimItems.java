package alfheim.common.core.registry;

import static alexsocol.asjlib.ASJUtilities.*;

import java.util.Arrays;
import java.util.List;

import alfheim.AlfheimCore;
import alfheim.api.lib.LibOreDict;
import alfheim.common.item.ItemHoloProjector;
import alfheim.common.item.equipment.armor.elemental.*;
import alfheim.common.item.equipment.armor.elvoruim.*;
import alfheim.common.item.equipment.bauble.*;
import alfheim.common.item.equipment.tool.*;
import alfheim.common.item.interaction.thaumcraft.*;
import alfheim.common.item.material.*;
import alfheim.common.item.relic.*;
import alfheim.common.item.rod.*;
import baubles.api.BaubleType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;
import vazkii.botania.common.item.record.ItemModRecord;

public class AlfheimItems {

	public static Item creativeReachPendant;
	public static Item elementalBoots;
	public static Item elementalChestplate;
	public static Item elementalHelmet;
	public static Item elementalHelmetRevealing;
	public static Item elementalLeggings;
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
	public static Item flugelSoul;
	//public static Item holoProjector; FIXME
	public static Item livingrockPickaxe;
	public static Item manaElvenRing;
	public static Item manaElvenRingGreater;
	public static Item manaStone;
	public static Item manaStoneGreater;
	public static Item mask;
	//public static Item mjolnir;
	public static Item pixieAttractor;
	public static Item realitySword;
	public static Item rodFire;
	public static Item rodIce;
	//public static Item rodLightning; FIXME
	//public static Item rodRays; FIXME
	
	public static void init() {
		construct();
		reg();
		regOreDict();
	}
	
	// There is some alphabetic mess cause Botania .setUnlocalizedName method includes registration,
	// so I need to construct some items in odd places to get beautiful Creative Tab representation :D
	// and yes, I'm too lazy to just reOverride Vazkii's code :P
	private static void construct() {
		elementalHelmet = new ElementalWaterHelm();
		elementalHelmetRevealing = new ElementalWaterHelmRevealing();
		elementalLeggings = new ElementalFireLeggings();
		elementalChestplate = new ElementalEarthChest();
		elementalBoots = new ElementalAirBoots();
		elvoriumHelmet = new ElvoriumHelmet();
		elvoriumHelmetRevealing = new ElvoriumHelmetRevealing();
		elvoriumChestplate = new ElvoriumArmor(1, "ElvoriumChestplate");
		elvoriumLeggings = new ElvoriumArmor(2, "ElvoriumLeggings");
		elvoriumBoots = new ElvoriumArmor(3, "ElvoriumBoots");
		realitySword = new ItemRealitySword();
		excaliber = new ItemExcaliber();
		//holoProjector = new ItemHoloProjector(); FIXME
		//mjolnir = new ItemMjolnir();
		mask = new ItemTankMask();
		flugelSoul = new ItemFlugelSoul();
		flugelDisc = new ItemModRecord("flugel", "FlugelDisc").setCreativeTab(AlfheimCore.alfheimTab);
		elfFirePendant = new FirePendant();
		elfIcePendant = new IcePendant();
		creativeReachPendant = new CreativeReachPendant();
		pixieAttractor = new ItemBauble("PixieAttractor") {@Override public BaubleType getBaubleType(ItemStack stack){return BaubleType.AMULET;}}.setCreativeTab(AlfheimCore.alfheimTab);
		livingrockPickaxe = new ItemLivingrockPickaxe();
		manaStone = new ItemManaStorage("ManaStone", 2, (BaubleType) null);
		manaStoneGreater = new ItemManaStorage("ManaStoneGreater", 8, (BaubleType) null);
		manaElvenRing = new ItemManaStorage("ManaElvenRing", 2, BaubleType.RING);
		manaElvenRingGreater = new ItemManaStorage("ManaElvenRingGreater", 8, BaubleType.RING);
		rodFire = new ItemRodElemental("MuspelheimRod", AlfheimBlocks.redFlame);
		rodIce = new ItemRodElemental("NiflheimRod", AlfheimBlocks.poisonIce);
		//rodLightning = new ItemRodLightningMark(); FIXME
		//rodRays = new ItemRodRays(); FIXME
	}

	private static void reg() {
		//register(holoProjector); FIXME
		register(realitySword);
		register(livingrockPickaxe);
		register(manaStone);
		register(manaStoneGreater);
		register(manaElvenRing);
		register(manaElvenRingGreater);
		register(rodFire);
		register(rodIce);
		//register(rodLightning); FIXME
		//register(rodRays); FIXME
		register(elvenResource);
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
		//ManaTester,
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
		//TheRodOfTheDebug
		//Transferer FIXME
		;
		static {
			List<String> items = Arrays.asList(ItemElvenResource.subItems);
			//ManaTester = items.indexOf("ManaTester");
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
			//TheRodOfTheDebug = items.indexOf("TheRodOfTheDebug");
			//Transferer = items.indexOf("Transferer"); FIXME
		}
	}
}
