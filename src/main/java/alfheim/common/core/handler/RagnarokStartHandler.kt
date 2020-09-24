package alfheim.common.core.handler

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alfheim.api.ModInfo
import alfheim.client.render.world.VisualEffectHandlerClient
import alfheim.common.block.tile.*
import alfheim.common.item.AlfheimItems
import alfheim.common.item.material.ElvenResourcesMetas
import alfmod.common.item.AlfheimModularItems
import alfmod.common.item.material.EventResourcesMetas
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World
import vazkii.botania.common.item.ModItems

object RagnarokStartHandler {
	
	fun check(player: EntityPlayer, x: Int, y: Int, z: Int): Boolean {
		val world = player.worldObj
		
		if (world.isRemote) return false
		
		val structure = javaClass.getResourceAsStream("/assets/${ModInfo.MODID}/schemas/ragnarok").use {
			it.readBytes().toString(Charsets.UTF_8)
		}
		
		if (!SchemaUtils.checkStructure(world, x, y, z, structure) { i, j, k ->
				VisualEffectHandler.sendPacket(VisualEffectHandlerClient.VisualEffects.WISP, world.provider.dimensionId, i + 0.5, j + 0.5, k + 0.5, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 5.0, 0.0)
			}) {
			ASJUtilities.say(player, "alfheimmisc.ragnarok.wrongStructure")
			return false
		}
		if (!checkItems(world, x, y, z)) {
			ASJUtilities.say(player, "alfheimmisc.ragnarok.wrongItems")
			return false
		}
		
		if (!checkStars(world, x, y, z)) {
			ASJUtilities.say(player, "alfheimmisc.ragnarok.wrongStars")
			return false
		}
		
		return true
	}
	
	private val platforms = arrayOf(
		arrayOf(0, 0, 0),
		arrayOf(4, 1, 0),
		arrayOf(-4, 1, 0),
		arrayOf(0, 1, 4),
		arrayOf(0, 1, -4),
		arrayOf(5, 2, 5),
		arrayOf(-5, 2, 5),
		arrayOf(-5, 2, -5),
		arrayOf(5, 2, -5)
	)
	
	private val items = arrayOf(
		ModItems.gaiaHead,
		AlfheimItems.wiltedLotus,
		AlfheimItems.rodFlameStar,
		AlfheimModularItems.snowHelmet,
		AlfheimModularItems.volcanoHelmet,
		AlfheimItems.elvenResource, // Fur
		AlfheimModularItems.eventResource, // snow relic
		AlfheimItems.elvenResource, // TODO change to Scale
		AlfheimModularItems.eventResource // volcano relic
	)
	
	private val metas = arrayOf(
		0, // head
		1, // lotus
		0, // rod
		0, // helm snow
		0, // helm volcano
		ElvenResourcesMetas.FenrirFur,
		EventResourcesMetas.SnowRelic,
		ElvenResourcesMetas.GrapeLeaf, // Scale
		EventResourcesMetas.VolcanoRelic
	)
	
	private val colors = arrayOf(
		-1,
		5066061,
		14254131,
		6724057,
		10040115,
		11750873,
		16777215,
		8440858,
		15132211
	)
	
	private val stars = arrayOf(
		arrayOf(
				arrayOf(2, 2),
				arrayOf(2, -2),
				arrayOf(-2, -2),
				arrayOf(-2, 2)
		) to 5079193,
		arrayOf(
				arrayOf(7, 0),
				arrayOf(0, 7),
				arrayOf(-7, 0),
				arrayOf(0, -7)
		) to 1710618)
	
	private fun checkItems(world: World, x: Int, y: Int, z: Int): Boolean {
		for ((id, pl) in platforms.withIndex()) {
			val (i, j, k) = pl
			
			fun check(): Boolean {
				val platform = world.getTileEntity(x + i, y + j, z + k) as? TileItemDisplay ?: return false
				val stack = platform[0] ?: return false
				return stack.item === items[id] && stack.meta == metas[id]
			}
			
			if (!check()) {
				VisualEffectHandler.sendPacket(VisualEffectHandlerClient.VisualEffects.WISP, world.provider.dimensionId, x + i + 0.5, y + j + 0.5, z + k + 0.5, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 5.0, 0.0)
				return false
			}
		}
		
		return true
	}
	
	private fun checkStars(world: World, x: Int, y: Int, z: Int): Boolean {
		val main = Vector3(x, y + 1, z)
		
		for ((id, st) in platforms.withIndex()) {
			val (i, j, k) = st
			
			fun check(): Boolean {
				val star = world.getTileEntity(x + i, y + j + 1, z + k) as? TileCracklingStar ?: return false
				if (star.color != colors[id]) return false
				if (Vector3.fromTileEntity(star) == main) return true
				return star.pos == main
			}
			
			if (!check()) {
				VisualEffectHandler.sendPacket(VisualEffectHandlerClient.VisualEffects.WISP, world.provider.dimensionId, x + i + 0.5, y + j + 0.5, z + k + 0.5, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 5.0, 0.0)
				return false
			}
		}
		
		for (starData in stars)
			for ((id, s) in starData.first.withIndex()) {
				val (i, k) = s
				
				fun check(starData: Pair<Array<Array<Int>>, Int>): Boolean {
					val star = world.getTileEntity(x + i, y, z + k) as? TileCracklingStar ?: return false
					if (star.color != starData.second) return false
					val (a, c) = starData.first[(id + 1) % starData.first.size]
					return star.pos == Vector3(x + a, y, z + c)
				}
				
				if (!check(starData)) {
					VisualEffectHandler.sendPacket(VisualEffectHandlerClient.VisualEffects.WISP, world.provider.dimensionId, x + i + 0.5, y + 0.5, z + k + 0.5, 1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 5.0, 0.0)
					return false
				}
			}
		
		return true
	}
}
