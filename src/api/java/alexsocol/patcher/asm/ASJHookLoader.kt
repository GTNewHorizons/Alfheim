package alexsocol.patcher.asm

import alexsocol.asjlib.ASJReflectionHelper
import alexsocol.asjlib.asm.*
import cpw.mods.fml.relauncher.*
import gloomyfolken.hooklib.minecraft.*

@IFMLLoadingPlugin.MCVersion(value = "1.7.10")
class ASJHookLoader: HookLoader() {
	
	override fun getASMTransformerClass(): Array<String>? {
		return arrayOf(PrimaryClassTransformer::class.java.name, ASJASM::class.java.name, ASJClassTransformer::class.java.name, ASJPacketCompleter::class.java.name, ASJSyntheticMethodsInjector::class.java.name)
	}
	
	override fun registerHooks() {
		FMLRelaunchLog.info("[ASJLib] Loaded coremod. Registering hooks...")
		
		registerHookContainer("alexsocol.patcher.asm.ASJHookHandler")
	}
	
	companion object {
		val maxParticles = 40000 // TODO to config
		val clearWater = true // TODO to config
		val voidFog = true // TODO to config
		
		val OBF = !ASJReflectionHelper.getStaticValue<CoreModManager, Boolean>(CoreModManager::class.java, "deobfuscatedEnvironment")
	}
}