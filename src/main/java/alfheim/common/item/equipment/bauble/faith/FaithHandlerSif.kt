package alfheim.common.item.equipment.bauble.faith

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alfheim.api.item.ColorOverrideHelper
import alfheim.common.item.AlfheimItems
import alfheim.common.item.equipment.bauble.*
import alfheim.common.item.relic.ItemSifRing
import alfheim.common.security.InteractionSecurity
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.block.*
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.ChunkCoordinates
import net.minecraft.world.World
import net.minecraftforge.event.entity.living.LivingAttackEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.common.Botania
import vazkii.botania.common.block.ModBlocks
import vazkii.botania.common.core.helper.ItemNBTHelper.*
import vazkii.botania.common.item.ModItems
import java.awt.Color

object FaithHandlerSif: IFaithHandler {
	
	const val COOLDOWN_PLANT = 15
	const val COOLDOWN_FLOWER = 200
	const val RANGE = 5
	const val TAG_COOLDOWN = "growth_cooldown"
	
	init {
		eventForge()
	}
	
	@SubscribeEvent
	fun onLivingAttacked(e: LivingAttackEvent) {
		val attacker = e.source.entity as? EntityLivingBase ?: return
		val player = e.entityLiving as? EntityPlayer ?: return
		if (ItemPriestCloak.getCloak(1, player) == null) return
		if (getGodPowerLevel(player) < 4) return
		
		if (Vector3(player.lookVec).angle(Vector3(attacker.lookVec).rotate(180, Vector3.oY)) > Math.PI / 6)
			e.isCanceled = true
	}
	
	val grow = ArrayList<Pair<ChunkCoordinates, IGrowable>>()
	
	override fun onWornTick(stack: ItemStack, player: EntityPlayer, type: IFaithHandler.FaithBauble) {
		if (type != IFaithHandler.FaithBauble.EMBLEM) return
		
		val world = player.worldObj
		
		if (!world.isRemote) {
			val cooldown = getInt(stack, TAG_COOLDOWN, 0)
			if (cooldown > 0) setInt(stack, TAG_COOLDOWN, cooldown - 1)
		}
		
		if (getGodPowerLevel(player) < 4) return
		
		if (!ManaItemHandler.requestManaExact(stack, player, 10, false)) return
		
		if (world.totalWorldTime % 40 == 0L)
			for (x in 0.bidiRange(RANGE))
				for (y in 0.bidiRange(RANGE))
					for (z in 0.bidiRange(RANGE)) {
						val block = world.getBlock(player, x, y, z)
						
						if (block is BlockSapling && block.func_149851_a(world, player.posX.mfloor() + x, player.posY.mfloor() + y, player.posZ.mfloor() + z, world.isRemote))
							grow.add(ChunkCoordinates(player.posX.mfloor() + x, player.posY.mfloor() + y, player.posZ.mfloor() + z) to block)
					}
		
		if (grow.isEmpty()) return
		val pair = grow.random()
		val (x, y, z) = pair.first
		bonemeal(world, pair.second, x, y, z, player, stack, 10)
		
		grow.clear()
	}
	
	@SubscribeEvent
	fun onClick(e: PlayerInteractEvent) {
		val player = e.entityPlayer
		
		if (e.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) return
		val emblem = ItemPriestEmblem.getEmblem(1, player) ?: return
		
		val cooldown = getInt(emblem, TAG_COOLDOWN, 0)
		if (cooldown != 0 || e.entityPlayer.isSneaking || player.heldItem != null || !ManaItemHandler.requestManaExact(emblem, e.entityPlayer, 50, false)) return
		
		if (!InteractionSecurity.canDoSomethingHere(player, e.x, e.y, e.z, e.world))
			return
		
		val world = e.world
		val block = world.getBlock(e.x, e.y, e.z)
		
		if (block is IGrowable && block !== Blocks.grass && block.func_149851_a(world, e.x, e.y, e.z, world.isRemote) && bonemeal(world, block, e.x, e.y, e.z, player, emblem, 50)) {
			if (!world.isRemote && !player.capabilities.isCreativeMode) setInt(emblem, TAG_COOLDOWN, COOLDOWN_PLANT)
		}
		
		val lvl = getGodPowerLevel(player)
		
		if (lvl > 5) {
			if (!world.isRemote && block === Blocks.grass && e.face == 1 && world.getBlock(e.x, e.y + 1, e.z).isAir(world, e.x, e.y + 1, e.z) &&
				(!world.provider.hasNoSky || e.y < 255) && ModBlocks.flower.canBlockStay(world, e.x, e.y + 1, e.z) && ManaItemHandler.requestManaExact(emblem, e.entityPlayer, 500, true) &&
				world.setBlock(e.x, e.y + 1, e.z, ModBlocks.flower, world.rand.nextInt(16), 3))
				
				if (!player.capabilities.isCreativeMode)
					setInt(emblem, TAG_COOLDOWN, COOLDOWN_FLOWER)
		}
	}
	
	fun bonemeal(world: World, block: IGrowable, x: Int, y: Int, z: Int, player: EntityPlayer, stack: ItemStack, cost: Int): Boolean {
		if (!InteractionSecurity.canDoSomethingHere(player, x, y, z, world))
			return false
		
		if (world.isRemote) {
			world.playAuxSFX(2005, x, y, z, 0)
			return true
		} else if (block.func_149852_a(world, world.rand, x, y, z) && ManaItemHandler.requestManaExact(stack, player, cost, true)) {
			block.func_149853_b(world, world.rand, x, y, z)
			world.playSound(x.D, y.D, z.D, "liquid.lavapop", 1f, 0.1f, false)
			return true
		}
		
		return false
	}
	
	override fun getGodPowerLevel(player: EntityPlayer): Int {
		var lvl = 0
		
		if (player.inventory.hasItemStack(ItemStack(ModItems.infiniteFruit))) lvl += 4
		if (ItemPriestCloak.getCloak(1, player) != null) lvl += 3
		if (ItemPriestEmblem.getEmblem(1, player) != null) lvl += 2
		if (ItemSifRing.getSifRing(player) != null) lvl += 1
		if (player.inventory.hasItemStack(ItemStack(AlfheimItems.rodColorfulSkyDirt))) lvl += 1
		
		return lvl
	}
	
	override fun doParticles(stack: ItemStack, player: EntityPlayer) {
		if (player.ticksExisted % 10 == 0) {
			for (i in 0..6) {
				val color = Color(ColorOverrideHelper.getColor(player, 0x964B00))
				val r = color.red.F / 255F
				val g = color.green.F / 255F
				val b = color.blue.F / 255F
				
				val (x, y, z) = Vector3.fromEntity(player)
				val motionX = (Math.random() - 0.5) * 0.15
				val motionZ = (Math.random() - 0.5) * 0.15
				
				Botania.proxy.wispFX(mc.theWorld, x, y, z, r, g, b, Math.random().F * 0.15f + 0.15f, motionX.F, 0.0075f, motionZ.F)
			}
		}
	}
}