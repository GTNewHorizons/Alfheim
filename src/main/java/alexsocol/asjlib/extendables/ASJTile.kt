package alexsocol.asjlib.extendables

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.NetworkManager
import net.minecraft.network.Packet
import net.minecraft.network.play.server.S35PacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity

open class ASJTile: TileEntity() {
	
	override fun writeToNBT(nbt: NBTTagCompound) {
		super.writeToNBT(nbt)
		writeCustomNBT(nbt)
	}
	
	override fun readFromNBT(nbt: NBTTagCompound) {
		super.readFromNBT(nbt)
		readCustomNBT(nbt)
	}
	
	open fun writeCustomNBT(nbt: NBTTagCompound) {
	
	}
	
	open fun readCustomNBT(nbt: NBTTagCompound) {
	
	}
	
	override fun getDescriptionPacket(): Packet {
		val nbt = NBTTagCompound()
		writeCustomNBT(nbt)
		return S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, blockMetadata, nbt)
	}
	
	override fun onDataPacket(net: NetworkManager?, packet: S35PacketUpdateTileEntity?) {
		super.onDataPacket(net, packet)
		readCustomNBT(packet!!.func_148857_g())
	}
}