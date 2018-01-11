package alfheim.common.block;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.Constants;
import alfheim.common.block.tile.TileAlfheimPortal;
import alfheim.common.block.tile.TileTradePortal;
import alfheim.common.core.registry.AlfheimAchievements;
import alfheim.common.core.registry.AlfheimItems;
import alfheim.common.core.registry.AlfheimItems.ElvenResourcesMetas;
import alfheim.common.core.utils.AlfheimConfig;
import alfheim.common.lexicon.AlfheimLexiconCategory;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.block.BlockAlfPortal;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileAlfPortal;
import vazkii.botania.common.lib.LibBlockNames;

public class BlockTradePortal extends Block implements ITileEntityProvider, ILexiconable {

	public static IIcon[] textures = new IIcon[3];
	
	public BlockTradePortal() {
		super(Material.rock);
		this.setBlockName("TradePortal");
		this.setBlockTextureName(Constants.MODID + ":TradePortal");
		this.setCreativeTab(AlfheimCore.alfheimTab);
		this.setHarvestLevel("pickaxe", 0);
		this.setHardness(10.0F);
		this.setResistance(600.0F);
		this.setStepSound(soundTypeStone);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister reg) {
		textures[0] = reg.registerIcon(Constants.MODID + ":TradePortal");
		textures[1] = reg.registerIcon(Constants.MODID + ":TradePortalActive");
		textures[2] = IconHelper.forBlock(reg, ModBlocks.alfPortal, "Inside");
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
		boolean did = ((TileTradePortal) world.getTileEntity(x, y, z)).onWanded();
		
		// if(did && player != null) player.addStat(ModAchievements.elfPortalOpen, 1);
		return did;
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		return world.getBlockMetadata(x, y, z) == 0 ? 0 : 15;
	}
	
	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return AlfheimLexiconCategory.trade;
	}
}