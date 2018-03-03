package alfheim.common.item.relic;

import static vazkii.botania.common.core.helper.ItemNBTHelper.*;

import java.util.List;

import org.lwjgl.opengl.GL11;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.common.core.registry.AlfheimAchievements;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.core.utils.AlfheimConfig;
import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.IBauble;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import vazkii.botania.api.item.IBaubleRender;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.relic.ItemRelicBauble;

public class ItemTankMask extends ItemRelicBauble implements IBaubleRender, IManaUsingItem {

	private static final String TAG_POSSESSION = "possession";
	private static final String TAG_ACTIVATED = "activated";
	private static final String TAG_COOLDOWN = "cooldown";
	private static final int MAX_COOLDOWN = 12000;

	public ItemTankMask() {
		super("TankMask");
		setCreativeTab(AlfheimCore.alfheimTab);
		setBindAchievement(AlfheimAchievements.mask);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public BaubleType getBaubleType(ItemStack stack) {
		return BaubleType.AMULET;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (!player.isSneaking()) return stack;
		initNBT(stack);
		setBoolean(stack, TAG_ACTIVATED, !getBoolean(stack, TAG_ACTIVATED, false));
		return stack;
	}
	
	@SubscribeEvent
	public void onLivingAttacked(LivingAttackEvent e) {
		if (e.entityLiving instanceof EntityPlayer && ASJUtilities.willEntityDie(e) && ((EntityPlayer) e.entityLiving).inventory.hasItem(this)) {
			EntityPlayer player = (EntityPlayer) e.entityLiving;
			int slot = ASJUtilities.getSlotWithItem(this, player.inventory);
			initNBT(player.inventory.getStackInSlot(slot));
			if (!getBoolean(player.inventory.getStackInSlot(slot), TAG_ACTIVATED, false) || getInt(player.inventory.getStackInSlot(slot), TAG_COOLDOWN, 0) > 0) return;
			IInventory baubles = BaublesApi.getBaubles(player);
			if (baubles.getStackInSlot(0) != null)
				if (((IBauble) baubles.getStackInSlot(0).getItem()).canUnequip(baubles.getStackInSlot(0), player)) {
					if (!player.inventory.addItemStackToInventory(baubles.getStackInSlot(0).copy())) player.dropPlayerItemWithRandomChoice(baubles.getStackInSlot(0).copy(), false);
				}
				else return;
			baubles.setInventorySlotContents(0, player.inventory.getStackInSlot(slot).copy());
			player.inventory.consumeInventoryItem(this);
			e.setCanceled(true);
		}
	}
	
	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent e) {
		if (e.entityLiving instanceof EntityPlayer) {
			IInventory baubles = BaublesApi.getBaubles((EntityPlayer) e.entityLiving);
			if (baubles.getStackInSlot(0) != null && baubles.getStackInSlot(0).getItem() == this) setInt(baubles.getStackInSlot(0), TAG_POSSESSION, 0);
		} 
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slotID, boolean inHand) {
		super.onUpdate(stack, world, entity, slotID, inHand);
		if(entity instanceof EntityPlayer && getInt(stack, TAG_COOLDOWN, 0) > 0) setInt(stack, TAG_COOLDOWN, getInt(stack, TAG_COOLDOWN, 0) - ManaItemHandler.requestMana(stack, (EntityPlayer) entity, 1, world.isRemote));
	}
	
	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase entity) {
		if (entity.worldObj.isRemote) return;
		initNBT(stack);
		setInt(stack, TAG_POSSESSION, getInt(stack, TAG_POSSESSION, 0) + 1);
		entity.addPotionEffect(new PotionEffect(Potion.damageBoost.id, 20, 4));
		entity.addPotionEffect(new PotionEffect(Potion.resistance.id, 20, 4));
		entity.addPotionEffect(new PotionEffect(AlfheimConfig.potionIDPossession, getInt(stack, TAG_POSSESSION, 1)));
	}

	@Override
	public void onEquipped(ItemStack stack, EntityLivingBase entity) {
		if (entity.worldObj.isRemote) return;
		initNBT(stack);
		setInt(stack, TAG_POSSESSION, 0);
		setInt(stack, TAG_COOLDOWN, MAX_COOLDOWN);
		entity.addPotionEffect(new PotionEffect(AlfheimConfig.potionIDPossession, 2));
	}

	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase entity) {
		if (entity.worldObj.isRemote) return;
		initNBT(stack);
		setInt(stack, TAG_POSSESSION, 0);
		PotionEffect possessed = entity.getActivePotionEffect(AlfheimRegistry.possession);
		if (possessed != null) entity.removePotionEffect(AlfheimConfig.potionIDPossession);
	}
	
	@Override
	public boolean canEquip(ItemStack stack, EntityLivingBase player) {
		return false;
	}

	@Override
	public boolean canUnequip(ItemStack stack, EntityLivingBase player) {
		initNBT(stack);
		return getInt(stack, TAG_POSSESSION, 0) < 1800;
	}
	
	@Override
	public void addHiddenTooltip(ItemStack stack, EntityPlayer player, List list, boolean advTT) {
		super.addHiddenTooltip(stack, player, list, advTT);
		EnumChatFormatting e = getInt(stack, TAG_COOLDOWN, 0) > 0 ? EnumChatFormatting.DARK_GRAY : getBoolean(stack, TAG_ACTIVATED, false) ? EnumChatFormatting.GREEN : EnumChatFormatting.DARK_RED;
		list.add(e + StatCollector.translateToLocal(getUnlocalizedName() + '.' + (getBoolean(stack, TAG_ACTIVATED, false) ? "" : "in") + "active"));
	}

	@Override
	public void onPlayerBaubleRender(ItemStack stack, RenderPlayerEvent e, RenderType type) {
		if (type != RenderType.HEAD) return;
		EntityItem entityitem = new EntityItem(e.entityPlayer.worldObj, 0.0D, 0.0D, 0.0D, stack);
		Item item = entityitem.getEntityItem().getItem();
		entityitem.getEntityItem().stackSize = 1;
		entityitem.hoverStart = 0.0F;
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glRotated(180, 0, 0, 1);
		GL11.glRotated(-90, 0, 1, 0);
		GL11.glTranslated(0, 0.3, 0.275);
		// Tessellator.instance.setBrightness(Blocks.air.getMixedBrightnessForBlock(e.entityPlayer.worldObj, MathHelper.floor_double(e.entityPlayer.posX), MathHelper.floor_double(e.entityPlayer.posY + 1), MathHelper.floor_double(e.entityPlayer.posZ)));

		RenderItem.renderInFrame = true;
		RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0D, -0.2501D, 0.0D, 0.0F, 0.0F);
		RenderItem.renderInFrame = false;

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return getInt(stack, TAG_COOLDOWN, 0) > 0;
	}
}