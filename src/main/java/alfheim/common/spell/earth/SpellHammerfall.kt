package alfheim.common.spell.earth

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alfheim.api.entity.EnumRace
import alfheim.api.lib.LibResourceLocations
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.VisualEffectHandlerClient.VisualEffects
import alfheim.common.core.handler.CardinalSystem.PartySystem
import alfheim.common.core.handler.VisualEffectHandler
import alfheim.common.core.util.DamageSourceSpell
import alfheim.common.security.InteractionSecurity
import net.minecraft.client.renderer.Tessellator
import net.minecraft.entity.EntityLivingBase
import net.minecraftforge.common.util.ForgeDirection
import org.lwjgl.opengl.GL11.*

object SpellHammerfall: SpellBase("hammerfall", EnumRace.GNOME, 10000, 200, 20) {
	
	override var damage = 10f
	override var radius = 10.0
	
	override val usableParams
		get() = arrayOf(damage, radius)
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		if (!caster.onGround || caster.worldObj.isAirBlock(caster.posX.mfloor(), caster.posY.mfloor() - 1, caster.posZ.mfloor())) return SpellCastResult.WRONGTGT
		
		// if (!WorldGuardCommons.canDoSomethingHere(caster)) return SpellCastResult.NOTALLOW
		
		val result = checkCastOver(caster)
		if (result != SpellCastResult.OK) return result
		
		VisualEffectHandler.sendPacket(VisualEffects.TREMORS, caster)
		
		val list = caster.worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, caster.boundingBox.expand(radius, radius/5, radius)) as MutableList<EntityLivingBase>
		list.remove(caster)
		
		for (living in list) {
			val block = living.worldObj.getBlock(living.posX.mfloor(), (living.posY - 1).mfloor(), living.posZ.mfloor())
			if (living.onGround &&
				block.material.isSolid &&
				block.isSideSolid(living.worldObj, living.posX.mfloor(), (living.posY - 1).mfloor(), living.posZ.mfloor(), ForgeDirection.UP) &&
				block.getBlockHardness(living.worldObj, living.posX.mfloor(), (living.posY - 1).mfloor(), living.posZ.mfloor()) < 2 &&
				!PartySystem.mobsSameParty(caster, living) &&
				Vector3.entityDistancePlane(living, caster) < radius) {
				
				if (!InteractionSecurity.canHurtEntity(caster, living)) continue
				
				living.attackEntityFrom(DamageSourceSpell.hammerfall(caster), over(caster, damage.D))
			}
		}
		
		return result
	}
	
	override fun render(caster: EntityLivingBase) {
		glDisable(GL_CULL_FACE)
		glAlphaFunc(GL_GREATER, 0.003921569f)
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