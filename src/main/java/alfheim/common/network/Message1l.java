package alfheim.common.network;

import alexsocol.asjlib.network.ASJPacket;

public class Message1l extends ASJPacket {
	
	public int type;
	public long data1;
	
	public Message1l(m1l t, long d) {
		type = t.ordinal();
		data1 = d;
	}
	
	public enum m1l {
		SEED
	}
}
