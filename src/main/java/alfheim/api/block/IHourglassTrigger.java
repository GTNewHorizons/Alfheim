package alfheim.api.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Have a block implement this class to make it do something when an adjacent
 * Hovering Hourglass turns.
 */
public interface IHourglassTrigger {

	void onTriggeredByHourglass(World world, int x, int y, int z, TileEntity hourglass);

}