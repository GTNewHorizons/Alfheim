package alfheim.api

object ModInfo {
	
	const val MAJOR = "BETA"
	//const val MINOR = ""
	const val BUILD = "pre19.3"
	
	const val MODID = "alfheim"
	const val NAME = "Alfheim"
	const val VERSION = "$MAJOR-$BUILD"
	
	var OBF: Boolean = false
	var DEV = !OBF
}