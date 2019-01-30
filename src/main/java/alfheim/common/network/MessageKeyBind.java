package alfheim.common.network;

import static alfheim.api.spell.SpellBase.SpellCastResult.*;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.network.ASJPacket;
import alfheim.AlfheimCore;
import alfheim.api.AlfheimAPI;
import alfheim.api.spell.SpellBase;
import alfheim.client.core.handler.KeyBindingHandlerClient.KeyBindingIDs;
import alfheim.common.core.handler.CardinalSystem;
import alfheim.common.core.handler.CardinalSystem.HotSpellsSystem;
import alfheim.common.core.handler.CardinalSystem.PlayerSegment;
import alfheim.common.core.handler.CardinalSystem.TargetingSystem;
import alfheim.common.core.handler.KeyBindingHandler;
import alfheim.common.network.Message2d.m2d;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;

public class MessageKeyBind extends ASJPacket {

	public int action;
	public int ticks;
	public boolean state;
	
	public MessageKeyBind(int action, boolean state, int ticks) {
		this.action = action;
		this.state = state;
		this.ticks = ticks;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		if (!ASJUtilities.isServer()) return;
		action = buf.readInt();
		ticks = buf.readInt();
		state = buf.readBoolean();
	}
	
	public static class Handler implements IMessageHandler<MessageKeyBind, IMessage> {

		@Override
		public IMessage onMessage(MessageKeyBind packet, MessageContext message) {
			EntityPlayerMP player = message.getServerHandler().playerEntity;
			
			switch (KeyBindingIDs.values()[packet.action]) {
				case ATTACK: KeyBindingHandler.atack(player); break;
				case CAST: {
					int ids = packet.state ? HotSpellsSystem.getHotSpellID(player, packet.ticks) : packet.ticks;
					PlayerSegment seg = CardinalSystem.forPlayer(player);
					SpellBase spell = AlfheimAPI.getSpellByIDs((ids >> 28) & 0xF, ids & 0xFFFFFFF);
					if (spell == null) AlfheimCore.network.sendTo(new Message2d(m2d.COOLDOWN, ids, -DESYNC.ordinal()), player);
					else {
						seg.ids = ids;
						seg.init = spell.getCastTime();
						seg.castableSpell = spell;
					}
					break;
				}
				case FLIGHT: KeyBindingHandler.enableFlight(player, packet.state); break;
				case SEL: {
					Entity e = player.worldObj.getEntityByID(packet.ticks);
					if (e != null && e instanceof EntityLivingBase) TargetingSystem.setTarget(player, (EntityLivingBase) e, packet.state); break;
				}
			}
			return null;
		}
	}
}