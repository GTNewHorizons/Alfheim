package alfheim.common.item.block

import alexsocol.asjlib.ASJUtilities
import alfheim.api.ModInfo
import alfheim.common.block.*
import alfheim.common.block.tile.TileAnomaly
import alfheim.common.block.tile.TileAnomaly.Companion.TAG_SUBTILE_COUNT
import alfheim.common.block.tile.TileAnomaly.Companion.TAG_SUBTILE_MAIN
import alfheim.common.block.tile.TileAnomaly.Companion.TAG_SUBTILE_NAME
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.util.IIcon
import net.minecraft.world.World
import vazkii.botania.common.core.helper.ItemNBTHelper.*

class ItemBlockAnomaly(block: Block): ItemBlock(block) {
	
	init {
		maxStackSize = 1
		setTextureName(ModInfo.MODID + ":undefined")
	}
	
	override fun getIcon(stack: ItemStack, pass: Int): IIcon {
		return BlockAnomaly.iconUndefined
	}
	
	override fun getUnlocalizedName(stack: ItemStack?): String {
		return "tile.Anomaly." + getString(stack, TAG_SUBTILE_MAIN, TYPE_UNDEFINED)
	}
	
	override fun getMetadata(meta: Int): Int {
		return meta
	}
	
	override fun placeBlockAt(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float, metadata: Int): Boolean {
		val placed = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata)
		if (placed) {
			val te = world.getTileEntity(x, y, z)
			if (te is TileAnomaly) {
				te.readCustomNBT(getNBT(stack))
				
				if (!world.isRemote) {
					world.markBlockForUpdate(x, y, z)
					ASJUtilities.dispatchTEToNearbyPlayers(te)
				}
			}
		}
		
		return placed
	}
	
	companion object {
		
		const val TYPE_UNDEFINED = "undefined"
		
		fun getType(stack: ItemStack): String {
			return if (detectNBT(stack)) getString(stack, TAG_SUBTILE_MAIN, TYPE_UNDEFINED) else TYPE_UNDEFINED
		}
		
		fun ofType(type: String): ItemStack {
			return ofType(ItemStack(AlfheimBlocks.anomaly), type)
		}
		
		fun ofType(stack: ItemStack, type: String?): ItemStack {
			var type = type
			if (type == null || type.isEmpty()) type = TYPE_UNDEFINED
			setString(stack, TAG_SUBTILE_MAIN, type)
			setInt(stack, TAG_SUBTILE_COUNT, 1)
			setString(stack, TAG_SUBTILE_NAME + "1", type)
			
			return stack
		}
	}
}
