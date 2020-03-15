package alfheim.common.item.relic.record

import alexsocol.asjlib.ASJUtilities
import alexsocol.asjlib.render.*
import alfheim.AlfheimCore
import alfheim.api.entity.EnumRace.Companion.glColor
import alfheim.api.item.relic.record.AkashicRecord
import alfheim.client.core.handler.CardinalSystemClient
import alfheim.client.core.util.*
import alfheim.client.render.world.SpellVisualizations
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.util.F
import alfheim.common.network.Message1d
import alfheim.common.spell.tech.SpellTimeStop
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.eventhandler.*
import cpw.mods.fml.common.gameevent.*
import cpw.mods.fml.relauncher.*
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.WorldClient
import net.minecraft.client.renderer.Tessellator
import net.minecraft.entity.player.*
import net.minecraft.item.ItemStack
import net.minecraftforge.client.IRenderHandler
import net.minecraftforge.common.MinecraftForge
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL11.glScaled
import org.lwjgl.opengl.GL12

object AkashicRecordGinnungagap: AkashicRecord("ginnungagap") {
	
	override fun canGet(player: EntityPlayer, stack: ItemStack) = true
	
	override fun apply(player: EntityPlayer, stack: ItemStack): Boolean {
		if (player.dimension != AlfheimConfigHandler.dimensionIDAlfheim) return false
		
		AlfheimCore.network.sendToAll(Message1d(Message1d.m1d.GINNUNGAGAP, 1.0))
		
		GinnungagapHandler.active = true
		GinnungagapHandler.activeTicks = 1200
		
		return true
	}
}

object GinnungagapHandler {
	
	var active = false
	var activeTicks = 0
	
	init {
		FMLCommonHandler.instance().bus().register(this)
		MinecraftForge.EVENT_BUS.register(this)
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onClientTick(e: TickEvent.ClientTickEvent) {
		val world = mc.theWorld
		if (world != null && world.provider.dimensionId == AlfheimConfigHandler.dimensionIDAlfheim) {
			if (active)
				world.provider.skyRenderer = GinnungagapSkyRenderer
			else if (world.provider.skyRenderer === GinnungagapSkyRenderer)
				world.provider.skyRenderer = null
		}
	}
	
	@SubscribeEvent
	fun onServerTick(e: TickEvent.WorldTickEvent) {
		if (e.phase != TickEvent.Phase.START || e.world.provider.dimensionId != AlfheimConfigHandler.dimensionIDAlfheim) return
		
		if (activeTicks > 0) activeTicks--
		else if (activeTicks == 0) {
			active = false
			AlfheimCore.network.sendToAll(Message1d(Message1d.m1d.GINNUNGAGAP, 0.0))
		}
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	fun onPlayerLoggedIn(e: PlayerEvent.PlayerLoggedInEvent) {
		val player = e.player as? EntityPlayerMP ?: return
		
		AlfheimCore.network.sendTo(Message1d(Message1d.m1d.GINNUNGAGAP, if (active) 1.0 else 0.0), player)
	}
}

object GinnungagapSkyRenderer: IRenderHandler() {
	
	override fun render(partialTicks: Float, world: WorldClient, mc: Minecraft) {
	
	}
}