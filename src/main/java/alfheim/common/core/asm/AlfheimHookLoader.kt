package alfheim.common.core.asm

import alexsocol.asjlib.ASJReflectionHelper
import alexsocol.asjlib.asm.*
import alfheim.api.ModInfo
import alfheim.common.core.handler.AlfheimConfigHandler
import cpw.mods.fml.relauncher.CoreModManager
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion
import gloomyfolken.hooklib.minecraft.*
import java.io.File

@MCVersion(value = "1.7.10")
class AlfheimHookLoader: HookLoader() {
	
	init {
		AlfheimConfigHandler.loadConfig(File("config/Alfheim/Alfheim.cfg"))
		AlfheimModularLoader
		
		ModInfo.OBF = !(ASJReflectionHelper.getStaticValue<CoreModManager, Any>(CoreModManager::class.java, "deobfuscatedEnvironment") as Boolean)
		ModInfo.DEV = !ModInfo.OBF
	}
	
	override fun getASMTransformerClass(): Array<String>? {
		return (arrayOf(AlfheimClassTransformer::class.java.name, ASJPacketCompleter::class.java.name, AlfheimSyntheticMethodsInjector::class.java.name, ASJASM::class.java.name)
		 + if (AlfheimConfigHandler.primaryClassTransformer) arrayOf(PrimaryClassTransformer::class.java.name) else emptyArray())
	}
	
	override fun registerHooks() {
		registerHookContainer("alfheim.common.core.asm.AlfheimHookHandler")
		if (AlfheimConfigHandler.hpHooks) registerHookContainer("alfheim.common.core.asm.AlfheimHPHooks")
		registerHookContainer("alfheim.common.crafting.recipe.RecipeAncientWillsFix")
		registerHookContainer("alfheim.common.item.equipment.tool.ItemTwigWandExtender")
		registerHookContainer("alfheim.common.integration.travellersgear.TGHandlerBotaniaAdapter")
		
		// if (ModInfo.OBF) ASJASM.registerFieldHookContainer("alfheim.common.core.asm.AlfheimFieldHookHandler")
	}
}