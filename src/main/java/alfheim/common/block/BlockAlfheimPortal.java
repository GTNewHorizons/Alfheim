package alfheim.common.block;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.common.block.tile.TileAlfheimPortal;
import alfheim.common.core.registry.AlfheimAchievements;
import alfheim.common.core.registry.AlfheimItems;
import alfheim.common.core.registry.AlfheimItems.ElvenResourcesMetas;
import alfheim.common.core.util.AlfheimConfig;
import alfheim.common.lexicon.AlfheimLexiconData;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
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

public class BlockAlfheimPortal extends BlockContainer implements ILexiconable {

	public static final IIcon[] textures = new IIcon[3];
	
	public BlockAlfheimPortal() {
		super(Material.wood);
		this.setBlockName("AlfheimPortal");
		this.setBlockTextureName(ModInfo.MODID + ":AlfheimPortal");
		this.setCreativeTab(AlfheimCore.alfheimTab);
		this.setHardness(10.0F);
		this.setResistance(600.0F);
		this.setStepSound(soundTypeWood);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister reg) {
		textures[0] = reg.registerIcon(ModInfo.MODID + ":AlfheimPortal");
		textures[1] = reg.registerIcon(ModInfo.MODID + ":AlfheimPortalActive");
		textures[2] = reg.registerIcon(ModInfo.MODID + ":AlfheimPortalInside");
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
		int newMeta = ((TileAlfheimPortal) world.getTileEntity(x, y, z)).getValidMetadata();
		if (newMeta == 0) return false;
		
		if (world.provider.dimensionId != AlfheimConfig.dimensionIDAlfheim) {
			if (world.getBlockMetadata(x, y, z) == 0 && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() == AlfheimItems.elvenResource && player.getCurrentEquippedItem().getItemDamage() == ElvenResourcesMetas.InterdimensionalGatewayCore) {
				ASJUtilities.consumeItemStack(player.inventory, new ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.InterdimensionalGatewayCore));
			} else return false;		
		}
		
		boolean did = ((TileAlfheimPortal) world.getTileEntity(x, y, z)).onWanded(newMeta);
		if(did && player != null) player.addStat(AlfheimAchievements.alfheim, 1);
		return did;
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		return world.getBlockMetadata(x, y, z) == 0 ? 0 : 15;
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta) {
		if (world.provider.dimensionId != AlfheimConfig.dimensionIDAlfheim && meta != 0) world.spawnEntityInWorld(new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, new ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.InterdimensionalGatewayCore)));
		super.breakBlock(world, x, y, z, block, meta);
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return AlfheimLexiconData.portal;
	}
}