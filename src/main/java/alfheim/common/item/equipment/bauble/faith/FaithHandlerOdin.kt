package alfheim.common.item.equipment.bauble.faith

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alexsocol.asjlib.security.InteractionSecurity
import alfheim.api.item.ColorOverrideHelper
import alfheim.common.core.handler.*
import alfheim.common.entity.EntityThrownPotion
import alfheim.common.item.AlfheimItems
import alfheim.common.item.equipment.bauble.*
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.entity.*
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.projectile.*
import net.minecraft.item.ItemStack
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.event.entity.living.*
import vazkii.botania.common.Botania
import vazkii.botania.common.item.relic.ItemOdinRing
import java.awt.Color
import java.util.*

object FaithHandlerOdin: IFaithHandler {
	
	val uuid_knock = UUID.fromString("bace7868-73c2-421e-989a-374cb1ffa3dc")
	val mod_knock = AttributeModifier(uuid_knock, "Odin faith modifier", 1.0, 0)
	
	init {
		eventForge()
	}
	
	override fun onEquipped(stack: ItemStack, player: EntityPlayer, type: IFaithHandler.FaithBauble) {
		if (type == IFaithHandler.FaithBauble.CLOAK) player.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).apply { removeModifier(mod_knock); applyModifier(mod_knock) }
	}
	
	override fun onUnequipped(stack: ItemStack, player: EntityPlayer, type: IFaithHandler.FaithBauble) {
		if (type == IFaithHandler.FaithBauble.CLOAK) player.getEntityAttribute(SharedMonsterAttributes.knockbackResistance).removeModifier(mod_knock)
	}
	
	// no potions
	override fun onWornTick(stack: ItemStack, player: EntityPlayer, type: IFaithHandler.FaithBauble) {
		if (type == IFaithHandler.FaithBauble.CLOAK) {
			val potions = player.worldObj.getEntitiesWithinAABB(EntityPotion::class.java, player.boundingBox(6)) as MutableList<EntityThrowable>
			potions.addAll(player.worldObj.getEntitiesWithinAABB(EntityThrownPotion::class.java, player.boundingBox(6)) as List<EntityThrowable>)
			potions.removeAll { it.thrower === player }
			
			if (AlfheimConfigHandler.enableMMO && ASJUtilities.isServer) {
				val pt = CardinalSystem.PartySystem.getParty(player)
				potions.removeAll { pt.isMember(it.thrower) }
			}
			
			potions.forEach {
				if (InteractionSecurity.canDoSomethingWithEntity(player, it))
					it.worldObj.removeEntity(it)
			}
		}
	}
	
	@SubscribeEvent
	fun noPotions(e: EntityJoinWorldEvent) {
		if (e.entity !is EntityPotion && e.entity !is EntityThrownPotion) return
		
		if (e.world.playerEntities.any {
				it as EntityPlayer
				Vector3.entityDistance(e.entity, it) < 6 &&
				ItemPriestCloak.getCloak(5, it) != null &&
				InteractionSecurity.canDoSomethingWithEntity(it, e.entity)
			})
			e.isCanceled = true
	}
	
	@SubscribeEvent
	fun onPlayerAttack(e: LivingHurtEvent) {
		val player = e.source.entity as? EntityPlayer ?: return
		
		if (ItemPriestEmblem.getEmblem(5, player) == null) return
		if (getGodPowerLevel(player) < 3) return
		
		e.ammount *= 1 + (1 - player.health / player.maxHealth) * 0.25f
	}
	
	@SubscribeEvent
	fun onPlayerTargeted(e: LivingSetAttackTargetEvent) {
		val player = e.target as? EntityPlayer ?: return
		
		if (ItemPriestCloak.getCloak(5, player) == null) return
		if (getGodPowerLevel(player) < 5) return
		
		e.entityLiving.entityLivingToAttack = null
		(e.entityLiving as? EntityLiving)?.attackTarget = null
	}
	
	override fun getGodPowerLevel(player: EntityPlayer): Int {
		var lvl = 0
		
		if (player.inventory.hasItemStack(ItemStack(AlfheimItems.gungnir))) lvl += 4
		if (ItemPriestCloak.getCloak(5, player) != null) lvl += 3
		if (ItemPriestEmblem.getEmblem(5, player) != null) lvl += 2
		if (ItemOdinRing.getOdinRing(player) != null) lvl += 1
		if (player.inventory.hasItemStack(ItemStack(AlfheimItems.rodPortal))) lvl += 1
		
		return lvl
	}
	
	override fun doParticles(stack: ItemStack, player: EntityPlayer) {
		val color = Color(ColorOverrideHelper.getColor(player, 0xC00000))
		val r = color.red / 255f
		val g = color.green / 255f
		val b = color.blue / 255f
		
		val (x, y, z) = Vector3.fromEntity(player)
		
		// fuck you minecraft and your rotations!!!
//		val yawOff = ASJRenderHelper.interpolate(player.prevRenderYawOffset.D, player.renderYawOffset.D)
//		val yaw = ASJRenderHelper.interpolate(player.prevRotationYawHead.D, player.rotationYawHead.D) - 270

//		spawnEmblem5(x, y, z, r.D, g.D, b.D, yawOff - yaw)
		
		for (i in 1..9) {
			val pos = Vector3(x, y + 2, z).add(Vector3(0.0, 0.0, 0.5)/*.rotate(yawOff, Vector3.oY.copy().negate()).rotate(yaw, Vector3.oY.copy().negate())*/.rotate(i * 40.0, Vector3.oY))
			Botania.proxy.sparkleFX(mc.theWorld, pos.x, pos.y, pos.z, r.F, g.F, b.F, 1f, 1)
		}
	}
	
	fun spawnEmblem5(x: Double, y: Double, z: Double, r: Double, g: Double, b: Double, yaw: Double) {
		for (i in 1..9) {
			val pos = Vector3(x, y + 1.75, z).add(Vector3(0.0, 0.0, 0.5).rotate(yaw + i * 40.0, Vector3.oY))
			Botania.proxy.sparkleFX(mc.theWorld, pos.x, pos.y, pos.z, r.F, g.F, b.F, 1f, 1)
		}
	}
}