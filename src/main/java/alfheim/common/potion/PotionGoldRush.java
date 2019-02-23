package alfheim.common.potion;

import alfheim.AlfheimCore;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.core.util.AlfheimConfig;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;

public class PotionGoldRush extends PotionAlfheim {

	public PotionGoldRush() {
		super(AlfheimConfig.potionIDGoldRush, "goldRush", false, 0x55FF00);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onBreakSpeed(BreakSpeed e) {
		if (AlfheimCore.enableMMO && e.entityLiving.isPotionActive(AlfheimRegistry.goldRush)) e.newSpeed *= 2;
	}
}
