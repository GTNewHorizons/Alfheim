package alfheim.common.core.asm

import alfheim.AlfheimCore
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.handler.CardinalSystem.PartySystem
import gloomyfolken.hooklib.asm.Hook
import gloomyfolken.hooklib.asm.Hook.ReturnValue
import net.minecraft.entity.*
import net.minecraft.util.*

object AlfheimHPHooks {
	
	@JvmStatic
	@Hook(injectOnExit = true, isMandatory = true)
	fun getHealth(e: EntityLivingBase, @ReturnValue hp: Float): Float {
		return if (AlfheimCore.enableMMO && e.activePotionsMap != null) {
			when {
				e.isPotionActive(AlfheimConfigHandler.potionIDLeftFlame) -> 0.01f
				
				e.isPotionActive(AlfheimConfigHandler.potionIDSharedHP)  -> {
					val pt = PartySystem.getMobParty(e) ?: return hp
					
					fun h(e: EntityLivingBase?) = e?.dataWatcher?.getWatchableObjectFloat(6) ?: 0f
					
					h(Array(pt.count) { pt[it] }.maxBy { h(it) })
				}
				
				else -> hp
			}
		} else hp
	}
	
	@JvmStatic
	@Hook(injectOnExit = true, isMandatory = true)
	fun getMaxHealth(e: EntityLivingBase, @ReturnValue hp: Float): Float {
		return if (AlfheimCore.enableMMO && e.activePotionsMap != null) {
			when {
				e.isPotionActive(AlfheimConfigHandler.potionIDLeftFlame) -> 0f
				
				e.isPotionActive(AlfheimConfigHandler.potionIDSharedHP)  -> {
					val pt = PartySystem.getMobParty(e) ?: return hp
					
					fun mh(e: EntityLivingBase?) = e?.getEntityAttribute(SharedMonsterAttributes.maxHealth)?.attributeValue	?: 0.0
					fun dh(e: EntityLivingBase?) = e?.getEntityAttribute(SharedMonsterAttributes.maxHealth)?.baseValue		?: 0.0
					
					Array(pt.count) { pt[it] }.sumByDouble { mh(it) - dh(it) }.toFloat()
				}
				
				else -> hp
			}
		} else hp
	}
	
	@JvmStatic
	@Hook(injectOnExit = true, isMandatory = true)
	fun setHealth(e: EntityLivingBase, hp: Float) {
		var new = hp
		if (!AlfheimCore.enableMMO)
		// e.getDataWatcher().updateObject(6, Float.valueOf(MathHelper.clamp_float(hp, 0.0F, e.getMaxHealth())));
			return
		
		if (e.activePotionsMap != null && e.isPotionActive(AlfheimConfigHandler.potionIDLeftFlame)) {
			new = 0.01f
			
			if (!e.isPotionActive(AlfheimConfigHandler.potionIDSharedHP)) {
				e.dataWatcher.updateObject(6, MathHelper.clamp_float(new, 0.0f, e.maxHealth))
				return
			}
		}
		
		val pt = PartySystem.getMobParty(e)
		if (pt == null) {
			e.dataWatcher.updateObject(6, MathHelper.clamp_float(new, 0.0f, e.maxHealth))
			return
		}
		
		val mr = Array(pt.count) { pt[it] }
		
		for (entityLivingBase in mr) {
			if (entityLivingBase != null) {
				entityLivingBase.dataWatcher.updateObject(6, MathHelper.clamp_float(new, 0.0f, entityLivingBase.maxHealth))
				if (new < 0.0f) entityLivingBase.onDeath(DamageSource.outOfWorld)
			}
		}
	}
}
