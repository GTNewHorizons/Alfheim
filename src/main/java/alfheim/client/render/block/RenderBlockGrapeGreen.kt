package alfheim.client.render.block

import alfheim.api.lib.LibRenderIDs
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler
import net.minecraft.block.Block
import net.minecraft.client.renderer.RenderBlocks
import net.minecraft.world.IBlockAccess

object RenderBlockGrapeGreen: ISimpleBlockRenderingHandler {
	
	override fun renderWorldBlock(world: IBlockAccess, x: Int, y: Int, z: Int, block: Block, modelId: Int, renderer: RenderBlocks): Boolean {
		renderer.renderBlockLilyPad(block, x, y, z)
		renderer.renderCrossedSquares(block, x, y, z)
		return true
	}
	
	override fun renderInventoryBlock(block: Block?, metadata: Int, modelId: Int, renderer: RenderBlocks?) = Unit
	override fun shouldRender3DInInventory(modelId: Int) = false
	override fun getRenderId() = LibRenderIDs.idGrapeWhite
}
