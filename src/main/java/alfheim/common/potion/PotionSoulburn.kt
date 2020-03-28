package alfheim.common.potion

import alexsocol.asjlib.*
import alfheim.common.block.*
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.util.DamageSourceSpell
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.EntityLivingBase
import org.lwjgl.opengl.GL11.*

class PotionSoulburn: PotionAlfheim(AlfheimConfigHandler.potionIDSoulburn, "soulburn", true, 0xCC4400) {
	
	override fun isReady(time: Int, mod: Int): Boolean {
		return time % 20 == 0
	}
	
	override fun performEffect(living: EntityLivingBase, mod: Int) {
		living.attackEntityFrom(DamageSourceSpell.soulburn, (mod + 1).F)
	}
	
	companion object {
		
		@SideOnly(Side.CLIENT)
		fun renderFireInFirstPerson(partialTicks: Float) {
			val tessellator = Tessellator.instance
			glColor4f(1f, 1f, 1f, 0.9f)
			glEnable(GL_BLEND)
			OpenGlHelper.glBlendFunc(770, 771, 1, 0)
			val f1 = 1f
			
			for (i in 0..1) {
				glPushMatrix()
				val iicon = (AlfheimBlocks.redFlame as BlockRedFlame).icons[1]
				mc.textureManager.bindTexture(TextureMap.locationBlocksTexture)
				val f2 = iicon.minU
				val f3 = iicon.maxU
				val f4 = iicon.minV
				val f5 = iicon.maxV
				val f6 = (0f - f1) / 2f
				val f7 = f6 + f1
				val f8 = 0f - f1 / 2f
				val f9 = f8 + f1
				val f10 = -0.5f
				glTranslatef((-(i * 2 - 1)).F * 0.24f, -0.3f, 0f)
				glRotatef((i * 2 - 1).F * 10f, 0f, 1f, 0f)
				tessellator.startDrawingQuads()
				tessellator.addVertexWithUV(f6.D, f8.D, f10.D, f3.D, f5.D)
				tessellator.addVertexWithUV(f7.D, f8.D, f10.D, f2.D, f5.D)
				tessellator.addVertexWithUV(f7.D, f9.D, f10.D, f2.D, f4.D)
				tessellator.addVertexWithUV(f6.D, f9.D, f10.D, f3.D, f4.D)
				tessellator.draw()
				glPopMatrix()
			}
			
			glColor4f(1f, 1f, 1f, 1f)
			glDisable(GL_BLEND)
		}
	}
}
