package advtitles

import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.client.core.util.mc
import alfheim.common.core.util.F
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.common.MinecraftForge
import vazkii.botania.common.Botania
import java.awt.Color
import kotlin.math.max

object RenderAdvancedTitle {
	
	init {
		MinecraftForge.EVENT_BUS.register(this)
	}
	
	@SubscribeEvent
	fun onPlayerRender(e: RenderPlayerEvent.Specials.Post) {
		val player = e.entityPlayer
//		if (player.isInvisible || player.isInvisibleToPlayer(mc.thePlayer) || player.isPotionActive(Potion.invisibility)) return
		if (player == mc.thePlayer && mc.gameSettings.thirdPersonView == 0) return
		
		val title = ItemAdvancedTitle.EnumTitle.getTitle(player) ?: return
		
		if (player.ticksExisted % 10 == 0 && !mc.isGamePaused) {
			val world = player.worldObj
			
			val yOff = if (player === mc.thePlayer) -1.6 else 0.0
			val v = Vector3().rand().sub(0.5).normalize().mul(player.width, player.height, player.width).mul(Math.random()).extend(0.5).add(player).add(0, yOff + player.height / 2, 0)
			
			when (title) {
				ItemAdvancedTitle.EnumTitle.EREBON    -> {
					AlfheimCore.proxy.bloodFX(world, v.x, max(player.posY + player.height * 1.2 + yOff, v.y - yOff), v.z, 60, 0.25f)
				}
				
				ItemAdvancedTitle.EnumTitle.SELIA     -> {
					AlfheimCore.proxy.featherFX(world, v.x, max(player.posY + player.height * 1.2 + yOff, v.y - yOff), v.z, Color(0x008800).rgb)
				}
				
				ItemAdvancedTitle.EnumTitle.LUXON     -> {
					Botania.proxy.wispFX(world, v.x, v.y, v.z, 0f, (Math.random() * 0.1 + 0.9).F, 1f, 1f)
				}
				
				ItemAdvancedTitle.EnumTitle.PLUTO     -> {
					AdvTitlesCore.coinFX(world, v.x, max(player.posY + player.height * 1.2 + yOff, v.y - yOff), v.z, 100, 0.25F, 0.5f)
				}
				
				ItemAdvancedTitle.EnumTitle.SMITH     -> Unit
				
				ItemAdvancedTitle.EnumTitle.CENTURION -> {
					val color = Color(0xe9a6ff)
					val r = color.red / 255f
					val g = color.green / 255f
					val b = color.blue / 255f
					
					Botania.proxy.wispFX(world, v.x, v.y, v.z, r, g, b, 0.5f)
					Botania.proxy.wispFX(world, v.x, v.y, v.z, 0f, 1f, 0f, 0.3f)
				}
				
				ItemAdvancedTitle.EnumTitle.UNKINGER  -> {
					Botania.proxy.wispFX(world, v.x, v.y, v.z, 1f, 0.5f, 0f, 0.3f)
					v.rand().sub(0.5).normalize().mul(player.width, player.height, player.width).mul(Math.random()).add(player).add(0, yOff + player.height / 2, 0)
					Botania.proxy.wispFX(world, v.x, v.y, v.z, 1f, 1f, 1f, 0.3f)
				}
				
				ItemAdvancedTitle.EnumTitle.PROTECTOR -> {
					Botania.proxy.wispFX(world, v.x, v.y, v.z, 0f, 1f, 0f, 0.3f)
					v.rand().sub(0.5).normalize().mul(player.width, player.height, player.width).mul(Math.random()).add(player).add(0, yOff + player.height / 2, 0)
					Botania.proxy.wispFX(world, v.x, v.y, v.z, 1f, 0.8f, 0f, 0.3f)
				}
				
				ItemAdvancedTitle.EnumTitle.SAVER     -> {
					val color = Color.getHSBColor(Botania.proxy.worldElapsedTicks * 2 % 360f / 360f, 1f, 1f)
					val r = color.red / 255f
					val g = color.green / 255f
					val b = color.blue / 255f
					
					v.mul(1.2)
					Botania.proxy.wispFX(world, v.x, v.y, v.z, r, g, b, 1f)
				}
				
				ItemAdvancedTitle.EnumTitle.ARCHMAGE  -> {
					Botania.proxy.wispFX(world, v.x, v.y, v.z, 0.5f, 0f, 1f, 0.3f)
					v.rand().sub(0.5).normalize().mul(player.width, player.height, player.width).mul(Math.random()).add(player).add(0, yOff + player.height / 2, 0)
					Botania.proxy.wispFX(world, v.x, v.y, v.z, 0f, 0.8f, 1f, 0.3f)
				}
			}
		}
		
//		glPushMatrix()
//		glEnable(GL_BLEND)
//		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
//		glDisable(GL_CULL_FACE)
//		glDisable(GL_LIGHTING)
//		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f)
//		glShadeModel(GL_SMOOTH)
		
//		glTranslated(0.0, 1.35, 0.0)
//		glColor4f(1f, 1f, 1f, 1f)
//		mc.renderEngine.bindTexture(texture[title.ordinal])
//		val tes = Tessellator.instance
//		tes.startDrawingQuads()
//		tes.addVertexWithUV(-1.0, 0.0, -1.0, 0.0, 0.0)
//		tes.addVertexWithUV(-1.0, 0.0, 1.0, 0.0, 1.0)
//		tes.addVertexWithUV(1.0, 0.0, 1.0, 1.0, 1.0)
//		tes.addVertexWithUV(1.0, 0.0, -1.0, 1.0, 0.0)
//		tes.draw()
//		glColor4f(1f, 1f, 1f, 1f)
		
//		glShadeModel(GL_FLAT)
//		glEnable(GL_LIGHTING)
//		glEnable(GL_CULL_FACE)
//		glDisable(GL_BLEND)
//		glPopMatrix()
	}
	
	val texture = Array(ItemAdvancedTitle.EnumTitle.values().size) { ResourceLocation("advtitles", "textures/model/entity/title${ItemAdvancedTitle.EnumTitle.values()[it]}.png") }
}