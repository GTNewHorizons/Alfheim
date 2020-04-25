package alfheim.common.item.relic

import alexsocol.asjlib.*
import alfheim.AlfheimCore
import alfheim.common.achievement.AlfheimAchievements
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.helper.IconHelper
import alfheim.common.core.util.*
import alfheim.common.item.AlfheimItems
import alfheim.common.network.MessageEffect
import baubles.api.*
import baubles.common.lib.PlayerHandler
import cpw.mods.fml.common.eventhandler.*
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.texture.*
import net.minecraft.entity.*
import net.minecraft.entity.player.*
import net.minecraft.item.ItemStack
import net.minecraft.potion.*
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.*
import org.lwjgl.opengl.GL11.*
import vazkii.botania.api.item.IBaubleRender
import vazkii.botania.api.mana.*
import vazkii.botania.common.core.helper.ItemNBTHelper.*
import vazkii.botania.common.item.relic.ItemRelicBauble
import kotlin.math.*

class ItemTankMask: ItemRelicBauble("TankMask"), IBaubleRender, IManaUsingItem {

	lateinit var jojocon: IIcon
	
	init {
		creativeTab = AlfheimTab
	}
	
	override fun getBaubleType(stack: ItemStack) = BaubleType.AMULET
	
	override fun onItemRightClick(stack: ItemStack, world: World?, player: EntityPlayer): ItemStack {
		if (!player.isSneaking) return stack
		setBoolean(stack, TAG_ACTIVATED, !getBoolean(stack, TAG_ACTIVATED, false))
		return stack
	}
	
	override fun onUpdate(stack: ItemStack?, world: World?, entity: Entity?, slotID: Int, inHand: Boolean) {
		super.onUpdate(stack, world, entity, slotID, inHand)
		setInt(stack!!, TAG_POSSESSION, 0)
		if (entity is EntityPlayer && getInt(stack, TAG_COOLDOWN, 0) > 0) setInt(stack, TAG_COOLDOWN, getInt(stack, TAG_COOLDOWN, 0) - ManaItemHandler.requestMana(stack, entity as EntityPlayer?, 1, world!!.isRemote))
	}
	
	override fun onWornTick(stack: ItemStack, player: EntityLivingBase) {
		if (player.worldObj.isRemote || player !is EntityPlayer) return
		
		val baubles = PlayerHandler.getPlayerBaubles(player)
		val inSlot: ItemStack? = baubles.getStackInSlot(0)
		
		// Multibauble exploit fix
		if (inSlot?.item !is ItemTankMask) return
		
		setInt(stack, TAG_POSSESSION, getInt(stack, TAG_POSSESSION, 0) + 1)
		player.addPotionEffect(PotionEffect(Potion.damageBoost.id, 20, 4))
		player.addPotionEffect(PotionEffect(Potion.resistance.id, 20, 4))
		val time = getInt(stack, TAG_POSSESSION, 1)
		val possessed = PotionEffect(AlfheimConfigHandler.potionIDPossession, time)
		possessed.curativeItems.clear()
		player.addPotionEffect(possessed)
		if (time >= 1200 && time % 20 == 0) player.attackEntityFrom(DamageSourceSpell.possession, (player.getActivePotionEffect(AlfheimConfigHandler.potionIDPossession)!!.getDuration() - 1200) / 400f)
		
		if (time >= 3600) {
			(inSlot.item as IBauble).onUnequipped(inSlot, player)
			val copy = inSlot.copy()
			baubles.setInventorySlotContents(0, null)
			
			setInt(copy, TAG_COOLDOWN, MAX_COOLDOWN * 3)
			player.triggerAchievement(AlfheimAchievements.outstander)
			
			player.removePotionEffect(Potion.damageBoost.id)
			player.removePotionEffect(Potion.resistance.id)
			
			if (!player.inventory.addItemStackToInventory(copy))
				player.dropPlayerItemWithRandomChoice(copy, true)
		}
	}
	
