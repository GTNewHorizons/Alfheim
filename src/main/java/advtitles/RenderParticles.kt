package advtitles

import alfheim.client.core.util.mc
import alfheim.common.core.util.D
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.RenderHelper
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.common.MinecraftForge
import org.lwjgl.opengl.GL11

object RenderParticles {
	
	init {
		MinecraftForge.EVENT_BUS.register(this)
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onRenderWorldLastEvent(event: RenderWorldLastEvent) {
		mc.entityRenderer.enableLightmap(event.partialTicks.D)
		RenderHelper.disableStandardItemLighting()
		GL11.glColor4f(1f, 1f, 1f, 1f)
		GL11.glDepthMask(false)
		GL11.glEnable(GL11.GL_BLEND)
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569f)
		mc.mcProfiler.startSection("coinParticles")
		EntityCoinFx.renderQueue()
		mc.mcProfiler.endSection()
		GL11.glDisable(GL11.GL_BLEND)
		GL11.glDepthMask(true)
		mc.entityRenderer.disableLightmap(event.partialTicks.D)
	}
}
