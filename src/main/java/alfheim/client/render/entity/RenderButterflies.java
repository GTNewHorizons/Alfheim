package alfheim.client.render.entity;

import static org.lwjgl.opengl.GL11.*;

import java.util.Random;

import alfheim.api.lib.LibResourceLocations;
import alfheim.common.core.registry.AlfheimRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class RenderButterflies {
	
	static Random rand = new Random();
	
	@SideOnly(Side.CLIENT)
	public static void render(Render render, Entity entity, double x, double y, double z, float partialTicks) {
		int flies = ((EntityLivingBase) entity).getActivePotionEffect(AlfheimRegistry.butterShield).getAmplifier() * 32;
		
		glPushMatrix();
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDisable(GL_CULL_FACE);
		glDisable(GL_LIGHTING);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
		glColor4d(1, 1, 1, 1);
		
		glTranslated(x, y, z);
		double s = Math.max(entity.width, entity.height) / 1.25;
		glScaled(s, s, s);
		
		rand.setSeed(entity.getEntityId());
		Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.butterfly);

		for (; flies > 0; --flies) {
			glPushMatrix();
			glColor4d(rand.nextDouble() * 0.4 + 0.6, rand.nextDouble() * 0.4 + 0.6, rand.nextDouble() * 0.4 + 0.6, 1);
			glTranslated(0, entity == Minecraft.getMinecraft().thePlayer ? -(Minecraft.getMinecraft().thePlayer.getDefaultEyeHeight() * s) : (entity.height / s) / 2, 0);
			glRotated(rand.nextDouble() * 360 + entity.ticksExisted, rand.nextDouble() * 2 - 1, rand.nextDouble() * 2 - 1, rand.nextDouble() * 2 - 1);
			glTranslated(0, 0, -1);
			drawRect(Tessellator.instance);
			glPopMatrix();
		}

		glEnable(GL_LIGHTING);
		glEnable(GL_CULL_FACE);
		glDisable(GL_BLEND);
		glPopMatrix();
	}
	
	private static void drawRect(Tessellator tessellator) {
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(-0.2, -0.2, 0, 0, 1);
		tessellator.addVertexWithUV(-0.2, 0.2, 0, 0, 0);
		tessellator.addVertexWithUV(0.2, 0.2, 0, 1, 0);
		tessellator.addVertexWithUV(0.2, -0.2, 0, 1, 1);
		tessellator.draw();
	}
}