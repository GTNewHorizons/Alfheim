package alfheim.common.lexicon.page;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

import com.google.common.base.Joiner;

import alfheim.api.ModInfo;
import alfheim.api.lib.LibResourceLocations;
import alfheim.api.spell.SpellBase;
import alfheim.client.gui.GUISpells;
import alfheim.common.core.asm.AlfheimHookHandler;
import alfheim.common.core.util.AlfheimConfig;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import vazkii.botania.api.internal.IGuiLexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.common.block.tile.mana.TilePool;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.lexicon.page.PageText;

public class PageSpell extends LexiconPage {

	SpellBase spell;
	
	public PageSpell(SpellBase s) {
		super("botania.page." + s.name);
		spell = s;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderScreen(IGuiLexiconEntry gui, int mx, int my) {
		int xn = gui.getLeft(), xx = xn + gui.getWidth(), yn = gui.getTop(), yx = yn + gui.getHeight();
		FontRenderer font = Minecraft.getMinecraft().fontRenderer;
		boolean unicode = font.getUnicodeFlag();
		font.setUnicodeFlag(true);
		String text;
		
		glPushMatrix();
		glTranslated(xn, yn, 0);
		
		glPushMatrix();
		glTranslated(gui.getWidth() / 2 - 16, 8, 0);
		GUISpells.drawRect(spell.hard ? LibResourceLocations.spellFrameEpic : LibResourceLocations.spellFrame, 32);
		glTranslated(8, 8, 0);
		GUISpells.drawRect(LibResourceLocations.spell(spell.name), 16);
		text = EnumChatFormatting.BOLD + StatCollector.translateToLocal("spell." + spell.name + ".name");
		font.drawString(text, font.getStringWidth(text) / -2 + 8, 24, 0);
		glColor4d(1, 1, 1, 1);
		
		glTranslated(-32, 0, 0);
		GUISpells.drawRect(LibResourceLocations.affinities[spell.race.ordinal()], 16);
		glTranslated(64, 0, 0);
		glMatrixMode(GL_TEXTURE);
		glScaled(-1, 1, 1);
		GUISpells.drawRect(LibResourceLocations.affinities[spell.race.ordinal()], 16);
		glScaled(-1, 1, 1);
		glMatrixMode(GL_MODELVIEW);
		
		glPopMatrix();
		String s = StatCollector.translateToLocal("lexicon.seconds");
		text = String.format(StatCollector.translateToLocal("lexicon.time") + s, spell.getCastTime() / 20.0).replaceAll("\\.0" + s, s);
		font.drawString(text, 16, 48, 0);
		text = String.format(StatCollector.translateToLocal("lexicon.cd") + s, spell.getCooldown() / 20.0).replaceAll("\\.0" + s, s);
		font.drawString(text, gui.getWidth() - font.getStringWidth(text) - 16, 48, 0);
		
		{ // Mana bar
			glTranslated(-xn, -yn-50, 0);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			
			int ratio = 10;
			int x = xn + gui.getWidth() / 2 - 50;
			int y = yn + 115;
			if(mx > x + 1 && mx <= x + 101 && my > y - 52 && my <= y - 38) ratio = 1;
			
			if (AlfheimConfig.numericalMana) {
				font.drawString(StatCollector.translateToLocal("lexicon.mana"), xn + 16, y - 8, 0);
				font.drawString("/" + TilePool.MAX_MANA / ratio, xn + (gui.getWidth() / 2), y - 8, 0x0000FF);
				text = spell.getManaCost() + "";
				font.drawString(text, xn + (gui.getWidth() / 2) - font.getStringWidth(text), y - 8, 0x0000FF);
			}
			
			AlfheimHookHandler.numMana = false;
			HUDHandler.renderManaBar(x, y, 0x0000FF, 0.75F, spell.getManaCost(), TilePool.MAX_MANA / ratio);
			AlfheimHookHandler.numMana = true;
			
			text = String.format(StatCollector.translateToLocal("botaniamisc.ratio"), ratio);
			font.drawString(text, x + 50 - font.getStringWidth(text) / 2, y + 5, 0x99000000);
	
			glDisable(GL_BLEND);
			glTranslated(xn, yn, 0);
		}
		
		glPopMatrix();
		font.setUnicodeFlag(unicode);
		
		PageText.renderText(xn + 16, yn + 72, gui.getWidth() - 30, gui.getHeight(), getUnlocalizedName());
	}
}