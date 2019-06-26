package alfheim.common.integration.waila.handler

import alfheim.common.block.tile.TileTradePortal
import mcp.mobius.waila.api.*
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.StatCollector
import net.minecraft.world.World
import net.minecraftforge.oredict.OreDictionary
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.recipe.RecipeElvenTrade

class WAILAHandlerTradePortal: IWailaDataProvider {
	
	override fun getNBTData(player: EntityPlayerMP, tile: TileEntity, result: NBTTagCompound, world: World, x: Int, y: Int, z: Int): NBTTagCompound {
		if (tile is TileTradePortal) {
			tile.writeCustomNBT(result)
			result.removeTag(TileTradePortal.TAG_TICKS_OPEN)
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
		get@ if (tag.hasKey(TileTradePortal.TAG_RECIPE_MULT) && tag.hasKey(TileTradePortal.TAG_RECIPE_NUM)) {
			val count = tag.getInteger(TileTradePortal.TAG_RECIPE_MULT)
			if (count <= 0) break@get
			
			val num = tag.getInteger(TileTradePortal.TAG_RECIPE_NUM)
			if (num < 0) break@get
			
			val recipe = BotaniaAPI.elvenTradeRecipes[num]
			currenttip.add(StatCollector.translateToLocal("alfheimmisc.waila.trade.request") + recipe.output.displayName + " x" + recipe.output.stackSize)
			currenttip.add(StatCollector.translateToLocal("alfheimmisc.waila.trade.results"))
			for (o in recipe.inputs) {
				if (o is String)
					currenttip.add(getOreName(o))
				else if (o is ItemStack)
					currenttip.add(o.displayName + " x" + o.stackSize)
				else
					currenttip.add(o.toString())
			}
			currenttip.add(StatCollector.translateToLocalFormatted("alfheimmisc.waila.trade.available", count))
		}
		return currenttip
	}
	
	override fun getWailaTail(itemStack: ItemStack, currenttip: List<String>, accessor: IWailaDataAccessor, config: IWailaConfigHandler): List<String> {
		return currenttip
	}
	
	private fun getOreName(o: String): String {
		val l = OreDictionary.getOres(o)
		if (l.isEmpty()) return o
		
		val stack = l[0].copy()
		if (stack.itemDamage == java.lang.Short.MAX_VALUE.toInt())
			stack.itemDamage = 0
		
		return stack.displayName + " x" + stack.stackSize
	}
}
