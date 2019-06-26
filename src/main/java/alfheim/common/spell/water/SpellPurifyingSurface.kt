package alfheim.common.spell.water

import org.lwjgl.opengl.GL11.*

import alexsocol.asjlib.math.Vector3
import alfheim.api.entity.EnumRace
import alfheim.api.lib.LibResourceLocations
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.SpellEffectHandlerClient.Spells
import alfheim.common.core.handler.CardinalSystem.PartySystem
import alfheim.common.core.handler.SpellEffectHandler
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.entity.EntityLivingBase
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect

class SpellPurifyingSurface: SpellBase("purifyingsurface", EnumRace.UNDINE, 5000, 600, 20) {
	
	override fun performCast(caster: EntityLivingBase): SpellBase.SpellCastResult {
		val result = checkCast(caster)
		if (result != SpellBase.SpellCastResult.OK) return result
		
		SpellEffectHandler.sendPacket(Spells.PURE_AREA, caster)
		
		val list = caster.worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, caster.boundingBox.expand(5.0, 0.5, 5.0))
		for (living in list) {
			if (!PartySystem.mobsSameParty(caster, living) || Vector3.entityDistancePlane(living, caster) > 5) continue
			
			living.extinguish()
			living.removePotionEffect(Potion.poison.id)
			living.addPotionEffect(PotionEffect(Potion.fireResistance.id, 3600, 0, true))
			
			SpellEffectHandler.sendPacket(Spells.PURE, living)
		}
		return result
	}
	
	override fun render(caster: EntityLivingBase) {
		val s = 5.0
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