package alexsocol.asjlib.render

import cpw.mods.fml.client.registry.*
import net.minecraft.block.Block
import net.minecraft.client.renderer.*
import net.minecraft.world.IBlockAccess
import net.minecraftforge.common.util.ForgeDirection
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
		
		val renderers = arrayOf(RenderBlocks::renderFaceYNeg, RenderBlocks::renderFaceYPos, RenderBlocks::renderFaceXPos, RenderBlocks::renderFaceXNeg, RenderBlocks::renderFaceZPos, RenderBlocks::renderFaceZNeg)
		
		for (dir in ForgeDirection.VALID_DIRECTIONS)
			if (block.getGlowIcon(dir.ordinal, meta) != null && block.shouldSideBeRendered(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, dir.ordinal))
				renderers[dir.ordinal].invoke(renderer, block, x.toDouble(), y.toDouble(), z.toDouble(), block.getGlowIcon(dir.ordinal, meta))
		
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
			block as IGlowingLayerBlock
			
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
			
			if (block.getGlowIcon(0, meta) != null) {
				tes.setNormal(0f, 0.8f, 0f)
				renderer.renderFaceYNeg(block, 0.0, 0.0, 0.0, block.getGlowIcon(0, meta))
			}
			
			if (block.getGlowIcon(1, meta) != null) {
				tes.setNormal(0f, 0.8f, 0f)
				renderer.renderFaceYPos(block, 0.0, 0.0, 0.0, block.getGlowIcon(1, meta))
			}
			
			if (block.getGlowIcon(1, meta) != null) {
				tes.setNormal(0f, 0f, 1f)
				renderer.renderFaceXPos(block, 0.0, 0.0, 0.0, block.getGlowIcon(2, meta))
			}
			
			if (block.getGlowIcon(1, meta) != null) {
				tes.setNormal(0f, 0f, -1f)
				renderer.renderFaceXNeg(block, 0.0, 0.0, 0.0, block.getGlowIcon(3, meta))
			}
			
			if (block.getGlowIcon(1, meta) != null) {
				tes.setNormal(0f, 0f, 0f)
				renderer.renderFaceZNeg(block, 0.0, 0.0, 0.0, block.getGlowIcon(4, meta))
			}
			
			if (block.getGlowIcon(1, meta) != null) {
				tes.setNormal(-0.5f, 0f, 0f)
				renderer.renderFaceZPos(block, 0.0, 0.0, 0.0, block.getGlowIcon(5, meta))
			}
			
			tes.draw()
			glEnable(GL_LIGHTING)
			glPopMatrix()
		}
	}
}