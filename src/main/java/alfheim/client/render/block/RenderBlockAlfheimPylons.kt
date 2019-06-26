package alfheim.client.render.block

import org.lwjgl.opengl.GL11.*

import alfheim.api.lib.LibRenderIDs
import alfheim.client.render.tile.RenderTileAlfheimPylons
import alfheim.common.block.tile.TileAlfheimPylon
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler
import net.minecraft.block.Block
import net.minecraft.client.renderer.RenderBlocks
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
import net.minecraft.world.IBlockAccess

class RenderBlockAlfheimPylons: ISimpleBlockRenderingHandler {
	
	override fun renderInventoryBlock(block: Block, metadata: Int, modelID: Int, renderer: RenderBlocks) {
		glPushMatrix()
		glTranslated(-0.5, -0.7, -0.5)
		RenderTileAlfheimPylons.orange = metadata == 1
		RenderTileAlfheimPylons.red = metadata == 2
		RenderTileAlfheimPylons.hand = true
		TileEntityRendererDispatcher.instance.renderTileEntityAt(TileAlfheimPylon(), 0.0, 0.0, 0.0, 0f)
		glPopMatrix()
	}
	
	override fun renderWorldBlock(world: IBlockAccess, x: Int, y: Int, z: Int, block: Block, modelId: Int, renderer: RenderBlocks): Boolean {
		return false
	}
	
	override fun getRenderId(): Int {
		return LibRenderIDs.idPylon
	}
	
	override fun shouldRender3DInInventory(modelId: Int): Boolean {
		return true
	}
}
