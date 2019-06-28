package alfheim.common.core.asm

import alexsocol.asjlib.ASJReflectionHelper
import alexsocol.asjlib.asm.*
import alfheim.api.ModInfo
import cpw.mods.fml.relauncher.CoreModManager
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion
import gloomyfolken.hooklib.minecraft.*

@MCVersion(value = "1.7.10")
class AlfheimHookLoader: HookLoader() {
	
	override fun getASMTransformerClass(): Array<String>? {
		return arrayOf(PrimaryClassTransformer::class.java!!.getName(), AlfheimClassTransformer::class.java!!.getName(), ASJPacketCompleter::class.java!!.getName(), AlfheimSyntheticMethodsInjector::class.java!!.getName(), ASJASM::class.java!!.getName())
	}
	
	public override fun registerHooks() {
		HookLoader.registerHookContainer("alfheim.common.core.asm.AlfheimHookHandler")
		if (hpSpells) HookLoader.registerHookContainer("alfheim.common.core.asm.AlfheimHPHooks")
		HookLoader.registerHookContainer("alfheim.common.item.equipment.tool.ItemTwigWandExtender")
		HookLoader.registerHookContainer("alfheim.common.integration.travellersgear.handler.TGHandlerBotaniaAdapter")
		
		if (ModInfo.OBF) ASJASM.registerFieldHookContainer("alfheim.common.core.asm.AlfheimFieldHookHandler")
	}
	
	companion object {
		
		var hpSpells = true
		var isThermos = false
		
		init {
			AlfheimASMData.load()
			
			ModInfo.OBF = !(ASJReflectionHelper.getStaticValue<CoreModManager, Any>(CoreModManager::class.java, "deobfuscatedEnvironment") as Boolean)
			ASJReflectionHelper.setStaticFinalValue<ModInfo, Boolean>(ModInfo::class.java, !ModInfo.OBF, "DEV")
		}
	}
}