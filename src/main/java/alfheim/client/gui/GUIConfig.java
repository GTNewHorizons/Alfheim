package alfheim.client.gui;

import java.util.ArrayList;
import java.util.List;

import alfheim.AlfheimCore;
import alfheim.api.ModInfo;
import alfheim.common.core.utils.AlfheimConfig;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

public class GUIConfig extends GuiConfig {

	public GUIConfig(GuiScreen screen) {
		super(screen, new ConfigElement(AlfheimConfig.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), ModInfo.MODID, false, false, GuiConfig.getAbridgedConfigPath(AlfheimConfig.config.toString()));
	}
}
