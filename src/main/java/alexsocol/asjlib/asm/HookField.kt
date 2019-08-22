package alexsocol.asjlib.asm

// WARNING! Incompatible with kotlin properties! Please, use OBLY java for classes with HookFields
@Target(AnnotationTarget.FIELD)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class HookField(val targetClassName: String)