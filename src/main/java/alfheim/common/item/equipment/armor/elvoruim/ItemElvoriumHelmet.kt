package alfheim.common.item.equipment.armor.elvoruim

import org.lwjgl.opengl.GL11.*

import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.ItemRenderer
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.potion.Potion
import net.minecraft.potion.PotionEffect
import net.minecraft.util.IIcon
import net.minecraft.util.StatCollector
import net.minecraft.world.World
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingHurtEvent
import vazkii.botania.api.item.IAncientWillContainer
import vazkii.botania.api.mana.IManaGivingItem
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.client.core.helper.IconHelper
import vazkii.botania.common.core.helper.ItemNBTHelper

open class ItemElvoriumHelmet(name: String): ItemElvoriumArmor(0, name), IAncientWillContainer, IManaGivingItem {
	
	constructor(): this("ElvoriumHelmet") {
		MinecraftForge.EVENT_BUS.register(this)
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerIcons(reg: IIconRegister) {
		super.registerIcons(reg)
		willIcon = IconHelper.forName(reg, "willFlame")
	}
	
	override fun onArmorTick(world: World, player: EntityPlayer?, stack: ItemStack?) {
		super.onArmorTick(world, player, stack)
		if (hasArmorSet(player)) {
			val food = player!!.foodStats.foodLevel
			if (food > 0 && food < 18 && player.shouldHeal() && player.ticksExisted % 80 == 0)
				player.heal(1f)
			ManaItemHandler.dispatchManaExact(stack, player, 1, true)
		}
	}
	
	override fun addAncientWill(stack: ItemStack, will: Int) {
		ItemNBTHelper.setBoolean(stack, TAG_ANCIENT_WILL + will, true)
	}
	
	override fun hasAncientWill(stack: ItemStack?, will: Int): Boolean {
		return hasAncientWill_(stack, will)
	}
	
	@SideOnly(Side.CLIENT)
	override fun addArmorSetDescription(stack: ItemStack?, list: List<String>) {
		super.addArmorSetDescription(stack, list)
		for (i in 0..5)
			if (hasAncientWill(stack, i))
				addStringToTooltip(StatCollector.translateToLocal("botania.armorset.will$i.desc"), list)
	}
	
	@SubscribeEvent
	fun onEntityAttacked(event: LivingHurtEvent) {
		val attacker = event.source.entity
		if (attacker is EntityPlayer) {
			if (hasArmorSet(attacker)) {
				val crit = attacker.fallDistance > 0.0f && !attacker.onGround && !attacker.isOnLadder && !attacker.isInWater && !attacker.isPotionActive(Potion.blindness) && attacker.ridingEntity == null
				val stack = attacker.inventory.armorItemInSlot(3)
				if (crit && stack != null && stack.item is ItemElvoriumHelmet) {
					val ahrim = hasAncientWill(stack, 0)
					val dharok = hasAncientWill(stack, 1)
					val guthan = hasAncientWill(stack, 2)
					val torag = hasAncientWill(stack, 3)
					val verac = hasAncientWill(stack, 4)
					val karil = hasAncientWill(stack, 5)
					
					if (ahrim)
						event.entityLiving.addPotionEffect(PotionEffect(Potion.weakness.id, 20, 1))
					if (dharok)
						event.ammount *= 1f + (1f - attacker.health / attacker.maxHealth) * 0.5f
					if (guthan)
						attacker.heal(event.ammount * 0.25f)
					if (torag)
						event.entityLiving.addPotionEffect(PotionEffect(Potion.moveSlowdown.id, 60, 1))
					if (verac)
						event.source.setDamageBypassesArmor()
					if (karil)
						event.entityLiving.addPotionEffect(PotionEffect(Potion.wither.id, 60, 1))
				}
			}
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onPlayerRender(event: RenderPlayerEvent.Specials.Post) {
		if (event.entityLiving.getActivePotionEffect(Potion.invisibility) != null)
			return
		
		val player = event.entityPlayer
		
		val yaw = player.prevRotationYawHead + (player.rotationYawHead - player.prevRotationYawHead) * event.partialRenderTick
		val yawOffset = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * event.partialRenderTick
		val pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * event.partialRenderTick
		
		glPushMatrix()
		glRotatef(yawOffset, 0f, -1f, 0f)
		glRotatef(yaw - 270, 0f, 1f, 0f)
		glRotatef(pitch, 0f, 0f, 1f)
		
		val helm = player.inventory.armorItemInSlot(3)
		if (helm != null && helm.item is ItemElvoriumHelmet)
			ItemElvoriumHelmet.renderOnPlayer(helm, event)
		
		glPopMatrix()
	}
	
	companion object {
		
		private val TAG_ANCIENT_WILL = "AncientWill"
		internal var willIcon: IIcon
		
		fun hasAncientWill_(stack: ItemStack?, will: Int): Boolean {
			return ItemNBTHelper.getBoolean(stack, TAG_ANCIENT_WILL + will, false)
		}
		
		fun hasAnyWill(stack: ItemStack): Boolean {
			for (i in 0..5)
				if (hasAncientWill_(stack, i))
					return true
			
			return false
		}
		
		@SideOnly(Side.CLIENT)
		fun renderOnPlayer(stack: ItemStack, event: RenderPlayerEvent) {
			if (hasAnyWill(stack) && !(stack.item as ItemElvoriumArmor).hasPhantomInk(stack)) {
				glPushMatrix()
				val f = willIcon.minU
				val f1 = willIcon.maxU
				val f2 = willIcon.minV
				val f3 = willIcon.maxV
				vazkii.botania.api.item.IBaubleRender.Helper.translateToHeadLevel(event.entityPlayer)
				Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture)
				glRotated(90.0, 0.0, 1.0, 0.0)
				glRotated(180.0, 1.0, 0.0, 0.0)
				glTranslated(-0.26, 0.15, -0.39)
				glScaled(0.5, 0.5, 0.5)
				ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, willIcon.iconWidth, willIcon.iconHeight, 1f / 16f)
				glPopMatrix()
			}
		}
	}
}
