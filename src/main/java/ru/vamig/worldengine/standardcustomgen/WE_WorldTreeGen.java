//- By Vamig Aliev.
//- https://vk.com/win_vista.

package ru.vamig.worldengine.standardcustomgen;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import ru.vamig.worldengine.standardcustomgen.help.*;

import java.util.*;

public class WE_WorldTreeGen implements IWorldGenerator {
	
	public final List<WE_TreeGen> smallTrees = new ArrayList();
	public final List<WE_BigTreeGen> bigTrees = new ArrayList();
	//-//
	public final List<Integer> dispersions = new ArrayList();
	public final List<Integer> treesPerChunkCounts = new ArrayList();
	public final List<Integer> chanceForBigTree = new ArrayList();
	
	public void add(Block wood, int woodMeta, Block leaves, int leavesMeta, Block sapling, Block vines, Block cocoa,
	                int dispersion, int perChunk, int chanceForBig,
	                int minHeight, boolean doVinesLog, boolean doVinesLeaves,
	                byte ocp1, byte ocp2, byte ocp3, byte ocp4, byte ocp5, byte ocp6, int ts, int hl, int ldl, double ha, double bs, double sw, double ld) {
		smallTrees.add(new WE_TreeGen(false));
		bigTrees.add(new WE_BigTreeGen(false));
		//-//
		dispersions.add(dispersion);
		treesPerChunkCounts.add(perChunk);
		chanceForBigTree.add(chanceForBig);
		
		smallTrees.get(smallTrees.size() - 1).bWood = wood;
		smallTrees.get(smallTrees.size() - 1).metaWood = woodMeta;
		smallTrees.get(smallTrees.size() - 1).bLeaves = leaves;
		smallTrees.get(smallTrees.size() - 1).metaLeaves = leavesMeta;
		smallTrees.get(smallTrees.size() - 1).bSapling = sapling;
		smallTrees.get(smallTrees.size() - 1).bVine = vines;
		smallTrees.get(smallTrees.size() - 1).bCocoa = cocoa;
		//-//
		bigTrees.get(bigTrees.size() - 1).bWood = wood;
		bigTrees.get(bigTrees.size() - 1).metaWood = woodMeta;
		bigTrees.get(bigTrees.size() - 1).bLeaves = leaves;
		bigTrees.get(bigTrees.size() - 1).metaLeaves = leavesMeta;
		bigTrees.get(bigTrees.size() - 1).bSapling = sapling;
		
		smallTrees.get(smallTrees.size() - 1).minTreeHeight = minHeight;
		smallTrees.get(smallTrees.size() - 1).vinesGrowLog = doVinesLog;
		smallTrees.get(smallTrees.size() - 1).vinesGrowLeaves = doVinesLeaves;
		//-//
		bigTrees.get(bigTrees.size() - 1).otherCoordPairs = new byte[] { ocp1, ocp2, ocp3, ocp4, ocp5, ocp6 };
		bigTrees.get(bigTrees.size() - 1).trunkSize = ts;
		bigTrees.get(bigTrees.size() - 1).heightLimitLimit = hl;
		bigTrees.get(bigTrees.size() - 1).leafDistanceLimit = ldl;
		bigTrees.get(bigTrees.size() - 1).heightAttenuation = ha;
		bigTrees.get(bigTrees.size() - 1).branchSlope = bs;
		bigTrees.get(bigTrees.size() - 1).scaleWidth = sw;
		bigTrees.get(bigTrees.size() - 1).leafDensity = ld;
	}
	
	public void add(Block bWood, int mWood, Block bLeaves, int mLeaves, Block bSapling, Block bVine, Block bCocoa, int v1, int v2, int minHeight, boolean vines) {
		smallTrees.add(new WE_TreeGen(false));
		bigTrees.add(null);
		//-//
		dispersions.add(v1);
		treesPerChunkCounts.add(v2);
		chanceForBigTree.add(0);
		
		smallTrees.get(smallTrees.size() - 1).bWood = bWood;
		smallTrees.get(smallTrees.size() - 1).metaWood = mWood;
		smallTrees.get(smallTrees.size() - 1).bLeaves = bLeaves;
		smallTrees.get(smallTrees.size() - 1).metaLeaves = mLeaves;
		smallTrees.get(smallTrees.size() - 1).bSapling = bSapling;
		smallTrees.get(smallTrees.size() - 1).bVine = bVine;
		smallTrees.get(smallTrees.size() - 1).bCocoa = bCocoa;
		//-//
		smallTrees.get(smallTrees.size() - 1).minTreeHeight = minHeight;
		smallTrees.get(smallTrees.size() - 1).vinesGrowLeaves = vines;
	}
	
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
		for (int i = 0; i < smallTrees.size(); i++)
			if (random.nextInt(dispersions.get(i)) == 0)
				for (int i2 = 0; i2 < treesPerChunkCounts.get(i); i2++) {
					int x = chunkX * 16 + random.nextInt(16),
						z = chunkZ * 16 + random.nextInt(16),
						y = world.getHeightValue(x, z);
					
					if (chanceForBigTree.get(i) <= 0)
						smallTrees.get(i).generate(world, random, x, y, z);
					else if (random.nextInt(chanceForBigTree.get(i)) == 0)
						bigTrees.get(i).generate(world, random, x, y, z);
					else
						smallTrees.get(i).generate(world, random, x, y, z);
				}
	}
}