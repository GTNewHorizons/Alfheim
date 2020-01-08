package alfheim.common.network;

import alexsocol.asjlib.network.ASJPacket;

public class MessageRaceSelection extends ASJPacket {
	
	boolean doMeta, custom, female, give;
	int meta, rot, arot, timer, x, y, z, dim;
	
	public MessageRaceSelection(boolean d, boolean c, boolean f, boolean g, int m, int r, int a, int t, int i, int j, int k, int e) {
		doMeta = d;
		custom = c;
		female = f;
		give = g;
		
		meta = m;
		rot = r;
		arot = a;
		timer = t;
		
		x = i;
		y = j;
		z = k;
		dim = e;
	}
}
