package alexsocol.patcher.asm;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;

public class ASJSyntheticMethods {
	
	public static void onFinishedPotionEffect(EntityLivingBase e, PotionEffect pe) {
		// e.onFinishedPotionEffect(pe);
	}
		
	public static void onChangedPotionEffect(EntityLivingBase e, PotionEffect pe, boolean isNew) {
		// e.onChangedPotionEffect(pe, isNew);
	}
}
