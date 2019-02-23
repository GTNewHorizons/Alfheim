package alfheim.common.potion;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.client.render.world.SpellEffectHandlerClient;
import alfheim.common.core.util.AlfheimConfig;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;

public class PotionShowMana extends PotionAlfheim {

	public PotionShowMana() {
		super(AlfheimConfig.potionIDShowMana, "showMana", false, 0x0000DD);
	}

	public boolean isReady(int time, int ampl) {
		return true;
	}
	
	public void performEffect(EntityLivingBase living, int ampl) {
		if (!AlfheimCore.enableMMO) return;
		PotionEffect pe = living.getActivePotionEffect(this);
		if (pe == null) return;
		
		if (ASJUtilities.isServer() || pe.amplifier <= 0) {
			pe.duration = 1;
			return;
		} else {
			if (pe.duration < Integer.MAX_VALUE) ++pe.duration;
			--pe.amplifier;
		}
		
		if (!ASJUtilities.isServer()) 
			for (int i = 0; i < Math.sqrt(Math.sqrt(Math.sqrt(pe.duration))); i++) // looks like this "i < VALUE" is fine
				SpellEffectHandlerClient.spawnMana(living, i);
	}
}
