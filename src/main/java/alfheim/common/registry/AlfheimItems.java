package alfheim.common.registry;

import static alexsocol.asjlib.ASJUtilities.registerItem;

import alfheim.common.items.ElvenItems;
import alfheim.common.items.ElvoriumArmor;
import alfheim.common.items.ElvoriumHelmet;
import alfheim.common.items.ElvoriumHelmetRevealing;
import alfheim.common.items.ItemManaStorage;
import baubles.api.BaubleType;
import net.minecraft.item.Item;

public class AlfheimItems {
	
	public static Item elvenItems = new ElvenItems();
	public static Item elvoriumHelmet = new ElvoriumHelmet();
	public static Item elvoriumHelmetRevealing = new ElvoriumHelmetRevealing();
	public static Item elvoriumChestplate = new ElvoriumArmor(1, "ElvoriumChestplate");
	public static Item elvoriumLeggings = new ElvoriumArmor(2, "ElvoriumLeggings");
	public static Item elvoriumBoots = new ElvoriumArmor(3, "ElvoriumBoots");
	public static Item manaStone = new ItemManaStorage("ManaStone", 2, (BaubleType) null);
	public static Item manaStoneGreater = new ItemManaStorage("ManaStoneGreater", 8, (BaubleType) null);
	public static Item manaElvenRing = new ItemManaStorage("ManaElvenRing", 2, BaubleType.RING);
	public static Item manaElvenRingGreater = new ItemManaStorage("ManaElvenRingGreater", 8, BaubleType.RING);
	
	public static void init() {
		registration();
		oreDictRegistration();
	}

	private static void registration() {
		registerItem(elvenItems);
		// Elvorium Armor is registered automatically via Botania's overridden setUnlocalisedName method, so no need to do this one more time
		registerItem(manaStone);
		registerItem(manaStoneGreater);
		registerItem(manaElvenRing);
		registerItem(manaElvenRingGreater);
	}

	private static void oreDictRegistration() {
		
	}
}
