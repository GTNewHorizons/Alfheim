package alfheim.common.block.compat.thaumcraft;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import alfheim.common.integration.thaumcraft.ThaumcraftAlfheimModule;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.common.config.ConfigItems;

public class BlockAlfheimThaumOre extends Block {
	
	public static final IIcon[] icon = new IIcon[4];
	public final Random rand = new Random();
	
	public BlockAlfheimThaumOre() {
		super(Material.rock);
		setBlockName("blockCustomOre");
		setCreativeTab(ThaumcraftAlfheimModule.tcnTab);
		setHardness(1.5F);
		setHarvestLevel("pickaxe", 2, 0);
		setHarvestLevel("pickaxe", 2, 7);
		setResistance(5);
		setStepSound(soundTypeStone);
		setTickRandomly(true);
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister ir) {
		icon[0] = ir.registerIcon("thaumcraft:cinnibarAlf");
		icon[1] = ir.registerIcon("thaumcraft:infusedorestoneAlf");
		icon[2] = ir.registerIcon("thaumcraft:infusedore");
		icon[3] = ir.registerIcon("thaumcraft:amberoreAlf");
	}
	
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta) {
		return meta == 0 ? icon[0] : (meta == 7 ? icon[3] : icon[1]);
	}
	
	public boolean canSilkHarvest(World world, EntityPlayer player, int x, int y, int z, int metadata) {
		return true;
	}
	
	public int damageDropped(int meta) {
		return meta;
	}
	
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item item, CreativeTabs tab, List subs) {
		subs.add(new ItemStack(item, 1, 0));
		subs.add(new ItemStack(item, 1, 1));
		subs.add(new ItemStack(item, 1, 2));
		subs.add(new ItemStack(item, 1, 3));
		subs.add(new ItemStack(item, 1, 4));
		subs.add(new ItemStack(item, 1, 5));
		subs.add(new ItemStack(item, 1, 6));
		subs.add(new ItemStack(item, 1, 7));
	}
	
	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(World worldObj, MovingObjectPosition target, EffectRenderer effectRenderer) {
		int meta = worldObj.getBlockMetadata(target.blockX, target.blockY, target.blockZ);
		
		if (0 < meta && meta < 6) {
			UtilsFX.infusedStoneSparkle(worldObj, target.blockX, target.blockY, target.blockZ, meta);
		}
		
		return super.addHitEffects(worldObj, target, effectRenderer);
	}
	
	public ArrayList getDrops(World world, int x, int y, int z, int meta, int fortune) {
		ArrayList ret = new ArrayList();
		if (meta == 0) {
			ret.add(new ItemStack(ThaumcraftAlfheimModule.alfheimThaumOre, 1, 0));
		} else if (meta == 7) {
			ret.add(new ItemStack(ConfigItems.itemResource, 1 + world.rand.nextInt(fortune + 1), 6));
		} else {
			int q = 1 + world.rand.nextInt(2 + fortune);
			
			for (int a = 0; a < q; ++a) {
				ret.add(new ItemStack(ConfigItems.itemShard, 1, meta - 1));
			}
		}
		
		return ret;
	}
	
	public int getExpDrop(IBlockAccess world, int meta, int fortune) {
		return	meta != 0 && meta != 7 ? MathHelper.getRandomIntegerInRange(rand, 0, 3) :
				meta == 7 ? MathHelper.getRandomIntegerInRange(rand, 1, 4) :
		super.getExpDrop(world, meta, fortune);
	}
	
	public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side) {
		return true;
	}
	
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	public int getRenderType() {
		return ThaumcraftAlfheimModule.renderIDOre;
	}
}