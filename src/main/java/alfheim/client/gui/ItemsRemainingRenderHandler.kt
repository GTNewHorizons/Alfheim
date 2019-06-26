package alfheim.client.gui

import cpw.mods.fml.relauncher.*
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.entity.RenderItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.*
import net.minecraft.util.EnumChatFormatting

import java.util.regex.Pattern

import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12.GL_RESCALE_NORMAL

object ItemsRemainingRenderHandler {
	
	private val maxTicks = 30
	private val leaveTicks = 20
	
	private var stack = ItemStack(Blocks.stone)
	private var customString: String? = null
	private var ticks: Int = 0
	private var count: Int = 0
	
	@SideOnly(Side.CLIENT)
	fun render(resolution: ScaledResolution, partTicks: Float) {
		if (ticks > 0 && isNotEmpty(stack)) {
			val pos = maxTicks - ticks
			val mc = Minecraft.getMinecraft()
			val x = resolution.scaledWidth / 2 + 10 + Math.max(0, pos - leaveTicks)
			val y = resolution.scaledHeight / 2
			
			val start = maxTicks - leaveTicks
			val alpha = if (ticks + partTicks > start) 1f else (ticks + partTicks) / start
			
			glDisable(GL_ALPHA_TEST)
			glEnable(GL_BLEND)
			glEnable(GL_RESCALE_NORMAL)
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
			
			glColor4f(1f, 1f, 1f, alpha)
			RenderHelper.enableGUIStandardItemLighting()
			val xp = x + (16f * (1f - alpha)).toInt()
			glTranslatef(xp.toFloat(), y.toFloat(), 0f)
			glScalef(alpha, 1f, 1f)
			RenderItem.getInstance().renderItemAndEffectIntoGUI(mc.fontRenderer, mc.renderEngine, stack, 0, 0)
			glScalef(1f / alpha, 1f, 1f)
			glTranslatef((-xp).toFloat(), (-y).toFloat(), 0f)
			RenderHelper.disableStandardItemLighting()
			glColor4f(1f, 1f, 1f, 1f)
			glEnable(GL_BLEND)
			
			var text = ""
			
			if (customString == null) {
				if (isNotEmpty(stack)) {
					text = EnumChatFormatting.GREEN.toString() + stack.displayName
					if (count >= 0) {
						val max = stack.maxStackSize
						val stacks = count / max
						val rem = count % max
						
						if (stacks == 0)
							text = "" + count
						else
							text = count.toString() + " (" + EnumChatFormatting.AQUA + stacks + EnumChatFormatting.RESET + "*" + EnumChatFormatting.GRAY + max + EnumChatFormatting.RESET + "+" + EnumChatFormatting.YELLOW + rem + EnumChatFormatting.RESET + ")"
					} else if (count == -1)
						text = "\u221E"
				}
			} else
				text = customString
			
			val color = 0x00FFFFFF or ((alpha * 0xFF).toInt() shl 24)
			mc.fontRenderer.drawStringWithShadow(text, x + 20, y + 6, color)
			
			glDisable(GL_BLEND)
			glEnable(GL_ALPHA_TEST)
		}
	}
	
	@SideOnly(Side.CLIENT)
	fun tick() {
		if (ticks > 0)
			--ticks
	}
	
	operator fun set(stack: ItemStack, str: String) {
		set(stack, 0, str)
	}
	
	@JvmOverloads
	fun set(stack: ItemStack, count: Int, str: String? = null) {
		ItemsRemainingRenderHandler.stack = stack
		ItemsRemainingRenderHandler.count = count
		ItemsRemainingRenderHandler.customString = str
		ticks = if (stack.item === Item.getItemFromBlock(Blocks.air)) 0 else maxTicks
	}
	
	operator fun set(player: EntityPlayer, displayStack: ItemStack, pattern: Pattern) {
		var count = 0
		for (i in 0 until player.inventory.sizeInventory) {
			val stack = player.inventory.getStackInSlot(i)
			if (isNotEmpty(stack) && pattern.matcher(stack.displayName).find())
				count += stack.stackSize
		}
		
		set(displayStack, count)
	}
	
	fun isNotEmpty(stack: ItemStack): Boolean {
		return stack.item !== Item.getItemFromBlock(Blocks.air)
	}
}