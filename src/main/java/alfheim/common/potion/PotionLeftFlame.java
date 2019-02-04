package alfheim.common.potion;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.client.render.world.SpellEffectHandlerClient;
import alfheim.common.core.registry.AlfheimItems;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.core.util.AlfheimConfig;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.world.BlockEvent;

public class PotionLeftFlame extends PotionAlfheim {

	public PotionLeftFlame() {
		super(AlfheimConfig.potionIDLeftFlame, "leftFlame", false, 0x0);
		MinecraftForge.EVENT_BUS.register(this);
	}

	public void applyAttributesModifiersToEntity(EntityLivingBase target, BaseAttributeMap attributes, int ampl) {
		super.applyAttributesModifiersToEntity(target, attributes, ampl);
		if (AlfheimCore.enableMMO && target instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) target;
			player.capabilities.allowEdit = false;
			player.capabilities.allowFlying = true;
			player.capabilities.disableDamage = true;
			player.capabilities.isFlying = true;
			if(player instanceof EntityPlayerMP) ((EntityPlayerMP) player).theItemInWorldManager.setBlockReachDistance(-1);
			if(!ASJUtilities.isServer()) SpellEffectHandlerClient.onDeath(target);
		}
	}
	
	public void removeAttributesModifiersFromEntity(EntityLivingBase target, BaseAttributeMap attributes, int ampl) {
		super.removeAttributesModifiersFromEntity(target, attributes, ampl);
		if (AlfheimCore.enableMMO && target instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) target;
			player.capabilities.allowEdit = true;
			player.capabilities.allowFlying = false;
			player.capabilities.disableDamage = false;
			player.capabilities.isFlying = false;
			if(player instanceof EntityPlayerMP) ((EntityPlayerMP) player).theItemInWorldManager.setBlockReachDistance(5);
			player.getDataWatcher().updateObject(6, Float.valueOf(ampl));
		}
	}
	
	@SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent e) {
		if (AlfheimCore.enableMMO && e.getPlayer().isPotionActive(AlfheimRegistry.leftFlame)) e.setCanceled(true);
		if (e.getPlayer().getCurrentEquippedItem() != null && (e.getPlayer().getCurrentEquippedItem().getItem() == AlfheimItems.flugelSoul || e.getPlayer().getCurrentEquippedItem().getItem() == AlfheimItems.holoProjector)) e.setCanceled(true);
    }
	
	@SubscribeEvent
    public void onBlockPlace(BlockEvent.PlaceEvent e) {
		if (AlfheimCore.enableMMO && e.player.isPotionActive(AlfheimRegistry.leftFlame)) e.setCanceled(true);
    }
	
	@SubscribeEvent
    public void onBlockMultiPlace(BlockEvent.MultiPlaceEvent e) {
		if (AlfheimCore.enableMMO && e.player.isPotionActive(AlfheimRegistry.leftFlame)) e.setCanceled(true);
    }
	
	@SubscribeEvent
	public void onPlayerDrop(ItemTossEvent e) {
		if (AlfheimCore.enableMMO && e.player.isPotionActive(AlfheimRegistry.leftFlame)) {
			e.setCanceled(true);
			e.player.inventory.addItemStackToInventory(e.entityItem.getEntityItem().copy());
		}
	}
	
	@SubscribeEvent
	public void onBreakSpeed(BreakSpeed e) {
		if (AlfheimCore.enableMMO && e.entityLiving.isPotionActive(AlfheimRegistry.leftFlame)) e.newSpeed = 0;
	}
}
