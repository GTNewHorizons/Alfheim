package alfheim.common.core.asm;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

public class AlfheimSyntheticMethods {
	
	public static void onFinishedPotionEffect(EntityLivingBase e, PotionEffect pe) {
		// e.onFinishedPotionEffect(pe);
	}
		
	public static void onChangedPotionEffect(EntityLivingBase e, PotionEffect pe, boolean isNew) {
		// e.onChangedPotionEffect(pe, isNew);
	}
	
	public static void onDeathPost(EntityLivingBase e, DamageSource ds) {
		// e.onDeathPost(ds)
	}
}
