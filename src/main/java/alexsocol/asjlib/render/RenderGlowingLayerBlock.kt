package alexsocol.asjlib.render

import cpw.mods.fml.client.registry.*
import net.minecraft.block.Block
import net.minecraft.client.renderer.*
import net.minecraft.world.IBlockAccess
import org.lwjgl.opengl.GL11.*

class RenderGlowingLayerBlock: ISimpleBlockRenderingHandler {
	
	override fun renderInventoryBlock(block: Block, metadata: Int, modelID: Int, renderer: RenderBlocks) {
		renderInvNormalBlock(renderer, block, metadata)
	}
	
	override fun renderWorldBlock(world: IBlockAccess, x: Int, y: Int, z: Int, block: Block, modelId: Int, renderer: RenderBlocks): Boolean {
		val meta = world.getBlockMetadata(x, y, z)
		val tes = Tessellator.instance
		renderer.renderStandardBlock(block, x, y, z)
		tes.draw()
		
		glPushMatrix()
		block as IGlowingLayerBlock
		tes.startDrawingQuads()
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f)
		tes.setColorRGBA_F(1f, 1f, 1f, 1f)
		if (block.getGlowIcon(0, meta) != null && block.shouldSideBeRendered(world, x, y - 1, z, 0)) renderer.renderFaceYNeg(block, x.toDouble(), y.toDouble(), z.toDouble(), block.getGlowIcon(0, meta))
		if (block.getGlowIcon(1, meta) != null && block.shouldSideBeRendered(world, x, y + 1, z, 1)) renderer.renderFaceYPos(block, x.toDouble(), y.toDouble(), z.toDouble(), block.getGlowIcon(1, meta))
		if (block.getGlowIcon(2, meta) != null && block.shouldSideBeRendered(world, x + 1, y, z, 2)) renderer.renderFaceXPos(block, x.toDouble(), y.toDouble(), z.toDouble(), block.getGlowIcon(2, meta))
		if (block.getGlowIcon(3, meta) != null && block.shouldSideBeRendered(world, x - 1, y, z, 3)) renderer.renderFaceXNeg(block, x.toDouble(), y.toDouble(), z.toDouble(), block.getGlowIcon(3, meta))
		if (block.getGlowIcon(4, meta) != null && block.shouldSideBeRendered(world, x, y, z - 1, 4)) renderer.renderFaceZNeg(block, x.toDouble(), y.toDouble(), z.toDouble(), block.getGlowIcon(4, meta))
		if (block.getGlowIcon(5, meta) != null && block.shouldSideBeRendered(world, x, y, z + 1, 5)) renderer.renderFaceZPos(block, x.toDouble(), y.toDouble(), z.toDouble(), block.getGlowIcon(5, meta))
		tes.draw()
		
		glPopMatrix()
		
		tes.startDrawingQuads()
		return false
	}
	
	override fun shouldRender3DInInventory(modelId: Int): Boolean {
		return true
	}
	
	override fun getRenderId(): Int {
		return glowBlockID
	}
	
	companion object {
		
		val glowBlockID = RenderingRegistry.getNextAvailableRenderId()
		
		init {
			RenderingRegistry.registerBlockHandler(glowBlockID, RenderGlowingLayerBlock())
		}
		
		fun renderInvNormalBlock(renderer: RenderBlocks, block: Block, meta: Int) {
			glPushMatrix()
			val tes = Tessellator.instance
			glRotated(90.0, 0.0, 1.0, 0.0)
			glTranslated(-0.5, -0.5, -0.5)
			glColor4d(1.0, 1.0, 1.0, 1.0)
			renderer.setRenderBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0)
			tes.startDrawingQuads()
			tes.setNormal(0f, 0.8f, 0f)
			renderer.renderFaceYNeg(block, 0.0, 0.0, 0.0, block.getIcon(0, meta))
			tes.setNormal(0f, 0.8f, 0f)
			renderer.renderFaceYPos(block, 0.0, 0.0, 0.0, block.getIcon(1, meta))
			tes.setNormal(0f, 0f, 1f)
			renderer.renderFaceXPos(block, 0.0, 0.0, 0.0, block.getIcon(2, meta))
			tes.setNormal(0f, 0f, -1f)
			renderer.renderFaceXNeg(block, 0.0, 0.0, 0.0, block.getIcon(3, meta))
			tes.setNormal(0f, 0f, 0f)
			renderer.renderFaceZNeg(block, 0.0, 0.0, 0.0, block.getIcon(4, meta))
			tes.setNormal(-0.5f, 0f, 0f)
			renderer.renderFaceZPos(block, 0.0, 0.0, 0.0, block.getIcon(5, meta))
			tes.draw()
			
			glDisable(GL_LIGHTING)
			tes.startDrawingQuads()
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f)
			tes.setNormal(0f, 0.8f, 0f)
			renderer.renderFaceYNeg(block, 0.0, 0.0, 0.0, (block as IGlowingLayerBlock).getGlowIcon(0, meta))
			tes.setNormal(0f, 0.8f, 0f)
			renderer.renderFaceYPos(block, 0.0, 0.0, 0.0, (block as IGlowingLayerBlock).getGlowIcon(1, meta))
			tes.setNormal(0f, 0f, 1f)
			renderer.renderFaceXPos(block, 0.0, 0.0, 0.0, (block as IGlowingLayerBlock).getGlowIcon(2, meta))
			tes.setNormal(0f, 0f, -1f)
			renderer.renderFaceXNeg(block, 0.0, 0.0, 0.0, (block as IGlowingLayerBlock).getGlowIcon(3, meta))
			tes.setNormal(0f, 0f, 0f)
			renderer.renderFaceZNeg(block, 0.0, 0.0, 0.0, (block as IGlowingLayerBlock).getGlowIcon(4, meta))
			tes.setNormal(-0.5f, 0f, 0f)
			renderer.renderFaceZPos(block, 0.0, 0.0, 0.0, (block as IGlowingLayerBlock).getGlowIcon(5, meta))
			tes.draw()
			glEnable(GL_LIGHTING)
			glPopMatrix()
		}
	}
}