package alfheim.common.core.handler.ragnarok

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alfheim.common.achievement.AlfheimAchievements
import alfheim.common.block.tile.*
import alfheim.common.entity.FakeLightning
import alfheim.common.entity.item.EntityItemImmortal
import alfheim.common.item.AlfheimItems
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.entity.effect.EntityLightningBolt
import net.minecraft.entity.item.EntityItem
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.AxisAlignedBB
import net.minecraft.world.World
import net.minecraftforge.event.entity.EntityStruckByLightningEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent

object RagnarokEmblemCraftHandler {
	
	init {
		eventForge()
	}
	
	@SubscribeEvent
	fun spawnLightningForPendant(e: PlayerInteractEvent) {
		if (e.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) return
		val player = e.entityPlayer
		if (player.heldItem?.item !== AlfheimItems.wiltedLotus || player.heldItem.meta != 1) return
		val tile = e.world.getTileEntity(e.x, e.y, e.z) as? TileAnomaly ?: return
		if (tile.mainSubTile != "Lightning") return
		e.world.addWeatherEffect(EntityLightningBolt(e.world, e.x.D, e.y.D, e.z.D))
	}
	
	val ORE_KEYS = arrayOf(4, 2, 0, 3, 1, 5)
	const val AETHER = -1
	const val WATER = 3362227
	const val AIR = 15132211
	const val FIRE = 10040115
	const val EARTH = 6717491
	const val ORDER = 16777215
	const val VOID = 1710618
	
	@SubscribeEvent
	fun craftPendant(e: EntityStruckByLightningEvent) {
		val entityItem = e.entity
		if (entityItem !is EntityItem || entityItem.entityItem == null || entityItem.entityItem.item !== AlfheimItems.attributionBauble) return
		val world = entityItem.worldObj
		val anomaly = world.getTileEntity(entityItem) as? TileAnomaly ?: return
		if (anomaly.mainSubTile != "Lightning") return
		
		val (x, y, z) = Vector3.fromEntity(entityItem).mf()
		
		val poses = mutableListOf<Vector3>()
		
		for (i in x.bidiRange(5))
			for (j in y.bidiRange(5))
				for (k in z.bidiRange(5)) {
					if (Vector3.pointDistanceSpace(x, y, z, i, j, k) > 25) continue
					val star = world.getTileEntity(i, j, k) as? TileCracklingStar ?: continue
					
					if (star.color == AETHER)
						poses.add(Vector3(i, j, k))
				}
		
		mainLoop@ for (pos in poses) {
			val (path, connections, colors) = walkPath(pos, world, 5)
			if (path.size != 6) continue
			if (connections[5] != path[0]) continue
			if (colors[0] == AETHER &&
				colors[1] == WATER &&
				colors[2] == AIR &&
				colors[3] == FIRE &&
				colors[4] == EARTH &&
				colors[5] == ORDER) {
				for (i in path.indices)
					(checkItem(path, i, world) ?: continue@mainLoop).setDead()
				
				entityItem.setDead()
				val entity = EntityItemImmortal(world, entityItem.posX, entityItem.posY + 1, entityItem.posZ, ItemStack(AlfheimItems.ragnarokEmblem))
				entity.motionY = 1.0
				entity.delayBeforeCanPickup = 30
				world.spawnEntityInWorld(entity)
				val fakeBolt = FakeLightning(world, entityItem.posX, entityItem.posY, entityItem.posZ)
				world.addWeatherEffect(fakeBolt)
				
				for (i in 0.bidiRange(3))
					for (j in 0.bidiRange(3))
						for (k in 0.bidiRange(3))
							if (world.getBlock(entityItem, i, j, k) === Blocks.fire)
								world.setBlock(entityItem, Blocks.air, i, j, k)
				
				for (p in path) {
					val tile = p.getTileEntity(world) as? TileCracklingStar ?: continue
					tile.color = VOID
					tile.pos.set(0, -1, 0)
					tile.markDirty()
				}
				
				// not so ok but no one (me) cares
				world.getClosestPlayer(x + 0.5, y + 0.5, z + 0.5, 16.0)?.triggerAchievement(AlfheimAchievements.ragnarok)
				
				break
			}
		}
	}
	
	fun walkPath(
		start: Vector3, world: World, max: Int, walked: Array<Vector3> = arrayOf(start),
		walkedConnections: Array<Vector3> = arrayOf((start.getTileEntity(world) as TileCracklingStar).pos),
		walkedColors: IntArray = intArrayOf((start.getTileEntity(world) as TileCracklingStar).color),
	)
		: Triple<Array<Vector3>, Array<Vector3>, IntArray> {
		
		if (walked.size > max) return Triple(walked, walkedConnections, walkedColors)
		val tile = start.getTileEntity(world)
		if (tile is TileCracklingStar) {
			val link = tile.pos.copy()
			if (link == Vector3(0, -1, 0)) return Triple(walked, walkedConnections, walkedColors)
			if (link in walked) return Triple(walked, walkedConnections, walkedColors)
			val linked = link.getTileEntity(world)
			if (linked is TileCracklingStar) {
				val linkPos = linked.pos.copy()
				if (linkPos != Vector3(0, -1, 0))
					return walkPath(link, world, max, arrayOf(*walked, link), arrayOf(*walkedConnections, linkPos), intArrayOf(*walkedColors, linked.color))
			}
		}
		return Triple(walked, walkedConnections, walkedColors)
	}
	
	fun Vector3.getTileEntity(world: World): TileEntity? = world.getTileEntity(x.mfloor(), y.mfloor(), z.mfloor())
	
	fun checkItem(path: Array<Vector3>, index: Int, world: World): EntityItem? {
		val (x, y, z) = path[index]
		val items = world.getEntitiesWithinAABB(EntityItem::class.java, AxisAlignedBB.getBoundingBox(x, y, z, x + 1, y + 1, z + 1)) as List<EntityItem>
		return items.firstOrNull { it.entityItem != null && it.entityItem.item === AlfheimItems.priestEmblem && it.entityItem.meta == ORE_KEYS[index] }
	}
}