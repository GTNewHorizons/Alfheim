package alfheim.common.item.relic;

import static vazkii.botania.common.core.helper.ItemNBTHelper.getBoolean;
import static vazkii.botania.common.core.helper.ItemNBTHelper.getInt;
import static vazkii.botania.common.core.helper.ItemNBTHelper.setBoolean;
import static vazkii.botania.common.core.helper.ItemNBTHelper.setInt;

import java.util.List;

import org.lwjgl.opengl.GL11;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.common.core.registry.AlfheimItems;
import alfheim.common.core.registry.AlfheimRegistry;
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
import net.minecraft.util.DamageSource;
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
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public BaubleType getBaubleType(ItemStack stack) {
		return BaubleType.AMULET;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if (!player.isSneaking()) return stack;
		setBoolean(stack, TAG_ACTIVATED, !getBoolean(stack, TAG_ACTIVATED, false));
		return stack;
	}
	
	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slotID, boolean inHand) {
		super.onUpdate(stack, world, entity, slotID, inHand);
		if(entity instanceof EntityPlayer && getInt(stack, TAG_COOLDOWN, 0) > 0) setInt(stack, TAG_COOLDOWN, getInt(stack, TAG_COOLDOWN, 0) - ManaItemHandler.requestMana(stack, (EntityPlayer) entity, 1, world.isRemote));
	}
	
	@Override
	public void onWornTick(ItemStack stack, EntityLivingBase entity) {
		if (entity.worldObj.isRemote) return;
		setInt(stack, TAG_POSSESSION, getInt(stack, TAG_POSSESSION, 0) + 1);
		entity.addPotionEffect(new PotionEffect(Potion.damageBoost.id, 20, 4));
		entity.addPotionEffect(new PotionEffect(Potion.resistance.id, 20, 4));
		int time = getInt(stack, TAG_POSSESSION, 1);
		PotionEffect possessed = new PotionEffect(AlfheimRegistry.possession.id, time);
		possessed.getCurativeItems().clear();
		entity.addPotionEffect(possessed);
		if (time >= (ModInfo.DEV ? 120 : 1200) && time % 20 == 0) {
			entity.setHealth(entity.getHealth() - ((entity.getActivePotionEffect(AlfheimRegistry.possession).getDuration() - (ModInfo.DEV ? 120 : 1200)) / 400.0F));
			if (!entity.isEntityAlive()) {
				IInventory baubles = BaublesApi.getBaubles((EntityPlayer) entity);
				if (baubles.getStackInSlot(0) != null && baubles.getStackInSlot(0).getItem() == AlfheimItems.mask) setInt(baubles.getStackInSlot(0), TAG_POSSESSION, 0);
				ASJUtilities.playDeathSound(entity);
				entity.onDeath(new DamageSource("possession"));
			}
		}
	}

	@Override
	public void onEquipped(ItemStack stack, EntityLivingBase entity) {
		if (entity.worldObj.isRemote) return;
		setInt(stack, TAG_POSSESSION, 0);
		setInt(stack, TAG_COOLDOWN, MAX_COOLDOWN);
		PotionEffect possessed = new PotionEffect(AlfheimRegistry.possession.id, 2);
		possessed.getCurativeItems().clear();
		entity.addPotionEffect(possessed);
	}

	@Override
	public void onUnequipped(ItemStack stack, EntityLivingBase entity) {
		if (entity.worldObj.isRemote) return;
		setInt(stack, TAG_POSSESSION, 0);
		PotionEffect possessed = entity.getActivePotionEffect(AlfheimRegistry.possession);
		if (possessed != null) entity.removePotionEffect(AlfheimRegistry.possession.id);
	}
	
	@Override
	public boolean canEquip(ItemStack stack, EntityLivingBase player) {
		return false;
	}

	@Override
	public boolean canUnequip(ItemStack stack, EntityLivingBase player) {
		return getInt(stack, TAG_POSSESSION, 0) < (ModInfo.DEV ? 180 : 1800);
	}
	
	@SubscribeEvent
	public void onLivingAttacked(LivingAttackEvent e) {
		if (e.entityLiving instanceof EntityPlayer && ASJUtilities.willEntityDie(e) && ((EntityPlayer) e.entityLiving).inventory.hasItem(AlfheimItems.mask)) {
			EntityPlayer player = (EntityPlayer) e.entityLiving;
			int slot = ASJUtilities.getSlotWithItem(AlfheimItems.mask, player.inventory);
			if (!getBoolean(player.inventory.getStackInSlot(slot), TAG_ACTIVATED, false) || getInt(player.inventory.getStackInSlot(slot), TAG_COOLDOWN, 0) > 0) return;
			IInventory baubles = BaublesApi.getBaubles(player);
			if (baubles.getStackInSlot(0) != null)
				if (((IBauble) baubles.getStackInSlot(0).getItem()).canUnequip(baubles.getStackInSlot(0), player)) {
					if (!player.inventory.addItemStackToInventory(baubles.getStackInSlot(0).copy())) player.dropPlayerItemWithRandomChoice(baubles.getStackInSlot(0).copy(), false);
				}
				else return;
			baubles.setInventorySlotContents(0, player.inventory.getStackInSlot(slot).copy());
			player.inventory.consumeInventoryItem(AlfheimItems.mask);
			e.setCanceled(true);
		}
	}
	
	@Override
	public void addHiddenTooltip(ItemStack stack, EntityPlayer player, List list, boolean advTT) {
		super.addHiddenTooltip(stack, player, list, advTT);
		EnumChatFormatting e = getInt(stack, TAG_COOLDOWN, 0) > 0 ? EnumChatFormatting.DARK_GRAY : getBoolean(stack, TAG_ACTIVATED, false) ? EnumChatFormatting.GREEN : EnumChatFormatting.DARK_RED;
		list.add("");
		list.add(e + StatCollector.translateToLocal(getUnlocalizedName() + '.' + (getBoolean(stack, TAG_ACTIVATED, false) ? "" : "in") + "active"));
	}

	@Override
	public void onPlayerBaubleRender(ItemStack stack, RenderPlayerEvent e, RenderType type) {
		if (type != RenderType.HEAD) return;
		EntityItem entityitem = new EntityItem(e.entityPlayer.worldObj, 0.0, 0.0, 0.0, stack);
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
		RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0, -0.2501, 0.0, 0.0F, 0.0F);
		RenderItem.renderInFrame = false;

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glPopMatrix();
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return getInt(stack, TAG_COOLDOWN, 0) > 0;
	}
}