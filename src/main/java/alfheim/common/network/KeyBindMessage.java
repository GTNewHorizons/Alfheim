package alfheim.common.network;

import alfheim.common.core.utils.KeyBindingsUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;

public class KeyBindMessage implements IMessage {

	public byte action;
	public int ticks;
	public boolean state;
	
	public KeyBindMessage() {
		this.action = 0;
		this.state = false;
		this.ticks = 0;
	}
	
	public KeyBindMessage(byte action, boolean state, int ticks) {
		this.action = action;
		this.state = state;
		this.ticks = ticks;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(action);
		buf.writeBoolean(state);
		buf.writeInt(ticks);
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		action = buf.readByte();
		state = buf.readBoolean();
		ticks = buf.readInt();
	}
	
	public static class Handler implements IMessageHandler<KeyBindMessage, IMessage> {

		@Override
		public IMessage onMessage(KeyBindMessage packet, MessageContext message) {
			EntityPlayerMP player = message.getServerHandler().playerEntity;
			
			switch (packet.action) {
				case 0: KeyBindingsUtils.enableFlight(player); break;
				case 1: KeyBindingsUtils.atack(player); break;
			}
			return null;
		}

	}
}
