package alfheim.common.item.relic

import alexsocol.asjlib.*
import alfheim.common.item.AlfheimItems
import baubles.api.BaubleType
import baubles.common.lib.PlayerHandler
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.world.biome.BiomeGenBase
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.item.ModItems
import vazkii.botania.common.item.relic.ItemRelicBauble

class ItemSifRing: ItemRelicBauble("SifRing") {
	
	override fun getBaubleType(stack: ItemStack?) = BaubleType.RING
	
	val desertIDs = arrayOf(BiomeGenBase.desert.biomeID, BiomeGenBase.desertHills.biomeID, BiomeGenBase.desert.biomeID + 128, BiomeGenBase.desertHills.biomeID + 128)
	
	override fun onWornTick(stack: ItemStack, player: EntityLivingBase?) {
		super.onWornTick(stack, player)
		if (player !is EntityPlayer) return
		
		reviveCacti(stack, player)
		supplyVineballs(stack, player)
	}
	
	fun reviveCacti(stack: ItemStack, player: EntityPlayer) {
		val world = player.worldObj
		
		if (!ManaItemHandler.requestManaExact(stack, player, 20, false)) return
		
		for (i in -8..8)
			for (j in -3..3)
				for (k in -8..8) {
					if (world.getBiomeGenForCoords(player.posX.mfloor() + i, player.posZ.mfloor() + k).biomeID in desertIDs) {
						if (world.getBlock(player, i, j, k) === Blocks.deadbush) {
							ManaItemHandler.requestManaExact(stack, player, 20, false)
							
							world.setBlock(player, Blocks.cactus, i, j, k)
							return
						}
					}
				}
	}
	
	fun supplyVineballs(stack: ItemStack, player: EntityPlayer) {
		if (player.inventory.hasItem(ModItems.slingshot) && !player.inventory.hasItem(ModItems.vineBall) && ManaItemHandler.requestManaExact(stack, player, 50, true)) {
			player.inventory.addItemStackToInventory(ItemStack(ModItems.vineBall))
		}
	}
	
	companion object {
		
		fun getSifRing(player: EntityPlayer): ItemStack? {
			val baubles = PlayerHandler.getPlayerBaubles(player) ?: return null
			val stack1 = baubles.getStackInSlot(1)
			val stack2 = baubles.getStackInSlot(2)
			return if (isSifRing(stack1)) stack1 else if (isSifRing(stack2)) stack2 else null
		}
		
		private fun isSifRing(stack: ItemStack?): Boolean {
			return stack != null && (stack.item === AlfheimItems.priestRingSif || stack.item === ModItems.aesirRing)
		}
	}
}
