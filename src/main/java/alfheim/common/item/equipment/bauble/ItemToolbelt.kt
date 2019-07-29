package alfheim.common.item.equipment.bauble

import alfheim.AlfheimCore
import alfheim.api.ModInfo
import alfheim.api.item.IToolbeltBlacklisted
import alfheim.common.core.helper.IconHelper
import alfheim.common.network.MessagePlayerItem
import baubles.api.BaubleType
import baubles.common.lib.PlayerHandler
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.*
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.model.ModelBiped
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.texture.*
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.*
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.*
import net.minecraftforge.client.*
import net.minecraftforge.client.event.*
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import org.lwjgl.opengl.GL11
import vazkii.botania.api.item.*
import vazkii.botania.client.core.handler.ClientTickHandler
import vazkii.botania.common.core.helper.ItemNBTHelper
import vazkii.botania.common.item.ItemBaubleBox
import vazkii.botania.common.item.equipment.bauble.ItemBauble
import java.awt.Color
import java.util.*
import kotlin.math.*

class ItemToolbelt: ItemBauble("toolbelt"), IBaubleRender, IBlockProvider, IToolbeltBlacklisted {
	// ItemToolbelt will not become an IManaItem. That's... a bit excessivly OP, imo. Store those in your Bauble Case, or keep them in your inventory.
	companion object {
		
		val glowTexture = ResourceLocation("${ModInfo.MODID}:textures/misc/toolbelt.png")
		val beltTexture = ResourceLocation("${ModInfo.MODID}:textures/model/entity/toolbelt.png")
		
		@SideOnly(Side.CLIENT)
		var model: ModelBiped? = null
		
		const val SEGMENTS = 12
		
		const val TAG_ITEM_PREFIX = "item"
		const val TAG_EQUIPPED = "equipped"
		const val TAG_ROTATION_BASE = "rotationBase"
		
		fun isEquipped(stack: ItemStack): Boolean = ItemNBTHelper.getBoolean(stack, TAG_EQUIPPED, false)
		fun setEquipped(stack: ItemStack, equipped: Boolean) = ItemNBTHelper.setBoolean(stack, TAG_EQUIPPED, equipped)
		fun getRotationBase(stack: ItemStack): Float = ItemNBTHelper.getFloat(stack, TAG_ROTATION_BASE, 0F)
		fun setRotationBase(stack: ItemStack, rotation: Float) = ItemNBTHelper.setFloat(stack, TAG_ROTATION_BASE, rotation)
		
		fun getSegmentLookedAt(stack: ItemStack, player: EntityLivingBase): Int {
			val yaw = getCheckingAngle(player, getRotationBase(stack))
			
			val angles = 360
			val segAngles = angles / SEGMENTS
			for (seg in 0 until SEGMENTS) {
				val calcAngle = seg.toFloat() * segAngles
				if (yaw >= calcAngle && yaw < calcAngle + segAngles)
					return seg
			}
			return -1
		}
		
		fun getCheckingAngle(player: EntityLivingBase): Float = getCheckingAngle(player, 0F)
		
		// Agreed, V, minecraft's rotation is shit. And no roll? Seriously?
		fun getCheckingAngle(player: EntityLivingBase, base: Float): Float {
			var yaw = MathHelper.wrapAngleTo180_float(player.rotationYaw) + 90F
			val angles = 360
			val segAngles = angles / SEGMENTS
			val shift = segAngles / 2
			
			if (yaw < 0)
				yaw = 180F + (180F + yaw)
			yaw -= 360F - base
			var angle = 360F - yaw + shift
			
			if (angle < 0)
				angle += 360F
			
			return angle
		}
		
		fun isLookingAtSegment(player: EntityLivingBase): Boolean {
			val pitch = player.rotationPitch
			
			return pitch > -33.75 && pitch < 45
		}
		
		fun getItemForSlot(stack: ItemStack, slot: Int): ItemStack? {
			if (slot >= SEGMENTS) return null
			val cmp = getStoredCompound(stack, slot)
					  ?: return null
			return ItemStack.loadItemStackFromNBT(cmp)
		}
		
		fun getStoredCompound(stack: ItemStack, slot: Int): NBTTagCompound? = ItemNBTHelper.getCompound(stack, TAG_ITEM_PREFIX + slot, true)
		fun setItem(beltStack: ItemStack, stack: ItemStack?, pos: Int) {
			if (stack == null) ItemNBTHelper.setCompound(beltStack, TAG_ITEM_PREFIX + pos, NBTTagCompound())
			else {
				val tag = NBTTagCompound()
				stack.writeToNBT(tag)
				ItemNBTHelper.setCompound(beltStack, TAG_ITEM_PREFIX + pos, tag)
			}
		}
		
		fun getEquippedBelt(player: EntityPlayer): ItemStack? {
			val inv = PlayerHandler.getPlayerBaubles(player)
			var beltStack: ItemStack? = null
			for (i in 0..inv.sizeInventory) {
				val stack = inv.getStackInSlot(i)
				if (stack != null && stack.item is ItemToolbelt) {
					beltStack = stack
				}
			}
			return beltStack
		}
	}
	
