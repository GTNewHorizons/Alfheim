package alfheim.common.blocks;

import java.util.List;
import java.util.Random;

import alfheim.AlfheimCore;
import alfheim.Constants;
import alfheim.common.registry.AlfheimBlocks;
import alfheim.common.registry.AlfheimItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import vazkii.botania.common.item.ModItems;

public class ElvenOres extends Block {

	public static final String[] names = { "Dragonstone", "Elementium", "Quartz", "Gold", "Iffesal" };
	public IIcon[] textures = new IIcon[names.length];
	public Item[] drops = { ModItems.manaResource, Item.getItemFromBlock(AlfheimBlocks.elvenOres), ModItems.quartz, Item.getItemFromBlock(AlfheimBlocks.elvenOres), AlfheimItems.elvenResource };
	public int[] metas = {9, 1, 5, 3, 9};
	public Random rand = new Random();
	
	public ElvenOres() {
		super(Material.rock);
		this.setBlockName("ElvenOre");
        this.setCreativeTab(AlfheimCore.alfheimTab);
        this.setHardness(2);
        this.setHarvestLevel("pickaxe", 2);
        this.setResistance(5.0F);
        this.setStepSound(soundTypeStone);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister reg) {
		for (int i = 0; i < names.length; i++) {
			textures[i] = reg.registerIcon(Constants.MODID + ':' + names[i] + "OreElven");
		}
	}

	@Override
	public void getSubBlocks(Item block, CreativeTabs tab, List list) {
		for (int i = 0; i < names.length; i++) {
			list.add(new ItemStack(block, 1, i));
		}
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		if (meta >= textures.length || meta < 0) return textures[0];
		return textures[meta];
	}

	@Override
	public Item getItemDropped(int meta, Random rand, int fortune) {
		Item drop = drops[meta];
		if (meta >= drops.length || meta < 0) drop = drops[0];
		System.out.println((drops[1] == null) + " - " + (drops[3] == null));
		return drop;
	}
	
	@Override
	public int damageDropped(int meta) {
		if (meta >= metas.length || meta < 0) return metas[0];
		return metas[meta];
	}

	@Override
	public int getExpDrop(IBlockAccess world, int meta, int fortune) {
		return (meta == 0 || meta == 2 || meta == 4) ? MathHelper.getRandomIntegerInRange(rand, 3, 7) : 0;
	}
}