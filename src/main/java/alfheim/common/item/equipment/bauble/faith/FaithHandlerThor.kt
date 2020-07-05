package alfheim.common.item.equipment.bauble.faith

import alexsocol.asjlib.*
import alfheim.api.item.ColorOverrideHelper
import alfheim.common.item.AlfheimItems
import alfheim.common.item.equipment.bauble.*
import alfheim.common.item.equipment.bauble.faith.IFaithHandler.FaithBauble
import alfheim.common.item.equipment.bauble.faith.IFaithHandler.FaithBauble.EMBLEM
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.entity.*
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.potion.*
import net.minecraft.util.DamageSource
import net.minecraftforge.event.entity.living.LivingHurtEvent
import vazkii.botania.common.Botania
import vazkii.botania.common.core.helper.Vector3
import vazkii.botania.common.item.relic.ItemThorRing
import java.awt.Color
import java.util.*

object FaithHandlerThor: IFaithHandler {
	
	val uuid = UUID.fromString("67d86aaf-e4c5-4f0e-af7e-7e56d1ec9fb0")
	val mod = AttributeModifier(uuid, "Thor faith modifier", 0.2, 1)
	
	init {
		eventForge()
	}
	
	override fun onEquipped(stack: ItemStack, player: EntityPlayer, type: FaithBauble) {
		if (type == EMBLEM) player.getEntityAttribute(SharedMonsterAttributes.movementSpeed).applyModifier(mod)
	}
	
	override fun onUnequipped(stack: ItemStack, player: EntityPlayer, type: FaithBauble) {
		if (type == EMBLEM) player.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(mod)
	}
	
	@SubscribeEvent
	fun powerhit(e: LivingHurtEvent) {
		val player = e.source.entity as? EntityPlayer ?: return
		if (player.heldItem?.item !is ItemAxe && player.heldItem?.item !== AlfheimItems.mjolnir) return
		if (ItemPriestEmblem.getEmblem(0, player) == null) return
		val lvl = getGodPowerLevel(player)
		
		e.entityLiving.addPotionEffect(PotionEffect(Potion.moveSlowdown.id, 20 * lvl, 1))
		e.ammount *= (lvl * 0.25f / 11) + 1f
	}
	
	@SubscribeEvent
	fun fallhit(e: LivingHurtEvent) {
		if (e.source.damageType != DamageSource.fall.damageType) return
		val player = e.entityLiving as? EntityPlayer ?: return
		if (ItemPriestCloak.getCloak(0, player) == null) return
		if (getGodPowerLevel(player) < 7) return
		
		val list = player.worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, getBoundingBox(player.posX, player.posY + 1, player.posZ).expand(5.0, 1.5, 5.0)) as MutableList<EntityLivingBase>
		list.remove(player)
		val dmg = e.ammount / list.size
		for (t in list) if (t.onGround) t.attackEntityFrom(e.source, dmg)
		e.isCanceled = true
	}
	
	override fun getGodPowerLevel(player: EntityPlayer): Int {
		var lvl = 0
		
		if (player.inventory.hasItemStack(ItemStack(AlfheimItems.mjolnir))) lvl += 4
		if (ItemPriestCloak.getCloak(0, player) != null) lvl += 3
		if (ItemPriestEmblem.getEmblem(0, player) != null) lvl += 2
		if (ItemThorRing.getThorRing(player) != null) lvl += 1
		if (player.inventory.hasItemStack(ItemStack(AlfheimItems.rodLightning))) lvl += 1
		
		return lvl
	}
	
	override fun doParticles(stack: ItemStack, player: EntityPlayer) {
		if (player.ticksExisted % 10 == 0) {
			val playerHead = Vector3.fromEntityCenter(player).add(0.0, 0.75, 0.0)
			val playerShift = playerHead.copy().add(getHeadOrientation(player))
			val color = Color(ColorOverrideHelper.getColor(player, 0x0079C4))
			val innerColor = Color(color.rgb).brighter().brighter()
			
			spawnEmblem0(playerHead.x, playerHead.y, playerHead.z, playerShift.x, playerShift.y, playerShift.z, color.rgb.D, innerColor.rgb.D)
		}
	}
	
	fun spawnEmblem0(xs: Double, ys: Double, zs: Double, xe: Double, ye: Double, ze: Double, color: Double, innerColor: Double) {
		Botania.proxy.lightningFX(mc.theWorld, Vector3(xs, ys, zs), Vector3(xe, ye, ze), 2f, color.I, innerColor.I)
	}
}
