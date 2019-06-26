package alexsocol.asjlib.network

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
		
		fun write(buf: ByteBuf, w: Boolean) {
			buf.writeBoolean(w)
		}
		
		fun write(buf: ByteBuf, w: Byte) {
			buf.writeByte(w.toInt())
		}
		
		fun write(buf: ByteBuf, w: Char) {
			buf.writeChar(w.toInt())
		}
		
		fun write(buf: ByteBuf, w: Double) {
			buf.writeDouble(w)
		}
		
		fun write(buf: ByteBuf, w: Float) {
			buf.writeFloat(w)
		}
		
		fun write(buf: ByteBuf, w: Int) {
			buf.writeInt(w)
		}
		
		fun write(buf: ByteBuf, w: Long) {
			buf.writeLong(w)
		}
		
		fun write(buf: ByteBuf, w: Short) {
			buf.writeShort(w.toInt())
		}
		
		fun write(buf: ByteBuf, w: String) {
			ByteBufUtils.writeUTF8String(buf, w)
		}
		
		fun write(buf: ByteBuf, w: ItemStack) {
			ByteBufUtils.writeItemStack(buf, w)
		}
		
		fun write(buf: ByteBuf, w: NBTTagCompound) {
			ByteBufUtils.writeTag(buf, w)
		}
		
		fun readZ(buf: ByteBuf): Boolean {
			return buf.readBoolean()
		}
		
		fun readB(buf: ByteBuf): Byte {
			return buf.readByte()
		}
		
		fun readC(buf: ByteBuf): Char {
			return buf.readChar()
		}
		
		fun readD(buf: ByteBuf): Double {
			return buf.readDouble()
		}
		
		fun readF(buf: ByteBuf): Float {
			return buf.readFloat()
		}
		
		fun readI(buf: ByteBuf): Int {
			return buf.readInt()
		}
		
		fun readJ(buf: ByteBuf): Long {
			return buf.readLong()
		}
		
		fun readS(buf: ByteBuf): Short {
			return buf.readShort()
		}
		
		fun readLjavalangString(buf: ByteBuf): String {
			return ByteBufUtils.readUTF8String(buf)
		}
		
		fun readLnetminecraftitemItemStack(buf: ByteBuf): ItemStack {
			return ByteBufUtils.readItemStack(buf)
		}
		
		fun readLnetminecraftnbtNBTTagCompound(buf: ByteBuf): NBTTagCompound {
			return ByteBufUtils.readTag(buf)
		}
	}
}