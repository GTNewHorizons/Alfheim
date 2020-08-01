package alfheim.common.potion

import alexsocol.asjlib.*
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.item.AlfheimItems
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingEvent
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.mana.IManaItem
import vazkii.botania.common.item.ModItems

class PotionManaVoid: PotionAlfheim(AlfheimConfigHandler.potionIDManaVoid, "manaVoid", true, 192) {
	
	init {
		MinecraftForge.EVENT_BUS.register(this)
	}
	
	@SubscribeEvent
	fun onEntityUpdate(event: LivingEvent.LivingUpdateEvent) {
		val e = event.entityLiving
		if (this.hasEffect(e)) {
			if (e is EntityPlayer) {
				val mainInv = e.inventory
				val baublesInv = BotaniaAPI.internalHandler.getBaublesInventory(e)
				val invSize = mainInv.sizeInventory
				var size = invSize
				
				if (baublesInv != null) {
					size = invSize + baublesInv.sizeInventory
				}
				var mana = 1040 // Will drain about 1/4 of a tablet in 5 seconds
				
				for (i in 0..size) {
					val useBaubles = i >= invSize
					val inv = if (useBaubles) baublesInv else mainInv
					
					val slot = i - (if (useBaubles) invSize else 0)
					val stackInSlot = (inv as IInventory)[slot]
					if (stackInSlot != null && stackInSlot.item is IManaItem) {
						val manaItemSlot = stackInSlot.item as IManaItem
						
						val hasMana = manaItemSlot.getMana(stackInSlot)
						
						if (hasMana > mana) {
							manaItemSlot.addMana(stackInSlot, -mana)
							
							if (useBaubles) {
								BotaniaAPI.internalHandler.sendBaubleUpdatePacket(e, slot)
							}
							
							break
						} else if (hasMana == mana) {
							manaItemSlot.addMana(stackInSlot, -manaItemSlot.getMana(stackInSlot))
							
							if (useBaubles) {
								BotaniaAPI.internalHandler.sendBaubleUpdatePacket(e, slot)
							}
							
							break
						}
						
						val rest = manaItemSlot.getMana(stackInSlot)
						
						manaItemSlot.addMana(stackInSlot, -rest)
						
						mana -= rest
					}
				}
				
				for (slot in 0 until invSize) {
					val stackInSlot = mainInv[slot]
					if (stackInSlot != null && stackInSlot.item == ModItems.blackLotus) {
						val wiltStack = ItemStack(AlfheimItems.wiltedLotus, stackInSlot.stackSize, stackInSlot.meta)
						wiltStack.stackTagCompound = stackInSlot.tagCompound
						mainInv[slot] = wiltStack
					}
				}
			}
		}
	}
}
