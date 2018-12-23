package alfheim.common.network;

import alexsocol.asjlib.network.ASJPacket;
import alfheim.client.core.handler.PacketHandlerClient;
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class MessageParty extends ASJPacket {

	public Party party;
	
	public MessageParty(Party pt) {
		party = pt;
	}
	
	@Override
	public void toCustomBytes(ByteBuf buf) {
		party.write(buf);
	}
	
	@Override
	public void fromCustomBytes(ByteBuf buf) {
		party = Party.read(buf);
	}
	
	public static class Handler implements IMessageHandler<MessageParty, IMessage> {

		@Override
		public IMessage onMessage(MessageParty packet, MessageContext message) {
			PacketHandlerClient.handle(packet);
			return null;
		}
	}
}
