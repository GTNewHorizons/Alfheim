package alfheim.common.spell.fire

import alexsocol.asjlib.*
import alexsocol.asjlib.render.ASJRenderHelper
import alfheim.api.entity.EnumRace
import alfheim.api.lib.LibResourceLocations
import alfheim.api.spell.SpellBase
import alfheim.common.entity.spell.EntitySpellFirewall
import net.minecraft.client.renderer.Tessellator
import net.minecraft.entity.EntityLivingBase
import org.lwjgl.opengl.GL11.*

object SpellFirewall: SpellBase("firewall", EnumRace.SALAMANDER, 4000, 200, 15) {
	
	override var duration = 600
	
	override val usableParams: Array<Any>
		get() = arrayOf(damage, duration)
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val wall = EntitySpellFirewall(caster.worldObj, caster)
		
		// if (!WorldGuardCommons.canDoSomethingWithEntity(caster, wall)) return SpellCastResult.NOTALLOW
		
		val result = checkCastOver(caster)
		if (result == SpellCastResult.OK)
			caster.worldObj.spawnEntityInWorld(wall)
		
		return result
	}
	
	override fun render(caster: EntityLivingBase) {
		val s = 3
		//glDisable(GL_ALPHA_TEST);
		glAlphaFunc(GL_GREATER, 1 / 255f)
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		ASJRenderHelper.interpolatedTranslation(caster)
		glRotated((-caster.rotationYaw).D, 0.0, 1.0, 0.0)
		glTranslated(0.0, -1.62, 5.0)
		mc.renderEngine.bindTexture(LibResourceLocations.targetq)
		Tessellator.instance.startDrawingQuads()
		Tessellator.instance.addVertexWithUV((-s).D, 0.1, -1.0, 0.0, 0.0)
		Tessellator.instance.addVertexWithUV((-s).D, 0.1, 1.0, 0.0, 1.0)
		Tessellator.instance.addVertexWithUV(s.D, 0.1, 1.0, 1.0, 1.0)
		Tessellator.instance.addVertexWithUV(s.D, 0.1, -1.0, 1.0, 0.0)
		Tessellator.instance.draw()
		glDisable(GL_BLEND)
		glAlphaFunc(GL_GREATER, 0.1f)
		//glEnable(GL_ALPHA_TEST);
	}
}