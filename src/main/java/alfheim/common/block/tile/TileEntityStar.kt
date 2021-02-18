package alfheim.common.block.tile

import alexsocol.asjlib.F
import alexsocol.asjlib.extendables.block.ASJTile
import net.minecraft.nbt.NBTTagCompound
import vazkii.botania.common.integration.coloredlights.ColoredLightHelper

/**
 * @author WireSegal
 * Created at 9:32 PM on 2/6/16.
 */
class TileEntityStar: ASJTile() {
	
	private val TAG_COLOR = "color"
	private val TAG_SIZE = "size"
	var starColor = -1
	var size = 0.05f
	
	override fun writeCustomNBT(nbt: NBTTagCompound) {
		nbt.setInteger(TAG_COLOR, starColor)
		nbt.setFloat(TAG_SIZE, size)
	}
	
	override fun readCustomNBT(nbt: NBTTagCompound) {
		starColor = nbt.getInteger(TAG_COLOR)
		size = nbt.getFloat(TAG_SIZE)
	}
	
	fun getColor() = starColor
	
	fun getLightColor(): Int {
		val r = (getColor() shr 16 and 255).F / 255f
		val g = (getColor() shr 8 and 255).F / 255f
		val b = (getColor() and 255).F / 255f
		return ColoredLightHelper.makeRGBLightValue(r, g, b, 1f)
	}
}
