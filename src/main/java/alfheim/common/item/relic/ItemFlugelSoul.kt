package alfheim.common.item.relic

import alexsocol.asjlib.*
import alexsocol.asjlib.math.Vector3
import alexsocol.asjlib.render.ASJRenderHelper
import alfheim.api.lib.LibResourceLocations
import alfheim.common.core.helper.ElvenFlightHelper
import alfheim.common.core.util.AlfheimTab
import alfheim.common.entity.boss.EntityFlugel
import alfheim.common.item.*
import alfheim.common.security.InteractionSecurity
import baubles.common.lib.PlayerHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.*
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.texture.*
import net.minecraft.entity.*
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.*
import net.minecraft.entity.projectile.EntityThrowable
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.server.MinecraftServer
import net.minecraft.util.*
import net.minecraft.world.World
import net.minecraftforge.client.event.*
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType
import net.minecraftforge.common.MinecraftForge
import org.lwjgl.opengl.GL11.*
import vazkii.botania.api.internal.IManaBurst
import vazkii.botania.api.mana.*
import vazkii.botania.client.core.handler.ClientTickHandler
import vazkii.botania.client.core.helper.IconHelper
import vazkii.botania.common.Botania
import vazkii.botania.common.block.ModBlocks
import vazkii.botania.common.block.tile.TileBrewery
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.item.equipment.bauble.ItemFlightTiara
import vazkii.botania.common.item.relic.ItemRelic
import java.awt.Color
import kotlin.math.*

@Suppress("UNCHECKED_CAST")
class ItemFlugelSoul: ItemRelic("FlugelSoul"), ILensEffect {
	
	init {
		creativeTab = AlfheimTab
	}
	
	@SideOnly(Side.CLIENT)
	override fun getColorFromItemStack(stack: ItemStack?, pass: Int) = ItemIridescent.rainbowColor()
	
	@SideOnly(Side.CLIENT)
	override fun registerIcons(reg: IIconRegister) {
		super.registerIcons(reg)
		signs = Array(12) { IconHelper.forName(reg, "unused/sign$it") }
	}
	
