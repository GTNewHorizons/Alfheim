package alfheim.common.integration.waila

import alexsocol.asjlib.meta
import alfheim.common.block.tile.TileAnyavil
import mcp.mobius.waila.api.*
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

class WAILAHandlerAnyavil: IWailaDataProvider {
	
	override fun getNBTData(player: EntityPlayerMP, tile: TileEntity, result: NBTTagCompound, world: World, x: Int, y: Int, z: Int): NBTTagCompound {
		if (tile is TileAnyavil) {
			val stack = tile.item
			if (stack != null)
				result.setString(TAG_ITEM, "${stack.displayName} (${stack.maxDamage - stack.meta}/${stack.maxDamage})")
			else
				result.removeTag(TAG_ITEM)
		}
		return result
	}
	
	override fun getWailaStack(accessor: IWailaDataAccessor, config: IWailaConfigHandler): ItemStack? {
		return null
	}
	
	override fun getWailaHead(itemStack: ItemStack, currenttip: List<String>, accessor: IWailaDataAccessor, config: IWailaConfigHandler): List<String> {
		return currenttip
	}
	
	override fun getWailaBody(itemStack: ItemStack, currenttip: MutableList<String>, accessor: IWailaDataAccessor, config: IWailaConfigHandler): List<String> {
		val tag = accessor.nbtData
		
		if (tag.hasKey(TAG_ITEM))
			currenttip.add(tag.getString(TAG_ITEM))
		
		return currenttip
	}
	
	override fun getWailaTail(itemStack: ItemStack, currenttip: List<String>, accessor: IWailaDataAccessor, config: IWailaConfigHandler): List<String> {
		return currenttip
	}
	
	companion object {
		
		private val TAG_ITEM = "waila:item"
	}
}