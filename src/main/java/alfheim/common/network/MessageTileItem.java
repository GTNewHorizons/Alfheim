package alfheim.common.network;

import alexsocol.asjlib.network.ASJPacket;
import alfheim.client.core.handler.PacketHandlerClient;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.item.ItemStack;

public class MessageTileItem extends ASJPacket {

	public int x, y, z;
	public ItemStack s;
	
	public MessageTileItem(int x, int y, int z, ItemStack s) {
		this.s = s;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public static class Handler implements IMessageHandler<MessageTileItem, IMessage> {

		@Override
		public IMessage onMessage(MessageTileItem packet, MessageContext message) {
			PacketHandlerClient.handle(packet);
			return null;
		}
	}
}