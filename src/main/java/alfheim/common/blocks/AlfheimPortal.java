package alfheim.common.blocks;

import alfheim.AlfheimCore;
import alfheim.Constants;
import alfheim.common.blocks.tileentity.AlfheimPortalTileEntity;
import alfheim.common.registry.AlfheimAchievements;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.block.BlockAlfPortal;
import vazkii.botania.common.block.tile.TileAlfPortal;
import vazkii.botania.common.entity.EntityPixie;

public class AlfheimPortal extends Block implements ITileEntityProvider, IWandable {

	public static IIcon[] textures = new IIcon[3];
	
	public AlfheimPortal() {
		super(Material.wood);
		this.setBlockName("AlfheimPortal");
		this.setBlockTextureName(Constants.MODID + ":AlfheimPortal");
		this.setCreativeTab(AlfheimCore.alfheimTab);
		this.setHardness(10.0F);
		this.setResistance(600.0F);
		this.setStepSound(soundTypeWood);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister reg) {
		textures[0] = reg.registerIcon(Constants.MODID + ":AlfheimPortal");
		textures[1] = reg.registerIcon(Constants.MODID + ":AlfheimPortalActive");
		textures[2] = reg.registerIcon(Constants.MODID + ":AlfheimPortalInside");
	}
	
	@Override
	public IIcon getIcon(int side, int meta) {
		return meta == 0 ? textures[0] : textures [1];
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new AlfheimPortalTileEntity();
	}
	
	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int side) {
		boolean did = ((AlfheimPortalTileEntity) world.getTileEntity(x, y, z)).onWanded();
		if(did && player != null)
			player.addStat(AlfheimAchievements.alfheim, 1);
		return did;
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		return world.getBlockMetadata(x, y, z) == 0 ? 0 : 15;
	}
}