package alfheim.common.potion

import alfheim.AlfheimCore
import alfheim.common.core.util.*
import net.minecraft.entity.EntityLivingBase

class PotionBleeding: PotionAlfheim(AlfheimConfig.potionIDBleeding, "bleeding", true, 0xFF0000) {
	
	override fun isReady(time: Int, ampl: Int): Boolean {
		return time % (20 / Math.max(1, ampl)) == 0
	}
	
	override fun performEffect(living: EntityLivingBase, ampl: Int) {
		if (AlfheimCore.enableMMO) living.attackEntityFrom(DamageSourceSpell.bleeding, (ampl + 1).toFloat())
	}
}
