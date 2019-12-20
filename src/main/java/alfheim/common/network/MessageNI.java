package alfheim.common.network;

import alexsocol.asjlib.network.ASJPacket;
import io.netty.buffer.ByteBuf;
import org.jetbrains.annotations.NotNull;

public class MessageNI extends ASJPacket {
	
	public int type;
	public int[] intArray;
	
	public MessageNI(mni ty, int[] iArr) {
		type = ty.ordinal();
		intArray = iArr;
	}
	
	@Override
	public void fromCustomBytes(@NotNull ByteBuf buf) {
		intArray = new int[buf.readInt()];
		for (int i = 0; i < intArray.length; i++) intArray[i] = buf.readInt();
	}
	
	@Override
	public void toCustomBytes(@NotNull ByteBuf buf) {
		write(buf, intArray.length);
		for (int value : intArray) write(buf, value);
	}
	
	public enum mni {
		WINGS_BL
	}
}
