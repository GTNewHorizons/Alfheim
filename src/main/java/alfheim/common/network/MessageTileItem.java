package alfheim.common.network;

import alexsocol.asjlib.network.ASJPacket;
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
}