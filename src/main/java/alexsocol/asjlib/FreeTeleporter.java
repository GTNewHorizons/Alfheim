package alexsocol.asjlib;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class FreeTeleporter extends Teleporter {

	WorldServer world;
	double x, y, z;
	
	public FreeTeleporter(WorldServer worldIn, double x, double y, double z) {
		super(worldIn);
		world = worldIn;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public boolean placeInExistingPortal(Entity entity, double x, double y, double z, float rotationYaw) {
		entity.posX = this.x;
		entity.posY = this.y;
		entity.posZ = this.z;
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			if (player.capabilities.allowFlying) player.capabilities.isFlying = true;
		}
		return true;
	}
}