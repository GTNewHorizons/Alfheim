package alfheim.common.network;

import alfheim.client.core.handler.PacketHandlerClient;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class MessageHotSpellC implements IMessage {

	public int[] ids;
	
	public MessageHotSpellC() {
		ids = new int[12];
	}
	
	public MessageHotSpellC(int[] ids) {
		this.ids = ids.clone();
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		for (int i = 0; i < ids.length; i++) buf.writeInt(ids[i]);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		for (int i = 0; i < ids.length; i++) ids[i] = buf.readInt();
	}
	
	public static class Handler implements IMessageHandler<MessageHotSpellC, IMessage> {

		@Override
		public IMessage onMessage(MessageHotSpellC packet, MessageContext message) {
			PacketHandlerClient.handle(packet);
			return null;
		}
	}
}
