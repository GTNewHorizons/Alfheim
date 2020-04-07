package alfheim.common.block.tile

import alexsocol.asjlib.*
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.*
import net.minecraft.network.play.server.S35PacketUpdateTileEntity
import vazkii.botania.common.Botania
import vazkii.botania.common.block.tile.TileMod
import vazkii.botania.common.integration.coloredlights.ColoredLightHelper

abstract class TileManaFlame: TileMod() {
	
	abstract fun getColor(): Int
	
	abstract fun shouldRender(): Boolean
	
	override fun updateEntity() {
		try {
			if (shouldRender()) {
				val c = 0.3f
				if (Math.random() < c.D) {
					val v = 0.1f
					val r = (getColor() shr 16 and 255).F / 255f + (Math.random() - 0.5).F * v
					val g = (getColor() shr 8 and 255).F / 255f + (Math.random() - 0.5).F * v
					val b = (getColor() and 255).F / 255f + (Math.random() - 0.5).F * v
					val w = 0.15f
					val h = 0.05f
					val x = xCoord.D + 0.5 + (Math.random() - 0.5) * w.D
					val y = yCoord.D + 0.25 + (Math.random() - 0.5) * h.D
					val z = zCoord.D + 0.5 + (Math.random() - 0.5) * w.D
					val s = 0.2f + Math.random().F * 0.1f
					val m = 0.03f + Math.random().F * 0.015f
					Botania.proxy.wispFX(worldObj, x, y, z, r, g, b, s, -m)
				}
			}
		} catch (e: NullPointerException) { }
	}
	
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
