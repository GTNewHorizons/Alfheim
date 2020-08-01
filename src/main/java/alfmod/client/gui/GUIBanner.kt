package alfmod.client.gui

import alexsocol.asjlib.mc
import alfmod.AlfheimModularCore
import alfmod.common.core.handler.*
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.TickEvent
import cpw.mods.fml.common.network.FMLNetworkEvent
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.Tessellator
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.common.MinecraftForge
import org.lwjgl.opengl.GL11.*
import kotlin.math.*

object GUIBanner: Gui() {
	
	var pop = 0.0
	
	val bannerHV = ResourceLocation(AlfheimModularCore.MODID, "textures/banner/HellishVacation.png")
	val bannerWOTW = ResourceLocation(AlfheimModularCore.MODID, "textures/banner/WrathOfTheWinter.png")
	
	val banners = arrayOf(
						  WRATH_OF_THE_WINTER to bannerWOTW,
						  HELLISH_VACATION to bannerHV
						  )
	
	init {
		FMLCommonHandler.instance().bus().register(this)
		MinecraftForge.EVENT_BUS.register(this)
	}
	
	@SubscribeEvent
	fun onOverlayRendering(e: RenderGameOverlayEvent.Post) {
		if (e.type != RenderGameOverlayEvent.ElementType.HOTBAR) return
		
		for (pair in banners) {
			if (!pair.first) continue
			
			glPushMatrix()
			glColor4f(1f, 1f, 1f, 1f)
			
			val width = e.resolution.scaledWidth_double / 2
			val height = width / 8
			val offsetY = min(0.0, -(pop.pow(2) / 12 - 100 - (e.partialTicks * 2))) + height / 8
			
			mc.textureManager.bindTexture(pair.second)
			
			Tessellator.instance.startDrawingQuads()
			Tessellator.instance.addVertexWithUV(width / 2, offsetY, 0.0, 0.0, 0.0)
			Tessellator.instance.addVertexWithUV(width / 2, height + offsetY, 0.0, 0.0, 1.0)
			Tessellator.instance.addVertexWithUV(width / 2 + width, height + offsetY, 0.0, 1.0, 1.0)
			Tessellator.instance.addVertexWithUV(width / 2 + width, offsetY, 0.0, 1.0, 0.0)
			Tessellator.instance.draw()
			
			glPopMatrix()
		}
	}
	
	@SubscribeEvent
	fun onClientTick(e: TickEvent.ClientTickEvent) {
		pop -= 0.5
	}
	
	@SubscribeEvent
	fun onClientJoinServer(e: FMLNetworkEvent.ClientConnectedToServerEvent) {
		pop = 50.0
	}
}