package alfheim.common.spell.sound

import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.SpellEffectHandlerClient.Spells
import alfheim.common.core.handler.SpellEffectHandler
import alfheim.common.network.MessageParticles
import net.minecraft.entity.*
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.monster.IMob
import net.minecraft.entity.player.*

class SpellEcho: SpellBase("echo", EnumRace.POOKA, 4000, 1500, 5) {
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val result = checkCast(caster)
		if (result != SpellCastResult.OK) return result
		
		val list = caster.worldObj.getEntitiesWithinAABB(Entity::class.java, caster.boundingBox.expand(16.0, 16.0, 16.0)) as List<Entity>
		for (e in list) {
			if (Vector3.entityDistance(e, caster) > 16) continue
			when (e) {
				is EntityItem                   -> SpellEffectHandler.sendPacket(Spells.ECHO_ITEM, e)
				is IMob                     -> SpellEffectHandler.sendPacket(Spells.ECHO_MOB, e)
				is EntityPlayer         -> SpellEffectHandler.sendPacket(Spells.ECHO_PLAYER, e)
				is EntityLivingBase -> SpellEffectHandler.sendPacket(Spells.ECHO_ENTITY, e)
			}
		}
		
		if (caster is EntityPlayerMP) AlfheimCore.network.sendTo(MessageParticles(Spells.ECHO.ordinal, caster.posX, caster.posY, caster.posZ), caster)
		return result
	}
}