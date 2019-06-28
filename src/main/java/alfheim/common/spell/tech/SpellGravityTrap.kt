package alfheim.common.spell.tech

import alexsocol.asjlib.ASJUtilities
import alfheim.api.entity.EnumRace
import alfheim.api.lib.LibResourceLocations
import alfheim.api.spell.SpellBase
import alfheim.common.entity.spell.EntitySpellGravityTrap
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.MovingObjectPosition.MovingObjectType
import org.lwjgl.opengl.GL11.*

class SpellGravityTrap: SpellBase("gravitytrap", EnumRace.LEPRECHAUN, 10000, 600, 20) {
	
	override fun performCast(caster: EntityLivingBase): SpellBase.SpellCastResult {
		val mop = ASJUtilities.getSelectedBlock(caster, 15.0, true)
		if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK) {
			val result = checkCastOver(caster)
			if (result == SpellBase.SpellCastResult.OK)
				caster.worldObj.spawnEntityInWorld(EntitySpellGravityTrap(caster.worldObj, caster, mop.hitVec.xCoord, mop.hitVec.yCoord + 0.1, mop.hitVec.zCoord))
			return result
		}
		return SpellBase.SpellCastResult.WRONGTGT
	}
	
	override fun render(caster: EntityLivingBase) {
		val mop = ASJUtilities.getSelectedBlock(caster, 15.0, true)
		if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK) {
			val s = 4
			//glDisable(GL_ALPHA_TEST);
			glAlphaFunc(GL_GREATER, 0.003921569f)
			glEnable(GL_BLEND)
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
			Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.target)
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