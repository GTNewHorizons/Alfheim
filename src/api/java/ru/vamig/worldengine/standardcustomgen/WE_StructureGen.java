package ru.vamig.worldengine.standardcustomgen;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.*;

public class WE_StructureGen implements IWorldGenerator {
	
	public final List<StructureEntry> sttngs = new ArrayList<StructureEntry>();
	
	public void add(StructureBaseClass str, int rarity) {
		sttngs.add(new StructureEntry(str, rarity));
	}
	
	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		for (StructureEntry a : sttngs) {
			if (rand.nextInt(a.rarity) != 0) continue;
			int x = chunkX * 16 + rand.nextInt(16);
			int z = chunkZ * 16 + rand.nextInt(16);
			a.str.generate(world, rand, x, world.getTopSolidOrLiquidBlock(x, z), z);
		}
	}
	
	public static class StructureEntry {
		
		public final StructureBaseClass str;
		public final int rarity;
		
		StructureEntry(StructureBaseClass str, int rarity) {
			this.str = str;
			this.rarity = rarity;
		}
	}
}