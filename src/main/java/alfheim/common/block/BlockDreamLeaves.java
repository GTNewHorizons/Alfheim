package alfheim.common.block;

import java.util.Random;

import alexsocol.asjlib.IGlowingLayerBlock;
import alexsocol.asjlib.RenderGlowingLayerBlock;
import alfheim.AlfheimCore;
import alfheim.Constants;
import alfheim.common.core.registry.AlfheimBlocks;
import alfheim.common.lexicon.AlfheimLexiconCategory;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;

public class BlockDreamLeaves extends BlockLeaves implements IGlowingLayerBlock, ILexiconable {

	public IIcon[] textures = new IIcon[3];
	
	public BlockDreamLeaves() {
		super();
		this.setBlockName("DreamLeaves");
		this.setBlockTextureName(Constants.MODID + ":DreamLeaves");
		this.setCreativeTab(AlfheimCore.alfheimTab);
		this.setLightOpacity(0);
	}

	@Override
	public boolean isLeaves(IBlockAccess world, int x, int y, int z) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube() {
        return Blocks.leaves.isOpaqueCube();
    }
	
	@Override
	public Item getItemDropped(int meta, Random rand, int p_149650_3_) {
        return Item.getItemFromBlock(AlfheimBlocks.dreamSapling);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public int getRenderColor(int meta) {
		return Integer.parseInt("E5FFF9", 16);
	}

	@Override
	@SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
        return Integer.parseInt("E5FFF9", 16);
    }
	
	@Override
    public void func_150124_c(World world, int x, int y, int z, int meta, int chance) {}

	@Override
	protected int func_150123_b(int meta) {
        return 100;
    }

	@Override
	public IIcon getIcon(int side, int meta) {
        return textures[Blocks.leaves.isOpaqueCube() ? 1 : 0];
	}

	@Override
	public IIcon getGlowIcon(int side, int meta) {
		return textures[2];
	}

	@SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
		textures[0] = reg.registerIcon(Constants.MODID + ":DreamLeaves");
		textures[1] = reg.registerIcon(Constants.MODID + ":DreamLeavesOpaque");
		textures[2] = reg.registerIcon(Constants.MODID + ":DreamSparks");
    }
	
	public int getRenderType() {
		return RenderGlowingLayerBlock.glowBlockID;
	}
	
	@Override
	public String[] func_150125_e() {
		return new String[] { "dream" };
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return AlfheimLexiconCategory.worldgen;
	}
}