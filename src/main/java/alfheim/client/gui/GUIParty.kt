package alfheim.client.gui

import alexsocol.asjlib.math.Vector3
import alexsocol.asjlib.render.*
import alfheim.api.entity.EnumRace
import alfheim.api.lib.*
import alfheim.client.core.handler.CardinalSystemClient
import alfheim.client.render.entity.RenderWings
import alfheim.common.core.helper.ElvenFlightHelper
import alfheim.common.core.util.*
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.Tessellator
import net.minecraft.entity.*
import net.minecraft.entity.boss.IBossDisplayData
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.potion.*
import net.minecraft.util.EnumChatFormatting
import net.minecraftforge.client.GuiIngameForge
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER
import vazkii.botania.api.BotaniaAPI
import vazkii.botania.api.mana.*
import vazkii.botania.common.core.handler.ConfigHandler
import java.awt.Color
import java.text.DecimalFormat
import kotlin.math.*

class GUIParty(private val mc: Minecraft): Gui() {
	
	@SubscribeEvent(receiveCanceled = true)
	fun onOverlayRendering(event: RenderGameOverlayEvent.Post) {
		if (event.type != ElementType.HOTBAR) return
		GuiIngameForge.renderBossHealth = false
		val player = mc.thePlayer
		val font = mc.fontRenderer
		val pt = CardinalSystemClient.segment().party
		var color: Int
		val R = -0x230000
		val Y = -0x222300
		val G = -0xff2300
		var data: String
		zLevel = -90f
		val s = AlfheimConfig.partyHUDScale
		
		glPushMatrix()
		glEnable(GL_BLEND)
		glColor4d(1.0, 1.0, 1.0, 1.0)
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
		glScaled(s, s, s)
		
		// ################################################################ SELF ################################################################
		
		if (AlfheimConfig.selfHealthUI) {
			glPushMatrix()
			/*glPushMatrix();
			glTranslated(8, 8, 0);
			glScaled(3./4, 3./8, 1);
			mc.renderEngine.bindTexture(mc.thePlayer.getLocationSkin());
			zLevel = -89.5F;
			drawTexturedModalRect(0, 0, 32, 64, 32, 64);
			zLevel = -89.0F;
			drawTexturedModalRect(0, 0, 160, 64, 32, 64);
			glPopMatrix();*/
			
			glColor4d(1.0, 1.0, 1.0, 1.0)
			mc.renderEngine.bindTexture(LibResourceLocations.health)
			drawTexturedModalRect(0, 0, 0, 0, 200, 40)
			ASJRenderHelper.glColor1u(ASJRenderHelper.addAlpha(EnumRace.getRace(player).rgbColor, 255))
			drawTexturedModalRect(0, 0, 0, 40, 38, 40)
			
			// ################ health: ################
			run {
				val mod = (min(player.health, player.maxHealth) / max(player.maxHealth, 1f) * 158.0).toInt() / 158.0
				ASJRenderHelper.glColor1u(if (mod > 0.5) G else if (mod > 0.1) Y else R)
				val length = (158 * mod).mfloor()
				
				if (length <= 10) {
					drawTexturedModalRect(38, 14 + (10 - length), 38, 54 + (10 - length), 1, length)
					drawTexturedModalRect(39, 14, 186 + (11 - length), 54, 10, 10)
				} else {
					drawTexturedModalRect(38, 14, 38, 54, 1, 10)
					drawTexturedModalRect(39 + length - 11, 14, 186, 54, 10, 10)
				}
				
				if (length > 11) drawTexturedModalRect(39, 14, 39, 54, length - 11, 10)
				
				glColor4d(1.0, 1.0, 1.0, 1.0)
			}
			
			// ################ mana: ################
			run mana@ {
				var totalMana = 0
				var totalMaxMana = 0
				var anyRequest = false
				var creative = player.capabilities.isCreativeMode
				
				val mainInv = player.inventory
				val baublesInv = BotaniaAPI.internalHandler.getBaublesInventory(player)
				
				val invSize = mainInv.sizeInventory
				var size = invSize
				if (baublesInv != null)
					size += baublesInv.sizeInventory
				
				for (i in 0 until size) {
					val useBaubles = i >= invSize
					val inv = if (useBaubles) baublesInv else mainInv
					val stack = inv.getStackInSlot(i - if (useBaubles) invSize else 0)
					
					if (stack != null) {
						val item = stack.item
						if (item is IManaUsingItem)
							anyRequest = anyRequest || (item as IManaUsingItem).usesMana(stack)
						
						if (creative) continue
						
						if (item is IManaItem) {
							if (!(item as IManaItem).isNoExport(stack)) {
								totalMana += (item as IManaItem).getMana(stack)
								totalMaxMana += (item as IManaItem).getMaxMana(stack)
							}
						}
						
						if (item is ICreativeManaProvider && (item as ICreativeManaProvider).isCreative(stack))
							creative = true
					}
				}
				
				val col = Color(Color.HSBtoRGB(0.55f, if (anyRequest) min(1.0, sin(System.currentTimeMillis() / 1000.0) * 0.25 + 1.0).toFloat() else 1f, 1f))
				glColor4ub(col.red.toByte(), col.green.toByte(), col.blue.toByte(), 255.toByte())
				
				var length = 158
				
				if (!creative) {
					length = if (totalMaxMana == 0)
						0
					else {
						val temp = totalMana.toDouble() / totalMaxMana.toDouble() * length
						temp.toInt()
					}
				}
				
				if (length == 0) {
					if (totalMana > 0)
						length = 1
					else
						return@mana
				}
				
				if (length <= 10) {
					drawTexturedModalRect(38, 26, 38, 66, 1, length)
					drawTexturedModalRect(39, 26, 186 + (11 - length), 66, 10, 10)
				} else {
					drawTexturedModalRect(38, 26, 38, 66, 1, 10)
					drawTexturedModalRect(39 + length - 11, 26, 186, 66, 10, 10)
				}
				
				if (length > 11) drawTexturedModalRect(39, 26, 39, 66, length - 11, 10)
			}
			
			// ################ hp: ################
			run {
				glTranslated(0.0, -0.5, -89.0)
				data = (format.format(player.health.toDouble()) + "/" + format.format(player.maxHealth.toDouble())).replace(',', '.')
				font.drawString(data, 117 - font.getStringWidth(data) / 2, 16, 0x0)
				glTranslated(0.0, 0.5, 89.0)
			}
			
			// ################ name: ################
			run {
				data = mc.thePlayer.commandSenderName
				var flag = false
				while (font.getStringWidth(data) > 82) {
					data = data.substring(0, data.length - 1)
					flag = true
				}
				if (flag) data = "$data..."
				
				var shadow = true
				
				for (i in 0 until data.length) {
					val c = data[i]
					if ("\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(c) == -1) {
						shadow = false
						break
					}
				}
				
				glTranslated(0.0, 0.0, -89.0)
				font.drawString(data, 88 - font.getStringWidth(data) / 2, 3, if (CardinalSystemClient.segment!!.target === mc.thePlayer) G else if (pt[0] === mc.thePlayer) R else -0x1, shadow)
				glTranslated(0.0, 0.0, 89.0)
			}
			glColor4d(1.0, 1.0, 1.0, 1.0)
			glPopMatrix()
		}
		
		// ################################################################ PARTY ################################################################
		var l: EntityLivingBase?
		
		run {
			glPushMatrix()
			var hp: Float
			var hpm: Float
			var y = 10
			var col = -0x222223 // bg color
			var st = false
			var shadow = true
			
			// ################ rest: ################
			for (i in 0 until pt.count) {
				
				// ################ colors: ################
				
				l = pt[i]
				if (l === mc.thePlayer) continue
				
				color = when {
					i == 0			-> R		// PL
					pt.isPlayer(i)	-> -0x1		// Player
					else			-> Y		// Mob
				}
				
				if (l == null) {
					col = -0x777778
					color = col                    // offline/unloaded
					hpm = -1f
					hp = hpm
				} else {
					if (l is EntityPlayer) col = EnumRace.getRace(l as EntityPlayer).rgbColor
					if (l is INpc) {
						color = -0xff5501
						col = color
					}                                        // NPC
					if (CardinalSystemClient.segment!!.target === l) color = 0x00FF00        // selected target
					if (Vector3.entityDistance(player, l!!) > 32) color = 0xCCCCCC        // out of reach
					//if (mc.thePlayer.dimension != l.dimension) color = 0x888888;		// other dim
					hp = min(l!!.health, l!!.maxHealth)
					hpm = l!!.maxHealth
				}
				
				if (pt.isDead(i)) {                        // dead
					color = 0x444444
					hpm = 0f
					hp = hpm
					st = true
				}
				
				// ################ hp bg: ################
				mc.renderEngine.bindTexture(LibResourceLocations.health)
				y += 40
				drawTexturedModalRect(0, y, 0, 80, 136, 40)
				// ################ ava bg: ################
				ASJRenderHelper.glColor1u(ASJRenderHelper.addAlpha(col, 255))
				drawTexturedModalRect(0, y, 0, 120, 32, 40)
				
				// ################ health: ################
				run health@ {
					if (pt.isDead(i)) return@health
					
					val mod: Double
					if (hp != -1f && hpm != -1f) {
						mod = (hp / max(hpm, 1f) * 100.0).toInt() / 100.0
						ASJRenderHelper.glColor1u(if (mod > 0.5) G else if (mod > 0.1) Y else R)
					} else {
						mod = 1.0
						ASJRenderHelper.glColor1u(-0xbbbbbc)
					}
					
					val length = (100 * mod).mfloor()
					
					when {
						length < 2  -> drawTexturedModalRect(34, y + 17 + 2, 133, 137, 1, 4)
						length == 2 -> {
							drawTexturedModalRect(34, y + 17, 34, 137, 1, 6)
							drawTexturedModalRect(35, y + 17, 132 + (3 - length), 137, 2, 6)
						}
						else        -> {
							drawTexturedModalRect(34, y + 17, 34, 137, 1, 6)
							drawTexturedModalRect(35 + length - 3, y + 17, 132, 137, 2, 6)
						}
					}
					
					if (length > 3) drawTexturedModalRect(35, y + 17, 35, 137, length - 3, 6)
					
					glColor4d(1.0, 1.0, 1.0, 1.0)
				}
				
				// ################ mana: ################
				run mana@ {
					if (l != null && !pt.isPlayer(i)) return@mana
					val length = min(100, pt.getMana(i) / 10000)
					
					if (length <= 0) return@mana
					
					ASJRenderHelper.glColor1u(-0xff4d01)
					
					if (l == null) ASJRenderHelper.glColor1u(-0xbbbbbc)
					
					when {
						length < 2  -> drawTexturedModalRect(34, y + 25, 133, 145, 1, 4)
						length == 2 -> {
							drawTexturedModalRect(34, y + 25, 34, 145, 1, 6)
							drawTexturedModalRect(35, y + 25, 132 + (3 - length), 145, 2, 6)
						}
						else        -> {
							drawTexturedModalRect(34, y + 25, 34, 145, 1, 6)
							drawTexturedModalRect(35 + length - 3, y + 25, 132, 145, 2, 6)
						}
					}
					
					if (length > 3) drawTexturedModalRect(35, y + 25, 35, 137, length - 3, 6)
					
					if (pt.getMana(i) > 1000000) {
						glTranslated(0.5, 0.5, 0.0)
						font.drawString(if (pt.getMana(i) == Integer.MAX_VALUE) "*" else "+", 128, y + 24, 0x0000FF)
						glTranslated(-0.5, -0.5, 0.0)
					}
				}
				
				// ################ name: ################
				run {
					data = if (l != null) l!!.commandSenderName else pt.getName(i)
					var flag = false
					while (font.getStringWidth(data) > 82) {
						data = data.substring(0, data.length - 1)
						flag = true
					}
					if (flag) data = "$data..."
					
					var j = 0
					while (j < data.length && shadow) {
						val c = data[j]
						if ("\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(c) == -1) {
							shadow = false
						}
						j++
					}
					
					if (st) data = EnumChatFormatting.STRIKETHROUGH.toString() + data
					glTranslated(0.0, 0.0, -85.0)
					font.drawString(data, 36, y + 4, color, shadow)
					glTranslated(0.0, 0.0, 85.0)
					
					if (l != null) {
						glTranslated(0.0, -0.5, -85.0)
						val unicode = font.unicodeFlag
						font.unicodeFlag = true
						data = (format.format(l!!.health.toDouble()) + "/" + format.format(l!!.maxHealth.toDouble())).replace(',', '.')
						font.drawString(data, 84 - font.getStringWidth(data) / 2, y + 16, 0x0)
						font.unicodeFlag = unicode
						glTranslated(0.0, 0.5, 85.0)
					}
				}
				
				// ################ debuffs: ################
				run debuffs@ {
					if (l == null) return@debuffs
					val pes = l!!.activePotionEffects as Collection<PotionEffect>
					if (pes.isEmpty()) return@debuffs
					glPushMatrix()
					glTranslated(34.0, (y + 32).toDouble(), 0.0)
					val s2 = 0.5
					glScaled(s2, s2, s2)
					glColor4d(1.0, 1.0, 1.0, 1.0)
					
					var bads = 0
					for (pe in pes) if (Potion.potionTypes[pe.potionID].isBadEffect) ++bads
					
					renderPotions(l, bads, true, true)
					
					glPopMatrix()
				}
				
				st = false
				shadow = true
				col = -0x222223
				glColor4d(1.0, 1.0, 1.0, 1.0)
			}
			
			glPopMatrix()
		}
		
		// ################################################################ TARGET ################################################################
		if (AlfheimConfig.targetUI && CardinalSystemClient.segment!!.target != null) {
			glPushMatrix()
			glColor4d(1.0, 1.0, 1.0, 1.0)
			glTranslated(event.resolution.scaledWidth.toDouble() / 2.0 / s - 120, 0.0, 0.0)
			zLevel = -80f
			l = CardinalSystemClient.segment!!.target
			var hp = min(l!!.health, l!!.maxHealth)
			var hpm = l!!.maxHealth
			var col = -0x222223 // bg color
			var st = false
			var shadow = true
			
			// ################ colors: ################
			run {
				color = Y
				
				if (l is EntityPlayer) {
					color = 0xFFFFFF
					col = EnumRace.getRace(l as EntityPlayer).rgbColor
				}
				if (l is INpc) {
					color = -0xff5501
					col = color
					//shadow = false;
				}
				if (CardinalSystemClient.segment!!.target is IBossDisplayData) {
					color = 0xA2018C
					col = color
				}
				color = if (CardinalSystemClient.segment!!.isParty) 0x00FF00 else color
				if (!l!!.isEntityAlive) {
					color = 0x444444
					hpm = 0f
					hp = hpm
					st = true
				}
			}
			
			// ################ hp bg: ################
			mc.renderEngine.bindTexture(LibResourceLocations.health)
			drawTexturedModalRect(0, 0, 0, 160, 240, 50)
			// ################ ava bg: ################
			ASJRenderHelper.glColor1u(ASJRenderHelper.addAlpha(col, 255))
			drawTexturedModalRect(0, 2, 0, 210, 34, 48)
			
			// ################ health: ################
			run health@ {
				if (!l!!.isEntityAlive) return@health
				
				val mod = (hp / max(hpm, 1f) * 200.0).toInt() / 200.0
				ASJRenderHelper.glColor1u(if (mod > 0.5) G else if (mod > 0.1) Y else R)
				val length = (200 * mod).mfloor()
				
				when {
					length >= 107 -> {
						drawTexturedModalRect(34, 2, 34, 210, 100, 48)
						drawTexturedModalRect(134, 2, 134 + (100 - (length - 100)), 210, length - 100, 48)
					}
					length >= 14  -> {
						drawTexturedModalRect(35, 2, 35 + (100 - (length - 7)), 210, length - 7, 48)
						drawTexturedModalRect(34 + length - 7, 2, 227, 210, 7, 48)
						drawTexturedModalRect(34, 2, 34, 210, 1, 30)
					}
					length >= 7   -> {
						drawTexturedModalRect(35, 2, 35 + (100 - (length - 7)), 210, length - 7, 48)
						drawTexturedModalRect(34 + length - 7, 2, 227, 210, 7, 48)
						drawTexturedModalRect(34, 2, 34, 210, 1, 30 - (14 - length))
					}
					else          -> {
						drawTexturedModalRect(34, 2, 227 + (7 - length), 210, length, 48)
						drawTexturedModalRect(34, 2, 34, 210, 1, 30 - (14 - length))
					}
				}
				
				glColor4d(1.0, 1.0, 1.0, 1.0)
			}
			
			// ################ name: ################
			run {
				data = l!!.commandSenderName
				var flag = false
				while (font.getStringWidth(data) > 82) {
					data = data.substring(0, data.length - 1)
					flag = true
				}
				if (flag) data = "$data..."
				
				var j = 0
				while (j < data.length && shadow) {
					val c = data[j]
					if ("\u00c0\u00c1\u00c2\u00c8\u00ca\u00cb\u00cd\u00d3\u00d4\u00d5\u00da\u00df\u00e3\u00f5\u011f\u0130\u0131\u0152\u0153\u015e\u015f\u0174\u0175\u017e\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&\'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00c7\u00fc\u00e9\u00e2\u00e4\u00e0\u00e5\u00e7\u00ea\u00eb\u00e8\u00ef\u00ee\u00ec\u00c4\u00c5\u00c9\u00e6\u00c6\u00f4\u00f6\u00f2\u00fb\u00f9\u00ff\u00d6\u00dc\u00f8\u00a3\u00d8\u00d7\u0192\u00e1\u00ed\u00f3\u00fa\u00f1\u00d1\u00aa\u00ba\u00bf\u00ae\u00ac\u00bd\u00bc\u00a1\u00ab\u00bb\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255d\u255c\u255b\u2510\u2514\u2534\u252c\u251c\u2500\u253c\u255e\u255f\u255a\u2554\u2569\u2566\u2560\u2550\u256c\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256b\u256a\u2518\u250c\u2588\u2584\u258c\u2590\u2580\u03b1\u03b2\u0393\u03c0\u03a3\u03c3\u03bc\u03c4\u03a6\u0398\u03a9\u03b4\u221e\u2205\u2208\u2229\u2261\u00b1\u2265\u2264\u2320\u2321\u00f7\u2248\u00b0\u2219\u00b7\u221a\u207f\u00b2\u25a0\u0000".indexOf(c) == -1) {
						shadow = false
					}
					j++
				}
				
				if (st) data = EnumChatFormatting.STRIKETHROUGH.toString() + data
				
				glTranslated(0.0, 0.0, -79.0)
				font.drawString(data, 36, 6, color, shadow)
				font.drawString(String.format("(%.2fm)", Vector3.entityDistance(player, CardinalSystemClient.segment!!.target!!)), 128, 6, color, shadow)
				glTranslated(0.0, 0.0, 79.0)
			}
			
			// ################ potions: ################
			run potions@{
				val pes = l!!.activePotionEffects as Collection<PotionEffect>
				if (pes.isEmpty()) return@potions
				
				glPushMatrix()
				val s2 = 0.5
				glScaled(s2, s2, s2)
				glColor4d(1.0, 1.0, 1.0, 1.0)
				
				var bads = 0
				var goods = 0
				for (pe in pes) if (Potion.potionTypes[pe.potionID].isBadEffect) ++bads else ++goods
				
				glTranslated(274.0, 56.0, 0.0)
				renderPotions(CardinalSystemClient.segment!!.target, bads, true, false)
				glTranslated(-198.0, 22.0, 0.0)
				renderPotions(CardinalSystemClient.segment!!.target, goods, false, false)
				
				glPopMatrix()
			}
			
			glColor4d(1.0, 1.0, 1.0, 1.0)
			glPopMatrix()
		}
		
		// ################ icon: ################
		run {
			var y = 0
			zLevel = -80f
			glMatrixMode(GL_TEXTURE)
			glPushMatrix()
			glScaled(512.0 / 464, 512.0 / 464, 1.0)
			glTranslated(-1.0 / 24, -1.0 / 24, 0.0)
			glMatrixMode(GL_MODELVIEW)
			
			if (AlfheimConfig.selfHealthUI) {
				mc.textureManager.bindTexture(RenderWings.getPlayerIconTexture(player))
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER)
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER)
				glColor4d(1.0, 1.0, 1.0, 0.5)
				Tessellator.instance.startDrawingQuads()
				Tessellator.instance.addVertexWithUV(4.0, 4.0, 0.0, 0.0, 0.0)
				Tessellator.instance.addVertexWithUV(4.0, 36.0, 0.0, 0.0, 1.0)
				Tessellator.instance.addVertexWithUV(36.0, 36.0, 0.0, 1.0, 1.0)
				Tessellator.instance.addVertexWithUV(36.0, 4.0, 0.0, 1.0, 0.0)
				Tessellator.instance.draw()
				
				if (ConfigHandler.useShaders) ASJShaderHelper.useShader(LibShaderIDs.idShadow)
				
				val mod = ElvenFlightHelper[mc.thePlayer].mfloor() / ElvenFlightHelper.max
				val time = sin((mc.theWorld.totalWorldTime / 2).toDouble()) * 0.5
				glColor4d(1.0, 1.0, 1.0, if (mc.thePlayer.capabilities.isFlying) if (mod > 0.1) time + 0.5 else time else 1.0)
				
				Tessellator.instance.startDrawingQuads()
				Tessellator.instance.addVertexWithUV(4.0, 36 - mod * 32, 0.0, 0.0, 1 - mod)
				Tessellator.instance.addVertexWithUV(4.0, 36.0, 0.0, 0.0, 1.0)
				Tessellator.instance.addVertexWithUV(36.0, 36.0, 0.0, 1.0, 1.0)
				Tessellator.instance.addVertexWithUV(36.0, 36 - mod * 32, 0.0, 1.0, 1 - mod)
				Tessellator.instance.draw()
			} else {
				if (ConfigHandler.useShaders) ASJShaderHelper.useShader(LibShaderIDs.idShadow)
			}
			
