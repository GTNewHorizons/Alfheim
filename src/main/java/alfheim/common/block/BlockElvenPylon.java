package alfheim.common.block;

import alfheim.AlfheimCore;
import alfheim.Constants;
import alfheim.client.core.proxy.ClientProxy;
import alfheim.common.block.tile.TileElvenPylon;
import alfheim.common.lexicon.AlfheimLexiconData;
import cpw.mods.fml.common.Optional;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import thaumcraft.api.crafting.IInfusionStabiliser;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.ConfigHandler;

@Optional.Interface(modid = "Thaumcraft", iface = "thaumcraft.api.crafting.IInfusionStabiliser", striprefs = true)
public class BlockElvenPylon extends Block implements ITileEntityProvider, ILexiconable, IInfusionStabiliser {

	public BlockElvenPylon() {
		super(Material.iron);
		final float f = 1F / 16F * 2F;
		this.setBlockBounds(f, 0F, f, 1F - f, 1F / 16F * 21F, 1F - f);
		this.setBlockName("ElvenPylon");
		this.setBlockTextureName(Constants.MODID + ":ElvoriumBlock");
		this.setCreativeTab(AlfheimCore.alfheimTab);
		this.setLightLevel(0.5F);
		this.setHardness(5.5F);
		this.setStepSound(soundTypeMetal);
	}

	@Override
	public IIcon getIcon(int par1, int par2) {
		return par2 == 0 ? Blocks.diamond_block.getIcon(0, 0) : ModBlocks.storage.getIcon(0, par2);
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
		return ClientProxy.idPylon;
	}

	@Override
	public float getEnchantPowerBonus(World world, int x, int y, int z) {
		return 8;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileElvenPylon();
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return AlfheimLexiconData.trade;
	}
	@Override
	public boolean canStabaliseInfusion(World world, int x, int y, int z) {
		return ConfigHandler.enableThaumcraftStablizers;
	}
}
