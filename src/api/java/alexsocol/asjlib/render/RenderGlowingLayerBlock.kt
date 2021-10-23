package alexsocol.asjlib.render

import alexsocol.asjlib.*
import cpw.mods.fml.client.registry.*
import net.minecraft.block.Block
import net.minecraft.client.renderer.*
import net.minecraft.util.IIcon
import net.minecraft.world.IBlockAccess
import net.minecraftforge.common.util.ForgeDirection
import org.lwjgl.opengl.GL11.*

class RenderGlowingLayerBlock: ISimpleBlockRenderingHandler {
	
	override fun renderWorldBlock(world: IBlockAccess, x: Int, y: Int, z: Int, block: Block, modelId: Int, renderer: RenderBlocks): Boolean {
		renderer.renderStandardBlock(block, x, y, z)
		
		Tessellator.instance.setColorOpaque_I(0xFFFFFF)
		Tessellator.instance.setBrightness(240)
		renderAllSides(world, x, y, z, block, renderer, false) { s, m -> (block as IGlowingLayerBlock).getGlowIcon(s, m) }
		
		return true
	}
	
	override fun renderInventoryBlock(block: Block, meta: Int, modelID: Int, renderer: RenderBlocks) {
		block as IGlowingLayerBlock
		
		renderer.setRenderBoundsFromBlock(block)
		drawFaces(renderer, block) { block.getIcon(it, meta) }
		
		glDisable(GL_LIGHTING)
		val lastX = OpenGlHelper.lastBrightnessX
		val lastY = OpenGlHelper.lastBrightnessY
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f)
		drawFaces(renderer, block) { block.getGlowIcon(it, meta) }
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastX, lastY)
		glEnable(GL_LIGHTING)
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
		
		val renderers = arrayOf(RenderBlocks::renderFaceYNeg, RenderBlocks::renderFaceYPos, RenderBlocks::renderFaceZNeg, RenderBlocks::renderFaceZPos, RenderBlocks::renderFaceXNeg, RenderBlocks::renderFaceXPos)
		
		fun renderAllSides(world: IBlockAccess, x: Int, y: Int, z: Int, block: Block, renderer: RenderBlocks, allsides: Boolean, getIcon: (Int, Int) -> IIcon?) {
			for ((i, d) in ForgeDirection.VALID_DIRECTIONS.withIndex()) if (allsides || block.shouldSideBeRendered(world, x + d.offsetX, y + d.offsetY, z + d.offsetZ, i)) getIcon.invoke(i, world.getBlockMetadata(x, y, z))?.let { renderers[i].invoke(renderer, block, x.D, y.D, z.D, it) }
		}
		
		fun drawFaces(renderblocks: RenderBlocks, block: Block, getIcon: (Int) -> IIcon?) {
			for ((i, d) in ForgeDirection.VALID_DIRECTIONS.withIndex()) {
				Tessellator.instance.startDrawingQuads()
				Tessellator.instance.setNormal(d.offsetX.F, d.offsetY.F, d.offsetZ.F)
				getIcon.invoke(i)?.let { renderers[i].invoke(renderblocks, block, -0.5, -0.5, -0.5, it) }
				Tessellator.instance.draw()
			}
		}
	}
}