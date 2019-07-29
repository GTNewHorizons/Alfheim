package alfheim.client.render.tile

import alfheim.api.lib.LibRenderIDs
import alfheim.common.block.base.IMultipassRenderer
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler
import net.minecraft.block.Block
import net.minecraft.client.renderer.*
import net.minecraft.world.IBlockAccess
import org.lwjgl.opengl.GL11.*

class MultipassRenderer : ISimpleBlockRenderingHandler {

    companion object {
        var pass = 0
    }

    override fun getRenderId() = LibRenderIDs.idMultipass

    override fun shouldRender3DInInventory(modelId: Int) = true

    override fun renderInventoryBlock(block: Block, metadata: Int, modelID: Int, renderer: RenderBlocks) {
        if (block is IMultipassRenderer) {
            renderInventoryBlock(block.innerBlock(metadata), metadata, 1.0F, renderer, 0)
            renderInventoryBlock(block, metadata, 1.0F, renderer, 1)
        }
    }

    override fun renderWorldBlock(world: IBlockAccess, x: Int, y: Int, z: Int, block: Block, modelId: Int, renderer: RenderBlocks): Boolean {
        if (block is IMultipassRenderer) {
            if (pass == 1) renderer.renderStandardBlock(block, x, y, z)
            else renderer.renderStandardBlock(block.innerBlock(world, x, y, z), x, y, z)
        }
        return true
    }

    fun renderInventoryBlock(block: Block, meta: Int, brightness: Float, renderer: RenderBlocks, stage: Int) {
        val tessellator = Tessellator.instance
        glPushMatrix()
        if (renderer.useInventoryTint) {
            val j = block.getRenderColor(meta)

            val f1 = (j shr 16 and 255).toFloat() / 255.0F
            val f2 = (j shr 8 and 255).toFloat() / 255.0F
            val f3 = (j and 255).toFloat() / 255.0F
            glColor4f(f1 * brightness, f2 * brightness, f3 * brightness, 1.0F)
        }

        block.setBlockBoundsForItemRender()
        renderer.setRenderBoundsFromBlock(block)
        glRotatef(90.0f, 0.0f, 1.0f, 0.0f)
        glTranslatef(-0.5f, -0.5f, -0.5f)

        val zf = stage.toFloat() / 200f
        glTranslatef(-zf, -zf, -zf)
        glScalef(1f + zf * 2f, 1f + zf * 2f, 1f + zf * 2f)
        tessellator.startDrawingQuads()
        tessellator.setNormal(0.0f, -1.0f, 0.0f)
        renderer.renderFaceYNeg(block, 0.0, 0.0, 0.0, renderer.getBlockIconFromSideAndMetadata(block, 0, meta))
        tessellator.draw()
        tessellator.startDrawingQuads()
        tessellator.setNormal(0.0f, 1.0f, 0.0f)
        renderer.renderFaceYPos(block, 0.0, 0.0, 0.0, renderer.getBlockIconFromSideAndMetadata(block, 1, meta))
        tessellator.draw()
        tessellator.startDrawingQuads()
        tessellator.setNormal(0.0f, 0.0f, -1.0f)
        renderer.renderFaceZNeg(block, 0.0, 0.0, 0.0, renderer.getBlockIconFromSideAndMetadata(block, 2, meta))
        tessellator.draw()
        tessellator.startDrawingQuads()
        tessellator.setNormal(0.0f, 0.0f, 1.0f)
        renderer.renderFaceZPos(block, 0.0, 0.0, 0.0, renderer.getBlockIconFromSideAndMetadata(block, 3, meta))
        tessellator.draw()
        tessellator.startDrawingQuads()
        tessellator.setNormal(-1.0f, 0.0f, 0.0f)
        renderer.renderFaceXNeg(block, 0.0, 0.0, 0.0, renderer.getBlockIconFromSideAndMetadata(block, 4, meta))
        tessellator.draw()
        tessellator.startDrawingQuads()
        tessellator.setNormal(1.0f, 0.0f, 0.0f)
        renderer.renderFaceXPos(block, 0.0, 0.0, 0.0, renderer.getBlockIconFromSideAndMetadata(block, 5, meta))
        tessellator.draw()
        glTranslatef(0.5f + zf, 0.5f + zf, 0.5f + zf)
        glPopMatrix()
    }
}
