package alfheim.client.gui

import alfheim.api.ModInfo
import alfheim.common.core.util.AlfheimConfig
import cpw.mods.fml.client.config.GuiConfig
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.common.config.*

class GUIConfig(screen: GuiScreen): GuiConfig(screen, ConfigElement<Any?>(AlfheimConfig.config.getCategory(Configuration.CATEGORY_GENERAL)).childElements, ModInfo.MODID, false, false, getAbridgedConfigPath(AlfheimConfig.config.toString()))