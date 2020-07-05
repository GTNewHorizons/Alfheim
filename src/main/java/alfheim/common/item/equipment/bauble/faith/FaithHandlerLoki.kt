package alfheim.common.item.equipment.bauble.faith

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alfheim.api.event.PlayerInteractAdequateEvent
import alfheim.api.item.ColorOverrideHelper
import alfheim.common.entity.*
import alfheim.common.item.AlfheimItems
import alfheim.common.item.equipment.bauble.*
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.EntityLargeFireball
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import net.minecraftforge.event.entity.living.LivingAttackEvent
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.Botania
import vazkii.botania.common.item.relic.ItemLokiRing
import java.awt.Color

object FaithHandlerLoki: IFaithHandler {
	
	init {
		eventForge()
	}
	
	override fun onWornTick(stack: ItemStack, player: EntityPlayer, type: IFaithHandler.FaithBauble) {
		if (type != IFaithHandler.FaithBauble.EMBLEM) return
		
		if (player.isBurning) player.extinguish()
	}
	
	@SubscribeEvent
	fun leftClick(e: PlayerInteractAdequateEvent.LeftClick) {
		if (e.action == PlayerInteractAdequateEvent.LeftClick.Action.LEFT_CLICK_BLOCK) return
		
		val player = e.player
		val stack = player.heldItem
		
		val emblem = ItemPriestEmblem.getEmblem(3, player) ?: return
		
		// TODO add cooldown
		if (stack == null) {
			if (!ManaItemHandler.requestManaExact(emblem, player, 300, true)) return
			
			val aura = EntityFireAura(player.worldObj, player)
			aura.shoot(player, player.rotationPitch, player.rotationYaw, 0.0f, 0.8f, 1.0f)
			
			if (!player.worldObj.isRemote)
				player.worldObj.spawnEntityInWorld(aura)
				
			for (i in 0..5)
				player.worldObj.playAuxSFXAtEntity(null, 1008, player.posX.mfloor(), player.posY.mfloor(), player.posZ.mfloor(), 0)
		} else if (stack.item === Items.fire_charge) {
			shootFireball(player, stack)
		}
	}
	
	fun shootFireball(player: EntityPlayer, stack: ItemStack) {
		val awakened = getGodPowerLevel(player) >= 6
		val c = if (awakened) 1.5 else 1.0
		val look = player.lookVec
		val ghastBall = EntityLargeFireball(player.worldObj, player, look.xCoord, look.yCoord, look.zCoord)
		
		ghastBall.field_92057_e = if (awakened) 2 else 1
		ghastBall.posX = player.posX + look.xCoord
		ghastBall.posY = player.posY + (player.height / 2.0f).toDouble() + 0.5
		ghastBall.posZ = player.posZ + look.zCoord
		ghastBall.motionX = look.xCoord * c
		ghastBall.motionY = look.yCoord * c
		ghastBall.motionZ = look.zCoord * c
		ghastBall.accelerationX = ghastBall.motionX * 0.1
		ghastBall.accelerationY = ghastBall.motionY * 0.1
		ghastBall.accelerationZ = ghastBall.motionZ * 0.1
		player.worldObj.spawnEntityInWorld(ghastBall)
		player.worldObj.playAuxSFXAtEntity(null, 1008, player.posX.mfloor(), player.posY.mfloor(), player.posZ.mfloor(), 0)
		
		if (!player.capabilities.isCreativeMode)
			--stack.stackSize
	}
	
	@SubscribeEvent
	fun onPlayerHurt(e: LivingAttackEvent) {
		val player = e.entityLiving as? EntityPlayer ?: return
		
		val awakened = getGodPowerLevel(player) >= 6
		
		if ((e.source.isFireDamage && (e.source != DamageSource.lava || awakened) && ItemPriestEmblem.getEmblem(3, player) != null) || (e.source.isExplosion && ItemPriestCloak.getCloak(3, player) != null)) {
			e.isCanceled = true
			
			if (awakened)
				e.entityLiving.heal(0.5f)
		}
	}
	
	override fun getGodPowerLevel(player: EntityPlayer): Int {
		var lvl = 0
		
		// if (ItemPriestCloak.getCloak(3, player) != null) lvl += 3 TODO
		if (ItemPriestEmblem.getEmblem(3, player) != null) lvl += 2
		if (ItemLokiRing.getLokiRing(player) != null) lvl += 1
		if (player.inventory.hasItemStack(ItemStack(AlfheimItems.rodFlameStar))) lvl += 1
		
		return lvl
	}
	
	override fun doParticles(stack: ItemStack, player: EntityPlayer) {
		val color = Color(ColorOverrideHelper.getColor(player, 0xF94407))
		val r = color.red / 255f
		val g = color.green / 255f
		val b = color.blue / 255f
		
		var (x, y, z) = Vector3.fromEntity(player)
		y = if (mc.thePlayer == player) y - player.yOffset else y
		spawnEmblem3(x, y, z, r.D, g.D, b.D)
	}
	
	fun spawnEmblem3(x: Double, y: Double, z: Double, r: Double, g: Double, b: Double) {
		for (i in 1..9) {
			val pos = Vector3(x, y, z).add(0.0, 0.25, 0.0).add(Vector3(0.0, 0.0, 0.5).rotate((Botania.proxy.worldElapsedTicks * 5 % 360 + i * 40.0), Vector3.oY))
			Botania.proxy.sparkleFX(mc.theWorld, pos.x, pos.y, pos.z, r.F, g.F, b.F, 1f, 4)
		}
	}
}
