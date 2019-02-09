package alfheim.api.block.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.math.Vector3;
import alfheim.api.AlfheimAPI;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

// Used for anomalies
public abstract class SubTileEntity {

	/** The Tag items should use to store which sub tile they are. **/
	public static final String TAG_TYPE = "type";
	public static final String TAG_TICKS = "ticks";
	public static final ArrayList<Object> EMPTY_LIST = new ArrayList<Object>(0);
	public Random rand = new Random();
	
	public int ticks;
	
	public TileEntity superTile;
	
	/** optional update method for particles or other stuff */
	protected void update() {}
	
	public final void updateEntity(List<Object> l) {
		update();
		
		effect: {
			if (l == null || l.isEmpty()) break effect;
			while(l.contains(null)) l.remove(null);
			if (l.isEmpty()) break effect;
		
			for (Object target : l) performEffect(target);
		}
		
		ticks++;
		//ASJUtilities.chatLog("Done");
	}
	
	public abstract List<Object> getTargets();
	
	public abstract void performEffect(Object target);
	
	public static final int NONE	= 0,			//	0b00000 - fully compatible, do not use this unless you know what you are doing 
							MOTION	= 1,			//	0b00001 - motion manipulation	- gravity 
							HEALTH	= 2,			//	0b00010 - health manipulation	- damaging
							MANA	= 4,			//	0b00100 - mana manipulation		- drain mana
							TIME	= 8,			//	0b01000 - ticks manipulation	- time speedup
							SPACE	= 17,			//	0b10001 - space manipulation	- teleportation		- also incompatible with motion
							ALL		= 0xFFFFFFFF;	// 			- fully incompatible
							
	/** Checks if two SubTiles can be mixed in single anomaly */
	public abstract int typeBits();
	
	public final void writeToNBT(NBTTagCompound cmp) {
		cmp.setInteger(TAG_TICKS, ticks);
		writeCustomNBT(cmp);
	}
	
	public void writeCustomNBT(NBTTagCompound cmp) {}

	public final void readFromNBT(NBTTagCompound cmp) {
		ticks = cmp.getInteger(TAG_TICKS);
		readCustomNBT(cmp);
	}
	
	public void readCustomNBT(NBTTagCompound cmp) {}

	public static SubTileEntity forName(String name) {
		try {
			return AlfheimAPI.getAnomaly(name).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public World worldObj() {
		return superTile.getWorldObj();
	}
	
	public int x() {
		return superTile.xCoord;
	}
	
	public int x(double x) {
		return x() + MathHelper.floor_double(x);
	}
	
	public int y() {
		return superTile.yCoord;
	}
	
	public int y(double y) {
		return y() + MathHelper.floor_double(y);
	}
	
	public int z() {
		return superTile.zCoord;
	}
	
	public int z(double z) {
		return z() + MathHelper.floor_double(z);
	}
	
	
	// ################################ UTILS ################################
	
	public EntityLivingBase findNearestVulnerableEntity(double radius) {
        List<EntityLivingBase> list = allAround(EntityLivingBase.class, radius);
        EntityLivingBase entity1 = null;
        double d0 = Double.MAX_VALUE;

        for (EntityLivingBase entity2 : list) {
            if (entity2 != null) {
            	if (entity2.isEntityInvulnerable()) continue;
            	if (entity2 instanceof EntityPlayer && ((EntityPlayer) entity2).capabilities.disableDamage) continue;
            	
                double d1 = Vector3.entityTileDistance(entity2, superTile);

                if (d1 <= d0) {
                    entity1 = entity2;
                    d0 = d1;
                }
            }
        }

        return entity1;
    }
	
	public EntityLivingBase findNearestEntity(double radius) {
        List<EntityLivingBase> list = allAround(EntityLivingBase.class, radius);
        EntityLivingBase entity1 = null;
        double d0 = Double.MAX_VALUE;

        for (EntityLivingBase entity2 : list) {
            if (entity2 != null) {
            	
                double d1 = Vector3.entityTileDistance(entity2, superTile);

                if (d1 <= d0) {
                    entity1 = entity2;
                    d0 = d1;
                }
            }
        }

        return entity1;
    }
	
	public <E> List<E> allAround(Class<E> clazz, double radius) {
		return worldObj().getEntitiesWithinAABB(clazz, AxisAlignedBB.getBoundingBox(x(), y(), z(), x(1), y(1), z(1)).expand(radius, radius, radius));
	}
	
	public List allAroundRaw(Class clazz, double radius) {
		return worldObj().getEntitiesWithinAABB(clazz, AxisAlignedBB.getBoundingBox(x(), y(), z(), x(1), y(1), z(1)).expand(radius, radius, radius));
	}
}
