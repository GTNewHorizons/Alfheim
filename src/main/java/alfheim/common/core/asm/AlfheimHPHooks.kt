package alfheim.common.core.asm

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.handler.CardinalSystem.PartySystem
import gloomyfolken.hooklib.asm.*
import gloomyfolken.hooklib.asm.Hook.ReturnValue
import net.minecraft.entity.EntityLivingBase
import net.minecraft.util.MathHelper

object AlfheimHPHooks {
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS, injectOnExit = true, isMandatory = true)
	fun getHealth(e: EntityLivingBase, @ReturnValue hp: Float): Float {
		return if (AlfheimConfigHandler.enableMMO && e.activePotionsMap != null && e.isPotionActive(AlfheimConfigHandler.potionIDLeftFlame)) 0.123f else hp
	}
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS, injectOnExit = true, isMandatory = true)
	fun getMaxHealth(e: EntityLivingBase, @ReturnValue hp: Float): Float {
		return if (AlfheimConfigHandler.enableMMO && ASJUtilities.isServer) {
			val ret = if (e.activePotionsMap != null && e.isPotionActive(AlfheimConfigHandler.potionIDLeftFlame)) 0.123f else hp
			
			val pt = PartySystem.getMobParty(e)
			
			if (pt != null) {
				val i = pt.indexOf(e)
				if (i != -1) pt.setMaxHealth(i, ret)
			}
			
			ret
		} else hp
	}
	
	@JvmStatic
	@Hook(returnCondition = ReturnCondition.ALWAYS, injectOnExit = true, isMandatory = true)
	fun setHealth(e: EntityLivingBase, hp: Float) {
		if (!AlfheimConfigHandler.enableMMO) return
		
		val pt = PartySystem.getMobParty(e)
		var new = hp
		
		if (e.activePotionsMap != null && e.isPotionActive(AlfheimConfigHandler.potionIDLeftFlame)) {
			new = 0.01f
		}
		
		e.dataWatcher.updateObject(6, MathHelper.clamp_float(new, 0f, e.maxHealth))
		if (pt != null) {
			val i = pt.indexOf(e)
			if (i != -1) pt.setHealth(i, MathHelper.clamp_float(new, 0f, e.maxHealth))
		}
	}
}