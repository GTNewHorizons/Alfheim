package alfheim.common.potion;

import alfheim.AlfheimCore;
import alfheim.common.core.util.AlfheimConfig;
import alfheim.common.core.util.DamageSourceSpell;
import net.minecraft.entity.EntityLivingBase;

public class PotionDeathMark extends PotionAlfheim {

	public PotionDeathMark() {
		super(AlfheimConfig.potionIDDeathMark, "deathMark", true, 0x553355);
	}

	public boolean isReady(int time, int ampl) {
		return time == 1; 
	}

	public void performEffect(EntityLivingBase living, int ampl) {
		if (AlfheimCore.enableMMO) living.attackEntityFrom(DamageSourceSpell.mark, Integer.MAX_VALUE);
	}
}
