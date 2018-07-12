package alfheim.common.core.asm;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import gloomyfolken.hooklib.minecraft.HookLoader;
import gloomyfolken.hooklib.minecraft.PrimaryClassTransformer;

@MCVersion(value = "1.7.10")
public class AlfheimHookLoader extends HookLoader {
	
	@Override public String[] getASMTransformerClass() {
		return new String[] { PrimaryClassTransformer.class.getName(), /*ASJASM.class.getName(),*/ AlfheimClassTransformer.class.getName() };
	}

	@Override public void registerHooks() {
		registerHookContainer("alfheim.common.core.asm.AlfheimHookHandler");
		registerHookContainer("alfheim.common.item.equipment.tool.ItemTwigWandExtender");
		registerHookContainer("alfheim.client.render.entity.RenderWings");
	}
}