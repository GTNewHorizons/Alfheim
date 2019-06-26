package alfheim.common.network;

import alexsocol.asjlib.network.ASJPacket;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class MessageEffect extends ASJPacket {
	
	public final int entity;
	public final int id;
	public final int amp;
	public final int dur;
	public final boolean upd;
	
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
	
	public static class Handler implements IMessageHandler<MessageEffect, IMessage> {
		@Override
		public IMessage onMessage(MessageEffect packet, MessageContext message) {
			Entity e = message.getClientHandler().clientWorldController.getEntityByID(packet.entity);
			if (e instanceof EntityLivingBase) {
				EntityLivingBase l = ((EntityLivingBase) e);
				PotionEffect pe = l.getActivePotionEffect(Potion.potionTypes[packet.id]);
				if (packet.dur <= 0) {
					if (pe != null) {
						l.removePotionEffect(packet.id);
						Potion.potionTypes[packet.id].removeAttributesModifiersFromEntity(l, l.getAttributeMap(), packet.amp);
					}
				} else {
					if (pe == null) {
						l.addPotionEffect(new PotionEffect(packet.id, packet.dur, packet.amp, true));
						Potion.potionTypes[packet.id].applyAttributesModifiersToEntity(l, l.getAttributeMap(), packet.amp);
					} else {
						if (packet.upd) Potion.potionTypes[packet.id].removeAttributesModifiersFromEntity(l, l.getAttributeMap(), packet.amp);
						pe.amplifier = packet.amp;
						pe.duration = packet.dur;
						pe.isAmbient = true;
						if (packet.upd) Potion.potionTypes[packet.id].applyAttributesModifiersToEntity(l, l.getAttributeMap(), packet.amp);
					}
				}
			}
			return null;
		}
	}
}
