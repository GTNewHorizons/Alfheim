package alfheim.common.core.registry;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.common.block.tile.TileAlfheimPortal;
import alfheim.common.block.tile.TileElvenPylon;
import alfheim.common.block.tile.TileManaInfuser;
import alfheim.common.block.tile.TileTradePortal;
import alfheim.common.entity.EntityAlfheimPixie;
import alfheim.common.entity.EntityElf;
import alfheim.common.world.dim.alfheim.gen.WorldGenAlfheim;
import cpw.mods.fml.common.IWorldGenerator;
import cpw.mods.fml.common.registry.GameRegistry;

public class AlfheimRegistry {
	
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
		GameRegistry.registerTileEntity(TileElvenPylon.class, "ElvenPylon");
		GameRegistry.registerTileEntity(TileManaInfuser.class, "ManaInfuser");
		GameRegistry.registerTileEntity(TileTradePortal.class, "TradePortal");
	}
}
