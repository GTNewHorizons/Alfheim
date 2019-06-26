package alfheim.common.core.asm;

import alfheim.AlfheimCore;
import alfheim.common.core.handler.CardinalSystem.PartySystem;
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party;
import alfheim.common.core.registry.AlfheimRegistry;
import gloomyfolken.hooklib.asm.Hook;
import gloomyfolken.hooklib.asm.Hook.ReturnValue;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;

public class AlfheimHPHooks {

	@Hook(injectOnExit = true, isMandatory = true)
	public static float getHealth(EntityLivingBase e, @ReturnValue float hp) {
		if (AlfheimCore.enableMMO && AlfheimRegistry.leftFlame != null && e.activePotionsMap != null && e.isPotionActive(AlfheimRegistry.leftFlame)) return 0.000000000000000000000000000000000000000000001F;
		else return hp;
	}
	
	@Hook(injectOnExit = true, isMandatory = true)
	public static float getMaxHealth(EntityLivingBase e, @ReturnValue float hp) {
		if (AlfheimCore.enableMMO && AlfheimRegistry.leftFlame != null && e.activePotionsMap != null && e.isPotionActive(AlfheimRegistry.leftFlame)) return 0.0F;
		else return hp;
	}
	
	@Hook(injectOnExit = true, isMandatory = true)
	public static void setHealth(EntityLivingBase e, float hp) {
		if (!AlfheimCore.enableMMO) 
			// e.getDataWatcher().updateObject(6, Float.valueOf(MathHelper.clamp_float(hp, 0.0F, e.getMaxHealth())));
			return;
		
		
		boolean flame;
		if (flame = (AlfheimRegistry.leftFlame != null && e.activePotionsMap != null && e.isPotionActive(AlfheimRegistry.leftFlame)))
			hp = 0.000000000000000000000000000000000000000000001F;
		
		if (AlfheimRegistry.sharedHP != null && e.activePotionsMap != null && !e.isPotionActive(AlfheimRegistry.sharedHP)) {
			if (flame) e.getDataWatcher().updateObject(6, MathHelper.clamp_float(hp, 0.0F, e.getMaxHealth()));
			return;
		}
		
		Party pt = PartySystem.getMobParty(e);
		if (pt == null) {
			if (flame) e.getDataWatcher().updateObject(6, MathHelper.clamp_float(hp, 0.0F, e.getMaxHealth()));
			return;
		}
		
		EntityLivingBase[] mr = new EntityLivingBase[pt.count];
		for (int i = 0; i < pt.count; i++) mr[i] = pt.get(i);
		
		for (EntityLivingBase entityLivingBase : mr) {
			if (entityLivingBase != null) {
				entityLivingBase.getDataWatcher().updateObject(6, MathHelper.clamp_float(hp, 0.0F, entityLivingBase.getMaxHealth()));
				if (hp < 0.0F) entityLivingBase.onDeath(DamageSource.outOfWorld);
			}
		}
	}
}
