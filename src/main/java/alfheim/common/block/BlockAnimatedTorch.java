package alfheim.common.block;

import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.api.block.IHourglassTrigger;
import alfheim.api.lib.LibRenderIDs;
import alfheim.common.block.tile.TileAnimatedTorch;
import alfheim.common.lexicon.AlfheimLexiconData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.IManaTrigger;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;

public class BlockAnimatedTorch extends Block implements ITileEntityProvider, IHourglassTrigger, IWandable, IManaTrigger, IWandHUD, ILexiconable {

	public BlockAnimatedTorch() {
		super(Material.circuits);
		setCreativeTab(AlfheimCore.alfheimTab);
		setBlockBounds(0, 0, 0, 1, 0.25F, 1);
		setBlockName("AnimatedTorch");
		setBlockTextureName(ModInfo.MODID + ":AnimatedTorch");
		setLightLevel(0.5F);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if(player.isSneaking() && player.getHeldItem() == null) {
			((TileAnimatedTorch) world.getTileEntity(x, y, z)).handRotate();
			return true;
		}

		return false;
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
		((TileAnimatedTorch) world.getTileEntity(x, y, z)).onPlace(entity);
	}

	@Override
	public void onBurstCollision(IManaBurst burst, World world, int x, int y, int z) {
		if(!burst.isFake()) ((TileAnimatedTorch) world.getTileEntity(x, y, z)).toggle();
	}

	@Override
	public void onTriggeredByHourglass(World world, int x, int y, int z, TileEntity hourglass) {
		((TileAnimatedTorch) world.getTileEntity(x, y, z)).toggle();
	}
	
	@Override
	public boolean onBlockEventReceived(World world, int x, int y, int z, int id, int param) {
		super.onBlockEventReceived(world, x, y, z, id, param);
        TileEntity tile = world.getTileEntity(x, y, z);
        return tile != null ? tile.receiveClientEvent(id, param) : false;
	}
	
	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int side) {
		((TileAnimatedTorch) world.getTileEntity(x, y, z)).onWanded();
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderHUD(Minecraft mc, ScaledResolution res, World world, int x, int y, int z) {
		((TileAnimatedTorch) world.getTileEntity(x, y, z)).renderHUD(mc, res);
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}
	
	@Override
	public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side) {
		return isProvidingWeakPower(world, x, y, z, side);
	}
	
	@Override
	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
		TileAnimatedTorch tile = (TileAnimatedTorch) world.getTileEntity(x, y, z);
		if(tile.rotating) return 0;
		if(TileAnimatedTorch.SIDES[tile.side].ordinal() == side) return 15;
		return 0;
	}
	
	@Override
	public int getRenderType() {
		return LibRenderIDs.idAniTorch;
	}
	
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean isNormalCube() {
		return false;
	}
	
	@Override
	public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta) {
		world.notifyBlocksOfNeighborChange(x, y, z, this);
		super.onBlockDestroyedByPlayer(world, x, y, z, meta);
	}
	
	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return AlfheimLexiconData.aniTorch;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileAnimatedTorch();
	}
}