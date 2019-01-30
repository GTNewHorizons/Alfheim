package alfheim.client.core.handler;

import static alfheim.api.spell.SpellBase.SpellCastResult.*;
import static alfheim.client.core.handler.KeyBindingHandlerClient.KeyBindingIDs.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.api.AlfheimAPI;
import alfheim.api.entity.EnumRace;
import alfheim.api.spell.SpellBase;
import alfheim.client.core.handler.CardinalSystemClient.TargetingSystemClient;
import alfheim.client.core.handler.CardinalSystemClient.TimeStopSystemClient;
import alfheim.client.core.proxy.ClientProxy;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.core.util.AlfheimConfig;
import alfheim.common.entity.Flight;
import alfheim.common.item.equipment.bauble.ItemCreativeReachPendant;
import alfheim.common.network.Message2d;
import alfheim.common.network.Message2d.m2d;
import alfheim.common.network.MessageHotSpellS;
import alfheim.common.network.MessageKeyBind;
import baubles.api.BaublesApi;
import baubles.common.lib.PlayerHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;

@SideOnly(Side.CLIENT)
public class KeyBindingHandlerClient {

	/** Toggle Keys */
	public static boolean toggleFlight, toggleLMB, toggleJump, toggleAlt, toggleCast, toggleSelMob, toggleSelTeam,
						  toggleLeft, toggleUp, toggleDown, toggleRight;
	
	/** IDs for spell selection GUI */
	public static int raceID = 1, spellID = 0;
	
	public static int prevHotSlot = 1;
	
	public static void parseKeybindings(EntityPlayer player) {
		if (Minecraft.getMinecraft().currentScreen != null) return;
		if (TimeStopSystemClient.affected(player)) return;
		
		if (Mouse.isButtonDown(0) && !toggleLMB) {
			toggleLMB = true;
			if (PlayerHandler.getPlayerBaubles(player).getStackInSlot(0) != null && BaublesApi.getBaubles(player).getStackInSlot(0).getItem() instanceof ItemCreativeReachPendant)
				AlfheimCore.network.sendToServer(new MessageKeyBind(ATTACK.ordinal(), false, 0));
		} else if (toggleLMB) {
			toggleLMB = false;
		}
		
		if (AlfheimCore.enableElvenStory) {
			if (Keyboard.isKeyDown(ClientProxy.keyFlight.getKeyCode())) {
				if (!toggleFlight) {
					toggleFlight = true;
					toggleFlight(player, false);
				}
			} else if (toggleFlight) {
				toggleFlight = false;
			}
			
			if (Minecraft.getMinecraft().gameSettings.keyBindJump.isPressed() && !toggleJump && Keyboard.isKeyDown(Keyboard.KEY_LMENU) && !toggleAlt && !player.capabilities.isFlying && player.onGround) {
				KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode(), false);
				toggleJump = toggleAlt = true;
				toggleFlight(player, true);
				if (Flight.get(player) >= 300) {
					player.motionY += 3;
				}
			} else if (toggleJump && toggleAlt) {
				toggleJump = toggleAlt = false;
			}
		}
		
