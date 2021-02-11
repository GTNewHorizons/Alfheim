package alfheim.common.network;

import alexsocol.asjlib.network.ASJPacket;
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party;
import io.netty.buffer.ByteBuf;

public class MessageTimeStop extends ASJPacket {
	
	public int id;
	public double x, y, z;
	public Party party;
	
	public MessageTimeStop(Party pt, double x, double y, double z, int id) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.z = z;
		party = pt;
	}
	
	@Override
	public void fromCustomBytes(ByteBuf buf) {
		if (buf.readBoolean()) party = Party.Companion.read(buf);
	}
	
	@Override
	public void toCustomBytes(ByteBuf buf) {
		buf.writeBoolean(party != null);
		if (party != null) party.write(buf);
	}
}