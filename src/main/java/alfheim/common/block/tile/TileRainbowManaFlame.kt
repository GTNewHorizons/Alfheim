package alfheim.common.block.tile

import alexsocol.asjlib.ASJUtilities
import net.minecraft.nbt.NBTTagCompound
import vazkii.botania.client.core.handler.ClientTickHandler
import vazkii.botania.common.Botania
import java.awt.Color
import java.util.*

class TileRainbowManaFlame: TileManaFlame() {
	
	private val TAG_INVISIBLE = "invisible"
	
	var invisible = false
	
	override fun writeCustomNBT(nbttagcompound: NBTTagCompound) {
		nbttagcompound.setBoolean(TAG_INVISIBLE, invisible)
	}
	
	override fun readCustomNBT(nbttagcompound: NBTTagCompound) {
		invisible = nbttagcompound.getBoolean(TAG_INVISIBLE)
	}
	
	override fun getColor(): Int {
		if (ASJUtilities.isServer) return 0xFFFFFF
		var time = ClientTickHandler.ticksInGame + ClientTickHandler.partialTicks
		time += Random((xCoord xor yCoord xor zCoord).toLong()).nextInt(100000)
		return Color.HSBtoRGB(time * 0.005F, 1F, 1F)
	}
	
	override fun shouldRender() = Botania.proxy.isClientPlayerWearingMonocle || !invisible
}
