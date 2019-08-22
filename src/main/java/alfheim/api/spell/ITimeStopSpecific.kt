package alfheim.api.spell

import java.util.*

/**
 * Used to check whether this [tile]entity is affectable by time-stop
 * @see alfheim.common.core.handler.CardinalSystem.TimeStopSystem.affected
 * @see alfheim.common.core.handler.CardinalSystem.TimeStopSystem.affected
 */
interface ITimeStopSpecific {
	
	/**
	 * @return true if this [tile]entity is totally immune to time-stop
	 */
	val isImmune: Boolean
	
	/**
	 * Called if [.isImmune] returned `false`
	 * @param uuid UUID of time-stop area
	 * @return true if this [tile]entity **isn't** "in party" with `uuid`
	 */
	fun affectedBy(uuid: UUID): Boolean
}
