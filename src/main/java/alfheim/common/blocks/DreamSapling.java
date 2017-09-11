package alfheim.common.blocks;

import java.util.Random;

import alfheim.AlfheimCore;
import alfheim.Constants;
import alfheim.common.registry.AlfheimBlocks;
import alfheim.common.world.dim.gen.structure.DreamsTreeStructure;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class DreamSapling extends BlockBush implements IGrowable {

	public DreamSapling() {
		this.setBlockName("DreamSapling");
		this.setBlockTextureName(Constants.MODID + ":DreamSapling");
		this.setCreativeTab(AlfheimCore.alfheimTab);
		this.setLightLevel(9.0F / 15.0F);
		this.setLightOpacity(0);
		this.setTickRandomly(true);
	}

	public void updateTick(World world, int x, int y, int z, Random rand) {
		if (!world.isRemote) {
			super.updateTick(world, x, y, z, rand);

			if (world.getBlockLightValue(x, y + 1, z) >= 9 && rand.nextInt(7) == 0) {
				this.func_149879_c(world, x, y, z, rand);
			}
		}
	}

	public void func_149879_c(World world, int x, int y, int z, Random rand) {
		int l = world.getBlockMetadata(x, y, z);

		if ((l & 8) == 0) {
			world.setBlockMetadataWithNotify(x, y, z, l | 8, 4);
		} else {
			this.func_149878_d(world, x, y, z, rand);
		}
	}

	public void func_149878_d(World world, int x, int y, int z, Random rand) {
		if (!net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(world, rand, x, y, z)) return;
		int l = world.getBlockMetadata(x, y, z) & 7;
		world.setBlock(x, y, z, Blocks.air, 0, 4);
		if (!(new DreamsTreeStructure()).generate(world, rand, x, y, z, AlfheimBlocks.dreamLog, AlfheimBlocks.dreamLeaves, 0, 4, 8, 0)) {
			world.setBlock(x, y, z, this, l, 4);
		}
	}

	/** Can the block grow
	 * @param b == world.isRemote */
	@Override
	public boolean func_149851_a(World world, int x, int y, int z, boolean b) {
		return true;
	}

	/** Applying chance to grow
	 * @return true to grow tree */
	@Override
	public boolean func_149852_a(World world, Random rand, int x, int y, int z) {
		return world.rand.nextDouble() < 0.45;
	}

	/** Grow block */
	@Override
	public void func_149853_b(World world, Random rand, int x, int y, int z) {
		this.func_149879_c(world, x, y, z, rand);
	}

	@Override
	public int damageDropped(int meta) {
		return 0;
	}
}
