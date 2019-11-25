package alfheim.client.gui

import alexsocol.asjlib.render.ASJRenderHelper
import alfheim.api.AlfheimAPI
import alfheim.api.entity.EnumRace
import alfheim.api.lib.LibResourceLocations
import alfheim.client.core.handler.CardinalSystemClient.PlayerSegmentClient
import alfheim.client.core.handler.CardinalSystemClient.SpellCastingSystemClient
import alfheim.client.core.handler.KeyBindingHandlerClient
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.Tessellator
import net.minecraft.util.*
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType
import org.lwjgl.opengl.GL11.*
import kotlin.math.min

class GUISpells(private val mc: Minecraft): Gui() {
	
	@SubscribeEvent
	fun onOverlayRendering(e: RenderGameOverlayEvent.Post) {
		if (e.type != ElementType.ALL) return
		glPushMatrix()
		glTranslated(-1.0, -1.0, 0.0)
		
		val eng = mc.renderEngine
		val font = mc.fontRenderer
		val tes = Tessellator.instance
		
		val width = e.resolution.scaledWidth
		val height = e.resolution.scaledHeight
		
		zLevel = -90.0f
		
		val count = AlfheimAPI.getSpellsFor(EnumRace[KeyBindingHandlerClient.raceID]).size
		val length = if (count >= 5) 81 else 1 + (count - 1) * 20
		val rID = KeyBindingHandlerClient.raceID
		var pos = KeyBindingHandlerClient.spellID
		if (count >= 5) {
			pos = when (pos) {
				0, 1      -> pos
				count - 1 -> 4
				count - 2 -> 3
				else      -> 2
			}
		}
		
		glColor4d(1.0, 1.0, 1.0, 1.0)
		glEnable(GL_ALPHA_TEST)
		
		glPushMatrix()
		glEnable(GL_BLEND)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		
		// ################ cast time ################
		if (PlayerSegmentClient.init > 0) {
			glDisable(GL_TEXTURE_2D)
			ASJRenderHelper.glColor1u(-0x779f8a80)
			drawTexturedModalRect(e.resolution.scaledWidth / 2 - 25, e.resolution.scaledHeight / 2 + 8, 0, 0, 52, 5)
			ASJRenderHelper.glColor1u(-0x77ff5501)
			drawTexturedModalRect(e.resolution.scaledWidth / 2 - 24, e.resolution.scaledHeight / 2 + 9, 0, 0, 50 - (50.0 * (PlayerSegmentClient.init / PlayerSegmentClient.initM.toDouble())).toInt(), 3)
			glColor4d(1.0, 1.0, 1.0, 1.0)
			glEnable(GL_TEXTURE_2D)
		}
		
		// ################ horizontal bar ################
		eng.bindTexture(LibResourceLocations.widgets)
		drawTexturedModalRect(21, height - 43, 0, 0, length, 22)
		drawTexturedModalRect(21 + length, height - 43, 161, 0, 22, 22)
		drawTexturedModalRect(20 + pos * 20, height - 44, 0, 22, 24, 24)
		
		// ################ spells icons ################
		glPushMatrix()
		glTranslated(24.0, (height - 40).toDouble(), 0.0)
		for (i in 0 until min(count, 5)) {
			if (i != pos) glColor4d(1.0, 1.0, 1.0, 0.5) else glColor4d(1.0, 1.0, 1.0, 1.0)
			
			var spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID)
			
			when (i) {
				0 -> when (pos) {
					0    -> Unit
					1    -> spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID - 1)
					else -> spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID - pos)
				}
				1 -> when (pos) {
					0    -> spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID + 1)
					1    -> Unit
					else -> spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID - (pos - 1))
				}
				2 -> when (pos) {
					0 -> spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID + 2)
					1 -> spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID + 1)
					3 -> spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID - 1)
					4 -> spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID - 2)
				}
				3 -> when (pos) {
					0 -> spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID + 3)
					1 -> spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID + 2)
					2 -> spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID + 1)
					4 -> spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID - 1)
				}
				4 -> when (pos) {
					0 -> spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID + 4)
					1 -> spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID + 3)
					2 -> spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID + 2)
					3 -> spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID + 1)
				}
			}
			
			if (spell == null) continue
			
			drawRect(LibResourceLocations.spell(spell.name), 16)
			
			if (SpellCastingSystemClient.getCoolDown(spell) > 0) {
				glDisable(GL_TEXTURE_2D)
				glColor4d(0.0, 0.0, 0.0, 0.5)
				drawRect(16)
				glColor4d(1.0, 1.0, 1.0, 1.0)
				glEnable(GL_TEXTURE_2D)
				val ttt = ticksToTime(SpellCastingSystemClient.getCoolDown(spell))
				font.drawString(ttt, 2 + (12 - font.getStringWidth(ttt)) / 2, 4, 0xFFFFFF)
			}
			
			glTranslated(20.0, 0.0, 0.0)
		}
		
		glEnable(GL_TEXTURE_2D)
		glPopMatrix()
		
		glColor4d(1.0, 1.0, 1.0, 1.0)
		
		// ################ Vertical bar ################
		glPushMatrix()
		glTranslated(0.0, (height - 62).toDouble(), 0.0)
		eng.bindTexture(LibResourceLocations.spellRace)
		tes.startDrawingQuads()
		tes.addVertexWithUV(0.0, 0.0, 0.0, 1.0, 0.0)
		tes.addVertexWithUV(0.0, 60.0, 0.0, 1.0, 1.0)
		tes.addVertexWithUV(24.0, 60.0, 0.0, 0.0, 1.0)
		tes.addVertexWithUV(24.0, 0.0, 0.0, 0.0, 0.0)
		tes.draw()
		glPopMatrix()
		
		// ################ Race icons ################
		glPopMatrix()
		glPushMatrix()
		glTranslated(4.0, (height - 40).toDouble(), 0.0)
		drawRect(LibResourceLocations.affinities[rID], 16)
		
		glColor4d(1.0, 1.0, 1.0, 0.75)
		
		glTranslated(0.0, 20.0, 0.0)
		var nrID = rID - 1
		nrID = if (nrID < 1) 9 else nrID
		drawRect(LibResourceLocations.affinities[nrID], 16)
		
		glTranslated(0.0, -40.0, 0.0)
		nrID = rID + 1
		nrID = if (nrID > 9) 1 else nrID
		drawRect(LibResourceLocations.affinities[nrID], 16)
		glColor4d(1.0, 1.0, 1.0, 1.0)
		glPopMatrix()
		
		// ################ Spell name ################
		var spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID)
		font.drawString(StatCollector.translateToLocal("spell." + spell!!.name + ".name"), 24, height - 18, ASJRenderHelper.enumColorToRGB(EnumRace.getEnumColor(KeyBindingHandlerClient.raceID.toDouble())))
		
		// ################################################################ HOTSPELLS ################################################################
		
		glPushMatrix()
		glColor4f(1f, 1f, 1f, 1f)
		Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.hotSpells)
		Tessellator.instance.startDrawingQuads()
		Tessellator.instance.addVertexWithUV((width - 22).toDouble(), (height / 2 - 121).toDouble(), 0.0, 0.0, 0.0)
		Tessellator.instance.addVertexWithUV((width - 22).toDouble(), (height / 2 + 121).toDouble(), 0.0, 0.0, 1.0)
		Tessellator.instance.addVertexWithUV(width.toDouble(), (height / 2 + 121).toDouble(), 0.0, 1.0, 1.0)
		Tessellator.instance.addVertexWithUV(width.toDouble(), (height / 2 - 121).toDouble(), 0.0, 1.0, 0.0)
		Tessellator.instance.draw()
		
		glTranslated((width - 19).toDouble(), (height / 2 - 138).toDouble(), 0.0)
		var txt: String
		for (i in PlayerSegmentClient.hotSpells.indices) {
			glTranslated(0.0, 20.0, 0.0)
			spell = AlfheimAPI.getSpellByIDs(PlayerSegmentClient.hotSpells[i] shr 28 and 0xF, PlayerSegmentClient.hotSpells[i] and 0xFFFFFFF)
			if (spell == null) continue
			glColor4d(1.0, 1.0, 1.0, 1.0)
			drawRect(LibResourceLocations.spell(spell.name), 16)
			if (SpellCastingSystemClient.getCoolDown(spell) > 0) {
				glDisable(GL_TEXTURE_2D)
				glColor4d(0.0, 0.0, 0.0, 0.5)
				drawRect(16)
				glEnable(GL_TEXTURE_2D)
				txt = ticksToTime(SpellCastingSystemClient.getCoolDown(spell))
				font.drawString(txt, 2 + (12 - font.getStringWidth(txt)) / 2, 4, 0xFFFFFF)
			}
			//txt = StatCollector.translateToLocal("spell." + spell.name + ".name");
			//font.drawString(txt, -font.getStringWidth(txt) - 4, 4, EnumRace.getRGBColor((PlayerSegmentClient.hotSpells[i] >> 28) & 0xF));
		}
		
		glPopMatrix()
		
		glDisable(GL_BLEND)
		glPopMatrix()
	}
	
	companion object {
		
		fun ticksToTime(ticks: Int): String {
			if (ticks < 20) return "\u00a7f$ticks"
			if (ticks < 1200) return "\u00a7e" + ticks / 20
			if (ticks < 72000) return "\u00a76" + ticks / 1200
			return if (ticks < 720000) "\u00a7c" + ticks / 72000 else "\u00a74omg"
		}
		
		fun drawRect(size: Int) {
			Tessellator.instance.startDrawingQuads()
			Tessellator.instance.addVertex(0.0, 0.0, 0.0)
			Tessellator.instance.addVertex(0.0, size.toDouble(), 0.0)
			Tessellator.instance.addVertex(size.toDouble(), size.toDouble(), 0.0)
			Tessellator.instance.addVertex(size.toDouble(), 0.0, 0.0)
			Tessellator.instance.draw()
		}
		
		fun drawRect(texture: ResourceLocation, size: Int) {
			Minecraft.getMinecraft().renderEngine.bindTexture(texture)
			Tessellator.instance.startDrawingQuads()
			Tessellator.instance.addVertexWithUV(0.0, 0.0, 0.0, 0.0, 0.0)
			Tessellator.instance.addVertexWithUV(0.0, size.toDouble(), 0.0, 0.0, 1.0)
			Tessellator.instance.addVertexWithUV(size.toDouble(), size.toDouble(), 0.0, 1.0, 1.0)
			Tessellator.instance.addVertexWithUV(size.toDouble(), 0.0, 0.0, 1.0, 0.0)
			Tessellator.instance.draw()
		}
	}
}