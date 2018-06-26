package alfheim.api.event;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.world.World;

public class NetherPortalActivationEvent extends Event  {

	public final World worldObj;
	public final int xCoord, yCoord, zCoord;
	
	public NetherPortalActivationEvent(World world, int x, int y, int z) {
		worldObj = world;
		xCoord = x;
		yCoord = y;
		zCoord = z;
	}
	
	@Override
	public boolean isCancelable() {
		return true;
	}
}