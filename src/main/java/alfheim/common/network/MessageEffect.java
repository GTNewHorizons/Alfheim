package alfheim.common.network;

import alexsocol.asjlib.network.ASJPacket;
import net.minecraft.entity.Entity;
import net.minecraft.potion.PotionEffect;

public class MessageEffect extends ASJPacket {
	
	public int entity;
	public int id;
	public int amp;
	public int dur;
	public boolean readd;
	// 1 - add, 0 - update, -1 - remove
	public int state;
	
	public MessageEffect(Entity e, PotionEffect p) { this(e.getEntityId(), p.potionID, p.duration, p.amplifier); }
	
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