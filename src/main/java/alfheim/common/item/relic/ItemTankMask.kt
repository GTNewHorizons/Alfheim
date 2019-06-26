package alfheim.common.item.relic

import org.lwjgl.opengl.GL11.*
import vazkii.botania.common.core.helper.ItemNBTHelper.*

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.common.core.registry.AlfheimItems
import alfheim.common.core.registry.AlfheimRegistry
import alfheim.common.core.util.DamageSourceSpell
import baubles.api.BaubleType
import baubles.api.IBauble
import baubles.common.lib.PlayerHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.client.renderer.entity.RenderItem
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.StatCollector
import net.minecraft.world.World
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingDeathEvent
import vazkii.botania.api.item.IBaubleRender
import vazkii.botania.api.mana.IManaUsingItem
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.item.relic.ItemRelicBauble

class ItemTankMask: ItemRelicBauble("TankMask"), IBaubleRender, IManaUsingItem {
	init {
		creativeTab = AlfheimCore.alfheimTab
		MinecraftForge.EVENT_BUS.register(this)
	}
	
	override fun getBaubleType(stack: ItemStack): BaubleType {
		return BaubleType.AMULET
	}
	
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
	
	override fun onWornTick(stack: ItemStack, entity: EntityLivingBase) {
		if (entity.worldObj.isRemote) return
		setInt(stack, TAG_POSSESSION, getInt(stack, TAG_POSSESSION, 0) + 1)
		entity.addPotionEffect(PotionEffect(Potion.damageBoost.id, 20, 4))
		entity.addPotionEffect(PotionEffect(Potion.resistance.id, 20, 4))
		val time = getInt(stack, TAG_POSSESSION, 1)
		val possessed = PotionEffect(AlfheimRegistry.possession.id, time)
		possessed.curativeItems.clear()
		entity.addPotionEffect(possessed)
		if (time >= 1200 && time % 20 == 0) entity.attackEntityFrom(DamageSourceSpell.possession, (entity.getActivePotionEffect(AlfheimRegistry.possession).getDuration() - 1200) / 400.0f)
	}
	
	override fun onEquipped(stack: ItemStack, entity: EntityLivingBase) {
		if (entity.worldObj.isRemote) return
		setInt(stack, TAG_POSSESSION, 0)
		setInt(stack, TAG_COOLDOWN, MAX_COOLDOWN)
		val possessed = PotionEffect(AlfheimRegistry.possession.id, 2)
		possessed.curativeItems.clear()
		entity.addPotionEffect(possessed)
	}
	
	override fun onUnequipped(stack: ItemStack?, entity: EntityLivingBase) {
		//if (entity.worldObj.isRemote) return;
		setInt(stack!!, TAG_POSSESSION, 0)
		if (entity.isPotionActive(AlfheimRegistry.possession)) entity.removePotionEffect(AlfheimRegistry.possession.id)
	}
	
	override fun canEquip(stack: ItemStack?, player: EntityLivingBase?): Boolean {
		return false
	}
	
	override fun canUnequip(stack: ItemStack?, player: EntityLivingBase?): Boolean {
		return getInt(stack, TAG_POSSESSION, 0) < 1800
	}
	
	@SubscribeEvent
	fun onEntityDeath(e: LivingDeathEvent) {
		val player: EntityPlayer
		if (e.entityLiving is EntityPlayer)
			player = e.entityLiving as EntityPlayer
		else
			return
		
		mask@ if (player.inventory.hasItem(AlfheimItems.mask)) {
			val slot = ASJUtilities.getSlotWithItem(AlfheimItems.mask, player.inventory)
			if (!getBoolean(player.inventory.getStackInSlot(slot), TAG_ACTIVATED, false) || getInt(player.inventory.getStackInSlot(slot), TAG_COOLDOWN, 0) > 0) break@mask
			val baubles = PlayerHandler.getPlayerBaubles(player)
			if (baubles.getStackInSlot(0) != null)
				if ((baubles.getStackInSlot(0).item as IBauble).canUnequip(baubles.getStackInSlot(0), player)) {
					if (!player.inventory.addItemStackToInventory(baubles.getStackInSlot(0).copy())) player.dropPlayerItemWithRandomChoice(baubles.getStackInSlot(0).copy(), false)
				} else
					break@mask
			baubles.setInventorySlotContents(0, player.inventory.getStackInSlot(slot).copy())
			player.inventory.consumeInventoryItem(AlfheimItems.mask)
			e.isCanceled = true
			return
		}
		
		if (e.source.damageType == DamageSourceSpell.possession.damageType) {
			val baubles = PlayerHandler.getPlayerBaubles(player)
			if (baubles.getStackInSlot(0) != null && baubles.getStackInSlot(0).item === AlfheimItems.mask) {
				ItemNBTHelper.setInt(baubles.getStackInSlot(0), ItemTankMask.TAG_POSSESSION, 0)
				if (!player.inventory.addItemStackToInventory(baubles.getStackInSlot(0).copy())) {
					player.dropPlayerItemWithRandomChoice(baubles.getStackInSlot(0).copy(), false)
				}
				baubles.setInventorySlotContents(0, null)
			}
		}
	}
	
	override fun addHiddenTooltip(stack: ItemStack, player: EntityPlayer?, list: MutableList<*>, advTT: Boolean) {
		super.addHiddenTooltip(stack, player, list, advTT)
		val e = if (getInt(stack, TAG_COOLDOWN, 0) > 0) EnumChatFormatting.DARK_GRAY else if (getBoolean(stack, TAG_ACTIVATED, false)) EnumChatFormatting.GREEN else EnumChatFormatting.DARK_RED
		list.add("")
		list.add(e.toString() + StatCollector.translateToLocal(unlocalizedName + '.'.toString() + (if (getBoolean(stack, TAG_ACTIVATED, false)) "" else "in") + "active"))
	}
	
	override fun onPlayerBaubleRender(stack: ItemStack, e: RenderPlayerEvent, type: IBaubleRender.RenderType) {
		if (type != IBaubleRender.RenderType.HEAD) return
		val entityitem = EntityItem(e.entityPlayer.worldObj, 0.0, 0.0, 0.0, stack)
		val item = entityitem.entityItem.item
		entityitem.entityItem.stackSize = 1
		entityitem.hoverStart = 0.0f
		glPushMatrix()
		glDisable(GL_CULL_FACE)
		glRotated(180.0, 0.0, 0.0, 1.0)
		glRotated(-90.0, 0.0, 1.0, 0.0)
		glTranslated(0.0, 0.3, 0.275)
		// Tessellator.instance.setBrightness(Blocks.air.getMixedBrightnessForBlock(e.entityPlayer.worldObj, MathHelper.floor_double(e.entityPlayer.posX), MathHelper.floor_double(e.entityPlayer.posY + 1), MathHelper.floor_double(e.entityPlayer.posZ)));
		
		RenderItem.renderInFrame = true
		RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0, -0.2501, 0.0, 0.0f, 0.0f)
		RenderItem.renderInFrame = false
		
		glEnable(GL_CULL_FACE)
		glPopMatrix()
	}
	
	override fun usesMana(stack: ItemStack): Boolean {
		return getInt(stack, TAG_COOLDOWN, 0) > 0
	}
	
	companion object {
		
		val TAG_POSSESSION = "possession"
		val TAG_ACTIVATED = "activated"
		val TAG_COOLDOWN = "cooldown"
		val MAX_COOLDOWN = 12000
	}
}