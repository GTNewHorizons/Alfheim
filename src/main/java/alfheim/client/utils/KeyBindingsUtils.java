package alfheim.client.utils;

import alfheim.AlfheimCore;
import alfheim.client.proxy.ClientProxy;
import alfheim.common.network.KeyBindMessage;
import net.minecraft.entity.player.EntityPlayer;

public class KeyBindingsUtils {

	static boolean toggleFlight;
	
	public static void parseKeybindings(EntityPlayer player) {
		if (AlfheimCore.enableElvenStory)
		if (ClientProxy.keyFlight.isPressed() && !toggleFlight) {
			toggleFlight = true;
			AlfheimCore.network.sendToServer(new KeyBindMessage((byte) KeyBindingIDs.FLIGHT.ordinal(), false, 0));
		} else if (toggleFlight) {
			toggleFlight = false;
		}
	}
	
	public static enum KeyBindingIDs {
		FLIGHT
	}
}