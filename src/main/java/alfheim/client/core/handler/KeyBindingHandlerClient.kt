package alfheim.client.core.handler

import alexsocol.asjlib.*
import alfheim.AlfheimCore
import alfheim.api.AlfheimAPI
import alfheim.api.entity.*
import alfheim.api.spell.SpellBase
import alfheim.api.spell.SpellBase.SpellCastResult.*
import alfheim.client.core.handler.CardinalSystemClient.PlayerSegmentClient
import alfheim.client.core.handler.CardinalSystemClient.TargetingSystemClient
import alfheim.client.core.handler.CardinalSystemClient.TimeStopSystemClient
import alfheim.client.core.handler.KeyBindingHandlerClient.KeyBindingIDs.*
import alfheim.client.core.proxy.ClientProxy
import alfheim.client.gui.GUISpells
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.helper.flight
import alfheim.common.item.equipment.bauble.ItemCreativeReachPendant
import alfheim.common.network.*
import alfheim.common.network.Message2d.m2d
import baubles.api.BaublesApi
import baubles.common.lib.PlayerHandler
import cpw.mods.fml.relauncher.*
import net.minecraft.client.settings.KeyBinding
import net.minecraft.entity.player.EntityPlayer
import org.lwjgl.input.*

@SideOnly(Side.CLIENT)
object KeyBindingHandlerClient {
	
	/** Toggle Keys  */
	var toggleESMAbility = false
	var toggleCorn = false
	var toggleFlight = false
	var toggleJump = false
	var toggleCast = false
	var toggleUnCast = false
	var toggleSelMob = false
	var toggleSelTeam = false
	var toggleLMB = false
	var toggleAlt = false
	var toggleLeft = false
	var toggleUp = false
	var toggleDown = false
	var toggleRight = false
	var toggleSecret = false
	
	/** IDs for spell selection GUI  */
	var raceID = 1
	var spellID = 0
	
	var prevHotSlot = 1
	
	fun parseKeybindings(player: EntityPlayer) {
		if (mc.currentScreen != null) return
		if (TimeStopSystemClient.affected(player)) return
		
		if (Mouse.isButtonDown(0) && !toggleLMB) {
			toggleLMB = true
			if (PlayerHandler.getPlayerBaubles(player).getStackInSlot(0) != null && BaublesApi.getBaubles(player).getStackInSlot(0).item is ItemCreativeReachPendant)
				AlfheimCore.network.sendToServer(MessageKeyBindS(ATTACK.ordinal, false, 0))
		} else if (toggleLMB) {
			toggleLMB = false
		}
		
		if (safeKeyDown(ClientProxy.keyLolicorn.keyCode)) {
			if (!toggleCorn) {
				toggleCorn = true
				AlfheimCore.network.sendToServer(MessageKeyBindS(CORN.ordinal, false, 0))
			}
		} else if (toggleCorn) {
			toggleCorn = false
		}
		
		if (AlfheimCore.enableElvenStory) {
			if (safeKeyDown(ClientProxy.keyFlight.keyCode)) {
				if (!toggleFlight) {
					toggleFlight = true
					toggleFlight(false)
				}
			} else if (toggleFlight) {
				toggleFlight = false
			}
			
			if (safeKeyDown(ClientProxy.keyESMAbility.keyCode)) {
				if (!toggleESMAbility) {
					toggleESMAbility = true
					if (AlfheimCore.enableElvenStory) {
						PlayerSegmentClient.esmAbility = !PlayerSegmentClient.esmAbility
						ASJUtilities.say(mc.thePlayer, "alfheimmisc.elvenAbility.${PlayerSegmentClient.esmAbility}")
						AlfheimCore.network.sendToServer(MessageKeyBindS(ESMABIL.ordinal, false, 0))
					}
				}
			} else if (toggleESMAbility) {
				toggleESMAbility = false
			}
			
			if (mc.gameSettings.keyBindJump.isPressed && !toggleJump && safeKeyDown(Keyboard.KEY_LMENU) && !toggleAlt && !player.capabilities.isFlying && player.onGround) {
				KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.keyCode, false)
				toggleAlt = true
				toggleJump = toggleAlt
				val boost = player.flight >= 300
				toggleFlight(boost)
				if (boost && mc.thePlayer.race != EnumRace.HUMAN) {
					player.motionY += 3.0
				}
			} else if (toggleJump && toggleAlt) {
				toggleAlt = false
				toggleJump = toggleAlt
			}
		}
		
