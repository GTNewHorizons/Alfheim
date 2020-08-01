package alfheim.common.network;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.network.ASJPacket;
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
		if (ASJUtilities.isClient()) return;
		slot = buf.readInt();
		id = buf.readInt();
	}
}