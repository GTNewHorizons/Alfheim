package alfheim.common.item.relic

import alexsocol.asjlib.*
import alfheim.common.item.AlfheimItems
import baubles.api.BaubleType
import baubles.common.lib.PlayerHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.entity.EntityAgeable
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.ChunkCoordinates
import net.minecraft.world.biome.BiomeGenBase
import net.minecraftforge.event.entity.living.LivingEvent
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.item.ModItems
import vazkii.botania.common.item.relic.ItemRelicBauble

class ItemSifRing: ItemRelicBauble("SifRing") {
	
	val desertIDs = arrayOf(BiomeGenBase.desert.biomeID, BiomeGenBase.desertHills.biomeID, BiomeGenBase.desert.biomeID + 128, BiomeGenBase.desertHills.biomeID + 128)
	
	init {
		eventForge()
	}
	
	@SubscribeEvent
	fun onPlayerTick(e: LivingEvent.LivingUpdateEvent) {
		val player = e.entityLiving as? EntityPlayer ?: return
		val ring = getSifRing(player) ?: return
		
		reviveCacti(ring, player)
		supplyVineballs(ring, player)
		growAnimals(ring, player)
	}
	
	val list = ArrayList<ChunkCoordinates>()
	
	fun reviveCacti(stack: ItemStack, player: EntityPlayer) {
		if (!ManaItemHandler.requestManaExact(stack, player, 20, true)) return
		
		val world = player.worldObj
		
		for (i in -8..8)
			for (j in -3..3)
				for (k in -8..8)
					if (world.getBiomeGenForCoords(player.posX.mfloor() + i, player.posZ.mfloor() + k).biomeID in desertIDs)
						if (world.getBlock(player, i, j, k) === Blocks.deadbush)
							list.add(ChunkCoordinates(i, j, k))
		
		if (list.isEmpty()) return
		
		val (i, j, k) = list.random()
		world.setBlock(player, Blocks.cactus, i, j, k)
		list.clear()
	}
	
	fun supplyVineballs(stack: ItemStack, player: EntityPlayer) {
		if (player.inventory.hasItem(ModItems.slingshot) && !player.inventory.hasItem(ModItems.vineBall) && ManaItemHandler.requestManaExact(stack, player, 50, true)) {
			player.inventory.addItemStackToInventory(ItemStack(ModItems.vineBall))
		}
	}
	
	fun growAnimals(stack: ItemStack, player: EntityPlayer) {
		val list = player.worldObj.getEntitiesWithinAABB(EntityAgeable::class.java, player.boundingBox(8)) as MutableList<EntityAgeable>
		
		for (e in list) {
			if (!ManaItemHandler.requestManaExact(stack, player, 1, true))
				return
			
			e.growingAge++
		}
	}
	
	override fun getBaubleType(stack: ItemStack?) = BaubleType.RING
	
	companion object {
		
		fun getSifRing(player: EntityPlayer): ItemStack? {
			val baubles = PlayerHandler.getPlayerBaubles(player) ?: return null
			val stack1 = baubles.get(1)
			val stack2 = baubles.get(2)
			return if (isSifRing(stack1)) stack1 else if (isSifRing(stack2)) stack2 else null
		}
		
		private fun isSifRing(stack: ItemStack?): Boolean {
			return stack != null && (stack.item === AlfheimItems.priestRingSif || stack.item === ModItems.aesirRing)
		}
	}
}
