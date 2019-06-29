package alfheim.common.core.asm

import net.minecraft.entity.EntityLivingBase
import net.minecraft.potion.PotionEffect

@Suppress("UNUSED_PARAMETER")
class AlfheimSyntheticMethods {
	
	companion object {
		@JvmStatic
		fun onFinishedPotionEffect(e: EntityLivingBase, pe: PotionEffect) {
			// e.onFinishedPotionEffect(pe);
		}
		
		@JvmStatic
		fun onChangedPotionEffect(e: EntityLivingBase, pe: PotionEffect, isNew: Boolean) {
			// e.onChangedPotionEffect(pe, isNew);
		}
	}
}
