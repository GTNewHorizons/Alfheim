package alfheim.common.integration.waila

import alfheim.common.block.tile.TileManaAccelerator
import alfheim.common.core.handler.AlfheimConfigHandler
import mcp.mobius.waila.api.*
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.*
import net.minecraft.world.World
import vazkii.botania.api.mana.IManaItem

class WAILAHandlerItemHolder: IWailaDataProvider {
	
	override fun getNBTData(player: EntityPlayerMP, tile: TileEntity, result: NBTTagCompound, world: World, x: Int, y: Int, z: Int): NBTTagCompound {
		if (tile is TileManaAccelerator) {
			val stack = tile.item
			if (stack != null && stack.item is IManaItem) {
				val mana = stack.item as IManaItem
				result.setInteger(TAG_MANA, mana.getMana(stack))
				result.setInteger(TAG_MAX_MANA, mana.getMaxMana(stack))
			}
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
		
		if (tag.hasKey(TAG_MANA) && tag.hasKey(TAG_MAX_MANA))
			currenttip.add(EnumChatFormatting.AQUA.toString() + StatCollector.translateToLocalFormatted("alfheimmisc.waila.holder.mana", if (AlfheimConfigHandler.numericalMana) tag.getInteger(TAG_MANA).toString() + "/" + tag.getInteger(TAG_MAX_MANA) else (tag.getInteger(TAG_MANA) * 100 / tag.getInteger(TAG_MAX_MANA)).toString() + "%"))
		
		return currenttip
	}
	
	override fun getWailaTail(itemStack: ItemStack, currenttip: List<String>, accessor: IWailaDataAccessor, config: IWailaConfigHandler): List<String> {
		return currenttip
	}
	
	companion object {
		
		val TAG_MANA = "mana"
		val TAG_MAX_MANA = "maxmana"
	}
}