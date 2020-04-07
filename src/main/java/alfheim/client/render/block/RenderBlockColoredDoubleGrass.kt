package alfheim.client.render.block

import alexsocol.asjlib.*
import alfheim.api.lib.LibRenderIDs
import alfheim.common.block.base.IDoublePlant
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler
import net.minecraft.block.Block
import net.minecraft.client.renderer.*
import net.minecraft.world.IBlockAccess

object RenderBlockColoredDoubleGrass: ISimpleBlockRenderingHandler {
	
	override fun renderWorldBlock(world: IBlockAccess, x: Int, y: Int, z: Int, block: Block, modelId: Int, renderer: RenderBlocks): Boolean {
		val tessellator = Tessellator.instance
		tessellator.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z))
		val l = if ((world.getBlockMetadata(x, y, z) and 8) != 0) {
			block.colorMultiplier(world, x, y - 1, z)
		} else {
			block.colorMultiplier(world, x, y, z)
		}
		var f = (l shr 16 and 255).F / 255f
		var f1 = (l shr 8 and 255).F / 255f
		var f2 = (l and 255).F / 255f
		if (EntityRenderer.anaglyphEnable) {
			val f3 = (f * 30f + f1 * 59f + f2 * 11f) / 100f
			val f4 = (f * 30f + f1 * 70f) / 100f
			val f5 = (f * 30f + f2 * 70f) / 100f
			f = f3
			f1 = f4
			f2 = f5
		}
		tessellator.setColorOpaque_F(f, f1, f2)
		var j1 = (x * 3129871).toLong() xor z.toLong() * 116129781L
		j1 = j1 * j1 * 42317861L + j1 * 11L
		var d19 = x.D
		val d0 = y.D
		var d1 = z.D
		d19 += (((j1 shr 16 and 15L).F / 15f).D - 0.5) * 0.3
		d1 += (((j1 shr 24 and 15L).F / 15f).D - 0.5) * 0.3
		val i1 = world.getBlockMetadata(x, y, z)
		val flag = (i1 and 8) != 0
		if (flag && world.getBlock(x, y - 1, z) !== block && world.getBlock(x, y - 1, z) !== block) {
			return false
		}
		
		if (block is IDoublePlant) {
			val iicon = if ((i1 and 8) != 0) block.getTopIcon(world.getBlockMetadata(x, y - 1, z)) else block.getBottomIcon(world.getBlockMetadata(x, y, z))
			renderer.drawCrossedSquares(iicon, d19, d0, d1, 1f)
		}
		return true
	}
	
	override fun renderInventoryBlock(block: Block, metadata: Int, modelID: Int, renderer: RenderBlocks) = Unit
	override fun shouldRender3DInInventory(modelId: Int) = false
	override fun getRenderId() = LibRenderIDs.idDoubleFlower
}