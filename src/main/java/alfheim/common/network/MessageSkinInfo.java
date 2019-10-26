package alfheim.common.network;

import alexsocol.asjlib.network.ASJPacket;

public class MessageSkinInfo extends ASJPacket {
	
	public String name;
	public boolean isFemale;
	public boolean isSkinOn;
	
	public MessageSkinInfo(String name, boolean fem, boolean on) {
		this.name = name;
		isFemale = fem;
		isSkinOn = on;
	}
}
