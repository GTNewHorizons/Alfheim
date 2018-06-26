package alfheim.common.core.asm

import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion
import gloomyfolken.hooklib.minecraft.HookLoader
import gloomyfolken.hooklib.minecraft.PrimaryClassTransformer

@MCVersion(value = "1.7.10")
class AlfheimHookLoader : HookLoader() {
	
	override fun getASMTransformerClass(): Array<String> = arrayOf(PrimaryClassTransformer::class.qualifiedName!!, AlfheimClassTransformer::class.qualifiedName!!)

	override fun registerHooks() {
		registerHookContainer("alfheim.common.core.asm.AlfheimHookHandler")
		registerHookContainer("alfheim.common.item.equipment.tool.ItemTwigWandExtender")
		registerHookContainer("alfheim.client.render.entity.RenderWings")
	}
}