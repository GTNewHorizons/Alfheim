package alfheim.common.network;

import alexsocol.asjlib.network.ASJPacket;

public class Message3d extends ASJPacket {
	
	public int type;
	public double data1, data2, data3;
	
	public Message3d(m3d ty, double d1, double d2, double d3) {
		type = ty.ordinal();
		data1 = d1;
		data2 = d2;
		data3 = d3;
	}
	
	public enum m3d {
		PARTY_STATUS, KEY_BIND, WAETHER, TOGGLER
	}
}