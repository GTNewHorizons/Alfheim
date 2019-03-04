package alfheim.common.world.dim.alfheim.customgens;

import java.util.Random;

import alexsocol.asjlib.ASJUtilities;
import alfheim.common.integration.thaumcraft.ThaumcraftAlfheimModule;
import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.config.Config;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.lib.world.biomes.BiomeHandler;
import vazkii.botania.common.block.ModBlocks;

public class WorldGenAlfheimThaumOre implements IWorldGenerator {
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		generateOres(world, random, chunkX, chunkZ);
	}
	
	private void generateOres(World world, Random random, int chunkX, int chunkZ) {
		int i;
		int randPosX;
		int randPosZ;
		int randPosY;
		Block md;
		if (Config.genCinnibar) {
			for (i = 0; i < 18; ++i) {
				randPosX = chunkX * 16 + random.nextInt(16);
				randPosZ = random.nextInt(world.getHeight() / 5);
				randPosY = chunkZ * 16 + random.nextInt(16);
				md = world.getBlock(randPosX, randPosZ, randPosY);
				if (md != null && md.isReplaceableOreGen(world, randPosX, randPosZ, randPosY, ModBlocks.livingrock)) {
					world.setBlock(randPosX, randPosZ, randPosY, ThaumcraftAlfheimModule.alfheimThaumOre, 0, 0);
				}
			}
		}
		
		if (Config.genAmber) {
			for (i = 0; i < 20; ++i) {
				randPosX = chunkX * 16 + random.nextInt(16);
				randPosZ = chunkZ * 16 + random.nextInt(16);
				randPosY = world.getHeightValue(randPosX, randPosZ) - random.nextInt(25);
				md = world.getBlock(randPosX, randPosY, randPosZ);
				if (md != null && md.isReplaceableOreGen(world, randPosX, randPosY, randPosZ, ModBlocks.livingrock)) {
					world.setBlock(randPosX, randPosY, randPosZ, ThaumcraftAlfheimModule.alfheimThaumOre, 7, 2);
				}
			}
		}
		
		if (Config.genInfusedStone) {
			for (i = 0; i < 8; ++i) {
				randPosX = chunkX * 16 + random.nextInt(16);
				randPosZ = chunkZ * 16 + random.nextInt(16);
				randPosY = random.nextInt(Math.max(5, world.getHeightValue(randPosX, randPosZ) - 5));
				int meta = random.nextInt(6) + 1;
				if (random.nextInt(3) == 0) {
					Aspect e = BiomeHandler.getRandomBiomeTag(world.getBiomeGenForCoords(randPosX, randPosZ).biomeID,
							random);
					if (e == null) {
						meta = 1 + random.nextInt(6);
					} else if (e == Aspect.AIR) {
						meta = 1;
					} else if (e == Aspect.FIRE) {
						meta = 2;
					} else if (e == Aspect.WATER) {
						meta = 3;
					} else if (e == Aspect.EARTH) {
						meta = 4;
					} else if (e == Aspect.ORDER) {
						meta = 5;
					} else if (e == Aspect.ENTROPY) {
						meta = 6;
					}
				}
				
				try {
					(new WorldGenMinable(ThaumcraftAlfheimModule.alfheimThaumOre, meta, 6, ModBlocks.livingrock)).generate(world, random, randPosX, randPosY, randPosZ);
				} catch (Exception e) {
					ASJUtilities.error("Something went wrong while generating Thaumcraft ores in Alfheim:");
					e.printStackTrace();
				}
			}
		}
	}
}
