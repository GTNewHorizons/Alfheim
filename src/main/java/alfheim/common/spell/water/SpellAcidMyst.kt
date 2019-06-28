package alfheim.common.spell.water

import alfheim.api.entity.EnumRace
import alfheim.api.lib.LibResourceLocations
import alfheim.api.spell.SpellBase
import alfheim.common.entity.spell.EntitySpellAcidMyst
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.entity.EntityLivingBase
import org.lwjgl.opengl.GL11.*

class SpellAcidMyst: SpellBase("acidmyst", EnumRace.UNDINE, 8000, 400, 20) {
	
	override fun performCast(caster: EntityLivingBase): SpellBase.SpellCastResult {
		val result = checkCastOver(caster)
		if (result == SpellBase.SpellCastResult.OK) caster.worldObj.spawnEntityInWorld(EntitySpellAcidMyst(caster.worldObj, caster))
		return result
	}
	
	override fun render(caster: EntityLivingBase) {
		val s = 8.0
		glDisable(GL_CULL_FACE)
		glAlphaFunc(GL_GREATER, 0.003921569f)
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		glTranslated(0.0, -1.61, 0.0)
		Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.target)
		Tessellator.instance.startDrawingQuads()
		Tessellator.instance.addVertexWithUV(caster.posX - s, caster.posY, caster.posZ - s, 0.0, 0.0)
		Tessellator.instance.addVertexWithUV(caster.posX - s, caster.posY, caster.posZ + s, 0.0, 1.0)
		Tessellator.instance.addVertexWithUV(caster.posX + s, caster.posY, caster.posZ + s, 1.0, 1.0)
		Tessellator.instance.addVertexWithUV(caster.posX + s, caster.posY, caster.posZ - s, 1.0, 0.0)
		Tessellator.instance.draw()
		glDisable(GL_BLEND)
		glAlphaFunc(GL_GREATER, 0.1f)
		glEnable(GL_CULL_FACE)
	}
}