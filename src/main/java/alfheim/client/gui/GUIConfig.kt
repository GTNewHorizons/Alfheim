package alfheim.client.gui

import alfheim.api.ModInfo
import alfheim.common.core.util.AlfheimConfig
import cpw.mods.fml.client.config.GuiConfig
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.common.config.ConfigElement
import net.minecraftforge.common.config.Configuration

class GUIConfig(screen: GuiScreen): GuiConfig(screen, ConfigElement(AlfheimConfig.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), ModInfo.MODID, false, false, GuiConfig.getAbridgedConfigPath(AlfheimConfig.config.toString()))
