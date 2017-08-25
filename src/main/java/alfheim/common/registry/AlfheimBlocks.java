package alfheim.common.registry;

import static alexsocol.asjlib.ASJUtilities.*;
import static cpw.mods.fml.common.registry.GameRegistry.*;
import static net.minecraft.block.Block.*;
import static net.minecraftforge.oredict.OreDictionary.*;

import alexsocol.asjlib.BlockPattern;
import alfheim.AlfheimCore;
import alfheim.ModInfo;
import alfheim.common.blocks.AlfheimPortal;
import alfheim.common.blocks.ElvenOres;
import alfheim.common.blocks.ManaInfuser;
import alfheim.common.items.ItemBlockElvenOres;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;

public class AlfheimBlocks {

	public static Block alfheimPortal = new AlfheimPortal();
	public static Block elvenOres = new ElvenOres();
	public static Block elvoriumBlock = new BlockPattern(ModInfo.MODID, Material.iron, "ElvoriumBlock", AlfheimCore.alfheimTab, 0, 1, 5, "pickaxe", 1, 60, soundTypeMetal, true, true);
	public static Block mauftriumBlock = new BlockPattern(ModInfo.MODID, Material.iron, "MauftriumBlock", AlfheimCore.alfheimTab, 0, 1, 5, "pickaxe", 1, 60, soundTypeMetal, true, true);
	public static Block manaInfuser = new ManaInfuser();
	
	public static void init() {
		register();
		registerOreDict();
	}

	private static void register() {
		registerBlock(alfheimPortal);
		registerBlock(elvenOres, ItemBlockElvenOres.class, getBlockName(elvenOres));
		registerBlock(elvoriumBlock);
		registerBlock(mauftriumBlock);
		registerBlock(manaInfuser);
	}

	private static void registerOreDict() {
		registerOre("oreGold", new ItemStack(elvenOres, 1, 3));
	}
}
