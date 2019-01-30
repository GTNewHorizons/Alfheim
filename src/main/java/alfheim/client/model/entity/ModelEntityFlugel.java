package alfheim.client.model.entity;

import static org.lwjgl.opengl.GL11.*;

import alexsocol.asjlib.ASJUtilities;
import alfheim.api.ModInfo;
import alfheim.api.lib.LibResourceLocations;
import alfheim.client.render.entity.RenderEntityFlugel;
import alfheim.common.entity.boss.EntityFlugel;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;

public class ModelEntityFlugel extends ModelBipedNew {
	
	public static IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(ModInfo.MODID, "model/Miku1.obj"));
	
	public ModelEntityFlugel() {
		super();
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		if (entity.getDataWatcher().getWatchableObjectString(10).equals("Hatsune Miku")) {
			FontRenderer font = Minecraft.getMinecraft().fontRenderer;
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glPushMatrix();
			glRotated(180, 1, 0, 0);
			glTranslated(0, -1.5, 0);
			Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.miku1);
			model.renderAll();
			glPopMatrix();
			
			Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(ModInfo.MODID, "textures/model/entity/Miku0.png"));
			super.render(entity, f, f1, f2, f3, f4, f5);
			glDisable(GL_BLEND);
			
			glPushMatrix();
			glTranslated(leftarm.rotationPointX * f5, leftarm.rotationPointY * f5, leftarm.rotationPointZ * f5);
			glRotated(leftarm.rotateAngleZ * (180F / (float)Math.PI), 0, 0, 1);
			glRotated(leftarm.rotateAngleY * (180F / (float)Math.PI), 0, 1, 0);
			glRotated(leftarm.rotateAngleX * (180F / (float)Math.PI), 1, 0, 0);
			double s = 0.01;
			glScaled(s, s, s);
			
			glPushMatrix();
			glTranslated(19, -5, (font.getStringWidth("01") / -2));
			glRotated(-90, 0, 1, 0);
			font.drawString("01", 0, 0, 0xFF0000);
			glPopMatrix();
			
			glPushMatrix();
			glTranslated(19, 2.5, (font.getStringWidth("Hatsune Miku.") / -2) / 5);
			glRotated(-90, 0, 1, 0);
			
			s = 0.175;
			glScaled(s, s, s);
			font.drawString("Hatsune Miku.", 0, 0, 0xFF0000);
			glPopMatrix();
			
			glColor4d(1, 1, 1, 1);
			glPopMatrix();
			
			return;
		}
		
		super.render(entity, f, f1, f2, f3, f4, f5); // ItemFlightTiara
		renderWings(entity, Minecraft.getMinecraft().timer.renderPartialTicks);
		renderHalo(entity, Minecraft.getMinecraft().timer.renderPartialTicks);
	}

	public void setRotationAngles(float limbSwing, float limbIpld, float ticksExisted, float yawHead, float pitchHead, float idk, Entity entity) {
		super.setRotationAngles(limbSwing, limbIpld, ticksExisted, yawHead, pitchHead, idk, entity);
		EntityFlugel flugel = (EntityFlugel) entity;

		if (flugel.isCasting()) {
			float f6 = 0.0F;
			float f7 = 0.0F;
			rightarm.rotateAngleZ = 0.0F;
			leftarm.rotateAngleZ = 0.0F;
			rightarm.rotateAngleY = -(0.1F - f6 * 0.6F) + head.rotateAngleY;
			leftarm.rotateAngleY = 0.1F - f6 * 0.6F + head.rotateAngleY + 0.4F;
			rightarm.rotateAngleX = -((float)Math.PI / 2F) + head.rotateAngleX;
			leftarm.rotateAngleX = -((float)Math.PI / 2F) + head.rotateAngleX;
			rightarm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
			leftarm.rotateAngleX -= f6 * 1.2F - f7 * 0.4F;
			rightarm.rotateAngleZ += MathHelper.cos(ticksExisted * 0.09F) * 0.05F + 0.05F;
			leftarm.rotateAngleZ -= MathHelper.cos(ticksExisted * 0.09F) * 0.05F + 0.05F;
			rightarm.rotateAngleX += MathHelper.sin(ticksExisted * 0.067F) * 0.05F;
			leftarm.rotateAngleX -= MathHelper.sin(ticksExisted * 0.067F) * 0.05F;
		}
	}
	
	public void renderWings(Entity entity, float partialTicks) {
		IIcon icon = ItemFlightTiara.wingIcons[0];
		Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);

		boolean flying = !entity.onGround;
		
		float rz = 120F;
		float rx = 20F + (float) ((Math.sin((double) (entity.ticksExisted + partialTicks) * (flying ? 0.4F : 0.2F)) + 0.5F) * (flying ? 30F : 5F));
		float ry = 0F;
		float h = 0.4F;
		float i = 0.15F;
		float s = 1F;

		glPushMatrix();
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glColor4d(1, 1, 1, 1);

		int light = 15728880;
		int lightmapX = light % 65536;
		int lightmapY = light / 65536;
		
		float f = icon.getMinU();
		float f1 = icon.getMaxU();
		float f2 = icon.getMinV();
		float f3 = icon.getMaxV();
		float sr = 1F / s;

		if (entity.isSneaking()) glRotatef(28.64789F, 1.0F, 0.0F, 0.0F);

		glTranslatef(0F, h, i);

		glRotatef(rz, 0F, 0F, 1F);
		glRotatef(rx, 1F, 0F, 0F);
		glRotatef(ry, 0F, 1F, 0F);
		glScalef(s, s, s);
		ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 32F);
		glScalef(sr, sr, sr);
		glRotatef(-ry, 0F, 1F, 0F);
		glRotatef(-rx, 1F, 0F, 0F);
		glRotatef(-rz, 0F, 0F, 1F);
		
		glScalef(-1F, 1F, 1F);
		glRotatef(rz, 0F, 0F, 1F);
		glRotatef(rx, 1F, 0F, 0F);
		glRotatef(ry, 0F, 1F, 0F);
		glScalef(s, s, s);
		ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 32F);
		glScalef(sr, sr, sr);
		glRotatef(-ry, 1F, 0F, 0F);
		glRotatef(-rx, 1F, 0F, 0F);
		glRotatef(-rz, 0F, 0F, 1F);
		
		glColor3f(1F, 1F, 1F);
		glPopMatrix();
	}
	
	@SideOnly(Side.CLIENT)
	public void renderHalo(Entity entity, float partialTicks) {
		EntityFlugel e = (EntityFlugel) entity;
		
		glPushMatrix();
		
		glTranslated(0, -(0.12 + (e.isSneaking() ? 0.0625 : 0)), 0);
		glRotated(ASJUtilities.interpolate(e.prevRenderYawOffset, e.renderYawOffset), 0, -1, 0);
		glRotated(ASJUtilities.interpolate(e.prevRotationYawHead, e.rotationYawHead) - 270, 0, 1, 0);
		glRotated(ASJUtilities.interpolate(e.prevRotationPitch, e.rotationPitch), 0, 0, 1);
		glRotated(30, 1, 0, -1);
		glTranslated(0.1F, -0.5F, -0.1F);
		glRotated(e.ticksExisted + partialTicks, 0, 1, 0);

		RenderEntityFlugel.so.addTranslation();

		glPopMatrix();
	}
}