	override fun onEquipped(stack: ItemStack, entity: EntityLivingBase) {
		if (entity.worldObj.isRemote) return
		setInt(stack, TAG_POSSESSION, 0)
		setInt(stack, TAG_COOLDOWN, MAX_COOLDOWN)
		val possessed = PotionEffect(AlfheimConfigHandler.potionIDPossession, 2)
		possessed.curativeItems.clear()
		entity.addPotionEffect(possessed)
	}
	
	override fun onUnequipped(stack: ItemStack, entity: EntityLivingBase) {
		//if (entity.worldObj.isRemote) return;
		setInt(stack, TAG_POSSESSION, 0)
		if (entity.isPotionActive(AlfheimConfigHandler.potionIDPossession)) entity.removePotionEffect(AlfheimConfigHandler.potionIDPossession)
	}
	
	override fun canEquip(stack: ItemStack?, player: EntityLivingBase?) = false
	
	override fun canUnequip(stack: ItemStack?, player: EntityLivingBase?) = getInt(stack, TAG_POSSESSION, 0) < 1800
	
	override fun addInformation(stack: ItemStack, player: EntityPlayer?, list: MutableList<Any?>, adv: Boolean) {
		if (stack.displayName.toLowerCase().trim() == "kono dio da") list.also {
			it.add("${EnumChatFormatting.DARK_RED}${EnumChatFormatting.BOLD}${EnumChatFormatting.UNDERLINE}SONO CHI NO SADAME")
			it.add("")
		}
		super.addInformation(stack, player, list, adv)
	}
	
	override fun addHiddenTooltip(stack: ItemStack, player: EntityPlayer?, list: MutableList<Any?>, advTT: Boolean) {
		super.addHiddenTooltip(stack, player, list, advTT)
		val e = if (getInt(stack, TAG_COOLDOWN, 0) > 0) EnumChatFormatting.DARK_GRAY else if (getBoolean(stack, TAG_ACTIVATED, false)) EnumChatFormatting.GREEN else EnumChatFormatting.DARK_RED
		list.add("")
		list.add("$e${StatCollector.translateToLocal("$unlocalizedName.${if (getBoolean(stack, TAG_ACTIVATED, false)) "" else "in"}active")}")
	}
	
	override fun onPlayerBaubleRender(stack: ItemStack, e: RenderPlayerEvent, type: IBaubleRender.RenderType) {
		if (type != IBaubleRender.RenderType.HEAD) return
		mc.renderEngine.bindTexture(TextureMap.locationItemsTexture)
		val stone = stack.displayName.toLowerCase().trim() == "kono dio da"
		val icon = if (stone) jojocon else itemIcon
		glPushMatrix()
		
		glTranslated(0.0, (if (e.entityPlayer !== mc.thePlayer) 1.68 else 0.0) - e.entityPlayer.defaultEyeHeight + if (e.entityPlayer.isSneaking) 0.0625 else 0.0, 0.0)
		glRotated(90.0, 0.0, 1.0, 0.0)
		glRotated(180.0, 1.0, 0.0, 0.0)
		glTranslated(-0.25 * 7.75/7 + if (stone) 0.1/6 else 0.0, -1/6.5, -0.2 * 8/7)
		glScaled(0.5 * 7.75 / 7)
		
		ItemRenderer.renderItemIn2D(Tessellator.instance, icon.maxU, icon.minV, icon.minU, icon.maxV, icon.iconWidth, icon.iconHeight, 1f / 16f)
		
		//glEnable(GL_CULL_FACE)
		glPopMatrix()
	}
	
	override fun usesMana(stack: ItemStack) = getInt(stack, TAG_COOLDOWN, 0) > 0
	
	override fun registerIcons(reg: IIconRegister) {
		itemIcon = IconHelper.forItem(reg, this)
		jojocon = IconHelper.forItem(reg, this, "Stone")
	}
	
	override fun getIconIndex(stack: ItemStack) =
		if (stack.displayName.toLowerCase().trim() == "kono dio da") jojocon else super.getIconIndex(stack)!!
	
