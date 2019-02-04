package alfheim.client.render.entity;

import static org.lwjgl.opengl.GL11.*;

import alfheim.api.ModInfo;
import alfheim.api.lib.LibResourceLocations;
import alfheim.common.core.util.AlfheimConfig;
import alfheim.common.item.material.ItemElvenResource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class RenderEntityWindBlade extends Render {

	public static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(ModInfo.MODID, "model/tor.obj"));
	
	public RenderEntityWindBlade() {
		super();
		shadowSize = 0.0F;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return LibResourceLocations.wind;
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTick) {
		glPushMatrix();
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glAlphaFunc(GL_GREATER, 0.003921569F);
		glTranslated(x, y + 0.05, z);
		glRotated(((Minecraft.getMinecraft().theWorld.getTotalWorldTime() + entity.ticksExisted) + Minecraft.getMinecraft().timer.renderPartialTicks) * 5, 0, 1, 0);
		if (AlfheimConfig.minimalGraphics) {
			glScaled(3, 0.5, 3);
			glRotated(90, 1, 0, 0);
			glTranslated(-0.5, -0.5, 0.03125);
			Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
			ItemRenderer.renderItemIn2D(Tessellator.instance, ItemElvenResource.wind.getMaxU(), ItemElvenResource.wind.getMinV(), ItemElvenResource.wind.getMinU(), ItemElvenResource.wind.getMaxV(), ItemElvenResource.wind.getIconWidth(), ItemElvenResource.wind.getIconHeight(), 1F / 16F);
		} else {
			glScaled(1, 0.1, 1);
			Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.wind);
			model.renderAll();
		}
		glDisable(GL_BLEND);
		glPopMatrix();
	}
}