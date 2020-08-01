package alfmod.common.core.handler

import java.util.*

private val calendar = Calendar.getInstance()!!
private val year = calendar.get(Calendar.YEAR)
private val month = calendar.get(Calendar.MONTH) + 1

/**
 * February of 2020
 */
val WRATH_OF_THE_WINTER = (year == 2020 && month == 2)

/**
 * August of 2020
 */
val HELLISH_VACATION = (year == 2020 && month == 8)