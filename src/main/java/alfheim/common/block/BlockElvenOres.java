package alfheim.common.block;

import java.util.List;
import java.util.Random;

import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.common.core.registry.AlfheimItems;
import alfheim.common.core.registry.AlfheimItems.ElvenResourcesMetas;
import alfheim.common.lexicon.AlfheimLexiconData;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.item.ModItems;

public class BlockElvenOres extends Block implements ILexiconable {

	public static final String[] names = { "Dragonstone", "Elementium", "Quartz", "Gold", "Iffesal" };
	public IIcon[] textures = new IIcon[names.length];
	public Item[] drops = { ModItems.manaResource, null, ModItems.quartz, null, AlfheimItems.elvenResource };
	public int[] metas = {9, 1, 5, 3, ElvenResourcesMetas.IffesalDust};
	public Random rand = new Random();
	
	public BlockElvenOres() {
		super(Material.rock);
		this.setBlockName("ElvenOre");
		this.setCreativeTab(AlfheimCore.alfheimTab);
		this.setHardness(2);
		this.setHarvestLevel("pickaxe", 2);
		this.setHarvestLevel("pickaxe", 1, 1);
		this.setResistance(5.0F);
		this.setStepSound(soundTypeStone);
	}
	
	@Override
	public void registerBlockIcons(IIconRegister reg) {
		for (int i = 0; i < names.length; i++)
			textures[i] = reg.registerIcon(ModInfo.MODID + ':' + names[i] + "OreElven");
	}

	@Override
	public void getSubBlocks(Item block, CreativeTabs tab, List list) {
		for (int i = 0; i < names.length; i++)
			list.add(new ItemStack(block, 1, i));
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		if (meta >= textures.length || meta < 0) return textures[0];
		return textures[meta];
	}
	
	@Override
	public Item getItemDropped(int meta, Random rand, int fortune) {
		if (meta < 0 || drops.length <= meta) return drops[0];
		if (drops[meta] == null) return Item.getItemFromBlock(this);
		return drops[meta];
	}
	
	@Override
	public int damageDropped(int meta) {
		if (meta >= metas.length || meta < 0) return metas[0];
		return metas[meta];
	}

	@Override
	public int getDamageValue(World world, int x, int y, int z) {
		// how can damageDropped even be here if it isn't related to dropping stuff? -_-
        return world.getBlockMetadata(x, y, z);
    }
	
	@Override
	public int getExpDrop(IBlockAccess world, int meta, int fortune) {
		return Item.getItemFromBlock(this) != this.getItemDropped(meta, new Random(), fortune) ? rand.nextInt(5) + 3 : 0;
	}

	@Override
	public int quantityDropped(int meta, int fortune, Random random) {
		return meta == 2 ? Math.max(random.nextInt(fortune + 2) - 1, 0) + 1 : 1;
	}
	
	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return AlfheimLexiconData.ores;
	}
}