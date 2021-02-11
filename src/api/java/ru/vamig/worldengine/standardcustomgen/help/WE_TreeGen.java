//- By Vamig Aliev.
//- https://vk.com/win_vista.

package ru.vamig.worldengine.standardcustomgen.help;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.*;

public class WE_TreeGen extends WorldGenerator {
	
	public final List<Block> ab = new ArrayList();
	public final List<Material> am = new ArrayList();
	public Block bWood = Blocks.log, bLeaves = Blocks.leaves, bSapling = Blocks.sapling, bVine = Blocks.vine, bCocoa = Blocks.cocoa;
	public int minTreeHeight = 4, metaWood = 0, metaLeaves = 0;
	public boolean vinesGrowLeaves = false;
	public boolean vinesGrowLog = false;
	
	public WE_TreeGen(boolean doBlockNotify) {
		super(doBlockNotify);
		
		ab.add(Blocks.grass);
		ab.add(Blocks.dirt);
		ab.add(Blocks.log);
		ab.add(Blocks.log2);
		ab.add(Blocks.sapling);
		ab.add(Blocks.vine);
		//-//
		am.add(Material.air);
		am.add(Material.leaves);
	}
	
	public boolean generate(World world, Random random, int x, int y, int z) {
		int l = random.nextInt(3) + minTreeHeight;
		boolean flag = true;
		if (y >= 1 && y + l + 1 <= 256) {
			byte b0;
			int k1;
			Block block;
			for (int i1 = y; i1 <= y + 1 + l; ++i1) {
				b0 = 1;
				if (i1 == y)
					b0 = 0;
				if (i1 >= y + 1 + l - 2)
					b0 = 2;
				
				for (int j1 = x - b0; j1 <= x + b0 && flag; ++j1)
					for (k1 = z - b0; k1 <= z + b0 && flag; ++k1)
						if (i1 >= 0 && i1 < 256) {
							if (!isReplaceable(world, j1, i1, k1))
								flag = false;
						} else
							flag = false;
			}
			
			if (!flag)
				return false;
			else {
				Block block2 = world.getBlock(x, y - 1, z);
				
				boolean isSoil = block2.canSustainPlant(world, x, y - 1, z, ForgeDirection.UP, (BlockSapling) bSapling);
				if (isSoil && y < 256 - l - 1) {
					block2.onPlantGrow(world, x, y - 1, z, x, y, z);
					
					b0 = 3;
					byte b1 = 0;
					int l1, i2, j2, i3;
					for (k1 = y - b0 + l; k1 <= y + l; ++k1) {
						i3 = k1 - y - l;
						l1 = b1 + 1 - i3 / 2;
						//-//
						for (i2 = x - l1; i2 <= x + l1; ++i2) {
							j2 = i2 - x;
							for (int k2 = z - l1; k2 <= z + l1; ++k2) {
								int l2 = k2 - z;
								if (Math.abs(j2) != l1 || Math.abs(l2) != l1 || random.nextInt(2) != 0 && i3 != 0) {
									Block block1 = world.getBlock(i2, k1, k2);
									if (block1.isAir(world, i2, k1, k2) || block1.isLeaves(world, i2, k1, k2))
										setBlockAndNotifyAdequately(world, i2, k1, k2, bLeaves, metaLeaves);
								}
							}
						}
					}
					
					for (k1 = 0; k1 < l; ++k1) {
						block = world.getBlock(x, y + k1, z);
						if (block.isAir(world, x, y + k1, z) || block.isLeaves(world, x, y + k1, z)) {
							setBlockAndNotifyAdequately(world, x, y + k1, z, bWood, metaWood);
							if (vinesGrowLog && k1 > 0) {
								if (random.nextInt(3) > 0 && world.isAirBlock(x - 1, y + k1, z))
									setBlockAndNotifyAdequately(world, x - 1, y + k1, z, bVine, 8);
								if (random.nextInt(3) > 0 && world.isAirBlock(x + 1, y + k1, z))
									setBlockAndNotifyAdequately(world, x + 1, y + k1, z, bVine, 2);
								if (random.nextInt(3) > 0 && world.isAirBlock(x, y + k1, z - 1))
									setBlockAndNotifyAdequately(world, x, y + k1, z - 1, bVine, 1);
								if (random.nextInt(3) > 0 && world.isAirBlock(x, y + k1, z + 1))
									setBlockAndNotifyAdequately(world, x, y + k1, z + 1, bVine, 4);
							}
						}
					}
					
					if (vinesGrowLeaves) {
						for (k1 = y - 3 + l; k1 <= y + l; ++k1) {
							i3 = k1 - y - l;
							l1 = 2 - i3 / 2;
							//-//
							for (i2 = x - l1; i2 <= x + l1; ++i2)
								for (j2 = z - l1; j2 <= z + l1; ++j2)
									if (world.getBlock(i2, k1, j2).isLeaves(world, i2, k1, j2)) {
										if (random.nextInt(4) == 0 && world.getBlock(i2 - 1, k1, j2).isAir(world, i2 - 1, k1, j2))
											growVines(world, i2 - 1, k1, j2, 8);
										if (random.nextInt(4) == 0 && world.getBlock(i2 + 1, k1, j2).isAir(world, i2 + 1, k1, j2))
											growVines(world, i2 + 1, k1, j2, 2);
										if (random.nextInt(4) == 0 && world.getBlock(i2, k1, j2 - 1).isAir(world, i2, k1, j2 - 1))
											growVines(world, i2, k1, j2 - 1, 1);
										if (random.nextInt(4) == 0 && world.getBlock(i2, k1, j2 + 1).isAir(world, i2, k1, j2 + 1))
											growVines(world, i2, k1, j2 + 1, 4);
									}
						}
						
						if (bCocoa != null && random.nextInt(5) == 0 && l > 5)
							for (k1 = 0; k1 < 2; ++k1)
								for (i3 = 0; i3 < 4; ++i3)
									if (random.nextInt(4 - k1) == 0) {
										l1 = random.nextInt(3);
										setBlockAndNotifyAdequately(world, x + Direction.offsetX[Direction.rotateOpposite[i3]], y + l - 5 + k1, z + Direction.offsetZ[Direction.rotateOpposite[i3]], bCocoa, l1 << 2 | i3);
									}
					}
					
					return true;
				} else
					return false;
			}
		} else
			return false;
	}
	
	public boolean isReplaceable(World world, int x, int y, int z) {
		Block b = world.getBlock(x, y, z);
		return b.isAir(world, x, y, z) || b.isLeaves(world, x, y, z) || b.isWood(world, x, y, z) || cb(b);
	}
	
	public void growVines(World p_76529_1_, int p_76529_2_, int p_76529_3_, int p_76529_4_, int p_76529_5_) {
		setBlockAndNotifyAdequately(p_76529_1_, p_76529_2_, p_76529_3_, p_76529_4_, bVine, p_76529_5_);
		int i1 = 4;
		//-//
		while (true) {
			--p_76529_3_;
			//-//
			if (!p_76529_1_.getBlock(p_76529_2_, p_76529_3_, p_76529_4_).isAir(p_76529_1_, p_76529_2_, p_76529_3_, p_76529_4_) || i1 <= 0)
				return;
			//-//
			setBlockAndNotifyAdequately(p_76529_1_, p_76529_2_, p_76529_3_, p_76529_4_, bVine, p_76529_5_);
			--i1;
		}
	}
	
	public boolean cb(Block b) {
		for (Block block : ab)
			if (b == block)
				return true;
		for (Material material : am)
			if (b.getMaterial() == material)
				return true;
		return false;
	}
}