package alfheim.client.render.block;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import alfheim.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MultiblockRenderHandler;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.IPylonModel;
import vazkii.botania.client.model.ModelPylon;
import vazkii.botania.client.model.ModelPylonOld;
import vazkii.botania.common.core.handler.ConfigHandler;

public class RenderTileElvenPylon extends TileEntitySpecialRenderer {

	private static final ResourceLocation textureOld = new ResourceLocation(Constants.MODID, "textures/model/block/ElvenPylonOld.png");
	private static final ResourceLocation texture = new ResourceLocation(Constants.MODID, "textures/model/block/ElvenPylon.png");

	IPylonModel model;

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d0, double d1, double d2, float pticks) {
		if(model == null)
			model = ConfigHandler.oldPylonModel ? new ModelPylonOld() : new ModelPylon();

			GL11.glPushMatrix();
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			float a = MultiblockRenderHandler.rendering ? 0.6F : 1F;
			GL11.glColor4f(1F, 1F, 1F, a);

			if(ConfigHandler.oldPylonModel) Minecraft.getMinecraft().renderEngine.bindTexture(textureOld);
			else Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Constants.MODID, "textures/model/block/ElvenPylon.png"));

			double worldTime = tileentity.getWorldObj() == null ? 0 : (double) (ClientTickHandler.ticksInGame + pticks);

			if(tileentity != null) worldTime += new Random(tileentity.xCoord ^ tileentity.yCoord ^ tileentity.zCoord).nextInt(360);

			if(ConfigHandler.oldPylonModel) {
				GL11.glTranslated(d0 + 0.5, d1 + 2.2, d2 + 0.5);
				GL11.glScalef(1F, -1.5F, -1F);
			} else {
				GL11.glTranslated(d0 + 0.2 -0.1, d1 + 0.05, d2 + 0.8 + 0.1);
				float scale = 0.8F;
				GL11.glScalef(scale, 0.6F, scale);
			}

			GL11.glPushMatrix();
			GL11.glTranslated(0D, Math.sin(worldTime / 20D) / 17.5, 0D);

			if(!ConfigHandler.oldPylonModel) GL11.glTranslatef(0.5F, 0F, -0.5F);

			GL11.glRotatef((float) -worldTime, 0F, 1F, 0F);
			if(!ConfigHandler.oldPylonModel) GL11.glTranslatef(-0.5F, 0F, 0.5F);


			GL11.glDisable(GL11.GL_CULL_FACE);
			model.renderCrystal();

			GL11.glColor4f(1F, 1F, 1F, a);
			if(!ShaderHelper.useShaders()) {
				int light = 15728880;
				int lightmapX = light % 65536;
				int lightmapY = light / 65536;
				OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightmapX, lightmapY);
				float alpha = (float) ((Math.sin(worldTime / 20D) / 2D + 0.5) / (ConfigHandler.oldPylonModel ? 1D : 2D));
				GL11.glColor4f(1F, 1F, 1F, a * (alpha + 0.183F));
			}

			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glScalef(1.1F, 1.1F, 1.1F);
			if(!ConfigHandler.oldPylonModel) GL11.glTranslatef(-0.05F, -0.1F, 0.05F);
			else GL11.glTranslatef(0F, -0.09F, 0F);

			ShaderHelper.useShader(ShaderHelper.pylonGlow);
			model.renderCrystal();
			ShaderHelper.releaseShader();

			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glPopMatrix();

			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glPopMatrix();
	}
}
