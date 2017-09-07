package alfheim.common.event;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.ToDoList;
import alfheim.common.registry.AlfheimAchievements;
import alfheim.common.registry.AlfheimRegistry;
import alfheim.common.utils.AlfheimConfig;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.StatisticsFile;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class CommonEventHandler {
	
	@SubscribeEvent
	public void onPlayerJoined(PlayerLoggedInEvent event) {
		event.player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.YELLOW.UNDERLINE + ">>> Задачи на реализацию <<<"));
		for (String task : ToDoList.tasks) {
			event.player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.BOLD + "> " + EnumChatFormatting.RESET.YELLOW + task));
		}
		if (event.player instanceof EntityPlayerMP) {
			StatisticsFile stats = ((EntityPlayerMP)event.player).func_147099_x();
			if (AlfheimCore.enableElvenStory && !stats.hasAchievementUnlocked(AlfheimAchievements.alfheim) && event.player.dimension == 0) {
				ASJUtilities.sendToDimensionWithoutPortal(event.player, AlfheimConfig.dimensionIDAlfheim, 0.5, 75, 0.5);
				event.player.triggerAchievement(AlfheimAchievements.alfheim);
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityUpdate(LivingUpdateEvent event) {
		
	}
}
