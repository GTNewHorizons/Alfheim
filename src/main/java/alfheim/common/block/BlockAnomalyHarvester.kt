package alfheim.common.block

import alexsocol.asjlib.*
import alfheim.api.AlfheimAPI
import alfheim.api.lib.LibRenderIDs
import alfheim.common.block.base.BlockContainerMod
import alfheim.common.block.tile.*
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import vazkii.botania.api.wand.IWandable
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.item.ModItems
import kotlin.math.max
class BlockAnomalyHarvester: BlockContainerMod(Material.iron), IWandable {
	
	init {
		setBlockName("AnomalyHarvester")
		setLightOpacity(0)
		setHardness(5f)
		setResistance(2000f)
		setStepSound(Block.soundTypeMetal)
	}
	
	override fun renderAsNormalBlock() = false
	override fun isOpaqueCube() = false
	override fun getRenderType() = LibRenderIDs.idHarvester
	
	override fun createNewTileEntity(world: World, meta: Int) = TileAnomalyHarvester()
	
	override fun onBlockPlaced(world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float, meta: Int) = ForgeDirection.VALID_DIRECTIONS.safeGet(side).ordinal
	
	override fun onBlockClicked(world: World, x: Int, y: Int, z: Int, player: EntityPlayer?) {
		if (player?.heldItem?.item !== ModItems.twigWand)
			return super.onBlockClicked(world, x, y, z, player)
		
		var meta = world.getBlockMetadata(x, y, z)
		world.setBlockMetadataWithNotify(x, y, z, ++meta % 6, 3)
	}
	
	override fun onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		if (player.currentEquippedItem?.item === ModItems.twigWand) return player.currentEquippedItem.item.onItemUse(player.currentEquippedItem, player, world, x, y, z, side, hitX, hitY, hitZ)
		
		// somehow insert anomaly...
		val tile = world.getTileEntity(x, y, z) as? TileAnomalyHarvester ?: return false
		
		if (player.currentEquippedItem?.item === AlfheimBlocks.anomaly.toItem()) {
			val main = ItemNBTHelper.getString(player.currentEquippedItem, TileAnomaly.TAG_SUBTILE_MAIN, "")
			if (!AlfheimAPI.anomalyBehaviors.containsKey(main)) return false
			tile.addSubTile(main)
			return true
		}
		
		if (player.currentEquippedItem != null) return false
		
		tile.power = max(0.0, tile.power + if (player.isSneaking) -1 else 1)
		
		if (!world.isRemote)
			ASJUtilities.say(player, "alfheimmisc.power", tile.power)
		
		ASJUtilities.dispatchTEToNearbyPlayers(tile)
		
		return true
	}
	
	override fun onUsedByWand(player: EntityPlayer?, stack: ItemStack, world: World, x: Int, y: Int, z: Int, side: Int): Boolean {
		if (player == null) return false
		val tile = world.getTileEntity(x, y, z) as? TileAnomalyHarvester ?: return false
		val fd = ForgeDirection.getOrientation(side)
		
		if (player.isSneaking)
			tile.radius.set(max(1.0, tile.radius.x + fd.offsetX), max(1.0, tile.radius.y + fd.offsetY), max(1.0, tile.radius.z + fd.offsetZ)).also {
				if (!world.isRemote)
					ASJUtilities.say(player, "alfheimmisc.area", it.x, it.y, it.z)
			}
		else
			tile.offset.add(fd.offsetX * 0.5, fd.offsetY * 0.5, fd.offsetZ * 0.5).also {
				if (!world.isRemote)
					ASJUtilities.say(player, "alfheimmisc.offset", it.x, it.y, it.z)
			}
		
		ASJUtilities.dispatchTEToNearbyPlayers(tile)
		
		return true
	}
}
