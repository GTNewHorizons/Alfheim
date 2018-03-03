package alfheim.client.render.block;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import alexsocol.asjlib.ItemContainingTileEntity;
import alfheim.common.block.tile.TileTransferer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelSpreader;

public class RenderTileTransferer extends TileEntitySpecialRenderer {

	private static final ResourceLocation texture = new ResourceLocation(LibResources.MODEL_SPREADER);
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

		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
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