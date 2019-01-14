package alfheim.common.network;

import alexsocol.asjlib.network.ASJPacket;
import alfheim.client.core.handler.PacketHandlerClient;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class Message2d extends ASJPacket {
	
	public int type;
	public double data1, data2;
	
	public Message2d(m2d ty, double d1, double d2) {
		type = ty.ordinal();
		data1 = d1;
		data2 = d2;
	}
	
	public static enum m2d {
		UUID, COOLDOWN, ATTRIBUTE, MODES
	}
	
	public static class Handler implements IMessageHandler<Message2d, IMessage> {

		@Override
		public IMessage onMessage(Message2d packet, MessageContext message) {
			PacketHandlerClient.handle(packet);
			return null;
		}
	}
}
