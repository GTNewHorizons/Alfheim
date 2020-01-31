package alfheim.common.network;

import alexsocol.asjlib.math.Vector3;
import alexsocol.asjlib.network.ASJPacket;

public class MessageEffectLightning extends ASJPacket {
	
	double x1, y1, z1, x2, y2, z2;
	float ticksPerMeter;
	int colorOuter, colorInner;
	
	public MessageEffectLightning(Vector3 v1, Vector3 v2, float tpm, int co, int ci) {
		x1 = v1.getX();
		y1 = v1.getY();
		z1 = v1.getZ();
		x2 = v2.getX();
		y2 = v2.getY();
		z2 = v2.getZ();
		ticksPerMeter = tpm;
		colorOuter = co;
		colorInner = ci;
	}
}
