package alfheim.common.core.asm.hook.extender

import gloomyfolken.hooklib.asm.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.passive.EntitySheep
import net.minecraft.item.*
import net.minecraft.util.IIcon
import vazkii.botania.client.core.helper.IconHelper
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.item.*
import java.awt.Color
import kotlin.math.min

/** This class adds new set of dreamwood wands and handles everything (I hope)  */
@Suppress("UNUSED_PARAMETER")
object ItemTwigWandExtender {
	
	lateinit var icons: Array<IIcon>
	const val TAG_COLOR1 = "color1"
	const val TAG_COLOR2 = "color2"
	const val TAG_ELVEN = "elven"
	const val TAG_BIND_MODE = "bindMode"
	
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
	
	fun getBindMode(stack: ItemStack) = ItemNBTHelper.getBoolean(stack, TAG_BIND_MODE, true)
	
	fun isElven(stack: ItemStack) = ItemNBTHelper.getBoolean(stack, TAG_ELVEN, false)
	
	fun getColor1(stack: ItemStack) = ItemNBTHelper.getInt(stack, TAG_COLOR1, 0)
	
	fun getColor2(stack: ItemStack) = ItemNBTHelper.getInt(stack, TAG_COLOR2, 0)
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	fun registerIcons(wand: ItemTwigWand, reg: IIconRegister) {
		icons = Array(5) { IconHelper.forItem(reg, wand, it) }
	}
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	fun getIcon(wand: ItemTwigWand, stack: ItemStack, pass: Int): IIcon {
		var p = pass
		if (p == 3 && !getBindMode(stack)) p = 0
		if (p == 4 && !isElven(stack)) p = 0
		if (p == 0 && isElven(stack)) p = 4
		
		return icons[min(icons.size - 1, p)]
	}
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	fun getRenderPasses(wand: ItemTwigWand, metadata: Int) = 5
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	fun getSubItems(wand: ItemTwigWand, par1: Item, tab: CreativeTabs?, list: MutableList<Any?>) {
		for (i in 0..15) list.add(forColors(i, i, false))
		for (i in 0..15) list.add(forColors(i, i, true))
	}
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS)
	fun getColorFromItemStack(wand: ItemTwigWand, par1ItemStack: ItemStack, pass: Int): Int {
		if (pass == 0 || pass == 3 || pass == 4) return 0xFFFFFF
		
		val color = EntitySheep.fleeceColorTable[if (pass == 1) getColor1(par1ItemStack) else getColor2(par1ItemStack)]
		return Color(color[0], color[1], color[2]).rgb
	}
}
