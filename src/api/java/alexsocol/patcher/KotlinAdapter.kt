package alexsocol.patcher

import alexsocol.asjlib.ASJReflectionHelper
import cpw.mods.fml.common.*
import cpw.mods.fml.relauncher.Side
import java.lang.reflect.*

/**
 * Kotlin Language Adapter
 * Allows to make [Mod]-annotated objects
 */
class KotlinAdapter: ILanguageAdapter {
	
	override fun getNewInstance(container: FMLModContainer?, modClass: Class<*>, classLoader: ClassLoader?, instanceFactory: Method?): Any {
		if (instanceFactory != null) return instanceFactory.invoke(null)
		
		ASJReflectionHelper.getField(modClass, "INSTANCE")?.apply {
			ASJReflectionHelper.getValue<Any>(this, null as Any?)?.apply { // cast because of 'Overload resolution ambiguity'
				return this
			}
		}
		
		return modClass.newInstance()
	}
	
	override fun supportsStatics() = true
	
	override fun setProxy(proxyField: Field?, modClass: Class<*>?, proxyObject: Any?) {
		ASJReflectionHelper.setValue(proxyField, null, proxyObject)
	}
	
	override fun setInternalProxies(container: ModContainer, side: Side, loader: ClassLoader) {
		val modClass = container.mod::class.java
		
		modClass.declaredFields.forEach { field ->
			val annotation = field.getAnnotation(KotlinProxy::class.java) ?: return@forEach
			val targetType = if (side.isClient) annotation.clientSide else annotation.serverSide
			val proxyInstance = getNewInstance(null, Class.forName(targetType, true, loader), loader, null)
			setProxy(field, modClass, proxyInstance)
		}
	}
	
	companion object {
		
		const val className = "alexsocol.patcher.KotlinAdapter"
	}
}

/**
 * Kotlin specific proxy annotation
 * Allows proxies to be objects
 * Works only in [Mod]-annotated classes/objects
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class KotlinProxy(
		/**
		 * The name of the client side class to load and populate
		 */
		val clientSide: String = "",
		/**
		 * The name of the server side class to load and populate
		 */
		val serverSide: String = "",
                            )