package alfheim.common.dimension.world.gen;

import java.util.Random;

import vazkii.botania.common.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;

public class BiomeGenAlfheim extends BiomeGenBase {

	public Block stoneBlock;
	public byte topMeta;
	public byte fillerMeta;
	public byte stoneMeta;
	
	public BiomeGenAlfheim(int par1) {
		super(par1);
		this.setBiomeName("Alfheim");
		this.topBlock = ModBlocks.altGrass;
		this.fillerBlock = Blocks.dirt;
		this.spawnableCreatureList.clear();
		this.spawnableMonsterList.clear();
		this.spawnableCaveCreatureList.clear();
		this.spawnableWaterCreatureList.clear();
		this.theBiomeDecorator.treesPerChunk = 6;
		this.waterColorMultiplier = 11519;
	}

	@Override
	public WorldGenAbstractTree func_150567_a(Random rand) {
		return this.worldGeneratorSwamp;
	}

	@Override
	public void genTerrainBlocks(World p_150573_1_, Random p_150573_2_,
			Block[] p_150573_3_, byte[] p_150573_4_, int p_150573_5_,
			int p_150573_6_, double p_150573_7_) {
		this.genABiomeTerrain(p_150573_1_, p_150573_2_, p_150573_3_,
				p_150573_4_, p_150573_5_, p_150573_6_, p_150573_7_);
	}

	public void genABiomeTerrain(World world, Random rand, Block[] block, byte[] meta, int x, int z, double stoneNoise)
	{
		Block topBlock = this.topBlock;
		byte topMeta = this.topMeta;
		Block fillerBlock = this.fillerBlock;
		byte fillerMeta = this.fillerMeta;
		int currentFillerDepth = -1;
		int maxFillerDepth = (int)(stoneNoise / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
		int maskX = x & 15;
		int maskZ = z & 15;
		int worldHeight = block.length / 256;
		int height = 63;
		int seaLevel = 63;
	
		for (int y = 255; y >= 0; y--)
		{
			int index = (maskZ * 16 + maskX) * worldHeight + y;
	
			if (y <= 0 + rand.nextInt(5))
			{
				block[index] = Blocks.bedrock;
			}
			else
			{
				Block currentBlock = block[index];
	
				if (currentBlock != null && currentBlock.getMaterial() != Material.air)
				{
					if (currentBlock == Blocks.stone)
					{
						if (this.stoneBlock != null)
						{
							block[index] = this.stoneBlock;
							meta[index] = this.stoneMeta;
						}
						if (currentFillerDepth == -1)
						{
							if (maxFillerDepth <= 0)
							{
								topBlock = null;
								topMeta = 0;
								fillerBlock = Blocks.dirt;
								fillerMeta = 0;
							}
							else if (y >= seaLevel - 5 && y <= seaLevel)
							{
								topBlock = this.topBlock;
								topMeta = this.topMeta;
								fillerBlock = this.fillerBlock;
								fillerMeta = 0;
							}
							if (y < seaLevel - 1 && (topBlock == null || topBlock.getMaterial() == Material.air))
							{
								if (this.getFloatTemperature(x, y, z) < 0.15F)
								{
									topBlock = Blocks.ice;
									topMeta = 0;
								}
								else
								{
									topBlock = Blocks.water;
									topMeta = 0;
								}
							}
	
							currentFillerDepth = maxFillerDepth;
	
							if (y >= seaLevel - 2)
							{
								block[index] = topBlock;
								meta[index] = topMeta;
							}
							else if (y < seaLevel - 8 - maxFillerDepth)
							{
								topBlock = null;
								fillerBlock = Blocks.dirt;
								fillerMeta = 0;
								block[index] = Blocks.gravel;
							}
							else
							{
								block[index] = fillerBlock;
								meta[index] = fillerMeta;
							}
						}
						else if (currentFillerDepth > 0)
						{
							currentFillerDepth--;
							block[index] = fillerBlock;
							meta[index] = fillerMeta;
	
							if (currentFillerDepth == 0 && fillerBlock == Blocks.sand)
							{
								currentFillerDepth = rand.nextInt(4) + Math.max(0, y - (seaLevel - 1));
								fillerBlock = Blocks.sandstone;
								fillerMeta = 0;
							}
						}
					}
				}
			}
		}
	}
}