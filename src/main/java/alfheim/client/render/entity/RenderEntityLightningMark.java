package alfheim.client.render.entity;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import alfheim.api.ModInfo;
import alfheim.common.entity.EntityAlfheimPixie;
import alfheim.common.entity.EntityLightningMark;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.client.core.helper.ShaderHelper;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelPixie;
import vazkii.botania.client.render.entity.RenderBabylonWeapon;
import vazkii.botania.common.entity.EntityBabylonWeapon;
import vazkii.botania.common.item.relic.ItemKingKey;

public class RenderEntityLightningMark extends Render {

	private static final ResourceLocation babylon = new ResourceLocation(LibResources.MISC_BABYLON);

	public RenderEntityLightningMark() {
		super();
		shadowSize = 0.0F;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return null;
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTick) {
		EntityLightningMark mark = (EntityLightningMark) entity;
		GL11.glPushMatrix();
		GL11.glTranslated(x, y + 0.01, z);

		float live = mark.getTimer() / 2F;
		float charge = Math.min(10F, live + partialTick);
		float chargeMul = charge / 10F;

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glColor4f(1F, 1F, 1F, chargeMul);

		Minecraft.getMinecraft().renderEngine.bindTexture(babylon);

		Tessellator tes = Tessellator.instance;
		ShaderHelper.useShader(ShaderHelper.halo);
		Random rand = new Random(mark.getUniqueID().getMostSignificantBits());

		float s = chargeMul;
		s += Math.min(1F, (live + partialTick) * 0.2F);
		s /= 2F;
		GL11.glScalef(s, s, s);

		GL11.glRotatef(charge * 9F + (mark.ticksExisted + partialTick) * 0.5F + rand.nextFloat() * 360F, 0F, 1F, 0F);

		tes.startDrawingQuads();
		tes.addVertexWithUV(-1, 0, -1, 0, 0);
		tes.addVertexWithUV(-1, 0, 1, 0, 1);
		tes.addVertexWithUV(1, 0, 1, 1, 1);
		tes.addVertexWithUV(1, 0, -1, 1, 0);
		tes.draw();

		ShaderHelper.releaseShader();

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glShadeModel(GL11.GL_FLAT);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}
}
