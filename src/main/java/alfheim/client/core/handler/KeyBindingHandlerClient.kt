package alfheim.client.core.handler

import alexsocol.asjlib.ASJUtilities
import alfheim.AlfheimCore
import alfheim.api.AlfheimAPI
import alfheim.api.entity.*
import alfheim.api.spell.SpellBase
import alfheim.api.spell.SpellBase.SpellCastResult.*
import alfheim.client.core.handler.CardinalSystemClient.PlayerSegmentClient
import alfheim.client.core.handler.CardinalSystemClient.SpellCastingSystemClient
import alfheim.client.core.handler.CardinalSystemClient.TargetingSystemClient
import alfheim.client.core.handler.CardinalSystemClient.TimeStopSystemClient
import alfheim.client.core.handler.KeyBindingHandlerClient.KeyBindingIDs.*
import alfheim.client.core.proxy.ClientProxy
import alfheim.common.core.handler.AlfheimConfigHandler
import alfheim.common.core.helper.flight
import alfheim.common.core.registry.AlfheimRegistry
import alfheim.common.item.equipment.bauble.ItemCreativeReachPendant
import alfheim.common.network.*
import alfheim.common.network.Message2d.m2d
import baubles.api.BaublesApi
import baubles.common.lib.PlayerHandler
import cpw.mods.fml.relauncher.*
import net.minecraft.client.Minecraft
import net.minecraft.client.settings.KeyBinding
import net.minecraft.entity.player.EntityPlayer
import org.lwjgl.input.*

@SideOnly(Side.CLIENT)
object KeyBindingHandlerClient {
	
	/** Toggle Keys  */
	var toggleCorn: Boolean = false
	var toggleFlight: Boolean = false
	var toggleJump: Boolean = false
	var toggleCast: Boolean = false
	var toggleUnCast: Boolean = false
	var toggleSelMob: Boolean = false
	var toggleSelTeam: Boolean = false
	var toggleLMB: Boolean = false
	var toggleAlt: Boolean = false
	var toggleLeft: Boolean = false
	var toggleUp: Boolean = false
	var toggleDown: Boolean = false
	var toggleRight: Boolean = false
	
	/** IDs for spell selection GUI  */
	var raceID = 1
	var spellID = 0
	
	var prevHotSlot = 1
	
	fun parseKeybindings(player: EntityPlayer) {
		if (Minecraft.getMinecraft().currentScreen != null) return
		if (TimeStopSystemClient.affected(player)) return
		
		if (Mouse.isButtonDown(0) && !toggleLMB) {
			toggleLMB = true
			if (PlayerHandler.getPlayerBaubles(player).getStackInSlot(0) != null && BaublesApi.getBaubles(player).getStackInSlot(0).item is ItemCreativeReachPendant)
				AlfheimCore.network.sendToServer(MessageKeyBind(ATTACK.ordinal, false, 0))
		} else if (toggleLMB) {
			toggleLMB = false
		}
		
		if (Keyboard.isKeyDown(ClientProxy.keyLolicorn.keyCode)) {
			if (!toggleCorn) {
				toggleCorn = true
				AlfheimCore.network.sendToServer(MessageKeyBind(CORN.ordinal, false, 0))
			}
		} else if (toggleCorn) {
			toggleCorn = false
		}
		
		if (AlfheimCore.enableElvenStory) {
			if (Keyboard.isKeyDown(ClientProxy.keyFlight.keyCode)) {
				if (!toggleFlight) {
					toggleFlight = true
					toggleFlight(player, false)
				}
			} else if (toggleFlight) {
				toggleFlight = false
			}
			
			if (Minecraft.getMinecraft().gameSettings.keyBindJump.isPressed && !toggleJump && Keyboard.isKeyDown(Keyboard.KEY_LMENU) && !toggleAlt && !player.capabilities.isFlying && player.onGround) {
				KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindJump.keyCode, false)
				toggleAlt = true
				toggleJump = toggleAlt
				val boost = player.flight >= 300
				toggleFlight(player, boost)
				if (boost && Minecraft.getMinecraft().thePlayer.race != EnumRace.HUMAN) {
					player.motionY += 3.0
				}
			} else if (toggleJump && toggleAlt) {
				toggleAlt = false
				toggleJump = toggleAlt
			}
		}
		
