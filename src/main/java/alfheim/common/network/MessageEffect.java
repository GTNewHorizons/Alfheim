package alfheim.common.network;

import alexsocol.asjlib.network.ASJPacket;

public class MessageEffect extends ASJPacket {
	
	public int entity;
	public int id;
	public int amp;
	public int dur;
	public boolean upd;
	
	public MessageEffect(int e, int i, int d, int a) {
		this(e, i, d, a, true);
	}
	
	public MessageEffect(int e, int i, int d, int a, boolean u) {
		entity = e;
		id = i;
		dur = d;
		amp = a;
		upd = u;
	}
}