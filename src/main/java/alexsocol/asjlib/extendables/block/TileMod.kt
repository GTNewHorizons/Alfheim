package alexsocol.asjlib.extendables.block

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.*
import net.minecraft.network.play.server.S35PacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity

open class TileMod: TileEntity() {
	
	override fun writeToNBT(nbt: NBTTagCompound) {
		super.writeToNBT(nbt)
		writeCustomNBT(nbt)
	}
	
	override fun readFromNBT(nbt: NBTTagCompound) {
		super.readFromNBT(nbt)
		readCustomNBT(nbt)
	}
	
	open fun writeCustomNBT(nbt: NBTTagCompound) = Unit // NO-OP
	
	open fun readCustomNBT(nbt: NBTTagCompound) = Unit // NO-OP
	
	override fun getDescriptionPacket(): Packet {
		val nbt = NBTTagCompound()
		writeCustomNBT(nbt)
		return S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, -999, nbt)
	}
	
	override fun onDataPacket(net: NetworkManager, packet: S35PacketUpdateTileEntity) {
		super.onDataPacket(net, packet)
		readCustomNBT(packet.func_148857_g())
	}
}