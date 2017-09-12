package alfheim.common.world.dim.gen;

import java.util.Random;

import alexsocol.asjlib.ASJUtilities;
import alfheim.common.registry.AlfheimBlocks;
import alfheim.common.utils.AlfheimConfig;
import alfheim.common.world.dim.gen.structure.ArenaStructure;
import alfheim.common.world.dim.gen.structure.DreamsTreeStructure;
import alfheim.common.world.dim.gen.structure.SpawnpointStructure;
import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import vazkii.botania.common.block.ModBlocks;

public class WorldGenAlfheim implements IWorldGenerator {

	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		if (world.provider.dimensionId != AlfheimConfig.dimensionIDAlfheim) return;
		generateElvenOres(world, rand, chunkX*16, chunkZ*16);
		if ((chunkX == 0 && chunkZ == 0)) (new SpawnpointStructure()).generate(world, rand, -11, 64, -41);
		if ((-3 < chunkX && chunkX < 3) && (-3 < chunkZ && chunkZ < 4)) return;
		if (rand.nextInt(5) == 0) {
			int x = chunkX*16 + rand.nextInt(16);
			int z = chunkZ*16 + rand.nextInt(16);
			(new DreamsTreeStructure()).generate(world, rand, x, world.getTopSolidOrLiquidBlock(x, z), z, Blocks.log, Blocks.leaves, 0, 4, 8, 0);
		}
		if (rand.nextInt(10) == 0) {
			int x = chunkX*16 + rand.nextInt(16);
			int z = chunkZ*16 + rand.nextInt(16);
			(new DreamsTreeStructure()).generate(world, rand, x, world.getTopSolidOrLiquidBlock(x, z), z, AlfheimBlocks.dreamLog, AlfheimBlocks.dreamLeaves, 0, 4, 8, 0);
		}
		if ((-8 < chunkX && chunkX < -8) && (-8 < chunkZ && chunkZ < 8)) return;
		if (rand.nextInt(1000) == 0) {
			int x = chunkX*16 + rand.nextInt(16);
			int z = chunkZ*16 + rand.nextInt(16);
			(new ArenaStructure()).generate(world, rand, x, world.getTopSolidOrLiquidBlock(x, z), z);
		}
	}

	public void generateElvenOres(World world, Random rand, int blockXPos, int blockZPos) {
		ASJUtilities.addOreSpawn(AlfheimBlocks.elvenOres, ModBlocks.livingrock, 0, world, rand, blockXPos, blockZPos, 16, 16, 1, 8, 1, 2, 75,  1, 16);		// Dragonstone
		ASJUtilities.addOreSpawn(AlfheimBlocks.elvenOres, ModBlocks.livingrock, 1, world, rand, blockXPos, blockZPos, 16, 16, 1, 8, 3, 6, 100, 1, 64);		// Elementium
		ASJUtilities.addOreSpawn(AlfheimBlocks.elvenOres, ModBlocks.livingrock, 2, world, rand, blockXPos, blockZPos, 16, 16, 4, 8, 1, 1, 100, 1, 48);		// Quartz
		ASJUtilities.addOreSpawn(AlfheimBlocks.elvenOres, ModBlocks.livingrock, 3, world, rand, blockXPos, blockZPos, 16, 16, 1, 8, 2, 3, 100, 1, 32);		// Gold
		ASJUtilities.addOreSpawn(AlfheimBlocks.elvenOres, ModBlocks.livingrock, 4, world, rand, blockXPos, blockZPos, 16, 16, 1, 4, 1, 1, 50,  1, 16);		// Iffesal
	}
}
