package alfheim.client.event;

import java.lang.reflect.Field;

import alfheim.api.ModInfo;
import alfheim.common.core.util.AlfheimConfig;
import net.minecraft.client.gui.GuiGameOver;

public class ClientOnEvents {

	static void onGameOver(GuiGameOver gui) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		if (ModInfo.DEV) return;
		Field field_146347_a = gui.getClass().getDeclaredField("field_146347_a");
		field_146347_a.setAccessible(true);
		field_146347_a.setInt(gui, -Math.abs(AlfheimConfig.deathScreenAddTime) + 20);
	}
}