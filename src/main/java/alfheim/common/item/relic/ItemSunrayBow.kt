package alfheim.common.item.relic

import alfheim.AlfheimCore
import alfheim.common.entity.*
import com.google.common.collect.Multimap
import com.sun.xml.internal.fastinfoset.stax.events.AttributeBase
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.enchantment.*
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.attributes.AttributeModifier
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.common.MinecraftForge
import org.lwjgl.opengl.GL11.glColor4f
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.item.relic.ItemRelic
import vazkii.botania.common.lib.LibMisc
import java.util.*
import kotlin.math.min

/**
 * @author ExtraMeteorP, CKATEPTb
 */
class ItemSunrayBow: ItemRelic("SunrayBow") {
	
	lateinit var icon: Array<IIcon>
	
	init {
		creativeTab = AlfheimCore.alfheimTab
		MinecraftForge.EVENT_BUS.register(this)
	}
	
	override fun getAttributeModifiers(stack: ItemStack): Multimap<String, AttributeBase> {
		val attrib = super.getAttributeModifiers(stack)
		val uuid = UUID(unlocalizedName.hashCode().toLong(), 0)
		attrib.put(SharedMonsterAttributes.attackDamage.attributeUnlocalizedName, AttributeModifier(uuid, "Weapon modifier", 5.0, 0))
		return attrib as Multimap<String, AttributeBase>
	}
	
	override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack {
		player.setItemInUse(stack, getMaxItemUseDuration(stack))
		return stack
	}
	
	override fun getMaxItemUseDuration(stack: ItemStack?) = 72000
	
	override fun getItemUseAction(stack: ItemStack) = EnumAction.bow
	
	override fun onPlayerStoppedUsing(stack: ItemStack, world: World, player: EntityPlayer, itemInUse: Int) {
		val m = maxDmg / 10
		val i = ((getMaxItemUseDuration(stack) - itemInUse) * chargeVelocityMultiplier).toInt()
		if (i < m) return
		val rank = (i - m) / 5
		if (isRightPlayer(player, stack) && ManaItemHandler.requestManaExactForTool(stack, player, min(maxDmg * 10, maxDmg + rank * 20), true)) {
			val arrow = EntityMagicArrow(world, player)
			arrow.shoot(player, player.rotationPitch, player.rotationYaw, 0.0f, 3.0f, 1.0f)
			arrow.damage = min(maxDmg, m + rank * 2)
			arrow.rotationYaw = player.rotationYaw
			arrow.rotation = MathHelper.wrapAngleTo180_float(-player.rotationYaw + 180)
			val j = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack)
			if (j > 0) arrow.damage = arrow.damage + j * 2
			arrow.life = min(150, 5 + i * 4)
			
			if (!world.isRemote)
				world.spawnEntityInWorld(arrow)
			
			world.playSoundAtEntity(player, "random.bow", 1f, 1f / (Item.itemRand.nextFloat() * 0.4f + 1.2f) + 0.5f)
		}
	}
	
	val chargeVelocityMultiplier: Float
		get() = 0.25f
	
	val maxDmg: Int
		get() = 20
	
	override fun addInformation(stack: ItemStack, player: EntityPlayer, list: MutableList<Any?>, adv: Boolean) {
		list.add(StatCollector.translateToLocalFormatted("${getUnlocalizedNameInefficiently(stack)}.desc", 2 * EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack)))
		list.add("")
		super.addInformation(stack, player, list, adv)
	}
	
	override fun registerIcons(reg: IIconRegister) {
		super.registerIcons(reg)
		icon = Array(4) {
			reg.registerIcon("${LibMisc.MOD_ID}:SunrayBow_${it + 1}")
		}
	}
	
	fun getItemIconForUseDuration(dur: Int) = icon[dur]
	
	override fun getIcon(stack: ItemStack, renderPass: Int, player: EntityPlayer?, usingItem: ItemStack?, useRemaining: Int): IIcon {
		val m = maxDmg / 10
		val j = ((((stack.maxItemUseDuration - useRemaining) * chargeVelocityMultiplier).toInt() - m) / 5) * 2 + m
		
		return if (usingItem == null) {
			itemIcon
		} else if (j >= maxDmg) {
			getItemIconForUseDuration(3)
		} else if (j >= maxDmg/3*2) {
			getItemIconForUseDuration(2)
		} else if (j > maxDmg/3) {
			getItemIconForUseDuration(1)
		} else {
			if (j > 0) getItemIconForUseDuration(0) else itemIcon
		}
	}
	
	@SubscribeEvent
	fun damageOverlay(e: RenderGameOverlayEvent.Post) {
		val mc = Minecraft.getMinecraft()
		if (mc.thePlayer.itemInUse?.item !== this) return
		
		try {
			val m = maxDmg / 10
			val i = ((getMaxItemUseDuration(mc.thePlayer.itemInUse) - mc.thePlayer.itemInUseCount) * chargeVelocityMultiplier).toInt()
			val dmg = if (i < m) 0 else min(maxDmg, m + ((i - m) / 5) * 2)
			val str = "Base dmg: $dmg"
			mc.fontRenderer.drawString(str, e.resolution.scaledWidth / 2 + 16, e.resolution.scaledHeight / 2 - 16, 0xFFFFFF)
			glColor4f(1f, 1f, 1f, 1f)
		} catch (t: Throwable) {}
	}
}
