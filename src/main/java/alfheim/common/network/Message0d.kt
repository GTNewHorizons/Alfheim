package alfheim.common.network

import alexsocol.asjlib.network.ASJPacket
import alfheim.common.core.handler.PacketHandler
import cpw.mods.fml.common.network.simpleimpl.*

class Message0d(ty: m0d): ASJPacket() {
	
	val type: Int
	
	init {
		type = ty.ordinal
	}
	
	enum class m0d {
		DODGE, JUMP
	}
	
	class Handler: IMessageHandler<Message0d, IMessage> {
		
		override fun onMessage(packet: Message0d, message: MessageContext): IMessage? {
			PacketHandler.handle(packet, message)
			return null
		}
	}
}