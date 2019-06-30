package alfheim.common.network;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.network.ASJPacket;
import io.netty.buffer.ByteBuf;

public class MessageKeyBind extends ASJPacket {
	
	public int action;
	public int ticks;
	public boolean state;
	
	public MessageKeyBind(int action, boolean state, int ticks) {
		this.action = action;
		this.state = state;
		this.ticks = ticks;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		if (!ASJUtilities.isServer()) return;
		action = buf.readInt();
		ticks = buf.readInt();
		state = buf.readBoolean();
	}
}