package alfheim.common.event;

import org.lwjgl.util.vector.Vector3f;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.Constants;
import alfheim.common.entity.EntityAlfheimPixie;
import alfheim.common.entity.EnumRace;
import alfheim.common.registry.AlfheimAchievements;
import alfheim.common.registry.AlfheimItems;
import alfheim.common.utils.AlfheimConfig;
import baubles.api.BaublesApi;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatisticsFile;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.helper.Vector3;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara;

public class CommonEventHandler {
	
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent e) {
		/*if (Constants.DEV) {
			e.player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.YELLOW.UNDERLINE + ">>> Задачи на реализацию <<<"));
			for (String task : ToDoList.tasks) e.player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.BOLD + "> " + EnumChatFormatting.RESET.YELLOW + task));
		}*/
		
		if (AlfheimCore.enableElvenStory) {
			if (e.player instanceof EntityPlayerMP) {
				StatisticsFile stats = ((EntityPlayerMP) e.player).func_147099_x();
				if (!stats.hasAchievementUnlocked(AlfheimAchievements.alfheim) && e.player.dimension != AlfheimConfig.dimensionIDAlfheim) {
					ASJUtilities.sendToDimensionWithoutPortal(e.player, AlfheimConfig.dimensionIDAlfheim, 0.5, 253, 0.5);
					e.player.triggerAchievement(AlfheimAchievements.alfheim);
					e.player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.UNDERLINE.GREEN + StatCollector.translateToLocal("elvenstory.welcome0")));
					e.player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.GOLD + StatCollector.translateToLocal("elvenstory.welcome1")));
					e.player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocal("elvenstory.welcome2")));
					e.player.inventory.addItemStackToInventory(new ItemStack(ModItems.lexicon));
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
		EnumRace r = EnumRace.fromID(((EntityPlayer) e.original).getEntityAttribute(Constants.RACE).getAttributeValue());
		((EntityPlayer) e.entityPlayer).getEntityAttribute(Constants.RACE).setBaseValue(r.ordinal());
	}
	
	@SubscribeEvent
	public void onEntityAttacked(LivingAttackEvent e) {
		if (e.entityLiving instanceof EntityAlfheimPixie && e.source.getDamageType().equals(DamageSource.inWall.getDamageType())) e.setCanceled(true);
		if (e.source.isFireDamage() && e.entityLiving instanceof EntityPlayer && ((EntityPlayer)e.entityLiving).getCurrentArmor(1) != null && ((EntityPlayer)e.entityLiving).getCurrentArmor(1).getItem() == AlfheimItems.elementalLeggings && ManaItemHandler.requestManaExact(((EntityPlayer)e.entityLiving).getCurrentArmor(1), ((EntityPlayer)e.entityLiving), MathHelper.ceiling_float_int(10 * e.ammount), !e.entityLiving.worldObj.isRemote)) e.setCanceled(true);
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
					double x = player.posX - 0.25;
					double y = player.posY - 0.5;
					double z = player.posZ - 0.25;
					for(int i = 0; i < 2; i++) Botania.proxy.sparkleFX(player.worldObj, x + Math.random() * player.width, y + Math.random() * 0.4, z + Math.random() * player.width, 1, 1, 1, 2F * (float) Math.random(), 20);
					
					} else								player.getAttributeMap().getAttributeInstance(Constants.FLIGHT)
														.setBaseValue(player.getAttributeMap().getAttributeInstance(Constants.FLIGHT).getAttributeValue() + 
														(player.getAttributeMap().getAttributeInstance(Constants.FLIGHT).getAttributeValue() < Constants.FLIGHT.getDefaultValue() ? 1 : 0));
					
				}
				if (player.getAttributeMap().getAttributeInstance(Constants.FLIGHT).getAttributeValue() <= 0)	player.capabilities.isFlying = false; 
			}
		}
	}
}
