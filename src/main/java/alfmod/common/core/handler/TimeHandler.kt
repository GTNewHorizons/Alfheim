package alfmod.common.core.handler

import alfheim.api.ModInfo
import java.util.*

/**
 * 24.01.2020 - 24.02.2020
 */
val WRATH_OF_THE_WINTER: Boolean = run {
	val year = Calendar.getInstance().get(Calendar.YEAR)
	val month = Calendar.getInstance().get(Calendar.MONTH) + 1
	val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
	year == 2020 && ((month == 1 && day in 24..31) || (month == 2 && day in 1..24))
	
	ModInfo.DEV
}