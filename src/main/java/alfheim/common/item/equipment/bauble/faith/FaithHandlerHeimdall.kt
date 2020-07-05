package alfheim.common.item.equipment.bauble.faith

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.common.item.equipment.bauble.ItemPriestEmblem
import alfheim.common.item.equipment.bauble.faith.IFaithHandler.FaithBauble.*
import alfheim.common.network.MessageHeimdallBlink
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.potion.*
import net.minecraftforge.fluids.IFluidBlock
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.block.ModBlocks
import vazkii.botania.common.block.tile.TileBifrost
import vazkii.botania.common.item.ModItems
import kotlin.math.abs

object FaithHandlerHeimdall: IFaithHandler {
	
	init {
		eventForge()
	}
	
	override fun onWornTick(stack: ItemStack, player: EntityPlayer, type: IFaithHandler.FaithBauble) {
		when (type) {
			EMBLEM -> onEmblemWornTick(stack, player)
			CLOAK  -> onCloakWornTick(stack, player)
		}
	}
	
	fun onEmblemWornTick(stack: ItemStack, player: EntityPlayer) {
		if (!player.worldObj.isRemote && ManaItemHandler.requestManaExact(stack, player, 1, true))
			player.addPotionEffect(PotionEffect(Potion.nightVision.id, 610, 0))
		
		// TODO test sync
		bifrostPlatform(player, stack)
	}
	
	fun onCloakWornTick(stack: ItemStack, player: EntityPlayer) {
		if (player.worldObj.isRemote && player.isSprinting && player.jumpTicks == 10) {
			val look = player.lookVec
			val dist = 6.0
			val (x, y, z) = Vector3.fromEntity(player).add(Vector3(look).mul(dist))
			
			if (!player.worldObj.getBlock(x.I, y.I, z.I).isNormalCube && !player.worldObj.getBlock(x.I, y.I + 1, z.I).isNormalCube) {
				player.isJumping = false
				AlfheimCore.network.sendToServer(MessageHeimdallBlink())
			}
		}
	}
	
	fun getMotionVec(e: Entity): Vector3 {
		if (e is EntityPlayer) {
			val last = Vector3(e.prevPosX, e.prevPosY, e.prevPosZ)
			val vec = Vector3.fromEntity(e).sub(last)
			if (vec.length() < 10)
				return vec
		}
		
		return Vector3(e.motionX, e.motionY, e.motionZ)
	}
	
//	@SubscribeEvent
//	fun bifrostPlatform(e: LivingUpdateEvent) {
	
	fun bifrostPlatform(player: EntityPlayer, emblem: ItemStack) {
		if (player.capabilities.isFlying) return
		val world = player.worldObj
		if (world.isRemote) return
		
		if (player.heldItem?.item === ModItems.rainbowRod) {
			if (ManaItemHandler.requestManaExact(emblem, player, 10, false)) {
				val motVec = getMotionVec(player)
				val pos = Vector3(player.posX + motVec.x, (player.posY + if (player.isSneaking) -2.99 else -0.99).mfloor(), player.posZ + motVec.z - 1)
				
				if (pos.y < 0 || pos.y >= 256) return
				
				for (i in -2..2)
					for (k in -2..2) {
						if (abs(i) == 2 && abs(k) == 2) continue
						
						val block = world.getBlock(pos.x.I + i, pos.y.I, pos.z.I + k)
						
						if (block.isAir(world, pos.x.I + i, pos.y.I, pos.z.I + k) || block.isReplaceable(world, pos.x.I + i, pos.y.I, pos.z.I + k) || block is IFluidBlock) {
							world.setBlock(pos.x.I + i, pos.y.I, pos.z.I + k, ModBlocks.bifrost)
							
							val tileBifrost = world.getTileEntity(pos.x.I + i, pos.y.I, pos.z.I + k) as TileBifrost
							
							tileBifrost.ticks = 5
							player.fallDistance = 0f
							ManaItemHandler.requestManaExact(emblem, player, 10, true)
						} else if (block == ModBlocks.bifrost) {
							val tileBifrost = world.getTileEntity(pos.x.I + i, pos.y.I, pos.z.I + k) as TileBifrost
							
							if (tileBifrost.ticks < 2) {
								tileBifrost.ticks = 5
								ManaItemHandler.requestManaExact(emblem, player, 10, true)
							}
						}
					}
			}
		}
	}
	
	override fun getGodPowerLevel(player: EntityPlayer): Int {
		var lvl = 0
		
		// if (ItemPriestCloak.getCloak(4, player) != null) lvl += 3 TODO
		if (ItemPriestEmblem.getEmblem(4, player) != null) lvl += 2
		// if (ItemLokiRing.getLokiRing(player) != null) lvl += 1
		if (player.inventory.hasItemStack(ItemStack(ModItems.rainbowRod))) lvl += 1
		
		return lvl
	}
	
	override fun doParticles(stack: ItemStack, player: EntityPlayer) {
		
	}
	
}
