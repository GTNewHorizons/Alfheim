package alfheim.common.network

import alexsocol.asjlib.network.ASJPacket
import alfheim.client.core.handler.PacketHandlerClient
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party
import cpw.mods.fml.common.network.simpleimpl.IMessage
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler
import cpw.mods.fml.common.network.simpleimpl.MessageContext
import io.netty.buffer.ByteBuf

class MessageTimeStop(var party: Party?, val x: Double, val y: Double, val z: Double, val id: Int): ASJPacket() {
	
	override fun toCustomBytes(buf: ByteBuf) {
		buf.writeBoolean(party != null)
		if (party != null) party!!.write(buf)
	}
	
	override fun fromCustomBytes(buf: ByteBuf) {
		if (buf.readBoolean()) party = Party.read(buf)
	}
	
	class Handler: IMessageHandler<MessageTimeStop, IMessage> {
		
		override fun onMessage(packet: MessageTimeStop, message: MessageContext): IMessage? {
			PacketHandlerClient.handle(packet)
			return null
		}
	}
}
