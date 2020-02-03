package alfmod.common.core.handler

import alfheim.api.ModInfo
import java.util.*

private val calendar = Calendar.getInstance()!!
private val year = calendar.get(Calendar.YEAR)
private val month = calendar.get(Calendar.MONTH) + 1

/**
 * February of 2020
 */
val WRATH_OF_THE_WINTER = (year == 2020 && month == 2) || ModInfo.DEV