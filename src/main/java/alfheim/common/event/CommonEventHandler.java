package alfheim.common.event;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.Constants;
import alfheim.ToDoList;
import alfheim.common.entity.EntityAlfheimPixie;
import alfheim.common.entity.EnumRace;
import alfheim.common.registry.AlfheimAchievements;
import alfheim.common.utils.AlfheimConfig;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.StatisticsFile;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class CommonEventHandler {
	
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent e) {
		e.player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.YELLOW.UNDERLINE + ">>> Задачи на реализацию <<<"));
		for (String task : ToDoList.tasks) e.player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.BOLD + "> " + EnumChatFormatting.RESET.YELLOW + task));
		
		if (AlfheimCore.enableElvenStory) {
			if (e.player instanceof EntityPlayerMP) {
				StatisticsFile stats = ((EntityPlayerMP) e.player).func_147099_x();
				if (!stats.hasAchievementUnlocked(AlfheimAchievements.alfheim) && e.player.dimension != AlfheimConfig.dimensionIDAlfheim) {
					ASJUtilities.sendToDimensionWithoutPortal(e.player, AlfheimConfig.dimensionIDAlfheim, 0.5, 253, 0.5);
					e.player.triggerAchievement(AlfheimAchievements.alfheim);
					e.player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.UNDERLINE.GREEN + StatCollector.translateToLocal("elvenstory.welcome0")));
					e.player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GOLD + StatCollector.translateToLocal("elvenstory.welcome1")));
					e.player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("elvenstory.welcome2")));
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing e) {
		if (AlfheimCore.enableElvenStory) if (e.entity instanceof EntityPlayer) {
			((EntityPlayer) e.entity).getAttributeMap().registerAttribute(Constants.RACE);
			((EntityPlayer) e.entity).getAttributeMap().registerAttribute(Constants.FLIGHT);
		}
	}
	
	@SubscribeEvent
	public void onClonePlayer(PlayerEvent.Clone e) {
		if (!AlfheimCore.enableElvenStory) return;
		EnumRace r = EnumRace.fromDouble(((EntityPlayer) e.original).getEntityAttribute(Constants.RACE).getAttributeValue());
		((EntityPlayer) e.entityPlayer).getEntityAttribute(Constants.RACE).setBaseValue(r.ordinal());
	}
	
	@SubscribeEvent
	public void onEntityAttacked(LivingAttackEvent e) {
		if (e.entityLiving instanceof EntityAlfheimPixie && e.source.getDamageType().equals(DamageSource.inWall.getDamageType())) e.setCanceled(true);
	}
	
	@SubscribeEvent
	public void onEntityUpdate(LivingUpdateEvent e) {
		if (e.entityLiving instanceof EntityPlayer) onPlayerUpdate(e);
	}

	private void onPlayerUpdate(LivingUpdateEvent e) {
		EntityPlayer player = (EntityPlayer) e.entityLiving;
		if (!player.capabilities.isCreativeMode) {
			if (AlfheimCore.enableElvenStory) {
				if (player.getAttributeMap().getAttributeInstance(Constants.FLIGHT).getAttributeValue() >= 0
				&&	player.getAttributeMap().getAttributeInstance(Constants.FLIGHT).getAttributeValue() <= Constants.FLIGHT.getDefaultValue()) {
					if (player.capabilities.isFlying) {
														player.getAttributeMap().getAttributeInstance(Constants.FLIGHT)
														.setBaseValue(player.getAttributeMap().getAttributeInstance(Constants.FLIGHT).getAttributeValue() -
														(player.isSprinting() ? 4 : (player.motionX != 0.0 || player.motionY > 0.0 || player.motionZ != 0.0) ? 2 : 1));
					if (player.isSprinting()) player.moveFlying(0F, 1F, 0.01F);
					} else								player.getAttributeMap().getAttributeInstance(Constants.FLIGHT)
														.setBaseValue(player.getAttributeMap().getAttributeInstance(Constants.FLIGHT).getAttributeValue() + 
														(player.getAttributeMap().getAttributeInstance(Constants.FLIGHT).getAttributeValue() < Constants.FLIGHT.getDefaultValue() ? 1 : 0));
					
				}
				if (player.getAttributeMap().getAttributeInstance(Constants.FLIGHT).getAttributeValue() <= 0)	player.capabilities.isFlying = false; 
			}
		}
	}
}
