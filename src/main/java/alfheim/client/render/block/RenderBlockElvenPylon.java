package alfheim.client.render.block;

import org.lwjgl.opengl.GL11;

import alfheim.client.core.proxy.ClientProxy;
import alfheim.common.block.tile.TileElvenPylon;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.world.IBlockAccess;

public class RenderBlockElvenPylon implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		GL11.glPushMatrix();
		GL11.glTranslatef(-0.5F, -0.7F, -0.5F);
		TileEntityRendererDispatcher.instance.renderTileEntityAt(new TileElvenPylon(), 0.0D, 0.0D, 0.0D, 0.0F);
		GL11.glPopMatrix();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		return false;
	}

	@Override
	public int getRenderId() {
		return ClientProxy.idPylon;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}
}
