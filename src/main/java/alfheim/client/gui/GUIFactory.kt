package alfheim.client.gui

import cpw.mods.fml.client.IModGuiFactory
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen

class GUIFactory: IModGuiFactory {
	
	override fun initialize(minecraftInstance: Minecraft) {}
	
	override fun mainConfigGuiClass(): Class<out GuiScreen> {
		return GUIConfig::class.java
	}
	
	override fun runtimeGuiCategories(): Set<IModGuiFactory.RuntimeOptionCategoryElement>? {
		return null
	}
	
	override fun getHandlerFor(element: IModGuiFactory.RuntimeOptionCategoryElement): IModGuiFactory.RuntimeOptionGuiHandler? {
		return null
	}
}