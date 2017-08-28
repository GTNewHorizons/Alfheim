package alfheim.common.registry;

import static alexsocol.asjlib.ASJUtilities.registerItem;

import alexsocol.asjlib.ItemPattern;
import alfheim.AlfheimCore;
import alfheim.ModInfo;
import alfheim.common.items.ElementalArmor;
import alfheim.common.items.ElvenResource;
import alfheim.common.items.ElvoriumArmor;
import alfheim.common.items.ElvoriumHelmet;
import alfheim.common.items.ElvoriumHelmetRevealing;
import alfheim.common.items.ItemManaStorage;
import alfheim.common.items.RealitySword;
import baubles.api.BaubleType;
import net.minecraft.item.Item;

public class AlfheimItems {

	public static Item elementalLeggings = new ElementalArmor(2).setTextureName(ModInfo.MODID + ":ElementalLeggings").setUnlocalizedName("ElementalLeggings");
	public static Item elementalChestplate = new ElementalArmor(1).setTextureName(ModInfo.MODID + ":ElementalChestplate").setUnlocalizedName("ElementalChestplate");
	public static Item elementalHelmet = new ElementalArmor(0).setTextureName(ModInfo.MODID + ":ElementalHelmet").setUnlocalizedName("ElementalHelmet");
	public static Item elementalBoots = new ElementalArmor(3).setTextureName(ModInfo.MODID + ":ElementalBoots").setUnlocalizedName("ElementalBoots");
	public static Item elvenResource = new ElvenResource();
	public static Item elvoriumHelmet = new ElvoriumHelmet();
	public static Item elvoriumHelmetRevealing = new ElvoriumHelmetRevealing();
	public static Item elvoriumChestplate = new ElvoriumArmor(1, "ElvoriumChestplate");
	public static Item elvoriumLeggings = new ElvoriumArmor(2, "ElvoriumLeggings");
	public static Item elvoriumBoots = new ElvoriumArmor(3, "ElvoriumBoots");
	public static Item manaStone = new ItemManaStorage("ManaStone", 2, (BaubleType) null);
	public static Item manaStoneGreater = new ItemManaStorage("ManaStoneGreater", 8, (BaubleType) null);
	public static Item manaElvenRing = new ItemManaStorage("ManaElvenRing", 2, BaubleType.RING);
	public static Item manaElvenRingGreater = new ItemManaStorage("ManaElvenRingGreater", 8, BaubleType.RING);
	public static Item realitySword = new RealitySword();
	
	public static void init() {
		registration();
		oreDictRegistration();
	}

	private static void registration() {
		registerItem(elementalHelmet);
		registerItem(elementalChestplate);
		registerItem(elementalLeggings);
		registerItem(elementalBoots);
		registerItem(realitySword);
		registerItem(manaStone);
		registerItem(manaStoneGreater);
		registerItem(manaElvenRing);
		registerItem(manaElvenRingGreater);
		registerItem(elvenResource);
	}

	private static void oreDictRegistration() {
		
	}
}
