package alfheim.client.gui

import cpw.mods.fml.client.IModGuiFactory
import net.minecraft.client.Minecraft

class GUIFactory: IModGuiFactory {
	
	override fun initialize(minecraftInstance: Minecraft) = Unit
	override fun mainConfigGuiClass() = GUIConfig::class.java
	override fun runtimeGuiCategories() = null
	override fun getHandlerFor(element: IModGuiFactory.RuntimeOptionCategoryElement) = null
}