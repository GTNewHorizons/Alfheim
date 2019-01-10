package alfheim.client.render.block;

import org.lwjgl.opengl.GL11;

import alfheim.api.lib.LibRenderIDs;
import alfheim.client.render.tile.RenderTileAlfheimPylons;
import alfheim.common.block.tile.TileAlfheimPylons;
import alfheim.common.block.tile.TileItemHolder;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.world.IBlockAccess;

public class RenderBlockItemHolder implements ISimpleBlockRenderingHandler {

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		GL11.glPushMatrix();
		GL11.glTranslatef(-0.5F, 0.25F, -0.5F);
		TileEntityRendererDispatcher.instance.renderTileEntityAt(new TileItemHolder(), 0.0, 0.0, 0.0, 0.0F);
		GL11.glPopMatrix();
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		return false;
	}

	@Override
	public int getRenderId() {
		return LibRenderIDs.idItemHolder;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}
}
