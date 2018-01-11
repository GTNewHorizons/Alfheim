package alfheim.common.core.registry;

import java.util.ArrayList;
import java.util.List;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.common.block.tile.*;
import alfheim.common.entity.*;
import alfheim.common.world.dim.alfheim.gen.WorldGenAlfheim;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

public class AlfheimRegistry {
	
	public static final ArmorMaterial ELVORIUM = EnumHelper.addArmorMaterial("ELVORIUM", 50, new int[] {5, 10, 8, 5}, 30);
	public static final ArmorMaterial ELEMENTAL = EnumHelper.addArmorMaterial("ELEMENTAL", 20, new int[] {2, 9, 5, 2}, 20);
	public static final ToolMaterial REALITY = EnumHelper.addToolMaterial("REALITY", 10, 9000, 3, 8, 30);
	public static final IWorldGenerator worldGen = new WorldGenAlfheim();
	
	/** List of RecipeElvenTrade outputs forbidden for re'trading from Alfheim trade portal */
	public static List<ItemStack> forbiddenRetrades = new ArrayList();
	
	public static void addForbiddenRetrade(ItemStack output) {
		forbiddenRetrades.add(output);
	}
	
	public static boolean isForbidden(ItemStack output) {
		for (ItemStack out : forbiddenRetrades) if (ItemStack.areItemStacksEqual(output, out)) return true;
		return false;
	}
	
	public static void preInit() {
		registerEntities();
		registerTileEntities();
	}

	public static void init() {
		GameRegistry.registerWorldGenerator(worldGen, 1);
		forbidRetrades();
	}
	
	public static void forbidRetrades() {
		addForbiddenRetrade(AlfheimRecipes.recipeInterdimensional.getOutput());
		addForbiddenRetrade(new ItemStack(Items.iron_ingot));
		addForbiddenRetrade(new ItemStack(Blocks.iron_block));
		addForbiddenRetrade(new ItemStack(Items.ender_pearl));
		addForbiddenRetrade(new ItemStack(Items.diamond));
		addForbiddenRetrade(new ItemStack(Blocks.diamond_block));
	}
	
	private static void registerEntities() {
		ASJUtilities.registerEntityEgg(EntityElf.class, "Elf", 0x1A660A, 0x4D3422, AlfheimCore.instance);
		ASJUtilities.registerEntityEgg(EntityAlfheimPixie.class, "Pixie", 0xFF76D6, 0xFFE3FF, AlfheimCore.instance);
	}
	
	private static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileAlfheimPortal.class, "AlfheimPortal");
		GameRegistry.registerTileEntity(TileManaInfuser.class, "ManaInfuser");
		GameRegistry.registerTileEntity(TileTradePortal.class, "TradePortal");
	}
}
