package alfheim.common.event;

import alexsocol.asjlib.ASJUtilities;
import alfheim.common.registry.AlfheimRegistry;
import alfheim.common.utils.AlfheimConfig;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class CommonEventHandler {
	
	@SubscribeEvent
//	@SideOnly(Side.SERVER)
	public void onPlayerJoined(PlayerLoggedInEvent event) {
		if (Loader.isModLoaded("elvenstory") && !MinecraftServer.getServer().getConfigurationManager().func_152602_a(event.player).hasAchievementUnlocked(AlfheimRegistry.alfheim) && event.player.dimension == 0) {
			ASJUtilities.sendToDimensionWithoutPortal(event.player, AlfheimConfig.dimensionIDAlfheim, 0.5, 75, 0.5);
			event.player.triggerAchievement(AlfheimRegistry.alfheim);
		}
	}
	
	@SubscribeEvent
	public void onEntityUpdate(LivingUpdateEvent event) {
		
	}
}
