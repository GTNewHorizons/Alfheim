package alfheim.common.network

import cpw.mods.fml.common.network.ByteBufUtils
import cpw.mods.fml.common.network.simpleimpl.IMessage
import io.netty.buffer.ByteBuf
import net.minecraft.item.ItemStack

class MessagePlayerItem(var item: ItemStack? = null): IMessage {
	
	override fun fromBytes(buf: ByteBuf?) {
		buf?.let {
			item = ByteBufUtils.readItemStack(buf)
		}
	}
	
	override fun toBytes(buf: ByteBuf?) {
		ByteBufUtils.writeItemStack(buf, item)
	}
}