package alfheim.client.render.block

import alfheim.api.lib.LibRenderIDs
import alfheim.common.block.BlockFunnel
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler
import net.minecraft.block.Block
import net.minecraft.client.renderer.*
import net.minecraft.world.IBlockAccess

object RenderBlockHopper: ISimpleBlockRenderingHandler {
	
	override fun renderWorldBlock(world: IBlockAccess, x: Int, y: Int, z: Int, block: Block, modelId: Int, renderer: RenderBlocks): Boolean {
		val tessellator = Tessellator.instance
		tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z))
		val l = block.colorMultiplier(world, x, y, z)
		var f = (l shr 16 and 255).toFloat() / 255.0f
		var f1 = (l shr 8 and 255).toFloat() / 255.0f
		var f2 = (l and 255).toFloat() / 255.0f
		
		if (EntityRenderer.anaglyphEnable) {
			val f3 = (f * 30.0f + f1 * 59.0f + f2 * 11.0f) / 100.0f
			val f4 = (f * 30.0f + f1 * 70.0f) / 100.0f
			val f5 = (f * 30.0f + f2 * 70.0f) / 100.0f
			f = f3
			f1 = f4
			f2 = f5
		}
		
		tessellator.setColorOpaque_F(f, f1, f2)
		val meta = world.getBlockMetadata(x, y, z)
		
		val i1 = BlockFunnel.getDirectionFromMetadata(meta)
		val d0 = 0.625
		renderer.setRenderBounds(0.0, d0, 0.0, 1.0, 1.0, 1.0)
		
		renderer.renderStandardBlock(block, x, y, z)
		
		tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z))
		val j1 = block.colorMultiplier(renderer.blockAccess, x, y, z)
		var g = (j1 shr 16 and 255).toFloat() / 255.0f
		f1 = (j1 shr 8 and 255).toFloat() / 255.0f
		var g1 = (j1 and 255).toFloat() / 255.0f
		
		if (EntityRenderer.anaglyphEnable) {
			val f3 = (g * 30.0f + f1 * 59.0f + g1 * 11.0f) / 100.0f
			val f4 = (g * 30.0f + f1 * 70.0f) / 100.0f
			val f5 = (g * 30.0f + g1 * 70.0f) / 100.0f
			g = f3
			f1 = f4
			g1 = f5
		}
		
		tessellator.setColorOpaque_F(g, f1, g1)
		
		val iicon = BlockFunnel.getHopperIcon("funnel_outside")
		val iicon1 = BlockFunnel.getHopperIcon("funnel_inside")
		f1 = 0.125f
		
		renderer.renderFaceXPos(block, (x.toFloat() - 1.0f + f1).toDouble(), y.toDouble(), z.toDouble(), iicon)
		renderer.renderFaceXNeg(block, (x.toFloat() + 1.0f - f1).toDouble(), y.toDouble(), z.toDouble(), iicon)
		renderer.renderFaceZPos(block, x.toDouble(), y.toDouble(), (z.toFloat() - 1.0f + f1).toDouble(), iicon)
		renderer.renderFaceZNeg(block, x.toDouble(), y.toDouble(), (z.toFloat() + 1.0f - f1).toDouble(), iicon)
		renderer.renderFaceYPos(block, x.toDouble(), (y.toFloat() - 1.0f).toDouble() + d0, z.toDouble(), iicon1)
		
		renderer.setOverrideBlockTexture(iicon)
		val d3 = 0.25
		val d4 = 0.25
		renderer.setRenderBounds(d3, d4, d3, 1.0 - d3, d0 - 0.002, 1.0 - d3)
		
		renderer.renderStandardBlock(block, x, y, z)
		
		val d1 = 0.375
		val d2 = 0.25
		renderer.setOverrideBlockTexture(iicon)
		
		if (i1 == 0) {
			renderer.setRenderBounds(d1, 0.0, d1, 1.0 - d1, 0.25, 1.0 - d1)
			renderer.renderStandardBlock(block, x, y, z)
		}
		
		if (i1 == 2) {
			renderer.setRenderBounds(d1, d4, 0.0, 1.0 - d1, d4 + d2, d3)
			renderer.renderStandardBlock(block, x, y, z)
		}
		
		if (i1 == 3) {
			renderer.setRenderBounds(d1, d4, 1.0 - d3, 1.0 - d1, d4 + d2, 1.0)
			renderer.renderStandardBlock(block, x, y, z)
		}
		
		if (i1 == 4) {
			renderer.setRenderBounds(0.0, d4, d1, d3, d4 + d2, 1.0 - d1)
			renderer.renderStandardBlock(block, x, y, z)
		}
		
		if (i1 == 5) {
			renderer.setRenderBounds(1.0 - d3, d4, d1, 1.0, d4 + d2, 1.0 - d1)
			renderer.renderStandardBlock(block, x, y, z)
		}
		
		renderer.clearOverrideBlockTexture()
		
		return true
	}
	
	override fun renderInventoryBlock(block: Block, metadata: Int, modelID: Int, renderer: RenderBlocks) = Unit
	override fun shouldRender3DInInventory(modelId: Int) = false
	override fun getRenderId() = LibRenderIDs.idHopper
}