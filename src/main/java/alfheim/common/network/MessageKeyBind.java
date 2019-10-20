package alfheim.common.network;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.network.ASJPacket;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

public class MessageKeyBind extends ASJPacket {
	
	public int action;
	public int data;
	public boolean state;
	
	public MessageKeyBind(int action, boolean state, int data) {
		this.action = action;
		this.state = state;
		this.data = data;
	}
	
	@Override
	public void fromBytes(@NotNull ByteBuf buf) {
		if (!ASJUtilities.isServer()) return;
		action = buf.readInt();
		data = buf.readInt();
		state = buf.readBoolean();
	}
}