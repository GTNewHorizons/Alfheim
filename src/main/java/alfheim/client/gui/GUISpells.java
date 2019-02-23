package alfheim.client.gui;

import static org.lwjgl.opengl.GL11.*;

import alexsocol.asjlib.ASJUtilities;
import alfheim.api.AlfheimAPI;
import alfheim.api.entity.EnumRace;
import alfheim.api.lib.LibResourceLocations;
import alfheim.api.spell.SpellBase;
import alfheim.client.core.handler.CardinalSystemClient;
import alfheim.client.core.handler.CardinalSystemClient.SpellCastingSystemClient;
import alfheim.client.core.handler.KeyBindingHandlerClient;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

public class GUISpells extends Gui {
	
	private Minecraft mc;
	
	public GUISpells(Minecraft mc) {
		super();
		this.mc = mc;
	}
	
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onOverlayRendering(RenderGameOverlayEvent.Post e) {
		if (e.type != ElementType.HOTBAR) return;
		glPushMatrix();
		glTranslated(-1, -1, 0);
		
		TextureManager eng = mc.renderEngine;
        FontRenderer font = mc.fontRenderer;
        Tessellator tes = Tessellator.instance;
        
        int width = e.resolution.getScaledWidth();
        int height = e.resolution.getScaledHeight();

        zLevel = -90.0F;
        
        int count = AlfheimAPI.getSpellsFor(EnumRace.getByID(KeyBindingHandlerClient.raceID)).size();
        int length = count >= 5 ? 81 : 1 + (count-1) * 20;
        int rID = KeyBindingHandlerClient.raceID;
        int pos = KeyBindingHandlerClient.spellID;
		if (count >= 5) {
			if (pos == 0);
			else if (pos == 1);
			else if (pos == count - 1) pos = 4;
			else if (pos == count - 2) pos = 3;
			else pos = 2;
		}
        
		glColor4d(1, 1, 1, 1);
		glEnable(GL_ALPHA_TEST);
		
		glPushMatrix();
		glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        
        // ################ cast time ################
        if (CardinalSystemClient.segment().init > 0) {
	        glDisable(GL_TEXTURE_2D);
	        ASJUtilities.glColor1u(0x88607580);
	        drawTexturedModalRect(e.resolution.getScaledWidth() / 2 - 25, e.resolution.getScaledHeight() / 2 + 8, 0, 0, 52, 5);
	        ASJUtilities.glColor1u(0x8800aaff);
	        drawTexturedModalRect(e.resolution.getScaledWidth() / 2 - 24, e.resolution.getScaledHeight() / 2 + 9, 0, 0, 50 - (int) (50.0 * (CardinalSystemClient.segment.init / (double) CardinalSystemClient.segment.initM)), 3);
	        glColor4d(1, 1, 1, 1);
	        glEnable(GL_TEXTURE_2D);
        }
        
        // ################ horizontal bar ################
        eng.bindTexture(LibResourceLocations.widgets);
        drawTexturedModalRect(21, height - 43, 0, 0, length, 22);
        drawTexturedModalRect(21 + length, height - 43, 161, 0, 22, 22);
        drawTexturedModalRect(20 + pos * 20, height - 44, 0, 22, 24, 24);
		
        // ################ spells icons ################
        glPushMatrix();
    	glTranslated(24, height - 40, 0);
        for (int i = 0; i < Math.min(count, 5); i++) {
        	if (i != pos) glColor4d(1, 1, 1, 0.5); else glColor4d(1, 1, 1, 1);
        	SpellBase spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID);
        	if (i == 0) {
        		if (pos == 0);
    			else if (pos == 1) spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID - 1);
    			else spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID - pos);
        	} else
        	if (i == 1) {
        		if (pos == 0) spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID + 1);
        		else if (pos == 1);
        		else spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID - (pos - 1));
        	} else
        	if (i == 2) {
        		if (pos == 0) spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID + 2);
        		else if (pos == 1) spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID + 1);
        		else if (pos == 3) spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID - 1);
        		else if (pos == 4) spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID - 2);
        	} else
        	if (i == 3) {
        		if (pos == 0) spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID + 3);
        		else if (pos == 1) spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID + 2);
        		else if (pos == 2) spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID + 1);
        		else if (pos == 4) spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID - 1);
        	} else
        	if (i == 4) {
        		if (pos == 0) spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID + 4);
        		else if (pos == 1) spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID + 3);
        		else if (pos == 2) spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID + 2);
        		else if (pos == 3) spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID + 1);
        	}
        	drawRect(LibResourceLocations.spell(spell.name), 16);
        	
        	if (SpellCastingSystemClient.getCoolDown(spell) > 0) {
        		glDisable(GL_TEXTURE_2D);
                glColor4d(0, 0, 0, 0.5);
        		drawRect(16);
                glColor4d(1, 1, 1, 1);
        		glEnable(GL_TEXTURE_2D);
        		String ttt = ticksToTime(SpellCastingSystemClient.getCoolDown(spell));
        		font.drawString(ttt, 2 + (12 - font.getStringWidth(ttt)) / 2, 4, 0xFFFFFF);
        	}
        	
        	glTranslated(20, 0, 0);
        }
        
        glEnable(GL_TEXTURE_2D);
        glPopMatrix();
        
        glColor4d(1, 1, 1, 1);
        
        // ################ Vertical bar ################
        glPushMatrix();
        glTranslated(0, height - 62, 0);
		eng.bindTexture(LibResourceLocations.spellRace);
		tes.startDrawingQuads();
		tes.addVertexWithUV(0, 0, 0, 1, 0);
		tes.addVertexWithUV(0, 60, 0, 1, 1);
		tes.addVertexWithUV(24, 60, 0, 0, 1);
		tes.addVertexWithUV(24, 0, 0, 0, 0);
		tes.draw();
        glPopMatrix();
        
        // ################ Race icons ################
        glPopMatrix();
        glPushMatrix();
		glTranslated(4, height - 40, 0);
		drawRect(LibResourceLocations.affinities[rID], 16);

		glColor4d(1, 1, 1, 0.75);
		
		glTranslated(0, 20, 0);
		int nrID = rID - 1;
		nrID = nrID < 1 ? 9 : nrID;
		drawRect(LibResourceLocations.affinities[nrID], 16);
		
        glTranslated(0, -40, 0);
		nrID = rID + 1;
		nrID = nrID > 9 ? 1 : nrID;
		drawRect(LibResourceLocations.affinities[nrID], 16);
        glColor4d(1, 1, 1, 1);
        glPopMatrix();
        
        // ################ Spell name ################
        SpellBase spell = AlfheimAPI.getSpellByIDs(KeyBindingHandlerClient.raceID, KeyBindingHandlerClient.spellID);
        font.drawString(StatCollector.translateToLocal("spell." + spell.name + ".name"), 24, height - 18, ASJUtilities.enumColorToRGB(EnumRace.getEnumColor(KeyBindingHandlerClient.raceID)));
        
        // ################################################################ HOTSPELLS ################################################################
        
        glPushMatrix();
        glColor4f(1, 1, 1, 1);
        Minecraft.getMinecraft().renderEngine.bindTexture(LibResourceLocations.hotSpells);
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.addVertexWithUV(width - 22	, height / 2 - 121, 0, 0, 0);
		Tessellator.instance.addVertexWithUV(width - 22	, height / 2 + 121, 0, 0, 1);
		Tessellator.instance.addVertexWithUV(width		, height / 2 + 121, 0, 1, 1);
		Tessellator.instance.addVertexWithUV(width		, height / 2 - 121, 0, 1, 0);
		Tessellator.instance.draw();
		
		glTranslated(width - 19, height / 2 - 138, 0);
		String txt;
		for (int i = 0; i < CardinalSystemClient.segment().hotSpells.length; i++) {
			glTranslated(0, 20, 0);
			spell = AlfheimAPI.getSpellByIDs((CardinalSystemClient.segment.hotSpells[i] >> 28) & 0xF, CardinalSystemClient.segment.hotSpells[i] & 0xFFFFFFF);
			if (spell == null) continue;
			glColor4d(1, 1, 1, 1);
			drawRect(LibResourceLocations.spell(spell.name), 16);
			if (SpellCastingSystemClient.getCoolDown(spell) > 0) {
        		glDisable(GL_TEXTURE_2D);
                glColor4d(0, 0, 0, 0.5);
        		drawRect(16);
        		glEnable(GL_TEXTURE_2D);
        		txt = ticksToTime(SpellCastingSystemClient.getCoolDown(spell));
        		font.drawString(txt, 2 + (12 - font.getStringWidth(txt)) / 2, 4, 0xFFFFFF);
        	}
			//txt = StatCollector.translateToLocal("spell." + spell.name + ".name");
			//font.drawString(txt, -font.getStringWidth(txt) - 4, 4, EnumRace.getRGBColor((CardinalSystemClient.segment.hotSpells[i] >> 28) & 0xF));
		}
		
        glPopMatrix();
        
        glDisable(GL_BLEND);
        glPopMatrix();
	}
	
	public static String ticksToTime(int ticks) {
		if (ticks < 20) return "\u00a7f" + ticks;
		if (ticks < 1200) return "\u00a7e" + ticks/20;
		if (ticks < 72000) return "\u00a76" + ticks/1200;
		if (ticks < 720000) return "\u00a7c" + ticks/72000;
		return "\u00a74omg";
	}
	
	public static void drawRect(int size) {
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.addVertex(0, 0, 0);
		Tessellator.instance.addVertex(0, size, 0);
		Tessellator.instance.addVertex(size, size, 0);
		Tessellator.instance.addVertex(size, 0, 0);
		Tessellator.instance.draw();
	}
	
	public static void drawRect(ResourceLocation texture, int size) {
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.addVertexWithUV(0, 0, 0, 0, 0);
		Tessellator.instance.addVertexWithUV(0, size, 0, 0, 1);
		Tessellator.instance.addVertexWithUV(size, size, 0, 1, 1);
		Tessellator.instance.addVertexWithUV(size, 0, 0, 1, 0);
		Tessellator.instance.draw();
	}
}