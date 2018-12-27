package alfheim.common.potion;

import org.lwjgl.opengl.GL11;

import alfheim.common.block.BlockRedFlame;
import alfheim.common.core.registry.AlfheimBlocks;
import alfheim.common.core.util.AlfheimConfig;
import alfheim.common.core.util.DamageSourceSpell;
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
import net.minecraft.util.IIcon;

public class PotionSoulburn extends PotionAlfheim {

	public PotionSoulburn() {
		super(AlfheimConfig.potionIDSoulburn, "soulburn", true, 0xCC4400);
	}

	@Override
	public boolean isReady(int time, int mod) {
		return time % 20 == 0; 
	}

	@Override
	public void performEffect(EntityLivingBase living, int mod) {
		living.attackEntityFrom(DamageSourceSpell.soulburn, mod+1);
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
