package alfmod.common.core.asm

import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion
import gloomyfolken.hooklib.minecraft.HookLoader

@MCVersion(value = "1.7.10")
class AlfheimModularHookLoader: HookLoader() {
	
	override fun getASMTransformerClass() = null
	
	override fun registerHooks() {
		registerHookContainer("alfmod.common.core.asm.AlfheimModularHookHandler")
	}
}