package alfheim.common.network;

import alexsocol.asjlib.ASJUtilities;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.entity.EnumRace;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;

public class AttributeMessage implements IMessage {

	public int id;
	public double value;
	
	public AttributeMessage() {
		id = 0;
		value = 0;
	}
	
	public AttributeMessage(int i, double v) {
		id = i;
		value = v;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		value = buf.readDouble();
		id = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeDouble(value);
		buf.writeInt(id);
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