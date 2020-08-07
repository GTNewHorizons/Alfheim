package alfmod.common.core.handler

import alexsocol.asjlib.*
import alfheim.common.block.AlfheimBlocks
import alfmod.common.entity.*
import alfmod.common.item.AlfheimModularItems
import alfmod.common.item.material.EventResourcesMetas
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.init.Blocks
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.event.world.BlockEvent

object EventHandlerSummer {

	@SubscribeEvent
	fun onPlayerInteracted(e: PlayerInteractEvent) {
		if (!HELLISH_VACATION || e.world.isRemote) return
		
		if (e.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK || e.face != 1) return
		if (e.world.getBlock(e.x, e.y, e.z) !== AlfheimBlocks.alfStorage || e.world.getBlockMetadata(e.x, e.y, e.z) != 2) return
		if (e.entityPlayer.heldItem?.let { it.item === AlfheimModularItems.eventResource && it.meta == EventResourcesMetas.LavaMelon } != true) return
		EntityFireSpirit.startRitual(e.world, e.x, e.y, e.z, e.entityPlayer)
	}
	
	@SubscribeEvent
	fun onBlockBreak(e: BlockEvent.BreakEvent) {
		if (e.player.capabilities.isCreativeMode) return
		
		if (e.block === Blocks.melon_block && e.world.rand.nextInt(10) == 0) {
			val word = e.world.rand.nextInt(10)
			if (word in 1..3 && !e.world.isRemote) ASJUtilities.say(e.player, "alfmodmisc.donteat.$word")
			
			e.isCanceled = true
			e.world.setBlockToAir(e.x, e.y, e.z);
			if (!e.world.isRemote) e.world.spawnEntityInWorld(EntityRollingMelon(e.world).apply { setPosition(e.x + 0.5, e.y + 0.5, e.z + 0.5); onSpawnWithEgg(null) })
		}
	}
}
