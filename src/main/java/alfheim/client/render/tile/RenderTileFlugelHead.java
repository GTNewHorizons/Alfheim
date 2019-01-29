package alfheim.client.render.tile;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

import alfheim.api.lib.LibResourceLocations;
import alfheim.client.model.entity.ModelBipedNew;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.tileentity.TileEntitySkull;

public class RenderTileFlugelHead extends TileEntitySkullRenderer {

	@Override
	public void renderTileEntityAt(TileEntitySkull skull, double x, double y, double z, float ticks) {
		render(skull, x, y, z, skull.getBlockMetadata() & 7, skull.func_145906_b() * 360 / 16.0F, skull.func_145904_a());
	}

	public void render(TileEntitySkull skull, double x, double y, double z, int meta, float rotation, int type) {
		bindTexture(LibResourceLocations.jibril);
		glPushMatrix();
		glDisable(GL_CULL_FACE);
		if (meta != 1) {
			switch (meta) {
			case 2:
				glTranslated(x + 0.5, y + 0.25, z + 0.74);
				break;
			case 3:
				glTranslated(x + 0.5, y + 0.25, z + 0.26);
				rotation = 180;
				break;
			case 4:
				glTranslated(x + 0.74, y + 0.25, z + 0.5);
				rotation = 270;
				break;
			case 5:
			default:
				glTranslated(x + 0.26, y + 0.25, z + 0.5);
				rotation = 90;
			}
		} else glTranslated(x + 0.5, y, z + 0.5);

		glEnable(GL_RESCALE_NORMAL);
		glScaled(-1, -1, 1);
		glEnable(GL_ALPHA_TEST);
		glRotated(rotation, 0, 1, 0);
		ModelBipedNew.model.head.render(0.0625F);

		glPopMatrix();
	}
}