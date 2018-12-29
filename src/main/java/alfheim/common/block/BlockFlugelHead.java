package alfheim.common.block;

import java.util.ArrayList;
import java.util.Random;

import alfheim.common.block.tile.TileFlugelHead;
import alfheim.common.core.registry.AlfheimItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockSkull;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class BlockFlugelHead extends BlockSkull {

	public BlockFlugelHead() {
		setBlockName("FlugelHeadBlock");
		setHardness(1.0F);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World p_149694_1_, int p_149694_2_, int p_149694_3_, int p_149694_4_) {
		return AlfheimItems.flugelHead;
	}

	@Override
	public void registerBlockIcons(IIconRegister p_149651_1_) {
		// NO-OP
	}

	@Override
	public ArrayList<ItemStack> getDrops(World p_149749_1_, int p_149749_2_, int p_149749_3_, int p_149749_4_, int p_149749_6_, int fortune) {
		ArrayList<ItemStack> ret = new ArrayList<ItemStack>();

		if((p_149749_6_ & 8) == 0) {
			ItemStack itemstack = new ItemStack(AlfheimItems.flugelHead, 1);
			TileEntitySkull tileentityskull = (TileEntitySkull)p_149749_1_.getTileEntity(p_149749_2_, p_149749_3_, p_149749_4_);

			if(tileentityskull == null)
				return ret;

			ret.add(itemstack);
		}
		return ret;
	}

	@Override
	public Item getItemDropped(int p_149650_1_, Random p_149650_2_, int p_149650_3_) {
		return AlfheimItems.flugelHead;
	}

	@Override
	public int getDamageValue(World p_149643_1_, int p_149643_2_, int p_149643_3_, int p_149643_4_)  {
		return 0;
	}

	@Override
	public int damageDropped(int p_149692_1_) {
		return 0;
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileFlugelHead();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int p_149691_1_, int p_149691_2_) {
		return Blocks.coal_block.getBlockTextureFromSide(p_149691_1_);
	}
}