	override fun getIcon(stack: ItemStack, pass: Int) = getIconIndex(stack)
	
	companion object {
		
		const val TAG_POSSESSION = "possession"
		const val TAG_ACTIVATED = "activated"
		const val TAG_COOLDOWN = "cooldown"
		const val MAX_COOLDOWN = 12000
		
		init {
			MinecraftForge.EVENT_BUS.register(this)
		}
		
		@SubscribeEvent(priority = EventPriority.LOWEST)
		fun onEntityDeath(e: LivingDeathEvent) {
			val player = e.entityLiving as? EntityPlayer ?: return
			
			if (e.source.damageType == DamageSourceSpell.possession.damageType) {
				val baubles = PlayerHandler.getPlayerBaubles(player)
				if (baubles.getStackInSlot(0)?.item === AlfheimItems.mask) {
					setInt(baubles.getStackInSlot(0), TAG_POSSESSION, 0)
					if (!player.inventory.addItemStackToInventory(baubles.getStackInSlot(0).copy())) {
						player.dropPlayerItemWithRandomChoice(baubles.getStackInSlot(0).copy(), false)
					}
					baubles.setInventorySlotContents(0, null)
				}
			}
			
			if (e.entityLiving.isPotionActive(AlfheimConfigHandler.potionIDLeftFlame))
				return
			
			if (canBeSaved(player)) {
				val slot = ASJUtilities.getSlotWithItem(AlfheimItems.mask, player.inventory)
				val baubles = PlayerHandler.getPlayerBaubles(player)
				if (baubles.getStackInSlot(0) != null)
					if (!player.inventory.addItemStackToInventory(baubles.getStackInSlot(0).copy())) player.dropPlayerItemWithRandomChoice(baubles.getStackInSlot(0).copy(), false)
				
				baubles.setInventorySlotContents(0, player.inventory.getStackInSlot(slot).copy())
				player.inventory.consumeInventoryItem(AlfheimItems.mask)
				e.isCanceled = true
				val h =  max(0f, min(max(e.entityLiving.health, e.entityLiving.maxHealth / 4f), e.entityLiving.maxHealth))
				e.entityLiving.health = h
			}
		}
		
		@SubscribeEvent
		fun onEntityUpdate(e: LivingEvent.LivingUpdateEvent) {
			val player = e.entityLiving as? EntityPlayerMP ?: return
			
			if (player.hasAchievement(AlfheimAchievements.outstander)) {
				player.getActivePotionEffect(Potion.damageBoost.id)?.let { if (it.amplifier == 0) it.duration = max(it.duration, 20) } ?: player.addPotionEffect(PotionEffect(Potion.damageBoost.id, 20, 0))
				
				player.getActivePotionEffect(Potion.resistance.id)?.let { if (it.amplifier == 0) it.duration = max(it.duration, 20) } ?: player.addPotionEffect(PotionEffect(Potion.resistance.id, 20, 0))
				
				AlfheimCore.network.sendToAll(MessageEffect(player, player.getActivePotionEffect(Potion.damageBoost.id)))
				AlfheimCore.network.sendToAll(MessageEffect(player, player.getActivePotionEffect(Potion.resistance.id)))
			}
		}
		
		fun canBeSaved(player: EntityPlayer): Boolean {
			if (player.inventory.hasItem(AlfheimItems.mask)) {
				val slot = ASJUtilities.getSlotWithItem(AlfheimItems.mask, player.inventory)
				if (!getBoolean(player.inventory.getStackInSlot(slot), TAG_ACTIVATED, false) || getInt(player.inventory.getStackInSlot(slot), TAG_COOLDOWN, 0) > 0) return false
				
				val baubles = PlayerHandler.getPlayerBaubles(player)
				if (baubles.getStackInSlot(0) != null)
					if (!(baubles.getStackInSlot(0).item as IBauble).canUnequip(baubles.getStackInSlot(0), player))
						return false
				
				return true
			}
			
			return false
		}
	}
}