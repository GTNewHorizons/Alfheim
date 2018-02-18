package alfheim.common.block;

import alexsocol.asjlib.ItemContainingTileEntity;
import alfheim.AlfheimCore;
import alfheim.client.core.proxy.ClientProxy;
import alfheim.client.lib.LibRenderIDs;
import alfheim.common.block.tile.TileAnyavil;
import alfheim.common.lexicon.AlfheimLexiconData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.IManaTrigger;
import vazkii.botania.api.wand.IWandHUD;

public class BlockAnyavil extends Block implements ITileEntityProvider, IManaTrigger, IWandHUD, ILexiconable {

	public BlockAnyavil() {
		super(Material.iron);
		this.setBlockName("Anyavil");
		this.setCreativeTab(AlfheimCore.alfheimTab);
		this.setLightOpacity(0);
		this.setHardness(5.0F);
		this.setResistance(2000.0F);
		this.setStepSound(soundTypeAnvil);
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
		return LibRenderIDs.idAnyavil;
	}
	
	@Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
        int l = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        world.setBlockMetadataWithNotify(x, y, z, l, 2);
    }

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		ItemContainingTileEntity te = (ItemContainingTileEntity) world.getTileEntity(x, y, z);
		ItemStack stack = player.inventory.getCurrentItem();
		if(player.isSneaking()) return false;
		if(te != null) {
			if(te.getItem() != null) {
				if(!world.isRemote){
					EntityItem entityitem = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, te.item);
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
	public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
		int l = world.getBlockMetadata(x, y, z) & 3;

		if (l != 3 && l != 1) {
			this.setBlockBounds(0.125F, 0.0F, 0.0F, 0.875F, 1.0F, 1.0F);
		} else {
			this.setBlockBounds(0.0F, 0.0F, 0.125F, 1.0F, 1.0F, 0.875F);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
    public int getMixedBrightnessForBlock(IBlockAccess world, int x, int y, int z) {
		return world.getLightBrightnessForSkyBlocks(x, y, z, world.getBlock(x, y, z).getLightValue(world, x, y - 1, z));
    }
	

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		ItemContainingTileEntity te = (ItemContainingTileEntity) world.getTileEntity(x, y, z);
		if(te != null) {
			if(te.getItem() != null) {
				EntityItem entityitem = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, te.getItem());
				world.spawnEntityInWorld(entityitem);
				te.setItem(null);
			}
		}

		super.breakBlock(world, x, y, z, block, meta);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileAnyavil();
	}
	
	@Override
	public void onBurstCollision(IManaBurst burst, World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if (tile != null && tile instanceof TileAnyavil) ((TileAnyavil) tile).onBurstCollision(burst, world, x, y, z);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderHUD(Minecraft mc, ScaledResolution res, World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(x, y, z);
		if (tile != null && tile instanceof TileAnyavil) ((TileAnyavil) tile).renderHUD(mc, res, world, x, y, z);
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return AlfheimLexiconData.anyavil;
	}
}