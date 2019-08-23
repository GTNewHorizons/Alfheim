package alfheim.common.item.equipment.bauble

import alfheim.AlfheimCore
import alfheim.common.network.Message0d
import alfheim.common.network.Message0d.m0d
import baubles.api.BaubleType
import baubles.common.lib.PlayerHandler
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent
import cpw.mods.fml.relauncher.*
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.*
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.MathHelper
import org.lwjgl.opengl.GL11.*
import vazkii.botania.client.core.handler.ClientTickHandler
import vazkii.botania.common.core.helper.*
import vazkii.botania.common.item.equipment.bauble.ItemBauble

class ItemDodgeRing: ItemBauble("DodgeRing") {
	
	init {
		FMLCommonHandler.instance().bus().register(this)
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	fun onKeyDown(event: KeyInputEvent) {
		val mc = Minecraft.getMinecraft()
		
		val baublesInv = PlayerHandler.getPlayerBaubles(mc.thePlayer)
		var ringStack: ItemStack? = baublesInv.getStackInSlot(1)
		if (ringStack == null || ringStack.item !is ItemDodgeRing) {
			ringStack = baublesInv.getStackInSlot(2)
			if (ringStack == null || ringStack.item !is ItemDodgeRing)
				return
		}
		
		if (ItemNBTHelper.getInt(ringStack, TAG_DODGE_COOLDOWN, 0) > 0) return
		
		val threshold = 4
		if (mc.gameSettings.keyBindLeft.isKeyPressed && !oldLeftDown) {
			val oldLeft = leftDown
			leftDown = ClientTickHandler.ticksInGame
			
			if (leftDown - oldLeft < threshold) dodge(mc.thePlayer, ringStack, true)
		} else if (mc.gameSettings.keyBindRight.isKeyPressed && !oldRightDown) {
			val oldRight = rightDown
			rightDown = ClientTickHandler.ticksInGame
			
			if (rightDown - oldRight < threshold) dodge(mc.thePlayer, ringStack, false)
		}
		
		oldLeftDown = mc.gameSettings.keyBindLeft.isKeyPressed
		oldRightDown = mc.gameSettings.keyBindRight.isKeyPressed
	}
	
	override fun onWornTick(stack: ItemStack, player: EntityLivingBase) {
		val cd = ItemNBTHelper.getInt(stack, TAG_DODGE_COOLDOWN, 0)
		if (cd > 0) ItemNBTHelper.setInt(stack, TAG_DODGE_COOLDOWN, cd - 1)
	}
	
	override fun getBaubleType(arg0: ItemStack): BaubleType {
		return BaubleType.RING
	}
	
	companion object {
		
		val TAG_DODGE_COOLDOWN = "dodgeCooldown"
		val MAX_CD = 20
		
		private var oldLeftDown: Boolean = false
		private var oldRightDown: Boolean = false
		private var leftDown: Int = 0
		private var rightDown: Int = 0
		
		private fun dodge(player: EntityPlayer, stack: ItemStack, left: Boolean) {
			if (player.capabilities.isFlying || !player.onGround || player.moveForward > 0.2 || player.moveForward < -0.2) return
			
			val yaw = player.rotationYaw
			val x = MathHelper.sin(-yaw * 0.017453292f - Math.PI.toFloat())
			val z = MathHelper.cos(-yaw * 0.017453292f - Math.PI.toFloat())
			val lookVec = Vector3(x.toDouble(), 0.0, z.toDouble())
			val sideVec = lookVec.crossProduct(Vector3(0.0, (if (left) 1 else -1).toDouble(), 0.0)).multiply(1.25)
			
			player.motionX = sideVec.x
			player.motionY = sideVec.y
			player.motionZ = sideVec.z
			
			AlfheimCore.network.sendToServer(Message0d(m0d.DODGE))
			// stupid singleplayer NBT autosync -_-
			if (!Minecraft.getMinecraft().isSingleplayer) ItemNBTHelper.setInt(stack, TAG_DODGE_COOLDOWN, MAX_CD)
		}
		
		@SideOnly(Side.CLIENT)
		fun renderHUD(resolution: ScaledResolution, player: EntityPlayer, stack: ItemStack, pticks: Float) {
			val xo = resolution.scaledWidth / 2 - 20
			val y = resolution.scaledHeight / 2 + 20
			
			if (!player.capabilities.isFlying) {
				val cd = ItemNBTHelper.getInt(stack, TAG_DODGE_COOLDOWN, 0)
				val width = Math.min(((cd - pticks) * 2).toInt(), 40)
				glColor4d(1.0, 1.0, 1.0, 1.0)
				if (width > 0) {
					Gui.drawRect(xo, y - 2, xo + 40, y - 1, -0x78000000)
					Gui.drawRect(xo, y - 2, xo + width, y - 1, -0x1)
				}
			}
			
			glEnable(GL_ALPHA_TEST)
			glColor4d(1.0, 1.0, 1.0, 1.0)
		}
	}
}