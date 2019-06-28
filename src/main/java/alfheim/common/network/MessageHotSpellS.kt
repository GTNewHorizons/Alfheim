package alfheim.common.network

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.network.ASJPacket
import alfheim.common.core.handler.CardinalSystem.HotSpellsSystem
import cpw.mods.fml.common.network.simpleimpl.*
import io.netty.buffer.ByteBuf

class MessageHotSpellS(var slot: Int, var id: Int): ASJPacket() {
	
	override fun fromBytes(buf: ByteBuf) {
		if (!ASJUtilities.isServer) return
		slot = buf.readInt()
		id = buf.readInt()
	}
	
	class Handler: IMessageHandler<MessageHotSpellS, IMessage> {
		
		override fun onMessage(packet: MessageHotSpellS, message: MessageContext): IMessage? {
			HotSpellsSystem.setHotSpellID(message.serverHandler.playerEntity, packet.slot, packet.id)
			return null
		}
	}
}
