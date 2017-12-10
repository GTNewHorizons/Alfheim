package alfheim.common.registry;

import static alexsocol.asjlib.ASJUtilities.*;

import java.util.List;

import alfheim.common.items.CreativeReachPendant;
import alfheim.common.items.ElementalAirBoots;
import alfheim.common.items.ElementalEarthChest;
import alfheim.common.items.ElementalFireLeggings;
import alfheim.common.items.ElementalWaterHelm;
import alfheim.common.items.ElementalWaterHelmRevealing;
import alfheim.common.items.ElvenResource;
import alfheim.common.items.ElvoriumArmor;
import alfheim.common.items.ElvoriumHelmet;
import alfheim.common.items.ElvoriumHelmetRevealing;
import alfheim.common.items.FirePendant;
import alfheim.common.items.IcePendant;
import alfheim.common.items.ItemManaStorage;
import alfheim.common.items.LivingrockPickaxe;
import alfheim.common.items.RealitySword;
import alfheim.common.items.Rod;
import baubles.api.BaubleType;
import net.minecraft.item.Item;
import scala.actors.threadpool.Arrays;

public class AlfheimItems {
	
	// There is some alphabetic mess cause Botania .setUnlocalizedName method includes registration,
	// so I need to construct some items in odd places to get beautiful Creative Tab representation :D
	// and yes, I'm too lazy to just reOverride Vazkii's code :P
	
	public static Item elementalHelmet = new ElementalWaterHelm(); //.setTextureName(Constants.MODID + ":ElementalHelmet").setUnlocalizedName("ElementalHelmet");
	public static Item elementalHelmetRevealing = new ElementalWaterHelmRevealing();
	public static Item elementalLeggings = new ElementalFireLeggings(); //.setTextureName(Constants.MODID + ":ElementalLeggings").setUnlocalizedName("ElementalLeggings");
	public static Item elementalChestplate = new ElementalEarthChest(); //.setTextureName(Constants.MODID + ":ElementalChestplate").setUnlocalizedName("ElementalChestplate");
	public static Item elementalBoots = new ElementalAirBoots(); //.setTextureName(Constants.MODID + ":ElementalBoots").setUnlocalizedName("ElementalBoots");
	public static Item elvenResource = new ElvenResource();
	public static Item elvoriumHelmet = new ElvoriumHelmet();
	public static Item elvoriumHelmetRevealing = new ElvoriumHelmetRevealing();
	public static Item elvoriumChestplate = new ElvoriumArmor(1, "ElvoriumChestplate");
	public static Item elvoriumLeggings = new ElvoriumArmor(2, "ElvoriumLeggings");
	public static Item elvoriumBoots = new ElvoriumArmor(3, "ElvoriumBoots");
	public static Item elfFirePendant = new FirePendant();
	public static Item elfIcePendant = new IcePendant();
	public static Item creativeReachPendant = new CreativeReachPendant();
	public static Item livingrockPickaxe = new LivingrockPickaxe();
	public static Item manaStone = new ItemManaStorage("ManaStone", 2, (BaubleType) null);
	public static Item manaStoneGreater = new ItemManaStorage("ManaStoneGreater", 8, (BaubleType) null);
	public static Item manaElvenRing = new ItemManaStorage("ManaElvenRing", 2, BaubleType.RING);
	public static Item manaElvenRingGreater = new ItemManaStorage("ManaElvenRingGreater", 8, BaubleType.RING);
	public static Item realitySword = new RealitySword();
	public static Item rod = new Rod();

	
	public static void init() {
		reg();
		regOreDict();
	}

	private static void reg() {
		register(realitySword);
		register(livingrockPickaxe);
		register(manaStone);
		register(manaStoneGreater);
		register(manaElvenRing);
		register(manaElvenRingGreater);
		register(rod);
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
