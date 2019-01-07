package alfheim.client.render.tile;

import static org.lwjgl.opengl.GL11.*;

import alexsocol.asjlib.extendables.ItemContainingTileEntity;
import alfheim.common.block.tile.TileItemHolder;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;

public class RenderTileItemHolder extends TileEntitySpecialRenderer {

	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
		renderTE((TileItemHolder)tile, x, y, z, partialTicks);		
	}
	
	private void renderTE(TileItemHolder tile, double x, double y, double z, float partialTicks) {
		glPushMatrix();
		glTranslated(x + 0.5, y + 0.5, z + 0.5);

		/*GL11.glRotated(90, 1, 0, 0);
		GL11.glRotated(135, 0, 0, 1);
		GL11.glTranslated(0, 0.07, -0.6);
		GL11.glScaled(1.5, 1.5, 1.5);*/

		ItemContainingTileEntity.renderItem(tile);
		glPopMatrix();
	}
}