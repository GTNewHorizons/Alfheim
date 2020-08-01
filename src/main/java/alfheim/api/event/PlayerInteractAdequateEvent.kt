package alfheim.api.event

import cpw.mods.fml.common.eventhandler.Event
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer

/**
 * Adequate interaction event
 * Liquid COUNTS as block
 * Entity NOT counts as any type of interactions
 */
abstract class PlayerInteractAdequateEvent(val player: EntityPlayer, val x: Int, val y: Int, val z: Int, val side: Int, val entity: Entity?): Event() {
	
	class LeftClick(player: EntityPlayer, val action: Action, x: Int, y: Int, z: Int, side: Int, entity: Entity?): PlayerInteractAdequateEvent(player, x, y, z, side, entity) {
		
		enum class Action {
			LEFT_CLICK_AIR,
			LEFT_CLICK_BLOCK,
			LEFT_CLICK_ENTIY,
			LEFT_CLICK_LIQUID
		}
	}
	
	class RightClick(player: EntityPlayer, val action: Action, x: Int, y: Int, z: Int, side: Int, entity: Entity?): PlayerInteractAdequateEvent(player, x, y, z, side, entity) {
		
		enum class Action {
			RIGHT_CLICK_AIR,
			RIGHT_CLICK_BLOCK,
			RIGHT_CLICK_ENTIY,
			RIGHT_CLICK_LIQUID
		}
	}
}
