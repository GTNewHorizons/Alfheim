package alfheim.common.network

import alexsocol.asjlib.network.ASJPacket
import alfheim.client.core.handler.PacketHandlerClient
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party
import cpw.mods.fml.common.network.simpleimpl.*
import io.netty.buffer.ByteBuf

class MessageParty(var party: Party): ASJPacket() {
	
	override fun toCustomBytes(buf: ByteBuf) {
		party.write(buf)
	}
	
	override fun fromCustomBytes(buf: ByteBuf) {
		party = Party.read(buf)
	}
	
	class Handler: IMessageHandler<MessageParty, IMessage> {
		
		override fun onMessage(packet: MessageParty, message: MessageContext): IMessage? {
			PacketHandlerClient.handle(packet)
			return null
		}
	}
}
