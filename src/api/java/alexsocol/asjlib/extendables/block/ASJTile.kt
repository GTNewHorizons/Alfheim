package alexsocol.asjlib.extendables.block

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.*
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
	
	open fun writeCustomNBT(nbt: NBTTagCompound) = Unit
	
	open fun readCustomNBT(nbt: NBTTagCompound) = Unit
	
	override fun getDescriptionPacket(): Packet {
		val nbt = NBTTagCompound()
		writeCustomNBT(nbt)
		return S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, blockMetadata, nbt)
	}
	
	override fun onDataPacket(net: NetworkManager?, packet: S35PacketUpdateTileEntity) {
		super.onDataPacket(net, packet)
		readCustomNBT(packet.func_148857_g())
	}
}