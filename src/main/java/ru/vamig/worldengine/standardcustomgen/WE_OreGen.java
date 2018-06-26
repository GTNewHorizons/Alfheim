package ru.vamig.worldengine.standardcustomgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import alexsocol.asjlib.ASJUtilities;
import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.minecraft.world.gen.feature.WorldGenMinable;

public class WE_OreGen implements IWorldGenerator {

	public List<WorldGenMinableParametrized> oreGen = new ArrayList<WorldGenMinableParametrized>();
	
	public void add(Block ore, Block replace, int meta, int minVeinSize, int maxVeinSize, int minVeinsPerChunk, int maxVeinsPerChunk, int chanceToSpawn, int minY, int maxY) {
		oreGen.add(new WorldGenMinableParametrized(ore, replace, meta, minVeinSize, maxVeinSize, minVeinsPerChunk, maxVeinsPerChunk, chanceToSpawn, minY, maxY));
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		for (WorldGenMinableParametrized wgmp : oreGen) {
			if (random.nextInt(101) < (100 - wgmp.chanceToSpawn)) return;
			int veins = ASJUtilities.randInBounds(random, wgmp.minVeinsPerChunk, wgmp.maxVeinsPerChunk);
			for (int i = 0; i < veins; i++) {
				int posX = chunkX * 16 + random.nextInt(16) + 8;
				int posY = ASJUtilities.randInBounds(random, wgmp.minY, wgmp.maxY);
				int posZ = chunkZ * 16 + random.nextInt(16) + 8;
				(new WorldGenMinable(wgmp.ore, wgmp.meta, ASJUtilities.randInBounds(random, wgmp.minVeinSize, wgmp.maxVeinSize), wgmp.replace)).generate(world, random, posX, posY, posZ);
			}
		}
	}
	
	public static class WorldGenMinableParametrized {
		
		public final Block ore;
		public final Block replace;
		public final int meta;
		public final int minVeinSize;
		public final int maxVeinSize;
		public final int minVeinsPerChunk;
		public final int maxVeinsPerChunk;
		public final int chanceToSpawn;
		public final int minY;
		public final int maxY;
		
		public WorldGenMinableParametrized(Block ore, Block replace, int meta, int minVeinSize, int maxVeinSize, int minVeinsPerChunk, int maxVeinsPerChunk, int chanceToSpawn, int minY, int maxY) {
			this.ore = ore;
			this.meta = meta;
			this.replace = replace;
			this.minVeinSize = minVeinSize;
			this.maxVeinSize = maxVeinSize;
			this.minVeinsPerChunk = minVeinsPerChunk;
			this.maxVeinsPerChunk = maxVeinsPerChunk;
			this.chanceToSpawn = chanceToSpawn;
			this.minY = minY;
			this.maxY = maxY;
		}
	}
}
