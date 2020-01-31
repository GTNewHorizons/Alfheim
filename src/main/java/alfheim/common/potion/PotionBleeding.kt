package alfheim.common.potion

import alfheim.AlfheimCore
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.util.DamageSourceSpell
import net.minecraft.entity.EntityLivingBase
import kotlin.math.max

class PotionBleeding: PotionAlfheim(AlfheimConfigHandler.potionIDBleeding, "bleeding", true, 0xFF0000) {
	
	override fun isReady(time: Int, ampl: Int) = time % (20 / max(1, ampl)) == 0
	
	override fun performEffect(living: EntityLivingBase, ampl: Int) {
		if (AlfheimCore.enableMMO) living.attackEntityFrom(DamageSourceSpell.bleeding, (ampl + 1).toFloat())
	}
}
