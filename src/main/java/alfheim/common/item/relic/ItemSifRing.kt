package alfheim.common.item.relic

import alexsocol.asjlib.*
import alfheim.common.item.AlfheimItems
import alfheim.common.security.InteractionSecurity
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
		
		for (x in -8..8)
			for (y in -3..3)
				for (z in -8..8)
					if (world.getBiomeGenForCoords(player.posX.mfloor() + x, player.posZ.mfloor() + z).biomeID in desertIDs)
						if (world.getBlock(player, x, y, z) === Blocks.deadbush)
							list.add(ChunkCoordinates(x, y, z))
		
		val (x, y, z) = list.firstOrNull {
			val (x, y, z) = it
			InteractionSecurity.canDoSomethingHere(player, x, y, z)
		} ?: return
		world.setBlock(player, Blocks.cactus, x, y, z)
		list.clear()
	}
	
	fun supplyVineballs(stack: ItemStack, player: EntityPlayer) {
		if (player.heldItem?.item === ModItems.slingshot && !player.inventory.hasItem(ModItems.vineBall) && ManaItemHandler.requestManaExact(stack, player, 50, true)) {
			player.inventory.addItemStackToInventory(ItemStack(ModItems.vineBall))
		}
	}
	
	fun growAnimals(stack: ItemStack, player: EntityPlayer) {
		val list = player.worldObj.getEntitiesWithinAABB(EntityAgeable::class.java, player.boundingBox(8)) as MutableList<EntityAgeable>
		
		for (e in list) {
			if (!InteractionSecurity.canDoSomethingWithEntity(player, e))
				return
				
			if (!ManaItemHandler.requestManaExact(stack, player, 1, true))
				return
			
			e.growingAge++
		}
	}
	
	override fun getBaubleType(stack: ItemStack?) = BaubleType.RING
	
	companion object {
		
		fun getSifRing(player: EntityPlayer): ItemStack? {
			val baubles = PlayerHandler.getPlayerBaubles(player)
			val stack1 = baubles[1]
			val stack2 = baubles[2]
			return if (isSifRing(stack1)) stack1 else if (isSifRing(stack2)) stack2 else null
		}
		
		private fun isSifRing(stack: ItemStack?): Boolean {
			return stack != null && (stack.item === AlfheimItems.priestRingSif || stack.item === ModItems.aesirRing)
		}
	}
}
