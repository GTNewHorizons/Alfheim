package alfheim.common.item.equipment.tool;

import java.awt.Color;
import java.util.List;

import gloomyfolken.hooklib.asm.Hook;
import gloomyfolken.hooklib.asm.ReturnCondition;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.core.helper.ItemNBTHelper;
import vazkii.botania.common.item.ItemTwigWand;
import vazkii.botania.common.item.ModItems;

/** This class adds new set of dreamwood wands and handles everything (I hope) */
public class ItemTwigWandExtender {
	
	public static IIcon[] icons;
	public static final String TAG_COLOR1 = "color1";
	public static final String TAG_COLOR2 = "color2";
	public static final String TAG_ELVEN = "elven";
	public static final String TAG_BIND_MODE = "bindMode";
	
	/**
	 * Core 0 - livingwood, Core 1 - dreamwwod
	 * */
	public static ItemStack forColors(int color1, int color2, boolean core) {
		ItemStack stack = new ItemStack(ModItems.twigWand);
		ItemNBTHelper.setInt(stack, TAG_COLOR1, color1);
		ItemNBTHelper.setInt(stack, TAG_COLOR2, color2);
		ItemNBTHelper.setBoolean(stack, TAG_ELVEN, core);
		
		return stack;
	}
	
	public static boolean getBindMode(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_BIND_MODE, true);
	}
	
	public static boolean isElven(ItemStack stack) {
		return ItemNBTHelper.getBoolean(stack, TAG_ELVEN, false);
	}

	public static int getColor1(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_COLOR1, 0);
	}

	public static int getColor2(ItemStack stack) {
		return ItemNBTHelper.getInt(stack, TAG_COLOR2, 0);
	}
	
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	public static void registerIcons(ItemTwigWand wand, IIconRegister par1IconRegister) {
		icons = new IIcon[5];
		for(int i = 0; i < icons.length; i++)
			icons[i] = IconHelper.forItem(par1IconRegister, wand, i);
	}
	
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	public static IIcon getIcon(ItemTwigWand wand, ItemStack stack, int pass) {
		if(pass == 3 && !getBindMode(stack)) pass = 0;
		if(pass == 4 && !isElven(stack)) pass = 0;
		if(pass == 0 && isElven(stack)) pass = 4;
		
		return icons[Math.min(icons.length - 1, pass)];
	}
	
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	public static int getRenderPasses(ItemTwigWand wand, int metadata) {
		return 5;
	}
	
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	public static void getSubItems(ItemTwigWand wand, Item par1, CreativeTabs par2CreativeTabs, List par3List) {
		for(int i = 0; i < 16; i++) par3List.add(forColors(i, i, false));
		for(int i = 0; i < 16; i++) par3List.add(forColors(i, i, true));
	}
	
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	public static int getColorFromItemStack(ItemTwigWand wand, ItemStack par1ItemStack, int par2) {
		if(par2 == 0 || par2 == 3 || par2 == 4) return 0xFFFFFF;

		float[] color = EntitySheep.fleeceColorTable[par2 == 1 ? getColor1(par1ItemStack) : getColor2(par1ItemStack)];
		return new Color(color[0], color[1], color[2]).getRGB();
	}
}
