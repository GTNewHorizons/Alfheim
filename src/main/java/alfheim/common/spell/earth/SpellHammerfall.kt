package alfheim.common.spell.earth

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
import net.minecraft.util.*
import net.minecraftforge.common.util.ForgeDirection
import org.lwjgl.opengl.GL11.*

class SpellHammerfall: SpellBase("hammerfall", EnumRace.GNOME, 10000, 200, 20) {
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		if (!caster.onGround || caster.worldObj.isAirBlock(MathHelper.floor_double(caster.posX), MathHelper.floor_double(caster.posY) - 1, MathHelper.floor_double(caster.posZ))) return SpellCastResult.WRONGTGT
		
		val result = checkCastOver(caster)
		if (result != SpellCastResult.OK) return result
		
		SpellEffectHandler.sendPacket(Spells.TREMORS, caster)
		
		val list = caster.worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, caster.boundingBox.expand(10.0, 2.0, 10.0)) as MutableList<EntityLivingBase>
		list.remove(caster)
		for (living in list) {
			val block = living.worldObj.getBlock(MathHelper.floor_double(living.posX), MathHelper.floor_double(living.posY - 1), MathHelper.floor_double(living.posZ))
			if (living.onGround &&
				block.material.isSolid &&
				block.isSideSolid(living.worldObj, MathHelper.floor_double(living.posX), MathHelper.floor_double(living.posY - 1), MathHelper.floor_double(living.posZ), ForgeDirection.UP) &&
				block.getBlockHardness(living.worldObj, MathHelper.floor_double(living.posX), MathHelper.floor_double(living.posY - 1), MathHelper.floor_double(living.posZ)) < 2 &&
				!PartySystem.mobsSameParty(caster, living) &&
				Vector3.entityDistancePlane(living, caster) < 10)
				living.attackEntityFrom(DamageSource.inWall, over(caster, 10.0))
		}
		return result
	}
	
	override fun render(caster: EntityLivingBase) {
		val s = 10.0
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