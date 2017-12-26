package alfheim.client.utils;

import org.lwjgl.input.Mouse;

import alfheim.AlfheimCore;
import alfheim.client.proxy.ClientProxy;
import alfheim.common.network.KeyBindMessage;
import alfheim.common.registry.AlfheimItems;
import baubles.api.BaublesApi;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class KeyBindingsUtils {

	static boolean toggleFlight, toggleLMB;
	
	public static void parseKeybindings(EntityPlayer player) {
		if (Mouse.isButtonDown(0) && !toggleLMB) {
			toggleLMB = true;
			if (BaublesApi.getBaubles(player).getStackInSlot(0) != null && BaublesApi.getBaubles(player).getStackInSlot(0).getItem() == AlfheimItems.creativeReachPendant)
				AlfheimCore.network.sendToServer(new KeyBindMessage((byte) KeyBindingIDs.ATTACK.ordinal(), false, 0));
		} else if (toggleLMB) {
			toggleLMB = false;
		}
		
		if (AlfheimCore.enableElvenStory) {
			if (ClientProxy.keyFlight.isPressed() && !toggleFlight) {
				toggleFlight = true;
				AlfheimCore.network.sendToServer(new KeyBindMessage((byte) KeyBindingIDs.FLIGHT.ordinal(), false, 0));
			} else if (toggleFlight) {
				toggleFlight = false;
			}
		}
	}
	
	public static enum KeyBindingIDs {
		FLIGHT,
		ATTACK
	}
}