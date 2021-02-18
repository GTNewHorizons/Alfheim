package alfheim.common.network;

import alexsocol.asjlib.network.ASJPacket;
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party;
import io.netty.buffer.ByteBuf;

public class MessageParty extends ASJPacket {
	
	public Party party;
	
	public MessageParty(Party pt) {
		party = pt;
	}
	
	@Override
	public void fromCustomBytes(ByteBuf buf) {
		party = Party.Companion.read(buf);
	}
	
	@Override
	public void toCustomBytes(ByteBuf buf) {
		party.write(buf);
	}
}