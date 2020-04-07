package alfheim.common.lexicon.page

import alexsocol.asjlib.*
import alfheim.api.entity.race
import alfheim.api.lib.LibResourceLocations
import alfheim.api.spell.SpellBase
import alfheim.client.gui.GUISpells
import alfheim.common.core.asm.AlfheimHookHandler
import alfheim.common.core.handler.AlfheimConfigHandler
import cpw.mods.fml.relauncher.*
import net.minecraft.util.*
import org.lwjgl.opengl.GL11.*
import vazkii.botania.api.internal.IGuiLexiconEntry
import vazkii.botania.api.lexicon.LexiconPage
import vazkii.botania.client.core.handler.HUDHandler
import vazkii.botania.common.block.tile.mana.TilePool
import vazkii.botania.common.lexicon.page.PageText

class PageSpell(internal val spell: SpellBase): LexiconPage("botania.page." + spell.name) {
	
	@SideOnly(Side.CLIENT)
	override fun renderScreen(gui: IGuiLexiconEntry, mx: Int, my: Int) {
		val xn = gui.left
		val yn = gui.top
		val font = mc.fontRenderer
		val unicode = font.unicodeFlag
		font.unicodeFlag = true
		var text: String
		
		glPushMatrix()
		glTranslated(xn.D, yn.D, 0.0)
		
		glPushMatrix()
		glTranslated((gui.width / 2 - 16).D, 8.0, 0.0)
		GUISpells.drawRect(if (spell.hard) LibResourceLocations.spellFrameEpic else LibResourceLocations.spellFrame, 32)
		glTranslated(8.0, 8.0, 0.0)
		GUISpells.drawRect(LibResourceLocations.spell(spell.name), 16)
		text = EnumChatFormatting.BOLD.toString() + StatCollector.translateToLocal("spell." + spell.name + ".name")
		font.drawString(text, font.getStringWidth(text) / -2 + 8, 24, 0)
		glColor4d(1.0, 1.0, 1.0, 1.0)
		
		glTranslated(-32.0, 0.0, 0.0)
		GUISpells.drawRect(LibResourceLocations.affinities[spell.race.ordinal], 16)
		glTranslated(64.0, 0.0, 0.0)
		glMatrixMode(GL_TEXTURE)
		glScaled(-1.0, 1.0, 1.0)
		GUISpells.drawRect(LibResourceLocations.affinities[spell.race.ordinal], 16)
		glScaled(-1.0, 1.0, 1.0)
		glMatrixMode(GL_MODELVIEW)
		
		glPopMatrix()
		val s = StatCollector.translateToLocal("lexicon.seconds")
		text = String.format(StatCollector.translateToLocal("lexicon.time") + s, spell.getCastTime() / 20.0).replace("\\.0$s".toRegex(), s)
		font.drawString(text, 16, 48, 0)
		text = String.format(StatCollector.translateToLocal("lexicon.cd") + s, spell.getCooldown() / 20.0).replace("\\.0$s".toRegex(), s)
		font.drawString(text, gui.width - font.getStringWidth(text) - 16, 48, 0)
		
		run {
			// Mana bar
			glTranslated((-xn).D, (-yn - 50).D, 0.0)
			glEnable(GL_BLEND)
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
			
			var ratio = 10
			val x = xn + gui.width / 2 - 50
			val y = yn + 115
			if (mx > x + 1 && mx <= x + 101 && my > y - 52 && my <= y - 38) ratio = 1
			
			val cost = spell.getManaCost().times(if (spell.race == mc.thePlayer.race || spell.hard) 1.toByte() else AlfheimConfigHandler.raceManaMult).I
			
			if (AlfheimConfigHandler.numericalMana) {
				font.drawString(StatCollector.translateToLocal("lexicon.mana"), xn + 16, y - 8, 0)
				font.drawString("/" + TilePool.MAX_MANA / ratio, xn + gui.width / 2, y - 8, 0x0000FF)
				text = cost.toString()
				font.drawString(text, xn + gui.width / 2 - font.getStringWidth(text), y - 8, 0x0000FF)
			}
			
			AlfheimHookHandler.numMana = false
			HUDHandler.renderManaBar(x, y, 0x0000FF, 0.75f, cost, TilePool.MAX_MANA / ratio)
			AlfheimHookHandler.numMana = true
			
			text = String.format(StatCollector.translateToLocal("botaniamisc.ratio"), ratio)
			font.drawString(text, x + 50 - font.getStringWidth(text) / 2, y + 5, -0x67000000)
			
			glDisable(GL_BLEND)
			glTranslated(xn.D, yn.D, 0.0)
		}
		
		glPopMatrix()
		font.unicodeFlag = unicode
		
		PageText.renderText(xn + 16, yn + 72, gui.width - 30, gui.height, getUnlocalizedName())
	}
}