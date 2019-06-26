package alfheim.common.core.asm

import net.minecraft.entity.EntityLivingBase
import net.minecraft.potion.PotionEffect

object AlfheimSyntheticMethods {
	
	fun onFinishedPotionEffect(e: EntityLivingBase, pe: PotionEffect) {
		// e.onFinishedPotionEffect(pe);
	}
	
	fun onChangedPotionEffect(e: EntityLivingBase, pe: PotionEffect, isNew: Boolean) {
		// e.onChangedPotionEffect(pe, isNew);
	}
}
