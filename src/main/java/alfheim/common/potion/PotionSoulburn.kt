package alfheim.common.potion

import alfheim.common.block.BlockRedFlame
import alfheim.common.block.AlfheimBlocks
import alfheim.common.core.util.*
import cpw.mods.fml.relauncher.*
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.EntityLivingBase
import org.lwjgl.opengl.GL11.*

class PotionSoulburn: PotionAlfheim(AlfheimConfig.potionIDSoulburn, "soulburn", true, 0xCC4400) {
	
	override fun isReady(time: Int, mod: Int): Boolean {
		return time % 20 == 0
	}
	
	override fun performEffect(living: EntityLivingBase, mod: Int) {
		living.attackEntityFrom(DamageSourceSpell.soulburn, (mod + 1).toFloat())
	}
	
	companion object {
		
		@SideOnly(Side.CLIENT)
		fun renderFireInFirstPerson(partialTicks: Float) {
			val tessellator = Tessellator.instance
			glColor4f(1.0f, 1.0f, 1.0f, 0.9f)
			glEnable(GL_BLEND)
			OpenGlHelper.glBlendFunc(770, 771, 1, 0)
			val f1 = 1.0f
			
			for (i in 0..1) {
				glPushMatrix()
				val iicon = (AlfheimBlocks.redFlame as BlockRedFlame).icons[1]
				Minecraft.getMinecraft().textureManager.bindTexture(TextureMap.locationBlocksTexture)
				val f2 = iicon.minU
				val f3 = iicon.maxU
				val f4 = iicon.minV
				val f5 = iicon.maxV
				val f6 = (0.0f - f1) / 2.0f
				val f7 = f6 + f1
				val f8 = 0.0f - f1 / 2.0f
				val f9 = f8 + f1
				val f10 = -0.5f
				glTranslatef((-(i * 2 - 1)).toFloat() * 0.24f, -0.3f, 0.0f)
				glRotatef((i * 2 - 1).toFloat() * 10.0f, 0.0f, 1.0f, 0.0f)
				tessellator.startDrawingQuads()
				tessellator.addVertexWithUV(f6.toDouble(), f8.toDouble(), f10.toDouble(), f3.toDouble(), f5.toDouble())
				tessellator.addVertexWithUV(f7.toDouble(), f8.toDouble(), f10.toDouble(), f2.toDouble(), f5.toDouble())
				tessellator.addVertexWithUV(f7.toDouble(), f9.toDouble(), f10.toDouble(), f2.toDouble(), f4.toDouble())
				tessellator.addVertexWithUV(f6.toDouble(), f9.toDouble(), f10.toDouble(), f3.toDouble(), f4.toDouble())
				tessellator.draw()
				glPopMatrix()
			}
			
			glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
			glDisable(GL_BLEND)
		}
	}
}
