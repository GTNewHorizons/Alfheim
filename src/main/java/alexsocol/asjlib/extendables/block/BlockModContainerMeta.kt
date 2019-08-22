package alexsocol.asjlib.extendables.block

import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.world.World

abstract class BlockModContainerMeta(mat: Material, subtypes: Int, modid: String): BlockModMeta(mat, subtypes, modid), ITileEntityProvider {
	
	init {
		isBlockContainer = true
	}
	
	override fun breakBlock(world: World, x: Int, y: Int, z: Int, block: Block?, meta: Int) {
		super.breakBlock(world, x, y, z, block, meta)
		world.removeTileEntity(x, y, z)
	}
	
	override fun onBlockEventReceived(world: World?, x: Int, y: Int, z: Int, id: Int, value: Int): Boolean {
		super.onBlockEventReceived(world, x, y, z, id, value)
		val tileentity = world!!.getTileEntity(x, y, z)
		return tileentity != null && tileentity.receiveClientEvent(id, value)
	}
	
	override fun damageDropped(meta: Int) = meta
}
