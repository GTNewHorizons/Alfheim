package alfheim.common.core.handler.ragnarok

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.common.achievement.AlfheimAchievements
import alfheim.common.block.tile.*
import alfheim.common.core.asm.hook.AlfheimHookHandler
import alfheim.common.core.handler.VisualEffectHandler
import alfheim.common.item.AlfheimItems
import alfheim.common.item.equipment.bauble.faith.ItemRagnarokEmblem
import alfheim.common.item.equipment.tool.ItemSoulSword
import alfheim.common.item.material.ElvenResourcesMetas
import alfheim.common.network.Message1d
import alfmod.common.item.AlfheimModularItems
import alfmod.common.item.material.EventResourcesMetas
import cpw.mods.fml.common.eventhandler.*
import net.minecraft.entity.passive.EntityVillager
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.world.World
import net.minecraftforge.event.entity.living.LivingDeathEvent
import vazkii.botania.common.item.ModItems

object RagnarokEndHandler {
	
	const val SOUL_COST = 2000
	
	init {
		eventForge()
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	fun endRagnarok(e: LivingDeathEvent) {
		if (!RagnarokHandler.canEndRagnarok()) return
		
		val sacr = e.entityLiving as? EntityVillager ?: return
		if (sacr.isChild) return // suffer more >:)
		if (!sacr.entityData.getBoolean(AlfheimHookHandler.TAG_COCOONED)) return
		
		val killer = e.source.entity as? EntityPlayer ?: return
		if (!killer.hasAchievement(AlfheimAchievements.theEND)) return
		
		ItemRagnarokEmblem.getEmblem(killer) ?: return
		val sword = killer.heldItem ?: return
		if (sword.item !== AlfheimItems.soulSword) return
		val lvl = ItemSoulSword.getLevelP(sword)
		if (lvl < SOUL_COST) return
		
		val (x, y, z) = Vector3.fromEntity(sacr).mf()
		if (!check(killer, x, y, z)) return
		
		ItemSoulSword.setLevelP(sword, lvl - SOUL_COST)
		val world = sacr.worldObj
		for (pl in platforms) {
			val (i, j, k) = pl
			(world.getTileEntity(x + i, y + j, z + k) as? TileItemDisplay ?: continue)[0] = null
		}
		
		RagnarokHandler.ragnarok = false
		AlfheimCore.network.sendToAll(Message1d(Message1d.m1d.RAGNAROK, 1.0))
	}
	
	fun check(player: EntityPlayer, x: Int, y: Int, z: Int): Boolean {
		val world = player.worldObj
		
		if (world.isRemote) return false
		
		if (!SchemaUtils.checkStructure(world, x, y, z, structure, VisualEffectHandler::sendError)) {
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
	
	val structure = SchemaUtils.loadStructure("${ModInfo.MODID}/schemas/ragnarok")
	
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
		AlfheimItems.elvenResource, // Jormungand Scale
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
		ElvenResourcesMetas.JormungandScale,
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
				VisualEffectHandler.sendError(world.provider.dimensionId, x + i, y + j, z + k)
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
				world.setBlock(x + i, y + j + 5, z + k, Blocks.stone)
				VisualEffectHandler.sendError(world.provider.dimensionId, x + i, y + j, z + k)
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
					world.setBlock(x + i, y + 5, z + k, Blocks.stone)
					VisualEffectHandler.sendError(world.provider.dimensionId, x + i, y, z + k)
					return false
				}
			}
		
		return true
	}
}
