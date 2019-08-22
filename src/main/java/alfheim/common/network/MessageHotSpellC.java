package alfheim.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
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
		for (int id : ids) buf.writeInt(id);
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		for (int i = 0; i < ids.length; i++) ids[i] = buf.readInt();
	}
}