	override fun onItemUse(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean {
		val block = world.getBlock(x, y, z)
		if (block === ModBlocks.brewery) {
			val brew = world.getTileEntity(x, y, z) as TileBrewery
			brew.set(0, stack.splitStack(1))
		} else { // Stupid Et Futurum
			if (player.isSneaking && getBlocked(stack) < SEGMENTS) {
				val success = EntityFlugel.spawn(player, stack, world, x, y, z, true, false)
				if (success) setDisabled(stack, getBlocked(stack), true)
				return success
			}
		}
		return false
	}
	
	override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack {
		if (isRightPlayer(player, stack) && !player.isSneaking) {
			val metaWas = stack.meta
			stack.meta = 0xFACE17
			
			val segment = getSegmentLookedAt(stack, player)
			val pos = getWarpPoint(stack, segment)
			if (pos.isValid) {
				if (!world.isRemote && player is EntityPlayerMP && ManaItemHandler.requestManaExact(stack, player, pos.mana(player), true)) {
					if (InteractionSecurity.canDoSomethingHere(player, pos.x, pos.y, pos.z, MinecraftServer.getServer().worldServerForDimension(pos.dim))) {
						world.playSoundAtEntity(player, "mob.endermen.portal", 1f, 1f)
						ASJUtilities.sendToDimensionWithoutPortal(player, pos.dim, pos.x, pos.y, pos.z)
					}
				}
			} else {
				if (InteractionSecurity.canDoSomethingHere(player))
					setWarpPoint(stack, segment, player.posX, player.posY, player.posZ, world.provider.dimensionId)
			}
			
			stack.meta = metaWas
		}
		
		return stack
	}
	
	override fun onEntitySwing(player: EntityLivingBase, stack: ItemStack): Boolean {
		if (player.isSneaking && player is EntityPlayer && isRightPlayer(player, stack)) {
			val segment = getSegmentLookedAt(stack, player)
			val pos = getWarpPoint(stack, segment)
			if (pos.isValid) {
				setWarpPoint(stack, segment, 0.0, -1.0, 0.0, 0)
				return false
			}
		}
		
		return false
	}
	
	override fun onUpdate(stack: ItemStack, world: World, entity: Entity, pos: Int, equipped: Boolean) {
		super.onUpdate(stack, world, entity, pos, equipped)
		val eqLastTick = wasEquipped(stack)
		val firstTick = isFirstTick(stack)
		if (eqLastTick != equipped) setEquipped(stack, equipped)
		
		if ((!equipped || firstTick) && entity is EntityLivingBase) {
			val angles = 360
			val segAngles = angles / SEGMENTS
			val shift = (segAngles / 2).F
			setRotationBase(stack, getCheckingAngle(entity) - shift)
			if (firstTick) tickFirst(stack)
		}
		
		if (entity is EntityPlayer) {
			val tiara = PlayerHandler.getPlayerBaubles((entity as EntityPlayer?)!!).get(0)
			if (tiara != null && tiara.item is ItemFlightTiara)
				ItemNBTHelper.setInt(tiara, TAG_TIME_LEFT, MAX_FLY_TIME)
			
			ElvenFlightHelper.regen(entity, 10)
		}
	}
	
	override fun doParticles(burst: IManaBurst?, stack: ItemStack?) = true
	
	override fun collideBurst(burst: IManaBurst?, pos: MovingObjectPosition?, isManaBlock: Boolean, dead: Boolean, stack: ItemStack?) = dead
	
	override fun apply(stack: ItemStack?, props: BurstProperties?) = Unit
	
	override fun updateBurst(burst: IManaBurst?, stack: ItemStack) {
		val entity = burst as EntityThrowable
		val axis = AxisAlignedBB.getBoundingBox(entity.posX, entity.posY, entity.posZ, entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).expand(1.0)
		val entities = entity.worldObj.getEntitiesWithinAABB(EntityLivingBase::class.java, axis) as List<EntityLivingBase>
		val attacker = ItemNBTHelper.getInt(burst.sourceLens, TAG_ATTACKER_ID, -1)
		
		for (living in entities) {
			if (living.entityId == attacker) continue
			
			if (living.hurtTime == 0) {
				if (!burst.isFake && !entity.worldObj.isRemote) {
					living.attackEntityFrom(DamageSource.magic, if (stack.meta > 0) 10f else 8f)
					entity.setDead()
					break
				}
			}
		}
	}
	
	override fun onEntityItemUpdate(entity: EntityItem): Boolean {
		val horn: EntityItem? = (entity.worldObj.getEntitiesWithinAABB(EntityItem::class.java, entity.boundingBox(0.5)) as List<EntityItem>).firstOrNull { it.entityItem?.item === AlfheimItems.soulHorn }
		
		if (horn != null && horn.entityItem.meta == 0 && getBlocked(entity.entityItem) == 0) {
			for (i in 0 until SEGMENTS) setDisabled(entity.entityItem, i, true)
			horn.entityItem.meta = 1
			
			val v = Vector3()
			for (i in 0 until 360 step 5) {
				val c = Color.getHSBColor(i.F / 360f, 1f, 1f)
				v.rand().sub(0.5).normalize().mul(Math.random() * 0.5)
				Botania.proxy.sparkleFX(entity.worldObj, entity.posX + v.x, entity.posY + v.y, entity.posZ + v.z, c.red / 255f, c.green / 255f, c.blue / 255f, 1.5f, 1000)
			}
		}
		
		return super.onEntityItemUpdate(entity)
	}
	
	companion object {
		
		const val SEGMENTS = 12
		
		const val TAG_ATTACKER_ID = "attackerId"
		const val TAG_EQUIPPED = "equipped"
		const val TAG_ROTATION_BASE = "rotationBase"
		const val TAG_WARP_PREFIX = "warp"
		const val TAG_POS_X = "posX"
		const val TAG_POS_Y = "posY"
		const val TAG_POS_Z = "posZ"
		const val TAG_DIMENSION = "dim"
		const val TAG_FIRST_TICK = "firstTick"
		const val TAG_DISABLED = "disabled"
		const val TAG_BLOCKED = "blocked"
		
		// ItemFlightTiara
		const val TAG_TIME_LEFT = "timeLeft"
		const val MAX_FLY_TIME = 1200
		
		val FALLBACK_POSITION = MultiversePosition(0.0, -1.0, 0.0, 0)
		
		lateinit var signs: Array<IIcon>
		
		init {
			MinecraftForge.EVENT_BUS.register(this)
		}
		
		@SubscribeEvent
		@SideOnly(Side.CLIENT)
		fun onRenderWorldLast(event: RenderWorldLastEvent) {
			val player = mc.thePlayer
			val stack = player.currentEquippedItem
			if (stack != null && stack.item === AlfheimItems.flugelSoul)
				render(stack, player, event.partialTicks)
		}
		
		@SubscribeEvent
		@SideOnly(Side.CLIENT)
		fun onDrawScreenPost(e: RenderGameOverlayEvent.Post) {
			if (e.type != ElementType.ALL) return
			
			val player = mc.thePlayer
			val stack = player.currentEquippedItem
			if (stack != null && stack.item === AlfheimItems.flugelSoul) renderHUD(e.resolution, player, stack)
		}
		
		@SideOnly(Side.CLIENT)
		fun render(stack: ItemStack, player: EntityPlayer, partialTicks: Float) {
			val tess = Tessellator.instance
			Tessellator.renderingWorldRenderer = false
			
			glPushMatrix()
			glEnable(GL_BLEND)
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
			val alpha = (sin(((ClientTickHandler.ticksInGame + partialTicks) * 0.2f).D).F * 0.5f + 0.5f) * 0.4f + 0.3f
			
			ASJRenderHelper.interpolatedTranslation(player)
			glTranslated(-RenderManager.renderPosX, -RenderManager.renderPosY, -RenderManager.renderPosZ)
			
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
				glScaled(0.75)
				glTranslatef(0f, 0f, 0.5f)
				val icon = signs[seg]
				glRotatef(90f, 0f, 1f, 0f)
				glColor4f(1f, (if (!isDisabled(stack, seg)) 1 else 0).F, (if (!isDisabled(stack, seg)) 1 else 0).F, if (getWarpPoint(stack, seg).isValid && !isDisabled(stack, seg)) 1f else 0.2f)
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
					val ang = i.F + (seg * segAngles).F + shift
					var xp = cos(ang * Math.PI / 180f) * s
					var zp = sin(ang * Math.PI / 180f) * s
					
					tess.addVertexWithUV(xp * m, y.D, zp * m, u.D, v.D)
					tess.addVertexWithUV(xp, y0.D, zp, u.D, 0.0)
					
					xp = cos((ang + 1) * Math.PI / 180f) * s
					zp = sin((ang + 1) * Math.PI / 180f) * s
					
					tess.addVertexWithUV(xp, y0.D, zp, 0.0, 0.0)
					tess.addVertexWithUV(xp * m, y.D, zp * m, 0.0, v.D)
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
			
			val font = mc.fontRenderer
			var s = StatCollector.translateToLocal("item.FlugelSoul.sign$slot")
			font.drawStringWithShadow(s, resolution.scaledWidth / 2 - font.getStringWidth(s) / 2, resolution.scaledHeight / 2 - 65, if (isDisabled(stack, slot)) 0xAAAAAA else 0xFFD409)
			
			when {
				pos.isValid             -> {
					val dist = Vector3.pointDistanceSpace(pos.x, pos.y, pos.z, player.posX, player.posY - 1.6, player.posZ).mfloor()
					
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
				}
				
				isDisabled(stack, slot) -> {
					s = StatCollector.translateToLocal("item.FlugelSoul.blockedWarp")
					font.drawStringWithShadow(s, resolution.scaledWidth / 2 - font.getStringWidth(s) / 2, resolution.scaledHeight / 2 - 40, 0xAAAAAA)
				}
				
				else                    -> {
					s = StatCollector.translateToLocal("item.FlugelSoul.unboundWarp")
					font.drawStringWithShadow(s, resolution.scaledWidth / 2 - font.getStringWidth(s) / 2, resolution.scaledHeight / 2 - 40, 0xFFFFFF)
					s = StatCollector.translateToLocal("item.FlugelSoul.clickToAddWarp")
					font.drawStringWithShadow(s, resolution.scaledWidth / 2 - font.getStringWidth(s) / 2, resolution.scaledHeight / 2 - 30, 0xFFFFFF)
				}
			}
		}
		
		fun getSegmentLookedAt(stack: ItemStack, player: Entity): Int {
			val yaw = getCheckingAngle(player, getRotationBase(stack))
			
			val angles = 360
			val segAngles = angles / SEGMENTS
			for (seg in 0 until SEGMENTS) {
				val calcAngle = seg.F * segAngles
				if (yaw >= calcAngle && yaw < calcAngle + segAngles) return seg
			}
			return 0
		}
		
		fun getCheckingAngle(player: Entity): Float {
			return getCheckingAngle(player, 0f)
		}
		
		// Screw the way minecraft handles rotation
		// Really...
		fun getCheckingAngle(player: Entity, base: Float): Float {
			var yaw = MathHelper.wrapAngleTo180_float(player.rotationYaw) + 90f
			val angles = 360
			val segAngles = angles / SEGMENTS
			val shift = (segAngles / 2).F
			
			if (yaw < 0) yaw = 180f + (180f + yaw)
			yaw -= 360f - base
			var angle = 360f - yaw + shift
			
			if (angle < 0) angle += 360f
			
			return angle
		}
		
		fun isFirstTick(stack: ItemStack): Boolean {
			return ItemNBTHelper.getBoolean(stack, TAG_FIRST_TICK, true)
		}
		
		fun tickFirst(stack: ItemStack) {
			ItemNBTHelper.setBoolean(stack, TAG_FIRST_TICK, false)
		}
		
		fun wasEquipped(stack: ItemStack): Boolean {
			return ItemNBTHelper.getBoolean(stack, TAG_EQUIPPED, false)
		}
		
		fun setEquipped(stack: ItemStack, equipped: Boolean) {
			ItemNBTHelper.setBoolean(stack, TAG_EQUIPPED, equipped)
		}
		
		fun getRotationBase(stack: ItemStack): Float {
			return ItemNBTHelper.getFloat(stack, TAG_ROTATION_BASE, 0f)
		}
		
		fun setRotationBase(stack: ItemStack, rotation: Float) {
			ItemNBTHelper.setFloat(stack, TAG_ROTATION_BASE, rotation)
		}
		
		fun isDisabled(stack: ItemStack, warp: Int): Boolean {
			return ItemNBTHelper.getBoolean(stack, TAG_DISABLED + warp, false)
		}
		
		fun setDisabled(stack: ItemStack, warp: Int, disable: Boolean) {
			ItemNBTHelper.setBoolean(stack, TAG_DISABLED + (warp - if (disable) 0 else 1), disable)
			stack.tagCompound.tagMap.remove(TAG_WARP_PREFIX + warp)
			setBlocked(stack, getBlocked(stack) + if (disable) 1 else -1)
		}
		
		fun getBlocked(item: ItemStack): Int {
			return ItemNBTHelper.getInt(item, TAG_BLOCKED, 0)
		}
		
		fun setBlocked(item: ItemStack, blocked: Int) {
			ItemNBTHelper.setInt(item, TAG_BLOCKED, blocked)
		}
		
		fun setWarpPoint(stack: ItemStack, warp: Int, x: Double, y: Double, z: Double, dim: Int) {
			if (isDisabled(stack, warp)) return
			val cmp = NBTTagCompound()
			cmp.setDouble(TAG_POS_X, x)
			cmp.setDouble(TAG_POS_Y, y)
			cmp.setDouble(TAG_POS_Z, z)
			cmp.setInteger(TAG_DIMENSION, dim)
			ItemNBTHelper.setCompound(stack, TAG_WARP_PREFIX + warp, cmp)
		}
		
		fun getWarpPoint(stack: ItemStack, warp: Int): MultiversePosition {
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
			return ChunkCoordinates(pos.x.I, pos.y.I, pos.z.I)
		}
	}
	
	class MultiversePosition(val x: Double, val y: Double, val z: Double, val dim: Int) {
		
		internal val isValid: Boolean
			get() = y > 0 && (ASJUtilities.isClient || MinecraftServer.getServer().worldServerForDimension(dim) != null)
		
		internal fun mana(player: EntityPlayer): Int {
			val mod = if (player.dimension != dim) player.worldObj.provider.movementFactor / MinecraftServer.getServer().worldServerForDimension(dim).provider.movementFactor * 4.0 else 1.0
			return (Vector3.pointDistanceSpace(x, y, z, player.posX, player.posY - 1.6, player.posZ) * mod).mfloor() * 10
		}
	}
}