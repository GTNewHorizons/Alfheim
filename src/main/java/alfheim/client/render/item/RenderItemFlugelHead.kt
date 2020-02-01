package alfheim.client.render.item

import alfheim.api.lib.LibResourceLocations
import alfheim.client.core.util.mc
import alfheim.client.model.entity.*
import alfheim.common.core.util.D
import alfheim.common.item.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.client.event.RenderPlayerEvent
import org.lwjgl.opengl.GL11.*
import vazkii.botania.client.core.helper.ShaderHelper
import vazkii.botania.client.render.entity.RenderDoppleganger
import vazkii.botania.client.render.tile.RenderTileSkullOverride
import vazkii.botania.common.item.ItemGaiaHead

object RenderItemFlugelHead {
	
	// because!
	var swap = false
	
	fun render(e: RenderPlayerEvent.Pre, player: EntityPlayer) {
		val head = player.getCurrentArmor(3)?.item
		if (head is ItemHeadFlugel || head is ItemHeadMiku || head is ItemGaiaHead) {
			swap = true
			e.renderer.modelBipedMain.bipedHead.showModel = false
			e.renderer.modelBipedMain.bipedHeadwear.showModel = e.renderer.modelBipedMain.bipedHead.showModel
			e.renderer.modelBipedMain.bipedEars.showModel = e.renderer.modelBipedMain.bipedHead.showModel
		}
	}
	
	fun render(e: RenderPlayerEvent.Specials.Post, player: EntityPlayer) {
		if (swap) {
			val yaw = player.prevRotationYawHead + (player.rotationYawHead - player.prevRotationYawHead) * e.partialRenderTick
			val yawOffset = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * e.partialRenderTick
			val pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * e.partialRenderTick
			
			glPushMatrix()
			glColor4d(1.0,1.0,1.0,1.0)
			glRotated(yawOffset.D, 0.0, -1.0, 0.0)
			glRotated((yaw - 270).D, 0.0, 1.0, 0.0)
			glRotated(pitch.D, 0.0, 0.0, 1.0)
			glRotated(-90.0, 0.0, 1.0, 0.0)
			
			when (player.getCurrentArmor(3)?.item) {
				is ItemHeadFlugel	-> {
					mc.renderEngine.bindTexture(LibResourceLocations.jibril)
					ModelBipedNew.model.head.render(0.0625f)
				}
				
				is ItemHeadMiku		-> {
					mc.renderEngine.bindTexture(LibResourceLocations.miku0)
					ModelBipedNew.model.head.render(0.0625f)
					mc.renderEngine.bindTexture(LibResourceLocations.miku2)
					ModelEntityFlugel.model2.renderAll()
				}
				
				is ItemGaiaHead		-> {
					mc.renderEngine.bindTexture(mc.thePlayer.locationSkin)
					ShaderHelper.useShader(ShaderHelper.doppleganger, RenderDoppleganger.defaultCallback)
					RenderTileSkullOverride.modelSkull.render(null, 0f, 0f, 0f, 0f, 0f, 0.0625f)
					ShaderHelper.releaseShader()
				}
			}
			
			glPopMatrix()
			
			swap = false
			e.renderer.modelBipedMain.bipedHead.showModel = true
			e.renderer.modelBipedMain.bipedHeadwear.showModel = e.renderer.modelBipedMain.bipedHead.showModel
			e.renderer.modelBipedMain.bipedEars.showModel = e.renderer.modelBipedMain.bipedHead.showModel
		}
	}
}
