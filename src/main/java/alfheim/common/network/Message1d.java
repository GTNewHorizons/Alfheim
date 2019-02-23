package alfheim.common.network;

import alexsocol.asjlib.network.ASJPacket;
import alfheim.client.core.handler.PacketHandlerClient;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class Message1d extends ASJPacket {

	public int type;
	public double data1;
	
	public Message1d(m1d ty, double d1) {
		type = ty.ordinal();
		data1 = d1;
	}
	
	public static enum m1d {
		DEATH_TIMER, TIME_STOP_REMOVE, KNOWLEDGE, CL_SLOWDOWN
	}
	
	public static class Handler implements IMessageHandler<Message1d, IMessage> {

		@Override
		public IMessage onMessage(Message1d packet, MessageContext message) {
			PacketHandlerClient.handle(packet);
			return null;
		}
	}
}
