package alfheim.common.spell.fire

import org.lwjgl.opengl.GL11.*

import alexsocol.asjlib.ASJUtilities
import alfheim.api.entity.EnumRace
import alfheim.api.lib.LibResourceLocations
import alfheim.api.spell.SpellBase
import alfheim.common.entity.spell.EntitySpellFirewall
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.entity.EntityLivingBase

class SpellFirewall: SpellBase("firewall", EnumRace.SALAMANDER, 4000, 200, 15) {
	
	override fun performCast(caster: EntityLivingBase): SpellBase.SpellCastResult {
		val result = checkCastOver(caster)
		if (result == SpellBase.SpellCastResult.OK)
			caster.worldObj.spawnEntityInWorld(EntitySpellFirewall(caster.worldObj, caster))
		return result
	}
	
	override fun render(caster: EntityLivingBase) {
		val s = 3
		//glDisable(GL_ALPHA_TEST);
		glAlphaFunc(GL_GREATER, 0.003921569f)
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		ASJUtilities.interpolatedTranslation(caster)
		glRotated((-caster.rotationYaw).toDouble(), 0.0, 1.0, 0.0)
		glTranslated(0.0, -1.62, 5.0)
		Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.targetq)
		Tessellator.instance.startDrawingQuads()
		Tessellator.instance.addVertexWithUV((-s).toDouble(), 0.1, -1.0, 0.0, 0.0)
		Tessellator.instance.addVertexWithUV((-s).toDouble(), 0.1, 1.0, 0.0, 1.0)
		Tessellator.instance.addVertexWithUV(s.toDouble(), 0.1, 1.0, 1.0, 1.0)
		Tessellator.instance.addVertexWithUV(s.toDouble(), 0.1, -1.0, 1.0, 0.0)
		Tessellator.instance.draw()
		glDisable(GL_BLEND)
		glAlphaFunc(GL_GREATER, 0.1f)
		//glEnable(GL_ALPHA_TEST);
	}
}