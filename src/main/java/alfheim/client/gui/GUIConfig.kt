package alfheim.client.gui

import alfheim.api.ModInfo
import alfheim.common.core.handler.AlfheimConfigHandler
import cpw.mods.fml.client.config.GuiConfig
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.common.config.*

class GUIConfig(screen: GuiScreen): GuiConfig(screen, ConfigElement<Any?>(AlfheimConfigHandler.config.getCategory(Configuration.CATEGORY_GENERAL)).childElements, ModInfo.MODID, false, false, getAbridgedConfigPath(AlfheimConfigHandler.config.toString()))