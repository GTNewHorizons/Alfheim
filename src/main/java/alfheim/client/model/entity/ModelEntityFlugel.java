package alfheim.client.model.entity;

import org.lwjgl.opengl.GL11;

import alexsocol.asjlib.ASJUtilities;
import alfheim.common.entity.boss.EntityFlugel;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.item.IBaubleRender.Helper;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;

public class ModelEntityFlugel extends ModelBipedNew {
	
	private static ResourceLocation textureHalo = new ResourceLocation(LibResources.MISC_HALO);
	
	public ModelEntityFlugel() {
		super();
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5); // ItemFlightTiara
		renderWings(entity, f5);
		renderHalo(entity, f5);
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

		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(1F, 1F, 1F, 1F);

		int light = 15728880;
		int lightmapX = light % 65536;
		int lightmapY = light / 65536;
		
		float f = icon.getMinU();
		float f1 = icon.getMaxU();
		float f2 = icon.getMinV();
		float f3 = icon.getMaxV();
		float sr = 1F / s;

		if (entity.isSneaking()) GL11.glRotatef(28.64789F, 1.0F, 0.0F, 0.0F);

		GL11.glTranslatef(0F, h, i);

		GL11.glRotatef(rz, 0F, 0F, 1F);
		GL11.glRotatef(rx, 1F, 0F, 0F);
		GL11.glRotatef(ry, 0F, 1F, 0F);
		GL11.glScalef(s, s, s);
		ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 32F);
		GL11.glScalef(sr, sr, sr);
		GL11.glRotatef(-ry, 0F, 1F, 0F);
		GL11.glRotatef(-rx, 1F, 0F, 0F);
		GL11.glRotatef(-rz, 0F, 0F, 1F);
		
		GL11.glScalef(-1F, 1F, 1F);
		GL11.glRotatef(rz, 0F, 0F, 1F);
		GL11.glRotatef(rx, 1F, 0F, 0F);
		GL11.glRotatef(ry, 0F, 1F, 0F);
		GL11.glScalef(s, s, s);
		ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), 1F / 32F);
		GL11.glScalef(sr, sr, sr);
		GL11.glRotatef(-ry, 1F, 0F, 0F);
		GL11.glRotatef(-rx, 1F, 0F, 0F);
		GL11.glRotatef(-rz, 0F, 0F, 1F);
		
		GL11.glColor3f(1F, 1F, 1F);
		GL11.glPopMatrix();
	}
	
	@SideOnly(Side.CLIENT)
	public static void renderHalo(Entity entity, float partialTicks) {
		EntityFlugel flugel = (EntityFlugel) entity;
		GL11.glPushMatrix();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glColor4f(1F, 1F, 1F, 1F);

		Minecraft.getMinecraft().renderEngine.bindTexture(textureHalo);

		GL11.glTranslated(0, -0.1 + (entity.isSneaking() ? 0.0625 : 0), 0);
		
		GL11.glRotated(ASJUtilities.interpolate(flugel.prevRenderYawOffset, flugel.renderYawOffset, partialTicks), 0, -1, 0);
		GL11.glRotated(ASJUtilities.interpolate(flugel.prevRotationYawHead, flugel.rotationYawHead, partialTicks) - 270, 0, 1, 0);
		GL11.glRotated(ASJUtilities.interpolate(flugel.prevRotationPitch, flugel.prevRotationPitch, partialTicks), 0, 0, 1);
		
		GL11.glRotated(30, 1, 0, -1);
		GL11.glTranslatef(-0.1F, -0.5F, -0.1F);
		GL11.glRotatef(entity.ticksExisted + partialTicks, 0, 1, 0);

		Tessellator tes = Tessellator.instance;
		ShaderHelper.useShader(ShaderHelper.halo);
		tes.startDrawingQuads();
		tes.addVertexWithUV(-0.75, 0, -0.75, 0, 0);
		tes.addVertexWithUV(-0.75, 0, 0.75, 0, 1);
		tes.addVertexWithUV(0.75, 0, 0.75, 1, 1);
		tes.addVertexWithUV(0.75, 0, -0.75, 1, 0);
		tes.draw();
		ShaderHelper.releaseShader();

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}
}