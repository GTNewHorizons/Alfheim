package alfheim.common.network;

import alexsocol.asjlib.network.ASJPacket;

public class MessageParticles extends ASJPacket {

	public int i;
	public double x, y, z, x2, y2, z2;
	
	public MessageParticles(int i, double x, double y, double z) {
		this.i = i;
		this.x = x;
		this.y = y;
		this.z = z;
		x2 = y2 = z2 = 0;
	}
	
	public MessageParticles(int i, double x, double y, double z, double x2, double y2, double z2) {
		this(i, x, y, z);
		this.x2 = x2;
		this.y2 = y2;
		this.z2 = z2;
	}
}