		if (AlfheimCore.enableMMO) {
			if (prevHotSlot != Minecraft.getMinecraft().thePlayer.inventory.currentItem && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
				val flag = PlayerSegmentClient.hotSpells.indices.any { Keyboard.isKeyDown(it + 2) }
				
				if (flag) Minecraft.getMinecraft().thePlayer.inventory.currentItem = prevHotSlot
			} else
				prevHotSlot = Minecraft.getMinecraft().thePlayer.inventory.currentItem
			
			run {
				if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
					for (i in PlayerSegmentClient.hotSpells.indices) {
						if (Keyboard.isKeyDown(i + 2)) {
							if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
								PlayerSegmentClient.hotSpells[i] = raceID and 0xF shl 28 or (spellID and 0xFFFFFFF)
								AlfheimCore.network.sendToServer(MessageHotSpellS(i, PlayerSegmentClient.hotSpells[i]))
							} else {
								if (PlayerSegmentClient.hotSpells[i] == 0) {
									PlayerSegmentClient.hotSpells[i] = raceID and 0xF shl 28 or (spellID and 0xFFFFFFF)
									AlfheimCore.network.sendToServer(MessageHotSpellS(i, PlayerSegmentClient.hotSpells[i]))
								}
								
								val spell = AlfheimAPI.getSpellByIDs(raceID, spellID)
								if (spell == null)
									PacketHandlerClient.handle(Message2d(m2d.COOLDOWN, 0.0, (-DESYNC.ordinal).toDouble()))
								else if (!player.capabilities.isCreativeMode && !SpellBase.consumeMana(player, spell.getManaCost(), false) && !player.isPotionActive(AlfheimRegistry.leftFlame)) {
									PacketHandlerClient.handle(Message2d(m2d.COOLDOWN, 0.0, (-NOMANA.ordinal).toDouble()))
									return@run
								}
								
								AlfheimCore.network.sendToServer(MessageKeyBind(CAST.ordinal, true, i))
								PlayerSegmentClient.initM = AlfheimAPI.getSpellByIDs(PlayerSegmentClient.hotSpells[i] shr 28 and 0xF, PlayerSegmentClient.hotSpells[i] and 0xFFFFFFF)!!.getCastTime()
								PlayerSegmentClient.init = PlayerSegmentClient.initM
							}
						}
					}
				}
			}
			
			if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
				if (!toggleUp) {
					toggleUp = true
					raceID = if (++raceID > 9) 1 else raceID
					val size = AlfheimAPI.getSpellsFor(EnumRace[raceID]).size
					spellID = if (size == 0) 0 else spellID % size
				}
			} else if (toggleUp) {
				toggleUp = false
			}
			
			if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
				if (!toggleDown) {
					toggleDown = true
					raceID = if (--raceID < 1) 9 else raceID
					val size = AlfheimAPI.getSpellsFor(EnumRace[raceID]).size
					spellID = if (size == 0) 0 else spellID % size
				}
			} else if (toggleDown) {
				toggleDown = false
			}
			
			if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
				if (!toggleRight) {
					toggleRight = true
					val size = AlfheimAPI.getSpellsFor(EnumRace[raceID]).size
					spellID = if (size == 0) 0 else ++spellID % size
				}
			} else if (toggleRight) {
				toggleRight = false
			}
			
			if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
				if (!toggleLeft) {
					toggleLeft = true
					val size = AlfheimAPI.getSpellsFor(EnumRace[raceID]).size
					spellID = if (size == 0) 0 else (--spellID + size) % size
				}
			} else if (toggleLeft) {
				toggleLeft = false
			}
			
			run {
				if (Keyboard.isKeyDown(ClientProxy.keyCast.keyCode)) {
					if (!toggleCast) {
						toggleCast = true
						if (PlayerSegmentClient.init <= 0) {
							
							val spell = AlfheimAPI.getSpellByIDs(raceID, spellID)
							if (spell == null)
								PacketHandlerClient.handle(Message2d(m2d.COOLDOWN, 0.0, (-DESYNC.ordinal).toDouble()))
							else if (!player.capabilities.isCreativeMode && !SpellBase.consumeMana(player, spell.getManaCost(), false) && !player.isPotionActive(AlfheimRegistry.leftFlame)) {
								PacketHandlerClient.handle(Message2d(m2d.COOLDOWN, 0.0, (-NOMANA.ordinal).toDouble()))
								return@run
							}
							
							val i = raceID and 0xF shl 28 or (spellID and 0xFFFFFFF)
							AlfheimCore.network.sendToServer(MessageKeyBind(CAST.ordinal, false, i))
							PlayerSegmentClient.initM = AlfheimAPI.getSpellByIDs(raceID, spellID)!!.getCastTime()
							PlayerSegmentClient.init = PlayerSegmentClient.initM
						}
					}
				} else if (toggleCast) {
					toggleCast = false
				}
			}
			
			if (Keyboard.isKeyDown(ClientProxy.keyUnCast.keyCode)) {
				if (!toggleUnCast) {
					toggleUnCast = true
					AlfheimCore.network.sendToServer(MessageKeyBind(UNCAST.ordinal, false, 0))
					PlayerSegmentClient.initM = 0
					PlayerSegmentClient.init = PlayerSegmentClient.initM
				}
			} else if (toggleUnCast) {
				toggleUnCast = false
			}
			
			if (Keyboard.isKeyDown(ClientProxy.keySelMob.keyCode)) {
				if (!toggleSelMob) {
					toggleSelMob = true
					if (TargetingSystemClient.selectMob()) AlfheimCore.network.sendToServer(MessageKeyBind(SEL.ordinal, PlayerSegmentClient.isParty, PlayerSegmentClient.target?.entityId ?: 0))
				}
			} else if (toggleSelMob) {
				toggleSelMob = false
			}
			
			if (Keyboard.isKeyDown(ClientProxy.keySelTeam.keyCode)) {
				if (!toggleSelTeam) {
					toggleSelTeam = true
					if (TargetingSystemClient.selectTeam()) AlfheimCore.network.sendToServer(MessageKeyBind(SEL.ordinal, PlayerSegmentClient.isParty, PlayerSegmentClient.target?.entityId ?: 0))
				}
			} else if (toggleSelTeam) {
				toggleSelTeam = false
			}
		}
	}
	
	fun toggleFlight(player: EntityPlayer, boost: Boolean) {
		if (!AlfheimConfigHandler.enableWingsNonAlfheim && player.worldObj.provider.dimensionId != AlfheimConfigHandler.dimensionIDAlfheim) {
			ASJUtilities.say(player, "mes.flight.unavailable")
		} else
			AlfheimCore.network.sendToServer(MessageKeyBind(FLIGHT.ordinal, boost, 0))
	}
	
	enum class KeyBindingIDs {
		CORN, ATTACK, CAST, UNCAST, FLIGHT, SEL
	}
}