package alfheim.client.render.block

import alfheim.api.lib.LibRenderIDs
import alfheim.common.block.tile.TileTransferer
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler
import net.minecraft.block.Block
import net.minecraft.client.renderer.RenderBlocks
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
import net.minecraft.world.IBlockAccess
import org.lwjgl.opengl.GL11.*

class RenderBlockTransferer: ISimpleBlockRenderingHandler {
	
	override fun renderInventoryBlock(block: Block, metadata: Int, modelID: Int, renderer: RenderBlocks) {
		glPushMatrix()
		glTranslated(-0.5, -0.5, -0.5)
		
		val spreader = TileTransferer()
		spreader.rotX = -180f
		TileEntityRendererDispatcher.instance.renderTileEntityAt(spreader, 0.0, 0.0, 0.0, 0f)
		glPopMatrix()
	}
	
	override fun renderWorldBlock(world: IBlockAccess, x: Int, y: Int, z: Int, block: Block, modelId: Int, renderer: RenderBlocks): Boolean {
		return false
	}
	
	override fun getRenderId(): Int {
		return LibRenderIDs.idTransferer
	}
	
	override fun shouldRender3DInInventory(modelId: Int): Boolean {
		return true
	}
}