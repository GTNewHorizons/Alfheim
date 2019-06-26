package alfheim.common.network

import alexsocol.asjlib.network.ASJPacket
import alfheim.client.core.handler.PacketHandlerClient
import cpw.mods.fml.common.network.simpleimpl.IMessage
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler
import cpw.mods.fml.common.network.simpleimpl.MessageContext
import net.minecraft.item.ItemStack

class MessageTileItem(val x: Int, val y: Int, val z: Int, val s: ItemStack?): ASJPacket() {
	
	class Handler: IMessageHandler<MessageTileItem, IMessage> {
		
		override fun onMessage(packet: MessageTileItem, message: MessageContext): IMessage? {
			PacketHandlerClient.handle(packet)
			return null
		}
	}
}