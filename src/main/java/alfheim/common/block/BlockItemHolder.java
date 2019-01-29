package alfheim.common.block;

import alexsocol.asjlib.extendables.ItemContainingTileEntity;
import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.api.lib.LibRenderIDs;
import alfheim.common.block.tile.TileItemHolder;
import alfheim.common.lexicon.AlfheimLexiconData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.block.mana.BlockPool;

public class BlockItemHolder extends Block implements ILexiconable, ITileEntityProvider {

	public BlockItemHolder() {
		super(Material.iron);
		setBlockBounds(0, -0.5F, 0, 1, -0.125F, 1);
		setBlockName("ItemHolder");
		setBlockTextureName(ModInfo.MODID + ":ItemHolder");
		setCreativeTab(AlfheimCore.alfheimTab);
		setBlockTextureName(LibResources.PREFIX_MOD + "livingrock0");
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public int getRenderType() {
		return LibRenderIDs.idItemHolder;
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return world.getBlock(x, y - 1, z) instanceof BlockPool;
	}
	
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		ItemContainingTileEntity te = (ItemContainingTileEntity) world.getTileEntity(x, y, z);
		ItemStack stack = player.inventory.getCurrentItem();
		if(player.isSneaking()) return false;
		if(te != null) {
			if(te.getItem() != null) {
				if(!world.isRemote){
					EntityItem entityitem = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, te.getItem());
					world.spawnEntityInWorld(entityitem);				
				}
				te.setItem(null);
			}
			if(stack != null && stack.stackSize == 1 && stack.getItem().isDamageable()) {
				te.setItem(stack.copy());
				te.getItem().stackSize = stack.stackSize;
				stack.stackSize = 0;
			}
			
			world.setTileEntity(x, y, z, te);
			world.updateLightByType(EnumSkyBlock.Sky, x, y, z);	
			world.markTileEntityChunkModified(x, y, z, te);
		}
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public int getMixedBrightnessForBlock(IBlockAccess world, int x, int y, int z) {
		return world.getLightBrightnessForSkyBlocks(x, y, z, world.getBlock(x, y, z).getLightValue(world, x, y - 1, z));
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		ItemContainingTileEntity te = (ItemContainingTileEntity) world.getTileEntity(x, y, z);
		if(te != null && te.getItem() != null) {
			world.spawnEntityInWorld(new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, te.getItem()));
			te.setItem(null);
		}

		super.breakBlock(world, x, y, z, block, meta);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileItemHolder();
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return AlfheimLexiconData.itemHold;
	}
}