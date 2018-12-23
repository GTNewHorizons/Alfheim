package alfheim.api.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.tileentity.TileEntity;

@Cancelable
public class TileUpdateEvent extends Event {

	public final TileEntity tile;
	
	public TileUpdateEvent(TileEntity te) {
		tile = te;
	}
	
	/** Used in ASM */
	public static TileUpdateEvent instantiate(TileEntity te) {
		return new TileUpdateEvent(te);
	}
	
	/** Used in ASM */
	public static void stub() {}
}
