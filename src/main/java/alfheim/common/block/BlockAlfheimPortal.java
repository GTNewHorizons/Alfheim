package alfheim.common.block;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.Constants;
import alfheim.common.block.tile.TileAlfheimPortal;
import alfheim.common.item.ElvenResource;
import alfheim.common.registry.AlfheimAchievements;
import alfheim.common.registry.AlfheimItems;
import alfheim.common.registry.AlfheimItems.ElvenResourcesMetas;
import alfheim.common.utils.AlfheimConfig;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.item.EntityItem;
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

public class BlockAlfheimPortal extends Block implements ITileEntityProvider {

	public static IIcon[] textures = new IIcon[3];
	
	public BlockAlfheimPortal() {
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
		return new TileAlfheimPortal();
	}
	
	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (world.provider.dimensionId == 0)
			if (world.getBlockMetadata(x, y, z) == 0 && player.getCurrentEquippedItem() != null
			&&  player.getCurrentEquippedItem().getItem() == AlfheimItems.elvenResource
			&&  player.getCurrentEquippedItem().getItemDamage() == ElvenResourcesMetas.InterdimensionalGatewayCore)
				ASJUtilities.consumeItemStack(player.inventory, new ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.InterdimensionalGatewayCore));
			else
				return false;		

		boolean did = ((TileAlfheimPortal) world.getTileEntity(x, y, z)).onWanded();
		if(did && player != null)
			player.addStat(AlfheimAchievements.alfheim, 1);
		return did;
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		return world.getBlockMetadata(x, y, z) == 0 ? 0 : 15;
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		if (world.provider.dimensionId == AlfheimConfig.dimensionIDAlfheim && x == 0 && y == 72 && z == 0) return;
		if (world.provider.dimensionId != AlfheimConfig.dimensionIDAlfheim && meta != 0) world.spawnEntityInWorld(new EntityItem(world, x, y, z, new ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.InterdimensionalGatewayCore)));
		super.breakBlock(world, x, y, z, block, meta);
    }
	
	@Override
	public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
		if (world.provider.dimensionId == AlfheimConfig.dimensionIDAlfheim && x == 0 && y == 72 && z == 0) return false;
		return super.removedByPlayer(world, player, x, y, z, willHarvest);
    }
	
	@Override
	public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int meta) {
		if (world.provider.dimensionId == AlfheimConfig.dimensionIDAlfheim && x == 0 && y == 72 && z == 0) return;
		super.harvestBlock(world, player, x, y, z, meta);
	}
}