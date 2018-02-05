package alexsocol.asjlib;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class ItemContainingTileEntity extends TileEntity {

	public ItemStack item;

	public ItemStack getItem() {
		return this.item;
	}

	public ItemStack setItem(ItemStack it) {
		return this.item = it;
	}

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
		NBTTagCompound compound = new NBTTagCompound();
		nbt.setTag("item", compound);
		if (getItem() != null)
			getItem().writeToNBT(compound);
	}

	public void readCustomNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("item"))
			setItem(ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("item")));
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

	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox() {
		return INFINITE_EXTENT_AABB;
	}
}
