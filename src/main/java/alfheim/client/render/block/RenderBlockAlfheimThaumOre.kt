package alfheim.client.render.block

import alexsocol.asjlib.*
import alfheim.common.block.compat.thaumcraft.BlockAlfheimThaumOre
import alfheim.common.integration.thaumcraft.ThaumcraftAlfheimModule
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler
import net.minecraft.block.Block
import net.minecraft.client.renderer.*
import net.minecraft.init.Blocks
import net.minecraft.world.IBlockAccess
import org.lwjgl.opengl.GL11.glColor3f
import thaumcraft.client.renderers.block.BlockRenderer
import thaumcraft.common.blocks.BlockCustomOreItem
import java.awt.Color
import kotlin.math.max

class RenderBlockAlfheimThaumOre: BlockRenderer(), ISimpleBlockRenderingHandler {
	
	override fun renderInventoryBlock(block: Block, metad: Int, modelID: Int, renderer: RenderBlocks) {
		block.setBlockBounds(0f, 0f, 0f, 1f, 1f, 1f)
		renderer.setRenderBoundsFromBlock(block)
		when {
			metad == 0 -> drawFaces(renderer, block, BlockAlfheimThaumOre.icon[0], false)
			metad == 7 -> drawFaces(renderer, block, BlockAlfheimThaumOre.icon[3], false)
			
			metad < 7  -> {
				drawFaces(renderer, block, BlockAlfheimThaumOre.icon[1], false)
				val c = Color(BlockCustomOreItem.colors[metad])
				val r = c.red.F / 255f
				val g = c.green.F / 255f
				val b = c.blue.F / 255f
				glColor3f(r, g, b)
				block.setBlockBounds(0.005f, 0.005f, 0.005f, 0.995f, 0.995f, 0.995f)
				renderer.setRenderBoundsFromBlock(block)
				drawFaces(renderer, block, BlockAlfheimThaumOre.icon[2], false)
				block.setBlockBounds(0f, 0f, 0f, 1f, 1f, 1f)
				glColor3f(1f, 1f, 1f)
			}
		}
	}
	
	override fun renderWorldBlock(world: IBlockAccess, x: Int, y: Int, z: Int, block: Block, modelId: Int, renderer: RenderBlocks): Boolean {
		val bb = setBrightness(world, x, y, z, block)
		val metadata = world.getBlockMetadata(x, y, z)
		block.setBlockBounds(0f, 0f, 0f, 1f, 1f, 1f)
		renderer.setRenderBoundsFromBlock(block)
		renderer.renderStandardBlock(block, x, y, z)
		if (metadata != 0 && metadata < 7) {
			val t = Tessellator.instance
			t.setColorOpaque_I(BlockCustomOreItem.colors[metadata])
			t.setBrightness(max(bb, 160))
			renderAllSides(world, x, y, z, block, renderer, BlockAlfheimThaumOre.icon[2], false)
			if (mc.gameSettings.anisotropicFiltering > 1) {
				block.setBlockBounds(0.005f, 0.005f, 0.005f, 0.995f, 0.995f, 0.995f)
				renderer.setRenderBoundsFromBlock(block)
				t.setBrightness(bb)
				renderAllSides(world, x, y, z, block, renderer, Blocks.stone.getIcon(0, 0), false)
			}
		}
		
		renderer.clearOverrideBlockTexture()
		block.setBlockBounds(0f, 0f, 0f, 1f, 1f, 1f)
		renderer.setRenderBoundsFromBlock(block)
		return true
	}
	
	override fun shouldRender3DInInventory(modelId: Int) = true
	override fun getRenderId() = ThaumcraftAlfheimModule.renderIDOre
}