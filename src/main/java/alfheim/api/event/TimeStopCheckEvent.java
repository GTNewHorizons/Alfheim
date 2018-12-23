package alfheim.api.event;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;

/**
 * <p>Fired to change [tile]entity's behavior in time-stop area
 * <p>You should cancel event if you want it to take effect
 */
public abstract class TimeStopCheckEvent extends Event {

	public boolean result;
	
	@Cancelable
	public static class TimeStopEntityCheckEvent extends TimeStopCheckEvent {
		public final Entity entity;
		public TimeStopEntityCheckEvent(Entity e) {
			entity = e;
		}
	}
	
	@Cancelable
	public static class TimeStopTileCheckEvent extends TimeStopCheckEvent {
		public final TileEntity tile;
		public TimeStopTileCheckEvent(TileEntity te) {
			tile = te;
		}
	}
}