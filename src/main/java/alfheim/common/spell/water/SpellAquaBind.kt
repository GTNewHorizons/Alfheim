package alfheim.common.spell.water

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.lib.LibResourceLocations
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.SpellEffectHandlerClient.Spells
import alfheim.common.core.handler.CardinalSystem.PartySystem
import alfheim.common.core.handler.SpellEffectHandler
import alfheim.common.network.MessageEffect
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.Tessellator
import net.minecraft.entity.EntityLivingBase
import net.minecraft.potion.*
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.MovingObjectPosition.MovingObjectType
import org.lwjgl.opengl.GL11.*

class SpellAquaBind: SpellBase("aquabind", EnumRace.UNDINE, 4000, 600, 15) {
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val mop = ASJUtilities.getSelectedBlock(caster, 15.0, false)
		if (mop == null || mop.typeOfHit == MovingObjectType.MISS) return SpellCastResult.WRONGTGT
		val hit = Vector3(mop.hitVec)
		if (mop.typeOfHit == MovingObjectType.BLOCK) {
			if (mop.sideHit == 0) hit.y -= 0.1
			if (mop.sideHit == 1) hit.y += 0.1
		}
		
		val result = checkCast(caster)
		if (result != SpellCastResult.OK) return result
		
		val l = caster.worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, AxisAlignedBB.getBoundingBox(hit.x, hit.y, hit.z, hit.x, hit.y, hit.z).expand(3.5, 0.5, 3.5)) as List<EntityLivingBase>
		for (e in l) {
			if (PartySystem.mobsSameParty(caster, e)) continue
			val mob = Vector3.fromEntityCenter(e)
			mob.y = hit.y
			if (hit.copy().sub(mob).length() <= 3.5) {
				e.addPotionEffect(PotionEffect(Potion.moveSlowdown.id, 100, 1, true))
				AlfheimCore.network.sendToAll(MessageEffect(e.entityId, Potion.moveSlowdown.id, 100, 4))
			}
		}
		
		SpellEffectHandler.sendPacket(Spells.AQUABIND, caster.dimension, hit.x, hit.y, hit.z)
		return result
	}
	
	override fun render(caster: EntityLivingBase) {
		val mop = ASJUtilities.getSelectedBlock(caster, 15.0, false)
		if (mop == null || mop.typeOfHit == MovingObjectType.MISS) return
		val y = if (mop.typeOfHit == MovingObjectType.BLOCK) 0.1 * (if (mop.sideHit == 0) -1.0 else 1.0) else 0.0
		val s = 3.5
		glDisable(GL_CULL_FACE)
		//glDisable(GL_ALPHA_TEST);
		glAlphaFunc(GL_GREATER, 0.003921569f)
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.target)
		Tessellator.instance.startDrawingQuads()
		Tessellator.instance.addVertexWithUV(mop.hitVec.xCoord - s, mop.hitVec.yCoord + y, mop.hitVec.zCoord - s, 0.0, 0.0)
		Tessellator.instance.addVertexWithUV(mop.hitVec.xCoord - s, mop.hitVec.yCoord + y, mop.hitVec.zCoord + s, 0.0, 1.0)
		Tessellator.instance.addVertexWithUV(mop.hitVec.xCoord + s, mop.hitVec.yCoord + y, mop.hitVec.zCoord + s, 1.0, 1.0)
		Tessellator.instance.addVertexWithUV(mop.hitVec.xCoord + s, mop.hitVec.yCoord + y, mop.hitVec.zCoord - s, 1.0, 0.0)
		Tessellator.instance.draw()
		glDisable(GL_BLEND)
		glAlphaFunc(GL_GREATER, 0.1f)
		//glEnable(GL_ALPHA_TEST);
		glEnable(GL_CULL_FACE)
	}
}