	val handler = ToolbeltEventHandler()
	
	init {
		MinecraftForge.EVENT_BUS.register(handler)
		FMLCommonHandler.instance().bus().register(handler)
		setHasSubtypes(true)
		creativeTab = AlfheimCore.baTab
	}
	
	override fun registerIcons(par1IconRegister: IIconRegister) {
		itemIcon = IconHelper.forItem(par1IconRegister, this)
	}
	
	override fun getBlockCount(p0: EntityPlayer?, p1: ItemStack, p2: ItemStack, p3: Block, p4: Int): Int {
		var total = 0
		for (segment in 0 until SEGMENTS) {
			val slotStack = getItemForSlot(p2, segment)
			if (slotStack != null) {
				val slotItem = slotStack.item
				if (slotItem is IBlockProvider) {
					val count = slotItem.getBlockCount(p0, p1, slotStack, p3, p4)
					setItem(p2, slotStack, segment)
					if (count == -1) return -1
					total += count
				} else if (slotItem is ItemBlock && Block.getBlockFromItem(slotItem) == p3 && slotStack.itemDamage == p4) {
					total += slotStack.stackSize
				}
			}
		}
		return total
	}
	
	override fun provideBlock(p0: EntityPlayer?, p1: ItemStack, p2: ItemStack, p3: Block, p4: Int, p5: Boolean): Boolean {
		for (segment in 0 until SEGMENTS) {
			val slotStack = getItemForSlot(p2, segment)
			if (slotStack != null) {
				val slotItem = slotStack.item
				if (slotItem is IBlockProvider) {
					val provided = slotItem.provideBlock(p0, p1, slotStack, p3, p4, p5)
					setItem(p2, slotStack, segment)
					if (provided) return true
				} else if (slotItem is ItemBlock && Block.getBlockFromItem(slotItem) == p3 && slotStack.itemDamage == p4) {
					if (p5) slotStack.stackSize--
					
					if (slotStack.stackSize == 0) setItem(p2, null, segment)
					else setItem(p2, slotStack, segment)
					return true
				}
			}
		}
		return false
	}
	
	override fun allowedInToolbelt(stack: ItemStack) = false
	
	override fun addHiddenTooltip(par1ItemStack: ItemStack?, par2EntityPlayer: EntityPlayer?, par3List: MutableList<Any?>, par4: Boolean) {
		if (par1ItemStack != null) {
			val map = HashMap<String, Int>()
			for (segment in 0 until SEGMENTS) {
				val slotStack = getItemForSlot(par1ItemStack, segment)
				if (slotStack != null) {
					var base = 0
					val name = slotStack.displayName
					val node = map[name]
					if (node != null) base = node
					map[name] = base + slotStack.stackSize
				}
			}
			if (map.size > 0) par3List.add("${EnumChatFormatting.AQUA}" + StatCollector.translateToLocal("misc.${ModInfo.MODID}.contains"))
			else par3List.add("${EnumChatFormatting.AQUA}" + StatCollector.translateToLocal("misc.${ModInfo.MODID}.containsNothing"))
			val keys = ArrayList(map.keys)
			keys.sort()
			keys.mapTo(par3List) { "${map[it]}x ${EnumChatFormatting.WHITE}$it" }
		}
		super.addHiddenTooltip(par1ItemStack, par2EntityPlayer, par3List, par4)
	}
	
	override fun getBaubleType(stack: ItemStack) = BaubleType.BELT
	
	override fun onWornTick(stack: ItemStack, player: EntityLivingBase) {
		if (player is EntityPlayer) {
			val eqLastTick = isEquipped(stack)
			val eq = player.isSneaking && isLookingAtSegment(player)
			if (eqLastTick != eq)
				setEquipped(stack, eq)
			
			if (!player.isSneaking) {
				val angles = 360
				val segAngles = angles / SEGMENTS
				val shift = segAngles / 2
				setRotationBase(stack, getCheckingAngle(player) - shift)
			}
		}
	}
	
	override fun getUnlocalizedNameInefficiently(par1ItemStack: ItemStack) =
		super.getUnlocalizedNameInefficiently(par1ItemStack).replace("item\\.botania:".toRegex(), "item.${ModInfo.MODID}:")
	
