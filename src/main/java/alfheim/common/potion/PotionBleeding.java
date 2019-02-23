package alfheim.common.potion;

import alfheim.AlfheimCore;
import alfheim.common.core.util.AlfheimConfig;
import alfheim.common.core.util.DamageSourceSpell;
import net.minecraft.entity.EntityLivingBase;

public class PotionBleeding extends PotionAlfheim {

	public PotionBleeding() {
		super(AlfheimConfig.potionIDBleeding, "bleeding", true, 0xFF0000);
	}

	public boolean isReady(int time, int ampl) {
		return time % (20/Math.max(1, ampl)) == 0; 
	}

	public void performEffect(EntityLivingBase living, int ampl) {
		if (AlfheimCore.enableMMO) living.attackEntityFrom(DamageSourceSpell.bleeding, ampl+1);
	}
}
