package alfheim.common.block.tile

import net.minecraft.nbt.NBTTagCompound
import vazkii.botania.common.Botania

class TileInvisibleManaFlame: TileManaFlame() {
	
	private val TAG_COLOR = "color"
	
	var flameColor = 0x20FF20
	
	override fun writeCustomNBT(nbt: NBTTagCompound) {
		nbt.setInteger(TAG_COLOR, flameColor)
	}
	
	override fun readCustomNBT(nbt: NBTTagCompound) {
		flameColor = nbt.getInteger(TAG_COLOR)
	}
	
	override fun getColor() = flameColor
	
	override fun shouldRender() = Botania.proxy.isClientPlayerWearingMonocle
}
