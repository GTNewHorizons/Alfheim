package alfheim.common.spell.sound

import alexsocol.asjlib.expand
import alexsocol.asjlib.math.Vector3
import alexsocol.asjlib.security.InteractionSecurity
import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace
import alfheim.api.spell.SpellBase
import alfheim.client.render.world.VisualEffectHandlerClient.VisualEffects
import alfheim.common.core.handler.VisualEffectHandler
import alfheim.common.network.MessageVisualEffect
import net.minecraft.entity.*
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.monster.IMob
import net.minecraft.entity.player.*

object SpellEcho: SpellBase("echo", EnumRace.POOKA, 4000, 1500, 5) {
	
	override val usableParams: Array<Any>
		get() = arrayOf(radius)
	
	override fun performCast(caster: EntityLivingBase): SpellCastResult {
		val result = checkCast(caster)
		if (result != SpellCastResult.OK) return result
		
		val list = caster.worldObj.getEntitiesWithinAABB(Entity::class.java, caster.boundingBox.expand(radius)) as List<Entity>
		for (e in list) {
			if (Vector3.entityDistance(e, caster) > radius * 2) continue
			
			if (!InteractionSecurity.canDoSomethingWithEntity(caster, e)) continue
			
			when (e) {
				is EntityItem       -> VisualEffectHandler.sendPacket(VisualEffects.ECHO_ITEM, e)
				is IMob             -> VisualEffectHandler.sendPacket(VisualEffects.ECHO_MOB, e)
				is EntityPlayer     -> VisualEffectHandler.sendPacket(VisualEffects.ECHO_PLAYER, e)
				is EntityLivingBase -> VisualEffectHandler.sendPacket(VisualEffects.ECHO_ENTITY, e)
			}
		}
		
		if (caster is EntityPlayerMP) AlfheimCore.network.sendTo(MessageVisualEffect(VisualEffects.ECHO.ordinal, caster.posX, caster.posY, caster.posZ), caster)
		return result
	}
}