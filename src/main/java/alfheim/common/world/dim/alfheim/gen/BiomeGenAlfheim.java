package alfheim.common.world.dim.alfheim.gen;

import java.util.Random;

import alfheim.common.core.registry.AlfheimBlocks;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import vazkii.botania.common.block.ModBlocks;

public class BiomeGenAlfheim extends BiomeGenBase {

	public Block stoneBlock;
	public byte topMeta;
	public byte fillerMeta;
	public byte stoneMeta;
	
	public BiomeGenAlfheim(int par1) {
		super(par1);
		this.setBiomeName("Alfheim");
		this.topBlock = Blocks.grass;
		this.fillerBlock = Blocks.dirt;
		this.spawnableCreatureList.clear();
		this.spawnableMonsterList.clear();
		this.spawnableCaveCreatureList.clear();
		this.spawnableWaterCreatureList.clear();
		this.theBiomeDecorator.treesPerChunk = 6;
		this.waterColorMultiplier = 0x00C8FF;
		this.setColor(0x00FF00);
	}

	@Override
	@SideOnly(Side.CLIENT)
    public int getBiomeFoliageColor(int R, int G, int B) {
        return 0x08F500;
    }

	@Override
    @SideOnly(Side.CLIENT)
    public int getBiomeGrassColor(int R, int G, int B) {
        return 0x08F500;
    }
	
	@Override
	public WorldGenAbstractTree func_150567_a(Random rand) {
		return this.worldGeneratorSwamp;
	}
	
	@Override
	public void genTerrainBlocks(World world, Random rand, Block[] blocks, byte[] metas, int x, int z, double stoneNoise) {
		this.genABiomeTerrain(world, rand, blocks, metas, x, z, stoneNoise);
	}

	public void genABiomeTerrain(World world, Random rand, Block[] blocks, byte[] metas, int x, int z, double stoneNoise) {
		Block topBlock = this.topBlock;
		byte topMeta = this.topMeta;
		Block fillerBlock = this.fillerBlock;
		byte fillerMeta = this.fillerMeta;
		int currentFillerDepth = -1;
		int maxFillerDepth = (int)(stoneNoise / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
		int maskX = x & 15;
		int maskZ = z & 15;
		int worldHeight = blocks.length / 256;
		int height = 63;
		int seaLevel = 63;
	
		for (int y = 255; y >= 0; y--) {
			int index = (maskZ * 16 + maskX) * worldHeight + y;

			if (y == 0) {
				blocks[index] = Blocks.bedrock;
			} else {
				Block currentBlock = blocks[index];

				if (currentBlock != null && currentBlock.getMaterial() != Material.air) {
					if (currentBlock == Blocks.stone) {
						if (this.stoneBlock != null) {
							blocks[index] = this.stoneBlock;
							metas[index] = this.stoneMeta;
						}
						if (currentFillerDepth == -1) {
							if (maxFillerDepth <= 0) {
								topBlock = null;
								topMeta = 0;
								fillerBlock = Blocks.dirt;
								fillerMeta = 0;
							} else if (y >= seaLevel - 5 && y <= seaLevel) {
								topBlock = this.topBlock;
								topMeta = this.topMeta;
								fillerBlock = this.fillerBlock;
								fillerMeta = 0;
							}
							if (y < seaLevel - 1 && (topBlock == null || topBlock.getMaterial() == Material.air)) {
								if (this.getFloatTemperature(x, y, z) < 0.15F) {
									topBlock = Blocks.ice;
									topMeta = 0;
								} else {
									topBlock = Blocks.water;
									topMeta = 0;
								}
							}

							currentFillerDepth = maxFillerDepth;

							if (y >= seaLevel - 2) {
								blocks[index] = topBlock;
								metas[index] = topMeta;
							} else if (y < seaLevel - 8 - maxFillerDepth) {
								topBlock = null;
								fillerBlock = Blocks.dirt;
								fillerMeta = 0;
								blocks[index] = Blocks.gravel;
							} else {
								blocks[index] = fillerBlock;
								metas[index] = fillerMeta;
							}
						} else if (currentFillerDepth > 0) {
							currentFillerDepth--;
							blocks[index] = fillerBlock;
							metas[index] = fillerMeta;

							if (currentFillerDepth == 0 && fillerBlock == AlfheimBlocks.elvenSand) {
								currentFillerDepth = rand.nextInt(4) + Math.max(0, y - (seaLevel - 1));
								fillerBlock = ModBlocks.livingrock;
								fillerMeta = 0;
							}
						}
					}
				}
			}
		}
	}
	
	public String func_150572_a(Random p_150572_1_, int p_150572_2_, int p_150572_3_, int p_150572_4_) {
		double d0 = plantNoise.func_151601_a((double) p_150572_2_ / 200.0D, (double) p_150572_4_ / 200.0D);
		int l;

		if (d0 < -0.8D) {
			l = p_150572_1_.nextInt(4);
			return BlockFlower.field_149859_a[4 + l];
		} else if (p_150572_1_.nextInt(3) > 0) {
			l = p_150572_1_.nextInt(3);
			return l == 0 ? BlockFlower.field_149859_a[0]
					: (l == 1 ? BlockFlower.field_149859_a[3] : BlockFlower.field_149859_a[8]);
		} else {
			return BlockFlower.field_149858_b[0];
		}
	}
}