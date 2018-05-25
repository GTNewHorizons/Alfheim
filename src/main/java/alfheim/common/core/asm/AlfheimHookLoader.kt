package alfheim.common.core.asm

import gloomyfolken.hooklib.minecraft.HookLoader
import gloomyfolken.hooklib.minecraft.PrimaryClassTransformer

class AlfheimHookLoader : HookLoader() {

    override fun getASMTransformerClass(): Array<String> = arrayOf(PrimaryClassTransformer::class.qualifiedName!!, AlfheimClassTransformer::class.qualifiedName!!)

    override fun registerHooks() {
        registerHookContainer("alfheim.common.core.asm.AlfheimHookHandler");
        registerHookContainer("alfheim.common.item.equipment.tools.ItemTwigWandExtender");
    }
}