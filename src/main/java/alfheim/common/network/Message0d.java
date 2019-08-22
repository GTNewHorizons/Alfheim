package alfheim.common.network;

import alexsocol.asjlib.network.ASJPacket;

public class Message0d extends ASJPacket {

	public int type;
	
	public Message0d(m0d ty) {
		type = ty.ordinal();
	}
	
	public enum m0d {
		DODGE, JUMP
	}
}