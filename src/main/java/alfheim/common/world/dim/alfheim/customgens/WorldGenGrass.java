package alfheim.common.world.dim.alfheim.customgens;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import vazkii.botania.common.block.*;
import vazkii.botania.common.block.subtile.generating.SubTileDaybloom;
import vazkii.botania.common.block.tile.TileSpecialFlower;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lib.LibBlockNames;

import java.util.Random;

public class WorldGenGrass implements IWorldGenerator {
	
	public final boolean grass, flowers, doubleFlowers, botanicalFlowers;
	public final double mod;
	
	public WorldGenGrass(boolean g, boolean f, boolean df, boolean bf, double m) {
		grass = g;
		flowers = f;
		doubleFlowers = df;
		botanicalFlowers = bf;
		mod = m;
	}
	
	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		
		int cx = chunkX * 16, cz = chunkZ * 16;
		if (botanicalFlowers) {
			int dist = Math.min(8, Math.max(1, ConfigHandler.flowerPatchSize));
			for (int i = 0; i < ConfigHandler.flowerQuantity; i++)
				if (rand.nextInt((int) Math.round(ConfigHandler.flowerPatchChance / mod)) == 0) {
					int x = cx + rand.nextInt(16), z = cz + rand.nextInt(16), y = world.getTopSolidOrLiquidBlock(x, z), color = rand.nextInt(16);
					boolean primus = rand.nextInt(380) == 0;
					for (int j = 0; j < ConfigHandler.flowerDensity * ConfigHandler.flowerPatchChance; j++) {
						int x1 = x + rand.nextInt(dist * 2) - dist, z1 = z + rand.nextInt(dist * 2) - dist;
						if (world.isAirBlock(x1, y, z1) && world.getBlock(x1, y - 1, z1) == Blocks.grass)
							if (primus) {
								world.setBlock(x1, y, z1, ModBlocks.specialFlower, 0, 2);
								TileSpecialFlower flower = (TileSpecialFlower) world.getTileEntity(x1, y, z1);
								flower.setSubTile(rand.nextBoolean() ? LibBlockNames.SUBTILE_NIGHTSHADE_PRIME : LibBlockNames.SUBTILE_DAYBLOOM_PRIME);
								SubTileDaybloom subtile = (SubTileDaybloom) flower.getSubTile();
								subtile.setPrimusPosition();
							} else {
								world.setBlock(x1, y, z1, ModBlocks.flower, color, 2);
								if (rand.nextDouble() < ConfigHandler.flowerTallChance && ((BlockModFlower) ModBlocks.flower).func_149851_a(world, x1, y, z1, false)) BlockModFlower.placeDoubleFlower(world, x1, y, z1, color, 0);
							}
					}
				}
		}
		
		for(int i = 0; i < ConfigHandler.mushroomQuantity; i++) {
			int x = cx + rand.nextInt(16), z = cz + rand.nextInt(16), y = rand.nextInt(28) + 4, color = rand.nextInt(16);
			if(world.isAirBlock(x, y, z) && ModBlocks.mushroom.canBlockStay(world, x, y, z))
				world.setBlock(x, y, z, ModBlocks.mushroom, color, 2);
		}
		
		
		int perChunk = (int) Math.round(64 * mod), iteration = 256;
		
		Block[] types = {	Blocks.yellow_flower, Blocks.yellow_flower,																	// 0 1
							Blocks.red_flower, Blocks.red_flower, Blocks.red_flower,													// 2 3 4
							Blocks.tallgrass, Blocks.tallgrass, Blocks.tallgrass, Blocks.tallgrass, Blocks.tallgrass, Blocks.tallgrass, // 5 6 7 8 9 10
							Blocks.double_plant, Blocks.double_plant																	// 11 12
		};
		
		byte[] metas = {	0, 0,
							0, 0, -1,
							0, 2, 1, 1, 1, 1,
							2, -1};
		
		while(perChunk > 0 && iteration > 0) {
			--iteration;
			int x = cx + rand.nextInt(16), z = cz + rand.nextInt(16), y = world.getTopSolidOrLiquidBlock(x, z);
			if(!world.isAirBlock(x, y, z) || world.getBlock(x, y - 1, z) != Blocks.grass) continue;
			
			int type = rand.nextInt(20);
			select: {
				if (type > 12) continue;
				if (type > 10 && !doubleFlowers) break select;
				if (type < 5 && !flowers) break select;
				if (4 < type && type < 11 && !grass) break select;
				
				else if (type == 4) metas[4] = (byte) (rand.nextInt(8) + 1);
				else if (type == 11) world.setBlock(x, y + 1, z, types[11], metas[11] + 8, 2);
				else if (type == 12) {
					metas[12] = (byte) (rand.nextInt(6));
					if (metas[12] == 2) continue;
					world.setBlock(x, y + 1, z, types[12], metas[12] + 8, 2);
				}

				world.setBlock(x, y, z, types[type], metas[type], 2);
			}
			--perChunk;
		}
	}
}