package alfheim.client.gui

import alexsocol.asjlib.*
import alfheim.api.lib.LibResourceLocations
import alfheim.client.core.handler.CardinalSystemClient
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.item.relic.ItemHeimdallRing
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.client.event.RenderGameOverlayEvent
import vazkii.botania.common.core.helper.ItemNBTHelper

class GUIAggro: GUIIceLens() {
	
	override fun check(event: RenderGameOverlayEvent.Post): Boolean {
		return event.type != RenderGameOverlayEvent.ElementType.HELMET || !(mc.thePlayer?.let { ItemHeimdallRing.getHeimdallRing(it) }?.let { ItemNBTHelper.getInt(it, ItemHeimdallRing.TAG_AGGRO, 0) > 0 || lookForPlayer() } == true)
	}
	
	fun lookForPlayer(): Boolean {
		val seq = mc.theWorld.playerEntities.asSequence().filter { ASJUtilities.getMouseOver(it as EntityPlayer, 64.0, false)?.entityHit == mc.thePlayer }
		if (AlfheimConfigHandler.enableMMO) seq.filter { CardinalSystemClient.PlayerSegmentClient.party?.isMember(it as EntityPlayer) != true }
		return seq.any()
	}
	
	override fun getTexture() = LibResourceLocations.aggro
}
