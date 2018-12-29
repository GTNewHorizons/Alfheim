package alfheim.client.render.tile;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

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
		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		GL11.glTranslated(d0, d1, d2);
		
		GL11.glTranslatef(0.5F, 1.5F, 0.5F);

		ItemContainingTileEntity.renderItem(spreader);
		GL11.glRotatef(spreader.rotationX + 90F, 0F, 1F, 0F);
		GL11.glTranslatef(0F, -1F, 0F);
		GL11.glRotatef(spreader.rotationY, 1F, 0F, 0F);
		GL11.glTranslatef(0F, 1F, 0F);

		Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.spreader);
		GL11.glScalef(1F, -1F, -1F);

		double time = ClientTickHandler.ticksInGame + ticks;

		model.render();
		GL11.glColor3f(1F, 1F, 1F);

		GL11.glPushMatrix();
		double worldTicks = tileentity.getWorldObj() == null ? 0 : time;
		GL11.glRotatef((float) worldTicks % 360, 0F, 1F, 0F);
		GL11.glTranslatef(0F, (float) Math.sin(worldTicks / 20.0) * 0.05F, 0F);
		model.renderCube();
		GL11.glPopMatrix();

		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
	}
}