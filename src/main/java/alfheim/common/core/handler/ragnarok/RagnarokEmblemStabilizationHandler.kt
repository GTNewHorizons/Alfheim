package alfheim.common.core.handler.ragnarok

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alfheim.api.ModInfo
import alfheim.common.achievement.AlfheimAchievements
import alfheim.common.block.tile.TileItemDisplay
import alfheim.common.core.handler.*
import alfheim.common.item.AlfheimItems
import alfheim.common.item.equipment.bauble.faith.ItemRagnarokEmblem
import alfheim.common.item.material.ElvenResourcesMetas
import baubles.common.lib.PlayerHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.minecraftforge.event.ServerChatEvent
import vazkii.botania.common.block.tile.TileAltar
import vazkii.botania.common.item.ModItems

object RagnarokEmblemStabilizationHandler {
	
	init {
		eventForge()
	}
	
	val phrase1 = "I enclosed divine power in a vessel"
	val phrase2 = "whose strength is comparable to gods blessed."
	val phrase = "$phrase1 $phrase2"
	
	@SubscribeEvent
	fun onSomeoneSaidSomething(e: ServerChatEvent) {
		if (RagnarokHandler.ragnarok) return
		
		val player = e.player
		if (e.message != AlfheimConfigHandler.chatLimiters.format(phrase)) return
		
		if (!player.hasAchievement(AlfheimAchievements.theEND)) return
		val emblem = ItemRagnarokEmblem.getEmblem(player) ?: return
		
		val (x, y, z) = Vector3.fromEntity(player).mf()
		val world = player.worldObj
		
		if (!canStabilize(world, x, y, z)) return
		
		for ((i, k) in runePositions + mauftriumPositions)
			(world.getTileEntity(x + i, y, z + k) as? TileItemDisplay ?: continue)[0] = null
		
		for ((i, k) in lavaPositions)
			(world.getTileEntity(x + i, y, z + k) as? TileAltar ?: continue).setLava(false)
		
		val souls = ItemNBTHelper.getByteArray(emblem, ItemRagnarokEmblem.TAG_CONSUMED, ByteArray(6))
		if (souls.any { it < 1 }) return
		
		PlayerHandler.getPlayerBaubles(player)[0] = ItemStack(AlfheimItems.aesirEmblem).also { ItemNBTHelper.setBoolean(it, "cleared", true) }
	}
	
	fun canStabilize(world: World, x: Int, y: Int, z: Int): Boolean {
		SchemaUtils.checkStructure(world, x, y, z, structure, VisualEffectHandler::sendError)
		
		for ((id, pos) in runePositions.withIndex()) {
			val (i, k) = pos
			
			fun check(): Boolean {
				val display = world.getTileEntity(x + i, y, z + k) as? TileItemDisplay ?: return false
				return ASJUtilities.isItemStackEqualCrafting(items[id], display[0] ?: return false)
			}
			
			if (!check()) {
				VisualEffectHandler.sendError(world.provider.dimensionId, x + i, y, z + k)
				return false
			}
		}
		
		for ((i, k) in mauftriumPositions) {
			fun check(): Boolean {
				val display = world.getTileEntity(x + i, y, z + k) as? TileItemDisplay ?: return false
				return ASJUtilities.isItemStackEqualCrafting(ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.MauftriumIngot), display[0] ?: return false)
			}
			
			if (!check()) {
				VisualEffectHandler.sendError(world.provider.dimensionId, x + i, y, z + k)
				return false
			}
		}
		
		return true
	}
	
	val structure = SchemaUtils.loadStructure("${ModInfo.MODID}/schemas/StabilizationRitual")
	
	val runePositions = arrayOf(
		arrayOf(-2, 2),
		arrayOf(0, 3),
		arrayOf(2, 2),
		arrayOf(3, 0),
		arrayOf(2, -2),
		arrayOf(0, -3),
		arrayOf(-2, -2),
		arrayOf(-3, 0)
	)
	
	val mauftriumPositions = arrayOf(
		arrayOf(2, 5),
		arrayOf(2, -5),
		arrayOf(-2, -5),
		arrayOf(-2, 5),
		arrayOf(5, 2),
		arrayOf(5, -2),
		arrayOf(-5, -2),
		arrayOf(-5, 2)
	)
	
	val lavaPositions = arrayOf(
		arrayOf(6, 0),
		arrayOf(-6, 0),
		arrayOf(0, 6),
		arrayOf(0, -6),
		arrayOf(4, 4),
		arrayOf(4, -4),
		arrayOf(-4, -4),
		arrayOf(-4, 4)
	)
	
	val items = arrayOf(
		ItemStack(ModItems.rune, 1, 0),
		ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.MuspelheimRune),
		ItemStack(ModItems.rune, 1, 8),
		ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.PrimalRune),
		ItemStack(ModItems.rune, 1, 1),
		ItemStack(AlfheimItems.elvenResource, 1, ElvenResourcesMetas.NiflheimRune),
		ItemStack(ModItems.rune, 1, 13),
		ItemStack(ModItems.rune, 1, 2)
	)
}