package alexsocol.asjlib.network

import alexsocol.asjlib.I
import cpw.mods.fml.common.network.ByteBufUtils
import cpw.mods.fml.common.network.simpleimpl.IMessage
import io.netty.buffer.ByteBuf
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

/**
 * Auto-completable packet. **Doesn't** require:<br></br>
 *
 *  * default constructor
 *  * fromBytes implementation
 *  * toBytes implementation
 *
 */
abstract class ASJPacket: IMessage {
	
	override fun fromBytes(buf: ByteBuf) {
		fromCustomBytes(buf)
	}
	
	override fun toBytes(buf: ByteBuf) {
		toCustomBytes(buf)
	}
	
	open fun fromCustomBytes(buf: ByteBuf) {}
	
	open fun toCustomBytes(buf: ByteBuf) {}
	
	companion object {
		
		@JvmStatic
		fun write(buf: ByteBuf, w: Boolean) {
			buf.writeBoolean(w)
		}
		
		@JvmStatic
		fun write(buf: ByteBuf, w: Byte) {
			buf.writeByte(w.I)
		}
		
		@JvmStatic
		fun write(buf: ByteBuf, w: Char) {
			buf.writeChar(w.toInt())
		}
		
		@JvmStatic
		fun write(buf: ByteBuf, w: Double) {
			buf.writeDouble(w)
		}
		
		@JvmStatic
		fun write(buf: ByteBuf, w: Float) {
			buf.writeFloat(w)
		}
		
		@JvmStatic
		fun write(buf: ByteBuf, w: Int) {
			buf.writeInt(w)
		}
		
		@JvmStatic
		fun write(buf: ByteBuf, w: Long) {
			buf.writeLong(w)
		}
		
		@JvmStatic
		fun write(buf: ByteBuf, w: Short) {
			buf.writeShort(w.I)
		}
		
		@JvmStatic
		fun write(buf: ByteBuf, w: String?) {
			ByteBufUtils.writeUTF8String(buf, w)
		}
		
		@JvmStatic
		fun write(buf: ByteBuf, w: ItemStack?) {
			ByteBufUtils.writeItemStack(buf, w)
		}
		
		@JvmStatic
		fun write(buf: ByteBuf, w: NBTTagCompound?) {
			ByteBufUtils.writeTag(buf, w)
		}
		
		@JvmStatic
		fun readZ(buf: ByteBuf): Boolean {
			return buf.readBoolean()
		}
		
		@JvmStatic
		fun readB(buf: ByteBuf): Byte {
			return buf.readByte()
		}
		
		@JvmStatic
		fun readC(buf: ByteBuf): Char {
			return buf.readChar()
		}
		
		@JvmStatic
		fun readD(buf: ByteBuf): Double {
			return buf.readDouble()
		}
		
		@JvmStatic
		fun readF(buf: ByteBuf): Float {
			return buf.readFloat()
		}
		
		@JvmStatic
		fun readI(buf: ByteBuf): Int {
			return buf.readInt()
		}
		
		@JvmStatic
		fun readJ(buf: ByteBuf): Long {
			return buf.readLong()
		}
		
		@JvmStatic
		fun readS(buf: ByteBuf): Short {
			return buf.readShort()
		}
		
		@JvmStatic
		fun readLjavalangString(buf: ByteBuf): String? {
			return ByteBufUtils.readUTF8String(buf)
		}
		
		@JvmStatic
		fun readLnetminecraftitemItemStack(buf: ByteBuf): ItemStack? {
			return ByteBufUtils.readItemStack(buf)
		}
		
		@JvmStatic
		fun readLnetminecraftnbtNBTTagCompound(buf: ByteBuf): NBTTagCompound? {
			return ByteBufUtils.readTag(buf)
		}
	}
}