package alfheim.common.utils;

import net.minecraft.entity.player.EntityPlayer;

public class KeyBindingsUtils {
	
	public static void enableFlight(EntityPlayer player) {
		if (player.capabilities.isCreativeMode) return;
		player.capabilities.allowFlying = true;
		player.capabilities.isFlying = !player.capabilities.isFlying;
		player.sendPlayerAbilities();
	}
}
