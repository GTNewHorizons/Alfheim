package alfheim.common.world.dim.alfheim.gen.layers;

import alfheim.common.world.dim.DimensionUtil;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.layer.GenLayer;
import net.minecraft.world.gen.layer.IntCache;

public class GenLayersAlfheimBiomes extends GenLayer {
	protected BiomeGenBase[] commonBiomes = { DimensionUtil.alfheimBiome };
	protected BiomeGenBase[] rareBiomes = { DimensionUtil.alfheimBeachBiome };

	public GenLayersAlfheimBiomes(long seed, GenLayer genlayer) {
		super(seed);
		this.parent = genlayer;
	}

	public GenLayersAlfheimBiomes(long seed) {
		super(seed);
	}

	@Override
	public int[] getInts(int x, int z, int width, int depth) {
		int[] dest = IntCache.getIntCache(width * depth);

		for (int dz = 0; dz < depth; dz++) {
			for (int dx = 0; dx < width; dx++) {
				this.initChunkSeed(dx + x, dz + z);

				if (this.nextInt(15) == 0) {
					dest[dx + dz * width] = this.rareBiomes[this.nextInt(this.rareBiomes.length)].biomeID;
				} else {
					dest[dx + dz * width] = this.commonBiomes[this.nextInt(this.commonBiomes.length)].biomeID;
				}
			}
		}
		return dest;
	}
}
