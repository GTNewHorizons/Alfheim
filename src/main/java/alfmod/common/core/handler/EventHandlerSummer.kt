package alfmod.common.core.handler

import alexsocol.asjlib.meta
import alfheim.common.block.AlfheimBlocks
import alfheim.common.item.AlfheimItems
import alfheim.common.item.material.ElvenResourcesMetas
import alfmod.common.entity.EntityFirespirit
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent

object EventHandlerSummer {

	@SubscribeEvent
	fun onPlayerInteracted(e: PlayerInteractEvent) {
		if (!SUMMER_EVENT || e.world.isRemote) return
		
		if (e.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK || e.face != 1) return
		if (e.world.getBlock(e.x, e.y, e.z) !== AlfheimBlocks.alfStorage || e.world.getBlockMetadata(e.x, e.y, e.z) != 2) return // TODO check if fire can stay
		if (e.entityPlayer.heldItem?.let { it.item === AlfheimItems.elvenResource && it.meta == ElvenResourcesMetas.MuspelheimEssence } != true) return // TODO change sacrifice
		EntityFirespirit.startRitual(e.world, e.x, e.y, e.z, e.entityPlayer)
	}
}
