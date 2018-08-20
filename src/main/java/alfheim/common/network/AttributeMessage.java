package alfheim.common.network;

import alexsocol.asjlib.network.ASJPacket;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.entity.EnumRace;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;

public class AttributeMessage extends ASJPacket {

	public int id = 0;
	public double value = 0;
	
	public AttributeMessage(int i, double v) {
		id = i;
		value = v;
	}
	
	public static class Handler implements IMessageHandler<AttributeMessage, IMessage> {

		@Override
		public IMessage onMessage(AttributeMessage packet, MessageContext message) {
			switch (packet.id) {
				case 0: EnumRace.setRaceID(Minecraft.getMinecraft().thePlayer, packet.value); break;
				case 1: Minecraft.getMinecraft().thePlayer.getAttributeMap().getAttributeInstance(AlfheimRegistry.FLIGHT).setBaseValue(packet.value); break;
			}
			return null;
		}
	}
}