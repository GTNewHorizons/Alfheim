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
		return arrayOf(PrimaryClassTransformer::class.java.name, AlfheimClassTransformer::class.java.name, ASJPacketCompleter::class.java.name, AlfheimSyntheticMethodsInjector::class.java.name, ASJASM::class.java.name)
	}
	
	public override fun registerHooks() {
		registerHookContainer("alfheim.common.core.asm.AlfheimHookHandler")
		if (hpSpells) registerHookContainer("alfheim.common.core.asm.AlfheimHPHooks")
		registerHookContainer("alfheim.common.crafting.recipe.RecipeAncientWillsFix")
		registerHookContainer("alfheim.common.item.equipment.tool.ItemTwigWandExtender")
		registerHookContainer("alfheim.common.integration.travellersgear.TGHandlerBotaniaAdapter")
		
		// if (ModInfo.OBF) ASJASM.registerFieldHookContainer("alfheim.common.core.asm.AlfheimFieldHookHandler")
	}
	
	companion object {
		
		var hpSpells = true
		
		init {
			AlfheimASMData.load()
			
			ModInfo.OBF = !(ASJReflectionHelper.getStaticValue<CoreModManager, Any>(CoreModManager::class.java, "deobfuscatedEnvironment") as Boolean)
			//ASJReflectionHelper.setStaticFinalValue(ModInfo::class.java, !ModInfo.OBF, "DEV")
		}
	}
}