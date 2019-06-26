package alfheim.common.network

import alexsocol.asjlib.network.ASJPacket
import alfheim.client.core.handler.PacketHandlerClient
import cpw.mods.fml.common.network.simpleimpl.IMessage
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler
import cpw.mods.fml.common.network.simpleimpl.MessageContext

class Message3d(ty: m3d, val data1: Double, val data2: Double, val data3: Double): ASJPacket() {
	
	val type: Int
	
	init {
		type = ty.ordinal
	}
	
	enum class m3d {
		PARTY_STATUS, KEY_BIND, WAETHER, TOGGLER
	}
	
	class Handler: IMessageHandler<Message3d, IMessage> {
		
		override fun onMessage(packet: Message3d, message: MessageContext): IMessage? {
			PacketHandlerClient.handle(packet)
			return null
		}
	}
}
