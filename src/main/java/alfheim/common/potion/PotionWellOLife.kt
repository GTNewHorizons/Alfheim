package alfheim.common.potion

import alfheim.AlfheimCore
import alfheim.common.core.handler.AlfheimConfigHandler
import net.minecraft.entity.EntityLivingBase

class PotionWellOLife: PotionAlfheim(AlfheimConfigHandler.potionIDWellOLife, "wellolife", false, 0x00FFFF) {
	
	override fun isReady(time: Int, ampl: Int): Boolean {
		return AlfheimCore.enableMMO && time % 10 == 0
	}
	
	override fun performEffect(living: EntityLivingBase, ampl: Int) {
		if (!AlfheimCore.enableMMO) return
		if (living.isInWater) living.heal(0.5f)
	}
}
