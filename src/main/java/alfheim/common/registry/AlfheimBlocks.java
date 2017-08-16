package alfheim.common.registry;

import static alexsocol.asjlib.ASJUtilities.*;
import static cpw.mods.fml.common.registry.GameRegistry.*;
import static net.minecraftforge.oredict.OreDictionary.*;

import alfheim.common.blocks.AlfheimPortal;
import alfheim.common.blocks.ElvenOres;
import alfheim.common.items.ItemBlockElvenOres;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class AlfheimBlocks {

	public static Block alfheimPortal = new AlfheimPortal();
	public static Block elvenOres = new ElvenOres();
	
	public static void init() {
		register();
		registerOreDict();
	}

	private static void register() {
		registerBlock(alfheimPortal);
		registerBlock(elvenOres, ItemBlockElvenOres.class, getBlockName(elvenOres));
	}

	private static void registerOreDict() {
		registerOre("oreGold", new ItemStack(elvenOres, 1, 3));
	}
}
