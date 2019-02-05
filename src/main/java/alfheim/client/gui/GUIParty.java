package alfheim.client.gui;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Collection;

import alexsocol.asjlib.ASJUtilities;
import alexsocol.asjlib.math.Vector3;
import alexsocol.asjlib.render.ASJShaderHelper;
import alfheim.api.entity.EnumRace;
import alfheim.api.lib.LibResourceLocations;
import alfheim.api.lib.LibShaderIDs;
import alfheim.client.core.handler.CardinalSystemClient;
import alfheim.client.render.entity.RenderWings;
import alfheim.common.core.handler.CardinalSystem.PartySystem.Party;
import alfheim.common.core.util.AlfheimConfig;
import alfheim.common.entity.Flight;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.INpc;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.mana.ICreativeManaProvider;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.common.core.handler.ConfigHandler;

public class GUIParty extends Gui {
	
	private Minecraft mc;
	public static final DecimalFormat format = new DecimalFormat("0.0#");
	
	public GUIParty(Minecraft mc) {
		super();
		this.mc = mc;
	}

	@SubscribeEvent(priority = EventPriority.NORMAL)
	public void onOverlayRendering(RenderGameOverlayEvent.Post event) {
		if (event.type != ElementType.HOTBAR) return;
		GuiIngameForge.renderBossHealth = false;
		EntityPlayer player = mc.thePlayer;
		FontRenderer font = mc.fontRenderer;
		Party pt = CardinalSystemClient.segment().party;
		int color = 0xFFFFFFFF, R = 0xFFDD0000, Y = 0xFFDDDD00, G = 0xFF00DD00;
		String data;
		zLevel = -90;
		double s = AlfheimConfig.partyHUDScale;
		
		glPushMatrix();
		glEnable(GL_BLEND);
		glColor4d(1, 1, 1, 1);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glScaled(s, s, s);
		
		// ################################################################ SELF ################################################################
		
		if (AlfheimConfig.selfHealthUI) {
			/*glPushMatrix();
			glTranslated(8, 8, 0);
			glScaled(3./4, 3./8, 1);
			mc.renderEngine.bindTexture(mc.thePlayer.getLocationSkin());
			zLevel = -89.5F;
			drawTexturedModalRect(0, 0, 32, 64, 32, 64);
			zLevel = -89.0F;
			drawTexturedModalRect(0, 0, 160, 64, 32, 64);
			glPopMatrix();*/
			
			{
				glTranslated(0, -0.5, -89);
				data = (format.format(player.getHealth()) + "/" + format.format(player.getMaxHealth())).replace(',', '.');
				font.drawString(data, 117 - font.getStringWidth(data) / 2, 16, 0x0);
				glTranslated(0, 0.5, 89);
			}
			
			glColor4d(1, 1, 1, 1);
			mc.renderEngine.bindTexture(LibResourceLocations.health);
			drawTexturedModalRect(0, 0, 0, 0, 200, 40);
			ASJUtilities.glColor1u(ASJUtilities.addAlpha(EnumRace.getRace(player).getRGBColor(), 255));
			drawTexturedModalRect(0, 0, 0, 40, 38, 40);
			
			// ################ health: ################
			{
				double mod = ((int) (Math.min(player.getHealth(), player.getMaxHealth()) / Math.max(player.getMaxHealth(), 1) * 158.)) / 158.;
				ASJUtilities.glColor1u(mod > 0.5 ? G : mod > 0.1 ? Y : R);
				int length = MathHelper.floor_double(158 * mod);
	
				if (length <= 10) {
					drawTexturedModalRect(38, 14 + (10 - length), 38, 54 + (10 - length), 1, length);
					drawTexturedModalRect(39, 14, 186 + (11 - length), 54, 10, 10);
				} else {
					drawTexturedModalRect(38, 14, 38, 54, 1, 10);
					drawTexturedModalRect(39 + length - 11, 14, 186, 54, 10, 10);
				}
	
				if (length > 11) drawTexturedModalRect(39, 14, 39, 54, length - 11, 10);
				
				glColor4d(1, 1, 1, 1);
			}
			
			// ################ mana: ################
			mana: {
				int totalMana = 0;
				int totalMaxMana = 0;
				boolean anyRequest = false;
				boolean creative = false;
	
				IInventory mainInv = player.inventory;
				IInventory baublesInv = BotaniaAPI.internalHandler.getBaublesInventory(player);
	
				int invSize = mainInv.getSizeInventory();
				int size = invSize;
				if(baublesInv != null)
					size += baublesInv.getSizeInventory();
	
				for(int i = 0; i < size; i++) {
					boolean useBaubles = i >= invSize;
					IInventory inv = useBaubles ? baublesInv : mainInv;
					ItemStack stack = inv.getStackInSlot(i - (useBaubles ? invSize : 0));
	
					if(stack != null) {
						Item item = stack.getItem();
						if(item instanceof IManaUsingItem)
							anyRequest = anyRequest || ((IManaUsingItem) item).usesMana(stack);
	
						if(item instanceof IManaItem) {
							if(!((IManaItem) item).isNoExport(stack)) {
								totalMana += ((IManaItem) item).getMana(stack);
								totalMaxMana += ((IManaItem) item).getMaxMana(stack);
							}
						}
	
						if(item instanceof ICreativeManaProvider && ((ICreativeManaProvider) item).isCreative(stack))
							creative = true;
					}
				}
				
				Color col = new Color(Color.HSBtoRGB(0.55F, anyRequest ? (float) Math.min(1F, Math.sin(System.currentTimeMillis() / 1000D) * 0.25 + 1F) : 1, 1F));
				glColor4ub((byte) col.getRed(), (byte) col.getGreen(), (byte) col.getBlue(), (byte) 255);
				
				int length = 158;
				
				if(!creative) {
					if(totalMaxMana == 0)
						length = 0;
					else length *= (double) totalMana / (double) totalMaxMana;
				}

				if(length == 0) {
					if(totalMana > 0)
						length = 1;
					else break mana;
				}
				
				if (length <= 10) {
					drawTexturedModalRect(38, 26, 38, 66, 1, length);
					drawTexturedModalRect(39, 26, 186 + (11 - length), 66, 10, 10);
				} else {
					drawTexturedModalRect(38, 26, 38, 66, 1, 10);
					drawTexturedModalRect(39 + length - 11, 26, 186, 66, 10, 10);
				}

				if (length > 11) drawTexturedModalRect(39, 26, 39, 66, length - 11, 10);
			}
			
			// ################ name: ################
			{
				data = mc.thePlayer.getCommandSenderName();
				boolean flag = false;
				while (font.getStringWidth(data) > 82) {
					data = data.substring(0, data.length() - 1);
					flag = true;
				}
				if (flag) data = data.concat("...");
				
				boolean shadow = true;
				
				for (int i = 0; i < data.length(); i++) {
					char c = data.charAt(i);
					if ("\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(c) == -1) {
						shadow = false;
						break;
					}
				}
				
				glTranslated(0, 0, -89);
				font.drawString(data, 88 - (font.getStringWidth(data) / 2), 3, CardinalSystemClient.segment.target == mc.thePlayer ? G : pt.get(0) == mc.thePlayer ? R : 0xFFFFFFFF, shadow);
				glTranslated(0, 0, 89);
			}
		}
		
		// ################################################################ PARTY ################################################################
		
		{
			float hp = -1, hpm = -1;
			int y = 10, col = 0xFFDDDDDD; // bg color
			boolean st = false, shadow = true;
			EntityLivingBase l = null;
			
			// ################ icon: ################
			{
				zLevel = -85;
				glMatrixMode(GL_TEXTURE);
				glPushMatrix();
				glScaled(512./464, 512./464, 1);
				glTranslated(-1./24, -1./24, 0);
				glMatrixMode(GL_MODELVIEW);
				
				if (AlfheimConfig.selfHealthUI){
					mc.getTextureManager().bindTexture(RenderWings.getPlayerIconTexture(player));
					glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
					glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
					glColor4d(1, 1, 1, 0.5);
					Tessellator.instance.startDrawingQuads();
					Tessellator.instance.addVertexWithUV(4, 4, 0, 0, 0);
					Tessellator.instance.addVertexWithUV(4, 36, 0, 0, 1);
					Tessellator.instance.addVertexWithUV(36, 36, 0, 1, 1);
					Tessellator.instance.addVertexWithUV(36, 4, 0, 1, 0);
					Tessellator.instance.draw();
					
					if (ConfigHandler.useShaders) ASJShaderHelper.useShader(LibShaderIDs.idShadow);
					
					double mod = MathHelper.floor_double(Flight.get(mc.thePlayer)) / Flight.max();
					double time = Math.sin(mc.theWorld.getTotalWorldTime() / 2) * 0.5;
					glColor4d(1, 1, 1, (mc.thePlayer.capabilities.isFlying ? (mod > 0.1 ? time + 0.5 : time) : 1));
					
					Tessellator.instance.startDrawingQuads();
					Tessellator.instance.addVertexWithUV(4, 36-(mod*32), 0, 0, 1-mod);
					Tessellator.instance.addVertexWithUV(4, 36, 0, 0, 1);
					Tessellator.instance.addVertexWithUV(36, 36, 0, 1, 1);
					Tessellator.instance.addVertexWithUV(36, 36-(mod*32), 0, 1, 1-mod);
					Tessellator.instance.draw();
				} else {
					if (ConfigHandler.useShaders) ASJShaderHelper.useShader(LibShaderIDs.idShadow);
				}

				glColor4d(1, 1, 1, 1);
				for (int i = 0; i < pt.count; i++) {
					l = pt.get(i);
					if (l == mc.thePlayer) continue;
					
					icon: {
						y += 40;
						if (l == null) break icon;
						
						glPushMatrix();
						glTranslated(4, y, 0);
						mc.getTextureManager().bindTexture(pt.isPlayer(i) ? RenderWings.getPlayerIconTexture((EntityPlayer) l) : l instanceof IBossDisplayData ? LibResourceLocations.icons[LibResourceLocations.BOSS] : l instanceof INpc ? LibResourceLocations.icons[LibResourceLocations.NPC] : LibResourceLocations.icons[LibResourceLocations.MOB]);
						glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
						glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
						Tessellator.instance.startDrawingQuads();
						Tessellator.instance.addVertexWithUV(0, 0, 0, 0, 0);
						Tessellator.instance.addVertexWithUV(0, 28, 0, 0, 1);
						Tessellator.instance.addVertexWithUV(28, 28, 0, 1, 1);
						Tessellator.instance.addVertexWithUV(28, 0, 0, 1, 0);
						Tessellator.instance.draw();
						glPopMatrix();
					}
				}
				
				y = 0;
				
				tg_icon: {
					l = CardinalSystemClient.segment.target;
					if (l == null) break tg_icon;
					if (!AlfheimConfig.targetUI) break tg_icon;
					
					glPushMatrix();
					glTranslated(event.resolution.getScaledWidth() / 2. / s - 116, 11, 0);
					mc.getTextureManager().bindTexture(l instanceof EntityPlayer ? RenderWings.getPlayerIconTexture((EntityPlayer) l) : l instanceof IBossDisplayData ? LibResourceLocations.icons[LibResourceLocations.BOSS] : l instanceof INpc ? LibResourceLocations.icons[LibResourceLocations.NPC] : LibResourceLocations.icons[LibResourceLocations.MOB]);
					glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER);
					glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER);
					Tessellator.instance.startDrawingQuads();
					Tessellator.instance.addVertexWithUV(0, 0, 0, 0, 0);
					Tessellator.instance.addVertexWithUV(0, 28, 0, 0, 1);
					Tessellator.instance.addVertexWithUV(28, 28, 0, 1, 1);
					Tessellator.instance.addVertexWithUV(28, 0, 0, 1, 0);
					Tessellator.instance.draw();
					glPopMatrix();
				}
				
				glMatrixMode(GL_TEXTURE);
				glPopMatrix();
				glMatrixMode(GL_MODELVIEW);
				
				if (ConfigHandler.useShaders) ASJShaderHelper.releaseShader();
			}
			
