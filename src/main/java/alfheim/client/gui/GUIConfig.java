package alfheim.client.gui;

import alfheim.AlfheimCore;
import alfheim.Constants;
import cpw.mods.fml.client.config.GuiConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

public class GUIConfig extends GuiConfig {

	public GUIConfig(GuiScreen screen) {
		super(screen, new ConfigElement(AlfheimCore.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), Constants.MODID, false, false, GuiConfig.getAbridgedConfigPath(AlfheimCore.config.toString()));
	}

}
