package alfheim.common.block;

import java.util.List;

import alfheim.AlfheimCore;
import alfheim.api.lib.LibRenderIDs;
import alfheim.common.block.tile.TileAlfheimPylon;
import alfheim.common.core.registry.AlfheimBlocks;
import alfheim.common.lexicon.AlfheimLexiconData;
import cpw.mods.fml.common.Optional;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import thaumcraft.api.crafting.IInfusionStabiliser;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.block.BlockModContainer;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.ConfigHandler;

@Optional.Interface(modid = "Thaumcraft", iface = "thaumcraft.api.crafting.IInfusionStabiliser", striprefs = true)
public class BlockAlfheimPylon extends BlockModContainer implements ILexiconable, IInfusionStabiliser {
	
	public BlockAlfheimPylon() {
		super(Material.iron);
		final float f = 1F / 16F * 2F;
		this.setBlockBounds(f, 0F, f, 1F - f, 1F / 16F * 21F, 1F - f);
		this.setBlockName("AlfheimPylons");
		this.setCreativeTab(AlfheimCore.alfheimTab);
		this.setLightLevel(0.5F);
		this.setHardness(5.5F);
		this.setStepSound(soundTypeMetal);
	}
	
	@Override
	protected boolean shouldRegisterInNameSet() {
		return false;
	}
	
	@Override
	public void registerBlockIcons(IIconRegister reg) {
		// NO-OP
	}
	
	@Override
	public int damageDropped(int meta) {
		return meta;
	}
	
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List subs) {
		super.getSubBlocks(item, tab, subs); // elven		(pink)
		subs.add(new ItemStack(item, 1, 1)); // elvorium	(orange)
		subs.add(new ItemStack(item, 1, 2)); // anti		(red)
	}
	
	@Override
	public IIcon getIcon(int side, int meta) { // elementium for pink; elvorium for orange; redstone for red
		return meta == 2 ? Blocks.redstone_block.getIcon(side, 0) : meta == 1 ? AlfheimBlocks.elvoriumBlock.getIcon(side, 0) : ModBlocks.storage.getIcon(side, 2);
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
		return LibRenderIDs.idPylon; 
	}
	
	@Override
	public float getEnchantPowerBonus(World world, int x, int y, int z) {
		return world.getBlockMetadata(x, y, z) == 0 ? 8 : 15; // pink 8; orange and red 15
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileAlfheimPylon();
	}
	
	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		int meta = world.getBlockMetadata(x, y, z);
		return meta == 2 ? AlfheimLexiconData.soul: meta == 1 ? AlfheimLexiconData.trade : AlfheimLexiconData.pylons ;
	}
	
	@Override
	public boolean canStabaliseInfusion(World world, int x, int y, int z) {
		return ConfigHandler.enableThaumcraftStablizers;
	}
}
