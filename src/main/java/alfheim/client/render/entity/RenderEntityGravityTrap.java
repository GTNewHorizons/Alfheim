package alfheim.client.render.entity;

import static org.lwjgl.opengl.GL11.*;

import java.util.Random;

import alexsocol.asjlib.render.RenderPostShaders;
import alexsocol.asjlib.render.ShadedObject;
import alfheim.api.ModInfo;
import alfheim.api.lib.LibResourceLocations;
import alfheim.api.lib.LibShaderIDs;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderEntityGravityTrap extends Render {

	Random rand = new Random();
	
	public static ShadedObject so = new ShadedObject(LibShaderIDs.idGravity, RenderPostShaders.getNextAvailableRenderObjectMaterialID(), LibResourceLocations.gravity) {

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
			glShadeModel(GL_FLAT);
		}
		
	};
	
	public RenderEntityGravityTrap() {
		super();
		shadowSize = 0.0F;
		RenderPostShaders.registerShadedObject(so);
	}
	
	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return null;
	}
	
	@Override
	public void doRender(Entity e, double x, double y, double z, float yaw, float partialTick) {
		glPushMatrix();
		glTranslated(x, y + 0.01, z);

		float live = e.ticksExisted;
		float charge = Math.min(20F, live + partialTick);
		float chargeMul = charge / 20F;
		
		rand.setSeed(e.getUniqueID().getMostSignificantBits());

		float s = chargeMul;
		s += Math.min(1F, (live + partialTick) * 0.2F);
		s *= 2F;
		glScalef(s, s, s);
		
		glRotatef(charge * 9F + (e.ticksExisted + partialTick) * 0.5F + rand.nextFloat() * 360F, 0F, 1F, 0F);

		so.addTranslation();

		glPopMatrix();
	}
	
	
}