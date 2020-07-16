package alfheim.common.item.equipment.bauble.faith

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alfheim.api.item.ColorOverrideHelper
import alfheim.common.item.AlfheimItems
import alfheim.common.item.equipment.bauble.*
import alfheim.common.item.relic.ItemSifRing
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
import java.awt.Color

object FaithHandlerSif: IFaithHandler {
	
	const val COOLDOWN_PLANT = 15
	const val COOLDOWN_FLOWER = 200
	const val RANGE = 5
	const val TAG_COOLDOWN = "cooldown"
	
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
	
	override fun onWornTick(stack: ItemStack, player: EntityPlayer, type: IFaithHandler.FaithBauble) {
		if (type != IFaithHandler.FaithBauble.EMBLEM) return
		
		val world = player.worldObj
		
		if (!world.isRemote) {
			val cooldown = getInt(stack, TAG_COOLDOWN, 0)
			if (cooldown > 0) setInt(stack, TAG_COOLDOWN, cooldown - 1)
		}
		
		if (getGodPowerLevel(player) < 4) return
		
		if (!ManaItemHandler.requestManaExact(stack, player, 10, false)) return
		val grow = ArrayList<Pair<ChunkCoordinates, IGrowable>>()
		
		if (world.totalWorldTime % 40 == 0L)
			for (x in 0.bidiRange(RANGE))
				for (y in 0.bidiRange(RANGE))
					for (z in 0.bidiRange(RANGE)) {
						val block = world.getBlock(player, x, y, z)
						
						if (block is BlockSapling && block.func_149851_a(world, player.posX.mfloor() + x, player.posY.mfloor() + y, player.posZ.mfloor() + z, world.isRemote))
							grow.add(ChunkCoordinates(player.posX.mfloor() + x, player.posY.mfloor() + y, player.posZ.mfloor() + z) to block)
					}
		
		if (grow.size == 0) return
		
		val pair = grow[world.rand.nextInt(grow.size)]
		
		val (x, y, z) = pair.first
		bonemeal(world, pair.second, x, y, z, player, stack, 10)
	}
	
	@SubscribeEvent
	fun onClick(e: PlayerInteractEvent) {
		val player = e.entityPlayer
		
		if (e.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) return
		val emblem = ItemPriestEmblem.getEmblem(1, player) ?: return
		
		val lvl = getGodPowerLevel(player)
		
		val cooldown = getInt(emblem, TAG_COOLDOWN, 0)
		if (cooldown != 0 || !e.entityPlayer.isSneaking || player.heldItem != null || !ManaItemHandler.requestManaExact(emblem, e.entityPlayer, 50, false)) return
		
		val world = e.world
		val block = world.getBlock(e.x, e.y, e.z)
		
		if (block is IGrowable && block !== Blocks.grass && block.func_149851_a(world, e.x, e.y, e.z, world.isRemote) && bonemeal(world, block, e.x, e.y, e.z, player, emblem, 50)) {
			if (!world.isRemote) setInt(emblem, TAG_COOLDOWN, COOLDOWN_PLANT)
		}
		
		if (lvl >= 6) {
			if (!world.isRemote && block === Blocks.grass && e.face == 1 && world.getBlock(e.x, e.y + 1, e.z).isAir(world, e.x, e.y + 1, e.z) &&
				(!world.provider.hasNoSky || e.y < 255) && ModBlocks.flower.canBlockStay(world, e.x, e.y, e.z) && ManaItemHandler.requestManaExact(emblem, e.entityPlayer, 500, true) &&
				world.setBlock(e.x, e.y + 1, e.z, ModBlocks.flower, world.rand.nextInt(16), 3))
				
				setInt(emblem, TAG_COOLDOWN, COOLDOWN_FLOWER)
		}
	}
	
	fun bonemeal(world: World, block: IGrowable, x: Int, y: Int, z: Int, player: EntityPlayer, stack: ItemStack, cost: Int): Boolean {
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
		
		if (ItemPriestCloak.getCloak(1, player) != null) lvl += 3
		if (ItemPriestEmblem.getEmblem(1, player) != null) lvl += 2
		if (ItemSifRing.getSifRing(player) != null) lvl += 1
		if (player.inventory.hasItemStack(ItemStack(AlfheimItems.rodColorfulSkyDirt))) lvl += 1
		
		return lvl
	}
	
	override fun doParticles(stack: ItemStack, player: EntityPlayer) {
		if (player.ticksExisted % 10 == 0) {
			for (i in 0..6) {
				val xmotion = (Math.random() - 0.5) * 0.15
				val zmotion = (Math.random() - 0.5) * 0.15
				val color = Color(ColorOverrideHelper.getColor(player, 0x964B00))
				val r = color.red.F / 255F
				val g = color.green.F / 255F
				val b = color.blue.F / 255F
				
				spawnEmblem1(player.posX, player.posY - player.yOffset, player.posZ, r.D, g.D, b.D, xmotion, zmotion)
			}
		}
	}
	
	fun spawnEmblem1(x: Double, y: Double, z: Double, r: Double, g: Double, b: Double, motionX: Double, motionZ: Double) {
		Botania.proxy.wispFX(mc.theWorld, x, y, z, r.F, g.F, b.F, Math.random().F * 0.15f + 0.15f, motionX.F, 0.0075f, motionZ.F)
	}
}

fun Int.bidiRange(range: Int) = (this - range)..(this + range)
