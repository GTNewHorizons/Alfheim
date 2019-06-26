package alfheim.common.item.equipment.tool

import java.awt.Color

import gloomyfolken.hooklib.asm.Hook
import gloomyfolken.hooklib.asm.ReturnCondition
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.passive.EntitySheep
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import vazkii.botania.client.core.helper.IconHelper
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.item.ItemTwigWand
import vazkii.botania.common.item.ModItems

/** This class adds new set of dreamwood wands and handles everything (I hope)  */
object ItemTwigWandExtender {
	
	var icons: Array<IIcon>
	val TAG_COLOR1 = "color1"
	val TAG_COLOR2 = "color2"
	val TAG_ELVEN = "elven"
	val TAG_BIND_MODE = "bindMode"
	
	/**
	 * Core 0 - livingwood, Core 1 - dreamwwod
	 */
	fun forColors(color1: Int, color2: Int, core: Boolean): ItemStack {
		val stack = ItemStack(ModItems.twigWand)
		ItemNBTHelper.setInt(stack, TAG_COLOR1, color1)
		ItemNBTHelper.setInt(stack, TAG_COLOR2, color2)
		ItemNBTHelper.setBoolean(stack, TAG_ELVEN, core)
		
		return stack
	}
	
	fun getBindMode(stack: ItemStack): Boolean {
		return ItemNBTHelper.getBoolean(stack, TAG_BIND_MODE, true)
	}
	
	fun isElven(stack: ItemStack): Boolean {
		return ItemNBTHelper.getBoolean(stack, TAG_ELVEN, false)
	}
	
	fun getColor1(stack: ItemStack): Int {
		return ItemNBTHelper.getInt(stack, TAG_COLOR1, 0)
	}
	
	fun getColor2(stack: ItemStack): Int {
		return ItemNBTHelper.getInt(stack, TAG_COLOR2, 0)
	}
	
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	fun registerIcons(wand: ItemTwigWand, par1IconRegister: IIconRegister) {
		icons = arrayOfNulls(5)
		for (i in icons.indices)
			icons[i] = IconHelper.forItem(par1IconRegister, wand, i)
	}
	
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	fun getIcon(wand: ItemTwigWand, stack: ItemStack, pass: Int): IIcon {
		var pass = pass
		if (pass == 3 && !getBindMode(stack)) pass = 0
		if (pass == 4 && !isElven(stack)) pass = 0
		if (pass == 0 && isElven(stack)) pass = 4
		
		return icons[Math.min(icons.size - 1, pass)]
	}
	
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	fun getRenderPasses(wand: ItemTwigWand, metadata: Int): Int {
		return 5
	}
	
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	fun getSubItems(wand: ItemTwigWand, par1: Item, par2CreativeTabs: CreativeTabs, par3List: MutableList<*>) {
		for (i in 0..15) par3List.add(forColors(i, i, false))
		for (i in 0..15) par3List.add(forColors(i, i, true))
	}
	
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	fun getColorFromItemStack(wand: ItemTwigWand, par1ItemStack: ItemStack, par2: Int): Int {
		if (par2 == 0 || par2 == 3 || par2 == 4) return 0xFFFFFF
		
		val color = EntitySheep.fleeceColorTable[if (par2 == 1) getColor1(par1ItemStack) else getColor2(par1ItemStack)]
		return Color(color[0], color[1], color[2]).rgb
	}
}
