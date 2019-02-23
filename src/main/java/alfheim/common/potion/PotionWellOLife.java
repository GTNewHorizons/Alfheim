package alfheim.common.potion;

import alfheim.AlfheimCore;
import alfheim.common.core.util.AlfheimConfig;
import net.minecraft.entity.EntityLivingBase;

public class PotionWellOLife extends PotionAlfheim {

	public PotionWellOLife() {
		super(AlfheimConfig.potionIDWellOLife, "wellolife", false, 0x00FFFF);
	}
	
	public boolean isReady(int time, int ampl) {
		return AlfheimCore.enableMMO && time % 10 == 0;
	}

	public void performEffect(EntityLivingBase living, int ampl) {
		if (!AlfheimCore.enableMMO) return;
		if (living.isInWater()) living.heal(0.5F);
	}
}
