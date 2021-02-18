package alfheim.common.block.magtrees.lightning

import alfheim.common.block.base.BlockModRotatedPillar
import alfheim.common.block.tile.TileLightningRod
import alfheim.common.item.block.ItemBlockLeavesMod
import alfheim.common.lexicon.AlfheimLexiconData
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.*
import java.util.*

class BlockLightningWood: BlockModRotatedPillar(Material.wood), ITileEntityProvider {
	
	init {
		setBlockName("lightningWood")
		isBlockContainer = true
		blockHardness = 2f
	}
	
	override fun isInterpolated(): Boolean = true
	
	override fun canSustainLeaves(world: IBlockAccess, x: Int, y: Int, z: Int): Boolean = true
	
	override fun isWood(world: IBlockAccess?, x: Int, y: Int, z: Int): Boolean = true
	
	override fun breakBlock(world: World, x: Int, y: Int, z: Int, block: Block, fortune: Int) {
		val b0: Byte = 4
		val i1: Int = b0 + 1
		
		if (world.checkChunksExist(x - i1, y - i1, z - i1, x + i1, y + i1, z + i1)) {
			for (j1 in -b0..b0) for (k1 in -b0..b0)
				for (l1 in -b0..b0) {
					val blockInWorld: Block = world.getBlock(x + j1, y + k1, z + l1)
					if (blockInWorld.isLeaves(world, x + j1, y + k1, z + l1)) {
						blockInWorld.beginLeavesDecay(world, x + j1, y + k1, z + l1)
					}
				}
		}
		super.breakBlock(world, x, y, z, block, fortune)
		world.removeTileEntity(x, y, z)
	}
	
	override fun onBlockEventReceived(world: World?, x: Int, y: Int, z: Int, event: Int, eventArg: Int): Boolean {
		super.onBlockEventReceived(world, x, y, z, event, eventArg)
		val tileentity = world!!.getTileEntity(x, y, z)
		return tileentity?.receiveClientEvent(event, eventArg) ?: false
	}
	
	override fun createNewTileEntity(world: World?, meta: Int) = TileLightningRod()
	
	fun isHeartWood(meta: Int) = meta and 3 == 1
	
	override fun damageDropped(meta: Int) = 0
	
	override fun quantityDropped(random: Random) = 1
	
	override fun hasTileEntity(metadata: Int) = isHeartWood(metadata)
	
	override fun register(name: String) {
		GameRegistry.registerBlock(this, ItemBlockLeavesMod::class.java, name)
	}
	
	override fun getEntry(p0: World?, p1: Int, p2: Int, p3: Int, p4: EntityPlayer?, p5: ItemStack?) =
		AlfheimLexiconData.lightningSapling
}
