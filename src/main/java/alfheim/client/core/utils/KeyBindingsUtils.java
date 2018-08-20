package alfheim.client.core.utils;

import static alfheim.client.core.utils.KeyBindingsUtils.KeyBindingIDs.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.client.core.proxy.ClientProxy;
import alfheim.common.core.registry.AlfheimItems;
import alfheim.common.core.registry.AlfheimRegistry;
import alfheim.common.core.util.AlfheimConfig;
import alfheim.common.network.KeyBindMessage;
import baubles.api.BaublesApi;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;

public class KeyBindingsUtils {

	public static boolean toggleFlight, toggleLMB, toggleJump, toggleAlt;
	
	public static void parseKeybindings(EntityPlayer player) {
		if (Mouse.isButtonDown(0) && !toggleLMB) {
			toggleLMB = true;
			if (BaublesApi.getBaubles(player).getStackInSlot(0) != null && BaublesApi.getBaubles(player).getStackInSlot(0).getItem() == AlfheimItems.creativeReachPendant)
				AlfheimCore.network.sendToServer(new KeyBindMessage(KeyBindingIDs.ATTACK, false, 0));
		} else if (toggleLMB) {
			toggleLMB = false;
		}
		
		if (AlfheimCore.enableElvenStory) {
			if (ClientProxy.keyFlight.isPressed() && !toggleFlight) {
				toggleFlight = true;
				toggleFlight(player, false);
			} else if (toggleFlight) {
				toggleFlight = false;
			}
			
			if (Minecraft.getMinecraft().gameSettings.keyBindJump.isPressed() && !toggleJump && Keyboard.isKeyDown(Keyboard.KEY_LMENU) && !toggleAlt && !player.capabilities.isFlying && player.onGround) {
				KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindJump.getKeyCode(), false);
				toggleJump = toggleAlt = true;
				toggleFlight(player, true);
				if (player.getAttributeMap().getAttributeInstance(AlfheimRegistry.FLIGHT).getAttributeValue() >= 300) {
					player.motionY += 3;
				}
			} else if (toggleJump && toggleAlt) {
				toggleJump = toggleAlt = false;
			}
		}
	}
	
	public static void toggleFlight(EntityPlayer player, boolean boost) {
		if (!AlfheimConfig.enableWingsNonAlfheim && player.worldObj.provider.dimensionId != AlfheimConfig.dimensionIDAlfheim) {
			ASJUtilities.say(player, "mes.flight.unavailable");
		} else AlfheimCore.network.sendToServer(new KeyBindMessage(boost ? BOOST : FLIGHT, false, 0));
	}
	
	public static enum KeyBindingIDs {
		ATTACK, FLIGHT, BOOST
	}
}