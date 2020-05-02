package alfheim.common.spell.tech

import alexsocol.asjlib.*
import alfheim.api.entity.EnumRace
import alfheim.api.lib.LibResourceLocations
import alfheim.api.spell.SpellBase
import alfheim.common.entity.spell.EntitySpellGravityTrap
import net.minecraft.client.renderer.Tessellator
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.MovingObjectPosition.MovingObjectType
import org.lwjgl.opengl.GL11.*

object SpellGravityTrap: SpellBase("gravitytrap", EnumRace.LEPRECHAUN, 10000, 600, 20) {
	
	override var damage = 0.5f
	override var duration = 200
	override var efficiency = 16.0 // distance
	override var radius = 4.0
	
	override val usableParams
		get() = arrayOf(damage, duration, efficiency, radius)
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val mop = ASJUtilities.getSelectedBlock(caster, efficiency, true)
		if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK) {
			// if (!WorldGuardCommons.canDoSomethingHere(caster, mop.hitVec.xCoord, mop.hitVec.yCoord + 0.1, mop.hitVec.zCoord)) return SpellCastResult.NOTALLOW
			
			val result = checkCastOver(caster)
			if (result == SpellCastResult.OK)
				caster.worldObj.spawnEntityInWorld(EntitySpellGravityTrap(caster.worldObj, caster, mop.hitVec.xCoord, mop.hitVec.yCoord + 0.1, mop.hitVec.zCoord))
			return result
		}
		return SpellCastResult.WRONGTGT
	}
	
	override fun render(caster: EntityLivingBase) {
		val mop = ASJUtilities.getSelectedBlock(caster, efficiency, true)
		if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK) {
			val s = radius
			//glDisable(GL_ALPHA_TEST);
			glAlphaFunc(GL_GREATER, 1/255f)
			glEnable(GL_BLEND)
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
			mc.renderEngine.bindTexture(LibResourceLocations.target)
			Tessellator.instance.startDrawingQuads()
			Tessellator.instance.addVertexWithUV(mop.hitVec.xCoord - s, mop.hitVec.yCoord + 0.1, mop.hitVec.zCoord - s, 0.0, 0.0)
			Tessellator.instance.addVertexWithUV(mop.hitVec.xCoord - s, mop.hitVec.yCoord + 0.1, mop.hitVec.zCoord + s, 0.0, 1.0)
			Tessellator.instance.addVertexWithUV(mop.hitVec.xCoord + s, mop.hitVec.yCoord + 0.1, mop.hitVec.zCoord + s, 1.0, 1.0)
			Tessellator.instance.addVertexWithUV(mop.hitVec.xCoord + s, mop.hitVec.yCoord + 0.1, mop.hitVec.zCoord - s, 1.0, 0.0)
			Tessellator.instance.draw()
			glDisable(GL_BLEND)
			glAlphaFunc(GL_GREATER, 0.1f)
			//glEnable(GL_ALPHA_TEST);
		}
	}
}