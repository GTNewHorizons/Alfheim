package alfheim.client.render.block;

import static org.lwjgl.opengl.GL11.*;

import alfheim.api.lib.LibRenderIDs;
import alfheim.common.block.tile.TileAnyavil;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.world.IBlockAccess;

public class RenderBlockAnyavil implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		glPushMatrix();
		glRotated(-90, 0, 1, 0);
		glTranslated(-0.5, -0.6, -0.5);
		TileEntityRendererDispatcher.instance.renderTileEntityAt(new TileAnyavil(), 0, 0, 0, 0);
		glPopMatrix();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		return false;
	}

	@Override
	public int getRenderId() {
		return LibRenderIDs.idAnyavil;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}
}