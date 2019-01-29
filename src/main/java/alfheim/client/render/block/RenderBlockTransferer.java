package alfheim.client.render.block;

import static org.lwjgl.opengl.GL11.*;

import alfheim.api.lib.LibRenderIDs;
import alfheim.common.block.tile.TileTransferer;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.world.IBlockAccess;

public class RenderBlockTransferer implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		glPushMatrix();
		glTranslated(-0.5, -0.5, -0.5);

		TileTransferer spreader = new TileTransferer();
		spreader.rotationX = -180;
		TileEntityRendererDispatcher.instance.renderTileEntityAt(spreader, 0, 0, 0, 0);
		glPopMatrix();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		return false;
	}

	@Override
	public int getRenderId() {
		return LibRenderIDs.idTransferer;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}
}