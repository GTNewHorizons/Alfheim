package alfheim.common.lexicon.page

import alexsocol.asjlib.ASJUtilities
import alfheim.client.core.util.*
import alfheim.common.core.util.*
import cpw.mods.fml.relauncher.*
import net.minecraft.client.gui.*
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.entity.RenderItem
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.stats.Achievement
import net.minecraft.util.*
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12.GL_RESCALE_NORMAL
import vazkii.botania.api.internal.IGuiLexiconEntry
import vazkii.botania.api.lexicon.LexiconPage
import vazkii.botania.api.lexicon.multiblock.MultiblockSet
import vazkii.botania.client.core.handler.MultiblockRenderHandler
import vazkii.botania.client.lib.LibResources
import java.util.*
import kotlin.math.*

class PageMultiblockLearnable(unName: String, internal val setUn: MultiblockSet, internal val set: MultiblockSet, internal val achievement: Achievement): LexiconPage(unName) {
	internal lateinit var button: GuiButton
	internal val mb  = set.getForIndex(0)
	internal val mbUn = setUn.getForIndex(0)
	internal var ticksElapsed: Int = 0
	
	internal val buttonStr: String
		get() {
			val set = if (known()) set else setUn
			return StatCollector.translateToLocal(if (MultiblockRenderHandler.currentMultiblock === set) "botaniamisc.unvisualize" else "botaniamisc.visualize")
		}
	
	@SideOnly(Side.CLIENT)
	override fun renderScreen(gui: IGuiLexiconEntry, mx: Int, my: Int) {
		val render = mc.renderEngine
		render.bindTexture(multiblockOverlay)
		
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		glDisable(GL_ALPHA_TEST)
		glColor4f(1f, 1f, 1f, 1f)
		(gui as GuiScreen).drawTexturedModalRect(gui.left, gui.top, 0, 0, gui.getWidth(), gui.getHeight())
		glDisable(GL_BLEND)
		glEnable(GL_ALPHA_TEST)
		
		val maxX = 90f
		val maxY = 60f
		glPushMatrix()
		glTranslatef((gui.left + gui.getWidth() / 2).F, (gui.top + 90).F, gui.zLevel + 100f)
		
		val m = if (known()) mb else mbUn
		
		glScalef(-min(maxY / m.ySize.F, maxX / sqrt((m.xSize * m.xSize + m.zSize * m.zSize).D).F))
		
		glRotatef(-20f, 1f, 0f, 0f)
		glRotatef(gui.elapsedTicks, 0f, 1f, 0f)
		
		MultiblockRenderHandler.renderMultiblockOnPage(m)
		
		glPopMatrix()
		
		val font = mc.fontRenderer
		val unicode = font.unicodeFlag
		val s = EnumChatFormatting.BOLD.toString() + StatCollector.translateToLocal(getUnlocalizedName())
		font.unicodeFlag = true
		font.drawString(s, gui.left + gui.getWidth() / 2 - font.getStringWidth(s) / 2, gui.top + 16, 0x000000)
		font.unicodeFlag = unicode
		
		glEnable(GL_RESCALE_NORMAL)
		RenderHelper.enableGUIStandardItemLighting()
		val x = gui.left + 15
		val y = gui.top + 25
		RenderItem.getInstance().renderItemIntoGUI(font, render, ItemStack(Blocks.stonebrick), x, y)
		RenderHelper.disableStandardItemLighting()
		glDisable(GL_RESCALE_NORMAL)
		
		glPushMatrix()
		glTranslatef(0f, 0f, 200f)
		if (mx >= x && mx < x + 16 && my >= y && my < y + 16) {
			val mats = ArrayList<String>()
			mats.add(StatCollector.translateToLocal("botaniamisc.materialsRequired"))
			for (stack in m.materials) {
				var size = "" + stack.stackSize
				if (size.length < 2)
					size = "0$size"
				mats.add(" " + EnumChatFormatting.AQUA + size + " " + EnumChatFormatting.GRAY + stack.displayName)
			}
			
			vazkii.botania.client.core.helper.RenderHelper.renderTooltip(mx, my, mats)
		}
		glPopMatrix()
	}
	
	override fun onOpened(gui: IGuiLexiconEntry) {
		button = GuiButton(101, gui.left + 30, gui.top + gui.height - 50, gui.width - 60, 20, buttonStr)
		gui.buttonList.add(button)
	}
	
	override fun onClosed(gui: IGuiLexiconEntry) {
		gui.buttonList.remove(button)
	}
	
	@SideOnly(Side.CLIENT)
	override fun onActionPerformed(gui: IGuiLexiconEntry?, button: GuiButton?) {
		val set = if (known()) set else setUn
		if (button === this.button) {
			if (MultiblockRenderHandler.currentMultiblock === set)
				MultiblockRenderHandler.setMultiblock(null)
			else
				MultiblockRenderHandler.setMultiblock(set)
			button.displayString = buttonStr
		}
	}
	
	@SideOnly(Side.CLIENT)
	override fun updateScreen() {
		++ticksElapsed
	}
	
	fun known(): Boolean {
		return if (mc.thePlayer == null) false else mc.thePlayer.hasAchievement(achievement)
	}
	
	override fun getUnlocalizedName(): String {
		return if (ASJUtilities.isServer || known()) unlocalizedName else unlocalizedName + "u"
	}
	
	companion object {
		
		private val multiblockOverlay = ResourceLocation(LibResources.GUI_MULTIBLOCK_OVERLAY)
	}
}