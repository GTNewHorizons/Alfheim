package advtitles.items

import advtitles.items.ItemAdvancedTitle.Companion.TAG_TITLE_PREFIX
import advtitles.items.ItemAdvancedTitle.Titles.*
import alfheim.AlfheimCore
import alfheim.client.core.util.mc
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.util.*
import alfheim.common.item.ItemMod
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.*
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.potion.*
import net.minecraft.util.StatCollector
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.common.MinecraftForge
import travellersgear.TravellersGear
import travellersgear.api.*
import vazkii.botania.common.Botania
import vazkii.botania.common.core.helper.ItemNBTHelper
import java.awt.Color

class ItemAdvancedTitle: ItemMod("AdvancedTitle"), ITravellersGear {
	
	companion object {
		const val TAG_TITLE = "title"
		const val TAG_TITLE_PREFIX = "TG.personaltitle.adv."
	}
	
	enum class Titles { EREBON, SELIA, LUXON, PLUTO, SMITH, CENTURION, UNKINGER, PROTECTOR, SAVER, ARCHMAGE }
	
	init {
		creativeTab = TravellersGear.creativeTab
		maxStackSize = 1
		
		AdvancedTitleHandler
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerIcons(reg: IIconRegister) {
		itemIcon = reg.registerIcon("advtitles:AdvancedTitle")
	}
	
	override fun getSubItems(item: Item, tab: CreativeTabs?, list: MutableList<Any?>) {
		values().forEach { list.add(ItemStack(item).also { stack -> ItemNBTHelper.setString(stack, TAG_TITLE, "$TAG_TITLE_PREFIX$it") }) }
	}
	
	override fun addInformation(stack: ItemStack, player: EntityPlayer, list: MutableList<Any?>, adv: Boolean) {
		list.add(StatCollector.translateToLocal(ItemNBTHelper.getString(stack, TAG_TITLE, "")))
	}
	
	override fun onTravelGearTick(player: EntityPlayer, stack: ItemStack) {
		localPlayer = player
		
		maxStackSize = 1
		
		when (valueOf(ItemNBTHelper.getString(stack, TAG_TITLE, "").substring(TAG_TITLE_PREFIX.length))) {
			EREBON    -> potion(Potion.damageBoost.id, 2)
			SELIA     -> potion(Potion.regeneration.id, 2)
			LUXON     -> potion(Potion.moveSpeed.id, 2)
			PLUTO     -> {
				potion(Potion.digSpeed.id, 3)
				potion(AlfheimConfigHandler.potionIDGoldRush)
			}
			SMITH     -> potion(AlfheimConfigHandler.potionIDTank)
			CENTURION -> {
				val eff = player.getActivePotionEffect(AlfheimConfigHandler.potionIDNineLifes)
				if (eff != null) {
					if (eff.amplifier != 0) potion(AlfheimConfigHandler.potionIDNineLifes, 4)
				} else {
					potion(AlfheimConfigHandler.potionIDNineLifes, 4)
				}
				
				potion(Potion.damageBoost.id)
				potion(Potion.digSlowdown.id)
				potion(Potion.moveSlowdown.id)
				potion(Potion.resistance.id)
			}
			UNKINGER  -> potion(AlfheimConfigHandler.potionIDNinja)
			PROTECTOR -> {
				potion(Potion.regeneration.id)
				potion(Potion.field_76434_w.id)
				potion(Potion.field_76444_x.id)
			}
			SAVER     -> {
				potion(AlfheimConfigHandler.potionIDButterShield, 4)
				potion(AlfheimConfigHandler.potionIDStoneSkin)
				
				for (i in 0..24) {
					val potion = Potion.potionTypes[i] ?: continue
					if (!potion.isBadEffect) potion(potion.id, 2)
				}
			}
			ARCHMAGE  -> potion(AlfheimConfigHandler.potionIDOvermage)
		}
	}
	
	lateinit var localPlayer: EntityPlayer
	
	fun potion(id: Int, amp: Int = 0) {
		localPlayer.addPotionEffect(PotionEffect(id, 200, amp))
	}
	
	override fun getSlot(stack: ItemStack?) = 3
	override fun onTravelGearUnequip(p0: EntityPlayer?, p1: ItemStack?) = Unit
	override fun onTravelGearEquip(p0: EntityPlayer?, p1: ItemStack?) = Unit
}

object AdvancedTitleHandler {
	
