package alfheim.common.item.block

import alfheim.api.ModInfo
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.tile.TileCracklingStar
import alfheim.common.item.*
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.passive.EntitySheep
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.util.StatCollector
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import vazkii.botania.common.core.helper.ItemNBTHelper
import java.awt.Color
import java.util.*

/**
 * @author WireSegal
 * Created at 9:55 PM on 2/6/16.
 */
class ItemStarPlacer2: ItemMod("starPlacer2") {
	
	companion object {
		const val TAG_COLOR = "color"
		const val TAG_SIZE = "size"
		
		val defaultColors = ArrayList<Int>()
		
		init {
			for (color in EntitySheep.fleeceColorTable) {
				var colorint = Color(color[0], color[1], color[2]).rgb
				colorint += 0xFFFFFF + 1
				defaultColors.add(colorint)
			}
			defaultColors.add(-1)
		}
		
		fun setColor(stack: ItemStack, color: Int) {
			ItemNBTHelper.setInt(stack, TAG_COLOR, color)
		}
		
		fun getColor(stack: ItemStack) = ItemNBTHelper.getInt(stack, TAG_COLOR, -1)
		
		fun setSize(stack: ItemStack, size: Float) {
			ItemNBTHelper.setFloat(stack, TAG_SIZE, size)
		}
		
		fun getSize(stack: ItemStack) = ItemNBTHelper.getFloat(stack, TAG_SIZE, 0.05f)
		
		fun colorStack(color: Int): ItemStack {
			val stack = ItemStack(AlfheimItems.starPlacer2)
            setColor(stack, color)
			return stack
		}
		
		fun forColor(colorVal: Int) = colorStack(defaultColors[colorVal % defaultColors.size])
	}
	
	override fun addInformation(stack: ItemStack, player: EntityPlayer?, list: MutableList<Any?>, par4: Boolean) {
		super.addInformation(stack, player, list, par4)
		val color = getColor(stack)
		if (color in defaultColors)
			list.add(StatCollector.translateToLocal("misc.${ModInfo.MODID}.color.${defaultColors.indexOf(color)}"))
		else
			list.add("#${Integer.toHexString(color).toUpperCase()}")
		if (getSize(stack) != 0.05f)
			list.add(StatCollector.translateToLocalFormatted("misc.${ModInfo.MODID}.customSize", getSize(stack) / 0.1f))
	}
	
	override fun getSubItems(item: Item, tab: CreativeTabs?, list: MutableList<Any?>) {
		defaultColors.mapTo(list) { colorStack(it) }
	}
	
	override fun getColorFromItemStack(stack: ItemStack, pass: Int): Int {
		val color = getColor(stack)
		if (color == -1) return ItemIridescent.rainbowColor()
		return color
	}
	
	override fun onItemUse(par1ItemStack: ItemStack, par2EntityPlayer: EntityPlayer, par3World: World, x: Int, y: Int, z: Int, direction: Int, par8: Float, par9: Float, par10: Float): Boolean {
		if (par3World.isRemote) return false
		
		val toPlace = ItemStack(AlfheimBlocks.starBlock2)
		val dir = ForgeDirection.getOrientation(direction)
		if (par3World.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ).isAir(par3World, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ)) {
			toPlace.tryPlaceItemIntoWorld(par2EntityPlayer, par3World, x, y, z, direction, par8, par9, par10)
			if (toPlace.stackSize == 0) {
				val tile = par3World.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ)
				if (tile is TileCracklingStar) {
					tile.color = getColor(par1ItemStack)
					tile.size = getSize(par1ItemStack)
				}
				if (!par2EntityPlayer.capabilities.isCreativeMode) par1ItemStack.stackSize--
			}
			return true
		}
		return false
	}
}
