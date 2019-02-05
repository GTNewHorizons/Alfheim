package alfheim.common.block.tile;

import alfheim.api.AlfheimAPI;
import alfheim.api.block.tile.SubTileEntity;
import net.minecraft.nbt.NBTTagCompound;
import vazkii.botania.common.block.tile.TileMod;

public class TileAnomaly extends TileMod {
	
	private static final String TAG_SUBTILE_NAME = "subTileName";
	private static final String TAG_SUBTILE_CMP = "subTileCmp";
	
	public SubTileEntity subTile;
	public String subTileName;
	
	@Override
	public void updateEntity() {
		if (subTile != null) subTile.updateEntity();
	}
	
	public TileAnomaly setSubTile(String name) {
		provideSubTile(name);
		return this;
	}
	
	public TileAnomaly setSubTile(SubTileEntity sub) {
		subTile = sub;
		return (TileAnomaly) (sub.superTile = this);
	}
	
	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		super.writeCustomNBT(cmp);
		
		cmp.setString(TAG_SUBTILE_NAME, subTileName);
		
		NBTTagCompound subCmp = new NBTTagCompound();
		cmp.setTag(TAG_SUBTILE_CMP, subCmp);

		if(subTile != null)
			subTile.writeToNBT(subCmp);
	}

	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		super.readCustomNBT(cmp);

		subTileName = cmp.getString(TAG_SUBTILE_NAME);
		
		NBTTagCompound subCmp = cmp.getCompoundTag(TAG_SUBTILE_CMP);
		if(subTile == null)
			provideSubTile(subTileName);

		if(subTile != null)
			subTile.readFromNBT(subCmp);
	}

	public void provideSubTile(String name) {
		subTileName = name;
		
		Class<? extends SubTileEntity> tileClass = AlfheimAPI.getAnomaly(name);
		try {
			setSubTile(tileClass.newInstance());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
