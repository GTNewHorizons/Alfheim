package alfheim.client.core.utils;

import org.lwjgl.input.Mouse;

import alexsocol.asjlib.ASJUtilities;
import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.client.core.proxy.ClientProxy;
import alfheim.common.core.registry.AlfheimItems;
import alfheim.common.core.utils.AlfheimConfig;
import alfheim.common.network.KeyBindMessage;
import baubles.api.BaublesApi;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StatCollector;

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
				if (!AlfheimConfig.enableWingsNonAlfheim && player.worldObj.provider.dimensionId != AlfheimConfig.dimensionIDAlfheim) {
					ASJUtilities.say(player, "mes.flight.unavailable");
				} else AlfheimCore.network.sendToServer(new KeyBindMessage((byte) KeyBindingIDs.FLIGHT.ordinal(), false, 0));
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