package alfheim.common.network

import alfheim.client.core.handler.PacketHandlerClient
import cpw.mods.fml.common.network.simpleimpl.IMessage
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler
import cpw.mods.fml.common.network.simpleimpl.MessageContext
import io.netty.buffer.ByteBuf

class MessageHotSpellC: IMessage {
	
	val ids: IntArray
	
	constructor() {
		ids = IntArray(12)
	}
	
	constructor(ids: IntArray) {
		this.ids = ids.clone()
	}
	
	override fun toBytes(buf: ByteBuf) {
		for (id in ids) buf.writeInt(id)
	}
	
	override fun fromBytes(buf: ByteBuf) {
		for (i in ids.indices) ids[i] = buf.readInt()
	}
	
	class Handler: IMessageHandler<MessageHotSpellC, IMessage> {
		
		override fun onMessage(packet: MessageHotSpellC, message: MessageContext): IMessage? {
			PacketHandlerClient.handle(packet)
			return null
		}
	}
}
