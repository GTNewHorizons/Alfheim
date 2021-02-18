package alfheim.common.spell.water

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.lib.LibResourceLocations
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.VisualEffectHandlerClient.VisualEffects
import alfheim.common.core.handler.CardinalSystem.PartySystem
import alfheim.common.core.handler.VisualEffectHandler
import alfheim.common.network.MessageEffect
import net.minecraft.client.renderer.Tessellator
import net.minecraft.entity.EntityLivingBase
import net.minecraft.potion.*
import org.lwjgl.opengl.GL11.*

object SpellPurifyingSurface: SpellBase("purifyingsurface", EnumRace.UNDINE, 5000, 600, 20) {
	
	override var duration = 3600
	override var radius = 5.0
	
	override val usableParams
		get() = arrayOf(duration, radius)
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val result = checkCast(caster)
		if (result != SpellCastResult.OK) return result
		
		VisualEffectHandler.sendPacket(VisualEffects.PURE_AREA, caster)
		
		val list = caster.worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, caster.boundingBox.expand(radius)) as List<EntityLivingBase>
		for (living in list) {
			if (!PartySystem.mobsSameParty(caster, living) || Vector3.entityDistancePlane(living, caster) > radius) continue
			
			living.extinguish()
			living.removePotionEffect(Potion.poison.id)
			living.addPotionEffect(PotionEffect(Potion.fireResistance.id, duration, -1, true))
			AlfheimCore.network.sendToAll(MessageEffect(living.entityId, Potion.fireResistance.id, duration, -1))
			
			VisualEffectHandler.sendPacket(VisualEffects.PURE, living)
		}
		return result
	}
	
	override fun render(caster: EntityLivingBase) {
		glDisable(GL_CULL_FACE)
		glAlphaFunc(GL_GREATER, 1 / 255f)
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		glTranslated(0.0, -1.61, 0.0)
		mc.renderEngine.bindTexture(LibResourceLocations.target)
		Tessellator.instance.startDrawingQuads()
		Tessellator.instance.addVertexWithUV(caster.posX - radius, caster.posY, caster.posZ - radius, 0.0, 0.0)
		Tessellator.instance.addVertexWithUV(caster.posX - radius, caster.posY, caster.posZ + radius, 0.0, 1.0)
		Tessellator.instance.addVertexWithUV(caster.posX + radius, caster.posY, caster.posZ + radius, 1.0, 1.0)
		Tessellator.instance.addVertexWithUV(caster.posX + radius, caster.posY, caster.posZ - radius, 1.0, 0.0)
		Tessellator.instance.draw()
		glDisable(GL_BLEND)
		glAlphaFunc(GL_GREATER, 0.1f)
		glEnable(GL_CULL_FACE)
	}
}