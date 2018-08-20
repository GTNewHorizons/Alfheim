package alfheim.common.network;

import alexsocol.asjlib.network.ASJPacket;
import alfheim.client.core.utils.KeyBindingsUtils.KeyBindingIDs;
import alfheim.common.core.util.KeyBindingsUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;

public class KeyBindMessage extends ASJPacket {

	public KeyBindingIDs action;
	public int ticks;
	public boolean state;
	
	public KeyBindMessage(KeyBindingIDs action, boolean state, int ticks) {
		this.action = action;
		this.state = state;
		this.ticks = ticks;
	}
	
	@Override
	public void toCustomBytes(ByteBuf buf) {
		buf.writeInt(action.ordinal());
	}
	
	@Override
	public void fromCustomBytes(ByteBuf buf) {
		action = KeyBindingIDs.values()[buf.readInt()];
	}
	
	public static class Handler implements IMessageHandler<KeyBindMessage, IMessage> {

		@Override
		public IMessage onMessage(KeyBindMessage packet, MessageContext message) {
			EntityPlayerMP player = message.getServerHandler().playerEntity;
			
			switch (packet.action) {
				case ATTACK: KeyBindingsUtils.atack(player); break;
				case FLIGHT: case BOOST: KeyBindingsUtils.enableFlight(player, packet.action == KeyBindingIDs.BOOST); break;
			}
			return null;
		}

	}
}
