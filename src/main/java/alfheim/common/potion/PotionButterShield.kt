package alfheim.common.potion

import alexsocol.asjlib.*
import alexsocol.asjlib.render.ASJRenderHelper
import alexsocol.patcher.event.RenderEntityPostEvent
import alfheim.api.lib.LibResourceLocations
import alfheim.common.core.handler.AlfheimConfigHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.entity.EntityLivingBase
import org.lwjgl.opengl.GL11
import java.util.*
import kotlin.math.max

class PotionButterShield: PotionAlfheim(AlfheimConfigHandler.potionIDButterShield, "butterShield", false, 0x00FFFF) {
	
	init {
		eventForge()
	}
	
	val rand = Random()
	
	@SubscribeEvent
	fun onEntityPostRender(e: RenderEntityPostEvent) {
		if (!AlfheimConfigHandler.enableMMO) return
		val entity = e.entity as? EntityLivingBase ?: return
		
		var count = entity.getActivePotionEffect(AlfheimConfigHandler.potionIDButterShield)?.getAmplifier()?.times(32) ?: return
		
		ASJRenderHelper.setGlow()
		ASJRenderHelper.setTwoside()
		ASJRenderHelper.setBlend()
		
		GL11.glTranslated(e.x, e.y, e.z)
		val s = max(entity.width, entity.height) / 1.25
		glScaled(s)
		
		rand.setSeed(entity.uniqueID.mostSignificantBits)
		mc.renderEngine.bindTexture(LibResourceLocations.butterflyFlat)
		
		while (count > 0) {
			GL11.glPushMatrix()
			GL11.glColor4d(rand.nextDouble() * 0.4 + 0.6, rand.nextDouble() * 0.4 + 0.6, rand.nextDouble() * 0.4 + 0.6, 1.0)
			GL11.glTranslated(0.0, if (entity === mc.thePlayer) -(mc.thePlayer.defaultEyeHeight * s) else entity.height / s / 2, 0.0)
			GL11.glRotated(rand.nextDouble() * 360 + entity.ticksExisted + mc.timer.renderPartialTicks, rand.nextDouble() * 2 - 1, rand.nextDouble() * 2 - 1, rand.nextDouble() * 2 - 1)
			GL11.glTranslated(0.0, 0.0, -1.0)
			ASJRenderHelper.drawRect(0.2)
			GL11.glPopMatrix()
			--count
		}
		
		ASJRenderHelper.discard()
	}
}
