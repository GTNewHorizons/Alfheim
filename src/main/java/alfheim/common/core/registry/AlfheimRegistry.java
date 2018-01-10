package alfheim.common.core.registry;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.common.block.tile.TileAlfheimPortal;
import alfheim.common.block.tile.TileManaInfuser;
import alfheim.common.entity.EntityAlfheimPixie;
import alfheim.common.entity.EntityElf;
import alfheim.common.world.dim.alfheim.gen.WorldGenAlfheim;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;
import vazkii.botania.api.lexicon.multiblock.MultiblockSet;

public class AlfheimRegistry {
	
	public static final ArmorMaterial ELVORIUM = EnumHelper.addArmorMaterial("ELVORIUM", 50, new int[] {5, 10, 8, 5}, 30);
	public static final ArmorMaterial ELEMENTAL = EnumHelper.addArmorMaterial("ELEMENTAL", 20, new int[] {2, 9, 5, 2}, 20);
	public static final ToolMaterial REALITY = EnumHelper.addToolMaterial("REALITY", 10, 9000, 3, 8, 30);
	public static final IWorldGenerator worldGen = new WorldGenAlfheim();
	
	public static void preInit() {
		registerEntities();
		registerTileEntities();
	}

	public static void init() {
		GameRegistry.registerWorldGenerator(worldGen, 1);
	}
	
	private static void registerEntities() {
		ASJUtilities.registerEntityEgg(EntityElf.class, "Elf", 0x1A660A, 0x4D3422, AlfheimCore.instance);
		ASJUtilities.registerEntityEgg(EntityAlfheimPixie.class, "Pixie", 0xFF76D6, 0xFFE3FF, AlfheimCore.instance);
	}
	
	private static void registerTileEntities() {
		GameRegistry.registerTileEntity(TileAlfheimPortal.class, "AlfheimPortal");
		GameRegistry.registerTileEntity(TileManaInfuser.class, "ManaInfuser");
	}
}