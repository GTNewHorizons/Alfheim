package alfheim.common.network;

import alexsocol.asjlib.network.ASJPacket;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

public class MessageVisualEffect extends ASJPacket {

	public int type;
	public double[] data;
	
	public MessageVisualEffect(int i, double... params) {
		type = i;
		data = params;
	}
	
	@Override
	public void toCustomBytes(@NotNull ByteBuf buf) {
		super.toCustomBytes(buf);
		buf.writeInt(data.length);
		for (double d : data)
			buf.writeDouble(d);
	}
	
	@Override
	public void fromCustomBytes(@NotNull ByteBuf buf) {
		super.fromCustomBytes(buf);
		data = new double[buf.readInt()];
		for (int i = 0; i < data.length; i++)
			data[i] = buf.readDouble();
	}
	
	
}