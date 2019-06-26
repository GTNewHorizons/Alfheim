package alfheim.common.item.relic

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.api.lib.LibResourceLocations
import alfheim.common.entity.boss.EntityFlugel
import baubles.common.lib.PlayerHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.*
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.*
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.texture.*
import net.minecraft.entity.*
import net.minecraft.entity.player.*
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.server.MinecraftServer
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraftforge.client.event.*
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType
import net.minecraftforge.common.MinecraftForge
import vazkii.botania.api.mana.ManaItemHandler
import vazkii.botania.client.core.handler.ClientTickHandler
import vazkii.botania.client.core.helper.IconHelper
import vazkii.botania.common.Botania
import vazkii.botania.common.block.ModBlocks
import vazkii.botania.common.block.tile.TileBrewery
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara
import vazkii.botania.common.item.relic.ItemRelic

import java.awt.*

import org.lwjgl.opengl.GL11.*

class ItemFlugelSoul: ItemRelic("FlugelSoul") {
	
	
	internal var signs: Array<IIcon>
	
	init {
		creativeTab = AlfheimCore.alfheimTab
		MinecraftForge.EVENT_BUS.register(this)
	}
	
	@SideOnly(Side.CLIENT)
	override fun getColorFromItemStack(stack: ItemStack?, pass: Int): Int {
		return Color.HSBtoRGB(Botania.proxy.worldElapsedTicks * 2 % 360 / 360f, 0.75f, 1f)
	}
	
	@SideOnly(Side.CLIENT)
	override fun registerIcons(par1IconRegister: IIconRegister) {
		super.registerIcons(par1IconRegister)
		signs = arrayOfNulls(12)
		for (i in 0..11) signs[i] = IconHelper.forName(par1IconRegister, "unused/sign$i")
	}
	
	override fun onItemUse(stack: ItemStack?, player: EntityPlayer?, world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		val block = world.getBlock(x, y, z)
		if (block === Blocks.beacon) {
			if (player!!.isSneaking && getBlocked(stack) < SEGMENTS) {
				val success = EntityFlugel.spawn(player, stack, world, x, y, z, true)
				if (success) setDisabled(stack, getBlocked(stack), true)
				return success
			}
		} else if (block === ModBlocks.brewery) {
			val brew = world.getTileEntity(x, y, z) as TileBrewery
			brew.setInventorySlotContents(0, stack!!.splitStack(1))
		}
		return false
	}
	
	override fun onItemRightClick(stack: ItemStack, world: World?, player: EntityPlayer?): ItemStack? {
		if (ItemRelic.isRightPlayer(player!!, stack) && !player.isSneaking) {
			val segment = getSegmentLookedAt(stack, player)
			val pos = getWarpPoint(stack, segment)
			if (pos.isValid) {
				if (!world!!.isRemote && player is EntityPlayerMP && (player.capabilities.isCreativeMode || ManaItemHandler.requestManaExact(stack, player, pos.mana(player), true))) {
					world.playSoundAtEntity(player, "mob.endermen.portal", 1f, 1f)
					ASJUtilities.sendToDimensionWithoutPortal(player, pos.dim, pos.x, pos.y, pos.z)
				}
			} else
				setWarpPoint(stack, segment, player.posX, player.posY, player.posZ, world!!.provider.dimensionId)
		}
		
		return stack
	}
	
