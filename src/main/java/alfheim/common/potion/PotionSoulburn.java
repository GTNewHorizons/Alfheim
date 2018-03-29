package alfheim.common.potion;

import org.lwjgl.opengl.GL11;

import alfheim.common.block.BlockRedFlame;
import alfheim.common.core.registry.AlfheimBlocks;
import alfheim.common.core.utils.AlfheimConfig;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;

public class PotionSoulburn extends PotionAlfheim {

	public static final DamageSource solburn = new DamageSource("soulburn").setDamageBypassesArmor().setDamageIsAbsolute();
	
	public PotionSoulburn() {
		super(AlfheimConfig.potionIDSoulburn, "soulburn", true, 0xCC4400, 1);
	}

	@Override
	public boolean isReady(int time, int mod) {
		return time > 1200 && time % 20 == 0; 
	}

	@Override
	public void performEffect(EntityLivingBase living, int mod) {
		 living.attackEntityFrom(solburn, mod);
	}
	
	@SideOnly(Side.CLIENT)
	public static void renderEntityOnFire(Render render, Entity entity, double x, double y, double z, float partialTicks) {
		int i1 = 15728880;
		int j = i1 % 65536;
		int k = i1 / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) j / 1.0F, (float) k / 1.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		
		GL11.glDisable(GL11.GL_LIGHTING);
		IIcon iicon = ((BlockRedFlame) AlfheimBlocks.redFlame).icons[0];
		IIcon iicon1 = ((BlockRedFlame) AlfheimBlocks.redFlame).icons[1];
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x, (float) y, (float) z);
		float f1 = entity.width * 1.4F;
		GL11.glScalef(f1, f1, f1);
		Tessellator tessellator = Tessellator.instance;
		float f2 = 0.5F;
		float f3 = 0.0F;
		float f4 = entity.height / f1;
		float f5 = (float) (entity.posY - entity.boundingBox.minY);
		GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(0.0F, 0.0F, -0.3F + (float) ((int) f4) * 0.02F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		float f6 = 0.0F;
		int i = 0;
		tessellator.startDrawingQuads();

		while (f4 > 0.0F) {
			IIcon iicon2 = i % 2 == 0 ? iicon : iicon1;
			RenderManager.instance.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
			float f7 = iicon2.getMinU();
			float f8 = iicon2.getMinV();
			float f9 = iicon2.getMaxU();
			float f10 = iicon2.getMaxV();

			if (i / 2 % 2 == 0) {
				float f11 = f9;
				f9 = f7;
				f7 = f11;
			}

			tessellator.addVertexWithUV((double) (f2 - f3), (double) (0.0F - f5), (double) f6, (double) f9, (double) f10);
			tessellator.addVertexWithUV((double) (-f2 - f3), (double) (0.0F - f5), (double) f6, (double) f7, (double) f10);
			tessellator.addVertexWithUV((double) (-f2 - f3), (double) (1.4F - f5), (double) f6, (double) f7, (double) f8);
			tessellator.addVertexWithUV((double) (f2 - f3), (double) (1.4F - f5), (double) f6, (double) f9, (double) f8);
			f4 -= 0.45F;
			f5 -= 0.45F;
			f2 *= 0.9F;
			f6 += 0.03F;
			++i;
		}

		tessellator.draw();
		GL11.glPopMatrix();
		GL11.glEnable(GL11.GL_LIGHTING);
	}

	@SideOnly(Side.CLIENT)
	public static void renderFireInFirstPerson(float partialTicks) {
        Tessellator tessellator = Tessellator.instance;
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.9F);
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        float f1 = 1.0F;

        for (int i = 0; i < 2; ++i) {
            GL11.glPushMatrix();
            IIcon iicon = ((BlockRedFlame) AlfheimBlocks.redFlame).icons[1];
            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
            float f2 = iicon.getMinU();
            float f3 = iicon.getMaxU();
            float f4 = iicon.getMinV();
            float f5 = iicon.getMaxV();
            float f6 = (0.0F - f1) / 2.0F;
            float f7 = f6 + f1;
            float f8 = 0.0F - f1 / 2.0F;
            float f9 = f8 + f1;
            float f10 = -0.5F;
            GL11.glTranslatef((float)(-(i * 2 - 1)) * 0.24F, -0.3F, 0.0F);
            GL11.glRotatef((float)(i * 2 - 1) * 10.0F, 0.0F, 1.0F, 0.0F);
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV((double)f6, (double)f8, (double)f10, (double)f3, (double)f5);
            tessellator.addVertexWithUV((double)f7, (double)f8, (double)f10, (double)f2, (double)f5);
            tessellator.addVertexWithUV((double)f7, (double)f9, (double)f10, (double)f2, (double)f4);
            tessellator.addVertexWithUV((double)f6, (double)f9, (double)f10, (double)f3, (double)f4);
            tessellator.draw();
            GL11.glPopMatrix();
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
    }
}
