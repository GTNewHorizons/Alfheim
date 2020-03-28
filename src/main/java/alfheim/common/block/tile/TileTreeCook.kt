package alfheim.common.block.tile

import alexsocol.asjlib.boundingBox
import net.minecraft.entity.item.EntityItem
import net.minecraft.item.ItemFood
import net.minecraft.item.crafting.FurnaceRecipes
import net.minecraft.tileentity.TileEntity

class TileTreeCook: TileEntity() {
	
	override fun updateEntity() {
		if (!worldObj.isRemote && worldObj.totalWorldTime % 20 == 0L) {
			for (e in worldObj.getEntitiesWithinAABB(EntityItem::class.java, boundingBox(8)) as List<EntityItem>) {
				if (e.entityItem.item !is ItemFood || e.entityItem.stackSize < 1) continue
				
				val result = FurnaceRecipes.smelting().getSmeltingResult(e.entityItem) ?: continue
				if (result.item !is ItemFood) continue
				
				if (worldObj.spawnEntityInWorld(EntityItem(worldObj, e.posX, e.posY, e.posZ, result.copy()))) {
					--e.entityItem.stackSize
					break
				}
			}
		}
	}
}