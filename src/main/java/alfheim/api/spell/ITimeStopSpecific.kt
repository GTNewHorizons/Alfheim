package alfheim.api.spell

import java.util.*

/**
 * Used to check whether this entity is affectable by time-stop
 * @see alfheim.common.core.handler.CardinalSystem.TimeStopSystem.affected
 */
interface ITimeStopSpecific {
	
	/**
	 * @return true if this entity is totally immune to time-stop
	 */
	val isImmune: Boolean
	
	/**
	 * Called if [.isImmune] returned `false`
	 * @param uuid UUID of time-stop area's caster
	 * @return true if this entity should be prevented from updates
	 */
	fun affectedBy(uuid: UUID): Boolean
}