	override fun onEntitySwing(player: EntityLivingBase, stack: ItemStack?): Boolean {
		if (player.isSneaking && player is EntityPlayer && ItemRelic.isRightPlayer(player, stack)) {
			val segment = getSegmentLookedAt(stack, player)
			val pos = getWarpPoint(stack, segment)
			if (pos.isValid) {
				setWarpPoint(stack, segment, 0.0, -1.0, 0.0, 0)
				return false
			}
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
			val segAngles = angles / SEGMENTS
			val shift = (segAngles / 2).toFloat()
			setRotationBase(stack, getCheckingAngle(entity as EntityLivingBase?) - shift)
			if (firstTick) tickFirst(stack)
		}
		
		if (entity is EntityPlayer) {
			val tiara = PlayerHandler.getPlayerBaubles((entity as EntityPlayer?)!!).getStackInSlot(0)
			if (tiara != null && tiara.item is ItemFlightTiara)
				ItemNBTHelper.setInt(tiara, TAG_TIME_LEFT, MAX_FLY_TIME)
		}
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onRenderWorldLast(event: RenderWorldLastEvent) {
		val player = Minecraft.getMinecraft().thePlayer
		val stack = player.currentEquippedItem
		if (stack != null && stack.item === this)
			render(stack, player, event.partialTicks)
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
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		val alpha = (Math.sin(((ClientTickHandler.ticksInGame + partialTicks) * 0.2f).toDouble()).toFloat() * 0.5f + 0.5f) * 0.4f + 0.3f
		
		val posX = player.prevPosX + (player.posX - player.prevPosX) * partialTicks
		val posY = player.prevPosY + (player.posY - player.prevPosY) * partialTicks
		val posZ = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks
		
		glTranslated(posX - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ)
		
		val base = getRotationBase(stack)
		val angles = 360
		val segAngles = angles / SEGMENTS
		val shift = base - segAngles / 2f
		
		val u = 1f
		val v = 0.25f
		
		val s = 3f
		val m = 0.8f
		val y = v * s * 2f
		var y0 = 0f
		
		val segmentLookedAt = getSegmentLookedAt(stack, player)
		
		for (seg in 0 until SEGMENTS) {
			var inside = false
			val rotationAngle = (seg + 0.5f) * segAngles + shift
			if (segmentLookedAt == seg) inside = true
			
			glPushMatrix()
			glRotatef(rotationAngle, 0f, 1f, 0f)
			glTranslatef(s * m, -0.75f, 0f)
			
			mc.renderEngine.bindTexture(TextureMap.locationItemsTexture)
			glScalef(0.75f, 0.75f, 0.75f)
			glTranslatef(0f, 0f, 0.5f)
			val icon = signs[seg]
			glRotatef(90f, 0f, 1f, 0f)
			glColor4f(1f, (if (!isDisabled(stack, seg)) 1 else 0).toFloat(), (if (!isDisabled(stack, seg)) 1 else 0).toFloat(), if (getWarpPoint(stack, seg).isValid && !isDisabled(stack, seg)) 1 else 0.2f)
			val f = icon.minU
			val f1 = icon.maxU
			val f2 = icon.minV
			val f3 = icon.maxV
			ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.iconWidth, icon.iconHeight, 1f / 16f)
			
			glColor3f(1f, 1f, 1f)
			glPopMatrix()
			
			glPushMatrix()
			glRotatef(180f, 1f, 0f, 0f)
			var a = alpha
			if (inside) {
				a += 0.3f
				y0 = -y
			}
			
			val c = if (seg % 2 == 0) 0.6f else 1f
			if (isDisabled(stack, seg))
				glColor4f(c, 0f, 0f, a)
			else
				glColor4f(c, c, c, a)
			
			mc.renderEngine.bindTexture(if (isDisabled(stack, seg)) LibResourceLocations.glow else LibResourceLocations.glowCyan)
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
		glPopMatrix()
	}
	
	@SideOnly(Side.CLIENT)
	fun renderHUD(resolution: ScaledResolution, player: EntityPlayer, stack: ItemStack) {
		val slot = getSegmentLookedAt(stack, player)
		val pos = getWarpPoint(stack, slot)
		
		val font = Minecraft.getMinecraft().fontRenderer
		var s = StatCollector.translateToLocal("item.FlugelSoul.sign$slot")
		font.drawStringWithShadow(s, resolution.scaledWidth / 2 - font.getStringWidth(s) / 2, resolution.scaledHeight / 2 - 65, if (isDisabled(stack, slot)) 0xAAAAAA else 0xFFD409)
		
		if (pos.isValid) {
			val dist = MathHelper.floor_double(vazkii.botania.common.core.helper.MathHelper.pointDistanceSpace(pos.x, pos.y, pos.z, player.posX, player.posY - 1.6, player.posZ).toDouble())
			
			if (pos.dim != player.dimension) {
				s = String.format(StatCollector.translateToLocal("item.FlugelSoul.anotherDim"), pos.dim)
				font.drawStringWithShadow(s, resolution.scaledWidth / 2 - font.getStringWidth(s) / 2, resolution.scaledHeight / 2 - 50, 0x9999FF)
			}
			s = if (dist == 1) StatCollector.translateToLocal("item.FlugelSoul.blockAway") else String.format(StatCollector.translateToLocal("item.FlugelSoul.blocksAway"), dist)
			font.drawStringWithShadow(s, resolution.scaledWidth / 2 - font.getStringWidth(s) / 2, resolution.scaledHeight / 2 - 40, 0x9999FF)
			s = StatCollector.translateToLocal("item.FlugelSoul.clickToTeleport")
			font.drawStringWithShadow(s, resolution.scaledWidth / 2 - font.getStringWidth(s) / 2, resolution.scaledHeight / 2 - 30, 0xFFFFFF)
			s = StatCollector.translateToLocal("item.FlugelSoul.clickToRemoveWarp")
			font.drawStringWithShadow(s, resolution.scaledWidth / 2 - font.getStringWidth(s) / 2, resolution.scaledHeight / 2 - 20, 0xFFFFFF)
		} else if (isDisabled(stack, slot)) {
			s = StatCollector.translateToLocal("item.FlugelSoul.blockedWarp")
			font.drawStringWithShadow(s, resolution.scaledWidth / 2 - font.getStringWidth(s) / 2, resolution.scaledHeight / 2 - 40, 0xAAAAAA)
		} else {
			s = StatCollector.translateToLocal("item.FlugelSoul.unboundWarp")
			font.drawStringWithShadow(s, resolution.scaledWidth / 2 - font.getStringWidth(s) / 2, resolution.scaledHeight / 2 - 40, 0xFFFFFF)
			s = StatCollector.translateToLocal("item.FlugelSoul.clickToAddWarp")
			font.drawStringWithShadow(s, resolution.scaledWidth / 2 - font.getStringWidth(s) / 2, resolution.scaledHeight / 2 - 30, 0xFFFFFF)
		}
	}
	
	private class MultiversePosition(val x: Double, val y: Double, val z: Double, val dim: Int) {
		
		internal val isValid: Boolean
			get() = y > 0 && (!ASJUtilities.isServer || MinecraftServer.getServer().worldServerForDimension(dim) != null)
		
		internal fun mana(player: EntityPlayer): Int {
			val mod = if (player.dimension != dim) player.worldObj.provider.movementFactor / MinecraftServer.getServer().worldServerForDimension(dim).provider.movementFactor * 4.0 else 1.0
			return MathHelper.floor_double(vazkii.botania.common.core.helper.MathHelper.pointDistanceSpace(x, y, z, player.posX, player.posY - 1.6, player.posZ) * mod) * 10
		}
	}
	
	companion object {
		
		private val SEGMENTS = 12
		private val FALLBACK_POSITION = MultiversePosition(0.0, -1.0, 0.0, 0)
		
		private val TAG_EQUIPPED = "equipped"
		private val TAG_ROTATION_BASE = "rotationBase"
		private val TAG_WARP_PREFIX = "warp"
		private val TAG_POS_X = "posX"
		private val TAG_POS_Y = "posY"
		private val TAG_POS_Z = "posZ"
		private val TAG_DIMENSION = "dim"
		private val TAG_FIRST_TICK = "firstTick"
		private val TAG_DISABLED = "disabled"
		private val TAG_BLOCKED = "blocked"
		
		// ItemFlightTiara
		private val TAG_TIME_LEFT = "timeLeft"
		private val MAX_FLY_TIME = 1200
		
		private fun getSegmentLookedAt(stack: ItemStack?, player: EntityLivingBase): Int {
			val yaw = getCheckingAngle(player, getRotationBase(stack))
			
			val angles = 360
			val segAngles = angles / SEGMENTS
			for (seg in 0 until SEGMENTS) {
				val calcAngle = seg.toFloat() * segAngles
				if (yaw >= calcAngle && yaw < calcAngle + segAngles) return seg
			}
			return 0
		}
		
		fun getCheckingAngle(player: EntityLivingBase): Float {
			return getCheckingAngle(player, 0f)
		}
		
		// Screw the way minecraft handles rotation
		// Really...
		private fun getCheckingAngle(player: EntityLivingBase, base: Float): Float {
			var yaw = MathHelper.wrapAngleTo180_float(player.rotationYaw) + 90f
			val angles = 360
			val segAngles = angles / SEGMENTS
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
		
		fun getRotationBase(stack: ItemStack?): Float {
			return ItemNBTHelper.getFloat(stack, TAG_ROTATION_BASE, 0f)
		}
		
		fun setRotationBase(stack: ItemStack?, rotation: Float) {
			ItemNBTHelper.setFloat(stack!!, TAG_ROTATION_BASE, rotation)
		}
		
		fun isDisabled(stack: ItemStack?, warp: Int): Boolean {
			return ItemNBTHelper.getBoolean(stack, TAG_DISABLED + warp, false)
		}
		
		fun setDisabled(stack: ItemStack?, warp: Int, disable: Boolean) {
			ItemNBTHelper.setBoolean(stack!!, TAG_DISABLED + (warp - if (disable) 0 else 1), disable)
			stack.tagCompound.tagMap.remove(TAG_WARP_PREFIX + warp)
			setBlocked(stack, getBlocked(stack) + if (disable) 1 else -1)
		}
		
		fun getBlocked(item: ItemStack?): Int {
			return ItemNBTHelper.getInt(item, TAG_BLOCKED, 0)
		}
		
		fun setBlocked(item: ItemStack, blocked: Int) {
			ItemNBTHelper.setInt(item, TAG_BLOCKED, blocked)
		}
		
		fun setWarpPoint(stack: ItemStack?, warp: Int, x: Double, y: Double, z: Double, dim: Int) {
			if (isDisabled(stack, warp)) return
			val cmp = NBTTagCompound()
			cmp.setDouble(TAG_POS_X, x)
			cmp.setDouble(TAG_POS_Y, y)
			cmp.setDouble(TAG_POS_Z, z)
			cmp.setInteger(TAG_DIMENSION, dim)
			ItemNBTHelper.setCompound(stack, TAG_WARP_PREFIX + warp, cmp)
		}
		
		fun getWarpPoint(stack: ItemStack?, warp: Int): MultiversePosition {
			if (isDisabled(stack, warp)) return FALLBACK_POSITION
			val cmp = ItemNBTHelper.getCompound(stack, TAG_WARP_PREFIX + warp, true) ?: return FALLBACK_POSITION
			
			val x = cmp.getDouble(TAG_POS_X)
			val y = cmp.getDouble(TAG_POS_Y)
			val z = cmp.getDouble(TAG_POS_Z)
			val dim = cmp.getInteger(TAG_DIMENSION)
			return MultiversePosition(x, y, z, dim)
		}
		
		fun getFirstCoords(stack: ItemStack): ChunkCoordinates {
			val pos = getWarpPoint(stack, getBlocked(stack))
			return ChunkCoordinates(pos.x.toInt(), pos.y.toInt(), pos.z.toInt())
		}
	}
}