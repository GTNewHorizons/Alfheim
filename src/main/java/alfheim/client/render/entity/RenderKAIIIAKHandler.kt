package alfheim.client.render.entity

import alexsocol.asjlib.render.ASJRenderHelper
import alfheim.api.lib.LibResourceLocations
import alfheim.client.core.util.mc
import alfheim.common.core.helper.ContributorsPrivacyHelper
import alfheim.common.core.util.setSize
import cpw.mods.fml.client.registry.RenderingRegistry
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.eventhandler.*
import cpw.mods.fml.common.gameevent.TickEvent
import cpw.mods.fml.relauncher.*
import net.minecraft.client.model.ModelOcelot
import net.minecraft.client.renderer.EntityRenderer
import net.minecraft.client.renderer.entity.*
import net.minecraft.entity.Entity
import net.minecraft.entity.passive.EntityOcelot
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.living.LivingEvent
import org.lwjgl.opengl.GL11.*

object RenderKAIIIAKHandler {
	
	private fun EntityPlayer.correct() = ContributorsPrivacyHelper.isCorrect(this, "KAIIIAK")
	private fun String.correct() = ContributorsPrivacyHelper.isCorrect(this, "KAIIIAK")
	
	init {
		RenderingRegistry.registerEntityRenderingHandler(EntityKAIIIAK::class.java, RenderKAIIIAK())
		MinecraftForge.EVENT_BUS.register(this)
		FMLCommonHandler.instance().bus().register(this)
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	fun onPlayerPreRender(e: RenderPlayerEvent.Pre) {
		if (e.entityPlayer.correct()) {
			e.isCanceled = true
			
			renderCat(e.entityPlayer)
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	fun onPlayerPreRender(e: RenderPlayerEvent.Post) {
		if (e.entityPlayer.correct())
			e.isCanceled = true
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	fun onPlayerPreRender(e: RenderPlayerEvent.Specials.Pre) {
		if (e.entityPlayer.correct())
			e.isCanceled = true
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	fun onPlayerPreRender(e: RenderPlayerEvent.Specials.Post) {
		if (e.entityPlayer.correct()) {
			e.isCanceled = true
			
			RenderContributors.render(e, e.entityPlayer)
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	fun onPlayerPreRender(e: RenderPlayerEvent.SetArmorModel) {
		if (e.entityPlayer.correct())
			e.isCanceled = true
	}
	
	fun renderCat(player: EntityPlayer) {
		val kaiiiak = EntityKAIIIAK(mc.theWorld)
		kaiiiak.setPositionAndRotation(0.0, 0.0, 0.0, 0f, player.rotationPitch)
		
		kaiiiak.growingAge = -1
		
		kaiiiak.limbSwing = player.limbSwing
		kaiiiak.limbSwingAmount = player.limbSwingAmount
		kaiiiak.prevLimbSwingAmount = player.prevLimbSwingAmount
		
		kaiiiak.swingProgress = player.swingProgress
		kaiiiak.swingProgressInt = player.swingProgressInt
		kaiiiak.prevSwingProgress = player.prevSwingProgress
		kaiiiak.isSwingInProgress = player.isSwingInProgress
		
		kaiiiak.rotationYaw = player.rotationYaw
		kaiiiak.prevRotationYaw = player.prevRotationYaw
		kaiiiak.newRotationYaw = player.newRotationYaw
		kaiiiak.rotationYawHead = 0f // player.rotationYawHead
		kaiiiak.prevRotationYawHead = 0f // player.prevRotationYawHead
		
		kaiiiak.rotationPitch = player.rotationPitch
		kaiiiak.newRotationPitch = player.newRotationPitch
		kaiiiak.prevRotationPitch = player.prevRotationPitch
		
		glPushMatrix()
		glRotatef(-player.rotationYaw, 0f, 1f, 0f)
		ASJRenderHelper.interpolatedTranslation(player)
		val yOff = if (player === mc.thePlayer) -1.62f else 0f
		glTranslatef(0f, yOff + 1.3f, 0f)
		
		if (player.isRiding) {
			glTranslatef(0f, 0.65f, -0.3f)
			kaiiiak.isSitting = true
		} else {
			kaiiiak.isSitting = false
		}
		
		RenderManager.instance.renderEntitySimple(kaiiiak, mc.timer.renderPartialTicks)
		glPopMatrix()
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	fun onLivingUpdate(e: LivingEvent.LivingUpdateEvent) {
		if (e.entityLiving is EntityPlayer && e.entityLiving.commandSenderName.correct()) {
			e.entityLiving.setSize(0.45, 0.45)
			if (!e.entityLiving.worldObj.isRemote)
				(e.entityLiving as EntityPlayer).eyeHeight = 0.45f * 0.92f
		}
	}
	
	@SubscribeEvent
	fun onRenderTick(event: TickEvent.RenderTickEvent) {
		if (event.phase != TickEvent.Phase.START || mc.thePlayer == null || !mc.thePlayer.correct()) return
		
		mc.entityRenderer = RenderEntityViewer
	}
	
	private class EntityKAIIIAK(world: World): EntityOcelot(world)
	
	private class RenderKAIIIAK: RenderOcelot(ModelOcelot(), 0.4f) {
		override fun getEntityTexture(entity: Entity?) = LibResourceLocations.shavermik
		override fun getEntityTexture(entity: EntityOcelot?) = LibResourceLocations.shavermik
	}
	
	private object RenderEntityViewer: EntityRenderer(mc, mc.resourceManager) {
		
		var offset = -1.3
		
		private fun canShiftView(): Boolean {
			return mc.thePlayer != null && !mc.thePlayer.isPlayerSleeping && !mc.thePlayer.isRiding
		}
		
		override fun updateCameraAndRender(partialTicks: Float) {
			if (canShiftView()) {
				val player = mc.thePlayer
				player.posY += offset
				player.lastTickPosY += offset
				player.prevPosY += offset
				val savedHeight = mc.thePlayer.eyeHeight
				mc.thePlayer.eyeHeight = mc.thePlayer.defaultEyeHeight // + offset.F
				super.updateCameraAndRender(partialTicks)
				mc.thePlayer.eyeHeight = savedHeight
				player.posY -= offset
				player.lastTickPosY -= offset
				player.prevPosY -= offset
			} else {
				super.updateCameraAndRender(partialTicks)
			}
		}
		
		override fun getMouseOver(partialTicks: Float) {
			if (canShiftView()) {
				val player = mc.thePlayer
				player.posY += offset
				player.prevPosY += offset
				player.lastTickPosY += offset
				val savedHeight = mc.thePlayer.eyeHeight
				mc.thePlayer.eyeHeight = mc.thePlayer.defaultEyeHeight // + offset.F
				super.getMouseOver(partialTicks)
				mc.thePlayer.eyeHeight = savedHeight
				player.posY -= offset
				player.prevPosY -= offset
				player.lastTickPosY -= offset
			} else {
				super.getMouseOver(partialTicks)
			}
		}
	}
}