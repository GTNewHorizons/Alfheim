package alfheim.common.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityLightningMark extends Entity {

	public static final String TAG_TIMER = "timer";
	
	public EntityLightningMark(World world) {
		this(world, 0, 0, 0);
	}

	public EntityLightningMark(World world, double x, double y, double z) {
		super(world);
		setSize(1.5F, 0.0001F); 
		setPosition(x, y, z);
	}

	@Override
	public void onEntityUpdate() {
		motionX = motionY = motionZ = 0;
		setTimer(getTimer() + 1);
		if (getTimer() > 50) {
			if (!worldObj.isRemote) worldObj.addWeatherEffect(new EntityLightningBolt(worldObj, posX, posY, posZ));
			this.setDead();
		}
	}
	
	public int getTimer() {
		return dataWatcher.getWatchableObjectInt(20);
	}

	public void setTimer(int time) {
		dataWatcher.updateObject(20, time);
	}
	
	@Override
	public void entityInit() {
		dataWatcher.addObject(20, 0);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		setTimer(nbt.getInteger(TAG_TIMER));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		nbt.setInteger(TAG_TIMER, getTimer());
	}
}