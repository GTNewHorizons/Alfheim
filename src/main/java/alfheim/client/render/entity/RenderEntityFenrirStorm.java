package alfheim.client.render.entity;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.Random;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.math.OrientedBB;
import alexsocol.asjlib.math.Vector3;
import alfheim.common.entity.spell.EntitySpellFenrirStorm;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;

public class RenderEntityFenrirStorm extends Render {

	public RenderEntityFenrirStorm() {
		super();
		shadowSize = 0.0F;
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return null;
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTick) {
		EntitySpellFenrirStorm e = (EntitySpellFenrirStorm) entity;
		
		glPushMatrix();
		glTranslated(x, y, z);
		
		/*if (true) {
			OrientedBB area = new OrientedBB(AxisAlignedBB.getBoundingBox(-0.5, -0.5, -8, 0.5, 0.5, 8));
			area.rotateOX(e.rotationPitch);
			area.rotateOY(-e.rotationYaw);
			Vector3 v = new Vector3(e.getLookVec()).multiply(8.5);
			area.translate(v.x, v.y, v.z);
			area.draw(0);
		}*/
		
		int parts = Math.max(1, Math.min(e.ticksExisted * 2, 16));
		
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_LIGHTING);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
		glColor4d(0.65, 1, 1, 1);
		
		Random rand = new Random(e.getEntityId() + (e.ticksExisted / 3));
		
		for (int i = 0; i < 3; i++) {
			glLineWidth(ASJUtilities.randInBounds(1, 3));
			lightning(rand, parts, Vector3.zero.copy(), new Vector3(ASJUtilities.getLookVec(e)).normalize(), Vector3.zero.copy());
		}
		
		glEnable(GL_LIGHTING);
		glEnable(GL_TEXTURE_2D);

		glPopMatrix();
	}
	
	private void lightning(Random rand, int parts, Vector3 start, Vector3 dir, Vector3 end) {
		double amp = 0.65;
		ArrayList<Fork> forks = new ArrayList<Fork>();
		
		glBegin(GL_LINE_STRIP);
		for (int i = 0; i < parts; i++) {
			if (rand.nextInt() % 7 == 0) forks.add(new Fork(start, randVec(dir, rand), rand.nextInt(3)));
			start.glVertex().set(end.set(dir).add(rand.nextDouble() * amp * 2 - amp, rand.nextDouble() * amp * 2 - amp, rand.nextDouble() * amp * 2 - amp));
			dir.extend(1);
		}
		end.glVertex();
		
		glEnd();
		
		for (Fork fork : forks) {
			lightning(rand, fork.parts, fork.start, fork.dir, fork.start);
		}
	}
	
	private Vector3 randVec(Vector3 dir, Random rand) {
		double amp = 0.65;
		return dir.copy().extend(1).add(rand.nextDouble() * amp * 2 - amp, rand.nextDouble() * amp * 2 - amp, rand.nextDouble() * amp * 2 - amp);
	}
	
	private class Fork {
		public int parts;
		public Vector3 start, dir;
		public Fork(Vector3 s, Vector3 d, int p) {
			start = s.copy();
			dir = d.copy();
			parts = Math.max(p, 1);
		}
	}
}