	@SideOnly(Side.CLIENT)
	fun getGlowResource(): ResourceLocation = glowTexture
	
	@SideOnly(Side.CLIENT)
	override fun onPlayerBaubleRender(stack: ItemStack, event: RenderPlayerEvent, type: IBaubleRender.RenderType) {
		if (type == IBaubleRender.RenderType.BODY) {
			Minecraft.getMinecraft().renderEngine.bindTexture(beltTexture)
			IBaubleRender.Helper.rotateIfSneaking(event.entityPlayer)
			GL11.glTranslatef(0F, 0.2F, 0F)
			
			val s = 1.05F / 16F
			GL11.glScalef(s, s, s)
			if (model == null)
				model = ModelBiped()
			else
				model!!.bipedBody.render(1F)
		}
	}
}

/**
 * @author WireSegal
 * Created at 2:59 PM on 1/23/16.
 */
class ToolbeltEventHandler {
	
	@SideOnly(Side.CLIENT)
	private fun shouldRenderAsEntity(stack: ItemStack) =
		MinecraftForgeClient.getItemRenderer(stack, IItemRenderer.ItemRenderType.ENTITY) != null
	
	@SubscribeEvent
	fun onPlayerInteract(event: PlayerInteractEvent) {
		val player = event.entityPlayer
		val beltStack = ItemToolbelt.getEquippedBelt(player)
		
		val heldItem = player.currentEquippedItem
		if (beltStack != null && ItemToolbelt.isEquipped(beltStack)) {
			if (event.action === PlayerInteractEvent.Action.RIGHT_CLICK_AIR) {
				val segment = ItemToolbelt.getSegmentLookedAt(beltStack, player)
				val toolStack = ItemToolbelt.getItemForSlot(beltStack, segment)
				if (toolStack == null && heldItem != null) {
					val heldItemObject = heldItem.item
					if (!(heldItemObject is IToolbeltBlacklisted && !heldItemObject.allowedInToolbelt(heldItem)) && heldItemObject !is ItemBaubleBox) {
						if (!event.world.isRemote) {
							val item = heldItem.copy()
							
							ItemToolbelt.setItem(beltStack, item, segment)
							
							player.inventory.decrStackSize(player.inventory.currentItem, 64)
							player.inventory.markDirty()
							event.isCanceled = true
						}
					}
				} else if (toolStack != null) {
					AlfheimCore.network.sendToServer(MessagePlayerItem(toolStack))
					
					ItemToolbelt.setItem(beltStack, null, segment)
					event.isCanceled = true
				}
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	fun onRenderWorldLast(event: RenderWorldLastEvent) {
		val player = Minecraft.getMinecraft().thePlayer
		val beltStack = ItemToolbelt.getEquippedBelt(player)
		if (beltStack != null && ItemToolbelt.isEquipped(beltStack))
			render(beltStack, player, event.partialTicks)
	}
	
	@SideOnly(Side.CLIENT)
	fun render(stack: ItemStack, player: EntityPlayer, partialTicks: Float) {
		val mc = Minecraft.getMinecraft()
		val tess = Tessellator.instance
		Tessellator.renderingWorldRenderer = false
		
		GL11.glPushMatrix()
		GL11.glEnable(GL11.GL_BLEND)
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
		val alpha = (sin((ClientTickHandler.ticksInGame + partialTicks).toDouble() * 0.2) * 0.5F + 0.5F) * 0.4F + 0.3F
		
		val posX = player.prevPosX + (player.posX - player.prevPosX) * partialTicks
		val posY = player.prevPosY + (player.posY - player.prevPosY) * partialTicks
		val posZ = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks
		
		GL11.glTranslated(posX - RenderManager.renderPosX, posY - RenderManager.renderPosY, posZ - RenderManager.renderPosZ)
		
		val base = ItemToolbelt.getRotationBase(stack)
		val angles = 360
		val segAngles = angles / ItemToolbelt.SEGMENTS
		val shift = base - segAngles / 2
		
		val u = 1F
		val v = 0.25F
		
		val s = 3F
		val m = 0.8F
		val y = v * s * 2
		var y0 = 0.0
		
		val segmentLookedAt = ItemToolbelt.getSegmentLookedAt(stack, player)
		
		for (seg in 0 until ItemToolbelt.SEGMENTS) {
			
			var inside = false
			val rotationAngle = (seg + 0.5F) * segAngles + shift
			GL11.glPushMatrix()
			GL11.glRotatef(rotationAngle, 0F, 1F, 0F)
			GL11.glTranslatef(s * m, -0.75F, 0F)
			
			if (segmentLookedAt == seg)
				inside = true
			
			val slotStack = ItemToolbelt.getItemForSlot(stack, seg)
			if (slotStack != null) {
				mc.renderEngine.bindTexture(if (slotStack.item is ItemBlock) TextureMap.locationBlocksTexture else TextureMap.locationItemsTexture)
				
				if (slotStack.item is ItemBlock && RenderBlocks.renderItemIn3d(Block.getBlockFromItem(slotStack.item).renderType)) {
					GL11.glScalef(0.6F, 0.6F, 0.6F)
					GL11.glRotatef(180F, 0F, 1F, 0F)
					GL11.glTranslatef(0F, 0.6F, 0F)
					
					RenderBlocks.getInstance().renderBlockAsItem(Block.getBlockFromItem(slotStack.item), slotStack.itemDamage, 1F)
				} else if (slotStack.item is ItemBlock || shouldRenderAsEntity(slotStack)) {
					var entityitem: EntityItem?
					GL11.glPushMatrix()
					
					if (slotStack.item is ItemBlock) {
						GL11.glScalef(1F, 1F, 1F)
					} else {
						GL11.glScalef(1.5F, 1.5F, 1.5F)
						GL11.glTranslatef(0F, -0.125F, 0F)
					}
					GL11.glRotatef(90F, 0F, 1F, 0F)
					
					val `is` = slotStack.copy()
					`is`.stackSize = 1
					entityitem = EntityItem(player.worldObj, 0.0, 0.0, 0.0, `is`)
					entityitem.hoverStart = 0.0f
					
					GL11.glEnable(GL11.GL_BLEND)
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
					RenderManager.instance.renderEntityWithPosYaw(entityitem, 0.0, 0.0, 0.0, 0.0f, 0.0f)
					GL11.glDisable(GL11.GL_BLEND)
					
					GL11.glPopMatrix()
				} else {
					
					GL11.glScalef(0.75F, 0.75F, 0.75F)
					GL11.glTranslatef(0F, 0F, 0.5F)
					GL11.glRotatef(90F, 0F, 1F, 0F)
					var renderPass = 0
					
					do {
						val icon = slotStack.item.getIcon(slotStack, renderPass)
						if (icon != null) {
							val color = Color(slotStack.item.getColorFromItemStack(slotStack, renderPass))
							GL11.glColor3ub(color.red.toByte(), color.green.toByte(), color.blue.toByte())
							val f = icon.minU
							val f1 = icon.maxU
							val f2 = icon.minV
							val f3 = icon.maxV
							
							ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.iconWidth, icon.iconHeight, 0.0625f)
							GL11.glColor3f(1.0f, 1.0f, 1.0f)
						}
						
						++renderPass
					} while (renderPass < slotStack.item.getRenderPasses(slotStack.itemDamage))
				}
			}
			GL11.glPopMatrix()
			
			GL11.glPushMatrix()
			GL11.glRotatef(180F, 1F, 0F, 0F)
			var a = alpha.toFloat()
			if (inside) {
				a += 0.3F
				y0 = -y.toDouble()
			}
			
			if (seg % 2 == 0)
				GL11.glColor4f(0.6F, 0.6F, 0.6F, a)
			else GL11.glColor4f(1F, 1F, 1F, a)
			
			GL11.glDisable(GL11.GL_CULL_FACE)
			val item = stack.item as ItemToolbelt
			GL11.glEnable(GL11.GL_BLEND)
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
			mc.renderEngine.bindTexture(item.getGlowResource())
			tess.startDrawingQuads()
			for (i in 0 until segAngles) {
				val ang = i + seg * segAngles + shift
				var xp = cos(ang * Math.PI / 180F) * s
				var zp = sin(ang * Math.PI / 180F) * s
				
				tess.addVertexWithUV(xp * m, y.toDouble(), zp * m, u.toDouble(), v.toDouble())
				tess.addVertexWithUV(xp, y0, zp, u.toDouble(), 0.0)
				
				xp = cos((ang + 1) * Math.PI / 180F) * s
				zp = sin((ang + 1) * Math.PI / 180F) * s
				
				tess.addVertexWithUV(xp, y0, zp, 0.0, 0.0)
				tess.addVertexWithUV(xp * m, y.toDouble(), zp * m, 0.0, v.toDouble())
			}
			y0 = 0.0
			tess.draw()
			
			GL11.glColor4f(1f, 1f, 1f, 1f)
			GL11.glDisable(GL11.GL_BLEND)
			GL11.glEnable(GL11.GL_CULL_FACE)
			
			GL11.glPopMatrix()
		}
		GL11.glPopMatrix()
	}
}