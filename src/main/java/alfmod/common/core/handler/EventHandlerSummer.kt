package alfmod.common.core.handler

import alexsocol.asjlib.meta
import alfheim.common.block.AlfheimBlocks
import alfmod.common.entity.EntityFireSpirit
import alfmod.common.item.AlfheimModularItems
import alfmod.common.item.material.EventResourcesMetas
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.event.entity.player.PlayerInteractEvent

object EventHandlerSummer {

	@SubscribeEvent
	fun onPlayerInteracted(e: PlayerInteractEvent) {
		if (!HELLISH_VACATION || e.world.isRemote) return
		
		if (e.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK || e.face != 1) return
		if (e.world.getBlock(e.x, e.y, e.z) !== AlfheimBlocks.alfStorage || e.world.getBlockMetadata(e.x, e.y, e.z) != 2) return
		if (e.entityPlayer.heldItem?.let { it.item === AlfheimModularItems.eventResource && it.meta == EventResourcesMetas.LavaMelon } != true) return
		EntityFireSpirit.startRitual(e.world, e.x, e.y, e.z, e.entityPlayer)
	}
}
