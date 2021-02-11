package alfheim.common.network;

import alexsocol.asjlib.network.ASJPacket;

public class Message0dS extends ASJPacket {
	
	public int type;
	
	public Message0dS(m0ds ty) {
		type = ty.ordinal();
	}
	
	public enum m0ds {
		DODGE, JUMP
	}
}