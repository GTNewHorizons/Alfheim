package alfheim.client.render.entity;

import static org.lwjgl.opengl.GL11.*;

import alexsocol.asjlib.render.RenderPostShaders;
import alexsocol.asjlib.render.ShadedObject;
import alfheim.api.lib.LibResourceLocations;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.client.core.helper.ShaderHelper;

public class RenderEntityFlugel extends RenderLiving {

	public static final ShadedObject so = new ShadedObject(ShaderHelper.halo, RenderPostShaders.getNextAvailableRenderObjectMaterialID(), LibResourceLocations.halo) {
		
		@Override
		public void preRender() {
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glShadeModel(GL_SMOOTH);
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
			glDisable(GL_LIGHTING);
			glDisable(GL_CULL_FACE);
			glColor4f(1F, 1F, 1F, 1F);
		}
		
		@Override
		public void drawMesh() {
			Tessellator tes = Tessellator.instance;
			tes.startDrawingQuads();
			tes.addVertexWithUV(-0.75, 0, -0.75, 0, 0);
			tes.addVertexWithUV(-0.75, 0, 0.75, 0, 1);
			tes.addVertexWithUV(0.75, 0, 0.75, 1, 1);
			tes.addVertexWithUV(0.75, 0, -0.75, 1, 0);
			tes.draw();
		}
		
		@Override
		public void postRender() {
			glEnable(GL_CULL_FACE);
			//glEnable(GL_LIGHTING); breaks some other stuuf, urgh -_-
			glShadeModel(GL_FLAT);
			glDisable(GL_BLEND);
		}
	};
	
	public RenderEntityFlugel(ModelBase model, float shadowSize) {
		super(model, shadowSize);
		RenderPostShaders.registerShadedObject(so);
	}

	@Override
	public ResourceLocation getEntityTexture(Entity par1Entity) {
		return LibResourceLocations.jibril;
	}
}