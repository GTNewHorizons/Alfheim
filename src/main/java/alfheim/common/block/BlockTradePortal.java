package alfheim.common.block;

import alfheim.AlfheimCore;
import alfheim.ModInfo;
import alfheim.common.block.tile.TileTradePortal;
import alfheim.common.lexicon.AlfheimLexiconData;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.block.ModBlocks;

public class BlockTradePortal extends Block implements ITileEntityProvider, ILexiconable {

	public static IIcon[] textures = new IIcon[3];
	
	public BlockTradePortal() {
		super(Material.rock);
		this.setBlockName("TradePortal");
		this.setBlockTextureName(ModInfo.MODID + ":TradePortal");
		this.setCreativeTab(AlfheimCore.alfheimTab);
		this.setHarvestLevel("pickaxe", 0);
		this.setHardness(10.0F);
		this.setResistance(600.0F);
		this.setStepSound(soundTypeStone);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister reg) {
		textures[0] = reg.registerIcon(ModInfo.MODID + ":TradePortal");
		textures[1] = reg.registerIcon(ModInfo.MODID + ":TradePortalActive");
		textures[2] = reg.registerIcon(ModInfo.MODID + ":TradePortalInside");
	}
	
	@Override
	public IIcon getIcon(int side, int meta) {
		return meta == 0 ? textures[0] : textures [1];
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileTradePortal();
	}
	
	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) return ((TileTradePortal) world.getTileEntity(x, y, z)).onWanded();
		return false;
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		return world.getBlockMetadata(x, y, z) == 0 ? 0 : 15;
	}
	
	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return AlfheimLexiconData.trade;
	}
}