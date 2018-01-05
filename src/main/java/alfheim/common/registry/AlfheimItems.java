package alfheim.common.registry;

import static alexsocol.asjlib.ASJUtilities.register;

import java.util.Arrays;
import java.util.List;

import alfheim.common.item.CreativeReachPendant;
import alfheim.common.item.ElementalAirBoots;
import alfheim.common.item.ElementalEarthChest;
import alfheim.common.item.ElementalFireLeggings;
import alfheim.common.item.ElementalWaterHelm;
import alfheim.common.item.ElementalWaterHelmRevealing;
import alfheim.common.item.ElvenResource;
import alfheim.common.item.ElvoriumArmor;
import alfheim.common.item.ElvoriumHelmet;
import alfheim.common.item.ElvoriumHelmetRevealing;
import alfheim.common.item.FirePendant;
import alfheim.common.item.IcePendant;
import alfheim.common.item.ItemManaStorage;
import alfheim.common.item.LivingrockPickaxe;
import alfheim.common.item.RealitySword;
import alfheim.common.item.Rod;
import baubles.api.BaubleType;
import net.minecraft.item.Item;

public class AlfheimItems {

	public static Item creativeReachPendant;
	public static Item elementalBoots;
	public static Item elementalChestplate;
	public static Item elementalHelmet;
	public static Item elementalHelmetRevealing;
	public static Item elementalLeggings;
	public static Item elfFirePendant;
	public static Item elfIcePendant;
	public static Item elvenResource;
	public static Item elvoriumBoots;
	public static Item elvoriumChestplate;
	public static Item elvoriumHelmet;
	public static Item elvoriumHelmetRevealing;
	public static Item elvoriumLeggings;
	public static Item livingrockPickaxe;
	public static Item manaElvenRing;
	public static Item manaElvenRingGreater;
	public static Item manaStone;
	public static Item manaStoneGreater;
	public static Item realitySword;
	public static Item rodFire;
	public static Item rodIce;
	
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
		elvenResource = new ElvenResource();
		elvoriumHelmet = new ElvoriumHelmet();
		elvoriumHelmetRevealing = new ElvoriumHelmetRevealing();
		elvoriumChestplate = new ElvoriumArmor(1, "ElvoriumChestplate");
		elvoriumLeggings = new ElvoriumArmor(2, "ElvoriumLeggings");
		elvoriumBoots = new ElvoriumArmor(3, "ElvoriumBoots");
		elfFirePendant = new FirePendant();
		elfIcePendant = new IcePendant();
		creativeReachPendant = new CreativeReachPendant();
		livingrockPickaxe = new LivingrockPickaxe();
		manaStone = new ItemManaStorage("ManaStone", 2, (BaubleType) null);
		manaStoneGreater = new ItemManaStorage("ManaStoneGreater", 8, (BaubleType) null);
		manaElvenRing = new ItemManaStorage("ManaElvenRing", 2, BaubleType.RING);
		manaElvenRingGreater = new ItemManaStorage("ManaElvenRingGreater", 8, BaubleType.RING);
		realitySword = new RealitySword();
		rodFire = new Rod("MuspelheimRod", AlfheimBlocks.redFlame);
		rodIce = new Rod("NiflheimRod", AlfheimBlocks.poisonIce);		
	}

	private static void reg() {
		register(realitySword);
		register(livingrockPickaxe);
		register(manaStone);
		register(manaStoneGreater);
		register(manaElvenRing);
		register(manaElvenRingGreater);
		register(rodFire);
		register(rodIce);
		register(elvenResource);
	}

	private static void regOreDict() {
		
	}
	
	public static class ElvenResourcesMetas {
		private static List<String> items;
		
		public static int
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
		NiflheimRune,
		TheRodOfTheDebug;
		
		static {
			items = Arrays.asList(ElvenResource.subItems);
			
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
			TheRodOfTheDebug = items.indexOf("TheRodOfTheDebug");
		}
	}
}
