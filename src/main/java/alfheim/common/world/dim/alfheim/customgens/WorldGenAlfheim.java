package alfheim.common.world.dim.alfheim.customgens;

import java.util.Random;

import alfheim.common.core.util.AlfheimConfig;
import alfheim.common.world.dim.alfheim.struct.StructureSpawnpoint;
import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldGenAlfheim implements IWorldGenerator {

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
	}
}