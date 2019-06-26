package ru.vamig.worldengine.standardcustomgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import alfheim.common.world.dim.alfheim.struct.StructureBaseClass;
import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

public class WE_StructureGen implements IWorldGenerator {
	
	public final List<StrList> sttngs = new ArrayList<StrList>();
	
	public void add(StructureBaseClass str, int rarity) {
		sttngs.add(new StrList(str, rarity));
	}
	
	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		for(StrList a : sttngs) {
			if (rand.nextInt(a.rarity) != 0) continue;
			int x = chunkX * 16 + rand.nextInt(16);
			int z = chunkZ * 16 + rand.nextInt(16);
			a.str.generate(world, rand, x, world.getTopSolidOrLiquidBlock(x, z), z);
		}
	}
	
	public class StrList {
		public final StructureBaseClass str;
		public final int rarity;
		
		StrList(StructureBaseClass str, int rarity) {
			this.str = str;
			this.rarity = rarity;
		}
	}
}