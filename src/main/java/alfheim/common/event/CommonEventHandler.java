package alfheim.common.event;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.Constants;
import alfheim.ToDoList;
import alfheim.common.registry.AlfheimAchievements;
import alfheim.common.utils.AlfheimConfig;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.StatisticsFile;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class CommonEventHandler {
	
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent e) {
		e.player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.YELLOW.UNDERLINE + ">>> Задачи на реализацию <<<"));
		for (String task : ToDoList.tasks) e.player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.BOLD + "> " + EnumChatFormatting.RESET.YELLOW + task));
		
		// Elven Story stuff
		if (AlfheimCore.enableElvenStory) {
			if (e.player instanceof EntityPlayerMP) {
				StatisticsFile stats = ((EntityPlayerMP) e.player).func_147099_x();
				if (!stats.hasAchievementUnlocked(AlfheimAchievements.alfheim) && e.player.dimension != AlfheimConfig.dimensionIDAlfheim) {
					ASJUtilities.sendToDimensionWithoutPortal(e.player, AlfheimConfig.dimensionIDAlfheim, 0.5, 253, 0.5);
					e.player.triggerAchievement(AlfheimAchievements.alfheim);
					e.player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GREEN.UNDERLINE + StatCollector.translateToLocal("elvenstory.welcome0") + '\n' + EnumChatFormatting.GOLD + StatCollector.translateToLocal("elvenstory.welcome1") + '\n' + StatCollector.translateToLocal("elvenstory.welcome2")));
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing e) {
		if (e.entity instanceof EntityPlayer) {
			// TODO Make RACE saving and loading from XML
			((EntityPlayer) e.entity).getAttributeMap().registerAttribute(Constants.RACE);
			((EntityPlayer) e.entity).getAttributeMap().registerAttribute(Constants.FLIGHT);
		}
	}
	
	@SubscribeEvent
	public void onEntityUpdate(LivingUpdateEvent e) {
		if (e.entityLiving instanceof EntityPlayer) onPlayerUpdate(e);
	}

	private void onPlayerUpdate(LivingUpdateEvent e) {
		EntityPlayer player = (EntityPlayer) e.entityLiving;
		if (!player.capabilities.isCreativeMode) {
			if (player.getAttributeMap().getAttributeInstance(Constants.FLIGHT).getAttributeValue() >= 0
			&&	player.getAttributeMap().getAttributeInstance(Constants.FLIGHT).getAttributeValue() <= Constants.FLIGHT.getDefaultValue()) {
				if (player.capabilities.isFlying)	player.getAttributeMap().getAttributeInstance(Constants.FLIGHT)
													.setBaseValue(player.getAttributeMap().getAttributeInstance(Constants.FLIGHT).getAttributeValue() - 1);
				else								player.getAttributeMap().getAttributeInstance(Constants.FLIGHT)
													.setBaseValue(player.getAttributeMap().getAttributeInstance(Constants.FLIGHT).getAttributeValue() + 
													(player.getAttributeMap().getAttributeInstance(Constants.FLIGHT).getAttributeValue() < Constants.FLIGHT.getDefaultValue() ? 1 : 0));
				
			}
			if (player.getAttributeMap().getAttributeInstance(Constants.FLIGHT).getAttributeValue() <= 0)	player.capabilities.isFlying = false; 
		}
	}
}
