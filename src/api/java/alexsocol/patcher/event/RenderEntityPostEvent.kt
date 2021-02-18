package alexsocol.patcher.event

import cpw.mods.fml.common.eventhandler.*
import net.minecraft.entity.Entity

@Cancelable
class RenderEntityPostEvent(val entity: Entity, val x: Double, val y: Double, val z: Double, val yaw: Float): Event()
