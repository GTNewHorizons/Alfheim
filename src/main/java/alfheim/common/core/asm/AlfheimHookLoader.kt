package alfheim.common.core.asm

import alexsocol.asjlib.ASJReflectionHelper
import alexsocol.asjlib.asm.*
import alfheim.api.ModInfo
import alfheim.common.core.handler.AlfheimConfigHandler
import cpw.mods.fml.relauncher.*
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion
import gloomyfolken.hooklib.minecraft.*
import java.io.File

@MCVersion(value = "1.7.10")
class AlfheimHookLoader: HookLoader() {
	
	init {
		ModInfo.OBF = !ASJReflectionHelper.getStaticValue<CoreModManager, Boolean>(CoreModManager::class.java, "deobfuscatedEnvironment")
		ModInfo.DEV = false
		
		AlfheimConfigHandler.loadConfig(File("config/Alfheim/Alfheim.cfg"))
		AlfheimModularLoader
	}
	
	override fun getASMTransformerClass(): Array<String>? {
		return (arrayOf(AlfheimClassTransformer::class.java.name, ASJPacketCompleter::class.java.name, AlfheimSyntheticMethodsInjector::class.java.name, ASJASM::class.java.name)
				+ if (AlfheimConfigHandler.primaryClassTransformer) arrayOf(PrimaryClassTransformer::class.java.name) else emptyArray())
	}
	
	override fun registerHooks() {
		FMLRelaunchLog.info("[${ModInfo.MODID.toUpperCase()}] Loaded coremod. Registering hooks...")
		
		registerHookContainer("alfheim.common.core.asm.hook.fixes.BotaniaGlowingRenderFixes")
		registerHookContainer("alfheim.client.integration.nei.NEINoBossHook")
		
		registerHookContainer("alfheim.common.core.asm.hook.AlfheimHookHandler")
		if (AlfheimConfigHandler.hpHooks) registerHookContainer("alfheim.common.core.asm.hook.AlfheimHPHooks")
		registerHookContainer("alfheim.common.core.asm.hook.extender.FurnaceExtender")
		registerHookContainer("alfheim.common.core.asm.hook.extender.ItemLensExtender")
		registerHookContainer("alfheim.common.core.asm.hook.extender.ItemTwigWandExtender")
		registerHookContainer("alfheim.common.core.asm.hook.extender.ManaSpreaderExtender")
		registerHookContainer("alfheim.common.core.asm.hook.extender.PureDaisyExtender")
		registerHookContainer("alfheim.common.core.asm.hook.extender.QuartzExtender")
		registerHookContainer("alfheim.common.core.asm.hook.extender.RosaArcanaExtender")
		registerHookContainer("alfheim.common.core.asm.hook.fixes.GodAttributesHooks")
		registerHookContainer("alfheim.common.core.asm.hook.fixes.RecipeAncientWillsFix")
		registerHookContainer("alfheim.common.integration.travellersgear.TGHandlerBotaniaAdapterHooks")
		registerHookContainer("alfheim.common.integration.tinkersconstruct.TraitFairySpawner")
		
		
		// if (ModInfo.OBF) ASJASM.registerFieldHookContainer("alfheim.common.core.asm.AlfheimFieldHookHandler")
	}
}