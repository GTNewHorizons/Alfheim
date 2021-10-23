package alexsocol.asjlib.extendables.block

import net.minecraft.block.*
import net.minecraft.block.material.Material
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.world.World

abstract class BlockModContainerMeta(mat: Material, subtypes: Int, modid: String, name: String, tab: CreativeTabs? = null, hard: Float = 1f, harvTool: String = "pickaxe", harvLvl: Int = 1, resist: Float = 5f): BlockModMeta(mat, subtypes, modid, name, tab, hard, harvTool, harvLvl, resist), ITileEntityProvider {
	
	init {
		isBlockContainer = true
	}
	
	override fun breakBlock(world: World, x: Int, y: Int, z: Int, block: Block?, meta: Int) {
		super.breakBlock(world, x, y, z, block, meta)
		world.removeTileEntity(x, y, z)
	}
	
	override fun onBlockEventReceived(world: World, x: Int, y: Int, z: Int, id: Int, value: Int): Boolean {
		super.onBlockEventReceived(world, x, y, z, id, value)
		val tileentity = world.getTileEntity(x, y, z)
		return tileentity != null && tileentity.receiveClientEvent(id, value)
	}
}
