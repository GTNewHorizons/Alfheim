package alexsocol.asjlib.extendables;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class ASJTile extends TileEntity {
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		writeCustomNBT(nbt);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		readCustomNBT(nbt);
	}
	
	public void writeCustomNBT(NBTTagCompound nbt) {
		
	}
	
	public void readCustomNBT(NBTTagCompound nbt) {
		
	}
	
	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound nbt = new NBTTagCompound();
		writeCustomNBT(nbt);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, blockMetadata, nbt);
	}
	
	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		super.onDataPacket(net, packet);
		readCustomNBT(packet.func_148857_g());
	}
}