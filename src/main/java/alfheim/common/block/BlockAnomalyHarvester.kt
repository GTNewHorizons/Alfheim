package alfheim.common.block

import alexsocol.asjlib.ASJUtilities
import alfheim.api.ModInfo
import alfheim.api.lib.LibRenderIDs
import alfheim.common.block.base.BlockContainerMod
import alfheim.common.block.tile.TileAnomalyHarvester
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import vazkii.botania.api.wand.IWandable
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
	
	override fun onBlockActivated(world: World, x: Int, y: Int, z: Int, player: EntityPlayer, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		if (player.currentEquippedItem?.item === ModItems.twigWand) return player.currentEquippedItem.item.onItemUse(player.currentEquippedItem, player, world, x, y, z, side, hitX, hitY, hitZ)
		// somehow insert anomaly...
		val tile = world.getTileEntity(x, y, z) as? TileAnomalyHarvester ?: return false
		
		if (player.isSneaking)
			tile.power = max(1.0, tile.power + 1)
		else
			tile.power = max(1.0, tile.power - 1)
		
		if (!world.isRemote) player.addChatMessage(ChatComponentText("${StatCollector.translateToLocal("alfheimmisc.power")}: ${tile.power}"))
		
		ASJUtilities.dispatchTEToNearbyPlayers(tile)
		
		return true
	}
	
	override fun onUsedByWand(player: EntityPlayer?, stack: ItemStack, world: World, x: Int, y: Int, z: Int, side: Int): Boolean {
		if (player == null) return false
		val tile = world.getTileEntity(x, y, z) as? TileAnomalyHarvester ?: return false
		val fd = ForgeDirection.getOrientation(side)
		
		if (player.isSneaking)
			tile.radius.set(max(1.0, tile.radius.x + fd.offsetX), max(1.0, tile.radius.y + fd.offsetY), max(1.0, tile.radius.z + fd.offsetZ)).also {
				if (!world.isRemote) player.addChatMessage(ChatComponentText("${StatCollector.translateToLocal("alfheimmisc.radius")}: [${it.x}, ${it.y}, ${it.z}]"))
			}
		else
			tile.offset.add(fd.offsetX * 0.5, fd.offsetY * 0.5, fd.offsetZ * 0.5).also {
				if (!world.isRemote) player.addChatMessage(ChatComponentText("${StatCollector.translateToLocal("alfheimmisc.offset")}: [${it.x}, ${it.y}, ${it.z}]"))
			}
		
		ASJUtilities.dispatchTEToNearbyPlayers(tile)
		
		return true
	}
}
