package alfheim.common.network;

import alexsocol.asjlib.network.ASJPacket;

public class MessageContributor extends ASJPacket {
	
	public String key, value;
	public boolean isRequest;
	
	public MessageContributor(String k, String v) {
		key = k;
		value = v;
		// default
		isRequest = false;
	}
	
	public MessageContributor(boolean req) {
		isRequest = req;
		// default
		key = value = "";
	}
}
