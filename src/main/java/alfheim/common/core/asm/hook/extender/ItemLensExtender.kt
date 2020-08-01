package alfheim.common.core.asm.hook.extender

import alfheim.common.item.lens.*
import gloomyfolken.hooklib.asm.Hook
import vazkii.botania.common.item.lens.ItemLens

object ItemLensExtender {
	
	const val PROP_NONE = 0
	const val PROP_POWER = 1
	const val PROP_ORIENTATION = 2
	const val PROP_TOUCH = 4
	const val PROP_INTERACTION = 8
	const val PROP_DAMAGE = 16
	const val PROP_CONTROL = 32
	
	const val MESSANGER = 22
	const val TRIPWIRE = 23
	
	const val PUSH = 24
	const val SMELT = 25
	const val SUPERCONDUCTOR = 26
	const val TRACK = 27
	
	@JvmStatic
	@Hook(injectOnExit = true, isMandatory = true, targetMethod = "<clinit>")
	fun `ItemLens$clinit`(lens: ItemLens?) {
		// Botania
		ItemLens.setProps(MESSANGER, PROP_POWER)
		ItemLens.setProps(TRIPWIRE, PROP_CONTROL)
		// ExtraBotany
		ItemLens.setProps(PUSH, PROP_NONE)
		ItemLens.setProps(SMELT, PROP_NONE)
		ItemLens.setProps(SUPERCONDUCTOR, PROP_NONE)
		ItemLens.setProps(TRACK, PROP_CONTROL)
		
		// Botania
		ItemLens.setLens(MESSANGER, LensMessanger())
		ItemLens.setLens(TRIPWIRE, LensTripwire())
		// ExtraBotany
		ItemLens.setLens(PUSH, LensPush())
		ItemLens.setLens(SMELT, LensSmelt())
		ItemLens.setLens(SUPERCONDUCTOR, LensSuperconductor())
		ItemLens.setLens(TRACK, LensTrack())
	}
}