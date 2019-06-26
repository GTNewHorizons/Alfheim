package alfheim.client.render.item

import org.lwjgl.opengl.GL11.*

import alfheim.api.lib.LibResourceLocations
import alfheim.client.model.entity.ModelBipedNew
import alfheim.common.item.ItemFlugelHead
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.client.event.RenderPlayerEvent
import vazkii.botania.client.core.helper.ShaderHelper
import vazkii.botania.client.render.entity.RenderDoppleganger
import vazkii.botania.client.render.tile.RenderTileSkullOverride
import vazkii.botania.common.item.ItemGaiaHead

object RenderItemFlugelHead {
	
	// because!
	var notSwap = true
	
	fun render(e: RenderPlayerEvent.Pre, player: EntityPlayer) {
		if (player.getCurrentArmor(3) != null && (player.getCurrentArmor(3).item is ItemFlugelHead || player.getCurrentArmor(3).item is ItemGaiaHead)) {
			notSwap = false
			e.renderer.modelBipedMain.bipedHeadwear.showModel = notSwap
			e.renderer.modelBipedMain.bipedHead.showModel = e.renderer.modelBipedMain.bipedHeadwear.showModel
		}
	}
	
	fun render(e: RenderPlayerEvent.Specials.Post, player: EntityPlayer) {
		if (!notSwap) {
			val yaw = player.prevRotationYawHead + (player.rotationYawHead - player.prevRotationYawHead) * e.partialRenderTick
			val yawOffset = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * e.partialRenderTick
			val pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * e.partialRenderTick
			
			glPushMatrix()
			glRotated(yawOffset.toDouble(), 0.0, -1.0, 0.0)
			glRotated((yaw - 270).toDouble(), 0.0, 1.0, 0.0)
			glRotated(pitch.toDouble(), 0.0, 0.0, 1.0)
			glRotated(-90.0, 0.0, 1.0, 0.0)
			
			if (player.getCurrentArmor(3) != null) {
				if (player.getCurrentArmor(3).item is ItemFlugelHead) {
					Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.jibril)
					ModelBipedNew.model.head.render(0.0625f)
				} else if (player.getCurrentArmor(3).item is ItemGaiaHead) {
					Minecraft.getMinecraft().renderEngine.bindTexture(Minecraft.getMinecraft().thePlayer.locationSkin)
					ShaderHelper.useShader(ShaderHelper.doppleganger, RenderDoppleganger.defaultCallback)
					RenderTileSkullOverride.modelSkull.render(null, 0f, 0f, 0f, 0f, 0f, 0.0625f)
					ShaderHelper.releaseShader()
				}
			}
			
			glPopMatrix()
			
			notSwap = true
			e.renderer.modelBipedMain.bipedHeadwear.showModel = notSwap
			e.renderer.modelBipedMain.bipedHead.showModel = e.renderer.modelBipedMain.bipedHeadwear.showModel
		}
	}
}
