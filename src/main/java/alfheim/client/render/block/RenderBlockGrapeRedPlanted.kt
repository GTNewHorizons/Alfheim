package alfheim.client.render.block

import alfheim.api.lib.LibRenderIDs
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler
import net.minecraft.block.*
import net.minecraft.client.renderer.RenderBlocks
import net.minecraft.init.Blocks
import net.minecraft.world.IBlockAccess

object RenderBlockGrapeRedPlanted: ISimpleBlockRenderingHandler {
	
	override fun renderWorldBlock(world: IBlockAccess, x: Int, y: Int, z: Int, block: Block, modelId: Int, renderer: RenderBlocks): Boolean {
		renderBlockFence(world, x, y, z, renderer)
		
		val m = 0.0001f
		val s = when (world.getBlockMetadata(x, y, z)) {
					0    -> 5f
					1    -> 3f
					else -> 1f
				} / 16
		
		var h = 1 - s + 1f / 16 - m
		if (world.getBlock(x, y + 1, z) === block) h = 1f - m
		
		val minX = if (world.getBlock(x - 1, y, z) === block) m else s
		val minY = if (world.isAirBlock(x, y - 1, z)) s else m
		val minZ = if (world.getBlock(x, y, z - 1) === block) m else s
		val maxX = if (world.getBlock(x + 1, y, z) === block) m else s
		val maxZ = if (world.getBlock(x, y, z + 1) === block) m else s
		
		renderer.setRenderBoundsFromBlock(block.apply { setBlockBounds(minX, minY, minZ, 1 - maxX, h, 1 - maxZ) })
		renderer.renderStandardBlock(block, x, y, z)
		renderer.setRenderBoundsFromBlock(block.apply { setBlockBounds(0f, 0f, 0f, 1f, 1f, 1f) })
		return true
	}
	
	fun renderBlockFence(world: IBlockAccess, x: Int, y: Int, z: Int, renderer: RenderBlocks): Boolean {
		val fence: BlockFence = Blocks.fence as BlockFence
		
		if (!world.isAirBlock(x, y + 1, z) || !world.isAirBlock(x, y - 1, z)) {
			val f = 0.375
			val f1 = 0.625
			
			renderer.setRenderBounds(f, 0.0, f, f1, 1.0, f1)
			renderer.renderStandardBlock(fence, x, y, z)
		}
		
		var flag = true
		var flag1 = false
		var flag2 = false
		if (fence.canConnectFenceTo(world, x - 1, y, z) || fence.canConnectFenceTo(world, x + 1, y, z)) flag1 = true
		if (fence.canConnectFenceTo(world, x, y, z - 1) || fence.canConnectFenceTo(world, x, y, z + 1)) flag2 = true
		val flag3 = fence.canConnectFenceTo(world, x - 1, y, z)
		val flag4 = fence.canConnectFenceTo(world, x + 1, y, z)
		val flag5 = fence.canConnectFenceTo(world, x, y, z - 1)
		val flag6 = fence.canConnectFenceTo(world, x, y, z + 1)
		if (!flag1 && !flag2) flag1 = true
		val f = 0.4375
		val f1 = 0.5625
		var f2 = 0.75
		var f3 = 0.9375
		val f4 = if (flag3) 0.0 else f
		val f5 = if (flag4) 1.0 else f1
		val f6 = if (flag5) 0.0 else f
		val f7 = if (flag6) 1.0 else f1
		renderer.field_152631_f = true
		if (flag1) {
			renderer.setRenderBounds(f4, f2, f, f5, f3, f1)
			renderer.renderStandardBlock(fence, x, y, z)
			flag = true
		}
		if (flag2) {
			renderer.setRenderBounds(f, f2, f6, f1, f3, f7)
			renderer.renderStandardBlock(fence, x, y, z)
			flag = true
		}
		f2 = 0.375
		f3 = 0.5625
		if (flag1) {
			renderer.setRenderBounds(f4, f2, f, f5, f3, f1)
			renderer.renderStandardBlock(fence, x, y, z)
			flag = true
		}
		if (flag2) {
			renderer.setRenderBounds(f, f2, f6, f1, f3, f7)
			renderer.renderStandardBlock(fence, x, y, z)
			flag = true
		}
		renderer.field_152631_f = false
		fence.setBlockBoundsBasedOnState(world, x, y, z)
		return flag
	}
	
	override fun renderInventoryBlock(block: Block?, metadata: Int, modelId: Int, renderer: RenderBlocks?) = Unit
	override fun shouldRender3DInInventory(modelId: Int) = false
	override fun getRenderId() = LibRenderIDs.idGrapeRedPlanted
}
