package alfheim.client.render.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.profiler.Profiler;

import org.lwjgl.opengl.GL11;

public final class ParticleRenderDispatcherBound {

	public static int wispFxCount = 0;

	// Called from LightningHandler.onRenderWorldLast since that was
	// already registered. /shrug
	public static void dispatch() {
		Tessellator tessellator = Tessellator.instance;

		Profiler profiler = Minecraft.getMinecraft().mcProfiler;

		GL11.glPushAttrib(GL11.GL_LIGHTING);
		GL11.glDepthMask(false);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
		GL11.glDisable(GL11.GL_LIGHTING);

		profiler.startSection("wisp");
		FXWispBound.dispatchQueuedRenders(tessellator);
		profiler.endSection();

		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDepthMask(true);
		GL11.glPopAttrib();
	}

}
