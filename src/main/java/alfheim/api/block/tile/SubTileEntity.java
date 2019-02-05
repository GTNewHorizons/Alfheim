package alfheim.api.block.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

// Used for anomalies
public abstract class SubTileEntity {

	/** The Tag items should use to store which sub tile they are. **/
	public static final String TAG_TYPE = "type";
	public static final String TAG_TICKS = "ticks";
	
	public int ticks;
	
	public TileEntity superTile;
	
	public final void updateEntity() {
		ticks++;
		update();
	}
	
	public abstract void update();

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
}
