package alfheim.common.potion

import alfheim.AlfheimCore
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.util.DamageSourceSpell
import net.minecraft.entity.EntityLivingBase

class PotionDeathMark: PotionAlfheim(AlfheimConfigHandler.potionIDDeathMark, "deathMark", true, 0x553355) {
	
	override fun isReady(time: Int, ampl: Int): Boolean {
		return time == 1
	}
	
	override fun performEffect(living: EntityLivingBase, ampl: Int) {
		if (AlfheimCore.enableMMO) living.attackEntityFrom(DamageSourceSpell.mark, Integer.MAX_VALUE.toFloat())
	}
}