		if (AlfheimCore.enableMMO) {
			if (prevHotSlot != Minecraft.getMinecraft().thePlayer.inventory.currentItem && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
				boolean flag = false;
				for (int i = 0; i < CardinalSystemClient.segment().hotSpells.length; i++) {
					if (Keyboard.isKeyDown(i+2)) {
						flag = true;
						break;
					}
				}
				
				if (flag) Minecraft.getMinecraft().thePlayer.inventory.currentItem = prevHotSlot;
			} else prevHotSlot = Minecraft.getMinecraft().thePlayer.inventory.currentItem;
			
			hotcast: if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
				for (int i = 0; i < CardinalSystemClient.segment().hotSpells.length; i++) {
					if (Keyboard.isKeyDown(i+2)) {
						if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
							CardinalSystemClient.segment.hotSpells[i] = ((raceID & 0xF) << 28) | (spellID & 0xFFFFFFF);
							AlfheimCore.network.sendToServer(new MessageHotSpellS(i, CardinalSystemClient.segment.hotSpells[i]));
						} else {
							if (CardinalSystemClient.segment.hotSpells[i] == 0) {
								CardinalSystemClient.segment.hotSpells[i] = ((raceID & 0xF) << 28) | (spellID & 0xFFFFFFF);
								AlfheimCore.network.sendToServer(new MessageHotSpellS(i, CardinalSystemClient.segment.hotSpells[i]));
							}
							
							SpellBase spell = AlfheimAPI.getSpellByIDs(raceID, spellID);
							if (spell == null) PacketHandlerClient.handle(new Message2d(m2d.COOLDOWN, 0, -DESYNC.ordinal()));
							if (!player.capabilities.isCreativeMode && !spell.consumeMana(player, spell.getManaCost(), false) && !player.isPotionActive(AlfheimRegistry.leftFlame)) {
								PacketHandlerClient.handle(new Message2d(m2d.COOLDOWN, 0, -NOMANA.ordinal()));
								break hotcast;
							}
							
							AlfheimCore.network.sendToServer(new MessageKeyBind(CAST.ordinal(), true, i));
							CardinalSystemClient.segment.init = CardinalSystemClient.segment.initM = AlfheimAPI.getSpellByIDs((CardinalSystemClient.segment.hotSpells[i] >> 28) & 0xF, CardinalSystemClient.segment.hotSpells[i] & 0xFFFFFFF).getCastTime();
						}
					}
				}
			}
			
			if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
				if (!toggleUp) {
					toggleUp = true;
					raceID = ++raceID > 9 ? 1 : raceID;
					int size = AlfheimAPI.getSpellsFor(EnumRace.getByID(raceID)).size();
					spellID = size == 0 ? 0 : spellID % size;
				}
			} else if (toggleUp) {
				toggleUp = false;
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
				if (!toggleDown) {
					toggleDown = true;
					raceID = --raceID < 1 ? 9 : raceID;
					int size = AlfheimAPI.getSpellsFor(EnumRace.getByID(raceID)).size();
					spellID = size == 0 ? 0 : spellID % size;
				}
			} else if (toggleDown) {
				toggleDown = false;
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
				if (!toggleRight) {
					toggleRight = true;
					int size = AlfheimAPI.getSpellsFor(EnumRace.getByID(raceID)).size();
					spellID = size == 0 ? 0 : (++spellID) % size;
				}
			} else if (toggleRight) {
				toggleRight = false;
			}

			if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
				if (!toggleLeft) {
					toggleLeft = true;
					int size = AlfheimAPI.getSpellsFor(EnumRace.getByID(raceID)).size();
					spellID = size == 0 ? 0 : (--spellID + size) % size;
				}
			} else if (toggleLeft) {
				toggleLeft = false;
			}
			
			cast: if (Keyboard.isKeyDown(ClientProxy.keyCast.getKeyCode()) && !toggleCast) {
				toggleCast = true;
				if (CardinalSystemClient.segment().init <= 0) {
					
					SpellBase spell = AlfheimAPI.getSpellByIDs(raceID, spellID);
					if (spell == null) PacketHandlerClient.handle(new Message2d(m2d.COOLDOWN, 0, -DESYNC.ordinal()));
					if (!player.capabilities.isCreativeMode && !spell.consumeMana(player, spell.getManaCost(), false) && !player.isPotionActive(AlfheimRegistry.leftFlame)) {
						PacketHandlerClient.handle(new Message2d(m2d.COOLDOWN, 0, -NOMANA.ordinal()));
						break cast;
					}
					
					int i = ((raceID & 0xF) << 28) | (spellID & 0xFFFFFFF);
					AlfheimCore.network.sendToServer(new MessageKeyBind(CAST.ordinal(), false, i));
					CardinalSystemClient.segment.init = CardinalSystemClient.segment.initM = AlfheimAPI.getSpellByIDs(raceID, spellID).getCastTime();
				}
			} else if (toggleCast) {
				toggleCast = false;
			}
			
			if (ClientProxy.keySelMob.isPressed()) {
				if (!toggleSelMob) {
					toggleSelMob = true;
					if (TargetingSystemClient.selectMob()) AlfheimCore.network.sendToServer(new MessageKeyBind(SEL.ordinal(), CardinalSystemClient.segment().isParty, CardinalSystemClient.segment.target.getEntityId()));
				}
			} else if (toggleSelMob) {
				toggleSelMob = false;
			}
			
			if (ClientProxy.keySelTeam.isPressed()) {
				if (!toggleSelTeam) {
					toggleSelTeam = true;
					if (TargetingSystemClient.selectTeam()) AlfheimCore.network.sendToServer(new MessageKeyBind(SEL.ordinal(), CardinalSystemClient.segment().isParty, CardinalSystemClient.segment.target.getEntityId()));
				}
			} else if (toggleSelTeam) {
				toggleSelTeam = false;
			}
		}
	}
	
	public static void toggleFlight(EntityPlayer player, boolean boost) {
		if (!AlfheimConfig.enableWingsNonAlfheim && player.worldObj.provider.dimensionId != AlfheimConfig.dimensionIDAlfheim) {
			ASJUtilities.say(player, "mes.flight.unavailable");
		} else AlfheimCore.network.sendToServer(new MessageKeyBind(FLIGHT.ordinal(), boost, 0));
	}
	
	public static enum KeyBindingIDs {
		ATTACK, CAST, FLIGHT, SEL
	}
}