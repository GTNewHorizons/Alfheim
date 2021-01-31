package alfheim.common.item.equipment.bauble.faith

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alexsocol.asjlib.security.InteractionSecurity
import alfheim.AlfheimCore
import alfheim.api.event.PlayerInteractAdequateEvent.RightClick
import alfheim.api.event.PlayerInteractAdequateEvent.RightClick.Action.*
import alfheim.api.item.ColorOverrideHelper
import alfheim.common.item.AlfheimItems
import alfheim.common.item.equipment.bauble.*
import alfheim.common.item.relic.ItemNjordRing
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.block.material.Material
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import net.minecraftforge.event.ForgeEventFactory
import net.minecraftforge.event.entity.living.LivingHurtEvent
import net.minecraftforge.event.entity.player.AttackEntityEvent
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.Botania
import vazkii.botania.common.core.helper.ItemNBTHelper
import java.awt.Color
import kotlin.math.*

object FaithHandlerNjord: IFaithHandler {
	
	init {
		eventForge()
		eventFML()
	}
	
	override fun onWornTick(stack: ItemStack, player: EntityPlayer, type: IFaithHandler.FaithBauble) {
		if (type == IFaithHandler.FaithBauble.CLOAK) onCloakWornTick(player)
		if (type == IFaithHandler.FaithBauble.EMBLEM)
			if (stack.cooldown > 0) --stack.cooldown
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
	fun onPlayerClickLiquid(e: RightClick) {
		if (e.action != RIGHT_CLICK_LIQUID) return
		
		val player = e.player
		
		ItemPriestEmblem.getEmblem(2, player) ?: return
		if (getGodPowerLevel(player) < 3) return
		if (player.isSneaking || !player.isInsideOfMaterial(Material.air)) return

		if (!InteractionSecurity.canDoSomethingHere(player, e.x, e.y, e.z))
			return
		
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
	
	@SubscribeEvent
	fun onPlayerClickAir(e: RightClick) {
		if (e.action != RIGHT_CLICK_AIR) return
		val player = e.player
		
		if (!player.onGround) return
		
		if (player.heldItem != null) return
		
		val emblem = ItemPriestEmblem.getEmblem(2, player) ?: return
		
		if (emblem.cooldown > 0) return
		
		if (ManaItemHandler.requestManaExactForTool(emblem, player, 50, true)) {
			player.addExhaustion(0.2f)
			
			if (!player.capabilities.isCreativeMode) emblem.cooldown = 50
			
			player.isSprinting = true
			player.motionY += 0.9
			val speed = 1.825
			player.motionX = (-sin(player.rotationYaw * Math.PI / 180) * cos(player.rotationPitch * Math.PI / 180) * speed)
			player.motionZ = (cos(player.rotationYaw * Math.PI / 180) * cos(player.rotationPitch * Math.PI / 180) * speed)
		}
	}
	
	// knowback entities
	@SubscribeEvent
	fun onPlayerAttack(e: AttackEntityEvent) {
		val player = e.entityPlayer
		var emblem = ItemPriestEmblem.getEmblem(2, player)
		if (emblem == null) if (AlfheimCore.ENABLE_RAGNAROK) emblem = ItemRagnarokEmblem.getEmblem(player, 2) ?: return else return
		
		val entity = e.target
		if (entity is EntityLivingBase)
			if (ManaItemHandler.requestManaExact(emblem, e.entityPlayer, 20, true)) {
				
				for (i in 0..(if (getGodPowerLevel(player) >= 6) 1 else 0))
					entity.knockBack(e.entityPlayer, -1f,
									 sin(e.entityPlayer.rotationYaw * Math.PI / 180),
									 -cos(e.entityPlayer.rotationYaw * Math.PI / 180))
			}
	}
	
	// minimize/prevent falldamage
	@SubscribeEvent
	fun onPlayerFall(e: LivingHurtEvent) {
		val player = e.entityLiving as? EntityPlayer ?: return
		val emblem = ItemPriestEmblem.getEmblem(2, player) ?: return
		
		if (e.source.damageType == DamageSource.fall.damageType) {
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
		if (ItemNjordRing.getNjordRing(player) != null) lvl += 1
		if (player.inventory.hasItemStack(ItemStack(AlfheimItems.rodInterdiction))) lvl += 1
		
		return lvl
	}
	
	override fun doParticles(stack: ItemStack, player: EntityPlayer) {
		if (player.ticksExisted % 10 == 0) {
			for (i in 0..6) {
				val vec = IFaithHandler.getHeadOrientation(player).mul(0.52)
				val color = Color(ColorOverrideHelper.getColor(player, 0x0101FF))
				val r = color.red.F / 255F
				val g = color.green.F / 255F
				val b = color.blue.F / 255F
				
				val (x, y, z) = Vector3.fromEntity(player)
				
				Botania.proxy.sparkleFX(mc.theWorld, x + vec.x, y + vec.y + 1.62, z + vec.z, r, g, b, 1f, 5)
			}
		}
	}
	
	private const val TAG_COOLDOWN = "throw_cooldown"
	
	private var ItemStack.cooldown
		get() = ItemNBTHelper.getInt(this, TAG_COOLDOWN, 0)
		set(value) = ItemNBTHelper.setInt(this, TAG_COOLDOWN, value)
}
