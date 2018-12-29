package alfheim.client.render.tile;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.render.RenderPostShaders;
import alexsocol.asjlib.render.ShadedObject;
import alfheim.api.ModInfo;
import alfheim.api.lib.LibResourceLocations;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
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

public class RenderTileAlfheimPylons extends TileEntitySpecialRenderer {

	
	

	public static IPylonModel model;
	public static boolean orange = false, hand = false;
	public Random rand = new Random();
	
	public ShadedObjectPylon shObjPO = new ShadedObjectPylon(LibResourceLocations.elvenPylonOld);
	public ShadedObjectPylon shObjP = new ShadedObjectPylon(LibResourceLocations.elvenPylon);
	public ShadedObjectPylon shObjOO = new ShadedObjectPylon(LibResourceLocations.yordinPylonOld);
	public ShadedObjectPylon shObjO = new ShadedObjectPylon(LibResourceLocations.yordinPylon);

	public RenderTileAlfheimPylons() {
		RenderPostShaders.registerShadedObject(shObjO);
		RenderPostShaders.registerShadedObject(shObjP);
		RenderPostShaders.registerShadedObject(shObjOO);
		RenderPostShaders.registerShadedObject(shObjPO);
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float ticks) {
		if (model == null)
			model = ConfigHandler.oldPylonModel ? new ModelPylonOld() : new ModelPylon();

		GL11.glPushMatrix();
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		float a = MultiblockRenderHandler.rendering ? 0.6F : 1F;
		GL11.glColor4f(1F, 1F, 1F, a);
		if (tile.getWorldObj() != null) {
			orange = tile.getBlockMetadata() == 1;
		}

		if (ConfigHandler.oldPylonModel)
			Minecraft.getMinecraft().renderEngine.bindTexture(orange ? LibResourceLocations.yordinPylonOld : LibResourceLocations.elvenPylonOld);
		else
			Minecraft.getMinecraft().renderEngine.bindTexture(orange ? LibResourceLocations.yordinPylon : LibResourceLocations.elvenPylon);

		double worldTime = tile.getWorldObj() == null ? 0 : (double) (ClientTickHandler.ticksInGame + ticks);

		rand.setSeed(tile.xCoord ^ tile.yCoord ^ tile.zCoord);
		if (tile != null)
			worldTime += rand.nextInt(360);

		if (ConfigHandler.oldPylonModel) {
			GL11.glTranslated(x + 0.5, y + 2.2, z + 0.5);
			GL11.glScalef(1F, -1.5F, -1F);
		} else {
			GL11.glTranslated(x + 0.2 + (orange ? -0.1 : 0), y + 0.05, z + 0.8 + (orange ? 0.1 : 0));
			float scale = orange ? 0.8F : 0.6F;
			GL11.glScalef(scale, 0.6F, scale);
		}

		if (!orange) {
			GL11.glPushMatrix();
			if (!ConfigHandler.oldPylonModel)
				GL11.glTranslatef(0.5F, 0F, -0.5F);
			GL11.glRotatef((float) worldTime * 1.5F, 0F, 1F, 0F);
			if (!ConfigHandler.oldPylonModel)
				GL11.glTranslatef(-0.5F, 0F, 0.5F);

			model.renderRing();
			GL11.glTranslated(0, Math.sin(worldTime / 20.0) / 20 - 0.025, 0);
			model.renderGems();
			GL11.glPopMatrix();
		}

		GL11.glPushMatrix();
		GL11.glTranslated(0, Math.sin(worldTime / 20.0) / 17.5, 0);

		if (!ConfigHandler.oldPylonModel)
			GL11.glTranslatef(0.5F, 0F, -0.5F);

		GL11.glRotatef((float) -worldTime, 0F, 1F, 0F);
		if (!ConfigHandler.oldPylonModel)
			GL11.glTranslatef(-0.5F, 0F, 0.5F);

		GL11.glDisable(GL11.GL_CULL_FACE);
		model.renderCrystal();

		GL11.glColor4f(1F, 1F, 1F, a);
		
		if (!ShaderHelper.useShaders() || hand) {
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
			float alpha = (float) ((Math.sin((ClientTickHandler.ticksInGame + ticks) / 20.0) / 2.0 + 0.5) / (ConfigHandler.oldPylonModel ? 1.0 : 2.0));
			GL11.glColor4f(1F, 1F, 1F, a * (alpha + 0.183F));
		}
		
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		GL11.glScalef(1.1F, 1.1F, 1.1F);
		if (!ConfigHandler.oldPylonModel)
			GL11.glTranslatef(-0.05F, -0.1F, 0.05F);
		else
			GL11.glTranslatef(0F, -0.09F, 0F);

		if (!hand) {
			ShadedObjectPylon shObj = ConfigHandler.oldPylonModel ? orange ? shObjOO : shObjPO : orange ? shObjO : shObjP;
			shObj.addTranslation();
		} else {
			model.renderCrystal();
		}
		
		GL11.glEnable(GL11.GL_ALPHA_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		GL11.glPopMatrix();
		
		hand = false;
	}
	
	public static class ShadedObjectPylon extends ShadedObject {

		public static final int matID = RenderPostShaders.getNextAvailableRenderObjectMaterialID();
		
		public ShadedObjectPylon(ResourceLocation rl) {
			super(ShaderHelper.pylonGlow, matID, rl);
		}

		@Override
		public final void preRender() {
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			float a = MultiblockRenderHandler.rendering ? 0.6F : 1F;
			GL11.glColor4f(1F, 1F, 1F, a);
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
		}

		@Override
		public void drawMesh() {
			model.renderCrystal();
		}

		@Override
		public final void postRender() {
			GL11.glEnable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		}
	}
}