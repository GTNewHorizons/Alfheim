package alfmod.common.core.asm

import alfmod.AlfheimModularCore
import cpw.mods.fml.relauncher.FMLRelaunchLog
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion
import gloomyfolken.hooklib.minecraft.HookLoader

@MCVersion(value = "1.7.10")
class AlfheimModularHookLoader: HookLoader() {
	
	override fun getASMTransformerClass() = null
	
	override fun registerHooks() {
		FMLRelaunchLog.info("[${AlfheimModularCore.MODID.toUpperCase()}] Loaded coremod. Registering hooks...")
		registerHookContainer("alfmod.common.core.asm.AlfheimModularHookHandler")
	}
}