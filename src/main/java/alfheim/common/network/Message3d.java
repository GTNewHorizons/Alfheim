package alfheim.common.network;

import alexsocol.asjlib.network.ASJPacket;
import alfheim.client.core.handler.PacketHandlerClient;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class Message3d extends ASJPacket {
	
	public int type;
	public double data1, data2, data3;
	
	public Message3d(m3d ty, double d1, double d2, double d3) {
		type = ty.ordinal();
		data1 = d1;
		data2 = d2;
		data3 = d3;
	}
	
	public static enum m3d {
		PARTY_STATUS, KEY_BIND, WAETHER, TOGGLER
	}
	
	public static class Handler implements IMessageHandler<Message3d, IMessage> {

		@Override
		public IMessage onMessage(Message3d packet, MessageContext message) {
			PacketHandlerClient.handle(packet);
			return null;
		}
	}
}
