package alfheim.common.potion

import alfheim.common.core.handler.AlfheimConfigHandler
import net.minecraft.entity.EntityLivingBase

class PotionShield: PotionAlfheim(AlfheimConfigHandler.potionIDShield, "ForceShield", false, 0x6666FF) {
	
	override fun isReady(tick: Int, mod: Int) = true
	
	override fun performEffect(target: EntityLivingBase?, mod: Int) {
	
	}
}
