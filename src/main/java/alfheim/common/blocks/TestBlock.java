package alfheim.common.blocks;

import alexsocol.asjlib.ASJUtilities;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TestBlock extends Block implements ITileEntityProvider {
	
	public TestBlock() {
		super(Material.glass);
		this.setBlockName("TheBlockOfTesting");
		this.setCreativeTab(CreativeTabs.tabBlock);
		ASJUtilities.register(this);
	}
	
	@Override
    public boolean isOpaqueCube() {
        return false;
    }
	
	@Override
    public boolean renderAsNormalBlock() {
        return false;
    }
	
	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i) {
		return new TileEntityTestBlock();
	}
	
	public static class TileEntityTestBlock extends TileEntity{
		
	}
}
