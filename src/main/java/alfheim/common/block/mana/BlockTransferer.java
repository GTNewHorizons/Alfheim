package alfheim.common.block.mana;

import java.util.List;
import java.util.Random;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.ItemContainingTileEntity;
import alfheim.AlfheimCore;
import alfheim.client.lib.LibRenderIDs;
import alfheim.common.block.tile.TileTransferer;
import alfheim.common.lexicon.AlfheimLexiconData;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.ILens;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.api.wand.IWireframeAABBProvider;
import vazkii.botania.common.block.BlockModContainer;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockWithMetadataAndName;
import vazkii.botania.common.lexicon.LexiconData;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockTransferer extends BlockModContainer implements IWandable, IWandHUD, ILexiconable, IWireframeAABBProvider {

	Random random;
	
	public BlockTransferer() {
		super(Material.wood);
		this.setBlockName("Transferer");
		this.setCreativeTab(AlfheimCore.alfheimTab);
		this.setHardness(2.0F);
		this.setStepSound(soundTypeWood);
		random = new Random();
	}

	@Override
	protected boolean shouldRegisterInNameSet() {
		return false;
	}

	@Override
	public void registerBlockIcons(IIconRegister par1IconRegister) {
		// NO-OP
	}

	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
		int orientation = BlockPistonBase.determineOrientation(par1World, par2, par3, par4, par5EntityLivingBase);
		TileTransferer spreader = (TileTransferer) par1World.getTileEntity(par2, par3, par4);
		par1World.setBlockMetadataWithNotify(par2, par3, par4, 0, 1 | 2);

		switch(orientation) {
		case 0:
			spreader.rotationY = -90F;
			break;
		case 1:
			spreader.rotationY = 90F;
			break;
		case 2:
			spreader.rotationX = 270F;
			break;
		case 3:
			spreader.rotationX = 90F;
			break;
		case 4:
			break;
		default:
			spreader.rotationX = 180F;
			break;
		}
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
	public IIcon getIcon(int par1, int par2) {
		return ModBlocks.dreamwood.getIcon(par1, 0);
	}

	@Override
	public int getRenderType() {
		return LibRenderIDs.idTransferer;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if(player.isSneaking()) return false;
		ItemStack stack = player.inventory.getCurrentItem();
		if(stack != null && stack.getItem() == ModItems.twigWand) return false;
		TileEntity tile = world.getTileEntity(x, y, z);
		if (tile == null || !(tile instanceof TileTransferer)) return false;
		ItemContainingTileEntity te = (ItemContainingTileEntity) tile;
		
		if(te.getItem() != null) {
			if(!world.isRemote){
				EntityItem entityitem = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, te.item);
				world.spawnEntityInWorld(entityitem);				
			}
			te.setItem(null);
		}
		
		if(stack != null) {
			te.setItem(stack.copy());
			te.getItem().stackSize = stack.stackSize;
			stack.stackSize = 0;
		}
			
		world.setTileEntity(x, y, z, te);
		world.updateLightByType(EnumSkyBlock.Sky, x, y, z);	
		world.markTileEntityChunkModified(x, y, z, te);
	
		return true;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		ItemContainingTileEntity te = (ItemContainingTileEntity) world.getTileEntity(x, y, z);
		if(te != null) {
			if(te.getItem() != null) {
				EntityItem entityitem = new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, te.getItem().copy());
				world.spawnEntityInWorld(entityitem);
				te.setItem(null);
			}
			world.func_147453_f(x, y, z, block);
		}

		super.breakBlock(world, x, y, z, block, meta);
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int side) {
		((TileTransferer) world.getTileEntity(x, y, z)).onWanded(player, stack);
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileTransferer();
	}

	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res, World world, int x, int y, int z) {
		((TileTransferer) world.getTileEntity(x, y, z)).renderHUD(mc, res);
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return AlfheimLexiconData.trans;
	}

	@Override
	public AxisAlignedBB getWireframeAABB(World world, int x, int y, int z) {
		float f = 1F / 16F;
		return AxisAlignedBB.getBoundingBox(x + f, y + f, z + f, x + 1 - f, y + 1 - f, z + 1 - f);
	}
}