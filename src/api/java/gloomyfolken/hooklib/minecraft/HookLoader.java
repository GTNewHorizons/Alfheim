package gloomyfolken.hooklib.minecraft;

import cpw.mods.fml.common.asm.transformers.DeobfuscationTransformer;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import gloomyfolken.hooklib.asm.*;

import java.util.Map;

/**
 * Удобная базовая реализация IFMLLoadingPlugin для использования HookLib.
 * Регистрировать хуки и контейнеры нужно в registerHooks().
 */
public abstract class HookLoader implements IFMLLoadingPlugin {
	
	private static DeobfuscationTransformer deobfuscationTransformer;
	
	private static ClassMetadataReader deobfuscationMetadataReader;
	
	static {
		deobfuscationMetadataReader = new DeobfuscationMetadataReader();
	}
	
	/**
	 * Регистрирует вручную созданный хук
	 */
	public static void registerHook(AsmHook hook) {
		getTransformer().registerHook(hook);
	}
	
	public static HookClassTransformer getTransformer() {
		return PrimaryClassTransformer.instance.registeredSecondTransformer ?
		       MinecraftClassTransformer.instance : PrimaryClassTransformer.instance;
	}
	
	/**
	 * Деобфусцирует класс с хуками и регистрирует хуки из него
	 */
	public static void registerHookContainer(String className) {
		getTransformer().registerHookContainer(className);
	}
	
	public static ClassMetadataReader getDeobfuscationMetadataReader() {
		return deobfuscationMetadataReader;
	}
	
	public static DeobfuscationTransformer getDeobfuscationTransformer() {
		if (HookLibPlugin.getObfuscated() && deobfuscationTransformer == null) {
			deobfuscationTransformer = new DeobfuscationTransformer();
		}
		return deobfuscationTransformer;
	}
	
	// 1.6.x only
	public String[] getLibraryRequestClass() {
		return null;
	}
	
	@Override
	public String[] getASMTransformerClass() {
		return null;
	}
	
	@Override
	public String getModContainerClass() {
		return null;
	}
	
	@Override
	public String getSetupClass() {
		return null;
	}
	
	@Override
	public void injectData(Map<String, Object> data) {
		registerHooks();
	}
	
	// 1.7.x only
	public String getAccessTransformerClass() {
		return null;
	}
	
	protected abstract void registerHooks();
}
