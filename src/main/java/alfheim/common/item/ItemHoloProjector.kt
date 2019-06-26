package alfheim.common.item

import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.api.entity.EnumRace
import alfheim.api.lib.LibResourceLocations
import alfheim.client.model.entity.ModelBipedNew
import alfheim.common.core.command.CommandRace
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.*
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.*
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.entity.*
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraftforge.client.event.*
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType
import net.minecraftforge.common.MinecraftForge
import vazkii.botania.client.core.handler.ClientTickHandler
import vazkii.botania.common.core.helper.ItemNBTHelper

import org.lwjgl.opengl.GL11.*

class ItemHoloProjector: Item() {
	init {
		creativeTab = AlfheimCore.alfheimTab
		setTextureName(ModInfo.MODID + ":HoloProjector")
		unlocalizedName = "HoloProjector"
		MinecraftForge.EVENT_BUS.register(this)
	}
	
	// Shift+RMB - select race; RMB - rotate clockwise
	override fun onItemRightClick(stack: ItemStack, world: World?, player: EntityPlayer?): ItemStack {
		val seg = getSegmentLookedAt(stack, player)
		
		if (player!!.isSneaking && !world!!.isRemote)
			CommandRace().processCommand(player, arrayOf(EnumRace.values()[seg + 1].toString()))
		else {
			val pos = getModel(stack, seg)
			setModel(stack, seg, (pos.rotation + 20) % 360, pos.isMale)
		}
		return stack
	}
	
	// Shift+LMB - switch gender; LMB - rotate anti-clockwise
	override fun onEntitySwing(player: EntityLivingBase?, stack: ItemStack?): Boolean {
		if (player is EntityPlayer) {
			val seg = getSegmentLookedAt(stack, player)
			val pos = getModel(stack, seg)
			if (player.isSneaking)
				setModel(stack, seg, pos.rotation, !pos.isMale)
			else
				setModel(stack, seg, (pos.rotation - 20) % 360, pos.isMale)
			return false
		}
		
		return false
	}
	
