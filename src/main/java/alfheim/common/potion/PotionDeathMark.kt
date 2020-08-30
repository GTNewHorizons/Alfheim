package alfheim.common.potion

import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.util.DamageSourceSpell
import alfheim.common.spell.darkness.SpellDeathMark
import net.minecraft.entity.EntityLivingBase

class PotionDeathMark: PotionAlfheim(AlfheimConfigHandler.potionIDDeathMark, "deathMark", true, 0x553355) {
	
	override fun isReady(time: Int, ampl: Int) = time == 1
	
	override fun performEffect(living: EntityLivingBase, ampl: Int) {
		if (AlfheimConfigHandler.enableMMO) living.attackEntityFrom(DamageSourceSpell.mark, SpellDeathMark.damage)
	}
}
