package alexsocol.asjlib;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPattern extends BlockFalling {

	private boolean isBeacon;
	private boolean isOpaque;
	private boolean isFalling;

	public BlockPattern(String modid, Material material, String name, CreativeTabs tab, float lightlvl, int lightOpacity, float hardness, String harvTool, int harvLvl, float resistance, Block.SoundType sound, boolean isOpaque, boolean isBeaconBase, boolean isFalling) {
		super(material);
		this.setBlockName(name);
		this.setBlockTextureName(modid + ":" + name);
		this.setCreativeTab(tab);
		this.setLightLevel(lightlvl);
		this.setLightOpacity(lightOpacity);
		this.setHardness(hardness);
		this.setHarvestLevel(harvTool, harvLvl);
		this.setResistance(resistance);
		this.setStepSound(sound);
		this.isOpaque = isOpaque;
		this.isBeacon = isBeaconBase;
		this.isFalling = isFalling;
	}

	@Override
	public boolean isOpaqueCube() {
		return isOpaque;
	}

	@Override
	public boolean isBeaconBase(IBlockAccess world, int x, int y, int z, int beaconX, int beaconY, int beaconZ) {
		return this.isBeacon;
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		if (!world.isRemote && this.isFalling) this.func_149830_m(world, x, y, z);
	}

	private void func_149830_m(World world, int x, int y, int z) {
		if (func_149831_e(world, x, y - 1, z) && y >= 0) {
			byte b0 = 32;

			if (!fallInstantly && world.checkChunksExist(x - b0, y - b0, z - b0, x + b0, y + b0, z + b0)) {
				if (!world.isRemote) {
					EntityFallingBlock entityfallingblock = new EntityFallingBlock(world, (double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F), this, world.getBlockMetadata(x, y, z));
					this.func_149829_a(entityfallingblock);
					world.spawnEntityInWorld(entityfallingblock);
				}
			} else {
				world.setBlockToAir(x, y, z);
				while (func_149831_e(world, x, y - 1, z) && y > 0) --y;
				if (y > 0) world.setBlock(x, y, z, this);
			}
		}
	}
}