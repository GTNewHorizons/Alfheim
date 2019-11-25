package alfheim.common.network;

import alexsocol.asjlib.network.ASJPacket;

public class Message0dC extends ASJPacket {

	public int type;
	
	public Message0dC(m0dc ty) {
		type = ty.ordinal();
	}
	
	public enum m0dc {
		MTSPELL
	}
}