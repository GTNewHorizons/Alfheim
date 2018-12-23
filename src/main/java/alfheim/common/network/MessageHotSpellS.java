package alfheim.common.network;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.network.ASJPacket;
import alfheim.common.core.handler.CardinalSystem.HotSpellsSystem;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class MessageHotSpellS extends ASJPacket {

	public int slot;
	public int id;
	
	public MessageHotSpellS(int s, int i) {
		slot = s;
		id = i;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		if (!ASJUtilities.isServer()) return;
		slot = buf.readInt();
		id = buf.readInt();
	}
	
	public static class Handler implements IMessageHandler<MessageHotSpellS, IMessage> {

		@Override
		public IMessage onMessage(MessageHotSpellS packet, MessageContext message) {
			HotSpellsSystem.setHotSpellID(message.getServerHandler().playerEntity, packet.slot, packet.id);
			return null;
		}
	}
}
