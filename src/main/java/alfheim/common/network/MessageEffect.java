package alfheim.common.network;

import alexsocol.asjlib.network.ASJPacket;

public class MessageEffect extends ASJPacket {
	
	public int entity;
	public int id;
	public int amp;
	public int dur;
	public boolean readd;
	public int state;
	
	public MessageEffect(int e, int i, int d, int a) {
		this(e, i, d, a, false, 1);
	}
	
	public MessageEffect(int e, int i, int d, int a, boolean r, int s) {
		entity = e;
		id = i;
		dur = d;
		amp = a;
		readd = r;
		state = s;
	}
}