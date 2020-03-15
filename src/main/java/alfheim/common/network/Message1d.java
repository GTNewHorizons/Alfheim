package alfheim.common.network;

import alexsocol.asjlib.network.ASJPacket;

public class Message1d extends ASJPacket {

	public int type;
	public double data1;
	
	public Message1d(m1d ty, double d1) {
		type = ty.ordinal();
		data1 = d1;
	}
	
	public enum m1d {
		DEATH_TIMER, ELVEN_FLIGHT_MAX, ESMABIL, GINNUNGAGAP, KNOWLEDGE, TIME_STOP_REMOVE
	}
}