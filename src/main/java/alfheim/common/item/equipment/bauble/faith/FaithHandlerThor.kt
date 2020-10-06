	package alfheim.common.item.equipment.bauble.faith

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alfheim.api.item.ColorOverrideHelper
import alfheim.client.render.world.VisualEffectHandlerClient
import alfheim.common.core.handler.*
import alfheim.common.item.AlfheimItems
import alfheim.common.item.equipment.bauble.*
import alfheim.common.item.equipment.bauble.faith.IFaithHandler.FaithBauble
import alfheim.common.item.equipment.bauble.faith.IFaithHandler.FaithBauble.*
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.entity.*
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.potion.*
import net.minecraft.util.DamageSource
import net.minecraftforge.event.entity.living.*
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.Botania
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.item.relic.ItemThorRing
import java.awt.Color
import java.util.*
import vazkii.botania.common.core.helper.Vector3 as Bector3

object 	FaithHandlerThor: IFaithHandler {
	
	val uuid = UUID.fromString("67d86aaf-e4c5-4f0e-af7e-7e56d1ec9fb0")
	val mod = AttributeModifier(uuid, "Thor faith modifier", 0.2, 1)
	
	private const val TAG_COOLDOWN = "lightning_cooldown"
	
	private var ItemStack.cooldown
		get() = ItemNBTHelper.getInt(this, TAG_COOLDOWN, 0)
		set(value) = ItemNBTHelper.setInt(this, TAG_COOLDOWN, value)
	
	init {
		eventForge()
	}
	
	override fun onEquipped(stack: ItemStack, player: EntityPlayer, type: FaithBauble) {
		if (type == EMBLEM) player.getEntityAttribute(SharedMonsterAttributes.movementSpeed).applyModifier(mod)
	}
	
	override fun onUnequipped(stack: ItemStack, player: EntityPlayer, type: FaithBauble) {
		if (type == EMBLEM) player.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(mod)
	}
	
	override fun onWornTick(stack: ItemStack, player: EntityPlayer, type: FaithBauble) {
		if (stack.cooldown > 0 && ManaItemHandler.requestManaExact(stack, player, 1, true))
			stack.cooldown--
		
		if (type == CLOAK && stack.cooldown <= 0 && player.health < (player.maxHealth * 0.5f)) {
			player.addPotionEffect(PotionEffect(AlfheimConfigHandler.potionIDLightningShield, getGodPowerLevel(player) * 20))
			stack.cooldown = 1200
		}
	}
	
	@SubscribeEvent
	fun powerhit(e: LivingHurtEvent) {
		val player = e.source.entity as? EntityPlayer ?: return
		if (player.heldItem?.item !is ItemAxe && player.heldItem?.item !== AlfheimItems.mjolnir) return
		if (ItemPriestEmblem.getEmblem(0, player) == null) return
		val lvl = getGodPowerLevel(player)
		
		e.entityLiving.addPotionEffect(PotionEffect(Potion.moveSlowdown.id, 20 * lvl, 1))
		e.ammount *= (lvl * 0.25f / 12) + 1f
		
		traceLightning(player, e.entityLiving)
	}
	
	fun traceLightning(from: Entity, to: Entity) {
		if (ASJUtilities.isServer) {
			val (x, y, z) = Vector3.fromEntityCenter(from)
			val (x2, y2, z2) = Vector3.fromEntityCenter(to)
			val color = if (from is EntityPlayer) ColorOverrideHelper.getColor(from, 0x0079C4) else 0x0079C4
			val innerColor = Color(color).brighter().brighter().rgb
			VisualEffectHandler.sendPacket(VisualEffectHandlerClient.VisualEffects.LIGHTNING, from.dimension, x, y, z, x2, y2, z2, 1.0, color.D, innerColor.D)
		}
	}
	
	@SubscribeEvent(receiveCanceled = true)
	fun fallhit(e: LivingAttackEvent) {
		if (e.source.damageType != DamageSource.fall.damageType) return
		val player = e.entityLiving as? EntityPlayer ?: return
		val cloak = ItemPriestCloak.getCloak(0, player) ?: return
		if (getGodPowerLevel(player) < 7) return
		
		if (ManaItemHandler.requestManaExact(cloak, player, e.ammount.I, true)) return
		
		val list = player.worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, getBoundingBox(player.posX, player.posY + 1, player.posZ).expand(5.0, 1.5, 5.0)) as MutableList<EntityLivingBase>
		list.remove(player)
		val dmg = e.ammount / list.size
		for (t in list) if (t.onGround) t.attackEntityFrom(e.source, dmg)
		e.isCanceled = true
	}
	
	override fun getGodPowerLevel(player: EntityPlayer): Int {
		var lvl = 0
		
		if (player.inventory.hasItemStack(ItemStack(AlfheimItems.mjolnir))) lvl += 4
		if (ItemPriestCloak.getCloak(0, player) != null) {
			lvl += 3
			if (player.worldObj.canLightningStrikeAt(player.posX.mfloor(), player.posY.mfloor(), player.posZ.mfloor()))
				lvl += 1
		}
		if (ItemPriestEmblem.getEmblem(0, player) != null) lvl += 2
		if (ItemThorRing.getThorRing(player) != null) lvl += 1
		if (player.inventory.hasItemStack(ItemStack(AlfheimItems.rodLightning))) lvl += 1
		
		return lvl
	}
	
	override fun doParticles(stack: ItemStack, player: EntityPlayer) {
		if (player.ticksExisted % 10 == 0) {
			val playerHead = Bector3.fromEntityCenter(player).add(0.0, 0.75, 0.0)
			val (sx, sy, sz) = IFaithHandler.getHeadOrientation(player)
			val playerShift = playerHead.copy().add(sx, sy, sz)
			val color = ColorOverrideHelper.getColor(player, 0x0079C4)
			
			Botania.proxy.lightningFX(mc.theWorld, playerHead, playerShift, 2f, color, Color(color).brighter().brighter().rgb)
		}
	}
}
