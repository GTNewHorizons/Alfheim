package alfheim.client.render.entity;

import static org.lwjgl.opengl.GL11.*;

import alfheim.api.lib.LibResourceLocations;
import alfheim.common.entity.EntityAlfheimPixie;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.client.model.ModelPixie;

public class RenderEntityAlfheimPixie extends RenderLiving {

	public RenderEntityAlfheimPixie() {
		super(new ModelPixie(), 0.25F);
		setRenderPassModel(new ModelPixie());
		shadowSize = 0;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return LibResourceLocations.pixie;
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float pitch) {
		if (entity.isInvisible()) return;
		glPushMatrix();
		glTranslated(0, -0.25, 0);
		super.doRender(entity, x, y, z, yaw, pitch);
		glPopMatrix();
	}

	protected int setPixieBrightness(EntityAlfheimPixie pixie, int par2, float par3) {
		if (par2 != 0)
			return -1;
		else {
			bindTexture(getEntityTexture(pixie));
			glEnable(GL_BLEND);
			glDisable(GL_ALPHA_TEST);
			glBlendFunc(GL_ONE, GL_ONE);

			if (pixie.isInvisible())
				glDepthMask(false);
			else
				glDepthMask(true);

			char c0 = 61680;
			float j = c0 % 65536;
			float k = c0 / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j, k);
			glColor4d(1, 1, 1, 1);
			glColor4d(1, 1, 1, 1);
			return 1;
		}
	}

	@Override
	protected int shouldRenderPass(EntityLivingBase entity, int par2, float par3) {
		return setPixieBrightness((EntityAlfheimPixie) entity, par2, par3);
	}
}
