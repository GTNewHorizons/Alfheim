package alexsocol.asjlib.asm

@Target(AnnotationTarget.FIELD)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class HookField(val targetClassName: String)