	init {
		FMLCommonHandler.instance().bus().register(this)
		MinecraftForge.EVENT_BUS.register(this)
	}
	
	@SubscribeEvent
	fun omPlayerRender(e: RenderPlayerEvent.Specials.Post) {
		val player = e.entityPlayer
		val world = player.worldObj
		
		// if (player.isInvisible || player.isInvisibleToPlayer(mc.thePlayer) || player.isPotionActive(Potion.invisibility)) return
		if (player == mc.thePlayer && mc.gameSettings.thirdPersonView == 0) return
		
		when (valueOf(TravellersGearAPI.getTitleForPlayer(player).substring(TAG_TITLE_PREFIX.length))) {
			EREBON    -> {
				val x = player.posX + Math.random() - 0.5
				val y = player.posY + Math.random()
				val z = player.posZ + Math.random() - 0.5
//				for (i in 0..4)
//					Botania.proxy.sparkleFX(world, x, y, z, 0.5f, 1f, 0f, 2f, 100, true)
				
				AlfheimCore.proxy.bloodFX(world, x, y, z, 20)
			}
			SELIA     -> {
				AlfheimCore.proxy.featherFX(world, player.posX + Math.random() - 0.5, player.posY + Math.random(), player.posZ + Math.random() - 0.5, Color(0x008800).rgb)
			}
			LUXON     -> {
				Botania.proxy.wispFX(world, player.posX + Math.random() - 0.5, player.posY + Math.random(), player.posZ + Math.random() - 0.5, 0f, (Math.random() * 0.1 + 0.9).F, 1f, 1f)
			}
			PLUTO     -> {
				val x = player.posX + Math.random() - 0.5
				val y = player.posY + Math.random()
				val z = player.posZ + Math.random() - 0.5
				for (i in 0..4)
					Botania.proxy.sparkleFX(world, x, y, z, 1f, 0.8f, 0f, 2f, 100, true)
			}
			SMITH     -> Unit
			CENTURION -> {
				val color = Color(0xe9a6ff)
				val r = color.red / 255f
				val g = color.green / 255f
				val b = color.blue / 255f
				
				Botania.proxy.wispFX(world, player.posX + Math.random() - 0.5, player.posY + Math.random() * 2 + 0.5, player.posZ + Math.random() - 0.5, r, g, b, 0.3f)
				Botania.proxy.wispFX(world, player.posX + Math.random() - 0.5, player.posY + Math.random() * 2 + 0.5, player.posZ + Math.random() - 0.5, 0f, 1f, 0f, 0.3f)
			}
			UNKINGER  -> {
				Botania.proxy.wispFX(world, player.posX + Math.random() - 0.5, player.posY + Math.random() * 2 + 0.5, player.posZ + Math.random() - 0.5, 1f, 0.5f, 0f, 0.3f)
				Botania.proxy.wispFX(world, player.posX + Math.random() - 0.5, player.posY + Math.random() * 2 + 0.5, player.posZ + Math.random() - 0.5, 1f, 1f, 1f, 0.3f)
			}
			PROTECTOR -> {
				Botania.proxy.wispFX(world, player.posX + Math.random() - 0.5, player.posY + Math.random() * 2 + 0.5, player.posZ + Math.random() - 0.5, 0f, 1f, 0f, 0.3f)
				Botania.proxy.wispFX(world, player.posX + Math.random() - 0.5, player.posY + Math.random() * 2 + 0.5, player.posZ + Math.random() - 0.5, 1f, 0.8f, 0f, 0.3f)
			}
			SAVER     -> {
				val color = Color.getHSBColor(Botania.proxy.worldElapsedTicks * 2 % 360f / 360f, 1f, 1f)
				val r = color.red / 255f
				val g = color.green / 255f
				val b = color.blue / 255f
				
				Botania.proxy.wispFX(world, player.posX + Math.random() - 0.5, player.posY + Math.random() * 2 + 0.5, player.posZ + Math.random() - 0.5, r, g, b, 1f)
			}
			ARCHMAGE  -> {
				Botania.proxy.wispFX(world, player.posX + Math.random() - 0.5, player.posY + Math.random() * 2 + 0.5, player.posZ + Math.random() - 0.5, 0.5f, 0f, 1f, 0.3f)
				Botania.proxy.wispFX(world, player.posX + Math.random() - 0.5, player.posY + Math.random() * 2 + 0.5, player.posZ + Math.random() - 0.5, 0f, 0.8f, 1f, 0.3f)
			}
		}
	}
}
