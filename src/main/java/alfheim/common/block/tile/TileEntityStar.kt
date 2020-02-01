package alfheim.common.block.tile

import alfheim.common.core.util.F
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.*
import net.minecraft.network.play.server.S35PacketUpdateTileEntity
import vazkii.botania.common.block.tile.TileMod
import vazkii.botania.common.integration.coloredlights.ColoredLightHelper

/**
 * @author WireSegal
 * Created at 9:32 PM on 2/6/16.
 */
class TileEntityStar: TileMod() {
	
	private val TAG_COLOR = "color"
	private val TAG_SIZE = "size"
	var starColor = -1
	var size = 0.05f
	
	override fun writeCustomNBT(nbttagcompound: NBTTagCompound) {
		nbttagcompound.setInteger(TAG_COLOR, starColor)
		nbttagcompound.setFloat(TAG_SIZE, size)
	}
	
	override fun readCustomNBT(nbttagcompound: NBTTagCompound) {
		starColor = nbttagcompound.getInteger(TAG_COLOR)
		size = nbttagcompound.getFloat(TAG_SIZE)
	}
	
	fun getColor() = starColor
	
	fun getLightColor(): Int {
		val r = (getColor() shr 16 and 255).F / 255f
		val g = (getColor() shr 8 and 255).F / 255f
		val b = (getColor() and 255).F / 255f
		return ColoredLightHelper.makeRGBLightValue(r, g, b, 1f)
	}
	
	override fun getDescriptionPacket(): Packet {
		val nbttagcompound = NBTTagCompound()
		writeCustomNBT(nbttagcompound)
		return S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, -999, nbttagcompound)
	}
	
	override fun onDataPacket(net: NetworkManager?, pkt: S35PacketUpdateTileEntity?) {
		super.onDataPacket(net, pkt)
		readCustomNBT(pkt!!.func_148857_g())
	}
}
