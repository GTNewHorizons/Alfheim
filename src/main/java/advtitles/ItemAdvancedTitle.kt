package advtitles

import advtitles.ItemAdvancedTitle.EnumTitle.*
import alfheim.AlfheimCore
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.util.*
import alfheim.common.item.ItemMod
import alfheim.common.network.MessageEffect
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.*
import net.minecraft.block.IGrowable
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.potion.*
import net.minecraft.util.StatCollector
import net.minecraftforge.common.*
import net.minecraftforge.event.entity.living.*
import travellersgear.TravellersGear
import travellersgear.api.*
import vazkii.botania.common.core.helper.ItemNBTHelper

class ItemAdvancedTitle: ItemMod("AdvancedTitle"), ITravellersGear {
	
	companion object {
		const val TAG_TITLE = "title"
		const val TAG_TITLE_PREFIX = "TG.personaltitle.adv."
		
	}
	
	enum class EnumTitle {
		EREBON, SELIA, LUXON, PLUTO, SMITH, CENTURION, UNKINGER, PROTECTOR, SAVER, ARCHMAGE;
		
		companion object {
			fun getTitle(player: EntityPlayer): EnumTitle? {
				return valueOf(TravellersGearAPI.getTitleForPlayer(player)?.substring(TAG_TITLE_PREFIX.length) ?: return null)
			}
		}
	}
	
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
		
		when (EnumTitle.getTitle(player)) {
			EREBON    -> potion(Potion.damageBoost.id, 2)
			SELIA     -> if (player.ticksExisted % 12 == 0) // for isReady
				potion(Potion.regeneration.id, 2)
			LUXON     -> {
				potion(Potion.moveSpeed.id, 2)
				potion(Potion.nightVision.id)
			}
			
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
				if (player.ticksExisted % 50 == 0) // for isReady
					potion(Potion.regeneration.id)
				
				potion(Potion.field_76434_w.id)
				
				if (player.absorptionAmount < 4)
					player.removePotionEffect(Potion.field_76444_x.id)
				
				potion(Potion.field_76444_x.id)
			}
			
			SAVER     -> {
				potion(AlfheimConfigHandler.potionIDButterShield, 4)
				potion(AlfheimConfigHandler.potionIDStoneSkin)
				
				if (player.absorptionAmount < 12)
					player.removePotionEffect(Potion.field_76444_x.id)
				
				for (i in 0..24) {
					val potion = Potion.potionTypes[i] ?: continue
					if (potion.id == Potion.heal.id) continue // too OP
					if (potion.id == Potion.regeneration.id && player.ticksExisted % 12 != 0) continue // for isReady
					
					if (!potion.isBadEffect) potion(potion.id, 2)
				}
			}
			
			ARCHMAGE  -> potion(AlfheimConfigHandler.potionIDOvermage)
		}
	}
	
	lateinit var localPlayer: EntityPlayer
	
	fun potion(id: Int, amp: Int = 0) {
		var pe = localPlayer.getActivePotionEffect(id)
		val state: Int
		
		if (pe == null) {
			pe = PotionEffect(id, 600, amp)
			localPlayer.addPotionEffect(pe)
			state = 1
		} else {
			pe.duration = 600
			pe.amplifier = amp
			state = 0
		}
		
		AlfheimCore.network.sendToAll(MessageEffect(localPlayer, pe).also { it.state = state })
	}
	
	override fun getSlot(stack: ItemStack?) = 3
	override fun onTravelGearUnequip(p0: EntityPlayer?, p1: ItemStack?) = Unit
	override fun onTravelGearEquip(p0: EntityPlayer?, p1: ItemStack?) = Unit
}

object AdvancedTitleHandler {
	
	init {
		MinecraftForge.EVENT_BUS.register(this)
	}
	
	fun getTitle(e: Entity): ItemAdvancedTitle.EnumTitle? {
		return ItemAdvancedTitle.EnumTitle.getTitle(e as? EntityPlayer ?: return null)
	}
	
	@SubscribeEvent
	fun doErebon(e: LivingHurtEvent) {
		if (getTitle(e.source.entity ?: return) === EREBON) {
			e.entityLiving.addPotionEffect(PotionEffect(AlfheimConfigHandler.potionIDBleeding, 60))
		}
	}
	
	@SubscribeEvent
	fun onLivingUpdate(e: LivingEvent.LivingUpdateEvent) {
		when (getTitle(e.entityLiving)) {
			SELIA -> doSelia(e.entityLiving)
			else  -> Unit
		}
	}
	
	fun doSelia(e: EntityLivingBase) {
		val world = e.worldObj
		val x = e.posX.mfloor() + (-4..4).random()
		val y = e.posY.mfloor() + (-4..4).random()
		val z = e.posZ.mfloor() + (-4..4).random()
		val block = world.getBlock(x, y, z)
		
		if (block is IGrowable || block is IPlantable) {
			block.updateTick(world, x, y, z, world.rand)
		}
	}
}
