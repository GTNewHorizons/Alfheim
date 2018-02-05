package alfheim.common.event;

import com.sun.xml.internal.bind.v2.TODO;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.api.AlfheimAPI;
import alfheim.api.event.NetherPortalActivationEvent;
import alfheim.common.core.registry.AlfheimAchievements;
import alfheim.common.core.registry.AlfheimItems;
import alfheim.common.core.utils.AlfheimConfig;
import alfheim.common.entity.EntityAlfheimPixie;
import alfheim.common.entity.EnumRace;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.api.recipe.ElvenPortalUpdateEvent;
import vazkii.botania.common.block.tile.TileAlfPortal;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

public class CommonEventHandler {
	
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent e) {
		/*if (Constants.DEV) {
			e.player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.YELLOW.UNDERLINE + ">>> ������ �� ���������� <<<"));
			for (String task : ToDoList.tasks) e.player.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.BOLD + "> " + EnumChatFormatting.RESET.YELLOW + task));
		}*/
		
		if (AlfheimCore.enableElvenStory) {
			if (e.player instanceof EntityPlayerMP) {
				if (!((EntityPlayerMP) e.player).func_147099_x().hasAchievementUnlocked(AlfheimAchievements.alfheim) && e.player.dimension != AlfheimConfig.dimensionIDAlfheim) {
					ASJUtilities.sendToDimensionWithoutPortal(e.player, AlfheimConfig.dimensionIDAlfheim, 0.5, 253, 0.5);
					e.player.triggerAchievement(AlfheimAchievements.alfheim);
					e.player.addChatComponentMessage(new ChatComponentTranslation("elvenstory.welcome0"));
					e.player.addChatComponentMessage(new ChatComponentTranslation("elvenstory.welcome1"));
					e.player.addChatComponentMessage(new ChatComponentTranslation("elvenstory.welcome2"));
					e.player.inventory.addItemStackToInventory(new ItemStack(ModItems.lexicon));
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onNetherPortalActivation(NetherPortalActivationEvent e) {
		if (e.worldObj.provider.dimensionId == AlfheimConfig.dimensionIDAlfheim) e.setCanceled(true);
	}
	
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing e) {
		if (!AlfheimCore.enableElvenStory) return;
		if (e.entity instanceof EntityPlayer) {
			((EntityPlayer) e.entity).getAttributeMap().registerAttribute(AlfheimAPI.RACE);
			((EntityPlayer) e.entity).getAttributeMap().registerAttribute(AlfheimAPI.FLIGHT);
		}
	}
	
	@SubscribeEvent
	public void onClonePlayer(PlayerEvent.Clone e) {
		if (!AlfheimCore.enableElvenStory) return;
		EnumRace r = EnumRace.getRace((EntityPlayer) e.original);
		EnumRace.setRace((EntityPlayer) e.entityPlayer, r);
	}
	
	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		if (!AlfheimCore.enableElvenStory) return;
		if (!AlfheimConfig.enableWingsNonAlfheim && e.player.worldObj.provider.dimensionId != AlfheimConfig.dimensionIDAlfheim) return;
		e.player.capabilities.allowFlying = !EnumRace.getRace(e.player).equals(EnumRace.HUMAN);
	}
	
	@SubscribeEvent
	public void onPlayerChangeDimension(PlayerChangedDimensionEvent e) {
		if (!AlfheimCore.enableElvenStory) return;
		// TODO fix IAttribute sync across dimensions
	}
	
	@SubscribeEvent
	public void onAlfPortalUpdate(ElvenPortalUpdateEvent e) {
		if (e.portalTile.getWorldObj().provider.dimensionId == AlfheimConfig.dimensionIDAlfheim && ((TileAlfPortal)e.portalTile).ticksOpen >= 0) ((TileAlfPortal) e.portalTile).ticksOpen = 0;
	}
	
	@SubscribeEvent
	public void onEntityAttacked(LivingAttackEvent e) {
		if (e.entityLiving instanceof EntityAlfheimPixie && e.source.getDamageType().equals(DamageSource.inWall.getDamageType())) e.setCanceled(true);
		if (e.source.isFireDamage() && e.entityLiving instanceof EntityPlayer && ((EntityPlayer)e.entityLiving).getCurrentArmor(1) != null && ((EntityPlayer)e.entityLiving).getCurrentArmor(1).getItem() == AlfheimItems.elementalLeggings && ManaItemHandler.requestManaExact(((EntityPlayer)e.entityLiving).getCurrentArmor(1), ((EntityPlayer)e.entityLiving), MathHelper.ceiling_float_int(10 * e.ammount), !e.entityLiving.worldObj.isRemote)) e.setCanceled(true);
	}
	
	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event) {
		ItemStack equipped = event.entityPlayer.getCurrentEquippedItem();
		if (equipped != null && equipped.getItem() == Items.bowl && event.action == Action.RIGHT_CLICK_BLOCK && !event.world.isRemote) {
			MovingObjectPosition movingobjectposition = ToolCommons.raytraceFromEntity(event.world, event.entityPlayer, true, 4.5F);

			if (movingobjectposition != null) {
				if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && !event.world.isRemote) {
					int i = movingobjectposition.blockX;
					int j = movingobjectposition.blockY;
					int k = movingobjectposition.blockZ;

					if (event.world.getBlock(i, j, k).getMaterial() == Material.water) {
						--equipped.stackSize;

						if (equipped.stackSize <= 0) event.entityPlayer.inventory.setInventorySlotContents(event.entityPlayer.inventory.currentItem, new ItemStack(ModItems.waterBowl));
						else event.entityPlayer.dropPlayerItemWithRandomChoice(new ItemStack(ModItems.waterBowl), false);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityUpdate(LivingUpdateEvent e) {
		if (e.entityLiving instanceof EntityPlayer) onPlayerUpdate(e);
	}
	
	private void onPlayerUpdate(LivingUpdateEvent e) {
		EntityPlayer player = (EntityPlayer) e.entityLiving;
		if (!player.capabilities.isCreativeMode) {
			if (AlfheimCore.enableElvenStory) {
				if (player.getAttributeMap().getAttributeInstance(AlfheimAPI.FLIGHT).getAttributeValue() >= 0
				&&	player.getAttributeMap().getAttributeInstance(AlfheimAPI.FLIGHT).getAttributeValue() <= AlfheimAPI.FLIGHT.getDefaultValue()) {
					if (player.capabilities.isFlying) {
														player.getAttributeMap().getAttributeInstance(AlfheimAPI.FLIGHT)
														.setBaseValue(player.getAttributeMap().getAttributeInstance(AlfheimAPI.FLIGHT).getAttributeValue() -
														(player.isSprinting() ? 4 : (player.motionX != 0.0 || player.motionY > 0.0 || player.motionZ != 0.0) ? 2 : 1));
					if (player.isSprinting()) player.moveFlying(0F, 1F, 0.01F);
					} else								player.getAttributeMap().getAttributeInstance(AlfheimAPI.FLIGHT)
														.setBaseValue(player.getAttributeMap().getAttributeInstance(AlfheimAPI.FLIGHT).getAttributeValue() + 
														(player.getAttributeMap().getAttributeInstance(AlfheimAPI.FLIGHT).getAttributeValue() < AlfheimAPI.FLIGHT.getDefaultValue() ? 1 : 0));
					
				}
				if (player.getAttributeMap().getAttributeInstance(AlfheimAPI.FLIGHT).getAttributeValue() <= 0)	player.capabilities.isFlying = false; 
			}
		}
	}
}
