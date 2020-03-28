package alfheim.client.render.block

import alexsocol.asjlib.glTranslated
import alfheim.api.lib.*
import alfheim.client.render.tile.RenderTilePowerStone
import alfheim.common.block.tile.TilePowerStone
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler
import net.minecraft.block.Block
import net.minecraft.client.renderer.RenderBlocks
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
import net.minecraft.world.IBlockAccess
import org.lwjgl.opengl.GL11.*
import kotlin.math.*

object RenderBlockPowerStone: ISimpleBlockRenderingHandler {
	
	override fun renderInventoryBlock(block: Block, metadata: Int, modelID: Int, renderer: RenderBlocks) {
		glPushMatrix()
		glTranslated(-0.5)
		RenderTilePowerStone.forceMeta = max(0, min(metadata, LibResourceLocations.obelisk.size))
		
		TileEntityRendererDispatcher.instance.renderTileEntityAt(TilePowerStone(), 0.0, 0.0, 0.0, 0f)
		glPopMatrix()
	}
	
	override fun renderWorldBlock(world: IBlockAccess, x: Int, y: Int, z: Int, block: Block, modelId: Int, renderer: RenderBlocks) = false
	override fun shouldRender3DInInventory(modelId: Int) = true
	override fun getRenderId() = LibRenderIDs.idPowerStone
}
