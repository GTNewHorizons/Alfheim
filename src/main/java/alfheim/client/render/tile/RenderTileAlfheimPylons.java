package alfheim.client.render.tile;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

import java.util.Random;

import alexsocol.asjlib.render.RenderPostShaders;
import alexsocol.asjlib.render.ShadedObject;
import alfheim.api.lib.LibResourceLocations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.client.core.handler.ClientTickHandler;
import vazkii.botania.client.core.handler.MultiblockRenderHandler;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.model.IPylonModel;
import vazkii.botania.client.model.ModelPylon;
import vazkii.botania.client.model.ModelPylonOld;
import vazkii.botania.common.core.handler.ConfigHandler;

public class RenderTileAlfheimPylons extends TileEntitySpecialRenderer {

	public static IPylonModel model;
	public static boolean orange = false, red = false, hand = false;
	public Random rand = new Random();

	public ShadedObjectPylon shObjRO = new ShadedObjectPylon(LibResourceLocations.antiPylonOld);
	public ShadedObjectPylon shObjR = new ShadedObjectPylon(LibResourceLocations.antiPylon);
	public ShadedObjectPylon shObjPO = new ShadedObjectPylon(LibResourceLocations.elvenPylonOld);
	public ShadedObjectPylon shObjP = new ShadedObjectPylon(LibResourceLocations.elvenPylon);
	public ShadedObjectPylon shObjOO = new ShadedObjectPylon(LibResourceLocations.yordinPylonOld);
	public ShadedObjectPylon shObjO = new ShadedObjectPylon(LibResourceLocations.yordinPylon);

	public RenderTileAlfheimPylons() {
		RenderPostShaders.registerShadedObject(shObjO);
		RenderPostShaders.registerShadedObject(shObjP);
		RenderPostShaders.registerShadedObject(shObjR);
		RenderPostShaders.registerShadedObject(shObjOO);
		RenderPostShaders.registerShadedObject(shObjPO);
		RenderPostShaders.registerShadedObject(shObjRO);
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float ticks) {
		if (model == null)
			model = ConfigHandler.oldPylonModel ? new ModelPylonOld() : new ModelPylon();

		glPushMatrix();
		glEnable(GL_RESCALE_NORMAL);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		float a = MultiblockRenderHandler.rendering ? 0.6F : 1F;
		glColor4f(1F, 1F, 1F, a);
		if (tile.getWorldObj() != null) {
			orange = tile.getBlockMetadata() == 1;
			red = tile.getBlockMetadata() == 2;
		}

		if (ConfigHandler.oldPylonModel)
			Minecraft.getMinecraft().renderEngine.bindTexture(red ? LibResourceLocations.antiPylonOld : orange ? LibResourceLocations.yordinPylonOld : LibResourceLocations.elvenPylonOld);
		else
			Minecraft.getMinecraft().renderEngine.bindTexture(red ? LibResourceLocations.antiPylon : orange ? LibResourceLocations.yordinPylon : LibResourceLocations.elvenPylon);

		double worldTime = tile.getWorldObj() == null ? 0 : (double) (ClientTickHandler.ticksInGame + ticks);

		rand.setSeed(tile.xCoord ^ tile.yCoord ^ tile.zCoord);
		if (tile != null)
			worldTime += rand.nextInt(360);

		if (ConfigHandler.oldPylonModel) {
			glTranslated(x + 0.5, y + 2.2, z + 0.5);
			glScalef(1F, -1.5F, -1F);
		} else {
			glTranslated(x + 0.2 + (orange ? -0.1 : 0), y + 0.05, z + 0.8 + (orange ? 0.1 : 0));
			float scale = orange ? 0.8F : 0.6F;
			glScalef(scale, 0.6F, scale);
		}

		if (!orange) {
			glPushMatrix();
			if (!ConfigHandler.oldPylonModel)
				glTranslatef(0.5F, 0F, -0.5F);
			glRotatef((float) worldTime * 1.5F, 0F, 1F, 0F);
			if (!ConfigHandler.oldPylonModel)
				glTranslatef(-0.5F, 0F, 0.5F);

			model.renderRing();
			glTranslated(0, Math.sin(worldTime / 20.0) / 20 - 0.025, 0);
			model.renderGems();
			glPopMatrix();
		}

		glPushMatrix();
		glTranslated(0, Math.sin(worldTime / 20.0) / 17.5, 0);

		if (!ConfigHandler.oldPylonModel)
			glTranslatef(0.5F, 0F, -0.5F);

		glRotatef((float) -worldTime, 0F, 1F, 0F);
		if (!ConfigHandler.oldPylonModel)
			glTranslatef(-0.5F, 0F, 0.5F);

		glDisable(GL_CULL_FACE);
		model.renderCrystal();

		glColor4f(1F, 1F, 1F, a);
		
		if (!ShaderHelper.useShaders() || hand) {
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
			float alpha = (float) ((Math.sin((ClientTickHandler.ticksInGame + ticks) / 20.0) / 2.0 + 0.5) / (ConfigHandler.oldPylonModel ? 1.0 : 2.0));
			glColor4f(1F, 1F, 1F, a * (alpha + 0.183F));
		}
		
		glDisable(GL_ALPHA_TEST);
		glScalef(1.1F, 1.1F, 1.1F);
		if (!ConfigHandler.oldPylonModel)
			glTranslatef(-0.05F, -0.1F, 0.05F);
		else
			glTranslatef(0F, -0.09F, 0F);

		if (!hand) {
			ShadedObjectPylon shObj = ConfigHandler.oldPylonModel ? (red ? shObjRO : orange ? shObjOO : shObjPO) : (red ? shObjR : orange ? shObjO : shObjP);
			shObj.addTranslation();
		} else {
			model.renderCrystal();
		}
		
		glEnable(GL_ALPHA_TEST);
		glEnable(GL_CULL_FACE);
		glPopMatrix();

		glDisable(GL_BLEND);
		glEnable(GL_RESCALE_NORMAL);
		glPopMatrix();
		
		hand = false;
	}
	
	public static class ShadedObjectPylon extends ShadedObject {

		public static final int matID = RenderPostShaders.getNextAvailableRenderObjectMaterialID();
		
		public ShadedObjectPylon(ResourceLocation rl) {
			super(ShaderHelper.pylonGlow, matID, rl);
		}

		@Override
		public final void preRender() {
			glEnable(GL_RESCALE_NORMAL);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			float a = MultiblockRenderHandler.rendering ? 0.6F : 1F;
			glColor4f(1F, 1F, 1F, a);
			glDisable(GL_CULL_FACE);
			glDisable(GL_ALPHA_TEST);
		}

		@Override
		public void drawMesh() {
			model.renderCrystal();
		}

		@Override
		public final void postRender() {
			glEnable(GL_ALPHA_TEST);
			glEnable(GL_CULL_FACE);
			glDisable(GL_BLEND);
			glEnable(GL_RESCALE_NORMAL);
		}
	}
}