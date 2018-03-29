package alfheim.common.world.dim.alfheim.gen;

import java.util.Random;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.NoiseGeneratorPerlin;
import vazkii.botania.common.block.ModBlocks;

public class IslandsGen implements IWorldGenerator {

	private static Block top = Blocks.grass, mid = Blocks.dirt, bot = ModBlocks.livingrock;
	
	public static void setIslandFillers(Block t, Block m, Block b) {
		top = t;
		mid = m;
		bot = b;
	}
	
	@Override
	public void generate(Random rnd, int chunkX, int chunkZ, World world, IChunkProvider iChunkGenerator, IChunkProvider iChunkProvider) {
		genIslands(rnd, chunkZ, chunkZ, world);
	}
	
	/**
	 * @author Defernus
	 * */
	public static void genIslands(Random rnd, int chunkX, int chunkZ, World world) {
		int cchunkX = 0;
		int cchunkZ = 0;

		boolean cg = false;
		for (int k = 0; k < 49; k++) {
			int i = k % 7 - 3;
			int j = k / 7 - 3;
			if (canGenerateHere(world, chunkX + i, chunkZ + j)) {
				cchunkX = chunkX + i;
				cchunkZ = chunkZ + j;

				cg = true;
				break;
			}
		}

		if (!cg) return;

		int x = cchunkX * 16 + 8;
		int z = cchunkZ * 16 + 8;
		int y = 150;

		rnd = new Random((long) x * 341873128712L + (long) z * 132897987541L + world.getWorldInfo().getSeed() + (long) 27644437);

		int r = 16 + rnd.nextInt(17);
		NoiseGeneratorPerlin perlin = new NoiseGeneratorPerlin(rnd, 2);

		int iMin = (chunkX - cchunkX) * 16 - 8;
		int iMax = (chunkX - cchunkX + 1) * 16 - 8;
		int jMin = (chunkZ - cchunkZ) * 16 - 8;
		int jMax = (chunkZ - cchunkZ + 1) * 16 - 8;

		for (int i = iMin; i <= iMax; i++) {
			for (int j = jMin; j <= jMax; j++) {
				double l = Math.sqrt(i * i + j * j);

				int hT = (int) (perlin.func_151601_a((x + i) / 40., (z + j) / 40.) * 2 - (i * i + j * j) / (10. * r));
				int hB = (int) (perlin.func_151601_a((x + i) / 50., (z + j) / 50.) * ((r - l) / 5. + 7) + (perlin.func_151601_a((x + i), (z + j)) - 1) * (((r - l) / (double) r) * 10 > 0 ? ((r - l) / r) * 10 : 0));

				hB += (int) l - r;

				for (int k = hB; k <= hT; k++) {
					Block block;

					if (k == hT) {
						block = top;
					} else if (k > hT - 4) {
						block = mid;
					} else {
						block = bot;
					}
					world.setBlock(x + i, y + k, z + j, block);
				}
			}
		}
	}

	private static boolean canGenerateHere(World world, int chunkX, int chunkZ) {
		int i = chunkX;
		int j = chunkZ;

		if (chunkX < 0) i = chunkX - 9;
		if (chunkZ < 0) j = chunkZ - 9;

		int d = 8;
		i /= d;
		j /= d;
		i *= d;
		j *= d;

		Random random = new Random((long) i * 341873128712L + (long) j * 132897987541L + world.getWorldInfo().getSeed() + (long) 27644437);
		i += random.nextInt(5);
		j += random.nextInt(5);

		return (i == chunkX && j == chunkZ);
	}
}