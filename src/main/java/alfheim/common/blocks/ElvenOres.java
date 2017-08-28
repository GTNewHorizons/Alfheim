package alfheim.common.blocks;

import java.util.List;
import java.util.Random;

import alfheim.AlfheimCore;
import alfheim.ModInfo;
import alfheim.common.registry.AlfheimBlocks;
import alfheim.common.registry.AlfheimItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import vazkii.botania.common.item.ModItems;

public class ElvenOres extends Block {

	public static final String[] names = { "Dragonstone", "Elementium", "Quartz", "Gold", "Iffesal"};
	public IIcon[] textures = new IIcon[names.length];
	public Item[] drops = { ModItems.manaResource, Item.getItemFromBlock(AlfheimBlocks.elvenOres), ModItems.quartz, Item.getItemFromBlock(AlfheimBlocks.elvenOres), AlfheimItems.elvenResource };
	public int[] metas = {9, 1, 5, 3, 9};
	
	public ElvenOres() {
		super(Material.rock);
		this.setBlockName("ElvenOre");
        this.setCreativeTab(AlfheimCore.alfheimTab);
        this.setHardness(2);
        this.setHarvestLevel("pickaxe", 2);
        this.setResistance(600);
        this.setStepSound(soundTypeStone);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister reg) {
		for (int i = 0; i < names.length; i++) {
			textures[i] = reg.registerIcon(ModInfo.MODID + ':' + names[i] + "OreElven");
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
		if (meta >= drops.length || meta < 0) return drops[0];
		return drops[meta];
	}
	
	@Override
	public int damageDropped(int meta) {
		return metas[meta];
	}
}