			y += 20
			
			glColor4d(1.0, 1.0, 1.0, 1.0)
			for (i in 0 until pt.count) {
				l = pt[i]
				if (l === mc.thePlayer) continue
				
				run icon@{
					y += 40
					if (l == null) return@icon
					
					glPushMatrix()
					glTranslated(4.0, y.toDouble(), 0.0)
					mc.textureManager.bindTexture(if (pt.isPlayer(i)) RenderWings.getPlayerIconTexture((l as EntityPlayer?)!!) else if (l is IBossDisplayData) LibResourceLocations.icons[LibResourceLocations.BOSS] else if (l is INpc) LibResourceLocations.icons[LibResourceLocations.NPC] else LibResourceLocations.icons[LibResourceLocations.MOB])
					glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER)
					glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER)
					Tessellator.instance.startDrawingQuads()
					Tessellator.instance.addVertexWithUV(0.0, 0.0, 0.0, 0.0, 0.0)
					Tessellator.instance.addVertexWithUV(0.0, 28.0, 0.0, 0.0, 1.0)
					Tessellator.instance.addVertexWithUV(28.0, 28.0, 0.0, 1.0, 1.0)
					Tessellator.instance.addVertexWithUV(28.0, 0.0, 0.0, 1.0, 0.0)
					Tessellator.instance.draw()
					glPopMatrix()
				}
			}
			
			run tg_icon@{
				l = CardinalSystemClient.segment().target
				if (l == null) return@tg_icon
				if (!AlfheimConfig.targetUI) return@tg_icon
				
				glPushMatrix()
				glTranslated(event.resolution.scaledWidth.toDouble() / 2.0 / s - 116, 11.0, 0.0)
				mc.textureManager.bindTexture(if (l is EntityPlayer) RenderWings.getPlayerIconTexture((l as EntityPlayer?)!!) else if (l is IBossDisplayData) LibResourceLocations.icons[LibResourceLocations.BOSS] else if (l is INpc) LibResourceLocations.icons[LibResourceLocations.NPC] else LibResourceLocations.icons[LibResourceLocations.MOB])
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_BORDER)
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_BORDER)
				Tessellator.instance.startDrawingQuads()
				Tessellator.instance.addVertexWithUV(0.0, 0.0, 0.0, 0.0, 0.0)
				Tessellator.instance.addVertexWithUV(0.0, 28.0, 0.0, 0.0, 1.0)
				Tessellator.instance.addVertexWithUV(28.0, 28.0, 0.0, 1.0, 1.0)
				Tessellator.instance.addVertexWithUV(28.0, 0.0, 0.0, 1.0, 0.0)
				Tessellator.instance.draw()
				glPopMatrix()
			}
			
			glMatrixMode(GL_TEXTURE)
			glPopMatrix()
			glMatrixMode(GL_MODELVIEW)
			
			if (ConfigHandler.useShaders) ASJShaderHelper.releaseShader()
		}
		
		glDisable(GL_BLEND)
		glPopMatrix()
	}
	
	fun renderPotions(e: EntityLivingBase?, count: Int, bads: Boolean, pt: Boolean) {
		if (count < 1) return
		var potion: Potion
		var j = 0.0
		val k = if (count > (if (bads) if (pt) 10 else 9 else 20)) (if (bads) if (pt) 180.0 else 162.0 else 360.0) / (count - 1) else 18.0
		
		for (o in e!!.activePotionEffects) {
			val pe = o as PotionEffect
			potion = Potion.potionTypes[pe.getPotionID()]
			if (bads != potion.isBadEffect) continue
			
			if (potion.hasStatusIcon()) {
				glDisable(GL_BLEND)
				glColor4d((if (bads) 1 else 0).toDouble(), (if (bads) 0 else 1).toDouble(), 0.0, 1.0)
				mc.textureManager.bindTexture(LibResourceLocations.widgets)
				drawTexturedModalRect(j, 0.0, 1.0, 1.0, 20.0, 20.0)
				glEnable(GL_BLEND)
				glColor4d(1.0, 1.0, 1.0, 1.0)
				mc.textureManager.bindTexture(LibResourceLocations.inventory)
				val l = potion.statusIconIndex
				drawTexturedModalRect(j + 1, 1.0, (l % 8 * 18).toDouble(), (198 + l / 8 * 18).toDouble(), 18.0, 18.0)
				j += k
			}
		}
	}
	
	fun drawTexturedModalRect(x: Double, y: Double, u: Double, v: Double, width: Double, height: Double) {
		val f = 0.00390625f
		val f1 = 0.00390625f
		Tessellator.instance.startDrawingQuads()
		Tessellator.instance.addVertexWithUV(x, y + height, zLevel.toDouble(), u * f, (v + height) * f1)
		Tessellator.instance.addVertexWithUV(x + width, y + height, zLevel.toDouble(), (u + width) * f, (v + height) * f1)
		Tessellator.instance.addVertexWithUV(x + width, y, zLevel.toDouble(), (u + width) * f, v * f1)
		Tessellator.instance.addVertexWithUV(x, y, zLevel.toDouble(), u * f, v * f1)
		Tessellator.instance.draw()
	}
	
	companion object {
		val format = DecimalFormat("0.0#")
	}
}