package alfheim.client.render.block

import org.lwjgl.opengl.GL11.*

import alfheim.api.lib.LibRenderIDs
import alfheim.common.block.tile.TileItemHolder
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler
import net.minecraft.block.Block
import net.minecraft.client.renderer.RenderBlocks
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
import net.minecraft.world.IBlockAccess

class RenderBlockItemHolder: ISimpleBlockRenderingHandler {
	
	override fun renderInventoryBlock(block: Block, metadata: Int, modelID: Int, renderer: RenderBlocks) {
		glPushMatrix()
		glTranslated(-0.5, 0.25, -0.5)
		TileEntityRendererDispatcher.instance.renderTileEntityAt(TileItemHolder(), 0.0, 0.0, 0.0, 0f)
		glPopMatrix()
	}
	
	override fun renderWorldBlock(world: IBlockAccess, x: Int, y: Int, z: Int, block: Block, modelId: Int, renderer: RenderBlocks): Boolean {
		return false
	}
	
	override fun getRenderId(): Int {
		return LibRenderIDs.idItemHolder
	}
	
	override fun shouldRender3DInInventory(modelId: Int): Boolean {
		return true
	}
}
