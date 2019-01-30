package alfheim.client.render.tile;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

import alexsocol.asjlib.extendables.ItemContainingTileEntity;
import alfheim.api.lib.LibResourceLocations;
import alfheim.common.block.tile.TileTransferer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.model.ModelSpreader;

public class RenderTileTransferer extends TileEntitySpecialRenderer {

	private static final ModelSpreader model = new ModelSpreader();

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float ticks) {
		TileTransferer spreader = (TileTransferer) tileentity;
		glPushMatrix();
		glEnable(GL_RESCALE_NORMAL);
		glColor4f(1F, 1F, 1F, 1F);
		glTranslated(d0, d1, d2);
		
		glTranslatef(0.5F, 1.5F, 0.5F);

		ItemContainingTileEntity.renderItem(spreader);
		glRotatef(spreader.rotationX + 90F, 0F, 1F, 0F);
		glTranslatef(0F, -1F, 0F);
		glRotatef(spreader.rotationY, 1F, 0F, 0F);
		glTranslatef(0F, 1F, 0F);

		Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.spreader);
		glScalef(1F, -1F, -1F);

		double time = ClientTickHandler.ticksInGame + ticks;

		model.render();
		glColor3f(1F, 1F, 1F);

		glPushMatrix();
		double worldTicks = tileentity.getWorldObj() == null ? 0 : time;
		glRotatef((float) worldTicks % 360, 0F, 1F, 0F);
		glTranslatef(0F, (float) Math.sin(worldTicks / 20.0) * 0.05F, 0F);
		model.renderCube();
		glPopMatrix();

		glEnable(GL_RESCALE_NORMAL);
		glPopMatrix();
	}
}