package alfheim.common.world.dim.alfheim.gen;

import java.util.Random;

import alexsocol.asjlib.ASJUtilities;
import alfheim.common.core.registry.AlfheimBlocks;
import alfheim.common.core.utils.AlfheimConfig;
import alfheim.common.world.dim.alfheim.gen.structure.StructureArena;
import alfheim.common.world.dim.alfheim.gen.structure.StructureDreamsTree;
import alfheim.common.world.dim.alfheim.gen.structure.StructureSpawnpoint;
import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import vazkii.botania.common.block.BlockModFlower;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.subtile.generating.SubTileDaybloom;
import vazkii.botania.common.block.tile.TileSpecialFlower;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lib.LibBlockNames;

public class WorldGenAlfheim implements IWorldGenerator {

	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		if (world.provider.dimensionId == AlfheimConfig.dimensionIDAlfheim) generateAlfheim(rand, chunkX, chunkZ, world);
	}

	private static void generateAlfheim(final Random rand, int chunkX, int chunkZ, final World world) {
		if (chunkX == 0 && chunkZ == 0 && !world.isRemote) {
			Thread generate = new Thread(new Runnable() {
				@Override
				public void run() {
					ASJUtilities.log("Generating spawn...");
					StructureSpawnpoint.generate(world, rand, -11, world.getHeightValue(0, -41), -41);
				}
			}, "Alf Spawn Gen");
			generate.start();
		}
		for (int i = 0; i < 3 + rand.nextInt(2); i++) generateElvenOres(world, rand, chunkX * 16, chunkZ * 16);
		generateFlowers(world, rand, chunkX * 16, chunkZ * 16);
		generateTrees(world, rand, chunkX, chunkZ);
		generateStructures(world, rand, chunkX, chunkZ);
		//IslandsGen.genIslands(rand, chunkX, chunkZ, world);
	}

	public static void generateElvenOres(World world, Random rand, int blockXPos, int blockZPos) {
		ASJUtilities.addOreSpawn(AlfheimBlocks.elvenOres, ModBlocks.livingrock, 0, world, rand, blockXPos, blockZPos, 1, 8, 1, 2, 75,  1, 16);		// Dragonstone
		ASJUtilities.addOreSpawn(AlfheimBlocks.elvenOres, ModBlocks.livingrock, 1, world, rand, blockXPos, blockZPos, 1, 8, 3, 6, 100, 1, 64);		// Elementium
		ASJUtilities.addOreSpawn(AlfheimBlocks.elvenOres, ModBlocks.livingrock, 2, world, rand, blockXPos, blockZPos, 4, 8, 1, 1, 100, 1, 48);		// Quartz
		ASJUtilities.addOreSpawn(AlfheimBlocks.elvenOres, ModBlocks.livingrock, 3, world, rand, blockXPos, blockZPos, 1, 8, 2, 3, 100, 1, 32);		// Gold
		ASJUtilities.addOreSpawn(AlfheimBlocks.elvenOres, ModBlocks.livingrock, 4, world, rand, blockXPos, blockZPos, 1, 4, 1, 1, 50,  1, 16);		// Iffesal
	}
	
	public static void generateFlowers(World world, Random rand, int chunkX, int chunkZ) {
		{ // Botania
			// Flowers
			int dist = Math.min(8, Math.max(1, ConfigHandler.flowerPatchSize));
			for(int i = 0; i < ConfigHandler.flowerQuantity; i++) {
				if(rand.nextInt(ConfigHandler.flowerPatchChance) == 0) {
					int x = chunkX + rand.nextInt(16);
					int z = chunkZ + rand.nextInt(16);
					int y = world.getTopSolidOrLiquidBlock(x, z);
	
					int color = rand.nextInt(16);
					boolean primus = rand.nextInt(380) == 0;
	
					for(int j = 0; j < ConfigHandler.flowerDensity * ConfigHandler.flowerPatchChance; j++) {
						int x1 = x + rand.nextInt(dist * 2) - dist;
						int y1 = y;
						int z1 = z + rand.nextInt(dist * 2) - dist;
	
						if(world.isAirBlock(x1, y1, z1) && world.getBlock(x1, y1 - 1, z1) == Blocks.grass) {
							if(primus) {
								world.setBlock(x1, y1, z1, ModBlocks.specialFlower, 0, 2);
								TileSpecialFlower flower = (TileSpecialFlower) world.getTileEntity(x1, y1, z1);
								flower.setSubTile(rand.nextBoolean() ? LibBlockNames.SUBTILE_NIGHTSHADE_PRIME : LibBlockNames.SUBTILE_DAYBLOOM_PRIME);
								SubTileDaybloom subtile = (SubTileDaybloom) flower.getSubTile();
								subtile.setPrimusPosition();
							} else {
								world.setBlock(x1, y1, z1, ModBlocks.flower, color, 2);
								if(rand.nextDouble() < ConfigHandler.flowerTallChance && ((BlockModFlower) ModBlocks.flower).func_149851_a(world, x1, y1, z1, false))
									BlockModFlower.placeDoubleFlower(world, x1, y1, z1, color, 0);
							}
						}
					}
				}
			}
			
			// Mushrooms
			for(int i = 0; i < ConfigHandler.mushroomQuantity; i++) {
				int x = chunkX + rand.nextInt(16);
				int z = chunkZ + rand.nextInt(16);
				int y = rand.nextInt(28) + 4;
	
				int color = rand.nextInt(16);
				if(world.isAirBlock(x, y, z) && ModBlocks.mushroom.canBlockStay(world, x, y, z))
					world.setBlock(x, y, z, ModBlocks.mushroom, color, 2);
			}
		}
		
		{ // Vanilla
			int perChunk = 64;
			final int maxIterations = 256;
			int iteration = maxIterations;
			
			Block[] types = {	Blocks.yellow_flower, Blocks.yellow_flower, Blocks.red_flower, Blocks.red_flower, Blocks.red_flower,
								Blocks.tallgrass, Blocks.tallgrass, Blocks.tallgrass, Blocks.tallgrass, Blocks.tallgrass, Blocks.tallgrass, Blocks.double_plant, Blocks.double_plant };

			byte[] metas = {	0, 0, 0, 0, -1,
								0, 2, 1, 1, 1, 1, 2, -1 };
			
			while (perChunk > 0 && iteration > 0) {
				--iteration;
				int x = chunkX + rand.nextInt(16);
				int z = chunkZ + rand.nextInt(16);
				int y = world.getTopSolidOrLiquidBlock(x, z);
				if(!world.isAirBlock(x, y, z) || world.getBlock(x, y - 1, z) != Blocks.grass) continue;
				
				int type = rand.nextInt(20);
				if (type > 12) continue;
				
				if (type == 4) metas[4] = (byte) (rand.nextInt(8) + 1); // Single Flower
				if (type == 11) world.setBlock(x, y + 1, z, types[11], metas[11] + 8, 2); // Double grass
				if (type == 12) {
					metas[12] = (byte) (rand.nextInt(6));
					if (metas[12] == 2) continue;
					world.setBlock(x, y + 1, z, types[12], metas[12] + 8, 2); // Double flower
				}
					
				world.setBlock(x, y, z, types[type], metas[type], 2);
				--perChunk;
			}
		}
	}
	
	public static void generateTrees(World world, Random rand, int chunkX, int chunkZ) {
		if ((-3 < chunkX && chunkX < 3) && (-3 < chunkZ && chunkZ < 4)) return;
		if (rand.nextInt(5) == 0) {
			int x = chunkX * 16 + rand.nextInt(16);
			int z = chunkZ * 16 + rand.nextInt(16);
			StructureDreamsTree.generate(world, rand, x, world.getTopSolidOrLiquidBlock(x, z), z, Blocks.log, Blocks.leaves, 0, 4, 8, 0);
		}
		if (rand.nextInt(10) == 0) {
			int x = chunkX * 16 + rand.nextInt(16);
			int z = chunkZ * 16 + rand.nextInt(16);
			StructureDreamsTree.generate(world, rand, x, world.getTopSolidOrLiquidBlock(x, z), z, AlfheimBlocks.dreamlog, AlfheimBlocks.dreamLeaves, 0, 4, 8, 0);
		}
	}
	
	public static void generateStructures(World world, Random rand, int chunkX, int chunkZ) {
		if ((-8 < chunkX && chunkX < -8) && (-8 < chunkZ && chunkZ < 8)) return;
		if (rand.nextInt(1000) == 0) {
			int x = chunkX * 16 + rand.nextInt(16);
			int z = chunkZ * 16 + rand.nextInt(16);
			StructureArena.generate(world, rand, x, world.getTopSolidOrLiquidBlock(x, z), z);
		}
	}
}
