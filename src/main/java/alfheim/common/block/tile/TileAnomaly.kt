package alfheim.common.block.tile

import java.util.HashMap

import alexsocol.asjlib.ASJUtilities
import alfheim.api.block.tile.SubTileEntity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World
import vazkii.botania.common.block.tile.TileMod

class TileAnomaly: TileMod() {
	
	val subTiles = HashMap<String, SubTileEntity>()
	var mainSubTile: String? = null
	var compatibilityBit = 0 // not serializing because will be recalculated on load
	
	override fun updateEntity() {
		if (mainSubTile == null || mainSubTile!!.isEmpty() || subTiles.get(mainSubTile) == null) return
		
		val l = subTiles.get(mainSubTile).targets
		for (subTile in subTiles.values) subTile.updateEntity(l)
	}
	
	fun onActivated(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Boolean {
		var flag = false
		for (subTile in subTiles.values) flag = flag or subTile.onActivated(stack, player, world, x, y, z)
		return flag
	}
	
	fun addSubTile(name: String): TileAnomaly {
		return addSubTile(SubTileEntity.forName(name), name)
	}
	
	fun addSubTile(sub: SubTileEntity?, name: String): TileAnomaly {
		if (sub == null || !canAdd(sub)) return this
		
		compatibilityBit = compatibilityBit or sub.typeBits()
		
		if (mainSubTile == null || mainSubTile!!.isEmpty()) mainSubTile = name
		
		subTiles[name] = sub
		return (sub.superTile = this) as TileAnomaly
	}
	
	fun canAdd(sub: SubTileEntity): Boolean {
		return compatibilityBit and sub.typeBits() == 0
	}
	
	override fun writeCustomNBT(cmp: NBTTagCompound?) {
		super.writeCustomNBT(cmp)
		
		if (mainSubTile == null) return
		
		try {
			cmp!!.setString(TAG_SUBTILE_MAIN, mainSubTile!!)
			
			var c = subTiles.keys.size
			cmp.setInteger(TAG_SUBTILE_COUNT, c)
			
			var subCmp: NBTTagCompound
			
			for (name in subTiles.keys) {
				cmp.setString(TAG_SUBTILE_NAME + c, name)
				
				subCmp = NBTTagCompound()
				cmp.setTag(TAG_SUBTILE_CMP + c--, subCmp)
				
				subTiles[name].writeToNBT(subCmp)
			}
		} catch (e: Throwable) {
			ASJUtilities.error("Got exception writing anomaly data. It will be discarded.")
			e.printStackTrace()
		}
		
	}
	
	override fun readCustomNBT(cmp: NBTTagCompound?) {
		super.readCustomNBT(cmp)
		
		mainSubTile = cmp!!.getString(TAG_SUBTILE_MAIN)
		
		var c = cmp.getInteger(TAG_SUBTILE_COUNT)
		
		var subTileName: String
		var subCmp: NBTTagCompound
		var subTile: SubTileEntity?
		
		while (c > 0) {
			subTileName = cmp.getString(TAG_SUBTILE_NAME + c)
			subTile = SubTileEntity.forName(subTileName)
			
			subCmp = cmp.getCompoundTag(TAG_SUBTILE_CMP + c)
			if (subTile != null && !subCmp.hasNoTags())
				subTile.readFromNBT(subCmp)
			
			addSubTile(subTile, subTileName)
			c--
		}
	}
	
	companion object {
		
		val TAG_SUBTILE_MAIN = "subTileMain"
		val TAG_SUBTILE_NAME = "subTileName"
		val TAG_SUBTILE_CMP = "subTileCmp"
		val TAG_SUBTILE_COUNT = "subTileCount"
	}
}