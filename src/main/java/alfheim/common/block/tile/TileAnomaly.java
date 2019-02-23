package alfheim.common.block.tile;

import java.util.HashMap;
import java.util.List;

import alexsocol.asjlib.ASJUtilities;
import alfheim.api.block.tile.SubTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import vazkii.botania.common.block.tile.TileMod;

public class TileAnomaly extends TileMod {
	
	public static final String TAG_SUBTILE_MAIN = "subTileMain";
	public static final String TAG_SUBTILE_NAME = "subTileName";
	public static final String TAG_SUBTILE_CMP = "subTileCmp";
	public static final String TAG_SUBTILE_COUNT = "subTileCount";
	
	public HashMap<String, SubTileEntity> subTiles = new HashMap<String, SubTileEntity>();
	public String mainSubTile;
	public int compatibilityBit = 0; // not serializing because will be recalculated on load
	
	@Override
	public void updateEntity() {
		if (mainSubTile == null || mainSubTile.isEmpty() || subTiles.get(mainSubTile) == null) return;
		
		List<Object> l = subTiles.get(mainSubTile).getTargets();
		for (SubTileEntity subTile : subTiles.values()) subTile.updateEntity(l);
	}
	
	public boolean onActivated(ItemStack stack, EntityPlayer player, World world, int x, int y, int z) {
		boolean flag = false;
		for (SubTileEntity subTile : subTiles.values()) flag |= subTile.onActivated(stack, player, world, x, y, z);
		return flag;
	}
	
	public TileAnomaly addSubTile(String name) {
		return addSubTile(SubTileEntity.forName(name), name);
	}
	
	public TileAnomaly addSubTile(SubTileEntity sub, String name) {
		if (sub == null || !canAdd(sub)) return this;
		
		compatibilityBit |= sub.typeBits();
		
		if (mainSubTile == null || mainSubTile.isEmpty()) mainSubTile = name;
		
		subTiles.put(name, sub);
		return (TileAnomaly) (sub.superTile = this);
	}
	
	public boolean canAdd(SubTileEntity sub) {
		return (compatibilityBit & sub.typeBits()) == 0;
	}
	
	@Override
	public void writeCustomNBT(NBTTagCompound cmp) {
		super.writeCustomNBT(cmp);
		
		if (mainSubTile == null) return;
		
		try {
			cmp.setString(TAG_SUBTILE_MAIN, mainSubTile);
	
			int c = subTiles.keySet().size();
			cmp.setInteger(TAG_SUBTILE_COUNT, c);
			
			NBTTagCompound subCmp;
			
			for (String name : subTiles.keySet()) {
				cmp.setString(TAG_SUBTILE_NAME + c, name);
				
				subCmp = new NBTTagCompound();
				cmp.setTag(TAG_SUBTILE_CMP + (c--), subCmp);
				
				subTiles.get(name).writeToNBT(subCmp);
			}
		} catch (Throwable e) {
			ASJUtilities.error("Got exception writing anomaly data. It will be discarded.");
			e.printStackTrace();
		}
	}
	
	@Override
	public void readCustomNBT(NBTTagCompound cmp) {
		super.readCustomNBT(cmp);
		
		mainSubTile = cmp.getString(TAG_SUBTILE_MAIN);
		
		int c = cmp.getInteger(TAG_SUBTILE_COUNT);
		
		String subTileName;
		NBTTagCompound subCmp;
		SubTileEntity subTile;
		
		for (; c > 0; c--) {
			subTileName = cmp.getString(TAG_SUBTILE_NAME + c);
			subTile = SubTileEntity.forName(subTileName);
			
			subCmp = cmp.getCompoundTag(TAG_SUBTILE_CMP + c);
			if(subTile != null && !subCmp.hasNoTags())
				subTile.readFromNBT(subCmp);
			
			addSubTile(subTile, subTileName);
		}
	}
}