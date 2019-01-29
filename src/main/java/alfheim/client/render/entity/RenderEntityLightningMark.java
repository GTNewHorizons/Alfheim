package alfheim.client.render.entity;

import static org.lwjgl.opengl.GL11.*;

import java.util.Random;

import alexsocol.asjlib.render.RenderPostShaders;
import alexsocol.asjlib.render.ShadedObject;
import alfheim.api.lib.LibResourceLocations;
import alfheim.common.entity.EntityLightningMark;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.client.core.helper.ShaderHelper;

public class RenderEntityLightningMark extends Render {

	public static ShadedObject so = new ShadedObject(ShaderHelper.halo, RenderPostShaders.getNextAvailableRenderObjectMaterialID(), LibResourceLocations.mark) {

		@Override
		public void preRender() {
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

			glDisable(GL_CULL_FACE);
			glShadeModel(GL_SMOOTH);
			
			// glColor4d(1, 1, 1, chargeMul); 
		}

		@Override
		public void drawMesh() {
			Tessellator tes = Tessellator.instance;
			tes.startDrawingQuads();
			tes.addVertexWithUV(-1, 0, -1, 0, 0);
			tes.addVertexWithUV(-1, 0, 1, 0, 1);
			tes.addVertexWithUV(1, 0, 1, 1, 1);
			tes.addVertexWithUV(1, 0, -1, 1, 0);
			tes.draw();
		}
		
		@Override
		public void postRender() {
			glEnable(GL_TEXTURE_2D);
			glShadeModel(GL_FLAT);
			glDisable(GL_BLEND);
		}
		
	};
	
	public RenderEntityLightningMark() {
		super();
		shadowSize = 0.0F;
		RenderPostShaders.registerShadedObject(so);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return null;
	}

	Random rand = new Random();
	
	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTick) {
		EntityLightningMark mark = (EntityLightningMark) entity;
		glPushMatrix();
		glTranslated(x, y + 0.01, z);

		float live = mark.getTimer() / 2F;
		float charge = Math.min(10F, live + partialTick);
		float chargeMul = charge / 10F;

		
		rand.setSeed(mark.getUniqueID().getMostSignificantBits());

		float s = chargeMul;
		s += Math.min(1F, (live + partialTick) * 0.2F);
		s /= 2F;
		glScalef(s, s, s);

		glRotatef(charge * 9F + (mark.ticksExisted + partialTick) * 0.5F + rand.nextFloat() * 360F, 0F, 1F, 0F);

		so.addTranslation();
		
		glPopMatrix();
	}
}