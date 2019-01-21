package alfheim.common.network;

import alexsocol.asjlib.network.ASJPacket;
import alfheim.common.core.handler.PacketHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class Message0d extends ASJPacket {

	public int type;
	
	public Message0d(m0d ty) {
		type = ty.ordinal();
	}
	
	public static enum m0d {
		DODGE, JUMP
	}
	
	public static class Handler implements IMessageHandler<Message0d, IMessage> {

		@Override
		public IMessage onMessage(Message0d packet, MessageContext message) {
			PacketHandler.handle(packet, message);
			return null;
		}
	}
}