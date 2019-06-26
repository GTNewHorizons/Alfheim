package alexsocol.asjlib.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Auto-completable packet. <b>Doesn't</b> require:<br>
 * <ul>
 * <li>default constructor</li>
 * <li>fromBytes implementation</li>
 * <li>toBytes implementation</li>
 * </ul>
 * */
public abstract class ASJPacket implements IMessage {
	
	public ASJPacket() {}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		fromCustomBytes(buf);
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		toCustomBytes(buf);
	}
	
	public void fromCustomBytes(ByteBuf buf) {}
	
	public void toCustomBytes(ByteBuf buf) {}
	
	public static void write(ByteBuf buf, boolean w) {
		buf.writeBoolean(w);
	} 
	
	public static void write(ByteBuf buf, byte w) {
		buf.writeByte(w);
	} 
	
	public static void write(ByteBuf buf, char w) {
		buf.writeChar(w);
	} 
	
	public static void write(ByteBuf buf, double w) {
		buf.writeDouble(w);
	} 
	
	public static void write(ByteBuf buf, float w) {
		buf.writeFloat(w);
	} 
	
	public static void write(ByteBuf buf, int w) {
		buf.writeInt(w);
	} 
	
	public static void write(ByteBuf buf, long w) {
		buf.writeLong(w);
	} 
	
	public static void write(ByteBuf buf, short w) {
		buf.writeShort(w);
	} 
	
	public static void write(ByteBuf buf, String w) {
		ByteBufUtils.writeUTF8String(buf, w);
	} 
	
	public static void write(ByteBuf buf, ItemStack w) {
		ByteBufUtils.writeItemStack(buf, w);
	} 
	
	public static void write(ByteBuf buf, NBTTagCompound w) {
		ByteBufUtils.writeTag(buf, w);
	}
	
	public static boolean readZ(ByteBuf buf) {
		return buf.readBoolean();
	}
	
	public static byte readB(ByteBuf buf) {
		return buf.readByte();
	}
	
	public static char readC(ByteBuf buf) {
		return buf.readChar();
	}
	
	public static double readD(ByteBuf buf) {
		return buf.readDouble();
	}
	
	public static float readF(ByteBuf buf) {
		return buf.readFloat();
	}
	
	public static int readI(ByteBuf buf) {
		return buf.readInt();
	}
	
	public static long readJ(ByteBuf buf) {
		return buf.readLong();
	}
	
	public static short readS(ByteBuf buf) {
		return buf.readShort();
	}
	
	public static String readLjavalangString(ByteBuf buf) {
		return ByteBufUtils.readUTF8String(buf);
	}
	
	public static ItemStack readLnetminecraftitemItemStack(ByteBuf buf) {
		return ByteBufUtils.readItemStack(buf);
	}
	
	public static NBTTagCompound readLnetminecraftnbtNBTTagCompound(ByteBuf buf) {
		return ByteBufUtils.readTag(buf);
	}
}