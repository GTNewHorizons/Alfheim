package alfheim.common.core.asm;

import gloomyfolken.hooklib.minecraft.HookLoader;
import gloomyfolken.hooklib.minecraft.PrimaryClassTransformer;

public class AlfheimHookLoader extends HookLoader {

	@Override
	public String[] getASMTransformerClass() {
		return new String[] { PrimaryClassTransformer.class.getName() };
	}

	@Override
	protected void registerHooks() {
		registerHookContainer("alfheim.common.core.asm.AlfheimHookHandler");
		registerHookContainer("alfheim.common.item.equipment.tools.ItemTwigWandExtender");
	}
}