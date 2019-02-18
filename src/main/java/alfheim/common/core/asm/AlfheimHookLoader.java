package alfheim.common.core.asm;

import alexsocol.asjlib.ASJReflectionHelper;
import alexsocol.asjlib.asm.ASJASM;
import alexsocol.asjlib.asm.ASJPacketCompleter;
import alfheim.api.ModInfo;
import cpw.mods.fml.relauncher.CoreModManager;
import cpw.mods.fml.relauncher.FMLRelaunchLog;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import gloomyfolken.hooklib.minecraft.HookLoader;
import gloomyfolken.hooklib.minecraft.PrimaryClassTransformer;

@MCVersion(value = "1.7.10")
public class AlfheimHookLoader extends HookLoader {
	
	public static boolean isThermos = false;
	
	static {
		isThermos = System.getProperty("alfheim.thermos.suppress", "off").equals("on");
		if (isThermos) FMLRelaunchLog.info(String.format("[%s] Alfheim is configured to disable some features to provide compatibility with Thermos Core", ModInfo.MODID.toUpperCase()));
		ModInfo.OBF = !(Boolean) ASJReflectionHelper.getStaticValue(CoreModManager.class, "deobfuscatedEnvironment");
		ASJReflectionHelper.setStaticFinalValue(ModInfo.class, !ModInfo.OBF, "DEV");
	}
	
	@Override public String[] getASMTransformerClass() {
		return new String[] { PrimaryClassTransformer.class.getName(), AlfheimClassTransformer.class.getName(), ASJPacketCompleter.class.getName(), AlfheimSyntheticMethodsInjector.class.getName(), ASJASM.class.getName() };
	}
	
	@Override public void registerHooks() {
		registerHookContainer("alfheim.common.core.asm.AlfheimHookHandler");
		registerHookContainer("alfheim.common.item.equipment.tool.ItemTwigWandExtender");
		registerHookContainer("alfheim.common.integration.travellersgear.handler.TGHandlerBotaniaAdapter");
		
		ASJASM.registerFieldHookContainer("alfheim.common.core.asm.AlfheimFieldHookHandler");
	}
}