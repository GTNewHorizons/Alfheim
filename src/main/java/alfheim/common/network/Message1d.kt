package alfheim.common.network

import alexsocol.asjlib.network.ASJPacket
import alfheim.client.core.handler.PacketHandlerClient
import cpw.mods.fml.common.network.simpleimpl.*

class Message1d(ty: m1d, val data1: Double): ASJPacket() {
	
	val type: Int
	
	init {
		type = ty.ordinal
	}
	
	enum class m1d {
		DEATH_TIMER, TIME_STOP_REMOVE, KNOWLEDGE, CL_SLOWDOWN
	}
	
	class Handler: IMessageHandler<Message1d, IMessage> {
		
		override fun onMessage(packet: Message1d, message: MessageContext): IMessage? {
			PacketHandlerClient.handle(packet)
			return null
		}
	}
}