			// ################ rest: ################
			for (int i = 0; i < pt.count; i++) {
				
				// ################ colors: ################
				{
					l = pt.get(i);
					if (l == mc.thePlayer) continue;
					
					color = i == 0			? R :	// PL
							pt.isPlayer(i) 	? 0xFFFFFFFF :	// Player
											  Y ;	// Mob
					
					if (l == null) {
						color = col = 0xFF888888;					// offline/unloaded
						hp = hpm = -1;
					} else {
						if (l instanceof EntityPlayer) col = EnumRace.getRace((EntityPlayer) l).getRGBColor();
						if (l instanceof INpc) col = color = 0xFF00AAFF;										// NPC
						if (CardinalSystemClient.segment.target == l) color = 0x00FF00;		// selected target
						if (Vector3.entityDistance(player, l) > 32) color = 0xCCCCCC;		// out of reach
						//if (mc.thePlayer.dimension != l.dimension) color = 0x888888;		// other dim
						hp = Math.min(l.getHealth(), l.getMaxHealth());
						hpm = l.getMaxHealth();
					}
					
					if (pt.isDead(i)) {						// dead
						color = 0x444444;
						hp = hpm = 0;
						st = true;
					}
				}

				// ################ hp bg: ################
				mc.renderEngine.bindTexture(LibResourceLocations.health);
				drawTexturedModalRect(0, y += 40, 0, 80, 136, 40);
				// ################ ava bg: ################
				ASJUtilities.glColor1u(ASJUtilities.addAlpha(col, 255));
				drawTexturedModalRect(0, y, 0, 120, 32, 40);
				
				// ################ health: ################
				health: {
					if (pt.isDead(i)) break health;
					
					double mod;
					if (hp != -1 && hpm != -1) {
						mod = ((int) (hp / Math.max(hpm, 1) * 100.)) / 100.;
						ASJUtilities.glColor1u(mod > 0.5 ? G : mod > 0.1 ? Y : R);
					} else {
						mod = 1;
						ASJUtilities.glColor1u(0xFF444444);
					}
					
					int length = MathHelper.floor_double(100 * mod);
		
					if (length < 2) drawTexturedModalRect(34, y + 17 + 2, 133, 137, 1, 4); else
					if (length == 2) {
						drawTexturedModalRect(34, y + 17, 34, 137, 1, 6);
						drawTexturedModalRect(35, y + 17, 132 + (3 - length), 137, 2, 6);
					} else {
						drawTexturedModalRect(34, y + 17, 34, 137, 1, 6);
						drawTexturedModalRect(35 + length - 3, y + 17, 132, 137, 2, 6);
					}
		
					if (length > 3) drawTexturedModalRect(35, y + 17, 35, 137, length - 3, 6);
		
					glColor4d(1, 1, 1, 1);
				}
				
				// ################ mana: ################
				mana: {
					if (l != null && !pt.isPlayer(i)) break mana;
					int length = Math.min(100, pt.getMana(i) / 10000);
					
					if (length <= 0) break mana;
					
					ASJUtilities.glColor1u(0xFF00B2FF);
					
					if (l == null) ASJUtilities.glColor1u(0xFF444444);
					
					if (length < 2) drawTexturedModalRect(34, y + 25, 133, 145, 1, 4);
					
					else if (length == 2) {
						drawTexturedModalRect(34, y + 25, 34, 145, 1, 6);
						drawTexturedModalRect(35, y + 25, 132 + (3 - length), 145, 2, 6);
					} else {
						drawTexturedModalRect(34, y + 25, 34, 145, 1, 6);
						drawTexturedModalRect(35 + length - 3, y + 25, 132, 145, 2, 6);
					}
					
					if (length > 3) drawTexturedModalRect(35, y + 25, 35, 137, length - 3, 6);
						
					if (pt.getMana(i) > 1000000) {
						glTranslated(0.5, 0.5, 0);
						font.drawString(pt.getMana(i) == Integer.MAX_VALUE ? "*" : "+", 128, y + 24, 0x0000FF);
						glTranslated(-0.5, -0.5, 0);
					}
				}
				
				// ################ name: ################
				{
					data = l != null ? l.getCommandSenderName() : pt.getName(i);
					boolean flag = false;
					while (font.getStringWidth(data) > 82) {
						data = data.substring(0, data.length() - 1);
						flag = true;
					}
					if (flag) data = data.concat("...");
					
					for (int j = 0; j < data.length() && shadow; j++) {
						char c = data.charAt(j);
						if ("\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(c) == -1) {
							shadow = false;
						}
					}
					
					if (st) data = EnumChatFormatting.STRIKETHROUGH + data;
					glTranslated(0, 0, -85);
					font.drawString(data, 36, y + 4, color, shadow);
					glTranslated(0, 0, 85);
					
					if (l != null) {
						glTranslated(0, -0.5, -85);
						boolean unicode = font.getUnicodeFlag();
						font.setUnicodeFlag(true);
						data = (format.format(l.getHealth()) + "/" + format.format(l.getMaxHealth())).replace(',', '.');
						font.drawString(data, 84 - font.getStringWidth(data) / 2, y + 16, 0x0);
						font.setUnicodeFlag(unicode);
						glTranslated(0, 0.5, 85);
					}
				}
				
				// ################ debuffs: ################
				debuffs: {
					if (l == null) break debuffs;
					Collection<PotionEffect> pes = l.getActivePotionEffects();
					if (pes.isEmpty()) break debuffs;
					glPushMatrix();
					glTranslated(34, y + 32, 0);
					double s2 = 0.5;
					glScaled(s2, s2, s2);
					glColor4d(1, 1, 1, 1);
					
					int bads = 0;
					for (PotionEffect pe : pes) if(Potion.potionTypes[pe.potionID].isBadEffect) ++bads;
					
					renderPotions(l, bads, true, true);
					
					glPopMatrix();
				}
				
				st = false;
				shadow = true;
				col = 0xFFDDDDDD;
				ASJUtilities.glColor1u(0xFFFFFFFF);
			}
		}
		
		// ################################################################ TARGET ################################################################
		if (AlfheimConfig.targetUI && CardinalSystemClient.segment.target != null) {
			glTranslated(event.resolution.getScaledWidth() / 2. / s - 120, 0, 0);
			zLevel = -80;
			EntityLivingBase l = CardinalSystemClient.segment.target;
			float hp = Math.min(l.getHealth(), l.getMaxHealth());
			float hpm = l.getMaxHealth();
			int col = 0xFFDDDDDD; // bg color
			boolean st = false, shadow = true;
			
			// ################ colors: ################
			{
				color = Y;
				
				if (l instanceof EntityPlayer) {
					color = 0xFFFFFF;
					col = EnumRace.getRace((EntityPlayer) l).getRGBColor();
				}
				if (l instanceof INpc) {
					col = color = 0xFF00AAFF;	
					//shadow = false;
				}
				if (CardinalSystemClient.segment.target instanceof IBossDisplayData) {
					col = color = 0xA2018C;
				}
				color = CardinalSystemClient.segment.isParty ? 0x00FF00 : color;
				if (!l.isEntityAlive()) {
					color = 0x444444;
					hp = hpm = 0;
					st = true;
				}
			}
			
			// ################ hp bg: ################
			mc.renderEngine.bindTexture(LibResourceLocations.health);
			drawTexturedModalRect(0, 0, 0, 160, 240, 50);
			// ################ ava bg: ################
			ASJUtilities.glColor1u(ASJUtilities.addAlpha(col, 255));
			drawTexturedModalRect(0, 2, 0, 210, 34, 48);
			
			// ################ health: ################
			health: {
				if (!l.isEntityAlive()) break health;
				
				double mod = ((int) (hp / Math.max(hpm, 1) * 200.)) / 200.;
				ASJUtilities.glColor1u(mod > 0.5 ? G : mod > 0.1 ? Y : R);
				int length = MathHelper.floor_double(200 * mod);
				
				if (length >= 107) {
					drawTexturedModalRect(34, 2, 34, 210, 100, 48);
					drawTexturedModalRect(134, 2, 134 + (100 - (length - 100)), 210, length - 100, 48);
				} else
				
				if (length >= 14) {
					drawTexturedModalRect(35, 2, 35 + (100 - (length - 7)), 210, length - 7, 48);
					drawTexturedModalRect(34 + length - 7, 2, 227, 210, 7, 48);
					drawTexturedModalRect(34, 2, 34, 210, 1, 30);
				} else 
					
				if (length >= 7) {
					drawTexturedModalRect(35, 2, 35 + (100 - (length - 7)), 210, length - 7, 48);
					drawTexturedModalRect(34 + length - 7, 2, 227, 210, 7, 48);
					drawTexturedModalRect(34, 2, 34, 210, 1, 30 - (14 - length));
				} else 
				
				{
					drawTexturedModalRect(34, 2, 227 + (7 - length), 210, length, 48);
					drawTexturedModalRect(34, 2, 34, 210, 1, 30 - (14 - length));
				}
				
				glColor4d(1, 1, 1, 1);
			}
			
			// ################ name: ################
			{
				data = l.getCommandSenderName();
				boolean flag = false;
				while (font.getStringWidth(data) > 82) {
					data = data.substring(0, data.length() - 1);
					flag = true;
				}
				if (flag) data = data.concat("...");
				
				for (int j = 0; j < data.length() && shadow; j++) {
					char c = data.charAt(j);
					if ("\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(c) == -1) {
						shadow = false;
					}
				}
				
				if (st) data = EnumChatFormatting.STRIKETHROUGH + data;
				
				glTranslated(0, 0, -79);
				font.drawString(data, 36, 6, color, shadow);
				font.drawString(String.format("(%.2fm)", Vector3.entityDistance(player, CardinalSystemClient.segment.target)), 128, 6, color, shadow);
				glTranslated(0, 0, 79);
			}
			
			// ################ potions: ################
			potions: {
				Collection<PotionEffect> pes = l.getActivePotionEffects();
				if (pes.isEmpty()) break potions;
				
				glPushMatrix();
				double s2 = 0.5;
				glScaled(s2, s2, s2);
				glColor4d(1, 1, 1, 1);
				
				int bads = 0, goods = 0;
				for (PotionEffect pe : pes) if(Potion.potionTypes[pe.potionID].isBadEffect) ++bads; else ++goods;
				
				glTranslated(274, 56, 0);
				renderPotions(CardinalSystemClient.segment.target, bads, true, false);
				glTranslated(-198, 22, 0);
				renderPotions(CardinalSystemClient.segment.target, goods, false, false);
				
				glPopMatrix();
			}
			
			st = false;
			shadow = true;
			col = 0xFFDDDDDD;
			ASJUtilities.glColor1u(0xFFFFFFFF);
		}
		
		glDisable(GL_BLEND);
		glPopMatrix();
	}
	
	public void renderPotions(EntityLivingBase e, int count, boolean bads, boolean pt) {
		if (count < 1) return;
		Potion potion;
		double j = 0, k = count > (bads ? (pt ? 10 : 9) : 20) ? (bads ? (pt ? 180. : 162.) : 360. ) / (count - 1) : 18.;
		
		for (Object o : e.getActivePotionEffects()) {
			PotionEffect pe = (PotionEffect) o;
			potion = Potion.potionTypes[pe.getPotionID()];
			if (bads != potion.isBadEffect) continue; 
			
			if (potion.hasStatusIcon()) {
				glDisable(GL_BLEND);
				glColor4d(bads ? 1 : 0, bads ? 0 : 1, 0, 1);
				mc.getTextureManager().bindTexture(LibResourceLocations.widgets);
				drawTexturedModalRect(j, 0, 1, 1, 20, 20);
				glEnable(GL_BLEND);
				glColor4d(1, 1, 1, 1);
				mc.getTextureManager().bindTexture(LibResourceLocations.inventory);
				int l = potion.getStatusIconIndex();
				drawTexturedModalRect(j+1, 1, l % 8 * 18, 198 + l / 8 * 18, 18, 18);
				j += k;
			}
		}
	}
	
	public void drawTexturedModalRect(double x, double y, double u, double v, double width, double height) {
		float f = 0.00390625F;
		float f1 = 0.00390625F;
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.addVertexWithUV(x			, y + height, zLevel,  u			* f	, (v + height)	* f1);
		Tessellator.instance.addVertexWithUV(x + width	, y + height, zLevel, (u + width)	* f	, (v + height)	* f1);
		Tessellator.instance.addVertexWithUV(x + width	, y			, zLevel, (u + width)	* f	,  v			* f1);
		Tessellator.instance.addVertexWithUV(x			, y			, zLevel,  u			* f	,  v			* f1);
		Tessellator.instance.draw();
	}
}