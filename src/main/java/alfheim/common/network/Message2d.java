package alfheim.common.network;

import alexsocol.asjlib.network.ASJPacket;

public class Message2d extends ASJPacket {
	
	public int type;
	public double data1, data2;
	
	public Message2d(m2d ty, double d1, double d2) {
		type = ty.ordinal();
		data1 = d1;
		data2 = d2;
	}
	
	public enum m2d {
		UUID, COOLDOWN, ATTRIBUTE, MODES
	}
}