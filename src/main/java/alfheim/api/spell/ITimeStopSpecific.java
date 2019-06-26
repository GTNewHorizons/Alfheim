package alfheim.api.spell;

import java.util.UUID;

/**
 * Used to check whether this [tile]entity is affectable by time-stop
 * @see alfheim.common.core.handler.CardinalSystem.TimeStopSystem#affected(net.minecraft.entity.Entity e)
 * @see alfheim.common.core.handler.CardinalSystem.TimeStopSystem#affected(net.minecraft.tileentity.TileEntity te)
 * 
 */
public interface ITimeStopSpecific {

	/**
	 * @return true if this [tile]entity is totally immune to time-stop
	 */
	boolean isImmune();
	
	/**
	 * Called if {@link #isImmune()} returned {@code false}
	 * @param uuid UUID of time-stop area
	 * @return true if this [tile]entity <b>isn't</b> "in party" with {@code uuid}
	 */
	boolean affectedBy(UUID uuid);
}
