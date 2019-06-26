package alfheim.common.network;

import alexsocol.asjlib.network.ASJPacket;
import alfheim.client.core.handler.PacketHandlerClient;
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class MessageTimeStop extends ASJPacket {

	public final int id;
	public final double x;
	public final double y;
	public final double z;
	public Party party;
	
	public MessageTimeStop(Party pt, double x, double y, double z, int id) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.z = z;
		party = pt;
	}
	
	@Override
	public void toCustomBytes(ByteBuf buf) {
		buf.writeBoolean(party != null);
		if (party != null) party.write(buf);
	}
	
	@Override
	public void fromCustomBytes(ByteBuf buf) {
		if (buf.readBoolean()) party = Party.read(buf);
	}
	
	public static class Handler implements IMessageHandler<MessageTimeStop, IMessage> {

		@Override
		public IMessage onMessage(MessageTimeStop packet, MessageContext message) {
			PacketHandlerClient.handle(packet);
			return null;
		}
	}
}
