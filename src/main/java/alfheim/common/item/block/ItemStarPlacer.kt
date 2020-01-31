package alfheim.common.item.block

import alfheim.api.ModInfo
import alfheim.common.block.AlfheimBlocks
import alfheim.common.block.tile.TileEntityStar
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
class ItemStarPlacer: ItemMod("starPlacer") {
	
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
			val stack = ItemStack(AlfheimItems.starPlacer)
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
	
	override fun onItemUse(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, direction: Int, par8: Float, par9: Float, par10: Float): Boolean {
		if (world.isRemote) return false
		
		val toPlace = ItemStack(AlfheimBlocks.starBlock)
		val dir = ForgeDirection.getOrientation(direction)
		if (world.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ).isAir(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ)) {
			toPlace.tryPlaceItemIntoWorld(player, world, x, y, z, direction, par8, par9, par10)
			if (toPlace.stackSize == 0) {
				val tile = world.getTileEntity(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ)
				if (tile is TileEntityStar) {
					tile.starColor = getColor(stack)
					tile.size = getSize(stack)
				}
				if (!player.capabilities.isCreativeMode) stack.stackSize--
				
				player.swingItem()
			}
			return true
		}
		return false
	}
}
