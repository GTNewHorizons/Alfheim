package alfheim.client.entity.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import alfheim.common.entity.EntityAlfheimPixie;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.internal.ShaderCallback;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.client.model.ModelPixie;

public class RenderAlfheimPixie extends RenderLiving {

	ShaderCallback callback = new ShaderCallback() {

		@Override
		public void call(int shader) {
			// Frag Uniforms
			int disfigurationUniform = GL20.glGetUniformLocation(shader, "disfiguration");
			GL20.glUniform1f(disfigurationUniform, 0.025F);

			// Vert Uniforms
			int grainIntensityUniform = GL20.glGetUniformLocation(shader, "grainIntensity");
			GL20.glUniform1f(grainIntensityUniform, 0.05F);
		}
	};

	public RenderAlfheimPixie() {
		super(new ModelPixie(), 0.25F);
		setRenderPassModel(new ModelPixie());
		shadowSize = 0.0F;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return new ResourceLocation(LibResources.MODEL_PIXIE);
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float pitch) {
		if (entity.isInvisible()) return;
		GL11.glTranslated(0, -0.25, 0);
		super.doRender(entity, x, y, z, yaw, pitch);
	}

	protected int setPixieBrightness(EntityAlfheimPixie pixie, int par2, float par3) {
		if (par2 != 0)
			return -1;
		else {
			bindTexture(getEntityTexture(pixie));
			float f1 = 1.0F;
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);

			if (pixie.isInvisible())
				GL11.glDepthMask(false);
			else
				GL11.glDepthMask(true);

			char c0 = 61680;
			int j = c0 % 65536;
			int k = c0 / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, f1);
			return 1;
		}
	}

	@Override
	protected int shouldRenderPass(EntityLivingBase entity, int par2, float par3) {
		return setPixieBrightness((EntityAlfheimPixie) entity, par2, par3);
	}

}
