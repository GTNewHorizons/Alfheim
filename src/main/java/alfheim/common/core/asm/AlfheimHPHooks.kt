package alfheim.common.core.asm

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.client.core.handler.CardinalSystemClient
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
		if (ASJUtilities.isServer) {
			return if (AlfheimCore.enableMMO && e.activePotionsMap != null) {
				when {
					e.isPotionActive(AlfheimConfigHandler.potionIDLeftFlame) -> 0.01f
					
					e.isPotionActive(AlfheimConfigHandler.potionIDSharedHP)  -> {
						val pt = PartySystem.getMobParty(e) ?: return hp
						
						fun h(e: EntityLivingBase?) = e?.dataWatcher?.getWatchableObjectFloat(6) ?: 0f
						
						h(Array(pt.count) { pt[it] }.filter { it?.isPotionActive(AlfheimConfigHandler.potionIDSharedHP) == true }.maxBy { h(it) })
					}
					
					else -> hp
				}
			} else hp
		}
		
		// client
		val index = CardinalSystemClient.PlayerSegmentClient.party?.indexOf(e) ?: return hp
		return if (index != -1) {
			CardinalSystemClient.PlayerSegmentClient.party?.getHealth(index) ?: hp
		} else hp
	}
	
	@JvmStatic
	@Hook(injectOnExit = true, isMandatory = true)
	fun getMaxHealth(e: EntityLivingBase, @ReturnValue hp: Float): Float {
		if (ASJUtilities.isServer) {
			val pt = PartySystem.getMobParty(e)
			
			val ret = if (AlfheimCore.enableMMO && e.activePotionsMap != null) {
				when {
					e.isPotionActive(AlfheimConfigHandler.potionIDLeftFlame) -> 0f
					
					e.isPotionActive(AlfheimConfigHandler.potionIDSharedHP)  -> {
						if (pt == null) return hp
						
						fun mh(e: EntityLivingBase?) = e?.getEntityAttribute(SharedMonsterAttributes.maxHealth)?.attributeValue ?: 0.0
						
						Array(pt.count) { pt[it] }.filter { it?.isPotionActive(AlfheimConfigHandler.potionIDSharedHP) == true }.sumByDouble { mh(it) }.toFloat()
					}
					
					else  -> hp
				}
			} else hp
			
			if (pt != null) {
				val i = pt.indexOf(e)
				if (i != -1) pt.setMaxHealth(i, ret)
			}
			
			return ret
		}
		
		// client
		val i = CardinalSystemClient.PlayerSegmentClient.party?.indexOf(e) ?: return hp
		return if (i != -1) {
			CardinalSystemClient.PlayerSegmentClient.party?.getMaxHealth(i) ?: hp
		} else hp
	}
	
	@JvmStatic
	@Hook(injectOnExit = true, isMandatory = true)
	fun setHealth(e: EntityLivingBase, hp: Float) {
		if (!AlfheimCore.enableMMO) return
		
		if (!ASJUtilities.isServer) {
			/*if (CardinalSystemClient.PlayerSegmentClient.party != null) {
				val i = CardinalSystemClient.PlayerSegmentClient.party!!.indexOf(e)
				if (i != -1) {
					CardinalSystemClient.PlayerSegmentClient.party!!.setHealth(i, hp)
				}
			}*/
			return
		}
		
		val pt = PartySystem.getMobParty(e)
		var new = hp
		
		if (e.activePotionsMap != null) {
			if (e.isPotionActive(AlfheimConfigHandler.potionIDLeftFlame)) {
				new = 0.01f
			}
			
			if (!e.isPotionActive(AlfheimConfigHandler.potionIDSharedHP)) {
				e.dataWatcher.updateObject(6, MathHelper.clamp_float(new, 0.0f, e.maxHealthHook))
				if (pt != null) {
					val i = pt.indexOf(e)
					if (i != -1) pt.setHealth(i, MathHelper.clamp_float(new, 0.0f, e.maxHealthHook))
				}
				return
			}
		}
		
		if (pt == null) {
			e.dataWatcher.updateObject(6, MathHelper.clamp_float(new, 0.0f, e.maxHealthHook))
			return
		}
		
		for (i in 0 until pt.count) {
			val mr = pt[i] ?: continue
			if (mr.isPotionActive(AlfheimConfigHandler.potionIDSharedHP)) {
				mr.dataWatcher.updateObject(6, MathHelper.clamp_float(new, 0.0f, mr.maxHealthHook))
				pt.setHealth(i, MathHelper.clamp_float(new, 0.0f, mr.maxHealthHook))
				if (new < 0.0f) mr.onDeath(DamageSource.outOfWorld)
			}
		}
	}
}

val EntityLivingBase.healthHook
	get(): Float {
		val res = dataWatcher.getWatchableObjectFloat(6)
		return if (AlfheimHookLoader.hpSpells) AlfheimHPHooks.getHealth(this, res) else res
	}

val EntityLivingBase.maxHealthHook
	get(): Float {
		val res = getEntityAttribute(SharedMonsterAttributes.maxHealth).attributeValue.toFloat()
		return if (AlfheimHookLoader.hpSpells) AlfheimHPHooks.getMaxHealth(this, res) else res
	}