		if (AlfheimCore.enableMMO) {
			if (prevHotSlot != mc.thePlayer.inventory.currentItem && safeKeyDown(Keyboard.KEY_LCONTROL)) {
				val flag = PlayerSegmentClient.hotSpells.indices.any { safeKeyDown(it + 2) }
				
				if (flag) mc.thePlayer.inventory.currentItem = prevHotSlot
			} else
				prevHotSlot = mc.thePlayer.inventory.currentItem
			
			run {
				if (safeKeyDown(Keyboard.KEY_LCONTROL)) {
					for (i in PlayerSegmentClient.hotSpells.indices) {
						if (safeKeyDown(i + 2)) {
							GUISpells.fadeOut = 5f
							if (safeKeyDown(Keyboard.KEY_LSHIFT)) {
								PlayerSegmentClient.hotSpells[i] = raceID and 0xF shl 28 or (spellID and 0xFFFFFFF)
								AlfheimCore.network.sendToServer(MessageHotSpellS(i, PlayerSegmentClient.hotSpells[i]))
							} else {
								if (PlayerSegmentClient.hotSpells[i] == 0) {
									PlayerSegmentClient.hotSpells[i] = raceID and 0xF shl 28 or (spellID and 0xFFFFFFF)
									AlfheimCore.network.sendToServer(MessageHotSpellS(i, PlayerSegmentClient.hotSpells[i]))
								}
								
								val spell = AlfheimAPI.getSpellByIDs(raceID, spellID)
								if (spell == null)
									PacketHandlerClient.handle(Message2d(m2d.COOLDOWN, 0.0, (-DESYNC.ordinal).D))
								else if (!player.capabilities.isCreativeMode && !SpellBase.consumeMana(player, spell.getManaCost(), false) && !player.isPotionActive(AlfheimConfigHandler.potionIDLeftFlame)) {
									PacketHandlerClient.handle(Message2d(m2d.COOLDOWN, 0.0, (-NOMANA.ordinal).D))
									return@run
								}
								
								AlfheimCore.network.sendToServer(MessageKeyBindS(CAST.ordinal, true, i))
								PlayerSegmentClient.initM = AlfheimAPI.getSpellByIDs(PlayerSegmentClient.hotSpells[i] shr 28 and 0xF, PlayerSegmentClient.hotSpells[i] and 0xFFFFFFF)!!.getCastTime()
								PlayerSegmentClient.init = PlayerSegmentClient.initM
							}
						}
					}
				}
			}
			
			if (safeKeyDown(Keyboard.KEY_UP)) {
				GUISpells.fadeOut = 5f
				if (!toggleUp) {
					toggleUp = true
					raceID = if (++raceID > 9) 1 else raceID
					val size = AlfheimAPI.getSpellsFor(EnumRace[raceID]).size
					spellID = if (size == 0) 0 else spellID % size
				}
			} else if (toggleUp) {
				toggleUp = false
			}
			
			if (safeKeyDown(Keyboard.KEY_DOWN)) {
				GUISpells.fadeOut = 5f
				if (!toggleDown) {
					toggleDown = true
					raceID = if (--raceID < 1) 9 else raceID
					val size = AlfheimAPI.getSpellsFor(EnumRace[raceID]).size
					spellID = if (size == 0) 0 else spellID % size
				}
			} else if (toggleDown) {
				toggleDown = false
			}
			
			if (safeKeyDown(Keyboard.KEY_RIGHT)) {
				GUISpells.fadeOut = 5f
				if (!toggleRight) {
					toggleRight = true
					val size = AlfheimAPI.getSpellsFor(EnumRace[raceID]).size
					spellID = if (size == 0) 0 else ++spellID % size
				}
			} else if (toggleRight) {
				toggleRight = false
			}
			
			if (safeKeyDown(Keyboard.KEY_LEFT)) {
				GUISpells.fadeOut = 5f
				if (!toggleLeft) {
					toggleLeft = true
					val size = AlfheimAPI.getSpellsFor(EnumRace[raceID]).size
					spellID = if (size == 0) 0 else (--spellID + size) % size
				}
			} else if (toggleLeft) {
				toggleLeft = false
			}
			
			run {
				if (safeKeyDown(ClientProxy.keyCast.keyCode)) {
					GUISpells.fadeOut = 5f
					if (!toggleCast) {
						toggleCast = true
						if (PlayerSegmentClient.init <= 0) {
							
							val spell = AlfheimAPI.getSpellByIDs(raceID, spellID)
							if (spell == null)
								PacketHandlerClient.handle(Message2d(m2d.COOLDOWN, 0.0, (-DESYNC.ordinal).D))
							else if (!player.capabilities.isCreativeMode && !SpellBase.consumeMana(player, spell.getManaCost(), false) && !player.isPotionActive(AlfheimConfigHandler.potionIDLeftFlame)) {
								PacketHandlerClient.handle(Message2d(m2d.COOLDOWN, 0.0, (-NOMANA.ordinal).D))
								return@run
							}
							
							val i = raceID and 0xF shl 28 or (spellID and 0xFFFFFFF)
							AlfheimCore.network.sendToServer(MessageKeyBindS(CAST.ordinal, false, i))
							PlayerSegmentClient.initM = AlfheimAPI.getSpellByIDs(raceID, spellID)!!.getCastTime()
							PlayerSegmentClient.init = PlayerSegmentClient.initM
						}
					}
				} else if (toggleCast) {
					toggleCast = false
				}
			}
			
			if (safeKeyDown(ClientProxy.keyUnCast.keyCode)) {
				if (!toggleUnCast) {
					toggleUnCast = true
					AlfheimCore.network.sendToServer(MessageKeyBindS(UNCAST.ordinal, false, 0))
					PlayerSegmentClient.initM = 0
					PlayerSegmentClient.init = PlayerSegmentClient.initM
				}
			} else if (toggleUnCast) {
				toggleUnCast = false
			}
			
			if (safeKeyDown(ClientProxy.keySelMob.keyCode)) {
				if (!toggleSelMob) {
					toggleSelMob = true
					if (TargetingSystemClient.selectMob()) AlfheimCore.network.sendToServer(MessageKeyBindS(SEL.ordinal, PlayerSegmentClient.isParty, PlayerSegmentClient.target?.entityId ?: 0))
				}
			} else if (toggleSelMob) {
				toggleSelMob = false
			}
			
			if (safeKeyDown(ClientProxy.keySelTeam.keyCode)) {
				if (!toggleSelTeam) {
					toggleSelTeam = true
					if (TargetingSystemClient.selectTeam()) AlfheimCore.network.sendToServer(MessageKeyBindS(SEL.ordinal, PlayerSegmentClient.isParty, PlayerSegmentClient.partyIndex))
				}
			} else if (toggleSelTeam) {
				toggleSelTeam = false
			}
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_U)) {
			if (!toggleSecret) {
				toggleSecret = true
				AlfheimCore.network.sendToServer(MessageKeyBindS(SECRET.ordinal, false, 0))
			}
		} else if (toggleSecret) {
			toggleSecret = false
		}
	}
	
	fun safeKeyDown(id: Int) = try { Keyboard.isKeyDown(id) } catch (e: IndexOutOfBoundsException) { false }
	
	fun toggleFlight(boost: Boolean) {
		if (!PlayerSegmentClient.esmAbility) return
		
		mc.thePlayer.sendPlayerAbilities()
		AlfheimCore.network.sendToServer(MessageKeyBindS(FLIGHT.ordinal, boost, 0))
	}
	
	enum class KeyBindingIDs {
		CORN, FLIGHT, ESMABIL, ATTACK, CAST, UNCAST, SEL, SECRET
	}
}