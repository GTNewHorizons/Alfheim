package alfheim.common.world.dim.alfheim.customgens;

import java.util.ArrayList;
import java.util.Random;

import alfheim.api.AlfheimAPI;
import alfheim.api.block.tile.SubTileEntity;
import alfheim.api.block.tile.SubTileEntity.EnumAnomalityRarity;
import alfheim.common.block.tile.TileAnomaly;
import alfheim.common.core.registry.AlfheimBlocks;
import alfheim.common.core.util.AlfheimConfig;
import alfheim.common.world.dim.alfheim.struct.StructureSpawnpoint;
import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldGenAlfheim implements IWorldGenerator {
	
	public static ArrayList<String> common = new ArrayList<String>();
	public static ArrayList<String> epic = new ArrayList<String>();
	public static ArrayList<String> rare = new ArrayList<String>();
	
	static {
		for (String s : AlfheimAPI.anomalies.keySet())
			switch (AlfheimAPI.anomalyInstances.get(s).getRarity()) {
				case COMMON: common.add(s); break;
				case EPIC: epic.add(s); break;
				case RARE: rare.add(s); break;
			} 	
	}
	
	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		if (world.provider.dimensionId == AlfheimConfig.dimensionIDAlfheim)
			generateAlfheim(rand, chunkX, chunkZ, world);
	}
	
	private static void generateAlfheim(final Random rand, int chunkX, int chunkZ, final World world) {
		if (chunkX == 0 && chunkZ == 0 && !world.isRemote) {
			Thread generate = new Thread(new Runnable() {
				@Override
				public void run() {
					StructureSpawnpoint.generate(world, rand);
				}
			}, "Alf Spawn Gen");
			generate.start();
		}
		
		if (rand.nextInt(AlfheimConfig.anomaliesDispersion) == 0) {
			int chance = rand.nextInt(32) + 1;
			if (chance == 32) 
				genRandomAnomalyOfRarity(rand, chunkX, chunkZ, world, EnumAnomalityRarity.EPIC);
			else if (chance >= 24)
				genRandomAnomalyOfRarity(rand, chunkX, chunkZ, world, EnumAnomalityRarity.RARE);
			else if (chance >= 16)
				genRandomAnomalyOfRarity(rand, chunkX, chunkZ, world, EnumAnomalityRarity.COMMON);
		}
	}
	
	private static void genRandomAnomalyOfRarity(Random rand, int chunkX, int chunkZ, World world, EnumAnomalityRarity rarity) {
		String type = "";
		switch (rarity) {
			case COMMON: type = common.get(rand.nextInt(common.size())); break;
			case EPIC: type = epic.get(rand.nextInt(epic.size())); break;
			case RARE: type = rare.get(rand.nextInt(rare.size())); break;
		}
		
		setAnomality(rand, chunkX, chunkZ, world, type);
	}
	
	private static void setAnomality(Random rand, int chunkX, int chunkZ, World world, String type) {
		int x = chunkX * 16 + rand.nextInt(16) + 8;
		int z = chunkZ * 16 + rand.nextInt(16) + 8;
		int y = world.getTopSolidOrLiquidBlock(x, z) + 1;
		
		world.setBlock(x, y, z, AlfheimBlocks.anomaly);
		TileEntity te = world.getTileEntity(x, y, z);
		if (te != null && te instanceof TileAnomaly) {
			SubTileEntity sub = SubTileEntity.forName(type);
			sub.worldGen = true;
			
			((TileAnomaly) te).addSubTile(sub, type);
			
			for (int i = 0; i < AlfheimConfig.anomaliesUpdate; i++) te.updateEntity();
			
			sub.worldGen = false;
		}
	}
}