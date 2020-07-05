package alfheim.common.item.equipment.bauble.faith

import alexsocol.asjlib.*
import alfheim.api.event.PlayerInteractAdequateEvent.RightClick
import alfheim.api.event.PlayerInteractAdequateEvent.RightClick.Action.*
import alfheim.api.item.ColorOverrideHelper
import alfheim.common.item.AlfheimItems
import alfheim.common.item.equipment.bauble.*
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.*
import net.minecraftforge.event.ForgeEventFactory
import net.minecraftforge.event.entity.living.LivingHurtEvent
import net.minecraftforge.event.entity.player.AttackEntityEvent
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.Botania
import java.awt.Color
import kotlin.math.*

object FaithHandlerNjord: IFaithHandler {
	
	init {
		eventForge()
		eventFML()
	}
	
	override fun onWornTick(stack: ItemStack, player: EntityPlayer, type: IFaithHandler.FaithBauble) {
		if (type == IFaithHandler.FaithBauble.CLOAK) onCloakWornTick(player)
	}
	
	fun onCloakWornTick(player: EntityPlayer) {
//		if (!player.capabilities.isFlying && !player.onGround && !player.isSneaking && !player.isInWater)
//			player.motionY += 0.05
//
		val min = -0.0784000015258789
		val slowfall = 2.0
		if (!player.isSneaking && player.motionY < min && player.fallDistance >= 2.9) {
			player.motionY /= 1 + slowfall * 0.33
			player.fallDistance = max(2.9, player.fallDistance - slowfall / 3).F
		}
	}
	
	@SubscribeEvent
	fun onPlayerClick(e: RightClick) {
		if (e.action != RIGHT_CLICK_LIQUID) return
		
		val player = e.player
		
		ItemPriestEmblem.getEmblem(2, player) ?: return
		if (getGodPowerLevel(player) < 3) return
		if (player.isSneaking || player.isInWater) return

		val state = player.worldObj.getBlock(e.x, e.y, e.z)
		
		if (state.material.isLiquid) {
			val originalStack = player.heldItem?.copy() ?: return
			val result = player.heldItem.item.onItemUse(player.heldItem, player, player.worldObj, e.x, e.y, e.z, e.side, 0f, 0f, 0f)
			
			if (player.capabilities.isCreativeMode)
				player.setCurrentItemOrArmor(0, originalStack)
			else if (player.heldItem.stackSize <= 0) {
				ForgeEventFactory.onPlayerDestroyItem(player, originalStack)
				player.setCurrentItemOrArmor(0, null)
			}
			
			if (result)
				player.swingItem()
		}
	}
	
	// throw player
	@SubscribeEvent
	fun onPlayerClickAir(e: RightClick) {
		if (e.action != RIGHT_CLICK_AIR) return
		val player = e.player
		
		if (player.heldItem != null) return
		
		val emblem = ItemPriestEmblem.getEmblem(2, player) ?: return
		
		// TODO add cooldown
		if (ManaItemHandler.requestManaExactForTool(emblem, player, 50, true)) {
			player.addExhaustion(0.2f)
			
			player.isSprinting = true
			val increase = min(0.07f * 200 + 0.67f, 0.9f)
			
			player.motionY += increase.D
			val speed = min(0.12f * 200 + 1.1f, 1.825f)
			player.motionX = (-MathHelper.sin(player.rotationYaw / 180.0f * Math.PI.toFloat()) * MathHelper.cos(player.rotationPitch / 180.0f * Math.PI.toFloat()) * speed).toDouble()
			player.motionZ = (MathHelper.cos(player.rotationYaw / 180.0f * Math.PI.toFloat()) * MathHelper.cos(player.rotationPitch / 180.0f * Math.PI.toFloat()) * speed).toDouble()
		}
	}
	
	// knowback entities
	@SubscribeEvent
	fun onPlayerAttack(e: AttackEntityEvent) {
		val player = e.entityPlayer
		val emblem = ItemPriestEmblem.getEmblem(2, player) ?: return
		
		val entity = e.target
		if (entity is EntityLivingBase)
			if (ManaItemHandler.requestManaExact(emblem, e.entityPlayer, 20, true)) {
				
				for (i in 0..(if (getGodPowerLevel(player) >= 6) 1 else 0))
					entity.knockBack(e.entityPlayer, -1f,
									 MathHelper.sin(e.entityPlayer.rotationYaw * Math.PI.F / 180).toDouble(),
									 -MathHelper.cos(e.entityPlayer.rotationYaw * Math.PI.F / 180).toDouble())
			}
	}
	
	// minimize/prevent falldamage
	@SubscribeEvent
	fun onPlayerFall(e: LivingHurtEvent) {
		val player = e.entityLiving as? EntityPlayer ?: return
		val emblem = ItemPriestEmblem.getEmblem(2, player) ?: return
		
		if (e.source == DamageSource.fall) {
			if (getGodPowerLevel(player) >= 6)
				e.isCanceled = true
			else if (ManaItemHandler.requestManaExact(emblem, player, 100, true))
				e.ammount = min(e.ammount, 4f)
		}
	}
	
	override fun getGodPowerLevel(player: EntityPlayer): Int {
		var lvl = 0
		
		if (ItemPriestCloak.getCloak(2, player) != null) lvl += 3
		if (ItemPriestEmblem.getEmblem(2, player) != null) lvl += 2
		// if (ItemThorRing.getThorRing(player) != null) lvl += 1
		if (player.inventory.hasItemStack(ItemStack(AlfheimItems.rodInterdiction))) lvl += 1
		
		return lvl
	}
	
	override fun doParticles(stack: ItemStack, player: EntityPlayer) {
		if (player.ticksExisted % 10 == 0) {
			for (i in 0..6) {
				val vec = getHeadOrientation(player).multiply(0.52)
				val color = Color(ColorOverrideHelper.getColor(player, 0x0101FF))
				val r = color.red.F / 255F
				val g = color.green.F / 255F
				val b = color.blue.F / 255F
				
				if (mc.thePlayer !== player) vec.y += 1.62f
				
				spawnEmblem2(player.posX + vec.x, player.posY + vec.y, player.posZ + vec.z, r.D, g.D, b.D)
			}
		}
	}
	
	fun spawnEmblem2(x: Double, y: Double, z: Double, r: Double, g: Double, b: Double) {
		Botania.proxy.sparkleFX(mc.theWorld, x, y, z, r.F, g.F, b.F, 1f, 5)
	}
}
