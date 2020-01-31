package alfheim.common.network;

import alexsocol.asjlib.network.ASJPacket;

public class MessageSpellParams extends ASJPacket {
	
	public String name;
	public float damage;
	public int duration;
	public double efficiency, radius;
	
	public MessageSpellParams(String n, float dmg, int dur, double eff, double rad) {
		name = n;
		damage = dmg;
		duration = dur;
		efficiency = eff;
		radius = rad;
	}
}
