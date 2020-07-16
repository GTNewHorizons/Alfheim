package alfheim.common.item.equipment.bauble.faith

import alexsocol.asjlib.*
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
import net.minecraftforge.event.entity.living.*
import vazkii.botania.common.item.relic.ItemOdinRing
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
	
	override fun onWornTick(stack: ItemStack, player: EntityPlayer, type: IFaithHandler.FaithBauble) {
		if (type == IFaithHandler.FaithBauble.CLOAK) {
			val potions = player.worldObj.getEntitiesWithinAABB(EntityPotion::class.java, player.boundingBox(6)) as MutableList<EntityThrowable>
			potions.addAll(player.worldObj.getEntitiesWithinAABB(EntityThrownPotion::class.java, player.boundingBox(6)) as List<EntityThrowable> )
			potions.removeAll { it.thrower === player }
			
			if (AlfheimConfigHandler.enableMMO) {
				val pt = CardinalSystem.PartySystem.getParty(player)
				potions.removeAll { pt.isMember(it.thrower) }
			}
			
			potions.forEach { it.worldObj.removeEntity(it) }
		}
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

//		if (player.inventory.hasItemStack(ItemStack(AlfheimItems.gungnir))) lvl += 4 TODO
		if (ItemPriestCloak.getCloak(5, player) != null) lvl += 3
		if (ItemPriestEmblem.getEmblem(5, player) != null) lvl += 2
		if (ItemOdinRing.getOdinRing(player) != null) lvl += 1
		if (player.inventory.hasItemStack(ItemStack(AlfheimItems.rodPortal))) lvl += 1
		
		return lvl
	}
	
	override fun doParticles(stack: ItemStack, player: EntityPlayer) {
		// TODO
	}
	
}
