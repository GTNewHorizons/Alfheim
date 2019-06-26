package alfheim.client.render.block

import org.lwjgl.opengl.GL11.glColor3f

import java.awt.Color

import alfheim.common.block.compat.thaumcraft.BlockAlfheimThaumOre
import alfheim.common.integration.thaumcraft.ThaumcraftAlfheimModule
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.RenderBlocks
import net.minecraft.client.renderer.Tessellator
import net.minecraft.init.Blocks
import net.minecraft.world.IBlockAccess
import thaumcraft.client.renderers.block.BlockRenderer
import thaumcraft.common.blocks.BlockCustomOreItem

class RenderBlockAlfheimThaumOre: BlockRenderer(), ISimpleBlockRenderingHandler {
	
	override fun renderInventoryBlock(block: Block, metad: Int, modelID: Int, renderer: RenderBlocks) {
		block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f)
		renderer.setRenderBoundsFromBlock(block)
		if (metad == 0) {
			BlockRenderer.drawFaces(renderer, block, (block as BlockAlfheimThaumOre).icon[0], false)
		} else if (metad == 7) {
			BlockRenderer.drawFaces(renderer, block, (block as BlockAlfheimThaumOre).icon[3], false)
		} else if (metad < 7) {
			BlockRenderer.drawFaces(renderer, block, (block as BlockAlfheimThaumOre).icon[1], false)
			val c = Color(BlockCustomOreItem.colors[metad])
			val r = c.red.toFloat() / 255.0f
			val g = c.green.toFloat() / 255.0f
			val b = c.blue.toFloat() / 255.0f
			glColor3f(r, g, b)
			block.setBlockBounds(0.005f, 0.005f, 0.005f, 0.995f, 0.995f, 0.995f)
			renderer.setRenderBoundsFromBlock(block)
			BlockRenderer.drawFaces(renderer, block, (block as BlockAlfheimThaumOre).icon[2], false)
			glColor3f(1.0f, 1.0f, 1.0f)
		}
	}
	
	override fun renderWorldBlock(world: IBlockAccess, x: Int, y: Int, z: Int, block: Block, modelId: Int, renderer: RenderBlocks): Boolean {
		val bb = BlockRenderer.setBrightness(world, x, y, z, block)
		val metadata = world.getBlockMetadata(x, y, z)
		block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f)
		renderer.setRenderBoundsFromBlock(block)
		renderer.renderStandardBlock(block, x, y, z)
		if (metadata != 0 && metadata < 7) {
			val t = Tessellator.instance
			t.setColorOpaque_I(BlockCustomOreItem.colors[metadata])
			t.setBrightness(Math.max(bb, 160))
			BlockRenderer.renderAllSides(world, x, y, z, block, renderer, (block as BlockAlfheimThaumOre).icon[2], false)
			if (Minecraft.getMinecraft().gameSettings.anisotropicFiltering > 1) {
				block.setBlockBounds(0.005f, 0.005f, 0.005f, 0.995f, 0.995f, 0.995f)
				renderer.setRenderBoundsFromBlock(block)
				t.setBrightness(bb)
				BlockRenderer.renderAllSides(world, x, y, z, block, renderer, Blocks.stone.getIcon(0, 0), false)
			}
		}
		
		renderer.clearOverrideBlockTexture()
		block.setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f)
		renderer.setRenderBoundsFromBlock(block)
		return true
	}
	
	override fun shouldRender3DInInventory(modelId: Int): Boolean {
		return true
	}
	
	override fun getRenderId(): Int {
		return ThaumcraftAlfheimModule.renderIDOre
	}
}