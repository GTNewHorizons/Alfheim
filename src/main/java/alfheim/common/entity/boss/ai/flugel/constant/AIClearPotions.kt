package alfheim.common.entity.boss.ai.flugel.constant

import alfheim.common.entity.boss.EntityFlugel
import alfheim.common.entity.boss.ai.flugel.AIConstantExecutable
import net.minecraft.potion.*

@Suppress("UNCHECKED_CAST")
class AIClearPotions(flugel: EntityFlugel): AIConstantExecutable(flugel) {
	
	override fun execute() {
		flugel.playersAround.forEach { player ->
			(player.activePotionEffects as MutableCollection<PotionEffect>).removeIf { potion ->
				(potion.getDuration() < 200 && potion.getIsAmbient() && !Potion.potionTypes[potion.getPotionID()].isBadEffect)
			}
		}
	}
}