	override fun onUpdate(stack: ItemStack?, world: World?, entity: Entity?, pos: Int, equipped: Boolean) {
		super.onUpdate(stack, world, entity, pos, equipped)
		val eqLastTick = wasEquipped(stack)
		val firstTick = isFirstTick(stack)
		if (eqLastTick != equipped) setEquipped(stack, equipped)
		
		if ((!equipped || firstTick) && entity is EntityLivingBase) {
			val angles = 360
			val segAngles = angles / RACES
			val shift = (segAngles / 2).toFloat()
			if (firstTick) tickFirst(stack)
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onRenderWorldLast(event: RenderWorldLastEvent) {
		val player = Minecraft.getMinecraft().thePlayer
		val stack = player.currentEquippedItem
		if (stack != null && stack.item === this) render(stack, player, event.partialTicks)
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onDrawScreenPost(e: RenderGameOverlayEvent.Post) {
		if (e.type != ElementType.ALL) return
		val player = Minecraft.getMinecraft().thePlayer
		val stack = player.currentEquippedItem
		if (stack != null && stack.item === this) renderHUD(e.resolution, player, stack)
	}
	
	@SideOnly(Side.CLIENT)
	fun render(stack: ItemStack, player: EntityPlayer, partialTicks: Float) {
		val mc = Minecraft.getMinecraft()
		val tess = Tessellator.instance
		Tessellator.renderingWorldRenderer = false
		
		glPushMatrix()
		glDisable(GL_CULL_FACE)
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		val alpha = (Math.sin(((ClientTickHandler.ticksInGame + partialTicks) * 0.2f).toDouble()).toFloat() * 0.5f + 0.5f) * 0.4f + 0.3f
		
		val posX = player.prevPosX + (player.posX - player.prevPosX) * partialTicks
		val posY = player.prevPosY + (player.posY - player.prevPosY) * partialTicks
		val posZ = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks
		
		glTranslated(posX - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ)
		
		val angles = 360
		val segAngles = angles / RACES
		val shift = (360 - segAngles / 2).toFloat()
		
		val u = 1f
		val v = 0.25f
		
		val s = 3f
		val m = 0.8f
		val y = v * s * 2f
		var y0 = 0f
		
		val segmentLookedAt = getSegmentLookedAt(stack, player)
		
		for (seg in 0 until RACES) {
			var inside = false
			val rotationAngle = (seg + 0.5f) * segAngles + shift
			if (segmentLookedAt == seg) inside = true
			
			glPushMatrix()
			glRotatef(rotationAngle, 0f, 1f, 0f)
			glTranslatef(s * m, -0.75f, 0f)
			glColor4f(1f, 1f, 1f, 1f)
			
			val pos = getModel(stack, seg)
			mc.renderEngine.bindTexture(if (pos.isMale) LibResourceLocations.male[seg] else LibResourceLocations.female[seg])
			glScaled(0.75, 0.75, 0.75)
			glRotated(180.0, 1.0, 0.0, 0.0)
			glRotated((90 + pos.rotation).toDouble(), 0.0, 1.0, 0.0)
			glTranslated(0.0, -0.75, 0.0)
			ModelBipedNew.model.render(0.0625f)
			glPopMatrix()
			
			glPushMatrix()
			glRotatef(180f, 1f, 0f, 0f)
			if (inside) {
				y0 = -y
			}
			
			EnumRace.glColor((seg + 1).toDouble())
			
			mc.renderEngine.bindTexture(LibResourceLocations.glow)
			tess.startDrawingQuads()
			for (i in 0 until segAngles) {
				val ang = i.toFloat() + (seg * segAngles).toFloat() + shift
				var xp = Math.cos(ang * Math.PI / 180f) * s
				var zp = Math.sin(ang * Math.PI / 180f) * s
				
				tess.addVertexWithUV(xp * m, y.toDouble(), zp * m, u.toDouble(), v.toDouble())
				tess.addVertexWithUV(xp, y0.toDouble(), zp, u.toDouble(), 0.0)
				
				xp = Math.cos((ang + 1) * Math.PI / 180f) * s
				zp = Math.sin((ang + 1) * Math.PI / 180f) * s
				
				tess.addVertexWithUV(xp, y0.toDouble(), zp, 0.0, 0.0)
				tess.addVertexWithUV(xp * m, y.toDouble(), zp * m, 0.0, v.toDouble())
			}
			y0 = 0f
			tess.draw()
			
			glPopMatrix()
		}
		glDisable(GL_BLEND)
		glEnable(GL_CULL_FACE)
		glPopMatrix()
	}
	
	@SideOnly(Side.CLIENT)
	fun renderHUD(resolution: ScaledResolution, player: EntityPlayer, stack: ItemStack) {
		val seg = getSegmentLookedAt(stack, player)
		
		val font = Minecraft.getMinecraft().fontRenderer
		var s = StatCollector.translateToLocal("race." + EnumRace.values()[seg + 1].toString() + ".name")
		font.drawStringWithShadow(s, resolution.scaledWidth / 2 - font.getStringWidth(s) / 2, resolution.scaledHeight / 2 - 65, EnumRace.getRGBColor((seg + 1).toDouble()))
		
		s = StatCollector.translateToLocal("item.HoloProjector.select")
		font.drawStringWithShadow(s, resolution.scaledWidth / 2 - font.getStringWidth(s) / 2, resolution.scaledHeight / 2 + 45, 0xFFFFFF)
		s = StatCollector.translateToLocal("item.HoloProjector.switch")
		font.drawStringWithShadow(s, resolution.scaledWidth / 2 - font.getStringWidth(s) / 2, resolution.scaledHeight / 2 + 55, 0xFFFFFF)
		s = StatCollector.translateToLocal("item.HoloProjector.rotate")
		font.drawStringWithShadow(s, resolution.scaledWidth / 2 - font.getStringWidth(s) / 2, resolution.scaledHeight / 2 + 65, 0xFFFFFF)
	}
	
	private class ModelHolder(val rotation: Int, val isMale: Boolean)
	
	companion object {
		
		private val RACES = 9
		private val FALLBACK_MODEL = ModelHolder(0, true)
		
		private val TAG_MODEL_PREFIX = "model"
		private val TAG_GENDER = "gender"
		private val TAG_ROTATION = "rotation"
		private val TAG_EQUIPPED = "equipped"
		private val TAG_FIRST_TICK = "firstTick"
		
		private fun getSegmentLookedAt(stack: ItemStack?, player: EntityLivingBase?): Int {
			val yaw = getCheckingAngle(player!!, 360f)
			
			val angles = 360
			val segAngles = angles / RACES
			for (seg in 0 until RACES) {
				val calcAngle = seg.toFloat() * segAngles
				if (yaw >= calcAngle && yaw < calcAngle + segAngles) return seg
			}
			return 0
		}
		
		// Screw the way minecraft handles rotation
		// Really...
		private fun getCheckingAngle(player: EntityLivingBase, base: Float): Float {
			var yaw = MathHelper.wrapAngleTo180_float(player.rotationYaw) + 90f
			val angles = 360
			val segAngles = angles / RACES
			val shift = (segAngles / 2).toFloat()
			
			if (yaw < 0) yaw = 180f + (180f + yaw)
			yaw -= 360f - base
			var angle = 360f - yaw + shift
			
			if (angle < 0) angle = 360f + angle
			
			return angle
		}
		
		fun isFirstTick(stack: ItemStack?): Boolean {
			return ItemNBTHelper.getBoolean(stack, TAG_FIRST_TICK, true)
		}
		
		fun tickFirst(stack: ItemStack?) {
			ItemNBTHelper.setBoolean(stack!!, TAG_FIRST_TICK, false)
		}
		
		fun wasEquipped(stack: ItemStack?): Boolean {
			return ItemNBTHelper.getBoolean(stack, TAG_EQUIPPED, false)
		}
		
		fun setEquipped(stack: ItemStack?, equipped: Boolean) {
			ItemNBTHelper.setBoolean(stack!!, TAG_EQUIPPED, equipped)
		}
		
		fun getModel(stack: ItemStack?, seg: Int): ModelHolder {
			val cmp = ItemNBTHelper.getCompound(stack, TAG_MODEL_PREFIX + seg, true) ?: return FALLBACK_MODEL
			return ModelHolder(cmp.getInteger(TAG_ROTATION), cmp.getBoolean(TAG_GENDER))
		}
		
		fun setModel(stack: ItemStack?, seg: Int, rot: Int, gen: Boolean) {
			val cmp = NBTTagCompound()
			cmp.setInteger(TAG_ROTATION, rot)
			cmp.setBoolean(TAG_GENDER, gen)
			ItemNBTHelper.setCompound(stack, TAG_MODEL_PREFIX + seg, cmp)
		}
	}
}