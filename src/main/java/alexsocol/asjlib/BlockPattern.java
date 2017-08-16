package alexsocol.asjlib;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;

public class BlockPattern extends Block {

	private boolean isBeacon;
	private boolean isOpaque;
	
    public BlockPattern(String modid, Material material, String name, CreativeTabs tab, float lightlvl, int lightOpacity, float hardness, String harvTool, int harvLvl, float resistance, Block.SoundType sound, boolean isOpaque, boolean isBeaconBase) {
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
    }

    @Override
    public boolean isOpaqueCube() {
        return isOpaque;
    }

    @Override
    public boolean isBeaconBase(IBlockAccess world, int x, int y, int z, int beaconX, int beaconY, int beaconZ) {
        return this.isBeacon;
    }
}