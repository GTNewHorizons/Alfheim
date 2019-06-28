package alfheim.common.network

import alexsocol.asjlib.network.ASJPacket
import alfheim.client.core.handler.PacketHandlerClient
import cpw.mods.fml.common.network.simpleimpl.*

class Message2d(ty: m2d, val data1: Double, val data2: Double): ASJPacket() {
	
	val type: Int
	
	init {
		type = ty.ordinal
	}
	
	enum class m2d {
		UUID, COOLDOWN, ATTRIBUTE, MODES
	}
	
	class Handler: IMessageHandler<Message2d, IMessage> {
		
		override fun onMessage(packet: Message2d, message: MessageContext): IMessage? {
			PacketHandlerClient.handle(packet)
			return null
		}
	}
}
