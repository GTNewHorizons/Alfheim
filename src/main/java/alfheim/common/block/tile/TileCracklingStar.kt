package alfheim.common.block.tile

import alexsocol.asjlib.*
import alexsocol.asjlib.extendables.block.ASJTile
import alexsocol.asjlib.math.Vector3
import net.minecraft.nbt.NBTTagCompound
import vazkii.botania.common.Botania
import vazkii.botania.common.integration.coloredlights.ColoredLightHelper
import java.awt.Color
import java.util.*

class TileCracklingStar: ASJTile() {
	
	var pos: Vector3 = Vector3(0.0, -1.0, 0.0)
	val rand = Random()
	
	private val TAG_COLOR = "color"
	private val TAG_SIZE = "size"
	var color = -1
	var size = 0.05f
	
	override fun writeCustomNBT(nbt: NBTTagCompound) {
		nbt.setInteger(TAG_COLOR, color)
		nbt.setFloat(TAG_SIZE, size)
		nbt.setDouble("toX", pos.x)
		nbt.setDouble("toY", pos.y)
		nbt.setDouble("toZ", pos.z)
	}
	
	override fun readCustomNBT(nbt: NBTTagCompound) {
		color = nbt.getInteger(TAG_COLOR)
		size = nbt.getFloat(TAG_SIZE)
		pos = Vector3(nbt.getDouble("toX"), nbt.getDouble("toY"), nbt.getDouble("toZ"))
	}
	
	fun getLightColor(): Int {
		val r = (color shr 16 and 255).F / 255f
		val g = (color shr 8 and 255).F / 255f
		val b = (color and 255).F / 255f
		return ColoredLightHelper.makeRGBLightValue(r, g, b, 1f)
	}
	
	override fun updateEntity() {
		if (worldObj.isRemote) {
			val cur = Vector3.fromTileEntity(this)
			
			if (pos.y != -1.0 && pos != cur) {
				val vec = Vector3(pos).sub(Vector3.fromTileEntity(this))
				wispLine(Vector3.fromTileEntity(this).add(0.5 + (Math.random() - 0.5) * 0.05, 0.5 + (Math.random() - 0.5) * 0.05, 0.5 + (Math.random() - 0.5) * 0.05), vec, colorFromInt(color), Math.random() * 6.0, 10)
				wispLine(Vector3(pos).add(0.5 + (Math.random() - 0.5) * 0.05, 0.5 + (Math.random() - 0.5) * 0.05, 0.5 + (Math.random() - 0.5) * 0.05), vec.negate(), colorFromInt(color), Math.random() * 6.0, 10)
			} else {
				val c = Color(colorFromIntAndPos(color, cur))
				Botania.proxy.wispFX(worldObj, cur.x + 0.5, cur.y + 0.5, cur.z + 0.5, c.red / 255f, c.green / 255f, c.blue / 255f, 0.25f)
			}
		} else {
			val other = worldObj.getTileEntity(pos.x.mfloor(), pos.y.mfloor(), pos.z.mfloor()) as? TileCracklingStar
			if (other == null) {
				pos.set(0, -1, 0)
				ASJUtilities.dispatchTEToNearbyPlayers(this)
			}
		}
	}
	
	fun colorFromInt(color: Int): Int = if (color == -1) rainbow(1f) else color
	fun colorFromIntAndPos(color: Int, pos: Vector3) = if (color == -1) rainbow(pos, 1f) else color
	
	fun rainbow(saturation: Float) = Color.HSBtoRGB((Botania.proxy.worldElapsedTicks * 2L % 360L).F / 360f, saturation, 1f)
	fun rainbow(pos: Vector3, saturation: Float): Int {
		val ticks = (Botania.proxy.worldElapsedTicks * 2L % 360L).F / 360f
		val seed = ((pos.x.mfloor() xor pos.y.mfloor() xor pos.z.mfloor()) * 255 xor pos.hashCode()) % 360f / 360f
		return Color.HSBtoRGB(seed + ticks, saturation, 1F)
	}
	
	fun wispLine(start: Vector3, line: Vector3, color: Int, stepsPerBlock: Double, time: Int) {
		val len = line.length()
		val ray = line.copy().mul(1 / len)
		val steps = (len * stepsPerBlock).I
		
		for (i in 0 until steps) {
			val extended = ray.copy().mul(i / stepsPerBlock)
			val x = start.x + extended.x
			val y = start.y + extended.y
			val z = start.z + extended.z
			
			val c = Color(color)
			
			val r = c.red.F / 255f
			val g = c.green.F / 255f
			val b = c.blue.F / 255f
			
			Botania.proxy.wispFX(worldObj, x, y, z, r, g, b, time * 0.0125f)
		}
	}
}